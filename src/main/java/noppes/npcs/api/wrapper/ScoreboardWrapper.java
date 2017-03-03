
package noppes.npcs.api.wrapper;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IScoreboard;
import noppes.npcs.api.IScoreboardObjective;
import noppes.npcs.api.IScoreboardTeam;

public class ScoreboardWrapper implements IScoreboard {
	private Scoreboard board;

	protected ScoreboardWrapper() {
		board = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
	}

	@Override
	public IScoreboardObjective addObjective(String objective, String criteria) {
		getObjectiveWithException(objective);
		IScoreObjectiveCriteria icriteria = IScoreObjectiveCriteria.INSTANCES.get(criteria);
		if (icriteria == null) {
			throw new CustomNPCsException("Unknown score criteria: %s", new Object[] { criteria });
		}
		if ((objective.length() <= 0) || (objective.length() > 16)) {
			throw new CustomNPCsException("Score objective must be between 1-16 characters: %s",
					new Object[] { objective });
		}
		ScoreObjective obj = board.addScoreObjective(objective, icriteria);
		return new ScoreboardObjectiveWrapper(obj);
	}

	@Override
	public IScoreboardTeam addTeam(String name) {
		if (hasTeam(name)) {
			return null;
		}
		return new ScoreboardTeamWrapper(board.createTeam(name), board);
	}

	@Override
	public void deletePlayerScore(String player, String objective, String datatag) {
		ScoreObjective objec = getObjectiveWithException(objective);
		if (!test(datatag)) {
			return;
		}
		if (board.getObjectivesForEntity(player).remove(objec) != null) {
			board.func_96516_a(player);
		}
	}

	@Override
	public IScoreboardObjective getObjective(String name) {
		ScoreObjective obj = board.getObjective(name);
		if (obj == null) {
			return null;
		}
		return new ScoreboardObjectiveWrapper(obj);
	}

	@Override
	public IScoreboardObjective[] getObjectives() {
		List<ScoreObjective> collection = new ArrayList<>(board.getScoreObjectives());
		IScoreboardObjective[] objectives = new IScoreboardObjective[collection.size()];
		for (int i = 0; i < collection.size(); ++i) {
			objectives[i] = new ScoreboardObjectiveWrapper(collection.get(i));
		}
		return objectives;
	}

	private ScoreObjective getObjectiveWithException(String objective) {
		ScoreObjective objec = board.getObjective(objective);
		if (objec == null) {
			throw new CustomNPCsException("Score objective does not exist: %s", new Object[] { objective });
		}
		return objec;
	}

	@Override
	public int getPlayerScore(String player, String objective, String datatag) {
		ScoreObjective objec = getObjectiveWithException(objective);
		if (objec.getCriteria().isReadOnly() || !test(datatag)) {
			return 0;
		}
		return board.getValueFromObjective(player, objec).getScorePoints();
	}

	@Override
	public IScoreboardTeam getTeam(String name) {
		ScorePlayerTeam team = board.getPlayersTeam(name);
		if (team == null) {
			return null;
		}
		return new ScoreboardTeamWrapper(team, board);
	}

	@Override
	public IScoreboardTeam[] getTeams() {
		List<ScorePlayerTeam> list = new ArrayList<>(board.getTeams());
		IScoreboardTeam[] teams = new IScoreboardTeam[list.size()];
		for (int i = 0; i < list.size(); ++i) {
			teams[i] = new ScoreboardTeamWrapper(list.get(i), board);
		}
		return teams;
	}

	@Override
	public boolean hasObjective(String objective) {
		return board.getObjective(objective) != null;
	}

	@Override
	public boolean hasPlayerObjective(String player, String objective, String datatag) {
		ScoreObjective objec = getObjectiveWithException(objective);
		return test(datatag) && (board.getObjectivesForEntity(player).get(objec) != null);
	}

	@Override
	public boolean hasTeam(String name) {
		return board.getPlayersTeam(name) != null;
	}

	@Override
	public void removeObjective(String objective) {
		ScoreObjective obj = board.getObjective(objective);
		if (obj != null) {
			board.removeObjective(obj);
		}
	}

	@Override
	public void removeTeam(String name) {
		ScorePlayerTeam team = board.getPlayersTeam(name);
		if (team != null) {
			board.removeTeam(team);
		}
	}

	@Override
	public void setPlayerScore(String player, String objective, int score, String datatag) {
		ScoreObjective objec = getObjectiveWithException(objective);
		if (objec.getCriteria().isReadOnly() || (score < Integer.MIN_VALUE) || (score > Integer.MAX_VALUE)
				|| !test(datatag)) {
			return;
		}
		Score sco = board.getValueFromObjective(player, objec);
		sco.setScorePoints(score);
	}

	private boolean test(String datatag) {
		if (datatag.isEmpty()) {
			return true;
		}
		try {
			Entity entity = CommandBase.getEntity(MinecraftServer.getServer(), datatag);
			NBTTagCompound nbttagcompound = JsonToNBT.getTagFromJson(datatag);
			NBTTagCompound nbttagcompound2 = new NBTTagCompound();
			entity.writeToNBT(nbttagcompound2);
			return NBTUtil.func_181123_a(nbttagcompound, nbttagcompound2, true);
		} catch (Exception e) {
			return false;
		}
	}
}

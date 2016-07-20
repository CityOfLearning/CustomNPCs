//

//

package noppes.npcs.api.wrapper;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IScoreboardTeam;

public class ScoreboardTeamWrapper implements IScoreboardTeam {
	private ScorePlayerTeam team;
	private Scoreboard board;

	protected ScoreboardTeamWrapper(ScorePlayerTeam team, Scoreboard board) {
		this.team = team;
		this.board = board;
	}

	@Override
	public void addPlayer(String player) {
		board.addPlayerToTeam(player, getName());
	}

	@Override
	public void clearPlayers() {
		List<String> list = new ArrayList<String>(team.getMembershipCollection());
		for (String player : list) {
			board.removePlayerFromTeam(player, team);
		}
	}

	@Override
	public String getColor() {
		String prefix = team.getColorPrefix();
		if ((prefix == null) || prefix.isEmpty()) {
			return null;
		}
		for (EnumChatFormatting format : EnumChatFormatting.values()) {
			if (prefix.equals(format.toString()) && (format != EnumChatFormatting.RESET)) {
				return format.getFriendlyName();
			}
		}
		return null;
	}

	@Override
	public String getDisplayName() {
		return team.getTeamName();
	}

	@Override
	public boolean getFriendlyFire() {
		return team.getAllowFriendlyFire();
	}

	@Override
	public String getName() {
		return team.getRegisteredName();
	}

	@Override
	public String[] getPlayers() {
		List<String> list = new ArrayList<String>(team.getMembershipCollection());
		return list.toArray(new String[list.size()]);
	}

	@Override
	public boolean getSeeInvisibleTeamPlayers() {
		return team.getSeeFriendlyInvisiblesEnabled();
	}

	@Override
	public void removePlayer(String player) {
		board.removePlayerFromTeam(player, team);
	}

	@Override
	public void setColor(String color) {
		EnumChatFormatting enumchatformatting = EnumChatFormatting.getValueByName(color);
		if ((enumchatformatting == null) || enumchatformatting.isFancyStyling()) {
			throw new CustomNPCsException("Not a proper color name: %s", new Object[] { color });
		}
		team.setNamePrefix(enumchatformatting.toString());
		team.setNameSuffix(EnumChatFormatting.RESET.toString());
	}

	@Override
	public void setDisplayName(String name) {
		if ((name.length() <= 0) || (name.length() > 32)) {
			throw new CustomNPCsException("Score team display name must be between 1-32 characters: %s",
					new Object[] { name });
		}
		team.setTeamName(name);
	}

	@Override
	public void setFriendlyFire(boolean bo) {
		team.setAllowFriendlyFire(bo);
	}

	@Override
	public void setSeeInvisibleTeamPlayers(boolean bo) {
		team.setSeeFriendlyInvisiblesEnabled(bo);
	}
}

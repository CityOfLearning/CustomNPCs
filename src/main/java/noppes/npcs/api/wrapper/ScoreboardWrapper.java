package noppes.npcs.api.wrapper;

import java.util.ArrayList;
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
import noppes.npcs.api.wrapper.ScoreboardObjectiveWrapper;
import noppes.npcs.api.wrapper.ScoreboardTeamWrapper;

public class ScoreboardWrapper implements IScoreboard {

   private Scoreboard board = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();


   public IScoreboardObjective[] getObjectives() {
      ArrayList collection = new ArrayList(this.board.getScoreObjectives());
      IScoreboardObjective[] objectives = new IScoreboardObjective[collection.size()];

      for(int i = 0; i < collection.size(); ++i) {
         objectives[i] = new ScoreboardObjectiveWrapper((ScoreObjective)collection.get(i));
      }

      return objectives;
   }

   public IScoreboardObjective getObjective(String name) {
      ScoreObjective obj = this.board.getObjective(name);
      return obj == null?null:new ScoreboardObjectiveWrapper(obj);
   }

   public boolean hasObjective(String objective) {
      return this.board.getObjective(objective) != null;
   }

   public void removeObjective(String objective) {
      ScoreObjective obj = this.board.getObjective(objective);
      if(obj != null) {
         this.board.removeObjective(obj);
      }

   }

   public IScoreboardObjective addObjective(String objective, String criteria) {
      this.getObjectiveWithException(objective);
      IScoreObjectiveCriteria icriteria = (IScoreObjectiveCriteria)IScoreObjectiveCriteria.INSTANCES.get(criteria);
      if(icriteria == null) {
         throw new CustomNPCsException("Unknown score criteria: %s", new Object[]{criteria});
      } else if(objective.length() > 0 && objective.length() <= 16) {
         ScoreObjective obj = this.board.addScoreObjective(objective, icriteria);
         return new ScoreboardObjectiveWrapper(obj);
      } else {
         throw new CustomNPCsException("Score objective must be between 1-16 characters: %s", new Object[]{objective});
      }
   }

   public void setPlayerScore(String player, String objective, int score, String datatag) {
      ScoreObjective objec = this.getObjectiveWithException(objective);
      if(!objec.getCriteria().isReadOnly() && score >= Integer.MIN_VALUE && score <= Integer.MAX_VALUE && this.test(datatag)) {
         Score sco = this.board.getValueFromObjective(player, objec);
         sco.setScorePoints(score);
      }
   }

   private boolean test(String datatag) {
      if(datatag.isEmpty()) {
         return true;
      } else {
         try {
            Entity e = CommandBase.getEntity(MinecraftServer.getServer(), datatag);
            NBTTagCompound nbttagcompound = JsonToNBT.getTagFromJson(datatag);
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            e.writeToNBT(nbttagcompound1);
            return NBTUtil.func_181123_a(nbttagcompound, nbttagcompound1, true);
         } catch (Exception var5) {
            return false;
         }
      }
   }

   private ScoreObjective getObjectiveWithException(String objective) {
      ScoreObjective objec = this.board.getObjective(objective);
      if(objec == null) {
         throw new CustomNPCsException("Score objective does not exist: %s", new Object[]{objective});
      } else {
         return objec;
      }
   }

   public int getPlayerScore(String player, String objective, String datatag) {
      ScoreObjective objec = this.getObjectiveWithException(objective);
      return !objec.getCriteria().isReadOnly() && this.test(datatag)?this.board.getValueFromObjective(player, objec).getScorePoints():0;
   }

   public boolean hasPlayerObjective(String player, String objective, String datatag) {
      ScoreObjective objec = this.getObjectiveWithException(objective);
      return !this.test(datatag)?false:this.board.getObjectivesForEntity(player).get(objec) != null;
   }

   public void deletePlayerScore(String player, String objective, String datatag) {
      ScoreObjective objec = this.getObjectiveWithException(objective);
      if(this.test(datatag)) {
         if(this.board.getObjectivesForEntity(player).remove(objec) != null) {
            this.board.func_96516_a(player);
         }

      }
   }

   public IScoreboardTeam[] getTeams() {
      ArrayList list = new ArrayList(this.board.getTeams());
      IScoreboardTeam[] teams = new IScoreboardTeam[list.size()];

      for(int i = 0; i < list.size(); ++i) {
         teams[i] = new ScoreboardTeamWrapper((ScorePlayerTeam)list.get(i), this.board);
      }

      return teams;
   }

   public boolean hasTeam(String name) {
      return this.board.getPlayersTeam(name) != null;
   }

   public IScoreboardTeam addTeam(String name) {
      return this.hasTeam(name)?null:new ScoreboardTeamWrapper(this.board.createTeam(name), this.board);
   }

   public IScoreboardTeam getTeam(String name) {
      ScorePlayerTeam team = this.board.getPlayersTeam(name);
      return team == null?null:new ScoreboardTeamWrapper(team, this.board);
   }

   public void removeTeam(String name) {
      ScorePlayerTeam team = this.board.getPlayersTeam(name);
      if(team != null) {
         this.board.removeTeam(team);
      }

   }
}

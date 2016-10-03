package noppes.npcs.api.wrapper;

import net.minecraft.scoreboard.ScoreObjective;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IScoreboardObjective;

public class ScoreboardObjectiveWrapper implements IScoreboardObjective {

   private ScoreObjective objective;


   protected ScoreboardObjectiveWrapper(ScoreObjective objective) {
      this.objective = objective;
   }

   public String getName() {
      return this.objective.getName();
   }

   public String getDisplayName() {
      return this.objective.getDisplayName();
   }

   public void setDisplayName(String name) {
      if(name.length() > 0 && name.length() <= 32) {
         this.objective.setDisplayName(name);
      } else {
         throw new CustomNPCsException("Score objective display name must be between 1-32 characters: %s", new Object[]{name});
      }
   }

   public String getCriteria() {
      return this.objective.getCriteria().getName();
   }

   public boolean isReadyOnly() {
      return this.objective.getCriteria().isReadOnly();
   }
}

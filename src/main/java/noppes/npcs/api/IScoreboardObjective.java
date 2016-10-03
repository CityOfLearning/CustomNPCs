package noppes.npcs.api;


public interface IScoreboardObjective {

   String getName();

   String getDisplayName();

   void setDisplayName(String var1);

   String getCriteria();

   boolean isReadyOnly();
}

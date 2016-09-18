
package noppes.npcs.api;

public interface IScoreboard {
	IScoreboardObjective addObjective(String p0, String p1);

	IScoreboardTeam addTeam(String p0);

	void deletePlayerScore(String p0, String p1, String p2);

	IScoreboardObjective getObjective(String p0);

	IScoreboardObjective[] getObjectives();

	int getPlayerScore(String p0, String p1, String p2);

	IScoreboardTeam getTeam(String p0);

	IScoreboardTeam[] getTeams();

	boolean hasObjective(String p0);

	boolean hasPlayerObjective(String p0, String p1, String p2);

	boolean hasTeam(String p0);

	void removeObjective(String p0);

	void removeTeam(String p0);

	void setPlayerScore(String p0, String p1, int p2, String p3);
}

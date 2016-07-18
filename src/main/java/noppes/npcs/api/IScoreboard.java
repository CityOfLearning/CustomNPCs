//

//

package noppes.npcs.api;

public interface IScoreboard {
	IScoreboardObjective addObjective(final String p0, final String p1);

	IScoreboardTeam addTeam(final String p0);

	void deletePlayerScore(final String p0, final String p1, final String p2);

	IScoreboardObjective getObjective(final String p0);

	IScoreboardObjective[] getObjectives();

	int getPlayerScore(final String p0, final String p1, final String p2);

	IScoreboardTeam getTeam(final String p0);

	IScoreboardTeam[] getTeams();

	boolean hasObjective(final String p0);

	boolean hasPlayerObjective(final String p0, final String p1, final String p2);

	boolean hasTeam(final String p0);

	void removeObjective(final String p0);

	void removeTeam(final String p0);

	void setPlayerScore(final String p0, final String p1, final int p2, final String p3);
}

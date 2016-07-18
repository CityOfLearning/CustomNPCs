//

//

package noppes.npcs.api;

public interface IScoreboardTeam {
	void addPlayer(final String p0);

	void clearPlayers();

	String getColor();

	String getDisplayName();

	boolean getFriendlyFire();

	String getName();

	String[] getPlayers();

	boolean getSeeInvisibleTeamPlayers();

	void removePlayer(final String p0);

	void setColor(final String p0);

	void setDisplayName(final String p0);

	void setFriendlyFire(final boolean p0);

	void setSeeInvisibleTeamPlayers(final boolean p0);
}

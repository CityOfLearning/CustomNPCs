//

//

package noppes.npcs.api;

public interface IScoreboardTeam {
	void addPlayer(String p0);

	void clearPlayers();

	String getColor();

	String getDisplayName();

	boolean getFriendlyFire();

	String getName();

	String[] getPlayers();

	boolean getSeeInvisibleTeamPlayers();

	void removePlayer(String p0);

	void setColor(String p0);

	void setDisplayName(String p0);

	void setFriendlyFire(boolean p0);

	void setSeeInvisibleTeamPlayers(boolean p0);
}

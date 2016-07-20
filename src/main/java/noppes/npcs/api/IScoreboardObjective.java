//

//

package noppes.npcs.api;

public interface IScoreboardObjective {
	String getCriteria();

	String getDisplayName();

	String getName();

	boolean isReadyOnly();

	void setDisplayName(String p0);
}

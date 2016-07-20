//

//

package noppes.npcs.api.entity.data.role;

import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.entity.data.INPCRole;

public interface IRoleFollower extends INPCRole {
	void addDays(int p0);

	int getDays();

	IPlayer getFollowing();

	boolean getGuiDisabled();

	boolean getInfinite();

	boolean isFollowing();

	void setFollowing(IPlayer p0);

	void setGuiDisabled(boolean p0);

	void setInfinite(boolean p0);
}

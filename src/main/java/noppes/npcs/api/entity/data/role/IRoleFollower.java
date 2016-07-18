//

//

package noppes.npcs.api.entity.data.role;

import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.entity.data.INPCRole;

public interface IRoleFollower extends INPCRole {
	void addDays(final int p0);

	int getDays();

	IPlayer getFollowing();

	boolean getGuiDisabled();

	boolean getInfinite();

	boolean isFollowing();

	void setFollowing(final IPlayer p0);

	void setGuiDisabled(final boolean p0);

	void setInfinite(final boolean p0);
}

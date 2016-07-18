//

//

package noppes.npcs.api.entity.data.role;

import noppes.npcs.api.IItemStack;
import noppes.npcs.api.entity.data.INPCRole;

public interface IRoleTrader extends INPCRole {
	IItemStack getCurrency1(final int p0);

	IItemStack getCurrency2(final int p0);

	String getMarket();

	IItemStack getSold(final int p0);

	void remove(final int p0);

	void set(final int p0, final IItemStack p1, final IItemStack p2, final IItemStack p3);

	void setMarket(final String p0);
}

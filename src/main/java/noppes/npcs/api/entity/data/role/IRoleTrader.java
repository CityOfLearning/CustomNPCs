
package noppes.npcs.api.entity.data.role;

import noppes.npcs.api.IItemStack;
import noppes.npcs.api.entity.data.INPCRole;

public interface IRoleTrader extends INPCRole {
	IItemStack getCurrency1(int p0);

	IItemStack getCurrency2(int p0);

	String getMarket();

	IItemStack getSold(int p0);

	void remove(int p0);

	void set(int p0, IItemStack p1, IItemStack p2, IItemStack p3);

	void setMarket(String p0);
}

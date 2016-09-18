
package noppes.npcs.api.entity.data;

import noppes.npcs.api.IItemStack;

public interface INPCInventory {
	IItemStack getArmor(int p0);

	IItemStack getDropItem(int p0);

	int getExpMax();

	int getExpMin();

	int getExpRNG();

	IItemStack getLeftHand();

	IItemStack getProjectile();

	IItemStack getRightHand();

	void setArmor(int p0, IItemStack p1);

	void setDropItem(int p0, IItemStack p1, int p2);

	void setExp(int p0, int p1);

	void setLeftHand(IItemStack p0);

	void setProjectile(IItemStack p0);

	void setRightHand(IItemStack p0);
}

//

//

package noppes.npcs.api.entity.data;

import noppes.npcs.api.IItemStack;

public interface INPCInventory {
	IItemStack getArmor(final int p0);

	IItemStack getDropItem(final int p0);

	int getExpMax();

	int getExpMin();

	int getExpRNG();

	IItemStack getLeftHand();

	IItemStack getProjectile();

	IItemStack getRightHand();

	void setArmor(final int p0, final IItemStack p1);

	void setDropItem(final int p0, final IItemStack p1, final int p2);

	void setExp(final int p0, final int p1);

	void setLeftHand(final IItemStack p0);

	void setProjectile(final IItemStack p0);

	void setRightHand(final IItemStack p0);
}


package noppes.npcs.api;

import net.minecraft.item.ItemStack;
import noppes.npcs.api.entity.IEntityLiving;

public interface IItemStack {
	void damageItem(int p0, IEntityLiving p1);

	String getDisplayName();

	int getItemDamage();

	String getItemName();

	ItemStack getMCItemStack();

	String getName();

	int getStackSize();

	Object getTag(String p0);

	boolean hasCustomName();

	boolean hasEnchant(int p0);

	boolean hasTag(String p0);

	boolean isBlock();

	boolean isEnchanted();

	void setCustomName(String p0);

	void setItemDamage(int p0);

	void setStackSize(int p0);

	void setTag(String p0, Object p1);
}

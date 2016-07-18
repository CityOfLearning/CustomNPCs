//

//

package noppes.npcs.api;

import net.minecraft.item.ItemStack;
import noppes.npcs.api.entity.IEntityLiving;

public interface IItemStack {
	void damageItem(final int p0, final IEntityLiving p1);

	String getDisplayName();

	int getItemDamage();

	String getItemName();

	ItemStack getMCItemStack();

	String getName();

	int getStackSize();

	Object getTag(final String p0);

	boolean hasCustomName();

	boolean hasEnchant(final int p0);

	boolean hasTag(final String p0);

	boolean isBlock();

	boolean isEnchanted();

	void setCustomName(final String p0);

	void setItemDamage(final int p0);

	void setStackSize(final int p0);

	void setTag(final String p0, final Object p1);
}

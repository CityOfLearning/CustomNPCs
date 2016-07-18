//

//

package noppes.npcs.api.handler.data;

import net.minecraft.item.ItemStack;

public interface IRecipe {
	void delete();

	int getHeight();

	int getId();

	boolean getIgnoreDamage();

	boolean getIgnoreNBT();

	String getName();

	ItemStack[] getRecipe();

	ItemStack getResult();

	int getWidth();

	boolean isGlobal();

	void save();

	boolean saves();

	void saves(final boolean p0);

	void setIgnoreDamage(final boolean p0);

	void setIgnoreNBT(final boolean p0);

	void setIsGlobal(final boolean p0);
}


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

	void saves(boolean p0);

	void setIgnoreDamage(boolean p0);

	void setIgnoreNBT(boolean p0);

	void setIsGlobal(boolean p0);
}

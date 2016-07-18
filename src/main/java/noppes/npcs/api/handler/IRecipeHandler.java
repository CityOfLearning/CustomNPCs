//

//

package noppes.npcs.api.handler;

import java.util.List;

import net.minecraft.item.ItemStack;
import noppes.npcs.api.handler.data.IRecipe;

public interface IRecipeHandler {
	IRecipe addRecipe(final String p0, final boolean p1, final ItemStack p2, final int p3, final int p4,
			final ItemStack... p5);

	IRecipe addRecipe(final String p0, final boolean p1, final ItemStack p2, final Object... p3);

	IRecipe delete(final int p0);

	List<IRecipe> getCarpentryList();

	List<IRecipe> getGlobalList();
}

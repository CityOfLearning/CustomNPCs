//

//

package noppes.npcs.api.handler;

import java.util.List;

import net.minecraft.item.ItemStack;
import noppes.npcs.api.handler.data.IRecipe;

public interface IRecipeHandler {
	IRecipe addRecipe(String p0, boolean p1, ItemStack p2, int p3, int p4, ItemStack... p5);

	IRecipe addRecipe(String p0, boolean p1, ItemStack p2, Object... p3);

	IRecipe delete(int p0);

	List<IRecipe> getCarpentryList();

	List<IRecipe> getGlobalList();
}

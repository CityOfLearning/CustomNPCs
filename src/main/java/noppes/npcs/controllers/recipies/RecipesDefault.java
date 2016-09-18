
package noppes.npcs.controllers.recipies;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import noppes.npcs.CustomItems;

public class RecipesDefault {
	public static void addRecipe(String name, Object ob, boolean isGlobal, Object... recipe) {
		ItemStack item;
		if (ob instanceof Item) {
			item = new ItemStack((Item) ob);
		} else if (ob instanceof Block) {
			item = new ItemStack((Block) ob);
		} else {
			item = (ItemStack) ob;
		}
		RecipeCarpentry recipeAnvil = new RecipeCarpentry(name);
		recipeAnvil.isGlobal = isGlobal;
		recipeAnvil = RecipeCarpentry.createRecipe(recipeAnvil, item, recipe);
		try {
			RecipeController.instance.saveRecipe(recipeAnvil);
		} catch (IOException ex) {
		}
	}

	public static void loadDefaultRecipes(int i) {
		addRecipe("Npc Wand", CustomItems.wand, true, "XX", " Y", " Y", 'X', Items.bread, 'Y', Items.stick);
		addRecipe("Mob Cloner", CustomItems.cloner, true, "XX", "XY", " Y", 'X', Items.bread, 'Y', Items.stick);
		addRecipe("Carpentry Bench", CustomItems.carpentyBench, true, "XYX", "Z Z", "Z Z", 'X', Blocks.planks, 'Z',
				Items.stick, 'Y', Blocks.crafting_table);
	}
}

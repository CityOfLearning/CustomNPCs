//

//

package noppes.npcs.controllers;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;

public class RecipesDefault {
	public static void addRecipe(final String name, final Object ob, final boolean isGlobal, final Object... recipe) {
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

	public static void loadDefaultRecipes(final int i) {
		if (i < 0) {
			addRecipe("Npc Wand", CustomItems.wand, true, "XX", " Y", " Y", 'X', Items.bread, 'Y', Items.stick);
			addRecipe("Mob Cloner", CustomItems.cloner, true, "XX", "XY", " Y", 'X', Items.bread, 'Y', Items.stick);
			addRecipe("Carpentry Bench", CustomItems.carpentyBench, true, "XYX", "Z Z", "Z Z", 'X', Blocks.planks, 'Z',
					Items.stick, 'Y', Blocks.crafting_table);
		}
		if (i < 1) {
			addRecipe("WallBanner Wooden", new ItemStack(CustomItems.wallBanner, 1, 0), false, "XXX", "ZZZ", "ZZZ",
					"Z Z", 'Z', Blocks.wool, 'X', Blocks.planks);
			addRecipe("WallBanner Stone", new ItemStack(CustomItems.wallBanner, 1, 1), false, "XXX", "ZZZ", "ZZZ",
					"Z Z", 'Z', Blocks.wool, 'X', Blocks.cobblestone);
			addRecipe("WallBanner Iron", new ItemStack(CustomItems.wallBanner, 1, 2), false, "XXX", "ZZZ", "ZZZ", "Z Z",
					'Z', Blocks.wool, 'X', Items.iron_ingot);
			addRecipe("WallBanner Gold", new ItemStack(CustomItems.wallBanner, 1, 3), false, "XXX", "ZZZ", "ZZZ", "Z Z",
					'Z', Blocks.wool, 'X', Items.gold_ingot);
			addRecipe("WallBanner Diamond", new ItemStack(CustomItems.wallBanner, 1, 4), false, "XXX", "ZZZ", "ZZZ",
					"Z Z", 'Z', Blocks.wool, 'X', Items.diamond);
			addRecipe("Banner Wooden", new ItemStack(CustomItems.banner, 1, 0), false, " X ", " Z ", " Z ", "ZZZ", 'X',
					new ItemStack(CustomItems.wallBanner, 1, 0), 'Z', Blocks.planks);
			addRecipe("Banner Stone", new ItemStack(CustomItems.banner, 1, 1), false, " X ", " Z ", " Z ", "ZZZ", 'X',
					new ItemStack(CustomItems.wallBanner, 1, 1), 'Z', Blocks.cobblestone);
			addRecipe("Banner Iron", new ItemStack(CustomItems.banner, 1, 2), false, " X ", " Z ", " Z ", "ZZZ", 'X',
					new ItemStack(CustomItems.wallBanner, 1, 2), 'Z', Items.iron_ingot);
			addRecipe("Banner Gold", new ItemStack(CustomItems.banner, 1, 3), false, " X ", " Z ", " Z ", "ZZZ", 'X',
					new ItemStack(CustomItems.wallBanner, 1, 3), 'Z', Items.gold_ingot);
			addRecipe("Banner Diamond", new ItemStack(CustomItems.banner, 1, 4), false, " X ", " Z ", " Z ", "ZZZ", 'X',
					new ItemStack(CustomItems.wallBanner, 1, 4), 'Z', Items.diamond);
			addRecipe("Lamp Wooden", new ItemStack(CustomItems.tallLamp, 1, 0), false, "YXY", " Z ", " Z ", "ZZZ", 'X',
					Blocks.torch, 'Y', Blocks.wool, 'Z', Blocks.planks);
			addRecipe("Lamp Stone", new ItemStack(CustomItems.tallLamp, 1, 1), false, "YXY", " Z ", " Z ", "ZZZ", 'X',
					Blocks.torch, 'Y', Blocks.wool, 'Z', Blocks.cobblestone);
			addRecipe("Lamp Iron", new ItemStack(CustomItems.tallLamp, 1, 2), false, "YXY", " Z ", " Z ", "ZZZ", 'X',
					Blocks.torch, 'Y', Blocks.wool, 'Z', Items.iron_ingot);
			addRecipe("Lamp Gold", new ItemStack(CustomItems.tallLamp, 1, 3), false, "YXY", " Z ", " Z ", "ZZZ", 'X',
					Blocks.torch, 'Y', Blocks.wool, 'Z', Items.gold_ingot);
			addRecipe("Lamp Diamond", new ItemStack(CustomItems.tallLamp, 1, 4), false, "YXY", " Z ", " Z ", "ZZZ", 'X',
					Blocks.torch, 'Y', Blocks.wool, 'Z', Items.diamond);
			addRecipe("Chair Wooden1", new ItemStack(CustomItems.chair, 1, 0), false, "  X", "  X", "XXX", "X X", 'X',
					new ItemStack(Blocks.planks, 1, 0));
			addRecipe("Chair Wooden2", new ItemStack(CustomItems.chair, 1, 1), false, "  X", "  X", "XXX", "X X", 'X',
					new ItemStack(Blocks.planks, 1, 1));
			addRecipe("Chair Wooden3", new ItemStack(CustomItems.chair, 1, 2), false, "  X", "  X", "XXX", "X X", 'X',
					new ItemStack(Blocks.planks, 1, 2));
			addRecipe("Chair Wooden4", new ItemStack(CustomItems.chair, 1, 3), false, "  X", "  X", "XXX", "X X", 'X',
					new ItemStack(Blocks.planks, 1, 3));
			addRecipe("Chair Wooden5", new ItemStack(CustomItems.chair, 1, 4), false, "  X", "  X", "XXX", "X X", 'X',
					new ItemStack(Blocks.planks, 1, 4));
			addRecipe("Chair Wooden6", new ItemStack(CustomItems.chair, 1, 5), false, "  X", "  X", "XXX", "X X", 'X',
					new ItemStack(Blocks.planks, 1, 5));
			addRecipe("Crate Wooden1", new ItemStack(CustomItems.crate, 1, 0), false, "XXXX", "X  X", "X  X", "XXXX",
					'X', new ItemStack(Blocks.planks, 1, 0));
			addRecipe("Crate Wooden2", new ItemStack(CustomItems.crate, 1, 1), false, "XXXX", "X  X", "X  X", "XXXX",
					'X', new ItemStack(Blocks.planks, 1, 1));
			addRecipe("Crate Wooden3", new ItemStack(CustomItems.crate, 1, 2), false, "XXXX", "X  X", "X  X", "XXXX",
					'X', new ItemStack(Blocks.planks, 1, 2));
			addRecipe("Crate Wooden4", new ItemStack(CustomItems.crate, 1, 3), false, "XXXX", "X  X", "X  X", "XXXX",
					'X', new ItemStack(Blocks.planks, 1, 3));
			addRecipe("Crate Wooden5", new ItemStack(CustomItems.crate, 1, 4), false, "XXXX", "X  X", "X  X", "XXXX",
					'X', new ItemStack(Blocks.planks, 1, 4));
			addRecipe("Crate Wooden6", new ItemStack(CustomItems.crate, 1, 5), false, "XXXX", "X  X", "X  X", "XXXX",
					'X', new ItemStack(Blocks.planks, 1, 5));
			addRecipe("WeaponRack Wooden1", new ItemStack(CustomItems.weaponsRack, 1, 0), false, "XXX", "XYX", "XYX",
					"XXX", 'X', new ItemStack(Blocks.planks, 1, 0), 'Y', Items.stick);
			addRecipe("WeaponRack Wooden2", new ItemStack(CustomItems.weaponsRack, 1, 1), false, "XXX", "XYX", "XYX",
					"XXX", 'X', new ItemStack(Blocks.planks, 1, 1), 'Y', Items.stick);
			addRecipe("WeaponRack Wooden3", new ItemStack(CustomItems.weaponsRack, 1, 2), false, "XXX", "XYX", "XYX",
					"XXX", 'X', new ItemStack(Blocks.planks, 1, 2), 'Y', Items.stick);
			addRecipe("WeaponRack Wooden4", new ItemStack(CustomItems.weaponsRack, 1, 3), false, "XXX", "XYX", "XYX",
					"XXX", 'X', new ItemStack(Blocks.planks, 1, 3), 'Y', Items.stick);
			addRecipe("WeaponRack Wooden5", new ItemStack(CustomItems.weaponsRack, 1, 4), false, "XXX", "XYX", "XYX",
					"XXX", 'X', new ItemStack(Blocks.planks, 1, 4), 'Y', Items.stick);
			addRecipe("WeaponRack Wooden6", new ItemStack(CustomItems.weaponsRack, 1, 5), false, "XXX", "XYX", "XYX",
					"XXX", 'X', new ItemStack(Blocks.planks, 1, 5), 'Y', Items.stick);
			addRecipe("Couch Wooden1", new ItemStack(CustomItems.couchWood, 1, 0), false, "   X", "   X", "XXXX",
					"X  X", 'X', new ItemStack(Blocks.planks, 1, 0));
			addRecipe("Couch Wooden2", new ItemStack(CustomItems.couchWood, 1, 1), false, "   X", "   X", "XXXX",
					"X  X", 'X', new ItemStack(Blocks.planks, 1, 1));
			addRecipe("Couch Wooden3", new ItemStack(CustomItems.couchWood, 1, 2), false, "   X", "   X", "XXXX",
					"X  X", 'X', new ItemStack(Blocks.planks, 1, 2));
			addRecipe("Couch Wooden4", new ItemStack(CustomItems.couchWood, 1, 3), false, "   X", "   X", "XXXX",
					"X  X", 'X', new ItemStack(Blocks.planks, 1, 3));
			addRecipe("Couch Wooden5", new ItemStack(CustomItems.couchWood, 1, 4), false, "   X", "   X", "XXXX",
					"X  X", 'X', new ItemStack(Blocks.planks, 1, 4));
			addRecipe("Couch Wooden6", new ItemStack(CustomItems.couchWood, 1, 5), false, "   X", "   X", "XXXX",
					"X  X", 'X', new ItemStack(Blocks.planks, 1, 5));
			addRecipe("Couch Wool1", new ItemStack(CustomItems.couchWool, 1, 0), false, "   Z", "   Z", "ZZZZ", "X  X",
					'X', new ItemStack(Blocks.planks, 1, 0), 'Z', Blocks.wool);
			addRecipe("Couch Wool2", new ItemStack(CustomItems.couchWool, 1, 1), false, "   Z", "   Z", "ZZZZ", "X  X",
					'X', new ItemStack(Blocks.planks, 1, 1), 'Z', Blocks.wool);
			addRecipe("Couch Wool3", new ItemStack(CustomItems.couchWool, 1, 2), false, "   Z", "   Z", "ZZZZ", "X  X",
					'X', new ItemStack(Blocks.planks, 1, 2), 'Z', Blocks.wool);
			addRecipe("Couch Wool4", new ItemStack(CustomItems.couchWool, 1, 3), false, "   Z", "   Z", "ZZZZ", "X  X",
					'X', new ItemStack(Blocks.planks, 1, 3), 'Z', Blocks.wool);
			addRecipe("Couch Wool5", new ItemStack(CustomItems.couchWool, 1, 4), false, "   Z", "   Z", "ZZZZ", "X  X",
					'X', new ItemStack(Blocks.planks, 1, 4), 'Z', Blocks.wool);
			addRecipe("Couch Wool6", new ItemStack(CustomItems.couchWool, 1, 5), false, "   Z", "   Z", "ZZZZ", "X  X",
					'X', new ItemStack(Blocks.planks, 1, 5), 'Z', Blocks.wool);
			addRecipe("Table Wood1", new ItemStack(CustomItems.table, 1, 0), false, "XXXX", "X  X", "X  X", 'X',
					new ItemStack(Blocks.planks, 1, 0), 'Z', Blocks.wool);
			addRecipe("Table Wood2", new ItemStack(CustomItems.table, 1, 1), false, "XXXX", "X  X", "X  X", 'X',
					new ItemStack(Blocks.planks, 1, 1), 'Z', Blocks.wool);
			addRecipe("Table Wood3", new ItemStack(CustomItems.table, 1, 2), false, "XXXX", "X  X", "X  X", 'X',
					new ItemStack(Blocks.planks, 1, 2), 'Z', Blocks.wool);
			addRecipe("Table Wood4", new ItemStack(CustomItems.table, 1, 3), false, "XXXX", "X  X", "X  X", 'X',
					new ItemStack(Blocks.planks, 1, 3), 'Z', Blocks.wool);
			addRecipe("Table Wood5", new ItemStack(CustomItems.table, 1, 4), false, "XXXX", "X  X", "X  X", 'X',
					new ItemStack(Blocks.planks, 1, 4), 'Z', Blocks.wool);
			addRecipe("Table Wood6", new ItemStack(CustomItems.table, 1, 5), false, "XXXX", "X  X", "X  X", 'X',
					new ItemStack(Blocks.planks, 1, 5), 'Z', Blocks.wool);
			addRecipe("Stool Wood1", new ItemStack(CustomItems.stool, 1, 0), false, "XXX", " X ", "X X", 'X',
					new ItemStack(Blocks.planks, 1, 0));
			addRecipe("Stool Wood2", new ItemStack(CustomItems.stool, 1, 1), false, "XXX", " X ", "X X", 'X',
					new ItemStack(Blocks.planks, 1, 1));
			addRecipe("Stool Wood3", new ItemStack(CustomItems.stool, 1, 2), false, "XXX", " X ", "X X", 'X',
					new ItemStack(Blocks.planks, 1, 2));
			addRecipe("Stool Wood4", new ItemStack(CustomItems.stool, 1, 3), false, "XXX", " X ", "X X", 'X',
					new ItemStack(Blocks.planks, 1, 3));
			addRecipe("Stool Wood5", new ItemStack(CustomItems.stool, 1, 4), false, "XXX", " X ", "X X", 'X',
					new ItemStack(Blocks.planks, 1, 4));
			addRecipe("Stool Wood6", new ItemStack(CustomItems.stool, 1, 5), false, "XXX", " X ", "X X", 'X',
					new ItemStack(Blocks.planks, 1, 5));
			addRecipe("Barrel Wood1", new ItemStack(CustomItems.barrel, 1, 0), false, "XXX", "X X", "X X", "XXX", 'X',
					new ItemStack(Blocks.planks, 1, 0));
			addRecipe("Barrel Wood2", new ItemStack(CustomItems.barrel, 1, 1), false, "XXX", "X X", "X X", "XXX", 'X',
					new ItemStack(Blocks.planks, 1, 1));
			addRecipe("Barrel Wood3", new ItemStack(CustomItems.barrel, 1, 2), false, "XXX", "X X", "X X", "XXX", 'X',
					new ItemStack(Blocks.planks, 1, 2));
			addRecipe("Barrel Wood4", new ItemStack(CustomItems.barrel, 1, 3), false, "XXX", "X X", "X X", "XXX", 'X',
					new ItemStack(Blocks.planks, 1, 3));
			addRecipe("Barrel Wood5", new ItemStack(CustomItems.barrel, 1, 4), false, "XXX", "X X", "X X", "XXX", 'X',
					new ItemStack(Blocks.planks, 1, 4));
			addRecipe("Barrel Wood6", new ItemStack(CustomItems.barrel, 1, 5), false, "XXX", "X X", "X X", "XXX", 'X',
					new ItemStack(Blocks.planks, 1, 5));
			addRecipe("Shelf Wood1", new ItemStack(CustomItems.shelf, 2, 0), false, "XXX", "XY ", "X  ", 'Y',
					Items.stick, 'X', new ItemStack(Blocks.planks, 1, 0));
			addRecipe("Shelf Wood2", new ItemStack(CustomItems.shelf, 2, 1), false, "XXX", "XY ", "X  ", 'Y',
					Items.stick, 'X', new ItemStack(Blocks.planks, 1, 1));
			addRecipe("Shelf Wood3", new ItemStack(CustomItems.shelf, 2, 2), false, "XXX", "XY ", "X  ", 'Y',
					Items.stick, 'X', new ItemStack(Blocks.planks, 1, 2));
			addRecipe("Shelf Wood4", new ItemStack(CustomItems.shelf, 2, 3), false, "XXX", "XY ", "X  ", 'Y',
					Items.stick, 'X', new ItemStack(Blocks.planks, 1, 3));
			addRecipe("Shelf Wood5", new ItemStack(CustomItems.shelf, 2, 4), false, "XXX", "XY ", "X  ", 'Y',
					Items.stick, 'X', new ItemStack(Blocks.planks, 1, 4));
			addRecipe("Shelf Wood6", new ItemStack(CustomItems.shelf, 2, 5), false, "XXX", "XY ", "X  ", 'Y',
					Items.stick, 'X', new ItemStack(Blocks.planks, 1, 5));
			addRecipe("Beam Wood1", new ItemStack(CustomItems.beam, 2, 0), false, "XXX", "XXX", 'X',
					new ItemStack(Blocks.planks, 1, 0));
			addRecipe("Beam Wood2", new ItemStack(CustomItems.beam, 2, 1), false, "XXX", "XXX", 'X',
					new ItemStack(Blocks.planks, 1, 1));
			addRecipe("Beam Wood3", new ItemStack(CustomItems.beam, 2, 2), false, "XXX", "XXX", 'X',
					new ItemStack(Blocks.planks, 1, 2));
			addRecipe("Beam Wood4", new ItemStack(CustomItems.beam, 2, 3), false, "XXX", "XXX", 'X',
					new ItemStack(Blocks.planks, 1, 3));
			addRecipe("Beam Wood5", new ItemStack(CustomItems.beam, 2, 4), false, "XXX", "XXX", 'X',
					new ItemStack(Blocks.planks, 1, 4));
			addRecipe("Beam Wood6", new ItemStack(CustomItems.beam, 2, 5), false, "XXX", "XXX", 'X',
					new ItemStack(Blocks.planks, 1, 5));
			addRecipe("Sign Wood1", new ItemStack(CustomItems.sign, 1, 0), false, "YYY", "XXX", "XXX", 'X',
					new ItemStack(Blocks.planks, 1, 0), 'Y', Items.iron_ingot);
			addRecipe("Sign Wood2", new ItemStack(CustomItems.sign, 1, 1), false, "YYY", "XXX", "XXX", 'X',
					new ItemStack(Blocks.planks, 1, 1), 'Y', Items.iron_ingot);
			addRecipe("Sign Wood3", new ItemStack(CustomItems.sign, 1, 2), false, "YYY", "XXX", "XXX", 'X',
					new ItemStack(Blocks.planks, 1, 2), 'Y', Items.iron_ingot);
			addRecipe("Sign Wood4", new ItemStack(CustomItems.sign, 1, 3), false, "YYY", "XXX", "XXX", 'X',
					new ItemStack(Blocks.planks, 1, 3), 'Y', Items.iron_ingot);
			addRecipe("Sign Wood5", new ItemStack(CustomItems.sign, 1, 4), false, "YYY", "XXX", "XXX", 'X',
					new ItemStack(Blocks.planks, 1, 4), 'Y', Items.iron_ingot);
			addRecipe("Sign Wood6", new ItemStack(CustomItems.sign, 1, 5), false, "YYY", "XXX", "XXX", 'X',
					new ItemStack(Blocks.planks, 1, 5), 'Y', Items.iron_ingot);
			addRecipe("Lamp", new ItemStack(CustomItems.lamp), false, "XXX", "YZY", "XXX", 'X', Items.iron_ingot, 'Y',
					Blocks.glass, 'Z', Blocks.torch);
			addRecipe("Candle", new ItemStack(CustomItems.candle), false, "XZX", " X ", 'X', Items.iron_ingot, 'Z',
					Blocks.torch);
			addRecipe("BigSign", new ItemStack(CustomItems.bigsign, 2), false, "XXX", "XXX", "XXX", 'X', Blocks.planks);
		}
	}
}

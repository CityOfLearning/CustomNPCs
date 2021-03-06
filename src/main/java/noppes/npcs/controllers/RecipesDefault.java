package noppes.npcs.controllers;

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
		if ((ob instanceof Item)) {
			item = new ItemStack((Item) ob);
		} else {
			if ((ob instanceof Block)) {
				item = new ItemStack((Block) ob);
			} else {
				item = (ItemStack) ob;
			}
		}
		RecipeCarpentry recipeAnvil = new RecipeCarpentry(name);
		recipeAnvil.isGlobal = isGlobal;
		recipeAnvil = RecipeCarpentry.createRecipe(recipeAnvil, item, recipe);
		try {
			RecipeController.instance.saveRecipe(recipeAnvil);
		} catch (IOException e) {
		}
	}

	public static void loadDefaultRecipes(int i) {
		if (i < 0) {
			addRecipe("Npc Wand", CustomItems.wand, true, new Object[] { "XX", " Y", " Y", Character.valueOf('X'),
					Items.bread, Character.valueOf('Y'), Items.stick });
			addRecipe("Mob Cloner", CustomItems.cloner, true, new Object[] { "XX", "XY", " Y", Character.valueOf('X'),
					Items.bread, Character.valueOf('Y'), Items.stick });
			addRecipe("Carpentry Bench", CustomItems.carpentyBench, true,
					new Object[] { "XYX", "Z Z", "Z Z", Character.valueOf('X'), Blocks.planks, Character.valueOf('Z'),
							Items.stick, Character.valueOf('Y'), Blocks.crafting_table });
		}
		if (i < 1) {

			addRecipe("WallBanner Wooden", new ItemStack(CustomItems.wallBanner, 1, 0), false, new Object[] { "XXX",
					"ZZZ", "ZZZ", "Z Z", Character.valueOf('Z'), Blocks.wool, Character.valueOf('X'), Blocks.planks });
			addRecipe("WallBanner Stone", new ItemStack(CustomItems.wallBanner, 1, 1), false,
					new Object[] { "XXX", "ZZZ", "ZZZ", "Z Z", Character.valueOf('Z'), Blocks.wool,
							Character.valueOf('X'), Blocks.cobblestone });
			addRecipe("WallBanner Iron", new ItemStack(CustomItems.wallBanner, 1, 2), false,
					new Object[] { "XXX", "ZZZ", "ZZZ", "Z Z", Character.valueOf('Z'), Blocks.wool,
							Character.valueOf('X'), Items.iron_ingot });
			addRecipe("WallBanner Gold", new ItemStack(CustomItems.wallBanner, 1, 3), false,
					new Object[] { "XXX", "ZZZ", "ZZZ", "Z Z", Character.valueOf('Z'), Blocks.wool,
							Character.valueOf('X'), Items.gold_ingot });
			addRecipe("WallBanner Diamond", new ItemStack(CustomItems.wallBanner, 1, 4), false, new Object[] { "XXX",
					"ZZZ", "ZZZ", "Z Z", Character.valueOf('Z'), Blocks.wool, Character.valueOf('X'), Items.diamond });

			addRecipe("Banner Wooden", new ItemStack(CustomItems.banner, 1, 0), false,
					new Object[] { " X ", " Z ", " Z ", "ZZZ", Character.valueOf('X'),
							new ItemStack(CustomItems.wallBanner, 1, 0), Character.valueOf('Z'), Blocks.planks });
			addRecipe("Banner Stone", new ItemStack(CustomItems.banner, 1, 1), false,
					new Object[] { " X ", " Z ", " Z ", "ZZZ", Character.valueOf('X'),
							new ItemStack(CustomItems.wallBanner, 1, 1), Character.valueOf('Z'), Blocks.cobblestone });
			addRecipe("Banner Iron", new ItemStack(CustomItems.banner, 1, 2), false,
					new Object[] { " X ", " Z ", " Z ", "ZZZ", Character.valueOf('X'),
							new ItemStack(CustomItems.wallBanner, 1, 2), Character.valueOf('Z'), Items.iron_ingot });
			addRecipe("Banner Gold", new ItemStack(CustomItems.banner, 1, 3), false,
					new Object[] { " X ", " Z ", " Z ", "ZZZ", Character.valueOf('X'),
							new ItemStack(CustomItems.wallBanner, 1, 3), Character.valueOf('Z'), Items.gold_ingot });
			addRecipe("Banner Diamond", new ItemStack(CustomItems.banner, 1, 4), false,
					new Object[] { " X ", " Z ", " Z ", "ZZZ", Character.valueOf('X'),
							new ItemStack(CustomItems.wallBanner, 1, 4), Character.valueOf('Z'), Items.diamond });

			addRecipe("Lamp Wooden", new ItemStack(CustomItems.tallLamp, 1, 0), false,
					new Object[] { "YXY", " Z ", " Z ", "ZZZ", Character.valueOf('X'), Blocks.torch,
							Character.valueOf('Y'), Blocks.wool, Character.valueOf('Z'), Blocks.planks });
			addRecipe("Lamp Stone", new ItemStack(CustomItems.tallLamp, 1, 1), false,
					new Object[] { "YXY", " Z ", " Z ", "ZZZ", Character.valueOf('X'), Blocks.torch,
							Character.valueOf('Y'), Blocks.wool, Character.valueOf('Z'), Blocks.cobblestone });
			addRecipe("Lamp Iron", new ItemStack(CustomItems.tallLamp, 1, 2), false,
					new Object[] { "YXY", " Z ", " Z ", "ZZZ", Character.valueOf('X'), Blocks.torch,
							Character.valueOf('Y'), Blocks.wool, Character.valueOf('Z'), Items.iron_ingot });
			addRecipe("Lamp Gold", new ItemStack(CustomItems.tallLamp, 1, 3), false,
					new Object[] { "YXY", " Z ", " Z ", "ZZZ", Character.valueOf('X'), Blocks.torch,
							Character.valueOf('Y'), Blocks.wool, Character.valueOf('Z'), Items.gold_ingot });
			addRecipe("Lamp Diamond", new ItemStack(CustomItems.tallLamp, 1, 4), false,
					new Object[] { "YXY", " Z ", " Z ", "ZZZ", Character.valueOf('X'), Blocks.torch,
							Character.valueOf('Y'), Blocks.wool, Character.valueOf('Z'), Items.diamond });

			addRecipe("Chair Wooden1", new ItemStack(CustomItems.chair, 1, 0), false, new Object[] { "  X", "  X",
					"XXX", "X X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 0) });
			addRecipe("Chair Wooden2", new ItemStack(CustomItems.chair, 1, 1), false, new Object[] { "  X", "  X",
					"XXX", "X X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 1) });
			addRecipe("Chair Wooden3", new ItemStack(CustomItems.chair, 1, 2), false, new Object[] { "  X", "  X",
					"XXX", "X X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 2) });
			addRecipe("Chair Wooden4", new ItemStack(CustomItems.chair, 1, 3), false, new Object[] { "  X", "  X",
					"XXX", "X X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 3) });
			addRecipe("Chair Wooden5", new ItemStack(CustomItems.chair, 1, 4), false, new Object[] { "  X", "  X",
					"XXX", "X X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 4) });
			addRecipe("Chair Wooden6", new ItemStack(CustomItems.chair, 1, 5), false, new Object[] { "  X", "  X",
					"XXX", "X X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 5) });

			addRecipe("Crate Wooden1", new ItemStack(CustomItems.crate, 1, 0), false, new Object[] { "XXXX", "X  X",
					"X  X", "XXXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 0) });
			addRecipe("Crate Wooden2", new ItemStack(CustomItems.crate, 1, 1), false, new Object[] { "XXXX", "X  X",
					"X  X", "XXXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 1) });
			addRecipe("Crate Wooden3", new ItemStack(CustomItems.crate, 1, 2), false, new Object[] { "XXXX", "X  X",
					"X  X", "XXXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 2) });
			addRecipe("Crate Wooden4", new ItemStack(CustomItems.crate, 1, 3), false, new Object[] { "XXXX", "X  X",
					"X  X", "XXXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 3) });
			addRecipe("Crate Wooden5", new ItemStack(CustomItems.crate, 1, 4), false, new Object[] { "XXXX", "X  X",
					"X  X", "XXXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 4) });
			addRecipe("Crate Wooden6", new ItemStack(CustomItems.crate, 1, 5), false, new Object[] { "XXXX", "X  X",
					"X  X", "XXXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 5) });

			addRecipe("WeaponRack Wooden1", new ItemStack(CustomItems.weaponsRack, 1, 0), false,
					new Object[] { "XXX", "XYX", "XYX", "XXX", Character.valueOf('X'),
							new ItemStack(Blocks.planks, 1, 0), Character.valueOf('Y'), Items.stick });
			addRecipe("WeaponRack Wooden2", new ItemStack(CustomItems.weaponsRack, 1, 1), false,
					new Object[] { "XXX", "XYX", "XYX", "XXX", Character.valueOf('X'),
							new ItemStack(Blocks.planks, 1, 1), Character.valueOf('Y'), Items.stick });
			addRecipe("WeaponRack Wooden3", new ItemStack(CustomItems.weaponsRack, 1, 2), false,
					new Object[] { "XXX", "XYX", "XYX", "XXX", Character.valueOf('X'),
							new ItemStack(Blocks.planks, 1, 2), Character.valueOf('Y'), Items.stick });
			addRecipe("WeaponRack Wooden4", new ItemStack(CustomItems.weaponsRack, 1, 3), false,
					new Object[] { "XXX", "XYX", "XYX", "XXX", Character.valueOf('X'),
							new ItemStack(Blocks.planks, 1, 3), Character.valueOf('Y'), Items.stick });
			addRecipe("WeaponRack Wooden5", new ItemStack(CustomItems.weaponsRack, 1, 4), false,
					new Object[] { "XXX", "XYX", "XYX", "XXX", Character.valueOf('X'),
							new ItemStack(Blocks.planks, 1, 4), Character.valueOf('Y'), Items.stick });
			addRecipe("WeaponRack Wooden6", new ItemStack(CustomItems.weaponsRack, 1, 5), false,
					new Object[] { "XXX", "XYX", "XYX", "XXX", Character.valueOf('X'),
							new ItemStack(Blocks.planks, 1, 5), Character.valueOf('Y'), Items.stick });

			addRecipe("Couch Wooden1", new ItemStack(CustomItems.couchWood, 1, 0), false, new Object[] { "   X", "   X",
					"XXXX", "X  X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 0) });
			addRecipe("Couch Wooden2", new ItemStack(CustomItems.couchWood, 1, 1), false, new Object[] { "   X", "   X",
					"XXXX", "X  X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 1) });
			addRecipe("Couch Wooden3", new ItemStack(CustomItems.couchWood, 1, 2), false, new Object[] { "   X", "   X",
					"XXXX", "X  X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 2) });
			addRecipe("Couch Wooden4", new ItemStack(CustomItems.couchWood, 1, 3), false, new Object[] { "   X", "   X",
					"XXXX", "X  X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 3) });
			addRecipe("Couch Wooden5", new ItemStack(CustomItems.couchWood, 1, 4), false, new Object[] { "   X", "   X",
					"XXXX", "X  X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 4) });
			addRecipe("Couch Wooden6", new ItemStack(CustomItems.couchWood, 1, 5), false, new Object[] { "   X", "   X",
					"XXXX", "X  X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 5) });

			addRecipe("Couch Wool1", new ItemStack(CustomItems.couchWool, 1, 0), false,
					new Object[] { "   Z", "   Z", "ZZZZ", "X  X", Character.valueOf('X'),
							new ItemStack(Blocks.planks, 1, 0), Character.valueOf('Z'), Blocks.wool });
			addRecipe("Couch Wool2", new ItemStack(CustomItems.couchWool, 1, 1), false,
					new Object[] { "   Z", "   Z", "ZZZZ", "X  X", Character.valueOf('X'),
							new ItemStack(Blocks.planks, 1, 1), Character.valueOf('Z'), Blocks.wool });
			addRecipe("Couch Wool3", new ItemStack(CustomItems.couchWool, 1, 2), false,
					new Object[] { "   Z", "   Z", "ZZZZ", "X  X", Character.valueOf('X'),
							new ItemStack(Blocks.planks, 1, 2), Character.valueOf('Z'), Blocks.wool });
			addRecipe("Couch Wool4", new ItemStack(CustomItems.couchWool, 1, 3), false,
					new Object[] { "   Z", "   Z", "ZZZZ", "X  X", Character.valueOf('X'),
							new ItemStack(Blocks.planks, 1, 3), Character.valueOf('Z'), Blocks.wool });
			addRecipe("Couch Wool5", new ItemStack(CustomItems.couchWool, 1, 4), false,
					new Object[] { "   Z", "   Z", "ZZZZ", "X  X", Character.valueOf('X'),
							new ItemStack(Blocks.planks, 1, 4), Character.valueOf('Z'), Blocks.wool });
			addRecipe("Couch Wool6", new ItemStack(CustomItems.couchWool, 1, 5), false,
					new Object[] { "   Z", "   Z", "ZZZZ", "X  X", Character.valueOf('X'),
							new ItemStack(Blocks.planks, 1, 5), Character.valueOf('Z'), Blocks.wool });

			addRecipe("Table Wood1", new ItemStack(CustomItems.table, 1, 0), false,
					new Object[] { "XXXX", "X  X", "X  X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 0),
							Character.valueOf('Z'), Blocks.wool });
			addRecipe("Table Wood2", new ItemStack(CustomItems.table, 1, 1), false,
					new Object[] { "XXXX", "X  X", "X  X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 1),
							Character.valueOf('Z'), Blocks.wool });
			addRecipe("Table Wood3", new ItemStack(CustomItems.table, 1, 2), false,
					new Object[] { "XXXX", "X  X", "X  X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 2),
							Character.valueOf('Z'), Blocks.wool });
			addRecipe("Table Wood4", new ItemStack(CustomItems.table, 1, 3), false,
					new Object[] { "XXXX", "X  X", "X  X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 3),
							Character.valueOf('Z'), Blocks.wool });
			addRecipe("Table Wood5", new ItemStack(CustomItems.table, 1, 4), false,
					new Object[] { "XXXX", "X  X", "X  X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 4),
							Character.valueOf('Z'), Blocks.wool });
			addRecipe("Table Wood6", new ItemStack(CustomItems.table, 1, 5), false,
					new Object[] { "XXXX", "X  X", "X  X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 5),
							Character.valueOf('Z'), Blocks.wool });

			addRecipe("Stool Wood1", new ItemStack(CustomItems.stool, 1, 0), false,
					new Object[] { "XXX", " X ", "X X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 0) });
			addRecipe("Stool Wood2", new ItemStack(CustomItems.stool, 1, 1), false,
					new Object[] { "XXX", " X ", "X X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 1) });
			addRecipe("Stool Wood3", new ItemStack(CustomItems.stool, 1, 2), false,
					new Object[] { "XXX", " X ", "X X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 2) });
			addRecipe("Stool Wood4", new ItemStack(CustomItems.stool, 1, 3), false,
					new Object[] { "XXX", " X ", "X X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 3) });
			addRecipe("Stool Wood5", new ItemStack(CustomItems.stool, 1, 4), false,
					new Object[] { "XXX", " X ", "X X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 4) });
			addRecipe("Stool Wood6", new ItemStack(CustomItems.stool, 1, 5), false,
					new Object[] { "XXX", " X ", "X X", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 5) });

			addRecipe("Barrel Wood1", new ItemStack(CustomItems.barrel, 1, 0), false, new Object[] { "XXX", "X X",
					"X X", "XXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 0) });
			addRecipe("Barrel Wood2", new ItemStack(CustomItems.barrel, 1, 1), false, new Object[] { "XXX", "X X",
					"X X", "XXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 1) });
			addRecipe("Barrel Wood3", new ItemStack(CustomItems.barrel, 1, 2), false, new Object[] { "XXX", "X X",
					"X X", "XXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 2) });
			addRecipe("Barrel Wood4", new ItemStack(CustomItems.barrel, 1, 3), false, new Object[] { "XXX", "X X",
					"X X", "XXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 3) });
			addRecipe("Barrel Wood5", new ItemStack(CustomItems.barrel, 1, 4), false, new Object[] { "XXX", "X X",
					"X X", "XXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 4) });
			addRecipe("Barrel Wood6", new ItemStack(CustomItems.barrel, 1, 5), false, new Object[] { "XXX", "X X",
					"X X", "XXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 5) });

			addRecipe("Shelf Wood1", new ItemStack(CustomItems.shelf, 2, 0), false, new Object[] { "XXX", "XY ", "X  ",
					Character.valueOf('Y'), Items.stick, Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 0) });
			addRecipe("Shelf Wood2", new ItemStack(CustomItems.shelf, 2, 1), false, new Object[] { "XXX", "XY ", "X  ",
					Character.valueOf('Y'), Items.stick, Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 1) });
			addRecipe("Shelf Wood3", new ItemStack(CustomItems.shelf, 2, 2), false, new Object[] { "XXX", "XY ", "X  ",
					Character.valueOf('Y'), Items.stick, Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 2) });
			addRecipe("Shelf Wood4", new ItemStack(CustomItems.shelf, 2, 3), false, new Object[] { "XXX", "XY ", "X  ",
					Character.valueOf('Y'), Items.stick, Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 3) });
			addRecipe("Shelf Wood5", new ItemStack(CustomItems.shelf, 2, 4), false, new Object[] { "XXX", "XY ", "X  ",
					Character.valueOf('Y'), Items.stick, Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 4) });
			addRecipe("Shelf Wood6", new ItemStack(CustomItems.shelf, 2, 5), false, new Object[] { "XXX", "XY ", "X  ",
					Character.valueOf('Y'), Items.stick, Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 5) });

			addRecipe("Beam Wood1", new ItemStack(CustomItems.beam, 2, 0), false,
					new Object[] { "XXX", "XXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 0) });
			addRecipe("Beam Wood2", new ItemStack(CustomItems.beam, 2, 1), false,
					new Object[] { "XXX", "XXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 1) });
			addRecipe("Beam Wood3", new ItemStack(CustomItems.beam, 2, 2), false,
					new Object[] { "XXX", "XXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 2) });
			addRecipe("Beam Wood4", new ItemStack(CustomItems.beam, 2, 3), false,
					new Object[] { "XXX", "XXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 3) });
			addRecipe("Beam Wood5", new ItemStack(CustomItems.beam, 2, 4), false,
					new Object[] { "XXX", "XXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 4) });
			addRecipe("Beam Wood6", new ItemStack(CustomItems.beam, 2, 5), false,
					new Object[] { "XXX", "XXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 5) });

			addRecipe("Sign Wood1", new ItemStack(CustomItems.sign, 1, 0), false,
					new Object[] { "YYY", "XXX", "XXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 0),
							Character.valueOf('Y'), Items.iron_ingot });
			addRecipe("Sign Wood2", new ItemStack(CustomItems.sign, 1, 1), false,
					new Object[] { "YYY", "XXX", "XXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 1),
							Character.valueOf('Y'), Items.iron_ingot });
			addRecipe("Sign Wood3", new ItemStack(CustomItems.sign, 1, 2), false,
					new Object[] { "YYY", "XXX", "XXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 2),
							Character.valueOf('Y'), Items.iron_ingot });
			addRecipe("Sign Wood4", new ItemStack(CustomItems.sign, 1, 3), false,
					new Object[] { "YYY", "XXX", "XXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 3),
							Character.valueOf('Y'), Items.iron_ingot });
			addRecipe("Sign Wood5", new ItemStack(CustomItems.sign, 1, 4), false,
					new Object[] { "YYY", "XXX", "XXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 4),
							Character.valueOf('Y'), Items.iron_ingot });
			addRecipe("Sign Wood6", new ItemStack(CustomItems.sign, 1, 5), false,
					new Object[] { "YYY", "XXX", "XXX", Character.valueOf('X'), new ItemStack(Blocks.planks, 1, 5),
							Character.valueOf('Y'), Items.iron_ingot });

			addRecipe("Lamp", new ItemStack(CustomItems.lamp), false,
					new Object[] { "XXX", "YZY", "XXX", Character.valueOf('X'), Items.iron_ingot,
							Character.valueOf('Y'), Blocks.glass, Character.valueOf('Z'), Blocks.torch });
			addRecipe("Candle", new ItemStack(CustomItems.candle), false, new Object[] { "XZX", " X ",
					Character.valueOf('X'), Items.iron_ingot, Character.valueOf('Z'), Blocks.torch });
		}
	}
}

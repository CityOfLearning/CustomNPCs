//

//

package noppes.npcs.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.CustomNpcs;
import noppes.npcs.EventHooks;
import noppes.npcs.api.handler.IRecipeHandler;
import noppes.npcs.api.handler.data.IRecipe;

public class RecipeController implements IRecipeHandler {
	private static Collection<RecipeCarpentry> prevRecipes;
	public static RecipeController instance;
	public static final int version = 1;
	public static HashMap<Integer, RecipeCarpentry> syncRecipes;
	static {
		RecipeController.syncRecipes = new HashMap<Integer, RecipeCarpentry>();
	}
	public HashMap<Integer, RecipeCarpentry> globalRecipes;
	public HashMap<Integer, RecipeCarpentry> anvilRecipes;

	public int nextId;

	public RecipeController() {
		globalRecipes = new HashMap<Integer, RecipeCarpentry>();
		anvilRecipes = new HashMap<Integer, RecipeCarpentry>();
		nextId = 1;
		RecipeController.instance = this;
	}

	@Override
	public IRecipe addRecipe(final String name, final boolean global, final ItemStack result, final int width,
			final int height, final ItemStack... objects) {
		final RecipeCarpentry recipe = new RecipeCarpentry(width, height, objects, result);
		recipe.isGlobal = global;
		recipe.name = name;
		try {
			return saveRecipe(recipe);
		} catch (IOException e) {
			e.printStackTrace();
			return recipe;
		}
	}

	@Override
	public IRecipe addRecipe(final String name, final boolean global, final ItemStack result, final Object... objects) {
		RecipeCarpentry recipe = new RecipeCarpentry(name);
		recipe.isGlobal = global;
		recipe = RecipeCarpentry.createRecipe(recipe, result, objects);
		try {
			return saveRecipe(recipe);
		} catch (IOException e) {
			e.printStackTrace();
			return recipe;
		}
	}

	private boolean containsRecipeName(String name) {
		name = name.toLowerCase();
		for (final RecipeCarpentry recipe : globalRecipes.values()) {
			if (recipe.name.toLowerCase().equals(name)) {
				return true;
			}
		}
		for (final RecipeCarpentry recipe : anvilRecipes.values()) {
			if (recipe.name.toLowerCase().equals(name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public RecipeCarpentry delete(final int id) {
		final RecipeCarpentry recipe = getRecipe(id);
		if (recipe == null) {
			return null;
		}
		globalRecipes.remove(recipe.id);
		anvilRecipes.remove(recipe.id);
		saveCategories();
		reloadGlobalRecipes();
		recipe.id = -1;
		return recipe;
	}

	public RecipeCarpentry findMatchingRecipe(final InventoryCrafting par1InventoryCrafting) {
		for (final RecipeCarpentry recipe : anvilRecipes.values()) {
			if (recipe.isValid() && recipe.matches(par1InventoryCrafting, null)) {
				return recipe;
			}
		}
		return null;
	}

	@Override
	public List<IRecipe> getCarpentryList() {
		return new ArrayList<IRecipe>(anvilRecipes.values());
	}

	@Override
	public List<IRecipe> getGlobalList() {
		return new ArrayList<IRecipe>(globalRecipes.values());
	}

	public RecipeCarpentry getRecipe(final int id) {
		if (globalRecipes.containsKey(id)) {
			return globalRecipes.get(id);
		}
		if (anvilRecipes.containsKey(id)) {
			return anvilRecipes.get(id);
		}
		return null;
	}

	private int getUniqueId() {
		return ++nextId;
	}

	public void load() {
		this.loadCategories();
		reloadGlobalRecipes();
		EventHooks.onGlobalRecipesLoaded(this);
	}

	private void loadCategories() {
		final File saveDir = CustomNpcs.getWorldSaveDirectory();
		try {
			final File file = new File(saveDir, "recipes.dat");
			if (file.exists()) {
				this.loadCategories(file);
			} else {
				globalRecipes.clear();
				anvilRecipes.clear();
				loadDefaultRecipes(-1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				final File file2 = new File(saveDir, "recipes.dat_old");
				if (file2.exists()) {
					this.loadCategories(file2);
				}
			} catch (Exception ee) {
				e.printStackTrace();
			}
		}
	}

	private void loadCategories(final File file) throws Exception {
		final NBTTagCompound nbttagcompound1 = CompressedStreamTools.readCompressed(new FileInputStream(file));
		nextId = nbttagcompound1.getInteger("LastId");
		final NBTTagList list = nbttagcompound1.getTagList("Data", 10);
		final HashMap<Integer, RecipeCarpentry> globalRecipes = new HashMap<Integer, RecipeCarpentry>();
		final HashMap<Integer, RecipeCarpentry> anvilRecipes = new HashMap<Integer, RecipeCarpentry>();
		if (list != null) {
			for (int i = 0; i < list.tagCount(); ++i) {
				final RecipeCarpentry recipe = RecipeCarpentry.read(list.getCompoundTagAt(i));
				if (recipe.isGlobal) {
					globalRecipes.put(recipe.id, recipe);
				} else {
					anvilRecipes.put(recipe.id, recipe);
				}
				if (recipe.id > nextId) {
					nextId = recipe.id;
				}
			}
		}
		this.anvilRecipes = anvilRecipes;
		this.globalRecipes = globalRecipes;
		loadDefaultRecipes(nbttagcompound1.getInteger("Version"));
	}

	private void loadDefaultRecipes(final int i) {
		if (i == 1) {
			return;
		}
		RecipesDefault.loadDefaultRecipes(i);
		saveCategories();
	}

	public void reloadGlobalRecipes() {
		final List list = CraftingManager.getInstance().getRecipeList();
		if (RecipeController.prevRecipes != null) {
			list.removeAll(RecipeController.prevRecipes);
		}
		RecipeController.prevRecipes = new HashSet<RecipeCarpentry>();
		for (final RecipeCarpentry recipe : globalRecipes.values()) {
			if (recipe.isValid()) {
				RecipeController.prevRecipes.add(recipe);
			}
		}
		list.addAll(RecipeController.prevRecipes);
	}

	private void saveCategories() {
		try {
			final File saveDir = CustomNpcs.getWorldSaveDirectory();
			final NBTTagList list = new NBTTagList();
			for (final RecipeCarpentry recipe : globalRecipes.values()) {
				if (recipe.savesRecipe) {
					list.appendTag(recipe.writeNBT());
				}
			}
			for (final RecipeCarpentry recipe : anvilRecipes.values()) {
				if (recipe.savesRecipe) {
					list.appendTag(recipe.writeNBT());
				}
			}
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setTag("Data", list);
			nbttagcompound.setInteger("LastId", nextId);
			nbttagcompound.setInteger("Version", 1);
			final File file = new File(saveDir, "recipes.dat_new");
			final File file2 = new File(saveDir, "recipes.dat_old");
			final File file3 = new File(saveDir, "recipes.dat");
			CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file));
			if (file2.exists()) {
				file2.delete();
			}
			file3.renameTo(file2);
			if (file3.exists()) {
				file3.delete();
			}
			file.renameTo(file3);
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RecipeCarpentry saveRecipe(final RecipeCarpentry recipe) throws IOException {
		final RecipeCarpentry current = getRecipe(recipe.id);
		if ((current != null) && !current.name.equals(recipe.name)) {
			while (containsRecipeName(recipe.name)) {
				recipe.name += "_";
			}
		}
		if (recipe.id == -1) {
			recipe.id = getUniqueId();
			while (containsRecipeName(recipe.name)) {
				recipe.name += "_";
			}
		}
		if (recipe.isGlobal) {
			anvilRecipes.remove(recipe.id);
			globalRecipes.put(recipe.id, recipe);
		} else {
			globalRecipes.remove(recipe.id);
			anvilRecipes.put(recipe.id, recipe);
		}
		saveCategories();
		reloadGlobalRecipes();
		return recipe;
	}
}

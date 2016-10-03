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
	public static HashMap<Integer, RecipeCarpentry> syncRecipes = new HashMap();
	public HashMap<Integer, RecipeCarpentry> globalRecipes = new HashMap();
	public HashMap<Integer, RecipeCarpentry> anvilRecipes = new HashMap();
	public int nextId = 1;

	public RecipeController() {
		instance = this;
	}

	@Override
	public IRecipe addRecipe(String name, boolean global, ItemStack result, int width, int height,
			ItemStack... objects) {
		RecipeCarpentry recipe = new RecipeCarpentry(width, height, objects, result);
		recipe.isGlobal = global;
		recipe.name = name;
		try {
			return saveRecipe(recipe);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return recipe;
	}

	@Override
	public IRecipe addRecipe(String name, boolean global, ItemStack result, Object... objects) {
		RecipeCarpentry recipe = new RecipeCarpentry(name);
		recipe.isGlobal = global;
		recipe = RecipeCarpentry.createRecipe(recipe, result, objects);
		try {
			return saveRecipe(recipe);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return recipe;
	}

	private boolean containsRecipeName(String name) {
		name = name.toLowerCase();
		for (RecipeCarpentry recipe : globalRecipes.values()) {
			if (recipe.name.toLowerCase().equals(name)) {
				return true;
			}
		}
		for (RecipeCarpentry recipe : anvilRecipes.values()) {
			if (recipe.name.toLowerCase().equals(name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public RecipeCarpentry delete(int id) {
		RecipeCarpentry recipe = getRecipe(id);
		if (recipe == null) {
			return null;
		}
		globalRecipes.remove(Integer.valueOf(recipe.id));
		anvilRecipes.remove(Integer.valueOf(recipe.id));
		saveCategories();
		reloadGlobalRecipes();
		recipe.id = -1;
		return recipe;
	}

	public RecipeCarpentry findMatchingRecipe(InventoryCrafting par1InventoryCrafting) {
		for (RecipeCarpentry recipe : anvilRecipes.values()) {
			if ((recipe.isValid()) && (recipe.matches(par1InventoryCrafting, null))) {
				return recipe;
			}
		}
		return null;
	}

	@Override
	public List<IRecipe> getCarpentryList() {
		return new ArrayList(anvilRecipes.values());
	}

	@Override
	public List<IRecipe> getGlobalList() {
		return new ArrayList(globalRecipes.values());
	}

	public RecipeCarpentry getRecipe(int id) {
		if (globalRecipes.containsKey(Integer.valueOf(id))) {
			return globalRecipes.get(Integer.valueOf(id));
		}
		if (anvilRecipes.containsKey(Integer.valueOf(id))) {
			return anvilRecipes.get(Integer.valueOf(id));
		}
		return null;
	}

	private int getUniqueId() {
		nextId += 1;
		return nextId;
	}

	public void load() {
		loadCategories();
		reloadGlobalRecipes();
		EventHooks.onGlobalRecipesLoaded(this);
	}

	private void loadCategories() {
		File saveDir = CustomNpcs.getWorldSaveDirectory();
		try {
			File file = new File(saveDir, "recipes.dat");
			if (file.exists()) {
				loadCategories(file);
			} else {
				globalRecipes.clear();
				anvilRecipes.clear();
				loadDefaultRecipes(-1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				File file = new File(saveDir, "recipes.dat_old");
				if (file.exists()) {
					loadCategories(file);
				}
			} catch (Exception ee) {
				e.printStackTrace();
			}
		}
	}

	private void loadCategories(File file) throws Exception {
		NBTTagCompound nbttagcompound1 = CompressedStreamTools.readCompressed(new FileInputStream(file));
		nextId = nbttagcompound1.getInteger("LastId");
		NBTTagList list = nbttagcompound1.getTagList("Data", 10);
		HashMap<Integer, RecipeCarpentry> globalRecipes = new HashMap();
		HashMap<Integer, RecipeCarpentry> anvilRecipes = new HashMap();
		if (list != null) {
			for (int i = 0; i < list.tagCount(); i++) {
				RecipeCarpentry recipe = RecipeCarpentry.read(list.getCompoundTagAt(i));
				if (recipe.isGlobal) {
					globalRecipes.put(Integer.valueOf(recipe.id), recipe);
				} else {
					anvilRecipes.put(Integer.valueOf(recipe.id), recipe);
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

	private void loadDefaultRecipes(int i) {
		if (i == 1) {
			return;
		}
		RecipesDefault.loadDefaultRecipes(i);
		saveCategories();
	}

	public void reloadGlobalRecipes() {
		List list = CraftingManager.getInstance().getRecipeList();
		if (prevRecipes != null) {
			list.removeAll(prevRecipes);
		}
		prevRecipes = new HashSet();
		for (RecipeCarpentry recipe : globalRecipes.values()) {
			if (recipe.isValid()) {
				prevRecipes.add(recipe);
			}
		}
		list.addAll(prevRecipes);
	}

	private void saveCategories() {
		try {
			File saveDir = CustomNpcs.getWorldSaveDirectory();
			NBTTagList list = new NBTTagList();
			for (RecipeCarpentry recipe : globalRecipes.values()) {
				if (recipe.savesRecipe) {
					list.appendTag(recipe.writeNBT());
				}
			}
			for (RecipeCarpentry recipe : anvilRecipes.values()) {
				if (recipe.savesRecipe) {
					list.appendTag(recipe.writeNBT());
				}
			}
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setTag("Data", list);
			nbttagcompound.setInteger("LastId", nextId);
			nbttagcompound.setInteger("Version", 1);
			File file = new File(saveDir, "recipes.dat_new");
			File file1 = new File(saveDir, "recipes.dat_old");
			File file2 = new File(saveDir, "recipes.dat");
			CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file));
			if (file1.exists()) {
				file1.delete();
			}
			file2.renameTo(file1);
			if (file2.exists()) {
				file2.delete();
			}
			file.renameTo(file2);
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RecipeCarpentry saveRecipe(RecipeCarpentry recipe) throws IOException {
		RecipeCarpentry current = getRecipe(recipe.id);
		if ((current != null) && (!current.name.equals(recipe.name))) {
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
			anvilRecipes.remove(Integer.valueOf(recipe.id));
			globalRecipes.put(Integer.valueOf(recipe.id), recipe);
		} else {
			globalRecipes.remove(Integer.valueOf(recipe.id));
			anvilRecipes.put(Integer.valueOf(recipe.id), recipe);
		}
		saveCategories();
		reloadGlobalRecipes();

		return recipe;
	}
}

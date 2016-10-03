package noppes.npcs.controllers;

import java.io.IOException;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.api.handler.data.IRecipe;

public class RecipeCarpentry extends ShapedRecipes implements IRecipe {
	public static RecipeCarpentry createRecipe(RecipeCarpentry recipe, ItemStack par1ItemStack,
			Object... par2ArrayOfObj) {
		String var3 = "";
		int var4 = 0;
		int var5 = 0;
		int var6 = 0;
		if ((par2ArrayOfObj[var4] instanceof String[])) {
			String[] var7 = (String[]) par2ArrayOfObj[(var4++)];
			String[] var8 = var7;
			int var9 = var7.length;
			for (int var10 = 0; var10 < var9; var10++) {
				String var11 = var8[var10];
				var6++;
				var5 = var11.length();
				var3 = var3 + var11;
			}
		} else {
			while ((par2ArrayOfObj[var4] instanceof String)) {
				String var13 = (String) par2ArrayOfObj[(var4++)];
				var6++;
				var5 = var13.length();
				var3 = var3 + var13;
			}
		}
		HashMap var14 = new HashMap();
		for (; var4 < par2ArrayOfObj.length; var4 += 2) {
			Character var16 = (Character) par2ArrayOfObj[var4];
			ItemStack var17 = null;
			if ((par2ArrayOfObj[(var4 + 1)] instanceof Item)) {
				var17 = new ItemStack((Item) par2ArrayOfObj[(var4 + 1)]);
			} else if ((par2ArrayOfObj[(var4 + 1)] instanceof Block)) {
				var17 = new ItemStack((Block) par2ArrayOfObj[(var4 + 1)], 1, -1);
			} else if ((par2ArrayOfObj[(var4 + 1)] instanceof ItemStack)) {
				var17 = (ItemStack) par2ArrayOfObj[(var4 + 1)];
			}
			var14.put(var16, var17);
		}
		ItemStack[] var15 = new ItemStack[var5 * var6];
		for (int var9 = 0; var9 < (var5 * var6); var9++) {
			char var18 = var3.charAt(var9);
			if (var14.containsKey(Character.valueOf(var18))) {
				var15[var9] = ((ItemStack) var14.get(Character.valueOf(var18))).copy();
			} else {
				var15[var9] = null;
			}
		}
		RecipeCarpentry newrecipe = new RecipeCarpentry(var5, var6, var15, par1ItemStack);
		newrecipe.copy(recipe);
		if ((var5 == 4) || (var6 == 4)) {
			newrecipe.isGlobal = false;
		}
		return newrecipe;
	}

	public static RecipeCarpentry read(NBTTagCompound compound) {
		RecipeCarpentry recipe = new RecipeCarpentry(compound.getInteger("Width"), compound.getInteger("Height"),
				NBTTags.getItemStackArray(compound.getTagList("Materials", 10)),
				ItemStack.loadItemStackFromNBT(compound.getCompoundTag("Item")));
		recipe.name = compound.getString("Name");
		recipe.id = compound.getInteger("ID");
		recipe.availability.readFromNBT(compound.getCompoundTag("Availability"));
		recipe.ignoreDamage = compound.getBoolean("IgnoreDamage");
		recipe.ignoreNBT = compound.getBoolean("IgnoreNBT");
		recipe.isGlobal = compound.getBoolean("Global");

		return recipe;
	}

	public int id = -1;
	public String name = "";
	public Availability availability = new Availability();
	public boolean isGlobal = false;
	public boolean ignoreDamage = false;

	public boolean ignoreNBT = false;

	public boolean savesRecipe = true;

	public RecipeCarpentry(int width, int height, ItemStack[] recipe, ItemStack result) {
		super(width, height, recipe, result);
	}

	public RecipeCarpentry(String name) {
		super(0, 0, new ItemStack[0], null);
		this.name = name;
	}

	private boolean checkMatch(InventoryCrafting par1InventoryCrafting, int par2, int par3, boolean par4) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				int var7 = i - par2;
				int var8 = j - par3;
				ItemStack var9 = null;
				if ((var7 >= 0) && (var8 >= 0) && (var7 < recipeWidth) && (var8 < recipeHeight)) {
					if (par4) {
						var9 = recipeItems[((recipeWidth - var7 - 1) + (var8 * recipeWidth))];
					} else {
						var9 = recipeItems[(var7 + (var8 * recipeWidth))];
					}
				}
				ItemStack var10 = par1InventoryCrafting.getStackInRowAndColumn(i, j);
				if (((var10 != null) || (var9 != null))
						&& (!NoppesUtilPlayer.compareItems(var9, var10, ignoreDamage, ignoreNBT))) {
					return false;
				}
			}
		}
		return true;
	}

	public void copy(RecipeCarpentry recipe) {
		id = recipe.id;
		name = recipe.name;
		availability = recipe.availability;
		isGlobal = recipe.isGlobal;
		ignoreDamage = recipe.ignoreDamage;
		ignoreNBT = recipe.ignoreNBT;
	}

	@Override
	public void delete() {
		RecipeController.instance.delete(id);
	}

	public ItemStack getCraftingItem(int i) {
		if ((recipeItems == null) || (i >= recipeItems.length)) {
			return null;
		}
		return recipeItems[i];
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		if (getRecipeOutput() == null) {
			return null;
		}
		return getRecipeOutput().copy();
	}

	@Override
	public int getHeight() {
		return recipeHeight;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public boolean getIgnoreDamage() {
		return ignoreDamage;
	}

	@Override
	public boolean getIgnoreNBT() {
		return ignoreNBT;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemStack[] getRecipe() {
		return recipeItems;
	}

	@Override
	public int getRecipeSize() {
		return 16;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting p_179532_1_) {
		ItemStack[] aitemstack = new ItemStack[p_179532_1_.getSizeInventory()];
		for (int i = 0; i < aitemstack.length; i++) {
			ItemStack itemstack = p_179532_1_.getStackInSlot(i);
			aitemstack[i] = ForgeHooks.getContainerItem(itemstack);
		}
		return aitemstack;
	}

	@Override
	public ItemStack getResult() {
		return getRecipeOutput();
	}

	@Override
	public int getWidth() {
		return recipeWidth;
	}

	@Override
	public boolean isGlobal() {
		return isGlobal;
	}

	public boolean isValid() {
		if ((recipeItems.length == 0) || (getRecipeOutput() == null)) {
			return false;
		}
		for (ItemStack item : recipeItems) {
			if (item != null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean matches(InventoryCrafting par1InventoryCrafting, World world) {
		for (int i = 0; i <= (4 - recipeWidth); i++) {
			for (int j = 0; j <= (4 - recipeHeight); j++) {
				if (checkMatch(par1InventoryCrafting, i, j, true)) {
					return true;
				}
				if (checkMatch(par1InventoryCrafting, i, j, false)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void save() {
		try {
			RecipeController.instance.saveRecipe(this);
		} catch (IOException e) {
		}
	}

	@Override
	public boolean saves() {
		return savesRecipe;
	}

	@Override
	public void saves(boolean bo) {
		savesRecipe = bo;
	}

	public void setCraftingItem(int i, ItemStack item) {
		if (i < recipeItems.length) {
			recipeItems[i] = item;
		}
	}

	@Override
	public void setIgnoreDamage(boolean bo) {
		ignoreDamage = bo;
	}

	@Override
	public void setIgnoreNBT(boolean bo) {
		ignoreNBT = bo;
	}

	@Override
	public void setIsGlobal(boolean bo) {
		isGlobal = bo;
	}

	public NBTTagCompound writeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("ID", id);
		compound.setInteger("Width", recipeWidth);
		compound.setInteger("Height", recipeHeight);
		if (getRecipeOutput() != null) {
			compound.setTag("Item", getRecipeOutput().writeToNBT(new NBTTagCompound()));
		}
		compound.setTag("Materials", NBTTags.nbtItemStackArray(recipeItems));
		compound.setTag("Availability", availability.writeToNBT(new NBTTagCompound()));
		compound.setString("Name", name);
		compound.setBoolean("Global", isGlobal);
		compound.setBoolean("IgnoreDamage", ignoreDamage);
		compound.setBoolean("IgnoreNBT", ignoreNBT);
		return compound;
	}
}

//

//

package noppes.npcs.controllers.recipies;

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
import noppes.npcs.controllers.Availability;

public class RecipeCarpentry extends ShapedRecipes implements IRecipe {
	public static RecipeCarpentry createRecipe(final RecipeCarpentry recipe, final ItemStack par1ItemStack,
			final Object... par2ArrayOfObj) {
		String var3 = "";
		int var4 = 0;
		int var5 = 0;
		int var6 = 0;
		if (par2ArrayOfObj[var4] instanceof String[]) {
			final String[] var8;
			final String[] var7 = var8 = (String[]) par2ArrayOfObj[var4++];
			for (int var9 = var7.length, var10 = 0; var10 < var9; ++var10) {
				final String var11 = var8[var10];
				++var6;
				var5 = var11.length();
				var3 += var11;
			}
		} else {
			while (par2ArrayOfObj[var4] instanceof String) {
				final String var12 = (String) par2ArrayOfObj[var4++];
				++var6;
				var5 = var12.length();
				var3 += var12;
			}
		}
		final HashMap<Character, ItemStack> var13 = new HashMap<Character, ItemStack>();
		while (var4 < par2ArrayOfObj.length) {
			final Character var14 = (Character) par2ArrayOfObj[var4];
			ItemStack var15 = null;
			if (par2ArrayOfObj[var4 + 1] instanceof Item) {
				var15 = new ItemStack((Item) par2ArrayOfObj[var4 + 1]);
			} else if (par2ArrayOfObj[var4 + 1] instanceof Block) {
				var15 = new ItemStack((Block) par2ArrayOfObj[var4 + 1], 1, -1);
			} else if (par2ArrayOfObj[var4 + 1] instanceof ItemStack) {
				var15 = (ItemStack) par2ArrayOfObj[var4 + 1];
			}
			var13.put(var14, var15);
			var4 += 2;
		}
		final ItemStack[] var16 = new ItemStack[var5 * var6];
		for (int var9 = 0; var9 < (var5 * var6); ++var9) {
			final char var17 = var3.charAt(var9);
			if (var13.containsKey(var17)) {
				var16[var9] = var13.get(var17).copy();
			} else {
				var16[var9] = null;
			}
		}
		final RecipeCarpentry newrecipe = new RecipeCarpentry(var5, var6, var16, par1ItemStack);
		newrecipe.copy(recipe);
		if ((var5 == 4) || (var6 == 4)) {
			newrecipe.isGlobal = false;
		}
		return newrecipe;
	}

	public static RecipeCarpentry read(final NBTTagCompound compound) {
		final RecipeCarpentry recipe = new RecipeCarpentry(compound.getInteger("Width"), compound.getInteger("Height"),
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

	public int id;
	public String name;
	public Availability availability;
	public boolean isGlobal;
	public boolean ignoreDamage;

	public boolean ignoreNBT;

	public boolean savesRecipe;

	public RecipeCarpentry(final int width, final int height, final ItemStack[] recipe, final ItemStack result) {
		super(width, height, recipe, result);
		id = -1;
		name = "";
		availability = new Availability();
		isGlobal = false;
		ignoreDamage = false;
		ignoreNBT = false;
		savesRecipe = true;
	}

	public RecipeCarpentry(final String name) {
		super(0, 0, new ItemStack[0], (ItemStack) null);
		id = -1;
		this.name = "";
		availability = new Availability();
		isGlobal = false;
		ignoreDamage = false;
		ignoreNBT = false;
		savesRecipe = true;
		this.name = name;
	}

	private boolean checkMatch(final InventoryCrafting par1InventoryCrafting, final int par2, final int par3,
			final boolean par4) {
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				final int var7 = i - par2;
				final int var8 = j - par3;
				ItemStack var9 = null;
				if ((var7 >= 0) && (var8 >= 0) && (var7 < recipeWidth) && (var8 < recipeHeight)) {
					if (par4) {
						var9 = recipeItems[(recipeWidth - var7 - 1) + (var8 * recipeWidth)];
					} else {
						var9 = recipeItems[var7 + (var8 * recipeWidth)];
					}
				}
				final ItemStack var10 = par1InventoryCrafting.getStackInRowAndColumn(i, j);
				if (((var10 != null) || (var9 != null))
						&& !NoppesUtilPlayer.compareItems(var9, var10, ignoreDamage, ignoreNBT)) {
					return false;
				}
			}
		}
		return true;
	}

	public void copy(final RecipeCarpentry recipe) {
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

	public ItemStack getCraftingItem(final int i) {
		if ((recipeItems == null) || (i >= recipeItems.length)) {
			return null;
		}
		return recipeItems[i];
	}

	@Override
	public ItemStack getCraftingResult(final InventoryCrafting var1) {
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
	public ItemStack[] getRemainingItems(final InventoryCrafting p_179532_1_) {
		final ItemStack[] aitemstack = new ItemStack[p_179532_1_.getSizeInventory()];
		for (int i = 0; i < aitemstack.length; ++i) {
			final ItemStack itemstack = p_179532_1_.getStackInSlot(i);
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
		for (final ItemStack item : recipeItems) {
			if (item != null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean matches(final InventoryCrafting par1InventoryCrafting, final World world) {
		for (int i = 0; i <= (4 - recipeWidth); ++i) {
			for (int j = 0; j <= (4 - recipeHeight); ++j) {
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
		} catch (IOException ex) {
		}
	}

	@Override
	public boolean saves() {
		return savesRecipe;
	}

	@Override
	public void saves(final boolean bo) {
		savesRecipe = bo;
	}

	public void setCraftingItem(final int i, final ItemStack item) {
		if (i < recipeItems.length) {
			recipeItems[i] = item;
		}
	}

	@Override
	public void setIgnoreDamage(final boolean bo) {
		ignoreDamage = bo;
	}

	@Override
	public void setIgnoreNBT(final boolean bo) {
		ignoreNBT = bo;
	}

	@Override
	public void setIsGlobal(final boolean bo) {
		isGlobal = bo;
	}

	public NBTTagCompound writeNBT() {
		final NBTTagCompound compound = new NBTTagCompound();
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

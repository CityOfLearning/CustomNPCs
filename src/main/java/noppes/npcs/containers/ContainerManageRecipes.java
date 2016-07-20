//

//

package noppes.npcs.containers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.controllers.recipies.RecipeCarpentry;

public class ContainerManageRecipes extends Container {
	private InventoryBasic craftingMatrix;
	public RecipeCarpentry recipe;
	public int size;
	public int width;

	public ContainerManageRecipes(EntityPlayer player, int size) {
		this.size = size * size;
		width = size;
		craftingMatrix = new InventoryBasic("crafting", false, this.size + 1);
		recipe = new RecipeCarpentry("");
		addSlotToContainer(new Slot(craftingMatrix, 0, 87, 61));
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				addSlotToContainer(new Slot(craftingMatrix, (i * width) + j + 1, (j * 18) + 8, (i * 18) + 35));
			}
		}
		for (int i2 = 0; i2 < 3; ++i2) {
			for (int l1 = 0; l1 < 9; ++l1) {
				addSlotToContainer(new Slot(player.inventory, l1 + (i2 * 9) + 9, 8 + (l1 * 18), 113 + (i2 * 18)));
			}
		}
		for (int j2 = 0; j2 < 9; ++j2) {
			addSlotToContainer(new Slot(player.inventory, j2, 8 + (j2 * 18), 171));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	public void saveRecipe() {
		int nextChar = 0;
		char[] chars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P' };
		Map<ItemStack, Character> nameMapping = new HashMap<ItemStack, Character>();
		int firstRow = width;
		int lastRow = 0;
		int firstColumn = width;
		int lastColumn = 0;
		boolean seenRow = false;
		for (int i = 0; i < width; ++i) {
			boolean seenColumn = false;
			for (int j = 0; j < width; ++j) {
				ItemStack item = craftingMatrix.getStackInSlot((i * width) + j + 1);
				if (item != null) {
					if (!seenColumn && (j < firstColumn)) {
						firstColumn = j;
					}
					if (j > lastColumn) {
						lastColumn = j;
					}
					seenColumn = true;
					Character letter = null;
					for (ItemStack mapped : nameMapping.keySet()) {
						if (NoppesUtilPlayer.compareItems(mapped, item, recipe.ignoreDamage, recipe.ignoreNBT)) {
							letter = nameMapping.get(mapped);
						}
					}
					if (letter == null) {
						letter = chars[nextChar];
						++nextChar;
						nameMapping.put(item, letter);
					}
				}
			}
			if (seenColumn) {
				if (!seenRow) {
					firstRow = i;
					lastRow = i;
					seenRow = true;
				} else {
					lastRow = i;
				}
			}
		}
		ArrayList<Object> recipe = new ArrayList<Object>();
		for (int k = 0; k < width; ++k) {
			if (k >= firstRow) {
				if (k <= lastRow) {
					String row = "";
					for (int l = 0; l < width; ++l) {
						if (l >= firstColumn) {
							if (l <= lastColumn) {
								ItemStack item2 = craftingMatrix.getStackInSlot((k * width) + l + 1);
								if (item2 == null) {
									row += " ";
								} else {
									for (ItemStack mapped : nameMapping.keySet()) {
										if (NoppesUtilPlayer.compareItems(mapped, item2, false, false)) {
											row += nameMapping.get(mapped);
										}
									}
								}
							}
						}
					}
					recipe.add(row);
				}
			}
		}
		if (nameMapping.isEmpty()) {
			RecipeCarpentry r = new RecipeCarpentry(this.recipe.name);
			r.copy(this.recipe);
			this.recipe = r;
			return;
		}
		for (ItemStack mapped2 : nameMapping.keySet()) {
			Character letter2 = nameMapping.get(mapped2);
			recipe.add(letter2);
			recipe.add(mapped2);
		}
		this.recipe = RecipeCarpentry.createRecipe(this.recipe, craftingMatrix.getStackInSlot(0), recipe.toArray());
	}

	public void setRecipe(RecipeCarpentry recipe) {
		craftingMatrix.setInventorySlotContents(0, recipe.getRecipeOutput());
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < width; ++j) {
				if (j >= recipe.recipeWidth) {
					craftingMatrix.setInventorySlotContents((i * width) + j + 1, (ItemStack) null);
				} else {
					craftingMatrix.setInventorySlotContents((i * width) + j + 1,
							recipe.getCraftingItem((i * recipe.recipeWidth) + j));
				}
			}
		}
		this.recipe = recipe;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i) {
		return null;
	}
}

//

//

package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCrate extends Container {
	public IInventory lowerChestInventory;
	public IInventory upperChestInventory;
	private int numRows;

	public ContainerCrate(final IInventory par1IInventory, final IInventory par2IInventory) {
		lowerChestInventory = par2IInventory;
		upperChestInventory = par1IInventory;
		numRows = par2IInventory.getSizeInventory() / 9;
		par2IInventory.openInventory((EntityPlayer) null);
		final int i = (numRows - 4) * 18;
		for (int j = 0; j < numRows; ++j) {
			for (int k = 0; k < 9; ++k) {
				addSlotToContainer(new Slot(par2IInventory, k + (j * 9), 8 + (k * 18), 18 + (j * 18)));
			}
		}
		for (int j = 0; j < 3; ++j) {
			for (int k = 0; k < 9; ++k) {
				addSlotToContainer(new Slot(par1IInventory, k + (j * 9) + 9, 8 + (k * 18), 103 + (j * 18) + i));
			}
		}
		for (int j = 0; j < 9; ++j) {
			addSlotToContainer(new Slot(par1IInventory, j, 8 + (j * 18), 161 + i));
		}
	}

	@Override
	public boolean canInteractWith(final EntityPlayer par1EntityPlayer) {
		return lowerChestInventory.isUseableByPlayer(par1EntityPlayer);
	}

	public IInventory getLowerChestInventory() {
		return lowerChestInventory;
	}

	@Override
	public void onContainerClosed(final EntityPlayer par1EntityPlayer) {
		super.onContainerClosed(par1EntityPlayer);
		lowerChestInventory.closeInventory(par1EntityPlayer);
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer par1EntityPlayer, final int par2) {
		ItemStack itemstack = null;
		final Slot slot = inventorySlots.get(par2);
		if ((slot != null) && slot.getHasStack()) {
			final ItemStack itemstack2 = slot.getStack();
			itemstack = itemstack2.copy();
			if (par2 < (numRows * 9)) {
				if (!mergeItemStack(itemstack2, numRows * 9, inventorySlots.size(), true)) {
					return null;
				}
			} else if (!mergeItemStack(itemstack2, 0, numRows * 9, false)) {
				return null;
			}
			if (itemstack2.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}
		}
		return itemstack;
	}
}


package noppes.npcs.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

class SlotNpcTraderItems extends Slot {
	public SlotNpcTraderItems(IInventory iinventory, int i, int j, int k) {
		super(iinventory, i, j, k);
	}

	@Override
	public int getSlotStackLimit() {
		return 64;
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return false;
	}

	public void onPickupFromSlot(ItemStack itemstack) {
		if (itemstack == null) {
			return;
		}
		if (getStack() == null) {
			return;
		}
		if (itemstack.getItem() != getStack().getItem()) {
			return;
		}
		--itemstack.stackSize;
	}
}

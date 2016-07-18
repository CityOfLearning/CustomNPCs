//

//

package noppes.npcs.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

class SlotNpcTraderItems extends Slot {
	public SlotNpcTraderItems(final IInventory iinventory, final int i, final int j, final int k) {
		super(iinventory, i, j, k);
	}

	@Override
	public int getSlotStackLimit() {
		return 64;
	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		return false;
	}

	public void onPickupFromSlot(final ItemStack itemstack) {
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

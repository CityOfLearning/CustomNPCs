//

//

package noppes.npcs.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotValid extends Slot {
	private boolean canPutIn;

	public SlotValid(final IInventory par1iInventory, final int par2, final int par3, final int par4) {
		super(par1iInventory, par2, par3, par4);
		canPutIn = true;
	}

	public SlotValid(final IInventory par1iInventory, final int par2, final int par3, final int par4,
			final boolean bo) {
		super(par1iInventory, par2, par3, par4);
		canPutIn = true;
		canPutIn = bo;
	}

	@Override
	public boolean isItemValid(final ItemStack par1ItemStack) {
		return canPutIn && inventory.isItemValidForSlot(0, par1ItemStack);
	}
}

//

//

package noppes.npcs.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotNpcBankCurrency extends Slot {
	public ItemStack item;

	public SlotNpcBankCurrency(ContainerNPCBankInterface containerplayer, IInventory iinventory, int i, int j, int k) {
		super(iinventory, i, j, k);
		item = null;
	}

	@Override
	public int getSlotStackLimit() {
		return 64;
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return (item != null) && ((item.getItem() == itemstack.getItem())
				&& (!item.getHasSubtypes() || (item.getItemDamage() == itemstack.getItemDamage())));
	}
}

//

//

package noppes.npcs.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import noppes.npcs.roles.RoleFollower;

class SlotNpcMercenaryCurrency extends Slot {
	RoleFollower role;

	public SlotNpcMercenaryCurrency(final RoleFollower role, final IInventory inv, final int i, final int j,
			final int k) {
		super(inv, i, j, k);
		this.role = role;
	}

	@Override
	public int getSlotStackLimit() {
		return 64;
	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		final Item item = itemstack.getItem();
		for (final ItemStack is : role.inventory.items.values()) {
			if (item == is.getItem()) {
				if (itemstack.getHasSubtypes() && (itemstack.getItemDamage() != is.getItemDamage())) {
					continue;
				}
				return true;
			}
		}
		return false;
	}
}

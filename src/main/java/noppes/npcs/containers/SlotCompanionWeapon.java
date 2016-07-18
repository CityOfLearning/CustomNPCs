//

//

package noppes.npcs.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.roles.RoleCompanion;

class SlotCompanionWeapon extends Slot {
	final RoleCompanion role;

	public SlotCompanionWeapon(final RoleCompanion role, final IInventory iinventory, final int id, final int x,
			final int y) {
		super(iinventory, id, x, y);
		this.role = role;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		return (itemstack != null) && role.canWearSword(new ItemStackWrapper(itemstack));
	}
}

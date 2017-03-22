
package noppes.npcs.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.ai.roles.RoleCompanion;
import noppes.npcs.api.wrapper.ItemStackWrapper;

class SlotCompanionWeapon extends Slot {
	RoleCompanion role;

	public SlotCompanionWeapon(RoleCompanion role, IInventory iinventory, int id, int x, int y) {
		super(iinventory, id, x, y);
		this.role = role;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return (itemstack != null) && role.canWearSword(new ItemStackWrapper(itemstack));
	}
}

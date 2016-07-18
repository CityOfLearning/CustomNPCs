//

//

package noppes.npcs.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.roles.RoleCompanion;

class SlotCompanionArmor extends Slot {
	final int armorType;
	final RoleCompanion role;

	public SlotCompanionArmor(final RoleCompanion role, final IInventory iinventory, final int id, final int x,
			final int y, final int type) {
		super(iinventory, id, x, y);
		armorType = type;
		this.role = role;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getSlotTexture() {
		return ItemArmor.EMPTY_SLOT_NAMES[armorType];
	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		if ((itemstack.getItem() instanceof ItemArmor) && role.canWearArmor(itemstack)) {
			return ((ItemArmor) itemstack.getItem()).armorType == armorType;
		}
		return (itemstack.getItem() instanceof ItemBlock) && (armorType == 0);
	}
}

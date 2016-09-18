
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
	int armorType;
	RoleCompanion role;

	public SlotCompanionArmor(RoleCompanion role, IInventory iinventory, int id, int x, int y, int type) {
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
	public boolean isItemValid(ItemStack itemstack) {
		if ((itemstack.getItem() instanceof ItemArmor) && role.canWearArmor(itemstack)) {
			return ((ItemArmor) itemstack.getItem()).armorType == armorType;
		}
		return (itemstack.getItem() instanceof ItemBlock) && (armorType == 0);
	}
}

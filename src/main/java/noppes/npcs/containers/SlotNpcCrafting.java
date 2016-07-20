//

//

package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SlotNpcCrafting extends SlotCrafting {
	private InventoryCrafting craftMatrix;

	public SlotNpcCrafting(EntityPlayer player, InventoryCrafting craftingInventory, IInventory p_i45790_3_,
			int slotIndex, int xPosition, int yPosition) {
		super(player, craftingInventory, p_i45790_3_, slotIndex, xPosition, yPosition);
		craftMatrix = craftingInventory;
	}

	@Override
	public void onPickupFromSlot(EntityPlayer p_82870_1_, ItemStack p_82870_2_) {
		FMLCommonHandler.instance().firePlayerCraftingEvent(p_82870_1_, p_82870_2_, craftMatrix);
		this.onCrafting(p_82870_2_);
		for (int i = 0; i < craftMatrix.getSizeInventory(); ++i) {
			ItemStack itemstack1 = craftMatrix.getStackInSlot(i);
			if (itemstack1 != null) {
				craftMatrix.decrStackSize(i, 1);
				if (itemstack1.getItem().hasContainerItem(itemstack1)) {
					ItemStack itemstack2 = itemstack1.getItem().getContainerItem(itemstack1);
					if ((itemstack2 != null) && itemstack2.isItemStackDamageable()
							&& (itemstack2.getItemDamage() > itemstack2.getMaxDamage())) {
						MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(p_82870_1_, itemstack2));
					} else if (!p_82870_1_.inventory.addItemStackToInventory(itemstack2)) {
						if (craftMatrix.getStackInSlot(i) == null) {
							craftMatrix.setInventorySlotContents(i, itemstack2);
						} else {
							p_82870_1_.dropPlayerItemWithRandomChoice(itemstack2, false);
						}
					}
				}
			}
		}
	}
}

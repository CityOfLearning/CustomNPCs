//

//

package noppes.npcs.containers;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerMerchantAdd extends ContainerNpcInterface {
	private IMerchant theMerchant;
	private InventoryBasic merchantInventory;
	private World theWorld;

	public ContainerMerchantAdd(EntityPlayer player, IMerchant par2IMerchant, World par3World) {
		super(player);
		theMerchant = par2IMerchant;
		theWorld = par3World;
		merchantInventory = new InventoryBasic("", false, 3);
		addSlotToContainer(new Slot(merchantInventory, 0, 36, 53));
		addSlotToContainer(new Slot(merchantInventory, 1, 62, 53));
		addSlotToContainer(new Slot(merchantInventory, 2, 120, 53));
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(player.inventory, j + (i * 9) + 9, 8 + (j * 18), 84 + (i * 18)));
			}
		}
		for (int i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + (i * 18), 142));
		}
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer) {
		super.onContainerClosed(par1EntityPlayer);
		theMerchant.setCustomer((EntityPlayer) null);
		super.onContainerClosed(par1EntityPlayer);
		if (!theWorld.isRemote) {
			ItemStack itemstack = merchantInventory.removeStackFromSlot(0);
			if (itemstack != null) {
				par1EntityPlayer.dropPlayerItemWithRandomChoice(itemstack, false);
			}
			itemstack = merchantInventory.removeStackFromSlot(1);
			if (itemstack != null) {
				par1EntityPlayer.dropPlayerItemWithRandomChoice(itemstack, false);
			}
		}
	}

	@Override
	public void onCraftGuiOpened(ICrafting par1ICrafting) {
		super.onCraftGuiOpened(par1ICrafting);
	}

	@Override
	public void onCraftMatrixChanged(IInventory par1IInventory) {
		super.onCraftMatrixChanged(par1IInventory);
	}

	public void setCurrentRecipeIndex(int par1) {
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
		ItemStack itemstack = null;
		Slot slot = inventorySlots.get(par2);
		if ((slot != null) && slot.getHasStack()) {
			ItemStack itemstack2 = slot.getStack();
			itemstack = itemstack2.copy();
			if ((par2 != 0) && (par2 != 1) && (par2 != 2)) {
				if ((par2 >= 3) && (par2 < 30)) {
					if (!mergeItemStack(itemstack2, 30, 39, false)) {
						return null;
					}
				} else if ((par2 >= 30) && (par2 < 39) && !mergeItemStack(itemstack2, 3, 30, false)) {
					return null;
				}
			} else if (!mergeItemStack(itemstack2, 3, 39, false)) {
				return null;
			}
			if (itemstack2.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}
			if (itemstack2.stackSize == itemstack.stackSize) {
				return null;
			}
			slot.onPickupFromSlot(par1EntityPlayer, itemstack2);
		}
		return itemstack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
	}
}

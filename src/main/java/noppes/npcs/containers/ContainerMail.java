
package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.mail.PlayerMail;
import noppes.npcs.controllers.mail.PlayerMailData;

public class ContainerMail extends ContainerNpcInterface {
	public static PlayerMail staticmail;
	static {
		ContainerMail.staticmail = new PlayerMail();
	}
	public PlayerMail mail;
	private boolean canEdit;

	public ContainerMail(EntityPlayer player, boolean canEdit, boolean canSend) {
		super(player);
		mail = new PlayerMail();
		mail = ContainerMail.staticmail;
		ContainerMail.staticmail = new PlayerMail();
		this.canEdit = canEdit;
		player.inventory.openInventory(player);
		for (int k = 0; k < 4; ++k) {
			addSlotToContainer(new SlotValid(mail, k, 179 + (k * 24), 138, canEdit));
		}
		for (int j = 0; j < 3; ++j) {
			for (int k = 0; k < 9; ++k) {
				addSlotToContainer(new Slot(player.inventory, k + (j * 9) + 9, 28 + (k * 18), 175 + (j * 18)));
			}
		}
		for (int j = 0; j < 9; ++j) {
			addSlotToContainer(new Slot(player.inventory, j, 28 + (j * 18), 230));
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		if (!canEdit && !player.worldObj.isRemote) {
			PlayerMailData data = PlayerDataController.instance.getPlayerData(player).mailData;
			for (PlayerMail mail : data.playermail) {
				if ((mail.time == this.mail.time) && mail.sender.equals(this.mail.sender)) {
					mail.readNBT(this.mail.writeNBT());
					break;
				}
			}
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
		ItemStack itemstack = null;
		Slot slot = inventorySlots.get(par2);
		if ((slot != null) && slot.getHasStack()) {
			ItemStack itemstack2 = slot.getStack();
			itemstack = itemstack2.copy();
			if (par2 < 4) {
				if (!mergeItemStack(itemstack2, 4, inventorySlots.size(), true)) {
					return null;
				}
			} else if (!canEdit || !mergeItemStack(itemstack2, 0, 4, false)) {
				return null;
			}
			if (itemstack2.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}
		}
		return itemstack;
	}
}

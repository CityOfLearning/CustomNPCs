//

//

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

	public ContainerMail(final EntityPlayer player, final boolean canEdit, final boolean canSend) {
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
	public void onContainerClosed(final EntityPlayer player) {
		super.onContainerClosed(player);
		if (!canEdit && !player.worldObj.isRemote) {
			final PlayerMailData data = PlayerDataController.instance.getPlayerData(player).mailData;
			for (final PlayerMail mail : data.playermail) {
				if ((mail.time == this.mail.time) && mail.sender.equals(this.mail.sender)) {
					mail.readNBT(this.mail.writeNBT());
					break;
				}
			}
		}
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer par1EntityPlayer, final int par2) {
		ItemStack itemstack = null;
		final Slot slot = inventorySlots.get(par2);
		if ((slot != null) && slot.getHasStack()) {
			final ItemStack itemstack2 = slot.getStack();
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

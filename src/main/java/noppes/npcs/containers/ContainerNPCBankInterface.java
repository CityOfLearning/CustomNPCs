
package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.bank.PlayerBankData;

public class ContainerNPCBankInterface extends ContainerNpcInterface {
	public InventoryNPC currencyMatrix;
	public SlotNpcBankCurrency currency;
	public int slot;
	public int bankid;
	private PlayerBankData data;

	public ContainerNPCBankInterface(EntityPlayer player, int slot, int bankid) {
		super(player);
		this.slot = 0;
		this.bankid = bankid;
		this.slot = slot;
		currencyMatrix = new InventoryNPC("currency", 1, this);
		if (!isAvailable() || canBeUpgraded()) {
			addSlotToContainer(currency = new SlotNpcBankCurrency(this, currencyMatrix, 0, 80, 29));
		}
		NpcMiscInventory items = new NpcMiscInventory(54);
		if (!player.worldObj.isRemote) {
			data = PlayerDataController.instance.getBankData(player, bankid);
			items = data.getBankOrDefault(bankid).itemSlots.get(slot);
		}
		int xOffset = xOffset();
		for (int j = 0; j < getRowNumber(); ++j) {
			for (int i1 = 0; i1 < 9; ++i1) {
				int id = i1 + (j * 9);
				addSlotToContainer(new Slot(items, id, 8 + (i1 * 18), 17 + xOffset + (j * 18)));
			}
		}
		if (isUpgraded()) {
			xOffset += 54;
		}
		for (int k = 0; k < 3; ++k) {
			for (int j2 = 0; j2 < 9; ++j2) {
				addSlotToContainer(
						new Slot(player.inventory, j2 + (k * 9) + 9, 8 + (j2 * 18), 86 + xOffset + (k * 18)));
			}
		}
		for (int l = 0; l < 9; ++l) {
			addSlotToContainer(new Slot(player.inventory, l, 8 + (l * 18), 144 + xOffset));
		}
	}

	public boolean canBeUpgraded() {
		return false;
	}

	public int getRowNumber() {
		return 0;
	}

	public boolean isAvailable() {
		return false;
	}

	public boolean isUpgraded() {
		return false;
	}

	@Override
	public void onContainerClosed(EntityPlayer entityplayer) {
		super.onContainerClosed(entityplayer);
		if (!entityplayer.worldObj.isRemote) {
			ItemStack var3 = currencyMatrix.getStackInSlot(0);
			currencyMatrix.setInventorySlotContents(0, null);
			if (var3 != null) {
				entityplayer.dropPlayerItemWithRandomChoice(var3, false);
			}
		}
	}

	@Override
	public void onCraftMatrixChanged(IInventory inv) {
	}

	public synchronized void setCurrency(ItemStack item) {
		currency.item = item;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i) {
		return null;
	}

	public int xOffset() {
		return 0;
	}
}

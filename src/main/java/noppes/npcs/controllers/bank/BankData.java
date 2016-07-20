//

//

package noppes.npcs.controllers.bank;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.Server;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.containers.ContainerNPCBankInterface;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.CustomNPCsScheduler;

public class BankData {
	public HashMap<Integer, NpcMiscInventory> itemSlots;
	public HashMap<Integer, Boolean> upgradedSlots;
	public int unlockedSlots;
	public int bankId;

	public BankData() {
		unlockedSlots = 0;
		bankId = -1;
		itemSlots = new HashMap<Integer, NpcMiscInventory>();
		upgradedSlots = new HashMap<Integer, Boolean>();
		for (int i = 0; i < 6; ++i) {
			itemSlots.put(i, new NpcMiscInventory(54));
			upgradedSlots.put(i, false);
		}
	}

	private ContainerNPCBankInterface getContainer(final EntityPlayer player) {
		final Container con = player.openContainer;
		if ((con == null) || !(con instanceof ContainerNPCBankInterface)) {
			return null;
		}
		return (ContainerNPCBankInterface) con;
	}

	private HashMap<Integer, NpcMiscInventory> getItemSlots(final NBTTagList tagList) {
		final HashMap<Integer, NpcMiscInventory> list = new HashMap<Integer, NpcMiscInventory>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			final int slot = nbttagcompound.getInteger("Slot");
			final NpcMiscInventory inv = new NpcMiscInventory(54);
			inv.setFromNBT(nbttagcompound.getCompoundTag("BankItems"));
			list.put(slot, inv);
		}
		return list;
	}

	public boolean isUpgraded(final Bank bank, final int slot) {
		return bank.isUpgraded(slot) || (bank.canBeUpgraded(slot) && upgradedSlots.get(slot));
	}

	private NBTTagList nbtItemSlots(final HashMap<Integer, NpcMiscInventory> items) {
		final NBTTagList list = new NBTTagList();
		for (final int slot : items.keySet()) {
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Slot", slot);
			nbttagcompound.setTag("BankItems", items.get(slot).getToNBT());
			list.appendTag(nbttagcompound);
		}
		return list;
	}

	public void openBankGui(final EntityPlayer player, final EntityNPCInterface npc, final int bankId, final int slot) {
		final Bank bank = BankController.getInstance().getBank(bankId);
		if (bank.getMaxSlots() <= slot) {
			return;
		}
		if (bank.startSlots > unlockedSlots) {
			unlockedSlots = bank.startSlots;
		}
		ItemStack currency = null;
		if (unlockedSlots <= slot) {
			currency = bank.currencyInventory.getStackInSlot(slot);
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerBankUnlock, npc, slot, bank.id, 0);
		} else if (isUpgraded(bank, slot)) {
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerBankLarge, npc, slot, bank.id, 0);
		} else if (bank.canBeUpgraded(slot)) {
			currency = bank.upgradeInventory.getStackInSlot(slot);
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerBankUprade, npc, slot, bank.id, 0);
		} else {
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerBankSmall, npc, slot, bank.id, 0);
		}
		final ItemStack item = currency;
		CustomNPCsScheduler.runTack(new Runnable() {
			@Override
			public void run() {
				final NBTTagCompound compound = new NBTTagCompound();
				compound.setInteger("MaxSlots", bank.getMaxSlots());
				compound.setInteger("UnlockedSlots", unlockedSlots);
				if (item != null) {
					compound.setTag("Currency", item.writeToNBT(new NBTTagCompound()));
					final ContainerNPCBankInterface container = BankData.this.getContainer(player);
					if (container != null) {
						container.setCurrency(item);
					}
				}
				Server.sendDataChecked((EntityPlayerMP) player, EnumPacketClient.GUI_DATA, compound);
			}
		}, 100);
	}

	public void readNBT(final NBTTagCompound nbttagcompound) {
		bankId = nbttagcompound.getInteger("DataBankId");
		unlockedSlots = nbttagcompound.getInteger("UnlockedSlots");
		itemSlots = getItemSlots(nbttagcompound.getTagList("BankInv", 10));
		upgradedSlots = NBTTags.getBooleanList(nbttagcompound.getTagList("UpdatedSlots", 10));
	}

	public void writeNBT(final NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("DataBankId", bankId);
		nbttagcompound.setInteger("UnlockedSlots", unlockedSlots);
		nbttagcompound.setTag("UpdatedSlots", NBTTags.nbtBooleanList(upgradedSlots));
		nbttagcompound.setTag("BankInv", nbtItemSlots(itemSlots));
	}
}

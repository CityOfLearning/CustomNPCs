//

//

package noppes.npcs.controllers;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NBTTags;
import noppes.npcs.NpcMiscInventory;

public class Bank {
	public int id;
	public String name;
	public HashMap<Integer, Integer> slotTypes;
	public int startSlots;
	public int maxSlots;
	public NpcMiscInventory currencyInventory;
	public NpcMiscInventory upgradeInventory;

	public Bank() {
		id = -1;
		name = "";
		startSlots = 1;
		maxSlots = 6;
		slotTypes = new HashMap<Integer, Integer>();
		currencyInventory = new NpcMiscInventory(6);
		upgradeInventory = new NpcMiscInventory(6);
		for (int i = 0; i < 6; ++i) {
			slotTypes.put(i, 0);
		}
	}

	public boolean canBeUpgraded(final int slot) {
		return (upgradeInventory.getStackInSlot(slot) != null)
				&& ((slotTypes.get(slot) == null) || (slotTypes.get(slot) == 0));
	}

	public int getMaxSlots() {
		for (int i = 0; i < maxSlots; ++i) {
			if ((currencyInventory.getStackInSlot(i) == null) && (i > (startSlots - 1))) {
				return i;
			}
		}
		return maxSlots;
	}

	public boolean isUpgraded(final int slot) {
		return (slotTypes.get(slot) != null) && (slotTypes.get(slot) == 2);
	}

	public void readEntityFromNBT(final NBTTagCompound nbttagcompound) {
		id = nbttagcompound.getInteger("BankID");
		name = nbttagcompound.getString("Username");
		startSlots = nbttagcompound.getInteger("StartSlots");
		maxSlots = nbttagcompound.getInteger("MaxSlots");
		slotTypes = NBTTags.getIntegerIntegerMap(nbttagcompound.getTagList("BankTypes", 10));
		currencyInventory.setFromNBT(nbttagcompound.getCompoundTag("BankCurrency"));
		upgradeInventory.setFromNBT(nbttagcompound.getCompoundTag("BankUpgrade"));
	}

	public void writeEntityToNBT(final NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("BankID", id);
		nbttagcompound.setTag("BankCurrency", currencyInventory.getToNBT());
		nbttagcompound.setTag("BankUpgrade", upgradeInventory.getToNBT());
		nbttagcompound.setString("Username", name);
		nbttagcompound.setInteger("MaxSlots", maxSlots);
		nbttagcompound.setInteger("StartSlots", startSlots);
		nbttagcompound.setTag("BankTypes", NBTTags.nbtIntegerIntegerMap(slotTypes));
	}
}

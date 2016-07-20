//

//

package noppes.npcs.controllers.bank;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerBankData {
	public HashMap<Integer, BankData> banks;

	public PlayerBankData() {
		banks = new HashMap<Integer, BankData>();
	}

	public BankData getBank(final int bankId) {
		return banks.get(bankId);
	}

	public BankData getBankOrDefault(final int bankId) {
		final BankData data = banks.get(bankId);
		if (data != null) {
			return data;
		}
		final Bank bank = BankController.getInstance().getBank(bankId);
		return banks.get(bank.id);
	}

	public boolean hasBank(final int bank) {
		return banks.containsKey(bank);
	}

	public void loadNBTData(final NBTTagCompound compound) {
		final HashMap<Integer, BankData> banks = new HashMap<Integer, BankData>();
		final NBTTagList list = compound.getTagList("BankData", 10);
		if (list == null) {
			return;
		}
		for (int i = 0; i < list.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
			final BankData data = new BankData();
			data.readNBT(nbttagcompound);
			banks.put(data.bankId, data);
		}
		this.banks = banks;
	}

	public void loadNew(final int bank) {
		final BankData data = new BankData();
		data.bankId = bank;
		banks.put(bank, data);
	}

	public void saveNBTData(final NBTTagCompound playerData) {
		final NBTTagList list = new NBTTagList();
		for (final BankData data : banks.values()) {
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			data.writeNBT(nbttagcompound);
			list.appendTag(nbttagcompound);
		}
		playerData.setTag("BankData", list);
	}
}

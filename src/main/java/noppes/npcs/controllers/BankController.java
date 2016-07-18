//

//

package noppes.npcs.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.CustomNpcs;

public class BankController {
	private static BankController instance;

	public static BankController getInstance() {
		if (newInstance()) {
			BankController.instance = new BankController();
		}
		return BankController.instance;
	}

	private static boolean newInstance() {
		if (BankController.instance == null) {
			return true;
		}
		final File file = CustomNpcs.getWorldSaveDirectory();
		return (file != null) && !BankController.instance.filePath.equals(file.getAbsolutePath());
	}

	public HashMap<Integer, Bank> banks;

	private String filePath;

	public BankController() {
		filePath = "";
		BankController.instance = this;
		banks = new HashMap<Integer, Bank>();
		this.loadBanks();
		if (banks.isEmpty()) {
			final Bank bank = new Bank();
			bank.id = 0;
			bank.name = "Default Bank";
			for (int i = 0; i < 6; ++i) {
				bank.slotTypes.put(i, 0);
			}
			banks.put(bank.id, bank);
		}
	}

	public Bank getBank(final int bankId) {
		final Bank bank = banks.get(bankId);
		if (bank != null) {
			return bank;
		}
		return banks.values().iterator().next();
	}

	public NBTTagCompound getNBT() {
		final NBTTagList list = new NBTTagList();
		for (final Bank bank : banks.values()) {
			final NBTTagCompound nbtfactions = new NBTTagCompound();
			bank.writeEntityToNBT(nbtfactions);
			list.appendTag(nbtfactions);
		}
		final NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setTag("Data", list);
		return nbttagcompound;
	}

	public int getUnusedId() {
		int id;
		for (id = 0; banks.containsKey(id); ++id) {
		}
		return id;
	}

	private void loadBanks() {
		final File saveDir = CustomNpcs.getWorldSaveDirectory();
		if (saveDir == null) {
			return;
		}
		filePath = saveDir.getAbsolutePath();
		try {
			final File file = new File(saveDir, "bank.dat");
			if (file.exists()) {
				this.loadBanks(file);
			}
		} catch (Exception e) {
			try {
				final File file2 = new File(saveDir, "bank.dat_old");
				if (file2.exists()) {
					this.loadBanks(file2);
				}
			} catch (Exception ex) {
			}
		}
	}

	private void loadBanks(final File file) throws IOException {
		this.loadBanks(CompressedStreamTools.readCompressed(new FileInputStream(file)));
	}

	public void loadBanks(final NBTTagCompound nbttagcompound1) throws IOException {
		final HashMap<Integer, Bank> banks = new HashMap<Integer, Bank>();
		final NBTTagList list = nbttagcompound1.getTagList("Data", 10);
		if (list != null) {
			for (int i = 0; i < list.tagCount(); ++i) {
				final NBTTagCompound nbttagcompound2 = list.getCompoundTagAt(i);
				final Bank bank = new Bank();
				bank.readEntityFromNBT(nbttagcompound2);
				banks.put(bank.id, bank);
			}
		}
		this.banks = banks;
	}

	public void removeBank(final int bank) {
		if ((bank < 0) || (banks.size() <= 1)) {
			return;
		}
		banks.remove(bank);
		saveBanks();
	}

	public void saveBank(final Bank bank) {
		if (bank.id < 0) {
			bank.id = getUnusedId();
		}
		banks.put(bank.id, bank);
		saveBanks();
	}

	public void saveBanks() {
		try {
			final File saveDir = CustomNpcs.getWorldSaveDirectory();
			final File file = new File(saveDir, "bank.dat_new");
			final File file2 = new File(saveDir, "bank.dat_old");
			final File file3 = new File(saveDir, "bank.dat");
			CompressedStreamTools.writeCompressed(getNBT(), new FileOutputStream(file));
			if (file2.exists()) {
				file2.delete();
			}
			file3.renameTo(file2);
			if (file3.exists()) {
				file3.delete();
			}
			file.renameTo(file3);
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

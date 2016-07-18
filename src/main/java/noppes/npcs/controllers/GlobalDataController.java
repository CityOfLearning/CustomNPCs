//

//

package noppes.npcs.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.CustomNpcs;

public class GlobalDataController {
	public static GlobalDataController instance;
	private int itemGiverId;

	public GlobalDataController() {
		itemGiverId = 0;
		(GlobalDataController.instance = this).load();
	}

	public int incrementItemGiverId() {
		++itemGiverId;
		saveData();
		return itemGiverId;
	}

	private void load() {
		final File saveDir = CustomNpcs.getWorldSaveDirectory();
		try {
			final File file = new File(saveDir, "global.dat");
			if (file.exists()) {
				loadData(file);
			}
		} catch (Exception e) {
			try {
				final File file2 = new File(saveDir, "global.dat_old");
				if (file2.exists()) {
					loadData(file2);
				}
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
	}

	private void loadData(final File file) throws Exception {
		final NBTTagCompound nbttagcompound1 = CompressedStreamTools.readCompressed(new FileInputStream(file));
		itemGiverId = nbttagcompound1.getInteger("itemGiverId");
	}

	public void saveData() {
		try {
			final File saveDir = CustomNpcs.getWorldSaveDirectory();
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("itemGiverId", itemGiverId);
			final File file = new File(saveDir, "global.dat_new");
			final File file2 = new File(saveDir, "global.dat_old");
			final File file3 = new File(saveDir, "global.dat");
			CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file));
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

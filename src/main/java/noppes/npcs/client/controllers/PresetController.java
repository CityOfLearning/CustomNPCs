
package noppes.npcs.client.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.CustomNpcs;

public class PresetController {
	public static PresetController instance;
	public HashMap<String, Preset> presets;
	private File dir;

	public PresetController(File dir) {
		presets = new HashMap<>();
		PresetController.instance = this;
		this.dir = dir;
		load();
	}

	public void addPreset(Preset preset) {
		while (presets.containsKey(preset.name.toLowerCase())) {
			preset.name += "_";
		}
		presets.put(preset.name.toLowerCase(), preset);
		save();
	}

	public Preset getPreset(String username) {
		if (presets.isEmpty()) {
			load();
		}
		return presets.get(username.toLowerCase());
	}

	public void load() {
		NBTTagCompound compound = loadPreset();
		HashMap<String, Preset> presets = new HashMap<>();
		if (compound != null) {
			NBTTagList list = compound.getTagList("Presets", 10);
			for (int i = 0; i < list.tagCount(); ++i) {
				NBTTagCompound comp = list.getCompoundTagAt(i);
				Preset preset = new Preset();
				preset.readFromNBT(comp);
				presets.put(preset.name.toLowerCase(), preset);
			}
		}
		Preset.FillDefault(presets);
		this.presets = presets;
	}

	private NBTTagCompound loadPreset() {
		String filename = "presets.dat";
		try {
			File file = new File(dir, filename);
			if (!file.exists()) {
				return null;
			}
			return CompressedStreamTools.readCompressed(new FileInputStream(file));
		} catch (Exception e) {
			CustomNpcs.logger.catching(e);
			try {
				File file = new File(dir, filename + "_old");
				if (!file.exists()) {
					return null;
				}
				return CompressedStreamTools.readCompressed(new FileInputStream(file));
			} catch (Exception e1) {
				CustomNpcs.logger.catching(e1);
				return null;
			}
		}
	}

	public void removePreset(String preset) {
		if (preset == null) {
			return;
		}
		presets.remove(preset.toLowerCase());
		save();
	}

	public void save() {
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for (Preset preset : presets.values()) {
			list.appendTag(preset.writeToNBT());
		}
		compound.setTag("Presets", list);
		savePreset(compound);
	}

	private void savePreset(NBTTagCompound compound) {
		String filename = "presets.dat";
		try {
			File file = new File(dir, filename + "_new");
			File file2 = new File(dir, filename + "_old");
			File file3 = new File(dir, filename);
			CompressedStreamTools.writeCompressed(compound, new FileOutputStream(file));
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
			CustomNpcs.logger.catching(e);
		}
	}
}

//

//

package noppes.npcs.client.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.LogWriter;

public class PresetController {
	public static PresetController instance;
	public HashMap<String, Preset> presets;
	private File dir;

	public PresetController(final File dir) {
		presets = new HashMap<String, Preset>();
		PresetController.instance = this;
		this.dir = dir;
		load();
	}

	public void addPreset(final Preset preset) {
		while (presets.containsKey(preset.name.toLowerCase())) {
			preset.name += "_";
		}
		presets.put(preset.name.toLowerCase(), preset);
		save();
	}

	public Preset getPreset(final String username) {
		if (presets.isEmpty()) {
			load();
		}
		return presets.get(username.toLowerCase());
	}

	public void load() {
		final NBTTagCompound compound = loadPreset();
		final HashMap<String, Preset> presets = new HashMap<String, Preset>();
		if (compound != null) {
			final NBTTagList list = compound.getTagList("Presets", 10);
			for (int i = 0; i < list.tagCount(); ++i) {
				final NBTTagCompound comp = list.getCompoundTagAt(i);
				final Preset preset = new Preset();
				preset.readFromNBT(comp);
				presets.put(preset.name.toLowerCase(), preset);
			}
		}
		Preset.FillDefault(presets);
		this.presets = presets;
	}

	private NBTTagCompound loadPreset() {
		final String filename = "presets.dat";
		try {
			final File file = new File(dir, filename);
			if (!file.exists()) {
				return null;
			}
			return CompressedStreamTools.readCompressed(new FileInputStream(file));
		} catch (Exception e) {
			LogWriter.except(e);
			try {
				final File file = new File(dir, filename + "_old");
				if (!file.exists()) {
					return null;
				}
				return CompressedStreamTools.readCompressed(new FileInputStream(file));
			} catch (Exception e1) {
				LogWriter.except(e1);
				return null;
			}
		}
	}

	public void removePreset(final String preset) {
		if (preset == null) {
			return;
		}
		presets.remove(preset.toLowerCase());
		save();
	}

	public void save() {
		final NBTTagCompound compound = new NBTTagCompound();
		final NBTTagList list = new NBTTagList();
		for (final Preset preset : presets.values()) {
			list.appendTag(preset.writeToNBT());
		}
		compound.setTag("Presets", list);
		savePreset(compound);
	}

	private void savePreset(final NBTTagCompound compound) {
		final String filename = "presets.dat";
		try {
			final File file = new File(dir, filename + "_new");
			final File file2 = new File(dir, filename + "_old");
			final File file3 = new File(dir, filename);
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
			LogWriter.except(e);
		}
	}
}

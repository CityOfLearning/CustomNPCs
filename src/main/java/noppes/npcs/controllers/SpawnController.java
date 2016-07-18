//

//

package noppes.npcs.controllers;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.GZIPInputStream;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.WeightedRandom;
import noppes.npcs.CustomNpcs;
import noppes.npcs.LogWriter;

public class SpawnController {
	public static SpawnController instance;
	public HashMap<String, List<SpawnData>> biomes;
	public ArrayList<SpawnData> data;
	public Random random;
	private int lastUsedID;

	public SpawnController() {
		biomes = new HashMap<String, List<SpawnData>>();
		data = new ArrayList<SpawnData>();
		random = new Random();
		lastUsedID = 0;
		(SpawnController.instance = this).loadData();
	}

	private void fillBiomeData() {
		final HashMap<String, List<SpawnData>> biomes = new HashMap<String, List<SpawnData>>();
		for (final SpawnData spawn : data) {
			for (final String s : spawn.biomes) {
				List<SpawnData> list = biomes.get(s);
				if (list == null) {
					biomes.put(s, list = new ArrayList<SpawnData>());
				}
				list.add(spawn);
			}
		}
		this.biomes = biomes;
	}

	public NBTTagCompound getNBT() {
		final NBTTagList list = new NBTTagList();
		for (final SpawnData spawn : data) {
			final NBTTagCompound nbtfactions = new NBTTagCompound();
			spawn.writeNBT(nbtfactions);
			list.appendTag(nbtfactions);
		}
		final NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setInteger("lastID", lastUsedID);
		nbttagcompound.setTag("NPCSpawnData", list);
		return nbttagcompound;
	}

	public SpawnData getRandomSpawnData(final String biome, final boolean isAir) {
		final List<SpawnData> list = getSpawnList(biome);
		if ((list == null) || list.isEmpty()) {
			return null;
		}
		return (SpawnData) WeightedRandom.getRandomItem(random, (Collection) list);
	}

	public Map<String, Integer> getScroll() {
		final Map<String, Integer> map = new HashMap<String, Integer>();
		for (final SpawnData spawn : data) {
			map.put(spawn.name, spawn.id);
		}
		return map;
	}

	public SpawnData getSpawnData(final int id) {
		for (final SpawnData spawn : data) {
			if (spawn.id == id) {
				return spawn;
			}
		}
		return null;
	}

	public List<SpawnData> getSpawnList(final String biome) {
		return biomes.get(biome);
	}

	public int getUnusedId() {
		return ++lastUsedID;
	}

	private void loadData() {
		final File saveDir = CustomNpcs.getWorldSaveDirectory();
		if (saveDir == null) {
			return;
		}
		try {
			final File file = new File(saveDir, "spawns.dat");
			if (file.exists()) {
				loadDataFile(file);
			}
		} catch (Exception e) {
			try {
				final File file2 = new File(saveDir, "spawns.dat_old");
				if (file2.exists()) {
					loadDataFile(file2);
				}
			} catch (Exception ex) {
			}
		}
	}

	public void loadData(final DataInputStream stream) throws IOException {
		final ArrayList<SpawnData> data = new ArrayList<SpawnData>();
		final NBTTagCompound nbttagcompound1 = CompressedStreamTools.read(stream);
		lastUsedID = nbttagcompound1.getInteger("lastID");
		final NBTTagList nbtlist = nbttagcompound1.getTagList("NPCSpawnData", 10);
		if (nbtlist != null) {
			for (int i = 0; i < nbtlist.tagCount(); ++i) {
				final NBTTagCompound nbttagcompound2 = nbtlist.getCompoundTagAt(i);
				final SpawnData spawn = new SpawnData();
				spawn.readNBT(nbttagcompound2);
				data.add(spawn);
			}
		}
		this.data = data;
		fillBiomeData();
	}

	private void loadDataFile(final File file) throws IOException {
		final DataInputStream var1 = new DataInputStream(
				new BufferedInputStream(new GZIPInputStream(new FileInputStream(file))));
		this.loadData(var1);
		var1.close();
	}

	public void removeSpawnData(final int id) {
		final ArrayList<SpawnData> data = new ArrayList<SpawnData>();
		for (final SpawnData spawn : this.data) {
			if (spawn.id == id) {
				continue;
			}
			data.add(spawn);
		}
		this.data = data;
		fillBiomeData();
		saveData();
	}

	public void saveData() {
		try {
			final File saveDir = CustomNpcs.getWorldSaveDirectory();
			final File file = new File(saveDir, "spawns.dat_new");
			final File file2 = new File(saveDir, "spawns.dat_old");
			final File file3 = new File(saveDir, "spawns.dat");
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
			LogWriter.except(e);
		}
	}

	public void saveSpawnData(final SpawnData spawn) {
		if (spawn.id < 0) {
			spawn.id = getUnusedId();
		}
		final SpawnData original = getSpawnData(spawn.id);
		if (original == null) {
			data.add(spawn);
		} else {
			original.readNBT(spawn.writeNBT(new NBTTagCompound()));
		}
		fillBiomeData();
		saveData();
	}
}

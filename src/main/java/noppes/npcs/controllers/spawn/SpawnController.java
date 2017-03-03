
package noppes.npcs.controllers.spawn;

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

public class SpawnController {
	public static SpawnController instance;
	public HashMap<String, List<SpawnData>> biomes;
	public ArrayList<SpawnData> data;
	public Random random;
	private int lastUsedID;

	public SpawnController() {
		biomes = new HashMap<>();
		data = new ArrayList<>();
		random = new Random();
		lastUsedID = 0;
		(SpawnController.instance = this).loadData();
	}

	private void fillBiomeData() {
		HashMap<String, List<SpawnData>> biomes = new HashMap<>();
		for (SpawnData spawn : data) {
			for (String s : spawn.biomes) {
				List<SpawnData> list = biomes.get(s);
				if (list == null) {
					biomes.put(s, list = new ArrayList<>());
				}
				list.add(spawn);
			}
		}
		this.biomes = biomes;
	}

	public NBTTagCompound getNBT() {
		NBTTagList list = new NBTTagList();
		for (SpawnData spawn : data) {
			NBTTagCompound nbtfactions = new NBTTagCompound();
			spawn.writeNBT(nbtfactions);
			list.appendTag(nbtfactions);
		}
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setInteger("lastID", lastUsedID);
		nbttagcompound.setTag("NPCSpawnData", list);
		return nbttagcompound;
	}

	public SpawnData getRandomSpawnData(String biome, boolean isAir) {
		List<SpawnData> list = getSpawnList(biome);
		if ((list == null) || list.isEmpty()) {
			return null;
		}
		return (SpawnData) WeightedRandom.getRandomItem(random, (Collection) list);
	}

	public Map<String, Integer> getScroll() {
		Map<String, Integer> map = new HashMap<>();
		for (SpawnData spawn : data) {
			map.put(spawn.name, spawn.id);
		}
		return map;
	}

	public SpawnData getSpawnData(int id) {
		for (SpawnData spawn : data) {
			if (spawn.id == id) {
				return spawn;
			}
		}
		return null;
	}

	public List<SpawnData> getSpawnList(String biome) {
		return biomes.get(biome);
	}

	public int getUnusedId() {
		return ++lastUsedID;
	}

	private void loadData() {
		File saveDir = CustomNpcs.getWorldSaveDirectory();
		if (saveDir == null) {
			return;
		}
		try {
			File file = new File(saveDir, "spawns.dat");
			if (file.exists()) {
				loadDataFile(file);
			}
		} catch (Exception e) {
			try {
				File file2 = new File(saveDir, "spawns.dat_old");
				if (file2.exists()) {
					loadDataFile(file2);
				}
			} catch (Exception ex) {
			}
		}
	}

	public void loadData(DataInputStream stream) throws IOException {
		ArrayList<SpawnData> data = new ArrayList<>();
		NBTTagCompound nbttagcompound1 = CompressedStreamTools.read(stream);
		lastUsedID = nbttagcompound1.getInteger("lastID");
		NBTTagList nbtlist = nbttagcompound1.getTagList("NPCSpawnData", 10);
		if (nbtlist != null) {
			for (int i = 0; i < nbtlist.tagCount(); ++i) {
				NBTTagCompound nbttagcompound2 = nbtlist.getCompoundTagAt(i);
				SpawnData spawn = new SpawnData();
				spawn.readNBT(nbttagcompound2);
				data.add(spawn);
			}
		}
		this.data = data;
		fillBiomeData();
	}

	private void loadDataFile(File file) throws IOException {
		DataInputStream var1 = new DataInputStream(
				new BufferedInputStream(new GZIPInputStream(new FileInputStream(file))));
		this.loadData(var1);
		var1.close();
	}

	public void removeSpawnData(int id) {
		ArrayList<SpawnData> data = new ArrayList<>();
		for (SpawnData spawn : this.data) {
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
			File saveDir = CustomNpcs.getWorldSaveDirectory();
			File file = new File(saveDir, "spawns.dat_new");
			File file2 = new File(saveDir, "spawns.dat_old");
			File file3 = new File(saveDir, "spawns.dat");
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
			CustomNpcs.logger.catching(e);
		}
	}

	public void saveSpawnData(SpawnData spawn) {
		if (spawn.id < 0) {
			spawn.id = getUnusedId();
		}
		SpawnData original = getSpawnData(spawn.id);
		if (original == null) {
			data.add(spawn);
		} else {
			original.readNBT(spawn.writeNBT(new NBTTagCompound()));
		}
		fillBiomeData();
		saveData();
	}
}

package noppes.npcs.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.CustomNpcs;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.NBTJsonUtil;
import noppes.npcs.util.NBTTags;

public class LinkedNpcController {
	public static class LinkedData {
		public String name;
		public long time;
		public NBTTagCompound data;

		public LinkedData() {
			name = "LinkedNpc";
			time = 0L;
			data = new NBTTagCompound();
			time = System.currentTimeMillis();
		}

		public NBTTagCompound getNBT() {
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("LinkedName", name);
			compound.setTag("NPCData", data);
			return compound;
		}

		public void setNBT(NBTTagCompound compound) {
			name = compound.getString("LinkedName");
			data = compound.getCompoundTag("NPCData");
		}
	}

	public static LinkedNpcController Instance;

	public List<LinkedData> list;

	public LinkedNpcController() {
		list = new ArrayList<LinkedData>();
		(LinkedNpcController.Instance = this).load();
	}

	public void addData(String name) {
		if ((getData(name) != null) || name.isEmpty()) {
			return;
		}
		LinkedData data = new LinkedData();
		data.name = name;
		list.add(data);
		save();
	}

	private void cleanTags(NBTTagCompound compound) {
		compound.removeTag("MovingPathNew");
	}

	public LinkedData getData(String name) {
		for (LinkedData data : list) {
			if (data.name.equalsIgnoreCase(name)) {
				return data;
			}
		}
		return null;
	}

	public File getDir() {
		File dir = new File(CustomNpcs.getWorldSaveDirectory(), "linkednpcs");
		if (!dir.exists()) {
			dir.mkdir();
		}
		return dir;
	}

	private void load() {
		try {
			loadNpcs();
		} catch (Exception e) {
			CustomNpcs.logger.catching(e);
		}
	}

	public void loadNpcData(EntityNPCInterface npc) {
		if (npc.linkedName.isEmpty()) {
			return;
		}
		LinkedData data = getData(npc.linkedName);
		if (data == null) {
			npc.linkedLast = 0L;
			npc.linkedName = "";
			npc.linkedData = null;
		} else {
			npc.linkedData = data;
			if ((npc.posX == 0.0) && (npc.posY == 0.0) && (npc.posX == 0.0)) {
				return;
			}
			npc.linkedLast = data.time;
			List<int[]> points = npc.ai.getMovingPath();
			NBTTagCompound compound = NBTTags.NBTMerge(readNpcData(npc), data.data);
			npc.display.readToNBT(compound);
			npc.stats.readToNBT(compound);
			npc.advanced.readToNBT(compound);
			npc.inventory.readEntityFromNBT(compound);
			if (compound.hasKey("ModelData")) {
				((EntityCustomNpc) npc).modelData.readFromNBT(compound.getCompoundTag("ModelData"));
			}
			npc.ai.readToNBT(compound);
			npc.transform.readToNBT(compound);
			npc.ai.setMovingPath(points);
			npc.updateClient = true;
		}
	}

	private void loadNpcs() {
		File dir = getDir();
		if (dir.exists()) {
			List<LinkedData> list = new ArrayList<LinkedData>();
			for (File file : dir.listFiles()) {
				if (file.getName().endsWith(".json")) {
					try {
						NBTTagCompound compound = NBTJsonUtil.LoadFile(file);
						LinkedData linked = new LinkedData();
						linked.setNBT(compound);
						list.add(linked);
					} catch (Exception e) {
						CustomNpcs.logger.error("Error loading: " + file.getAbsolutePath(), e);
					}
				}
			}
			this.list = list;
		}
	}

	private NBTTagCompound readNpcData(EntityNPCInterface npc) {
		NBTTagCompound compound = new NBTTagCompound();
		npc.display.writeToNBT(compound);
		npc.inventory.writeEntityToNBT(compound);
		npc.stats.writeToNBT(compound);
		npc.ai.writeToNBT(compound);
		npc.advanced.writeToNBT(compound);
		npc.transform.writeToNBT(compound);
		compound.setTag("ModelData", ((EntityCustomNpc) npc).modelData.writeToNBT());
		return compound;
	}

	public void removeData(String name) {
		Iterator<LinkedData> ita = list.iterator();
		while (ita.hasNext()) {
			if (ita.next().name.equalsIgnoreCase(name)) {
				ita.remove();
			}
		}
		save();
	}

	public void save() {
		for (LinkedData npc : list) {
			try {
				saveNpc(npc);
			} catch (IOException e) {
				CustomNpcs.logger.catching(e);
			}
		}
	}

	private void saveNpc(LinkedData npc) throws IOException {
		File file = new File(getDir(), npc.name + ".json_new");
		File file2 = new File(getDir(), npc.name + ".json");
		try {
			NBTJsonUtil.SaveFile(file, npc.getNBT());
			if (file2.exists()) {
				file2.delete();
			}
			file.renameTo(file2);
		} catch (NBTJsonUtil.JsonException e) {
			CustomNpcs.logger.catching(e);
		}
	}

	public void saveNpcData(EntityNPCInterface npc) {
		NBTTagCompound compound = readNpcData(npc);
		cleanTags(compound);
		if (npc.linkedData.data.equals(compound)) {
			return;
		}
		npc.linkedData.data = compound;
		npc.linkedData.time = System.currentTimeMillis();
		save();
	}
}

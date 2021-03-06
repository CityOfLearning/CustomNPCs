
package noppes.npcs.controllers.transport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.CustomNpcs;
import noppes.npcs.ai.roles.RoleTransporter;
import noppes.npcs.entity.EntityNPCInterface;

public class TransportController {
	private static TransportController instance;

	public static TransportController getInstance() {
		return TransportController.instance;
	}

	private HashMap<Integer, TransportLocation> locations;
	public HashMap<Integer, TransportCategory> categories;

	private int lastUsedID;

	public TransportController() {
		locations = new HashMap<>();
		categories = new HashMap<>();
		lastUsedID = 0;
		(TransportController.instance = this).loadCategories();
		if (categories.isEmpty()) {
			TransportCategory cat = new TransportCategory();
			cat.id = 1;
			cat.title = "Default";
			categories.put(cat.id, cat);
		}
	}

	private boolean containsCategoryName(String name) {
		name = name.toLowerCase();
		for (TransportCategory cat : categories.values()) {
			if (cat.title.toLowerCase().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsLocationName(String name) {
		name = name.toLowerCase();
		for (TransportLocation loc : locations.values()) {
			if (loc.name.toLowerCase().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public NBTTagCompound getNBT() {
		NBTTagList list = new NBTTagList();
		for (TransportCategory category : categories.values()) {
			NBTTagCompound compound = new NBTTagCompound();
			category.writeNBT(compound);
			list.appendTag(compound);
		}
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setInteger("lastID", lastUsedID);
		nbttagcompound.setTag("NPCTransportCategories", list);
		return nbttagcompound;
	}

	public TransportLocation getTransport(int transportId) {
		return locations.get(transportId);
	}

	public TransportLocation getTransport(String name) {
		for (TransportLocation loc : locations.values()) {
			if (loc.name.equals(name)) {
				return loc;
			}
		}
		return null;
	}

	private int getUniqueIdCategory() {
		int id = 0;
		for (int catid : categories.keySet()) {
			if (catid > id) {
				id = catid;
			}
		}
		return ++id;
	}

	private int getUniqueIdLocation() {
		if (lastUsedID == 0) {
			for (int catid : locations.keySet()) {
				if (catid > lastUsedID) {
					lastUsedID = catid;
				}
			}
		}
		return ++lastUsedID;
	}

	private void loadCategories() {
		File saveDir = CustomNpcs.getWorldSaveDirectory();
		if (saveDir == null) {
			return;
		}
		try {
			File file = new File(saveDir, "transport.dat");
			if (!file.exists()) {
				return;
			}
			this.loadCategories(file);
		} catch (IOException e) {
			try {
				File file2 = new File(saveDir, "transport.dat_old");
				if (!file2.exists()) {
					return;
				}
				this.loadCategories(file2);
			} catch (IOException ex) {
			}
		}
	}

	public void loadCategories(File file) throws IOException {
		HashMap<Integer, TransportLocation> locations = new HashMap<>();
		HashMap<Integer, TransportCategory> categories = new HashMap<>();
		NBTTagCompound nbttagcompound1 = CompressedStreamTools.readCompressed(new FileInputStream(file));
		lastUsedID = nbttagcompound1.getInteger("lastID");
		NBTTagList list = nbttagcompound1.getTagList("NPCTransportCategories", 10);
		if (list == null) {
			return;
		}
		for (int i = 0; i < list.tagCount(); ++i) {
			TransportCategory category = new TransportCategory();
			NBTTagCompound compound = list.getCompoundTagAt(i);
			category.readNBT(compound);
			for (TransportLocation location : category.locations.values()) {
				locations.put(location.id, location);
			}
			categories.put(category.id, category);
		}
		this.locations = locations;
		this.categories = categories;
	}

	public void removeCategory(int id) {
		if (categories.size() == 1) {
			return;
		}
		TransportCategory cat = categories.get(id);
		if (cat == null) {
			return;
		}
		for (int i : cat.locations.keySet()) {
			locations.remove(i);
		}
		categories.remove(id);
		saveCategories();
	}

	public TransportLocation removeLocation(int location) {
		TransportLocation loc = locations.get(location);
		if (loc == null) {
			return null;
		}
		loc.category.locations.remove(location);
		locations.remove(location);
		saveCategories();
		return loc;
	}

	public void saveCategories() {
		try {
			File saveDir = CustomNpcs.getWorldSaveDirectory();
			File file = new File(saveDir, "transport.dat_new");
			File file2 = new File(saveDir, "transport.dat_old");
			File file3 = new File(saveDir, "transport.dat");
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

	public void saveCategory(String name, int id) {
		if (id < 0) {
			id = getUniqueIdCategory();
		}
		if (categories.containsKey(id)) {
			TransportCategory category = categories.get(id);
			if (!category.title.equals(name)) {
				while (containsCategoryName(name)) {
					name += "_";
				}
				categories.get(id).title = name;
			}
		} else {
			while (containsCategoryName(name)) {
				name += "_";
			}
			TransportCategory category = new TransportCategory();
			category.id = id;
			category.title = name;
			categories.put(id, category);
		}
		saveCategories();
	}

	public TransportLocation saveLocation(int categoryId, NBTTagCompound compound, EntityPlayerMP player,
			EntityNPCInterface npc) {
		TransportCategory category = categories.get(categoryId);
		if ((category == null) || (npc.advanced.role != 4)) {
			return null;
		}
		RoleTransporter role = (RoleTransporter) npc.roleInterface;
		TransportLocation location = new TransportLocation();
		location.readNBT(compound);
		location.category = category;
		if (role.hasTransport()) {
			location.id = role.transportId;
		}
		if ((location.id < 0) || !locations.get(location.id).name.equals(location.name)) {
			while (containsLocationName(location.name)) {
				StringBuilder sb = new StringBuilder();
				TransportLocation transportLocation = location;
				transportLocation.name = sb.append(transportLocation.name).append("_").toString();
			}
		}
		if (location.id < 0) {
			location.id = getUniqueIdLocation();
		}
		category.locations.put(location.id, location);
		locations.put(location.id, location);
		saveCategories();
		return location;
	}

	public void setLocation(TransportLocation location) {
		if (locations.containsKey(location.id)) {
			for (TransportCategory cat : categories.values()) {
				cat.locations.remove(location.id);
			}
		}
		locations.put(location.id, location);
		location.category.locations.put(location.id, location);
	}
}

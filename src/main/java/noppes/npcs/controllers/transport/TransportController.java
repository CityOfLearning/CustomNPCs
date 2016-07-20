//

//

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
import noppes.npcs.LogWriter;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleTransporter;

public class TransportController {
	private static TransportController instance;

	public static TransportController getInstance() {
		return TransportController.instance;
	}

	private HashMap<Integer, TransportLocation> locations;
	public HashMap<Integer, TransportCategory> categories;

	private int lastUsedID;

	public TransportController() {
		locations = new HashMap<Integer, TransportLocation>();
		categories = new HashMap<Integer, TransportCategory>();
		lastUsedID = 0;
		(TransportController.instance = this).loadCategories();
		if (categories.isEmpty()) {
			final TransportCategory cat = new TransportCategory();
			cat.id = 1;
			cat.title = "Default";
			categories.put(cat.id, cat);
		}
	}

	private boolean containsCategoryName(String name) {
		name = name.toLowerCase();
		for (final TransportCategory cat : categories.values()) {
			if (cat.title.toLowerCase().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsLocationName(String name) {
		name = name.toLowerCase();
		for (final TransportLocation loc : locations.values()) {
			if (loc.name.toLowerCase().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public NBTTagCompound getNBT() {
		final NBTTagList list = new NBTTagList();
		for (final TransportCategory category : categories.values()) {
			final NBTTagCompound compound = new NBTTagCompound();
			category.writeNBT(compound);
			list.appendTag(compound);
		}
		final NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setInteger("lastID", lastUsedID);
		nbttagcompound.setTag("NPCTransportCategories", list);
		return nbttagcompound;
	}

	public TransportLocation getTransport(final int transportId) {
		return locations.get(transportId);
	}

	public TransportLocation getTransport(final String name) {
		for (final TransportLocation loc : locations.values()) {
			if (loc.name.equals(name)) {
				return loc;
			}
		}
		return null;
	}

	private int getUniqueIdCategory() {
		int id = 0;
		for (final int catid : categories.keySet()) {
			if (catid > id) {
				id = catid;
			}
		}
		return ++id;
	}

	private int getUniqueIdLocation() {
		if (lastUsedID == 0) {
			for (final int catid : locations.keySet()) {
				if (catid > lastUsedID) {
					lastUsedID = catid;
				}
			}
		}
		return ++lastUsedID;
	}

	private void loadCategories() {
		final File saveDir = CustomNpcs.getWorldSaveDirectory();
		if (saveDir == null) {
			return;
		}
		try {
			final File file = new File(saveDir, "transport.dat");
			if (!file.exists()) {
				return;
			}
			this.loadCategories(file);
		} catch (IOException e) {
			try {
				final File file2 = new File(saveDir, "transport.dat_old");
				if (!file2.exists()) {
					return;
				}
				this.loadCategories(file2);
			} catch (IOException ex) {
			}
		}
	}

	public void loadCategories(final File file) throws IOException {
		final HashMap<Integer, TransportLocation> locations = new HashMap<Integer, TransportLocation>();
		final HashMap<Integer, TransportCategory> categories = new HashMap<Integer, TransportCategory>();
		final NBTTagCompound nbttagcompound1 = CompressedStreamTools.readCompressed(new FileInputStream(file));
		lastUsedID = nbttagcompound1.getInteger("lastID");
		final NBTTagList list = nbttagcompound1.getTagList("NPCTransportCategories", 10);
		if (list == null) {
			return;
		}
		for (int i = 0; i < list.tagCount(); ++i) {
			final TransportCategory category = new TransportCategory();
			final NBTTagCompound compound = list.getCompoundTagAt(i);
			category.readNBT(compound);
			for (final TransportLocation location : category.locations.values()) {
				locations.put(location.id, location);
			}
			categories.put(category.id, category);
		}
		this.locations = locations;
		this.categories = categories;
	}

	public void removeCategory(final int id) {
		if (categories.size() == 1) {
			return;
		}
		final TransportCategory cat = categories.get(id);
		if (cat == null) {
			return;
		}
		for (final int i : cat.locations.keySet()) {
			locations.remove(i);
		}
		categories.remove(id);
		saveCategories();
	}

	public TransportLocation removeLocation(final int location) {
		final TransportLocation loc = locations.get(location);
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
			final File saveDir = CustomNpcs.getWorldSaveDirectory();
			final File file = new File(saveDir, "transport.dat_new");
			final File file2 = new File(saveDir, "transport.dat_old");
			final File file3 = new File(saveDir, "transport.dat");
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

	public void saveCategory(String name, int id) {
		if (id < 0) {
			id = getUniqueIdCategory();
		}
		if (categories.containsKey(id)) {
			final TransportCategory category = categories.get(id);
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
			final TransportCategory category = new TransportCategory();
			category.id = id;
			category.title = name;
			categories.put(id, category);
		}
		saveCategories();
	}

	public TransportLocation saveLocation(final int categoryId, final NBTTagCompound compound,
			final EntityPlayerMP player, final EntityNPCInterface npc) {
		final TransportCategory category = categories.get(categoryId);
		if ((category == null) || (npc.advanced.role != 4)) {
			return null;
		}
		final RoleTransporter role = (RoleTransporter) npc.roleInterface;
		final TransportLocation location = new TransportLocation();
		location.readNBT(compound);
		location.category = category;
		if (role.hasTransport()) {
			location.id = role.transportId;
		}
		if ((location.id < 0) || !locations.get(location.id).name.equals(location.name)) {
			while (containsLocationName(location.name)) {
				final StringBuilder sb = new StringBuilder();
				final TransportLocation transportLocation = location;
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

	public void setLocation(final TransportLocation location) {
		if (locations.containsKey(location.id)) {
			for (final TransportCategory cat : categories.values()) {
				cat.locations.remove(location.id);
			}
		}
		locations.put(location.id, location);
		location.category.locations.put(location.id, location);
	}
}

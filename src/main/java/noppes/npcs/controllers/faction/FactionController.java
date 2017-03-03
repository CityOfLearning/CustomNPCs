
package noppes.npcs.controllers.faction;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.CustomNpcs;
import noppes.npcs.EventHooks;
import noppes.npcs.api.handler.IFactionHandler;
import noppes.npcs.api.handler.data.IFaction;

public class FactionController implements IFactionHandler {
	private static FactionController instance;

	public static FactionController getInstance() {
		return FactionController.instance;
	}

	public HashMap<Integer, Faction> factions;

	private int lastUsedID;

	public FactionController() {
		lastUsedID = 0;
		FactionController.instance = this;
		factions = new HashMap<>();
		this.loadFactions();
		if (factions.isEmpty()) {
			factions.put(0, new Faction(0, "Friendly", 56576, 2000));
			factions.put(1, new Faction(1, "Neutral", 15916288, 1000));
			factions.put(2, new Faction(2, "Aggressive", 14483456, 0));
		}
		EventHooks.onGlobalFactionsLoaded(this);
	}

	@Override
	public IFaction create(String name, int color) {
		Faction faction = new Faction();
		while (hasName(name)) {
			name += "_";
		}
		faction.name = name;
		faction.color = color;
		saveFaction(faction);
		return faction;
	}

	@Override
	public IFaction delete(int id) {
		if ((id < 0) || (factions.size() <= 1)) {
			return null;
		}
		Faction faction = factions.remove(id);
		if (faction == null) {
			return null;
		}
		saveFactions();
		faction.id = -1;
		return faction;
	}

	public Faction getFaction(int faction) {
		return factions.get(faction);
	}

	public Faction getFactionFromName(String factioname) {
		for (Map.Entry<Integer, Faction> entryfaction : getInstance().factions.entrySet()) {
			if (entryfaction.getValue().name.equalsIgnoreCase(factioname)) {
				return entryfaction.getValue();
			}
		}
		return null;
	}

	public Faction getFirstFaction() {
		return factions.values().iterator().next();
	}

	public int getFirstFactionId() {
		return factions.keySet().iterator().next();
	}

	public String[] getNames() {
		String[] names = new String[factions.size()];
		int i = 0;
		for (Faction faction : factions.values()) {
			names[i] = faction.name.toLowerCase();
			++i;
		}
		return names;
	}

	public NBTTagCompound getNBT() {
		NBTTagList list = new NBTTagList();
		for (int slot : factions.keySet()) {
			Faction faction = factions.get(slot);
			NBTTagCompound nbtfactions = new NBTTagCompound();
			faction.writeNBT(nbtfactions);
			list.appendTag(nbtfactions);
		}
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setInteger("lastID", lastUsedID);
		nbttagcompound.setTag("NPCFactions", list);
		return nbttagcompound;
	}

	public int getUnusedId() {
		if (lastUsedID == 0) {
			for (int catid : factions.keySet()) {
				if (catid > lastUsedID) {
					lastUsedID = catid;
				}
			}
		}
		return ++lastUsedID;
	}

	public boolean hasName(String newName) {
		if (newName.trim().isEmpty()) {
			return true;
		}
		for (Faction faction : factions.values()) {
			if (faction.name.equals(newName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<IFaction> list() {
		return new ArrayList<>(factions.values());
	}

	private void loadFactions() {
		File saveDir = CustomNpcs.getWorldSaveDirectory();
		if (saveDir == null) {
			return;
		}
		try {
			File file = new File(saveDir, "factions.dat");
			if (file.exists()) {
				loadFactionsFile(file);
			}
		} catch (Exception e) {
			try {
				File file2 = new File(saveDir, "factions.dat_old");
				if (file2.exists()) {
					loadFactionsFile(file2);
				}
			} catch (Exception ex) {
			}
		}
	}

	public void loadFactions(DataInputStream stream) throws IOException {
		HashMap<Integer, Faction> factions = new HashMap<>();
		NBTTagCompound nbttagcompound1 = CompressedStreamTools.read(stream);
		lastUsedID = nbttagcompound1.getInteger("lastID");
		NBTTagList list = nbttagcompound1.getTagList("NPCFactions", 10);
		if (list != null) {
			for (int i = 0; i < list.tagCount(); ++i) {
				NBTTagCompound nbttagcompound2 = list.getCompoundTagAt(i);
				Faction faction = new Faction();
				faction.readNBT(nbttagcompound2);
				factions.put(faction.id, faction);
			}
		}
		this.factions = factions;
	}

	private void loadFactionsFile(File file) throws IOException {
		DataInputStream var1 = new DataInputStream(
				new BufferedInputStream(new GZIPInputStream(new FileInputStream(file))));
		this.loadFactions(var1);
		var1.close();
	}

	public void saveFaction(Faction faction) {
		if (faction.id < 0) {
			faction.id = getUnusedId();
			while (hasName(faction.name)) {
				faction.name += "_";
			}
		} else {
			Faction existing = factions.get(faction.id);
			if ((existing != null) && !existing.name.equals(faction.name)) {
				while (hasName(faction.name)) {
					faction.name += "_";
				}
			}
		}
		factions.remove(faction.id);
		factions.put(faction.id, faction);
		saveFactions();
	}

	public void saveFactions() {
		try {
			File saveDir = CustomNpcs.getWorldSaveDirectory();
			File file = new File(saveDir, "factions.dat_new");
			File file2 = new File(saveDir, "factions.dat_old");
			File file3 = new File(saveDir, "factions.dat");
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
}

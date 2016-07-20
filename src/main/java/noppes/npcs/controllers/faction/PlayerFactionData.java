//

//

package noppes.npcs.controllers.faction;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerFactionData {
	public HashMap<Integer, Integer> factionData;

	public PlayerFactionData() {
		factionData = new HashMap<Integer, Integer>();
	}

	public int getFactionPoints(final int id) {
		if (!factionData.containsKey(id)) {
			final Faction faction = FactionController.getInstance().getFaction(id);
			factionData.put(id, (faction == null) ? -1 : faction.defaultPoints);
		}
		return factionData.get(id);
	}

	public NBTTagCompound getPlayerGuiData() {
		final NBTTagCompound compound = new NBTTagCompound();
		saveNBTData(compound);
		final NBTTagList list = new NBTTagList();
		for (final int id : factionData.keySet()) {
			final Faction faction = FactionController.getInstance().getFaction(id);
			if (faction != null) {
				if (faction.hideFaction) {
					continue;
				}
				final NBTTagCompound com = new NBTTagCompound();
				faction.writeNBT(com);
				list.appendTag(com);
			}
		}
		compound.setTag("FactionList", list);
		return compound;
	}

	public void increasePoints(final int factionId, final int points) {
		if (!factionData.containsKey(factionId)) {
			final Faction faction = FactionController.getInstance().getFaction(factionId);
			factionData.put(factionId, (faction == null) ? -1 : faction.defaultPoints);
		}
		factionData.put(factionId, factionData.get(factionId) + points);
	}

	public void loadNBTData(final NBTTagCompound compound) {
		final HashMap<Integer, Integer> factionData = new HashMap<Integer, Integer>();
		if (compound == null) {
			return;
		}
		final NBTTagList list = compound.getTagList("FactionData", 10);
		if (list == null) {
			return;
		}
		for (int i = 0; i < list.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
			factionData.put(nbttagcompound.getInteger("Faction"), nbttagcompound.getInteger("Points"));
		}
		this.factionData = factionData;
	}

	public void saveNBTData(final NBTTagCompound compound) {
		final NBTTagList list = new NBTTagList();
		for (final int faction : factionData.keySet()) {
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Faction", faction);
			nbttagcompound.setInteger("Points", factionData.get(faction));
			list.appendTag(nbttagcompound);
		}
		compound.setTag("FactionData", list);
	}
}

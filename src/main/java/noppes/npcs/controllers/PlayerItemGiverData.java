
package noppes.npcs.controllers;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.ai.jobs.JobItemGiver;
import noppes.npcs.util.NBTTags;

public class PlayerItemGiverData {
	private HashMap<Integer, Long> itemgivers;
	private HashMap<Integer, Integer> chained;

	public PlayerItemGiverData() {
		itemgivers = new HashMap<>();
		chained = new HashMap<>();
	}

	public int getItemIndex(JobItemGiver jobItemGiver) {
		if (chained.containsKey(jobItemGiver.itemGiverId)) {
			return chained.get(jobItemGiver.itemGiverId);
		}
		return 0;
	}

	public long getTime(JobItemGiver jobItemGiver) {
		return itemgivers.get(jobItemGiver.itemGiverId);
	}

	public boolean hasInteractedBefore(JobItemGiver jobItemGiver) {
		return itemgivers.containsKey(jobItemGiver.itemGiverId);
	}

	public void loadNBTData(NBTTagCompound compound) {
		chained = NBTTags.getIntegerIntegerMap(compound.getTagList("ItemGiverChained", 10));
		itemgivers = NBTTags.getIntegerLongMap(compound.getTagList("ItemGiversList", 10));
	}

	public void saveNBTData(NBTTagCompound compound) {
		compound.setTag("ItemGiverChained", NBTTags.nbtIntegerIntegerMap(chained));
		compound.setTag("ItemGiversList", NBTTags.nbtIntegerLongMap(itemgivers));
	}

	public void setItemIndex(JobItemGiver jobItemGiver, int i) {
		chained.put(jobItemGiver.itemGiverId, i);
	}

	public void setTime(JobItemGiver jobItemGiver, long day) {
		itemgivers.put(jobItemGiver.itemGiverId, day);
	}
}

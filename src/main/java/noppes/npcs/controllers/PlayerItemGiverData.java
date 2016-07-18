//

//

package noppes.npcs.controllers;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NBTTags;
import noppes.npcs.roles.JobItemGiver;

public class PlayerItemGiverData {
	private HashMap<Integer, Long> itemgivers;
	private HashMap<Integer, Integer> chained;

	public PlayerItemGiverData() {
		itemgivers = new HashMap<Integer, Long>();
		chained = new HashMap<Integer, Integer>();
	}

	public int getItemIndex(final JobItemGiver jobItemGiver) {
		if (chained.containsKey(jobItemGiver.itemGiverId)) {
			return chained.get(jobItemGiver.itemGiverId);
		}
		return 0;
	}

	public long getTime(final JobItemGiver jobItemGiver) {
		return itemgivers.get(jobItemGiver.itemGiverId);
	}

	public boolean hasInteractedBefore(final JobItemGiver jobItemGiver) {
		return itemgivers.containsKey(jobItemGiver.itemGiverId);
	}

	public void loadNBTData(final NBTTagCompound compound) {
		chained = NBTTags.getIntegerIntegerMap(compound.getTagList("ItemGiverChained", 10));
		itemgivers = NBTTags.getIntegerLongMap(compound.getTagList("ItemGiversList", 10));
	}

	public void saveNBTData(final NBTTagCompound compound) {
		compound.setTag("ItemGiverChained", NBTTags.nbtIntegerIntegerMap(chained));
		compound.setTag("ItemGiversList", NBTTags.nbtIntegerLongMap(itemgivers));
	}

	public void setItemIndex(final JobItemGiver jobItemGiver, final int i) {
		chained.put(jobItemGiver.itemGiverId, i);
	}

	public void setTime(final JobItemGiver jobItemGiver, final long day) {
		itemgivers.put(jobItemGiver.itemGiverId, day);
	}
}

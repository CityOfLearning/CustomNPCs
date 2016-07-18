//

//

package noppes.npcs.controllers;

import net.minecraft.nbt.NBTTagCompound;

public class QuestData {
	public Quest quest;
	public boolean isCompleted;
	public NBTTagCompound extraData;

	public QuestData(final Quest quest) {
		extraData = new NBTTagCompound();
		this.quest = quest;
	}

	public void readEntityFromNBT(final NBTTagCompound nbttagcompound) {
		isCompleted = nbttagcompound.getBoolean("QuestCompleted");
		extraData = nbttagcompound.getCompoundTag("ExtraData");
	}

	public void writeEntityToNBT(final NBTTagCompound nbttagcompound) {
		nbttagcompound.setBoolean("QuestCompleted", isCompleted);
		nbttagcompound.setTag("ExtraData", extraData);
	}
}

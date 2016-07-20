//

//

package noppes.npcs.controllers.quest;

import net.minecraft.nbt.NBTTagCompound;

public class QuestData {
	public Quest quest;
	public boolean isCompleted;
	public NBTTagCompound extraData;

	public QuestData(Quest quest) {
		extraData = new NBTTagCompound();
		this.quest = quest;
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		isCompleted = nbttagcompound.getBoolean("QuestCompleted");
		extraData = nbttagcompound.getCompoundTag("ExtraData");
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setBoolean("QuestCompleted", isCompleted);
		nbttagcompound.setTag("ExtraData", extraData);
	}
}

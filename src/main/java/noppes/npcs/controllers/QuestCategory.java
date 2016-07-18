//

//

package noppes.npcs.controllers;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class QuestCategory {
	public HashMap<Integer, Quest> quests;
	public int id;
	public String title;

	public QuestCategory() {
		id = -1;
		title = "";
		quests = new HashMap<Integer, Quest>();
	}

	public void readNBT(final NBTTagCompound nbttagcompound) {
		id = nbttagcompound.getInteger("Slot");
		title = nbttagcompound.getString("Title");
		final NBTTagList dialogsList = nbttagcompound.getTagList("Dialogs", 10);
		if (dialogsList != null) {
			for (int ii = 0; ii < dialogsList.tagCount(); ++ii) {
				final NBTTagCompound nbttagcompound2 = dialogsList.getCompoundTagAt(ii);
				final Quest quest = new Quest();
				quest.readNBT(nbttagcompound2);
				quest.category = this;
				quests.put(quest.id, quest);
			}
		}
	}

	public NBTTagCompound writeNBT(final NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("Slot", id);
		nbttagcompound.setString("Title", title);
		final NBTTagList dialogs = new NBTTagList();
		for (final int dialogId : quests.keySet()) {
			final Quest quest = quests.get(dialogId);
			dialogs.appendTag(quest.writeToNBT(new NBTTagCompound()));
		}
		nbttagcompound.setTag("Dialogs", dialogs);
		return nbttagcompound;
	}
}

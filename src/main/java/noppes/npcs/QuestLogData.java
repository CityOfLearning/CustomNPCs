
package noppes.npcs;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.constants.EnumQuestCompletion;
import noppes.npcs.controllers.quest.PlayerQuestController;
import noppes.npcs.controllers.quest.Quest;
import noppes.npcs.util.NBTTags;

public class QuestLogData {
	public HashMap<String, Vector<String>> categories;
	public String selectedQuest;
	public String selectedCategory;
	public HashMap<String, String> questText;
	public HashMap<String, Vector<String>> questStatus;
	public HashMap<String, String> finish;

	public QuestLogData() {
		categories = new HashMap<>();
		selectedQuest = "";
		selectedCategory = "";
		questText = new HashMap<>();
		questStatus = new HashMap<>();
		finish = new HashMap<>();
	}

	public String getComplete() {
		return finish.get(selectedCategory + ":" + selectedQuest);
	}

	public Vector<String> getQuestStatus() {
		return questStatus.get(selectedCategory + ":" + selectedQuest);
	}

	public String getQuestText() {
		return questText.get(selectedCategory + ":" + selectedQuest);
	}

	public boolean hasSelectedQuest() {
		return !selectedQuest.isEmpty();
	}

	public void readNBT(NBTTagCompound compound) {
		categories = NBTTags.getVectorMap(compound.getTagList("Categories", 10));
		questText = NBTTags.getStringStringMap(compound.getTagList("Logs", 10));
		questStatus = NBTTags.getVectorMap(compound.getTagList("Status", 10));
		finish = NBTTags.getStringStringMap(compound.getTagList("QuestFinisher", 10));
	}

	public void setData(EntityPlayer player) {
		for (Quest quest : PlayerQuestController.getActiveQuests(player)) {
			String category = quest.category.title;
			if (!categories.containsKey(category)) {
				categories.put(category, new Vector<String>());
			}
			Vector<String> list = categories.get(category);
			list.add(quest.title);
			questText.put(category + ":" + quest.title, quest.logText);
			questStatus.put(category + ":" + quest.title, quest.questInterface.getQuestLogStatus(player));
			if ((quest.completion == EnumQuestCompletion.Npc) && quest.questInterface.isCompleted(player)) {
				finish.put(category + ":" + quest.title, quest.completerNpc);
			}
		}
	}

	public NBTTagCompound writeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setTag("Categories", NBTTags.nbtVectorMap(categories));
		compound.setTag("Logs", NBTTags.nbtStringStringMap(questText));
		compound.setTag("Status", NBTTags.nbtVectorMap(questStatus));
		compound.setTag("QuestFinisher", NBTTags.nbtStringStringMap(finish));
		return compound;
	}
}

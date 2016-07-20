//

//

package noppes.npcs.controllers.quest;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.EventHooks;
import noppes.npcs.Server;
import noppes.npcs.api.constants.EnumQuestType;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.constants.EnumQuestCompletion;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.quests.QuestInterface;

public class PlayerQuestData {
	public HashMap<Integer, QuestData> activeQuests;
	public HashMap<Integer, Long> finishedQuests;

	public PlayerQuestData() {
		activeQuests = new HashMap<Integer, QuestData>();
		finishedQuests = new HashMap<Integer, Long>();
	}

	public boolean checkQuestCompletion(EntityPlayer player, EnumQuestType type) {
		boolean bo = false;
		for (QuestData data : activeQuests.values()) {
			if ((data.quest.type != type) && (type != null)) {
				continue;
			}
			QuestInterface inter = data.quest.questInterface;
			if (inter.isCompleted(player)) {
				if (data.isCompleted) {
					continue;
				}
				if (!data.quest.complete(player, data)) {
					Server.sendData((EntityPlayerMP) player, EnumPacketClient.MESSAGE, "quest.completed",
							data.quest.title);
					Server.sendData((EntityPlayerMP) player, EnumPacketClient.CHAT, "quest.completed", ": ",
							data.quest.title);
				}
				data.isCompleted = true;
				bo = true;
				EventHooks.onQuestFinished(player, data.quest);
			} else {
				data.isCompleted = false;
			}
		}
		return bo;
	}

	public QuestData getQuestCompletion(EntityPlayer player, EntityNPCInterface npc) {
		for (QuestData data : activeQuests.values()) {
			Quest quest = data.quest;
			if ((quest != null) && (quest.completion == EnumQuestCompletion.Npc)
					&& quest.completerNpc.equals(npc.getName()) && quest.questInterface.isCompleted(player)) {
				return data;
			}
		}
		return null;
	}

	public void loadNBTData(NBTTagCompound mainCompound) {
		if (mainCompound == null) {
			return;
		}
		NBTTagCompound compound = mainCompound.getCompoundTag("QuestData");
		NBTTagList list = compound.getTagList("CompletedQuests", 10);
		if (list != null) {
			HashMap<Integer, Long> finishedQuests = new HashMap<Integer, Long>();
			for (int i = 0; i < list.tagCount(); ++i) {
				NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
				finishedQuests.put(nbttagcompound.getInteger("Quest"), nbttagcompound.getLong("Date"));
			}
			this.finishedQuests = finishedQuests;
		}
		NBTTagList list2 = compound.getTagList("ActiveQuests", 10);
		if (list2 != null) {
			HashMap<Integer, QuestData> activeQuests = new HashMap<Integer, QuestData>();
			for (int j = 0; j < list2.tagCount(); ++j) {
				NBTTagCompound nbttagcompound2 = list2.getCompoundTagAt(j);
				int id = nbttagcompound2.getInteger("Quest");
				Quest quest = QuestController.instance.quests.get(id);
				if (quest != null) {
					QuestData data = new QuestData(quest);
					data.readEntityFromNBT(nbttagcompound2);
					activeQuests.put(id, data);
				}
			}
			this.activeQuests = activeQuests;
		}
	}

	public void saveNBTData(NBTTagCompound maincompound) {
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for (int quest : finishedQuests.keySet()) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Quest", quest);
			nbttagcompound.setLong("Date", finishedQuests.get(quest));
			list.appendTag(nbttagcompound);
		}
		compound.setTag("CompletedQuests", list);
		NBTTagList list2 = new NBTTagList();
		for (int quest2 : activeQuests.keySet()) {
			NBTTagCompound nbttagcompound2 = new NBTTagCompound();
			nbttagcompound2.setInteger("Quest", quest2);
			activeQuests.get(quest2).writeEntityToNBT(nbttagcompound2);
			list2.appendTag(nbttagcompound2);
		}
		compound.setTag("ActiveQuests", list2);
		maincompound.setTag("QuestData", compound);
	}
}

//

//

package noppes.npcs.controllers;

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

	public boolean checkQuestCompletion(final EntityPlayer player, final EnumQuestType type) {
		boolean bo = false;
		for (final QuestData data : activeQuests.values()) {
			if ((data.quest.type != type) && (type != null)) {
				continue;
			}
			final QuestInterface inter = data.quest.questInterface;
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

	public QuestData getQuestCompletion(final EntityPlayer player, final EntityNPCInterface npc) {
		for (final QuestData data : activeQuests.values()) {
			final Quest quest = data.quest;
			if ((quest != null) && (quest.completion == EnumQuestCompletion.Npc)
					&& quest.completerNpc.equals(npc.getName()) && quest.questInterface.isCompleted(player)) {
				return data;
			}
		}
		return null;
	}

	public void loadNBTData(final NBTTagCompound mainCompound) {
		if (mainCompound == null) {
			return;
		}
		final NBTTagCompound compound = mainCompound.getCompoundTag("QuestData");
		final NBTTagList list = compound.getTagList("CompletedQuests", 10);
		if (list != null) {
			final HashMap<Integer, Long> finishedQuests = new HashMap<Integer, Long>();
			for (int i = 0; i < list.tagCount(); ++i) {
				final NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
				finishedQuests.put(nbttagcompound.getInteger("Quest"), nbttagcompound.getLong("Date"));
			}
			this.finishedQuests = finishedQuests;
		}
		final NBTTagList list2 = compound.getTagList("ActiveQuests", 10);
		if (list2 != null) {
			final HashMap<Integer, QuestData> activeQuests = new HashMap<Integer, QuestData>();
			for (int j = 0; j < list2.tagCount(); ++j) {
				final NBTTagCompound nbttagcompound2 = list2.getCompoundTagAt(j);
				final int id = nbttagcompound2.getInteger("Quest");
				final Quest quest = QuestController.instance.quests.get(id);
				if (quest != null) {
					final QuestData data = new QuestData(quest);
					data.readEntityFromNBT(nbttagcompound2);
					activeQuests.put(id, data);
				}
			}
			this.activeQuests = activeQuests;
		}
	}

	public void saveNBTData(final NBTTagCompound maincompound) {
		final NBTTagCompound compound = new NBTTagCompound();
		final NBTTagList list = new NBTTagList();
		for (final int quest : finishedQuests.keySet()) {
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Quest", quest);
			nbttagcompound.setLong("Date", finishedQuests.get(quest));
			list.appendTag(nbttagcompound);
		}
		compound.setTag("CompletedQuests", list);
		final NBTTagList list2 = new NBTTagList();
		for (final int quest2 : activeQuests.keySet()) {
			final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
			nbttagcompound2.setInteger("Quest", quest2);
			activeQuests.get(quest2).writeEntityToNBT(nbttagcompound2);
			list2.appendTag(nbttagcompound2);
		}
		compound.setTag("ActiveQuests", list2);
		maincompound.setTag("QuestData", compound);
	}
}

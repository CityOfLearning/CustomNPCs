//

//

package noppes.npcs.quests;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NBTTags;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.quest.PlayerQuestData;
import noppes.npcs.controllers.quest.QuestData;

public class QuestKill extends QuestInterface {
	public HashMap<String, Integer> targets;

	public QuestKill() {
		targets = new HashMap<String, Integer>();
	}

	public HashMap<String, Integer> getKilled(final QuestData data) {
		return NBTTags.getStringIntegerMap(data.extraData.getTagList("Killed", 10));
	}

	@Override
	public Vector<String> getQuestLogStatus(final EntityPlayer player) {
		final Vector<String> vec = new Vector<String>();
		final PlayerQuestData playerdata = PlayerDataController.instance.getPlayerData(player).questData;
		final QuestData data = playerdata.activeQuests.get(questId);
		if (data == null) {
			return vec;
		}
		final HashMap<String, Integer> killed = getKilled(data);
		for (final String entityName : targets.keySet()) {
			int amount = 0;
			if (killed.containsKey(entityName)) {
				amount = killed.get(entityName);
			}
			final String state = amount + "/" + targets.get(entityName);
			vec.add(entityName + ": " + state);
		}
		return vec;
	}

	@Override
	public void handleComplete(final EntityPlayer player) {
	}

	@Override
	public boolean isCompleted(final EntityPlayer player) {
		final PlayerQuestData playerdata = PlayerDataController.instance.getPlayerData(player).questData;
		final QuestData data = playerdata.activeQuests.get(questId);
		if (data == null) {
			return false;
		}
		final HashMap<String, Integer> killed = getKilled(data);
		if (killed.size() != targets.size()) {
			return false;
		}
		for (final String entity : killed.keySet()) {
			if (!targets.containsKey(entity) || (targets.get(entity) > killed.get(entity))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void readEntityFromNBT(final NBTTagCompound compound) {
		targets = NBTTags.getStringIntegerMap(compound.getTagList("QuestDialogs", 10));
	}

	public void setKilled(final QuestData data, final HashMap<String, Integer> killed) {
		data.extraData.setTag("Killed", NBTTags.nbtStringIntegerMap(killed));
	}

	@Override
	public void writeEntityToNBT(final NBTTagCompound compound) {
		compound.setTag("QuestDialogs", NBTTags.nbtStringIntegerMap(targets));
	}
}

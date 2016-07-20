//

//

package noppes.npcs.quests;

import java.util.Vector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.quest.PlayerQuestData;
import noppes.npcs.controllers.quest.QuestData;

public class QuestLocation extends QuestInterface {
	public String location;
	public String location2;
	public String location3;

	public QuestLocation() {
		location = "";
		location2 = "";
		location3 = "";
	}

	public boolean getFound(QuestData data, int i) {
		if (i == 1) {
			return data.extraData.getBoolean("LocationFound");
		}
		if (i == 2) {
			return data.extraData.getBoolean("Location2Found");
		}
		if (i == 3) {
			return data.extraData.getBoolean("Location3Found");
		}
		return (location.isEmpty() || data.extraData.getBoolean("LocationFound"))
				&& (location2.isEmpty() || data.extraData.getBoolean("Location2Found"))
				&& (location3.isEmpty() || data.extraData.getBoolean("Location3Found"));
	}

	@Override
	public Vector<String> getQuestLogStatus(EntityPlayer player) {
		Vector<String> vec = new Vector<String>();
		PlayerQuestData playerdata = PlayerDataController.instance.getPlayerData(player).questData;
		QuestData data = playerdata.activeQuests.get(questId);
		if (data == null) {
			return vec;
		}
		String found = StatCollector.translateToLocal("quest.found");
		String notfound = StatCollector.translateToLocal("quest.notfound");
		if (!location.isEmpty()) {
			vec.add(location + ": " + (getFound(data, 1) ? found : notfound));
		}
		if (!location2.isEmpty()) {
			vec.add(location2 + ": " + (getFound(data, 2) ? found : notfound));
		}
		if (!location3.isEmpty()) {
			vec.add(location3 + ": " + (getFound(data, 3) ? found : notfound));
		}
		return vec;
	}

	@Override
	public void handleComplete(EntityPlayer player) {
	}

	@Override
	public boolean isCompleted(EntityPlayer player) {
		PlayerQuestData playerdata = PlayerDataController.instance.getPlayerData(player).questData;
		QuestData data = playerdata.activeQuests.get(questId);
		return (data != null) && getFound(data, 0);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		location = compound.getString("QuestLocation");
		location2 = compound.getString("QuestLocation2");
		location3 = compound.getString("QuestLocation3");
	}

	public boolean setFound(QuestData data, String location) {
		if (location.equalsIgnoreCase(this.location) && !data.extraData.getBoolean("LocationFound")) {
			data.extraData.setBoolean("LocationFound", true);
			return true;
		}
		if (location.equalsIgnoreCase(location2) && !data.extraData.getBoolean("LocationFound2")) {
			data.extraData.setBoolean("Location2Found", true);
			return true;
		}
		if (location.equalsIgnoreCase(location3) && !data.extraData.getBoolean("LocationFound3")) {
			data.extraData.setBoolean("Location3Found", true);
			return true;
		}
		return false;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setString("QuestLocation", location);
		compound.setString("QuestLocation2", location2);
		compound.setString("QuestLocation3", location3);
	}
}

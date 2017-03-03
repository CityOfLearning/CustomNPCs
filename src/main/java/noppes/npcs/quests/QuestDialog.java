
package noppes.npcs.quests;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.dialog.Dialog;
import noppes.npcs.controllers.dialog.DialogController;
import noppes.npcs.util.NBTTags;

public class QuestDialog extends QuestInterface {
	public HashMap<Integer, Integer> dialogs;

	public QuestDialog() {
		dialogs = new HashMap<>();
	}

	@Override
	public Vector<String> getQuestLogStatus(EntityPlayer player) {
		Vector<String> vec = new Vector<>();
		for (int dialogId : dialogs.values()) {
			Dialog dialog = DialogController.instance.dialogs.get(dialogId);
			if (dialog == null) {
				continue;
			}
			String title = dialog.title;
			if (PlayerDataController.instance.getPlayerData(player).dialogData.dialogsRead.contains(dialogId)) {
				title += " (read)";
			} else {
				title += " (unread)";
			}
			vec.add(title);
		}
		return vec;
	}

	@Override
	public void handleComplete(EntityPlayer player) {
	}

	@Override
	public boolean isCompleted(EntityPlayer player) {
		for (int dialogId : dialogs.values()) {
			if (!PlayerDataController.instance.getPlayerData(player).dialogData.dialogsRead.contains(dialogId)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		dialogs = NBTTags.getIntegerIntegerMap(compound.getTagList("QuestDialogs", 10));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setTag("QuestDialogs", NBTTags.nbtIntegerIntegerMap(dialogs));
	}
}

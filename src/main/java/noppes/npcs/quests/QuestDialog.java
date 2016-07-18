//

//

package noppes.npcs.quests;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NBTTags;
import noppes.npcs.controllers.Dialog;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.PlayerDataController;

public class QuestDialog extends QuestInterface {
	public HashMap<Integer, Integer> dialogs;

	public QuestDialog() {
		dialogs = new HashMap<Integer, Integer>();
	}

	@Override
	public Vector<String> getQuestLogStatus(final EntityPlayer player) {
		final Vector<String> vec = new Vector<String>();
		for (final int dialogId : dialogs.values()) {
			final Dialog dialog = DialogController.instance.dialogs.get(dialogId);
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
	public void handleComplete(final EntityPlayer player) {
	}

	@Override
	public boolean isCompleted(final EntityPlayer player) {
		for (final int dialogId : dialogs.values()) {
			if (!PlayerDataController.instance.getPlayerData(player).dialogData.dialogsRead.contains(dialogId)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void readEntityFromNBT(final NBTTagCompound compound) {
		dialogs = NBTTags.getIntegerIntegerMap(compound.getTagList("QuestDialogs", 10));
	}

	@Override
	public void writeEntityToNBT(final NBTTagCompound compound) {
		compound.setTag("QuestDialogs", NBTTags.nbtIntegerIntegerMap(dialogs));
	}
}

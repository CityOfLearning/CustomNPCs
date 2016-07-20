//

//

package noppes.npcs.roles;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.constants.EnumOptionType;
import noppes.npcs.api.entity.data.role.IRoleDialog;
import noppes.npcs.controllers.dialog.Dialog;
import noppes.npcs.controllers.dialog.DialogOption;
import noppes.npcs.controllers.quest.PlayerQuestController;
import noppes.npcs.controllers.quest.Quest;
import noppes.npcs.controllers.quest.QuestController;
import noppes.npcs.entity.EntityNPCInterface;

public class RoleDialog extends RoleInterface implements IRoleDialog {
	public String dialog;
	public int questId;
	public HashMap<Integer, String> options;
	public HashMap<Integer, String> optionsTexts;

	public RoleDialog(final EntityNPCInterface npc) {
		super(npc);
		dialog = "";
		questId = -1;
		options = new HashMap<Integer, String>();
		optionsTexts = new HashMap<Integer, String>();
	}

	@Override
	public String getDialog() {
		return dialog;
	}

	@Override
	public String getOption(final int option) {
		return options.get(option);
	}

	@Override
	public String getOptionDialog(final int option) {
		return optionsTexts.get(option);
	}

	@Override
	public void interact(final EntityPlayer player) {
		if (dialog.isEmpty()) {
			npc.say(player, npc.advanced.getInteractLine());
		} else {
			final Dialog d = new Dialog();
			d.text = dialog;
			for (final Map.Entry<Integer, String> entry : options.entrySet()) {
				if (entry.getValue().isEmpty()) {
					continue;
				}
				final DialogOption option = new DialogOption();
				final String text = optionsTexts.get(entry.getKey());
				if ((text != null) && !text.isEmpty()) {
					option.optionType = EnumOptionType.ROLE_OPTION;
				} else {
					option.optionType = EnumOptionType.QUIT_OPTION;
				}
				option.title = entry.getValue();
				d.options.put(entry.getKey(), option);
			}
			NoppesUtilServer.openDialog(player, npc, d);
		}
		final Quest quest = QuestController.instance.quests.get(questId);
		if (quest != null) {
			PlayerQuestController.addActiveQuest(quest, player);
		}
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		questId = compound.getInteger("RoleQuestId");
		dialog = compound.getString("RoleDialog");
		options = NBTTags.getIntegerStringMap(compound.getTagList("RoleOptions", 10));
		optionsTexts = NBTTags.getIntegerStringMap(compound.getTagList("RoleOptionTexts", 10));
	}

	@Override
	public void setDialog(final String text) {
		dialog = text;
	}

	@Override
	public void setOption(final int option, final String text) {
		if ((option < 1) || (option > 6)) {
			throw new CustomNPCsException("Wrong dialog option slot given: " + option, new Object[0]);
		}
		options.put(option, text);
	}

	@Override
	public void setOptionDialog(final int option, final String text) {
		if ((option < 1) || (option > 6)) {
			throw new CustomNPCsException("Wrong dialog option slot given: " + option, new Object[0]);
		}
		optionsTexts.put(option, text);
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		compound.setInteger("RoleQuestId", questId);
		compound.setString("RoleDialog", dialog);
		compound.setTag("RoleOptions", NBTTags.nbtIntegerStringMap(options));
		compound.setTag("RoleOptionTexts", NBTTags.nbtIntegerStringMap(optionsTexts));
		return compound;
	}
}

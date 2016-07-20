//

//

package noppes.npcs.controllers.dialog;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.constants.EnumOptionType;
import noppes.npcs.api.handler.data.IDialogOption;

public class DialogOption implements IDialogOption {
	public int dialogId;
	public String title;
	public EnumOptionType optionType;
	public int optionColor;
	public String command;
	public int slot;

	public DialogOption() {
		dialogId = -1;
		title = "Talk";
		optionType = EnumOptionType.DIALOG_OPTION;
		optionColor = 14737632;
		command = "";
		slot = -1;
	}

	public Dialog getDialog() {
		if (!hasDialog()) {
			return null;
		}
		return DialogController.instance.dialogs.get(dialogId);
	}

	@Override
	public String getName() {
		return title;
	}

	@Override
	public int getSlot() {
		return slot;
	}

	@Override
	public EnumOptionType getType() {
		return optionType;
	}

	public boolean hasDialog() {
		if (dialogId <= 0) {
			return false;
		}
		if (!DialogController.instance.hasDialog(dialogId)) {
			dialogId = -1;
			return false;
		}
		return true;
	}

	public boolean isAvailable(EntityPlayer player) {
		Dialog dialog = getDialog();
		return (dialog != null) && dialog.availability.isAvailable(player);
	}

	public void readNBT(NBTTagCompound compound) {
		if (compound == null) {
			return;
		}
		title = compound.getString("Title");
		dialogId = compound.getInteger("Dialog");
		optionColor = compound.getInteger("DialogColor");
		optionType = EnumOptionType.values()[compound.getInteger("OptionType")];
		command = compound.getString("DialogCommand");
		if (optionColor == 0) {
			optionColor = 14737632;
		}
	}

	public NBTTagCompound writeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("Title", title);
		compound.setInteger("OptionType", optionType.ordinal());
		compound.setInteger("Dialog", dialogId);
		compound.setInteger("DialogColor", optionColor);
		compound.setString("DialogCommand", command);
		return compound;
	}
}

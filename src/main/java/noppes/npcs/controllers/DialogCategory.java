//

//

package noppes.npcs.controllers;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class DialogCategory {
	public int id;
	public String title;
	public HashMap<Integer, Dialog> dialogs;

	public DialogCategory() {
		id = -1;
		title = "";
		dialogs = new HashMap<Integer, Dialog>();
	}

	public void readNBT(final NBTTagCompound compound) {
		id = compound.getInteger("Slot");
		title = compound.getString("Title");
		final NBTTagList dialogsList = compound.getTagList("Dialogs", 10);
		if (dialogsList != null) {
			for (int ii = 0; ii < dialogsList.tagCount(); ++ii) {
				final Dialog dialog = new Dialog();
				dialog.category = this;
				final NBTTagCompound comp = dialogsList.getCompoundTagAt(ii);
				dialog.readNBT(comp);
				dialog.id = comp.getInteger("DialogId");
				dialogs.put(dialog.id, dialog);
			}
		}
	}

	public NBTTagCompound writeNBT(final NBTTagCompound nbtfactions) {
		nbtfactions.setInteger("Slot", id);
		nbtfactions.setString("Title", title);
		final NBTTagList dialogs = new NBTTagList();
		for (final Dialog dialog : this.dialogs.values()) {
			dialogs.appendTag(dialog.writeToNBT(new NBTTagCompound()));
		}
		nbtfactions.setTag("Dialogs", dialogs);
		return nbtfactions;
	}
}

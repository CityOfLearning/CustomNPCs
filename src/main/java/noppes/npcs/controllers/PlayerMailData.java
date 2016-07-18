//

//

package noppes.npcs.controllers;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerMailData {
	public ArrayList<PlayerMail> playermail;

	public PlayerMailData() {
		playermail = new ArrayList<PlayerMail>();
	}

	public boolean hasMail() {
		for (final PlayerMail mail : playermail) {
			if (!mail.beenRead) {
				return true;
			}
		}
		return false;
	}

	public void loadNBTData(final NBTTagCompound compound) {
		final ArrayList<PlayerMail> newmail = new ArrayList<PlayerMail>();
		final NBTTagList list = compound.getTagList("MailData", 10);
		if (list == null) {
			return;
		}
		for (int i = 0; i < list.tagCount(); ++i) {
			final PlayerMail mail = new PlayerMail();
			mail.readNBT(list.getCompoundTagAt(i));
			newmail.add(mail);
		}
		playermail = newmail;
	}

	public NBTTagCompound saveNBTData(final NBTTagCompound compound) {
		final NBTTagList list = new NBTTagList();
		for (final PlayerMail mail : playermail) {
			list.appendTag(mail.writeNBT());
		}
		compound.setTag("MailData", list);
		return compound;
	}
}

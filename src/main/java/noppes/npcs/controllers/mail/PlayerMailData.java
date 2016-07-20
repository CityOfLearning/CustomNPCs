//

//

package noppes.npcs.controllers.mail;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerMailData {
	public ArrayList<PlayerMail> playermail;

	public PlayerMailData() {
		playermail = new ArrayList<PlayerMail>();
	}

	public boolean hasMail() {
		for (PlayerMail mail : playermail) {
			if (!mail.beenRead) {
				return true;
			}
		}
		return false;
	}

	public void loadNBTData(NBTTagCompound compound) {
		ArrayList<PlayerMail> newmail = new ArrayList<PlayerMail>();
		NBTTagList list = compound.getTagList("MailData", 10);
		if (list == null) {
			return;
		}
		for (int i = 0; i < list.tagCount(); ++i) {
			PlayerMail mail = new PlayerMail();
			mail.readNBT(list.getCompoundTagAt(i));
			newmail.add(mail);
		}
		playermail = newmail;
	}

	public NBTTagCompound saveNBTData(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		for (PlayerMail mail : playermail) {
			list.appendTag(mail.writeNBT());
		}
		compound.setTag("MailData", list);
		return compound;
	}
}

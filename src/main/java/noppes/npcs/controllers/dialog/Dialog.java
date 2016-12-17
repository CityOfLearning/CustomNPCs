
package noppes.npcs.controllers.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.ICompatibilty;
import noppes.npcs.api.constants.EnumOptionType;
import noppes.npcs.api.handler.data.IDialog;
import noppes.npcs.api.handler.data.IDialogOption;
import noppes.npcs.controllers.Availability;
import noppes.npcs.controllers.faction.FactionOptions;
import noppes.npcs.controllers.mail.PlayerMail;
import noppes.npcs.controllers.quest.Quest;
import noppes.npcs.controllers.quest.QuestController;
import noppes.npcs.util.VersionCompatibility;

public class Dialog implements ICompatibilty, IDialog {
	public int version;
	public int id;
	public String title;
	public String text;
	public int quest;
	public DialogCategory category;
	public HashMap<Integer, DialogOption> options;
	public Availability availability;
	public FactionOptions factionOptions;
	public String sound;
	public String command;
	public PlayerMail mail;
	public boolean hideNPC;
	public boolean showWheel;
	public boolean disableEsc;

	public Dialog() {
		version = VersionCompatibility.ModRev;
		id = -1;
		title = "";
		text = "";
		quest = -1;
		options = new HashMap<Integer, DialogOption>();
		availability = new Availability();
		factionOptions = new FactionOptions();
		command = "";
		mail = new PlayerMail();
		hideNPC = false;
		showWheel = false;
		disableEsc = false;
	}

	public Dialog copy(EntityPlayer player) {
		Dialog dialog = new Dialog();
		dialog.id = id;
		dialog.text = text;
		dialog.title = title;
		dialog.category = category;
		dialog.quest = quest;
		dialog.sound = sound;
		dialog.mail = mail;
		dialog.command = command;
		dialog.hideNPC = hideNPC;
		dialog.showWheel = showWheel;
		dialog.disableEsc = disableEsc;
		for (int slot : options.keySet()) {
			DialogOption option = options.get(slot);
			if (option.optionType == EnumOptionType.DIALOG_OPTION) {
				if (!option.hasDialog()) {
					continue;
				}
				if (!option.isAvailable(player)) {
					continue;
				}
			}
			dialog.options.put(slot, option);
		}
		return dialog;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return title;
	}

	@Override
	public List<IDialogOption> getOptions() {
		return new ArrayList<IDialogOption>(options.values());
	}

	@Override
	public Quest getQuest() {
		if (QuestController.instance == null) {
			return null;
		}
		return QuestController.instance.quests.get(quest);
	}

	@Override
	public int getVersion() {
		return version;
	}

	public boolean hasDialogs(EntityPlayer player) {
		for (DialogOption option : options.values()) {
			if ((option != null) && (option.optionType == EnumOptionType.DIALOG_OPTION) && option.hasDialog()
					&& option.isAvailable(player)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasOtherOptions() {
		for (DialogOption option : options.values()) {
			if ((option != null) && (option.optionType != EnumOptionType.DISABLED)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasQuest() {
		return getQuest() != null;
	}

	public void readNBT(NBTTagCompound compound) {
		id = compound.getInteger("DialogId");
		readNBTPartial(compound);
	}

	public void readNBTPartial(NBTTagCompound compound) {
		version = compound.getInteger("ModRev");
		VersionCompatibility.CheckAvailabilityCompatibility(this, compound);
		title = compound.getString("DialogTitle");
		text = compound.getString("DialogText");
		quest = compound.getInteger("DialogQuest");
		sound = compound.getString("DialogSound");
		command = compound.getString("DialogCommand");
		mail.readNBT(compound.getCompoundTag("DialogMail"));
		hideNPC = compound.getBoolean("DialogHideNPC");
		showWheel = compound.getBoolean("DialogShowWheel");
		disableEsc = compound.getBoolean("DialogDisableEsc");
		NBTTagList options = compound.getTagList("Options", 10);
		HashMap<Integer, DialogOption> newoptions = new HashMap<Integer, DialogOption>();
		for (int iii = 0; iii < options.tagCount(); ++iii) {
			NBTTagCompound option = options.getCompoundTagAt(iii);
			int opslot = option.getInteger("OptionSlot");
			DialogOption dia = new DialogOption();
			dia.readNBT(option.getCompoundTag("Option"));
			newoptions.put(opslot, dia);
			dia.slot = opslot;
		}
		this.options = newoptions;
		availability.readFromNBT(compound);
		factionOptions.readFromNBT(compound);
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("DialogId", id);
		return writeToNBTPartial(compound);
	}

	public NBTTagCompound writeToNBTPartial(NBTTagCompound compound) {
		compound.setString("DialogTitle", title);
		compound.setString("DialogText", text);
		compound.setInteger("DialogQuest", quest);
		compound.setString("DialogCommand", command);
		compound.setTag("DialogMail", mail.writeNBT());
		compound.setBoolean("DialogHideNPC", hideNPC);
		compound.setBoolean("DialogShowWheel", showWheel);
		compound.setBoolean("DialogDisableEsc", disableEsc);
		if ((sound != null) && !sound.isEmpty()) {
			compound.setString("DialogSound", sound);
		}
		NBTTagList options = new NBTTagList();
		for (int opslot : this.options.keySet()) {
			NBTTagCompound listcompound = new NBTTagCompound();
			listcompound.setInteger("OptionSlot", opslot);
			listcompound.setTag("Option", this.options.get(opslot).writeNBT());
			options.appendTag(listcompound);
		}
		compound.setTag("Options", options);
		availability.writeToNBT(compound);
		factionOptions.writeToNBT(compound);
		compound.setInteger("ModRev", version);
		return compound;
	}
}

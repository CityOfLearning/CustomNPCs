
package noppes.npcs.controllers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.ICompatibilty;
import noppes.npcs.VersionCompatibility;
import noppes.npcs.constants.EnumAvailabilityDialog;
import noppes.npcs.constants.EnumAvailabilityFaction;
import noppes.npcs.constants.EnumAvailabilityFactionType;
import noppes.npcs.constants.EnumAvailabilityQuest;
import noppes.npcs.constants.EnumDayTime;
import noppes.npcs.controllers.faction.Faction;
import noppes.npcs.controllers.faction.FactionController;
import noppes.npcs.controllers.faction.PlayerFactionData;
import noppes.npcs.controllers.quest.PlayerQuestController;

public class Availability implements ICompatibilty {
	public int version;
	public EnumAvailabilityDialog dialogAvailable;
	public EnumAvailabilityDialog dialog2Available;
	public EnumAvailabilityDialog dialog3Available;
	public EnumAvailabilityDialog dialog4Available;
	public int dialogId;
	public int dialog2Id;
	public int dialog3Id;
	public int dialog4Id;
	public EnumAvailabilityQuest questAvailable;
	public EnumAvailabilityQuest quest2Available;
	public EnumAvailabilityQuest quest3Available;
	public EnumAvailabilityQuest quest4Available;
	public int questId;
	public int quest2Id;
	public int quest3Id;
	public int quest4Id;
	public EnumDayTime daytime;
	public int factionId;
	public int faction2Id;
	public EnumAvailabilityFactionType factionAvailable;
	public EnumAvailabilityFactionType faction2Available;
	public EnumAvailabilityFaction factionStance;
	public EnumAvailabilityFaction faction2Stance;
	public int minPlayerLevel;

	public Availability() {
		version = VersionCompatibility.ModRev;
		dialogAvailable = EnumAvailabilityDialog.Always;
		dialog2Available = EnumAvailabilityDialog.Always;
		dialog3Available = EnumAvailabilityDialog.Always;
		dialog4Available = EnumAvailabilityDialog.Always;
		dialogId = -1;
		dialog2Id = -1;
		dialog3Id = -1;
		dialog4Id = -1;
		questAvailable = EnumAvailabilityQuest.Always;
		quest2Available = EnumAvailabilityQuest.Always;
		quest3Available = EnumAvailabilityQuest.Always;
		quest4Available = EnumAvailabilityQuest.Always;
		questId = -1;
		quest2Id = -1;
		quest3Id = -1;
		quest4Id = -1;
		daytime = EnumDayTime.Always;
		factionId = -1;
		faction2Id = -1;
		factionAvailable = EnumAvailabilityFactionType.Always;
		faction2Available = EnumAvailabilityFactionType.Always;
		factionStance = EnumAvailabilityFaction.Friendly;
		faction2Stance = EnumAvailabilityFaction.Friendly;
		minPlayerLevel = 0;
	}

	public boolean dialogAvailable(int id, EnumAvailabilityDialog en, EntityPlayer player) {
		if (en == EnumAvailabilityDialog.Always) {
			return true;
		}
		boolean hasRead = PlayerDataController.instance.getPlayerData(player).dialogData.dialogsRead.contains(id);
		return (hasRead && (en == EnumAvailabilityDialog.After)) || (!hasRead && (en == EnumAvailabilityDialog.Before));
	}

	private boolean factionAvailable(int id, EnumAvailabilityFaction stance, EnumAvailabilityFactionType available,
			EntityPlayer player) {
		if (available == EnumAvailabilityFactionType.Always) {
			return true;
		}
		Faction faction = FactionController.getInstance().getFaction(id);
		if (faction == null) {
			return true;
		}
		PlayerFactionData data = PlayerDataController.instance.getPlayerData(player).factionData;
		int points = data.getFactionPoints(id);
		EnumAvailabilityFaction current = EnumAvailabilityFaction.Neutral;
		if (faction.neutralPoints >= points) {
			current = EnumAvailabilityFaction.Hostile;
		}
		if (faction.friendlyPoints < points) {
			current = EnumAvailabilityFaction.Friendly;
		}
		return ((available == EnumAvailabilityFactionType.Is) && (stance == current))
				|| ((available == EnumAvailabilityFactionType.IsNot) && (stance != current));
	}

	@Override
	public int getVersion() {
		return version;
	}

	public boolean isAvailable(EntityPlayer player) {
		if (daytime == EnumDayTime.Day) {
			long time = player.worldObj.getWorldTime() % 24000L;
			if (time > 12000L) {
				return false;
			}
		}
		if (daytime == EnumDayTime.Night) {
			long time = player.worldObj.getWorldTime() % 24000L;
			if (time < 12000L) {
				return false;
			}
		}
		return dialogAvailable(dialogId, dialogAvailable, player)
				&& dialogAvailable(dialog2Id, dialog2Available, player)
				&& dialogAvailable(dialog3Id, dialog3Available, player)
				&& dialogAvailable(dialog4Id, dialog4Available, player)
				&& questAvailable(questId, questAvailable, player) && questAvailable(quest2Id, quest2Available, player)
				&& questAvailable(quest3Id, quest3Available, player)
				&& questAvailable(quest4Id, quest4Available, player)
				&& factionAvailable(factionId, factionStance, factionAvailable, player)
				&& factionAvailable(faction2Id, faction2Stance, faction2Available, player)
				&& (player.experienceLevel >= minPlayerLevel);
	}

	public boolean questAvailable(int id, EnumAvailabilityQuest en, EntityPlayer player) {
		return (en == EnumAvailabilityQuest.Always)
				|| ((en == EnumAvailabilityQuest.After) && PlayerQuestController.isQuestFinished(player, id))
				|| ((en == EnumAvailabilityQuest.Before) && !PlayerQuestController.isQuestFinished(player, id))
				|| ((en == EnumAvailabilityQuest.Active) && PlayerQuestController.isQuestActive(player, id))
				|| ((en == EnumAvailabilityQuest.NotActive) && !PlayerQuestController.isQuestActive(player, id));
	}

	public void readFromNBT(NBTTagCompound compound) {
		version = compound.getInteger("ModRev");
		VersionCompatibility.CheckAvailabilityCompatibility(this, compound);
		dialogAvailable = EnumAvailabilityDialog.values()[compound.getInteger("AvailabilityDialog")];
		dialog2Available = EnumAvailabilityDialog.values()[compound.getInteger("AvailabilityDialog2")];
		dialog3Available = EnumAvailabilityDialog.values()[compound.getInteger("AvailabilityDialog3")];
		dialog4Available = EnumAvailabilityDialog.values()[compound.getInteger("AvailabilityDialog4")];
		dialogId = compound.getInteger("AvailabilityDialogId");
		dialog2Id = compound.getInteger("AvailabilityDialog2Id");
		dialog3Id = compound.getInteger("AvailabilityDialog3Id");
		dialog4Id = compound.getInteger("AvailabilityDialog4Id");
		questAvailable = EnumAvailabilityQuest.values()[compound.getInteger("AvailabilityQuest")];
		quest2Available = EnumAvailabilityQuest.values()[compound.getInteger("AvailabilityQuest2")];
		quest3Available = EnumAvailabilityQuest.values()[compound.getInteger("AvailabilityQuest3")];
		quest4Available = EnumAvailabilityQuest.values()[compound.getInteger("AvailabilityQuest4")];
		questId = compound.getInteger("AvailabilityQuestId");
		quest2Id = compound.getInteger("AvailabilityQuest2Id");
		quest3Id = compound.getInteger("AvailabilityQuest3Id");
		quest4Id = compound.getInteger("AvailabilityQuest4Id");
		setFactionAvailability(compound.getInteger("AvailabilityFaction"));
		setFactionAvailabilityStance(compound.getInteger("AvailabilityFactionStance"));
		setFaction2Availability(compound.getInteger("AvailabilityFaction2"));
		setFaction2AvailabilityStance(compound.getInteger("AvailabilityFaction2Stance"));
		factionId = compound.getInteger("AvailabilityFactionId");
		faction2Id = compound.getInteger("AvailabilityFaction2Id");
		daytime = EnumDayTime.values()[compound.getInteger("AvailabilityDayTime")];
		minPlayerLevel = compound.getInteger("AvailabilityMinPlayerLevel");
	}

	public void setFaction2Availability(int value) {
		faction2Available = EnumAvailabilityFactionType.values()[value];
	}

	public void setFaction2AvailabilityStance(int integer) {
		faction2Stance = EnumAvailabilityFaction.values()[integer];
	}

	public void setFactionAvailability(int value) {
		factionAvailable = EnumAvailabilityFactionType.values()[value];
	}

	public void setFactionAvailabilityStance(int integer) {
		factionStance = EnumAvailabilityFaction.values()[integer];
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("ModRev", version);
		compound.setInteger("AvailabilityDialog", dialogAvailable.ordinal());
		compound.setInteger("AvailabilityDialog2", dialog2Available.ordinal());
		compound.setInteger("AvailabilityDialog3", dialog3Available.ordinal());
		compound.setInteger("AvailabilityDialog4", dialog4Available.ordinal());
		compound.setInteger("AvailabilityDialogId", dialogId);
		compound.setInteger("AvailabilityDialog2Id", dialog2Id);
		compound.setInteger("AvailabilityDialog3Id", dialog3Id);
		compound.setInteger("AvailabilityDialog4Id", dialog4Id);
		compound.setInteger("AvailabilityQuest", questAvailable.ordinal());
		compound.setInteger("AvailabilityQuest2", quest2Available.ordinal());
		compound.setInteger("AvailabilityQuest3", quest3Available.ordinal());
		compound.setInteger("AvailabilityQuest4", quest4Available.ordinal());
		compound.setInteger("AvailabilityQuestId", questId);
		compound.setInteger("AvailabilityQuest2Id", quest2Id);
		compound.setInteger("AvailabilityQuest3Id", quest3Id);
		compound.setInteger("AvailabilityQuest4Id", quest4Id);
		compound.setInteger("AvailabilityFaction", factionAvailable.ordinal());
		compound.setInteger("AvailabilityFactionStance", factionStance.ordinal());
		compound.setInteger("AvailabilityFactionId", factionId);
		compound.setInteger("AvailabilityFaction2", faction2Available.ordinal());
		compound.setInteger("AvailabilityFaction2Stance", faction2Stance.ordinal());
		compound.setInteger("AvailabilityFaction2Id", faction2Id);
		compound.setInteger("AvailabilityDayTime", daytime.ordinal());
		compound.setInteger("AvailabilityMinPlayerLevel", minPlayerLevel);
		return compound;
	}
}

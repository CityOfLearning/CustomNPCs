
package noppes.npcs.controllers.quest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.ICompatibilty;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.Server;
import noppes.npcs.VersionCompatibility;
import noppes.npcs.api.constants.EnumQuestType;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.constants.EnumQuestCompletion;
import noppes.npcs.constants.EnumQuestRepeat;
import noppes.npcs.controllers.faction.FactionOptions;
import noppes.npcs.controllers.mail.PlayerMail;
import noppes.npcs.quests.QuestDialog;
import noppes.npcs.quests.QuestInterface;
import noppes.npcs.quests.QuestItem;
import noppes.npcs.quests.QuestKill;
import noppes.npcs.quests.QuestLocation;

public class Quest implements ICompatibilty, IQuest {
	public int version;
	public int id;
	public EnumQuestType type;
	public EnumQuestRepeat repeat;
	public EnumQuestCompletion completion;
	public String title;
	public QuestCategory category;
	public String logText;
	public String completeText;
	public String completerNpc;
	public int nextQuestid;
	public String nextQuestTitle;
	public PlayerMail mail;
	public String command;
	public QuestInterface questInterface;
	public int rewardExp;
	public NpcMiscInventory rewardItems;
	public boolean randomReward;
	public FactionOptions factionOptions;

	public Quest() {
		version = VersionCompatibility.ModRev;
		id = -1;
		type = EnumQuestType.ITEM;
		repeat = EnumQuestRepeat.NONE;
		completion = EnumQuestCompletion.Npc;
		title = "default";
		logText = "";
		completeText = "";
		completerNpc = "";
		nextQuestid = -1;
		nextQuestTitle = "";
		mail = new PlayerMail();
		command = "";
		questInterface = new QuestItem();
		rewardExp = 0;
		rewardItems = new NpcMiscInventory(9);
		randomReward = false;
		factionOptions = new FactionOptions();
	}

	public boolean complete(EntityPlayer player, QuestData data) {
		if (completion == EnumQuestCompletion.Instant) {
			Server.sendData((EntityPlayerMP) player, EnumPacketClient.QUEST_COMPLETION,
					data.quest.writeToNBT(new NBTTagCompound()));
			return true;
		}
		return false;
	}

	public Quest copy() {
		Quest quest = new Quest();
		quest.readNBT(writeToNBT(new NBTTagCompound()));
		return quest;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return title;
	}

	public Quest getNextQuest() {
		return (QuestController.instance == null) ? null : QuestController.instance.quests.get(nextQuestid);
	}

	@Override
	public EnumQuestType getType() {
		return type;
	}

	@Override
	public int getVersion() {
		return version;
	}

	public boolean hasNewQuest() {
		return getNextQuest() != null;
	}

	public void readNBT(NBTTagCompound compound) {
		id = compound.getInteger("Id");
		readNBTPartial(compound);
	}

	public void readNBTPartial(NBTTagCompound compound) {
		version = compound.getInteger("ModRev");
		VersionCompatibility.CheckAvailabilityCompatibility(this, compound);
		setType(EnumQuestType.values()[compound.getInteger("Type")]);
		title = compound.getString("Title");
		logText = compound.getString("Text");
		completeText = compound.getString("CompleteText");
		completerNpc = compound.getString("CompleterNpc");
		command = compound.getString("QuestCommand");
		nextQuestid = compound.getInteger("NextQuestId");
		nextQuestTitle = compound.getString("NextQuestTitle");
		if (hasNewQuest()) {
			nextQuestTitle = getNextQuest().title;
		} else {
			nextQuestTitle = "";
		}
		randomReward = compound.getBoolean("RandomReward");
		rewardExp = compound.getInteger("RewardExp");
		rewardItems.setFromNBT(compound.getCompoundTag("Rewards"));
		completion = EnumQuestCompletion.values()[compound.getInteger("QuestCompletion")];
		repeat = EnumQuestRepeat.values()[compound.getInteger("QuestRepeat")];
		questInterface.readEntityFromNBT(compound);
		factionOptions.readFromNBT(compound.getCompoundTag("QuestFactionPoints"));
		mail.readNBT(compound.getCompoundTag("QuestMail"));
	}

	public void setType(EnumQuestType questType) {
		type = questType;
		if (type == EnumQuestType.ITEM) {
			questInterface = new QuestItem();
		} else if (type == EnumQuestType.DIALOG) {
			questInterface = new QuestDialog();
		} else if ((type == EnumQuestType.KILL) || (type == EnumQuestType.AREA_KILL)) {
			questInterface = new QuestKill();
		} else if (type == EnumQuestType.LOCATION) {
			questInterface = new QuestLocation();
		}
		if (questInterface != null) {
			questInterface.questId = id;
		}
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("Id", id);
		return writeToNBTPartial(compound);
	}

	public NBTTagCompound writeToNBTPartial(NBTTagCompound compound) {
		compound.setInteger("ModRev", version);
		compound.setInteger("Type", type.ordinal());
		compound.setString("Title", title);
		compound.setString("Text", logText);
		compound.setString("CompleteText", completeText);
		compound.setString("CompleterNpc", completerNpc);
		compound.setInteger("NextQuestId", nextQuestid);
		compound.setString("NextQuestTitle", nextQuestTitle);
		compound.setInteger("RewardExp", rewardExp);
		compound.setTag("Rewards", rewardItems.getToNBT());
		compound.setString("QuestCommand", command);
		compound.setBoolean("RandomReward", randomReward);
		compound.setInteger("QuestCompletion", completion.ordinal());
		compound.setInteger("QuestRepeat", repeat.ordinal());
		questInterface.writeEntityToNBT(compound);
		compound.setTag("QuestFactionPoints", factionOptions.writeToNBT(new NBTTagCompound()));
		compound.setTag("QuestMail", mail.writeNBT());
		return compound;
	}
}

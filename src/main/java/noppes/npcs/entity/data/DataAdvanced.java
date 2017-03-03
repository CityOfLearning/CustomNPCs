
package noppes.npcs.entity.data;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.api.entity.data.INPCAdvanced;
import noppes.npcs.controllers.dialog.DialogOption;
import noppes.npcs.controllers.faction.FactionOptions;
import noppes.npcs.controllers.lines.Line;
import noppes.npcs.controllers.lines.Lines;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobBard;
import noppes.npcs.roles.JobBuilder;
import noppes.npcs.roles.JobChunkLoader;
import noppes.npcs.roles.JobConversation;
import noppes.npcs.roles.JobFarmer;
import noppes.npcs.roles.JobFollower;
import noppes.npcs.roles.JobGuard;
import noppes.npcs.roles.JobHealer;
import noppes.npcs.roles.JobItemGiver;
import noppes.npcs.roles.JobPuppet;
import noppes.npcs.roles.JobSpawner;
import noppes.npcs.roles.RoleBank;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.roles.RoleDialog;
import noppes.npcs.roles.RoleFollower;
import noppes.npcs.roles.RolePostman;
import noppes.npcs.roles.RoleTrader;
import noppes.npcs.roles.RoleTransporter;
import noppes.npcs.util.ValueUtil;

public class DataAdvanced implements INPCAdvanced {
	public Lines interactLines;
	public Lines worldLines;
	public Lines attackLines;
	public Lines killedLines;
	public Lines killLines;
	public boolean orderedLines;
	private String idleSound;
	private String angrySound;
	private String hurtSound;
	private String deathSound;
	private String stepSound;
	private EntityNPCInterface npc;
	public FactionOptions factions;
	public int role;
	public int job;
	public boolean attackOtherFactions;
	public boolean defendFaction;
	public boolean disablePitch;
	public DataScenes scenes;

	public DataAdvanced(EntityNPCInterface npc) {
		interactLines = new Lines();
		worldLines = new Lines();
		attackLines = new Lines();
		killedLines = new Lines();
		killLines = new Lines();
		orderedLines = false;
		idleSound = "";
		angrySound = "";
		hurtSound = "minecraft:game.player.hurt";
		deathSound = "minecraft:game.player.hurt";
		stepSound = "";
		factions = new FactionOptions();
		role = 0;
		job = 0;
		attackOtherFactions = false;
		defendFaction = false;
		disablePitch = false;
		this.npc = npc;
		scenes = new DataScenes(npc);
	}

	public Line getAttackLine() {
		return attackLines.getLine(!orderedLines);
	}

	private HashMap<Integer, DialogOption> getDialogs(NBTTagList tagList) {
		HashMap<Integer, DialogOption> map = new HashMap<>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			int slot = nbttagcompound.getInteger("DialogSlot");
			DialogOption option = new DialogOption();
			option.readNBT(nbttagcompound.getCompoundTag("NPCDialog"));
			map.put(slot, option);
		}
		return map;
	}

	public Line getInteractLine() {
		return interactLines.getLine(!orderedLines);
	}

	public Line getKilledLine() {
		return killedLines.getLine(!orderedLines);
	}

	public Line getKillLine() {
		return killLines.getLine(!orderedLines);
	}

	@Override
	public String getLine(int type, int slot) {
		Line line = getLines(type).lines.get(slot);
		if (line == null) {
			return null;
		}
		return line.text;
	}

	@Override
	public int getLineCount(int type) {
		return getLines(type).lines.size();
	}

	private Lines getLines(int type) {
		if (type == 0) {
			return interactLines;
		}
		if (type == 1) {
			return attackLines;
		}
		if (type == 2) {
			return worldLines;
		}
		if (type == 3) {
			return killedLines;
		}
		if (type == 4) {
			return killLines;
		}
		return null;
	}

	@Override
	public String getSound(int type) {
		String sound = null;
		if (type == 0) {
			sound = idleSound;
		} else if (type == 1) {
			sound = angrySound;
		} else if (type == 2) {
			sound = hurtSound;
		} else if (type == 3) {
			sound = deathSound;
		} else if (type == 4) {
			sound = stepSound;
		}
		if ((sound != null) && sound.isEmpty()) {
			return null;
		}
		return sound;
	}

	public Line getWorldLine() {
		return worldLines.getLine(!orderedLines);
	}

	public boolean hasWorldLines() {
		return !worldLines.isEmpty();
	}

	private NBTTagList nbtDialogs(HashMap<Integer, DialogOption> dialogs2) {
		NBTTagList nbttaglist = new NBTTagList();
		for (int slot : dialogs2.keySet()) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("DialogSlot", slot);
			nbttagcompound.setTag("NPCDialog", dialogs2.get(slot).writeNBT());
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public void readToNBT(NBTTagCompound compound) {
		interactLines.readNBT(compound.getCompoundTag("NpcInteractLines"));
		worldLines.readNBT(compound.getCompoundTag("NpcLines"));
		attackLines.readNBT(compound.getCompoundTag("NpcAttackLines"));
		killedLines.readNBT(compound.getCompoundTag("NpcKilledLines"));
		killLines.readNBT(compound.getCompoundTag("NpcKillLines"));
		orderedLines = compound.getBoolean("OrderedLines");
		idleSound = compound.getString("NpcIdleSound");
		angrySound = compound.getString("NpcAngrySound");
		hurtSound = compound.getString("NpcHurtSound");
		deathSound = compound.getString("NpcDeathSound");
		stepSound = compound.getString("NpcStepSound");
		npc.setFaction(compound.getInteger("FactionID"));
		npc.faction = npc.getFaction();
		attackOtherFactions = compound.getBoolean("AttackOtherFactions");
		defendFaction = compound.getBoolean("DefendFaction");
		disablePitch = compound.getBoolean("DisablePitch");
		setRole(compound.getInteger("Role"));
		setJob(compound.getInteger("NpcJob"));
		factions.readFromNBT(compound.getCompoundTag("FactionPoints"));
		npc.dialogs = getDialogs(compound.getTagList("NPCDialogOptions", 10));
		scenes.readFromNBT(compound.getCompoundTag("NpcScenes"));
	}

	public void setJob(int i) {
		if ((npc.jobInterface != null) && !npc.worldObj.isRemote) {
			npc.jobInterface.reset();
		}
		job = i % 12;
		if (job == 0) {
			npc.jobInterface = null;
		} else if ((job == 1) && !(npc.jobInterface instanceof JobBard)) {
			npc.jobInterface = new JobBard(npc);
		} else if ((job == 2) && !(npc.jobInterface instanceof JobHealer)) {
			npc.jobInterface = new JobHealer(npc);
		} else if ((job == 3) && !(npc.jobInterface instanceof JobGuard)) {
			npc.jobInterface = new JobGuard(npc);
		} else if ((job == 4) && !(npc.jobInterface instanceof JobItemGiver)) {
			npc.jobInterface = new JobItemGiver(npc);
		} else if ((job == 5) && !(npc.jobInterface instanceof JobFollower)) {
			npc.jobInterface = new JobFollower(npc);
		} else if ((job == 6) && !(npc.jobInterface instanceof JobSpawner)) {
			npc.jobInterface = new JobSpawner(npc);
		} else if ((job == 7) && !(npc.jobInterface instanceof JobConversation)) {
			npc.jobInterface = new JobConversation(npc);
		} else if ((job == 8) && !(npc.jobInterface instanceof JobChunkLoader)) {
			npc.jobInterface = new JobChunkLoader(npc);
		} else if ((job == 9) && !(npc.jobInterface instanceof JobPuppet)) {
			npc.jobInterface = new JobPuppet(npc);
		} else if ((job == 10) && !(npc.jobInterface instanceof JobBuilder)) {
			npc.jobInterface = new JobBuilder(npc);
		} else if ((job == 11) && !(npc.jobInterface instanceof JobFarmer)) {
			npc.jobInterface = new JobFarmer(npc);
		}
	}

	@Override
	public void setLine(int type, int slot, String text, String sound) {
		slot = ValueUtil.CorrectInt(slot, 0, 7);
		Lines lines = getLines(type);
		if ((text == null) || text.isEmpty()) {
			lines.lines.remove(slot);
		} else {
			Line line = lines.lines.get(slot);
			line.text = text;
			if (sound != null) {
				line.sound = sound;
			}
		}
	}

	public void setRole(int i) {
		if (8 <= i) {
			i -= 2;
		}
		role = i % 8;
		if (role == 0) {
			npc.roleInterface = null;
		} else if ((role == 3) && !(npc.roleInterface instanceof RoleBank)) {
			npc.roleInterface = new RoleBank(npc);
		} else if ((role == 2) && !(npc.roleInterface instanceof RoleFollower)) {
			npc.roleInterface = new RoleFollower(npc);
		} else if ((role == 5) && !(npc.roleInterface instanceof RolePostman)) {
			npc.roleInterface = new RolePostman(npc);
		} else if ((role == 1) && !(npc.roleInterface instanceof RoleTrader)) {
			npc.roleInterface = new RoleTrader(npc);
		} else if ((role == 4) && !(npc.roleInterface instanceof RoleTransporter)) {
			npc.roleInterface = new RoleTransporter(npc);
		} else if ((role == 6) && !(npc.roleInterface instanceof RoleCompanion)) {
			npc.roleInterface = new RoleCompanion(npc);
		} else if ((role == 7) && !(npc.roleInterface instanceof RoleDialog)) {
			npc.roleInterface = new RoleDialog(npc);
		}
	}

	@Override
	public void setSound(int type, String sound) {
		if (sound == null) {
			sound = "";
		}
		if (type == 0) {
			idleSound = sound;
		} else if (type == 1) {
			angrySound = sound;
		} else if (type == 2) {
			hurtSound = sound;
		} else if (type == 3) {
			deathSound = sound;
		} else if (type == 4) {
			stepSound = sound;
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("NpcLines", worldLines.writeToNBT());
		compound.setTag("NpcKilledLines", killedLines.writeToNBT());
		compound.setTag("NpcInteractLines", interactLines.writeToNBT());
		compound.setTag("NpcAttackLines", attackLines.writeToNBT());
		compound.setTag("NpcKillLines", killLines.writeToNBT());
		compound.setBoolean("OrderedLines", orderedLines);
		compound.setString("NpcIdleSound", idleSound);
		compound.setString("NpcAngrySound", angrySound);
		compound.setString("NpcHurtSound", hurtSound);
		compound.setString("NpcDeathSound", deathSound);
		compound.setString("NpcStepSound", stepSound);
		compound.setInteger("FactionID", npc.getFaction().id);
		compound.setBoolean("AttackOtherFactions", attackOtherFactions);
		compound.setBoolean("DefendFaction", defendFaction);
		compound.setBoolean("DisablePitch", disablePitch);
		compound.setInteger("Role", role);
		compound.setInteger("NpcJob", job);
		compound.setTag("FactionPoints", factions.writeToNBT(new NBTTagCompound()));
		compound.setTag("NPCDialogOptions", nbtDialogs(npc.dialogs));
		compound.setTag("NpcScenes", scenes.writeToNBT(new NBTTagCompound()));
		return compound;
	}
}

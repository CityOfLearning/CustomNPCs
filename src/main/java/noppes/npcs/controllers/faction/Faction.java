//

//

package noppes.npcs.controllers.faction;

import java.util.HashSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NBTTags;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.handler.data.IFaction;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.entity.EntityNPCInterface;

public class Faction implements IFaction {
	public static String formatName(String name) {
		name = name.toLowerCase().trim();
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public String name;
	public int color;
	public HashSet<Integer> attackFactions;
	public int id;
	public int neutralPoints;
	public int friendlyPoints;
	public int defaultPoints;
	public boolean hideFaction;

	public boolean getsAttacked;

	public Faction() {
		name = "";
		color = Integer.parseInt("FF00", 16);
		id = -1;
		neutralPoints = 500;
		friendlyPoints = 1500;
		defaultPoints = 1000;
		hideFaction = false;
		getsAttacked = false;
		attackFactions = new HashSet<Integer>();
	}

	public Faction(int id, String name, int color, int defaultPoints) {
		this.name = "";
		this.color = Integer.parseInt("FF00", 16);
		this.id = -1;
		neutralPoints = 500;
		friendlyPoints = 1500;
		this.defaultPoints = 1000;
		hideFaction = false;
		getsAttacked = false;
		this.name = name;
		this.color = color;
		this.defaultPoints = defaultPoints;
		this.id = id;
		attackFactions = new HashSet<Integer>();
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public int getDefaultPoints() {
		return defaultPoints;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean hostileToNpc(ICustomNpc npc) {
		return attackFactions.contains(npc.getFaction().getId());
	}

	public boolean isAggressiveToNpc(EntityNPCInterface entity) {
		return attackFactions.contains(entity.faction.id);
	}

	public boolean isAggressiveToPlayer(EntityPlayer player) {
		PlayerFactionData data = PlayerDataController.instance.getPlayerData(player).factionData;
		return data.getFactionPoints(id) < neutralPoints;
	}

	public boolean isFriendlyToPlayer(EntityPlayer player) {
		PlayerFactionData data = PlayerDataController.instance.getPlayerData(player).factionData;
		return data.getFactionPoints(id) >= friendlyPoints;
	}

	public boolean isNeutralToPlayer(EntityPlayer player) {
		PlayerFactionData data = PlayerDataController.instance.getPlayerData(player).factionData;
		int points = data.getFactionPoints(id);
		return (points >= neutralPoints) && (points < friendlyPoints);
	}

	@Override
	public int playerStatus(IPlayer player) {
		PlayerFactionData data = PlayerDataController.instance.getPlayerData(player.getMCEntity()).factionData;
		int points = data.getFactionPoints(id);
		if (points >= friendlyPoints) {
			return 1;
		}
		if (points < neutralPoints) {
			return -1;
		}
		return 0;
	}

	public void readNBT(NBTTagCompound compound) {
		name = compound.getString("Name");
		color = compound.getInteger("Color");
		id = compound.getInteger("Slot");
		neutralPoints = compound.getInteger("NeutralPoints");
		friendlyPoints = compound.getInteger("FriendlyPoints");
		defaultPoints = compound.getInteger("DefaultPoints");
		hideFaction = compound.getBoolean("HideFaction");
		getsAttacked = compound.getBoolean("GetsAttacked");
		attackFactions = NBTTags.getIntegerSet(compound.getTagList("AttackFactions", 10));
	}

	public void writeNBT(NBTTagCompound compound) {
		compound.setInteger("Slot", id);
		compound.setString("Name", name);
		compound.setInteger("Color", color);
		compound.setInteger("NeutralPoints", neutralPoints);
		compound.setInteger("FriendlyPoints", friendlyPoints);
		compound.setInteger("DefaultPoints", defaultPoints);
		compound.setBoolean("HideFaction", hideFaction);
		compound.setBoolean("GetsAttacked", getsAttacked);
		compound.setTag("AttackFactions", NBTTags.nbtIntegerCollection(attackFactions));
	}
}

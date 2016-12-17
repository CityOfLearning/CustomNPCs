
package noppes.npcs.roles;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import noppes.npcs.EventHooks;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.entity.data.role.IRoleFollower;
import noppes.npcs.api.event.RoleEvent;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.NBTTags;
import noppes.npcs.util.NoppesStringUtils;
import noppes.npcs.util.NoppesUtilServer;

public class RoleFollower extends RoleInterface implements IRoleFollower {
	private String ownerUUID;
	public boolean isFollowing;
	public HashMap<Integer, Integer> rates;
	public NpcMiscInventory inventory;
	public String dialogHire;
	public String dialogFarewell;
	public int daysHired;
	public long hiredTime;
	public boolean disableGui;
	public boolean infiniteDays;
	public boolean refuseSoulStone;
	public EntityPlayer owner;

	public RoleFollower(EntityNPCInterface npc) {
		super(npc);
		isFollowing = true;
		dialogHire = StatCollector.translateToLocal("follower.hireText") + " {days} "
				+ StatCollector.translateToLocal("follower.days");
		dialogFarewell = StatCollector.translateToLocal("follower.farewellText") + " {player}";
		disableGui = false;
		infiniteDays = false;
		refuseSoulStone = false;
		owner = null;
		inventory = new NpcMiscInventory(3);
		rates = new HashMap<Integer, Integer>();
	}

	@Override
	public void addDays(int days) {
		daysHired = days + getDays();
		hiredTime = npc.worldObj.getTotalWorldTime();
	}

	@Override
	public boolean aiShouldExecute() {
		owner = getOwner();
		if (!infiniteDays && (owner != null) && (getDays() <= 0)) {
			RoleEvent.FollowerFinishedEvent event = new RoleEvent.FollowerFinishedEvent(owner, npc.wrappedNPC);
			EventHooks.onNPCRole(npc, event);
			owner.addChatMessage(new ChatComponentTranslation(NoppesStringUtils.formatText(dialogFarewell, owner, npc),
					new Object[0]));
			killed();
		}
		return false;
	}

	@Override
	public boolean defendOwner() {
		return isFollowing() && (npc.advanced.job == 3);
	}

	@Override
	public void delete() {
	}

	@Override
	public int getDays() {
		if (infiniteDays) {
			return 100;
		}
		if (daysHired <= 0) {
			return 0;
		}
		int days = (int) ((npc.worldObj.getTotalWorldTime() - hiredTime) / 24000L);
		return daysHired - days;
	}

	@Override
	public IPlayer getFollowing() {
		EntityPlayer owner = getOwner();
		if (owner != null) {
			return (IPlayer) NpcAPI.Instance().getIEntity(owner);
		}
		return null;
	}

	@Override
	public boolean getGuiDisabled() {
		return disableGui;
	}

	@Override
	public boolean getInfinite() {
		return infiniteDays;
	}

	public EntityPlayer getOwner() {
		if ((ownerUUID == null) || ownerUUID.isEmpty()) {
			return null;
		}
		try {
			UUID uuid = UUID.fromString(ownerUUID);
			if (uuid != null) {
				return npc.worldObj.getPlayerEntityByUUID(uuid);
			}
		} catch (IllegalArgumentException ex) {
		}
		return npc.worldObj.getPlayerEntityByName(ownerUUID);
	}

	public boolean hasOwner() {
		return (infiniteDays || (daysHired > 0)) && (ownerUUID != null) && !ownerUUID.isEmpty();
	}

	@Override
	public void interact(EntityPlayer player) {
		if ((ownerUUID == null) || ownerUUID.isEmpty()) {
			npc.say(player, npc.advanced.getInteractLine());
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerFollowerHire, npc);
		} else if ((player == owner) && !disableGui) {
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerFollower, npc);
		}
	}

	@Override
	public boolean isFollowing() {
		return (owner != null) && isFollowing && (getDays() > 0);
	}

	@Override
	public void killed() {
		ownerUUID = null;
		daysHired = 0;
		hiredTime = 0L;
		isFollowing = true;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		ownerUUID = nbttagcompound.getString("MercenaryOwner");
		daysHired = nbttagcompound.getInteger("MercenaryDaysHired");
		hiredTime = nbttagcompound.getLong("MercenaryHiredTime");
		dialogHire = nbttagcompound.getString("MercenaryDialogHired");
		dialogFarewell = nbttagcompound.getString("MercenaryDialogFarewell");
		rates = NBTTags.getIntegerIntegerMap(nbttagcompound.getTagList("MercenaryDayRates", 10));
		inventory.setFromNBT(nbttagcompound.getCompoundTag("MercenaryInv"));
		isFollowing = nbttagcompound.getBoolean("MercenaryIsFollowing");
		disableGui = nbttagcompound.getBoolean("MercenaryDisableGui");
		infiniteDays = nbttagcompound.getBoolean("MercenaryInfiniteDays");
		refuseSoulStone = nbttagcompound.getBoolean("MercenaryRefuseSoulstone");
	}

	@Override
	public void setFollowing(IPlayer player) {
		if (player == null) {
			setOwner(null);
		} else {
			setOwner(player.getMCEntity());
		}
	}

	@Override
	public void setGuiDisabled(boolean disabled) {
		disableGui = disabled;
	}

	@Override
	public void setInfinite(boolean infinite) {
		infiniteDays = infinite;
	}

	public void setOwner(EntityPlayer player) {
		UUID id = player.getUniqueID();
		if ((ownerUUID == null) || (id == null) || !ownerUUID.equals(id)) {
			killed();
		}
		ownerUUID = id.toString();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("MercenaryDaysHired", daysHired);
		nbttagcompound.setLong("MercenaryHiredTime", hiredTime);
		nbttagcompound.setString("MercenaryDialogHired", dialogHire);
		nbttagcompound.setString("MercenaryDialogFarewell", dialogFarewell);
		if (hasOwner()) {
			nbttagcompound.setString("MercenaryOwner", ownerUUID);
		}
		nbttagcompound.setTag("MercenaryDayRates", NBTTags.nbtIntegerIntegerMap(rates));
		nbttagcompound.setTag("MercenaryInv", inventory.getToNBT());
		nbttagcompound.setBoolean("MercenaryIsFollowing", isFollowing);
		nbttagcompound.setBoolean("MercenaryDisableGui", disableGui);
		nbttagcompound.setBoolean("MercenaryInfiniteDays", infiniteDays);
		nbttagcompound.setBoolean("MercenaryRefuseSoulstone", refuseSoulStone);
		return nbttagcompound;
	}
}

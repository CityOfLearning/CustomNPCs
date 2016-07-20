//

//

package noppes.npcs.roles;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.event.RoleEvent;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.transport.PlayerTransportData;
import noppes.npcs.controllers.transport.TransportController;
import noppes.npcs.controllers.transport.TransportLocation;
import noppes.npcs.entity.EntityNPCInterface;

public class RoleTransporter extends RoleInterface {
	public int transportId;
	public String name;
	private int ticks;

	public RoleTransporter(EntityNPCInterface npc) {
		super(npc);
		transportId = -1;
		ticks = 10;
	}

	@Override
	public boolean aiShouldExecute() {
		--ticks;
		if (ticks > 0) {
			return false;
		}
		ticks = 10;
		if (!hasTransport()) {
			return false;
		}
		TransportLocation loc = getLocation();
		if (loc.type != 0) {
			return false;
		}
		List<EntityPlayer> inRange = npc.worldObj.getEntitiesWithinAABB((Class) EntityPlayer.class,
				npc.getEntityBoundingBox().expand(6.0, 6.0, 6.0));
		for (EntityPlayer player : inRange) {
			if (!npc.canSee(player)) {
				continue;
			}
			unlock(player, loc);
		}
		return false;
	}

	public TransportLocation getLocation() {
		if (npc.isRemote()) {
			return null;
		}
		return TransportController.getInstance().getTransport(transportId);
	}

	public boolean hasTransport() {
		TransportLocation loc = getLocation();
		return (loc != null) && (loc.id == transportId);
	}

	@Override
	public void interact(EntityPlayer player) {
		if (hasTransport()) {
			TransportLocation loc = getLocation();
			if (loc.type == 2) {
				unlock(player, loc);
			}
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerTransporter, npc);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		transportId = nbttagcompound.getInteger("TransporterId");
		TransportLocation loc = getLocation();
		if (loc != null) {
			name = loc.name;
		}
	}

	public void setTransport(TransportLocation location) {
		transportId = location.id;
		name = location.name;
	}

	private void unlock(EntityPlayer player, TransportLocation loc) {
		PlayerTransportData data = PlayerDataController.instance.getPlayerData(player).transportData;
		if (data.transports.contains(transportId)) {
			return;
		}
		RoleEvent.TransporterUnlockedEvent event = new RoleEvent.TransporterUnlockedEvent(player, npc.wrappedNPC);
		if (EventHooks.onNPCRole(npc, event)) {
			return;
		}
		data.transports.add(transportId);
		player.addChatMessage(new ChatComponentTranslation("transporter.unlock", new Object[] { loc.name }));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("TransporterId", transportId);
		return nbttagcompound;
	}
}

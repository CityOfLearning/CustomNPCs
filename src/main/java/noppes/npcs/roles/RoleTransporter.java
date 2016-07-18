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
import noppes.npcs.controllers.PlayerTransportData;
import noppes.npcs.controllers.TransportController;
import noppes.npcs.controllers.TransportLocation;
import noppes.npcs.entity.EntityNPCInterface;

public class RoleTransporter extends RoleInterface {
	public int transportId;
	public String name;
	private int ticks;

	public RoleTransporter(final EntityNPCInterface npc) {
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
		final TransportLocation loc = getLocation();
		if (loc.type != 0) {
			return false;
		}
		final List<EntityPlayer> inRange = npc.worldObj.getEntitiesWithinAABB((Class) EntityPlayer.class,
				npc.getEntityBoundingBox().expand(6.0, 6.0, 6.0));
		for (final EntityPlayer player : inRange) {
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
		final TransportLocation loc = getLocation();
		return (loc != null) && (loc.id == transportId);
	}

	@Override
	public void interact(final EntityPlayer player) {
		if (hasTransport()) {
			final TransportLocation loc = getLocation();
			if (loc.type == 2) {
				unlock(player, loc);
			}
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerTransporter, npc);
		}
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbttagcompound) {
		transportId = nbttagcompound.getInteger("TransporterId");
		final TransportLocation loc = getLocation();
		if (loc != null) {
			name = loc.name;
		}
	}

	public void setTransport(final TransportLocation location) {
		transportId = location.id;
		name = location.name;
	}

	private void unlock(final EntityPlayer player, final TransportLocation loc) {
		final PlayerTransportData data = PlayerDataController.instance.getPlayerData(player).transportData;
		if (data.transports.contains(transportId)) {
			return;
		}
		final RoleEvent.TransporterUnlockedEvent event = new RoleEvent.TransporterUnlockedEvent(player, npc.wrappedNPC);
		if (EventHooks.onNPCRole(npc, event)) {
			return;
		}
		data.transports.add(transportId);
		player.addChatMessage(new ChatComponentTranslation("transporter.unlock", new Object[] { loc.name }));
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("TransporterId", transportId);
		return nbttagcompound;
	}
}

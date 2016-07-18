//

//

package noppes.npcs.controllers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import noppes.npcs.CustomNpcs;
import noppes.npcs.entity.EntityNPCInterface;

public class ChunkController implements ForgeChunkManager.LoadingCallback {
	public static ChunkController instance;
	private HashMap<Entity, ForgeChunkManager.Ticket> tickets;

	public ChunkController() {
		tickets = new HashMap<Entity, ForgeChunkManager.Ticket>();
		ChunkController.instance = this;
	}

	public void clear() {
		tickets = new HashMap<Entity, ForgeChunkManager.Ticket>();
	}

	public void deleteNPC(final EntityNPCInterface npc) {
		final ForgeChunkManager.Ticket ticket = tickets.get(npc);
		if (ticket != null) {
			tickets.remove(npc);
			ForgeChunkManager.releaseTicket(ticket);
		}
	}

	public ForgeChunkManager.Ticket getTicket(final EntityNPCInterface npc) {
		ForgeChunkManager.Ticket ticket = tickets.get(npc);
		if (ticket != null) {
			return ticket;
		}
		if (size() >= CustomNpcs.ChuckLoaders) {
			return null;
		}
		ticket = ForgeChunkManager.requestTicket(CustomNpcs.instance, npc.worldObj, ForgeChunkManager.Type.ENTITY);
		if (ticket == null) {
			return null;
		}
		ticket.bindEntity(npc);
		ticket.setChunkListDepth(6);
		tickets.put(npc, ticket);
		return null;
	}

	public int size() {
		return tickets.size();
	}

	@Override
	public void ticketsLoaded(final List<ForgeChunkManager.Ticket> tickets, final World world) {
		for (final ForgeChunkManager.Ticket ticket : tickets) {
			if (!(ticket.getEntity() instanceof EntityNPCInterface)) {
				continue;
			}
			final EntityNPCInterface npc = (EntityNPCInterface) ticket.getEntity();
			if ((npc.advanced.job != 8) || tickets.contains(npc)) {
				continue;
			}
			this.tickets.put(npc, ticket);
			final double x = npc.posX / 16.0;
			final double z = npc.posZ / 16.0;
			ForgeChunkManager.forceChunk(ticket,
					new ChunkCoordIntPair(MathHelper.floor_double(x), MathHelper.floor_double(z)));
			ForgeChunkManager.forceChunk(ticket,
					new ChunkCoordIntPair(MathHelper.ceiling_double_int(x), MathHelper.ceiling_double_int(z)));
			ForgeChunkManager.forceChunk(ticket,
					new ChunkCoordIntPair(MathHelper.floor_double(x), MathHelper.ceiling_double_int(z)));
			ForgeChunkManager.forceChunk(ticket,
					new ChunkCoordIntPair(MathHelper.ceiling_double_int(x), MathHelper.floor_double(z)));
		}
	}

	public void unload(final int toRemove) {
		final Iterator<Entity> ite = tickets.keySet().iterator();
		int i = 0;
		while (ite.hasNext()) {
			if (i >= toRemove) {
				return;
			}
			final Entity entity = ite.next();
			ForgeChunkManager.releaseTicket(tickets.get(entity));
			ite.remove();
			++i;
		}
	}
}

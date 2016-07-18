//

//

package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;
import noppes.npcs.controllers.ChunkController;
import noppes.npcs.entity.EntityNPCInterface;

public class JobChunkLoader extends JobInterface {
	private List<ChunkCoordIntPair> chunks;
	private int ticks;
	private long playerLastSeen;

	public JobChunkLoader(final EntityNPCInterface npc) {
		super(npc);
		chunks = new ArrayList<ChunkCoordIntPair>();
		ticks = 20;
		playerLastSeen = 0L;
	}

	@Override
	public boolean aiContinueExecute() {
		return false;
	}

	@Override
	public boolean aiShouldExecute() {
		--ticks;
		if (ticks > 0) {
			return false;
		}
		ticks = 20;
		final List players = npc.worldObj.getEntitiesWithinAABB((Class) EntityPlayer.class,
				npc.getEntityBoundingBox().expand(48.0, 48.0, 48.0));
		if (!players.isEmpty()) {
			playerLastSeen = System.currentTimeMillis();
		}
		if (System.currentTimeMillis() > (playerLastSeen + 600000L)) {
			ChunkController.instance.deleteNPC(npc);
			chunks.clear();
			return false;
		}
		final ForgeChunkManager.Ticket ticket = ChunkController.instance.getTicket(npc);
		if (ticket == null) {
			return false;
		}
		final double x = npc.posX / 16.0;
		final double z = npc.posZ / 16.0;
		final List<ChunkCoordIntPair> list = new ArrayList<ChunkCoordIntPair>();
		list.add(new ChunkCoordIntPair(MathHelper.floor_double(x), MathHelper.floor_double(z)));
		list.add(new ChunkCoordIntPair(MathHelper.ceiling_double_int(x), MathHelper.ceiling_double_int(z)));
		list.add(new ChunkCoordIntPair(MathHelper.floor_double(x), MathHelper.ceiling_double_int(z)));
		list.add(new ChunkCoordIntPair(MathHelper.ceiling_double_int(x), MathHelper.floor_double(z)));
		for (final ChunkCoordIntPair chunk : list) {
			if (!chunks.contains(chunk)) {
				ForgeChunkManager.forceChunk(ticket, chunk);
			} else {
				chunks.remove(chunk);
			}
		}
		for (final ChunkCoordIntPair chunk : chunks) {
			ForgeChunkManager.unforceChunk(ticket, chunk);
		}
		chunks = list;
		return false;
	}

	@Override
	public void delete() {
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		playerLastSeen = compound.getLong("ChunkPlayerLastSeen");
	}

	@Override
	public void reset() {
		ChunkController.instance.deleteNPC(npc);
		chunks.clear();
		playerLastSeen = 0L;
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		compound.setLong("ChunkPlayerLastSeen", playerLastSeen);
		return compound;
	}
}

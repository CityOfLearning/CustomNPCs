//

//

package noppes.npcs.api;

import net.minecraft.world.World;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.entity.data.IData;

public interface IWorld {
	void broadcast(final String p0);

	IItemStack createItem(final String p0, final int p1, final int p2);

	void explode(final double p0, final double p1, final double p2, final float p3, final boolean p4, final boolean p5);

	IPlayer[] getAllPlayers();

	String getBiomeName(final int p0, final int p1);

	IBlock getBlock(final int p0, final int p1, final int p2);

	IEntity getClosestEntity(final int p0, final int p1, final int p2, final int p3, final int p4);

	float getLightValue(final int p0, final int p1, final int p2);

	World getMCWorld();

	IEntity[] getNearbyEntities(final int p0, final int p1, final int p2, final int p3, final int p4);

	IPlayer getPlayer(final String p0);

	int getRedstonePower(final int p0, final int p1, final int p2);

	IScoreboard getScoreboard();

	IData getStoreddata();

	IData getTempdata();

	long getTime();

	long getTotalTime();

	boolean isDay();

	boolean isRaining();

	void removeBlock(final int p0, final int p1, final int p2);

	void setBlock(final int p0, final int p1, final int p2, final String p3, final int p4);

	void setRaining(final boolean p0);

	void setTime(final long p0);

	IEntity spawnClone(final int p0, final int p1, final int p2, final int p3, final String p4);

	void spawnParticle(final String p0, final double p1, final double p2, final double p3, final double p4,
			final double p5, final double p6, final double p7, final int p8);

	void thunderStrike(final double p0, final double p1, final double p2);
}

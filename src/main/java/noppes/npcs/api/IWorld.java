
package noppes.npcs.api;

import net.minecraft.world.World;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.entity.data.IData;

public interface IWorld {
	void broadcast(String p0);

	IItemStack createItem(String p0, int p1, int p2);

	void explode(double p0, double p1, double p2, float p3, boolean p4, boolean p5);

	IPlayer[] getAllPlayers();

	String getBiomeName(int p0, int p1);

	IBlock getBlock(int p0, int p1, int p2);

	IEntity getClosestEntity(int p0, int p1, int p2, int p3, int p4);

	float getLightValue(int p0, int p1, int p2);

	World getMCWorld();

	IEntity[] getNearbyEntities(int p0, int p1, int p2, int p3, int p4);

	IPlayer getPlayer(String p0);

	int getRedstonePower(int p0, int p1, int p2);

	IScoreboard getScoreboard();

	IData getStoreddata();

	IData getTempdata();

	long getTime();

	long getTotalTime();

	boolean isDay();

	boolean isRaining();

	void removeBlock(int p0, int p1, int p2);

	void setBlock(int p0, int p1, int p2, String p3, int p4);

	void setRaining(boolean p0);

	void setTime(long p0);

	IEntity spawnClone(int p0, int p1, int p2, int p3, String p4);

	void spawnParticle(String p0, double p1, double p2, double p3, double p4, double p5, double p6, double p7, int p8);

	void thunderStrike(double p0, double p1, double p2);
}

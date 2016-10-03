package noppes.npcs.api;

import net.minecraft.world.World;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.IScoreboard;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.entity.data.IData;

public interface IWorld {

   IEntity[] getNearbyEntities(int var1, int var2, int var3, int var4, int var5);

   IEntity getClosestEntity(int var1, int var2, int var3, int var4, int var5);

   long getTime();

   void setTime(long var1);

   long getTotalTime();

   IBlock getBlock(int var1, int var2, int var3);

   void setBlock(int var1, int var2, int var3, String var4, int var5);

   void removeBlock(int var1, int var2, int var3);

   float getLightValue(int var1, int var2, int var3);

   IPlayer getPlayer(String var1);

   boolean isDay();

   boolean isRaining();

   void setRaining(boolean var1);

   void thunderStrike(double var1, double var3, double var5);

   void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, int var16);

   void broadcast(String var1);

   IScoreboard getScoreboard();

   IData getTempdata();

   IData getStoreddata();

   IItemStack createItem(String var1, int var2, int var3);

   void explode(double var1, double var3, double var5, float var7, boolean var8, boolean var9);

   IPlayer[] getAllPlayers();

   String getBiomeName(int var1, int var2);

   IEntity spawnClone(int var1, int var2, int var3, int var4, String var5);

   int getRedstonePower(int var1, int var2, int var3);

   World getMCWorld();
}

package noppes.npcs.api.entity;

import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.entity.IEntityLivingBase;

public interface IPlayer extends IEntityLivingBase {

   String getName();

   String getDisplayName();

   boolean hasFinishedQuest(int var1);

   boolean hasActiveQuest(int var1);

   void startQuest(int var1);

   void finishQuest(int var1);

   void stopQuest(int var1);

   void removeQuest(int var1);

   boolean hasReadDialog(int var1);

   void showDialog(int var1, String var2);

   void addFactionPoints(int var1, int var2);

   int getFactionPoints(int var1);

   void message(String var1);

   int getGamemode();

   void setGamemode(int var1);

   int inventoryItemCount(IItemStack var1);

   IItemStack[] getInventory();

   boolean removeItem(IItemStack var1, int var2);

   boolean removeItem(String var1, int var2, int var3);

   void removeAllItems(IItemStack var1);

   boolean giveItem(IItemStack var1);

   boolean giveItem(String var1, int var2, int var3);

   void setSpawnpoint(int var1, int var2, int var3);

   void resetSpawnpoint();

   boolean hasAchievement(String var1);

   int getExpLevel();

   void setExpLevel(int var1);

   boolean hasPermission(String var1);

   EntityPlayerMP getMCEntity();
}

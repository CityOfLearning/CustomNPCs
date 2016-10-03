package noppes.npcs.api.entity;

import net.minecraft.entity.EntityCreature;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.ITimers;
import noppes.npcs.api.entity.IEntityLivingBase;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.entity.data.INPCAi;
import noppes.npcs.api.entity.data.INPCDisplay;
import noppes.npcs.api.entity.data.INPCInventory;
import noppes.npcs.api.entity.data.INPCJob;
import noppes.npcs.api.entity.data.INPCRole;
import noppes.npcs.api.entity.data.INPCStats;
import noppes.npcs.api.handler.data.IFaction;

public interface ICustomNpc extends IEntityLivingBase {

   INPCDisplay getDisplay();

   INPCInventory getInventory();

   INPCStats getStats();

   INPCAi getAi();

   IFaction getFaction();

   void setFaction(int var1);

   INPCRole getRole();

   INPCJob getJob();

   ITimers getTimers();

   int getHomeX();

   int getHomeY();

   int getHomeZ();

   void setHome(int var1, int var2, int var3);

   void reset();

   void say(String var1);

   void kill();

   void shootItem(IEntityLivingBase var1, IItemStack var2, int var3);

   void giveItem(IPlayer var1, IItemStack var2);

   void executeCommand(String var1);

   EntityCreature getMCEntity();
}

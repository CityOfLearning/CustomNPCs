package noppes.npcs.api.wrapper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.ITimers;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IEntityLivingBase;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.entity.data.INPCAi;
import noppes.npcs.api.entity.data.INPCDisplay;
import noppes.npcs.api.entity.data.INPCInventory;
import noppes.npcs.api.entity.data.INPCJob;
import noppes.npcs.api.entity.data.INPCRole;
import noppes.npcs.api.entity.data.INPCStats;
import noppes.npcs.api.handler.data.IFaction;
import noppes.npcs.api.wrapper.EntityLivingWrapper;
import noppes.npcs.controllers.Faction;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.Line;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.ValueUtil;

public class NPCWrapper extends EntityLivingWrapper implements ICustomNpc {

   public NPCWrapper(EntityNPCInterface npc) {
      super(npc);
   }

   public void setMaxHealth(float health) {
      super.setMaxHealth(health);
      ((EntityNPCInterface)this.entity).stats.maxHealth = (int)health;
   }

   public INPCDisplay getDisplay() {
      return ((EntityNPCInterface)this.entity).display;
   }

   public INPCInventory getInventory() {
      return ((EntityNPCInterface)this.entity).inventory;
   }

   public INPCAi getAi() {
      return ((EntityNPCInterface)this.entity).ai;
   }

   public INPCStats getStats() {
      return ((EntityNPCInterface)this.entity).stats;
   }

   public IFaction getFaction() {
      return ((EntityNPCInterface)this.entity).faction;
   }

   public ITimers getTimers() {
      return ((EntityNPCInterface)this.entity).timers;
   }

   public void setFaction(int id) {
      Faction faction = FactionController.getInstance().getFaction(id);
      if(faction == null) {
         throw new CustomNPCsException("Unknown faction id: " + id, new Object[0]);
      } else {
         ((EntityNPCInterface)this.entity).setFaction(id);
      }
   }

   public INPCRole getRole() {
      return ((EntityNPCInterface)this.entity).roleInterface;
   }

   public INPCJob getJob() {
      return ((EntityNPCInterface)this.entity).jobInterface;
   }

   public int getHomeX() {
      return ((EntityNPCInterface)this.entity).ai.startPos().getX();
   }

   public int getHomeY() {
      return ((EntityNPCInterface)this.entity).ai.startPos().getY();
   }

   public int getHomeZ() {
      return ((EntityNPCInterface)this.entity).ai.startPos().getZ();
   }

   public void setHome(int x, int y, int z) {
      ((EntityNPCInterface)this.entity).ai.setStartPos(new BlockPos(x, y, z));
   }

   public int getOffsetX() {
      return (int)((EntityNPCInterface)this.entity).ai.bodyOffsetX;
   }

   public int getOffsetY() {
      return (int)((EntityNPCInterface)this.entity).ai.bodyOffsetY;
   }

   public int getOffsetZ() {
      return (int)((EntityNPCInterface)this.entity).ai.bodyOffsetZ;
   }

   public void setOffset(int x, int y, int z) {
      ((EntityNPCInterface)this.entity).ai.bodyOffsetX = ValueUtil.correctFloat((float)x, 0.0F, 9.0F);
      ((EntityNPCInterface)this.entity).ai.bodyOffsetY = ValueUtil.correctFloat((float)y, 0.0F, 9.0F);
      ((EntityNPCInterface)this.entity).ai.bodyOffsetZ = ValueUtil.correctFloat((float)z, 0.0F, 9.0F);
   }

   public void say(String message) {
      ((EntityNPCInterface)this.entity).saySurrounding(new Line(message));
   }

   public void kill() {
      ((EntityNPCInterface)this.entity).setDead();
   }

   public void reset() {
      ((EntityNPCInterface)this.entity).reset();
   }

   public long getAge() {
      return ((EntityNPCInterface)this.entity).totalTicksAlive;
   }

   public void shootItem(IEntityLivingBase target, IItemStack item, int accuracy) {
      if(item == null) {
         throw new CustomNPCsException("No item was given", new Object[0]);
      } else if(target == null) {
         throw new CustomNPCsException("No target was given", new Object[0]);
      } else {
         accuracy = ValueUtil.CorrectInt(accuracy, 1, 100);
         ((EntityNPCInterface)this.entity).shoot(target.getMCEntity(), accuracy, item.getMCItemStack(), false);
      }
   }

   public void giveItem(IPlayer player, IItemStack item) {
      ((EntityNPCInterface)this.entity).givePlayerItem(player.getMCEntity(), item.getMCItemStack());
   }

   public void executeCommand(String command) {
      NoppesUtilServer.runCommand(this.entity, ((EntityNPCInterface)this.entity).getName(), command, (EntityPlayer)null);
   }

   public int getType() {
      return 2;
   }

   public boolean typeOf(int type) {
      return type == 2?true:super.typeOf(type);
   }
}

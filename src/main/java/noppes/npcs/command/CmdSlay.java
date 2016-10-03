package noppes.npcs.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.entity.EntityNPCInterface;

public class CmdSlay extends CommandNoppesBase {

   public Map SlayMap = new LinkedHashMap();


   public CmdSlay() {
      this.SlayMap.clear();
      this.SlayMap.put("all", EntityLivingBase.class);
      this.SlayMap.put("mobs", EntityMob.class);
      this.SlayMap.put("animals", EntityAnimal.class);
      this.SlayMap.put("items", EntityItem.class);
      this.SlayMap.put("xporbs", EntityXPOrb.class);
      HashMap list = new HashMap(EntityList.stringToClassMapping);
      Iterator var2 = list.keySet().iterator();

      while(var2.hasNext()) {
         String name = (String)var2.next();
         Class cls = (Class)list.get(name);
         if(!EntityNPCInterface.class.isAssignableFrom(cls) && EntityLivingBase.class.isAssignableFrom(cls)) {
            this.SlayMap.put(name.toLowerCase(), list.get(name));
         }
      }

      this.SlayMap.remove("monster");
      this.SlayMap.remove("mob");
   }

   public String getCommandName() {
      return "slay";
   }

   public String getDescription() {
      return "Kills given entity within range. Also has all, mobs, animal options. Can have multiple types";
   }

   public String getUsage() {
      return "<type>.. [range]";
   }

   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
      ArrayList toDelete = new ArrayList();
      boolean deleteNPCs = false;
      String[] count = args;
      int range = args.length;

      for(int box = 0; box < range; ++box) {
         String list = count[box];
         list = list.toLowerCase();
         Class cls = (Class)this.SlayMap.get(list);
         if(cls != null) {
            toDelete.add(cls);
         }

         if(list.equals("mobs")) {
            toDelete.add(EntityGhast.class);
            toDelete.add(EntityDragon.class);
         }

         if(list.equals("npcs")) {
            deleteNPCs = true;
         }
      }

      int var12 = 0;
      range = 120;

      try {
         range = Integer.parseInt(args[args.length - 1]);
      } catch (NumberFormatException var11) {
         ;
      }

      AxisAlignedBB var13 = (new AxisAlignedBB(sender.getPosition(), sender.getPosition().add(1, 1, 1))).expand((double)range, (double)range, (double)range);
      List var14 = sender.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, var13);
      Iterator var15 = var14.iterator();

      Entity entity;
      while(var15.hasNext()) {
         entity = (Entity)var15.next();
         if(!(entity instanceof EntityPlayer) && (!(entity instanceof EntityTameable) || !((EntityTameable)entity).isTamed()) && (!(entity instanceof EntityNPCInterface) || deleteNPCs) && this.delete(entity, toDelete)) {
            ++var12;
         }
      }

      if(toDelete.contains(EntityXPOrb.class)) {
         var14 = sender.getEntityWorld().getEntitiesWithinAABB(EntityXPOrb.class, var13);

         for(var15 = var14.iterator(); var15.hasNext(); ++var12) {
            entity = (Entity)var15.next();
            entity.isDead = true;
         }
      }

      if(toDelete.contains(EntityItem.class)) {
         var14 = sender.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, var13);

         for(var15 = var14.iterator(); var15.hasNext(); ++var12) {
            entity = (Entity)var15.next();
            entity.isDead = true;
         }
      }

      sender.addChatMessage(new ChatComponentTranslation(var12 + " entities deleted", new Object[0]));
   }

   private boolean delete(Entity entity, ArrayList toDelete) {
      Iterator var3 = toDelete.iterator();

      Class delete;
      do {
         if(!var3.hasNext()) {
            return false;
         }

         delete = (Class)var3.next();
      } while(delete == EntityAnimal.class && entity instanceof EntityHorse || !delete.isAssignableFrom(entity.getClass()));

      entity.isDead = true;
      return true;
   }

   public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
      return CommandBase.getListOfStringsMatchingLastWord(args, (String[])this.SlayMap.keySet().toArray(new String[this.SlayMap.size()]));
   }
}

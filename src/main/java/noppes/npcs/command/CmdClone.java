package noppes.npcs.command;

import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.entity.EntityNPCInterface;

public class CmdClone extends CommandNoppesBase {

   public String getCommandName() {
      return "clone";
   }

   public String getDescription() {
      return "Clone operation (server side)";
   }

   @CommandNoppesBase.SubCommand(
      desc = "Add NPC(s) to clone storage",
      usage = "<npc> <tab> [clonedname]",
      permission = 4
   )
   public void add(ICommandSender sender, String[] args) {
      int tab = 0;

      try {
         tab = Integer.parseInt(args[1]);
      } catch (NumberFormatException var9) {
         ;
      }

      List list = this.getEntities(EntityNPCInterface.class, sender.getEntityWorld(), sender.getPosition(), 80);
      Iterator var5 = list.iterator();

      while(var5.hasNext()) {
         EntityNPCInterface npc = (EntityNPCInterface)var5.next();
         if(npc.display.getName().equalsIgnoreCase(args[0])) {
            String name = npc.display.getName();
            if(args.length > 2) {
               name = args[2];
            }

            NBTTagCompound compound = new NBTTagCompound();
            if(!npc.writeToNBTOptional(compound)) {
               return;
            }

            ServerCloneController.Instance.addClone(compound, name, tab);
         }
      }

   }

   @CommandNoppesBase.SubCommand(
      desc = "List NPC from clone storage",
      usage = "<tab>",
      permission = 2
   )
   public void list(ICommandSender sender, String[] args) {
      this.sendMessage(sender, "--- Stored NPCs --- (server side)", new Object[0]);
      int tab = 0;

      try {
         tab = Integer.parseInt(args[0]);
      } catch (NumberFormatException var6) {
         ;
      }

      Iterator ex = ServerCloneController.Instance.getClones(tab).iterator();

      while(ex.hasNext()) {
         String name = (String)ex.next();
         this.sendMessage(sender, name, new Object[0]);
      }

      this.sendMessage(sender, "------------------------------------", new Object[0]);
   }

   @CommandNoppesBase.SubCommand(
      desc = "Remove NPC from clone storage",
      usage = "<name> <tab>",
      permission = 4
   )
   public void del(ICommandSender sender, String[] args) throws CommandException {
      String nametodel = args[0];
      int tab = 0;

      try {
         tab = Integer.parseInt(args[1]);
      } catch (NumberFormatException var8) {
         ;
      }

      boolean deleted = false;
      Iterator var6 = ServerCloneController.Instance.getClones(tab).iterator();

      while(var6.hasNext()) {
         String name = (String)var6.next();
         if(nametodel.equalsIgnoreCase(name)) {
            ServerCloneController.Instance.removeClone(name, tab);
            deleted = true;
            break;
         }
      }

      if(!ServerCloneController.Instance.removeClone(nametodel, tab)) {
         throw new CommandException("Npc \'%s\' wasn\'t found", new Object[]{nametodel});
      }
   }

   @CommandNoppesBase.SubCommand(
      desc = "Spawn cloned NPC",
      usage = "<name> <tab> [[world:]x,y,z]] [newname]",
      permission = 2
   )
   public void spawn(ICommandSender sender, String[] args) throws CommandException {
      String name = args[0].replaceAll("%", " ");
      int tab = 0;

      try {
         tab = Integer.parseInt(args[1]);
      } catch (NumberFormatException var13) {
         ;
      }

      String newname = null;
      NBTTagCompound compound = ServerCloneController.Instance.getCloneData(sender, name, tab);
      if(compound == null) {
         throw new CommandException("Unknown npc", new Object[0]);
      } else {
         World world = sender.getEntityWorld();
         BlockPos pos = sender.getPosition();
         if(args.length > 2) {
            String entity = args[2];
            String[] npc;
            if(entity.contains(":")) {
               npc = entity.split(":");
               entity = npc[1];
               world = this.getWorld(npc[0]);
               if(world == null) {
                  throw new CommandException("\'%s\' is an unknown world", new Object[]{npc[0]});
               }
            }

            if(entity.contains(",")) {
               npc = entity.split(",");
               if(npc.length != 3) {
                  throw new CommandException("Location need be x,y,z", new Object[0]);
               }

               try {
                  pos = CommandBase.parseBlockPos(sender, npc, 0, false);
               } catch (NumberInvalidException var12) {
                  throw new CommandException("Location should be in numbers", new Object[0]);
               }

               if(args.length > 3) {
                  newname = args[3];
               }
            } else {
               newname = entity;
            }
         }

         if(pos.getX() == 0 && pos.getY() == 0 && pos.getZ() == 0) {
            throw new CommandException("Location needed", new Object[0]);
         } else {
            Entity entity1 = EntityList.createEntityFromNBT(compound, world);
            entity1.setPosition((double)pos.getX() + 0.5D, (double)(pos.getY() + 1), (double)pos.getZ() + 0.5D);
            if(entity1 instanceof EntityNPCInterface) {
               EntityNPCInterface npc1 = (EntityNPCInterface)entity1;
               npc1.ai.setStartPos(pos);
               if(newname != null && !newname.isEmpty()) {
                  npc1.display.setName(newname.replaceAll("%", " "));
               }
            }

            world.spawnEntityInWorld(entity1);
         }
      }
   }

   public World getWorld(String t) {
      WorldServer[] ws = MinecraftServer.getServer().worldServers;
      WorldServer[] var3 = ws;
      int var4 = ws.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         WorldServer w = var3[var5];
         if(w != null && (w.provider.getDimensionId() + "").equalsIgnoreCase(t)) {
            return w;
         }
      }

      return null;
   }

   public List getEntities(Class cls, World world, BlockPos pos, int range) {
      return world.getEntitiesWithinAABB(cls, (new AxisAlignedBB(pos, pos.add(1, 1, 1))).expand((double)range, (double)range, (double)range));
   }
}

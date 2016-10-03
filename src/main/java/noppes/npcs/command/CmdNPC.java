package noppes.npcs.command;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.roles.RoleFollower;

public class CmdNPC extends CommandNoppesBase {

   public EntityNPCInterface selectedNpc;


   public String getCommandName() {
      return "npc";
   }

   public String getDescription() {
      return "NPC operation";
   }

   public String getUsage() {
      return "<name> <command>";
   }

   public boolean runSubCommands() {
      return false;
   }

   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
      String npcname = args[0].replace("%", " ");
      String command = args[1];
      args = (String[])Arrays.copyOfRange(args, 2, args.length);
      if(command.equalsIgnoreCase("create")) {
         this.processSubCommand(sender, command, new String[]{args[0], npcname});
      } else {
         List list = this.getEntities(EntityNPCInterface.class, sender.getEntityWorld(), sender.getPosition(), 80);
         Iterator var6 = list.iterator();

         while(var6.hasNext()) {
            EntityNPCInterface npc = (EntityNPCInterface)var6.next();
            String name = npc.display.getName().replace(" ", "_");
            if(name.equalsIgnoreCase(npcname) && (this.selectedNpc == null || this.selectedNpc.getDistanceSq(sender.getPosition()) > npc.getDistanceSq(sender.getPosition()))) {
               this.selectedNpc = npc;
            }
         }

         if(this.selectedNpc == null) {
            throw new CommandException("Npc \'%s\' was not found", new Object[]{npcname});
         } else {
            this.processSubCommand(sender, command, args);
            this.selectedNpc = null;
         }
      }
   }

   @CommandNoppesBase.SubCommand(
      desc = "Set Home (respawn place)",
      usage = "[x] [y] [z]"
   )
   public void home(ICommandSender sender, String[] args) {
      BlockPos pos = sender.getPosition();
      if(args.length == 3) {
         try {
            pos = CommandBase.parseBlockPos(sender, args, 0, false);
         } catch (NumberInvalidException var5) {
            ;
         }
      }

      this.selectedNpc.ai.setStartPos(pos);
   }

   @CommandNoppesBase.SubCommand(
      desc = "Set NPC visibility",
      usage = "[true/false/semi]"
   )
   public void visible(ICommandSender sender, String[] args) {
      if(args.length >= 1) {
         boolean bo = args[0].equalsIgnoreCase("true");
         boolean semi = args[0].equalsIgnoreCase("semi");
         int current = this.selectedNpc.display.getVisible();
         if(semi) {
            this.selectedNpc.display.setVisible(2);
         } else if(bo) {
            this.selectedNpc.display.setVisible(0);
         } else {
            this.selectedNpc.display.setVisible(1);
         }

      }
   }

   @CommandNoppesBase.SubCommand(
      desc = "Delete an NPC"
   )
   public void delete(ICommandSender sender, String[] args) {
      this.selectedNpc.delete();
   }

   @CommandNoppesBase.SubCommand(
      desc = "Sets the owner of an follower/companion",
      usage = "[player]"
   )
   public void owner(ICommandSender sender, String[] args) {
      if(args.length < 1) {
         EntityPlayer player = null;
         if(this.selectedNpc.roleInterface instanceof RoleFollower) {
            player = ((RoleFollower)this.selectedNpc.roleInterface).owner;
         }

         if(this.selectedNpc.roleInterface instanceof RoleCompanion) {
            player = ((RoleCompanion)this.selectedNpc.roleInterface).owner;
         }

         if(player == null) {
            this.sendMessage(sender, "No owner", new Object[0]);
         } else {
            this.sendMessage(sender, "Owner is: " + player.getName(), new Object[0]);
         }
      } else {
         EntityPlayerMP player1 = null;

         try {
            player1 = CommandBase.getPlayer(sender, args[0]);
         } catch (PlayerNotFoundException var5) {
            ;
         }

         if(this.selectedNpc.roleInterface instanceof RoleFollower) {
            ((RoleFollower)this.selectedNpc.roleInterface).setOwner(player1);
         }

         if(this.selectedNpc.roleInterface instanceof RoleCompanion) {
            ((RoleCompanion)this.selectedNpc.roleInterface).setOwner(player1);
         }
      }

   }

   @CommandNoppesBase.SubCommand(
      desc = "Set NPC name",
      usage = "[name]"
   )
   public void name(ICommandSender sender, String[] args) {
      if(args.length >= 1) {
         String name = args[0];

         for(int i = 1; i < args.length; ++i) {
            name = name + " " + args[i];
         }

         if(!this.selectedNpc.display.getName().equals(name)) {
            this.selectedNpc.display.setName(name);
            this.selectedNpc.updateClient = true;
         }

      }
   }

   @CommandNoppesBase.SubCommand(
      desc = "Creates an NPC",
      usage = "[name]"
   )
   public void create(ICommandSender sender, String[] args) {
      World pw = sender.getEntityWorld();
      EntityCustomNpc npc = new EntityCustomNpc(pw);
      if(args.length > 0) {
         npc.display.setName(args[0]);
      }

      BlockPos pos = sender.getPosition();
      npc.setPositionAndRotation((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 0.0F, 0.0F);
      this.selectedNpc.ai.setStartPos(pos);
      pw.spawnEntityInWorld(npc);
      npc.setHealth(npc.getMaxHealth());
   }

   public List addTabCompletionOptions(ICommandSender par1, String[] args, BlockPos pos) {
      return args.length == 2?CommandBase.getListOfStringsMatchingLastWord(args, new String[]{"create", "home", "visible", "delete", "owner", "name"}):(args.length == 3 && args[1].equalsIgnoreCase("owner")?CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()):null);
   }

   public int getRequiredPermissionLevel() {
      return 4;
   }

   public List getEntities(Class cls, World world, BlockPos pos, int range) {
      return world.getEntitiesWithinAABB(cls, (new AxisAlignedBB(pos, pos.add(1, 1, 1))).expand((double)range, (double)range, (double)range));
   }
}

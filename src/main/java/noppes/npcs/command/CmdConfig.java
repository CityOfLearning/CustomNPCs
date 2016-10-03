package noppes.npcs.command;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.BlockVine;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.CustomNpcs;
import noppes.npcs.Server;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.ChunkController;

public class CmdConfig extends CommandNoppesBase {

   public String getCommandName() {
      return "config";
   }

   public String getDescription() {
      return "Some config things you can set";
   }

   @CommandNoppesBase.SubCommand(
      desc = "Disable/Enable the natural leaves decay",
      usage = "[true/false]",
      permission = 4
   )
   public void leavesdecay(ICommandSender sender, String[] args) {
      if(args.length == 0) {
         this.sendMessage(sender, "LeavesDecay: " + CustomNpcs.LeavesDecayEnabled, new Object[0]);
      } else {
         CustomNpcs.LeavesDecayEnabled = Boolean.parseBoolean(args[0]);
         CustomNpcs.Config.updateConfig();
         Set names = Block.blockRegistry.getKeys();
         Iterator var4 = names.iterator();

         while(var4.hasNext()) {
            ResourceLocation name = (ResourceLocation)var4.next();
            Block block = (Block)Block.blockRegistry.getObject(name);
            if(block instanceof BlockLeavesBase) {
               block.setTickRandomly(CustomNpcs.LeavesDecayEnabled);
            }
         }

         this.sendMessage(sender, "LeavesDecay is now " + CustomNpcs.LeavesDecayEnabled, new Object[0]);
      }

   }

   @CommandNoppesBase.SubCommand(
      desc = "Disable/Enable the vines growing",
      usage = "[true/false]",
      permission = 4
   )
   public void vinegrowth(ICommandSender sender, String[] args) {
      if(args.length == 0) {
         this.sendMessage(sender, "VineGrowth: " + CustomNpcs.VineGrowthEnabled, new Object[0]);
      } else {
         CustomNpcs.VineGrowthEnabled = Boolean.parseBoolean(args[0]);
         CustomNpcs.Config.updateConfig();
         Set names = Block.blockRegistry.getKeys();
         Iterator var4 = names.iterator();

         while(var4.hasNext()) {
            ResourceLocation name = (ResourceLocation)var4.next();
            Block block = (Block)Block.blockRegistry.getObject(name);
            if(block instanceof BlockVine) {
               block.setTickRandomly(CustomNpcs.VineGrowthEnabled);
            }
         }

         this.sendMessage(sender, "VineGrowth is now " + CustomNpcs.VineGrowthEnabled, new Object[0]);
      }

   }

   @CommandNoppesBase.SubCommand(
      desc = "Disable/Enable the ice melting",
      usage = "[true/false]",
      permission = 4
   )
   public void icemelts(ICommandSender sender, String[] args) {
      if(args.length == 0) {
         this.sendMessage(sender, "IceMelts: " + CustomNpcs.IceMeltsEnabled, new Object[0]);
      } else {
         CustomNpcs.IceMeltsEnabled = Boolean.parseBoolean(args[0]);
         CustomNpcs.Config.updateConfig();
         Set names = Block.blockRegistry.getKeys();
         Iterator var4 = names.iterator();

         while(var4.hasNext()) {
            ResourceLocation name = (ResourceLocation)var4.next();
            Block block = (Block)Block.blockRegistry.getObject(name);
            if(block instanceof BlockIce) {
               block.setTickRandomly(CustomNpcs.IceMeltsEnabled);
            }
         }

         this.sendMessage(sender, "IceMelts is now " + CustomNpcs.IceMeltsEnabled, new Object[0]);
      }

   }

   @CommandNoppesBase.SubCommand(
      desc = "Disable/Enable guns shooting",
      usage = "[true/false]",
      permission = 4
   )
   public void guns(ICommandSender sender, String[] args) {
      if(args.length == 0) {
         this.sendMessage(sender, "GunsEnabled: " + CustomNpcs.GunsEnabled, new Object[0]);
      } else {
         CustomNpcs.GunsEnabled = Boolean.parseBoolean(args[0]);
         CustomNpcs.Config.updateConfig();
         this.sendMessage(sender, "GunsEnabled is now " + CustomNpcs.GunsEnabled, new Object[0]);
      }

   }

   @CommandNoppesBase.SubCommand(
      desc = "Freezes/Unfreezes npcs",
      usage = "[true/false]",
      permission = 4
   )
   public void freezenpcs(ICommandSender sender, String[] args) {
      if(args.length == 0) {
         this.sendMessage(sender, "Frozen NPCs: " + CustomNpcs.FreezeNPCs, new Object[0]);
      } else {
         CustomNpcs.FreezeNPCs = Boolean.parseBoolean(args[0]);
         this.sendMessage(sender, "FrozenNPCs is now " + CustomNpcs.FreezeNPCs, new Object[0]);
      }

   }

   @CommandNoppesBase.SubCommand(
      desc = "Set how many active chunkloaders you can have",
      usage = "<number>",
      permission = 4
   )
   public void chunkloaders(ICommandSender sender, String[] args) throws CommandException {
      if(args.length == 0) {
         this.sendMessage(sender, "ChunkLoaders: " + ChunkController.instance.size() + "/" + CustomNpcs.ChuckLoaders, new Object[0]);
      } else {
         try {
            CustomNpcs.ChuckLoaders = Integer.parseInt(args[0]);
         } catch (NumberFormatException var4) {
            throw new CommandException("Didnt get a number", new Object[0]);
         }

         CustomNpcs.Config.updateConfig();
         int size = ChunkController.instance.size();
         if(size > CustomNpcs.ChuckLoaders) {
            ChunkController.instance.unload(size - CustomNpcs.ChuckLoaders);
            this.sendMessage(sender, size - CustomNpcs.ChuckLoaders + " chunksloaders unloaded", new Object[0]);
         }

         this.sendMessage(sender, "ChunkLoaders: " + ChunkController.instance.size() + "/" + CustomNpcs.ChuckLoaders, new Object[0]);
      }

   }

   @CommandNoppesBase.SubCommand(
      desc = "Get/Set font",
      usage = "[type] [size]",
      permission = 4
   )
   public void font(ICommandSender sender, String[] args) {
      if(sender instanceof EntityPlayerMP) {
         int size = 18;
         if(args.length > 1) {
            try {
               size = Integer.parseInt(args[args.length - 1]);
               args = (String[])Arrays.copyOfRange(args, 0, args.length - 1);
            } catch (Exception var6) {
               ;
            }
         }

         String font = "";

         for(int i = 0; i < args.length; ++i) {
            font = font + " " + args[i];
         }

         Server.sendData((EntityPlayerMP)sender, EnumPacketClient.CONFIG, new Object[]{Integer.valueOf(0), font.trim(), Integer.valueOf(size)});
      }
   }
}

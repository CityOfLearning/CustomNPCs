package noppes.npcs.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.LogWriter;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.Schematic;

public class SchematicController {

   public static SchematicController Instance = new SchematicController();
   private Schematic building = null;
   private ICommandSender buildStarter = null;
   private int buildingPercentage = 0;
   public List included = Arrays.asList(new String[]{"Archery_Range", "Bakery", "Barn", "Building_Site", "Chapel", "Church", "Gate", "Glassworks", "Guard_Tower", "Guild_House", "House", "House_Small", "Inn", "Library", "Lighthouse", "Mill", "Observatory", "Ship", "Shop", "Stall", "Stall2", "Stall3", "Tier_House1", "Tier_House2", "Tier_House3", "Tower", "Wall", "Wall_Corner"});


   public List list() {
      ArrayList list = new ArrayList();
      list.addAll(this.included);
      File[] var2 = this.getDir().listFiles();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         File file = var2[var4];
         String name = file.getName();
         if(name.toLowerCase().endsWith(".schematic") && !name.contains(" ")) {
            list.add(name.substring(0, name.length() - 10));
         }
      }

      Collections.sort(list);
      return list;
   }

   public File getDir() {
      File dir = new File(CustomNpcs.getWorldSaveDirectory(), "schematics");
      if(!dir.exists()) {
         dir.mkdir();
      }

      return dir;
   }

   public void info(ICommandSender sender) {
      if(this.building == null) {
         this.sendMessage(sender, "Nothing is being build");
      } else {
         this.sendMessage(sender, "Already building: " + this.building.name + " - " + this.building.getPercentage() + "%");
         if(this.buildStarter != null) {
            this.sendMessage(sender, "Build started by: " + this.buildStarter.getName());
         }
      }

   }

   private void sendMessage(ICommandSender sender, String message) {
      if(sender != null) {
         sender.addChatMessage(new ChatComponentText(message));
      }
   }

   public void stop(ICommandSender sender) {
      if(this.building != null && this.building.isBuilding) {
         this.sendMessage(sender, "Stopped building: " + this.building.name);
         this.building = null;
      } else {
         this.sendMessage(sender, "Not building");
      }

   }

   public void build(Schematic schem, ICommandSender sender) {
      if(this.building != null && this.building.isBuilding) {
         this.info(sender);
      } else {
         this.buildingPercentage = 0;
         this.building = schem;
         this.building.isBuilding = true;
         this.buildStarter = sender;
      }
   }

   public void updateBuilding() {
      if(this.building != null) {
         this.building.build();
         if(this.buildStarter != null && this.building.getPercentage() - this.buildingPercentage >= 10) {
            this.sendMessage(this.buildStarter, "Building at " + this.building.getPercentage() + "%");
            this.buildingPercentage = this.building.getPercentage();
         }

         if(!this.building.isBuilding) {
            if(this.buildStarter != null) {
               this.sendMessage(this.buildStarter, "Building finished");
            }

            this.building = null;
         }

      }
   }

   public Schematic load(String name) {
      Object stream = null;
      if(this.included.contains(name)) {
         stream = MinecraftServer.class.getResourceAsStream("/assets/customnpcs/schematics/" + name + ".schematic");
      }

      if(stream == null) {
         File e = new File(this.getDir(), name + ".schematic");
         if(!e.exists()) {
            return null;
         }

         try {
            stream = new FileInputStream(e);
         } catch (FileNotFoundException var6) {
            return null;
         }
      }

      try {
         Schematic e1 = new Schematic(name);
         e1.load(CompressedStreamTools.readCompressed((InputStream)stream));
         ((InputStream)stream).close();
         return e1;
      } catch (IOException var5) {
         LogWriter.except(var5);
         return null;
      }
   }

   public void save(ICommandSender sender, String name, BlockPos pos, short height, short width, short length) {
      name = name.replace(" ", "_");
      if(!this.included.contains(name)) {
         Schematic schema = new Schematic(name);
         schema.height = height;
         schema.width = width;
         schema.length = length;
         schema.size = height * width * length;
         schema.blockArray = new short[schema.size];
         schema.blockDataArray = new byte[schema.size];
         NoppesUtilServer.NotifyOPs("Creating schematic at: " + pos + " might lag slightly", new Object[0]);
         World world = sender.getEntityWorld();
         schema.tileList = new NBTTagList();

         for(int file = 0; file < schema.size; ++file) {
            int e = file % width;
            int z = (file - e) / width % length;
            int y = ((file - e) / width - z) / length;
            IBlockState state = world.getBlockState(pos.add(e, y, z));
            if(state.getBlock() != Blocks.air && state.getBlock() != CustomItems.copy) {
               schema.blockArray[file] = (short)Block.blockRegistry.getIDForObject(state.getBlock());
               schema.blockDataArray[file] = (byte)state.getBlock().getMetaFromState(state);
               if(state.getBlock() instanceof ITileEntityProvider) {
                  TileEntity tile = world.getTileEntity(pos.add(e, y, z));
                  NBTTagCompound compound = new NBTTagCompound();
                  tile.writeToNBT(compound);
                  compound.setInteger("x", e);
                  compound.setInteger("y", y);
                  compound.setInteger("z", z);
                  schema.tileList.appendTag(compound);
               }
            }
         }

         File var17 = new File(this.getDir(), name + ".schematic");
         NoppesUtilServer.NotifyOPs("Schematic " + name + " succesfully created", new Object[0]);

         try {
            CompressedStreamTools.writeCompressed(schema.save(), new FileOutputStream(var17));
         } catch (Exception var16) {
            var16.printStackTrace();
         }

      }
   }

}

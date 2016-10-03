package noppes.npcs.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import noppes.npcs.CustomNpcs;
import noppes.npcs.LogWriter;
import noppes.npcs.controllers.Bank;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.PlayerBankData;
import noppes.npcs.controllers.PlayerData;
import noppes.npcs.controllers.PlayerMail;
import noppes.npcs.util.NBTJsonUtil;

public class PlayerDataController {

   public static PlayerDataController instance;


   public PlayerDataController() {
      instance = this;
   }

   public File getSaveDir() {
      try {
         File e = new File(CustomNpcs.getWorldSaveDirectory(), "playerdata");
         if(!e.exists()) {
            e.mkdir();
         }

         return e;
      } catch (Exception var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public NBTTagCompound loadPlayerDataOld(String player) {
      File saveDir = this.getSaveDir();
      String filename = player;
      if(player.isEmpty()) {
         filename = "noplayername";
      }

      filename = filename + ".dat";

      File e;
      try {
         e = new File(saveDir, filename);
         if(e.exists()) {
            NBTTagCompound comp = CompressedStreamTools.readCompressed(new FileInputStream(e));
            e.delete();
            e = new File(saveDir, filename + "_old");
            if(e.exists()) {
               e.delete();
            }

            return comp;
         }
      } catch (Exception var7) {
         LogWriter.except(var7);
      }

      try {
         e = new File(saveDir, filename + "_old");
         if(e.exists()) {
            return CompressedStreamTools.readCompressed(new FileInputStream(e));
         }
      } catch (Exception var6) {
         LogWriter.except(var6);
      }

      return new NBTTagCompound();
   }

   public NBTTagCompound loadPlayerData(String player) {
      File saveDir = this.getSaveDir();
      String filename = player;
      if(player.isEmpty()) {
         filename = "noplayername";
      }

      filename = filename + ".json";
      File file = null;

      try {
         file = new File(saveDir, filename);
         if(file.exists()) {
            return NBTJsonUtil.LoadFile(file);
         }
      } catch (Exception var6) {
         LogWriter.error("Error loading: " + file.getAbsolutePath(), var6);
      }

      return new NBTTagCompound();
   }

   public void savePlayerData(PlayerData data) {
      NBTTagCompound compound = data.getNBT();
      String filename = data.uuid + ".json";

      try {
         File e = this.getSaveDir();
         File file = new File(e, filename + "_new");
         File file1 = new File(e, filename);
         NBTJsonUtil.SaveFile(file, compound);
         if(file1.exists()) {
            file1.delete();
         }

         file.renameTo(file1);
      } catch (Exception var7) {
         LogWriter.except(var7);
      }

   }

   public PlayerBankData getBankData(EntityPlayer player, int bankId) {
      Bank bank = BankController.getInstance().getBank(bankId);
      PlayerBankData data = this.getPlayerData(player).bankData;
      if(!data.hasBank(bank.id)) {
         data.loadNew(bank.id);
      }

      return data;
   }

   public PlayerData getPlayerData(EntityPlayer player) {
      PlayerData data = (PlayerData)player.getExtendedProperties("CustomNpcsData");
      if(data == null) {
         player.registerExtendedProperties("CustomNpcsData", data = new PlayerData());
         data.player = player;
         data.loadNBTData((NBTTagCompound)null);
      }

      data.player = player;
      return data;
   }

   public String hasPlayer(String username) {
      Iterator var2 = this.getUsernameData().keySet().iterator();

      String name;
      do {
         if(!var2.hasNext()) {
            return "";
         }

         name = (String)var2.next();
      } while(!name.equalsIgnoreCase(username));

      return name;
   }

   public PlayerData getDataFromUsername(String username) {
      EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(username);
      PlayerData data = null;
      if(player == null) {
         Map map = this.getUsernameData();
         Iterator var5 = map.keySet().iterator();

         while(var5.hasNext()) {
            String name = (String)var5.next();
            if(name.equalsIgnoreCase(username)) {
               data = new PlayerData();
               data.setNBT((NBTTagCompound)map.get(name));
               break;
            }
         }
      } else {
         data = this.getPlayerData(player);
      }

      return data;
   }

   public void addPlayerMessage(String username, PlayerMail mail) {
      mail.time = System.currentTimeMillis();
      EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(username);
      PlayerData data = this.getDataFromUsername(username);
      data.mailData.playermail.add(mail.copy());
      this.savePlayerData(data);
   }

   public Map getUsernameData() {
      HashMap map = new HashMap();
      File[] var2 = this.getSaveDir().listFiles();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         File file = var2[var4];
         if(!file.isDirectory() && file.getName().endsWith(".json")) {
            try {
               NBTTagCompound e = NBTJsonUtil.LoadFile(file);
               if(e.hasKey("PlayerName")) {
                  map.put(e.getString("PlayerName"), e);
               }
            } catch (Exception var7) {
               LogWriter.error("Error loading: " + file.getAbsolutePath(), var7);
            }
         }
      }

      return map;
   }

   public List getPlayersData(ICommandSender sender, String username) {
      ArrayList list = new ArrayList();
      List players = PlayerSelector.matchEntities(sender, username, EntityPlayerMP.class);
      if(players.isEmpty()) {
         PlayerData data = instance.getDataFromUsername(username);
         if(data != null) {
            list.add(data);
         }
      } else {
         Iterator data1 = players.iterator();

         while(data1.hasNext()) {
            EntityPlayer player = (EntityPlayer)data1.next();
            list.add(instance.getPlayerData(player));
         }
      }

      return list;
   }

   public boolean hasMail(EntityPlayer player) {
      return this.getPlayerData(player).mailData.hasMail();
   }
}

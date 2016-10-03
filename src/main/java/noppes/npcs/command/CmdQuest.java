package noppes.npcs.command;

import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.Server;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.PlayerData;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.Quest;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.QuestData;

public class CmdQuest extends CommandNoppesBase {

   public String getCommandName() {
      return "quest";
   }

   public String getDescription() {
      return "Quest operations";
   }

   @CommandNoppesBase.SubCommand(
      desc = "Start a quest",
      usage = "<player> <quest>"
   )
   public void start(ICommandSender sender, String[] args) throws CommandException {
      String playername = args[0];

      int questid;
      try {
         questid = Integer.parseInt(args[1]);
      } catch (NumberFormatException var10) {
         throw new CommandException("QuestID must be an integer", new Object[0]);
      }

      List data = PlayerDataController.instance.getPlayersData(sender, playername);
      if(data.isEmpty()) {
         throw new CommandException("Unknow player \'%s\'", new Object[]{playername});
      } else {
         Quest quest = (Quest)QuestController.instance.quests.get(Integer.valueOf(questid));
         if(quest == null) {
            throw new CommandException("Unknown QuestID", new Object[0]);
         } else {
            Iterator var7 = data.iterator();

            while(var7.hasNext()) {
               PlayerData playerdata = (PlayerData)var7.next();
               QuestData questdata = new QuestData(quest);
               playerdata.questData.activeQuests.put(Integer.valueOf(questid), questdata);
               playerdata.saveNBTData((NBTTagCompound)null);
               Server.sendData((EntityPlayerMP)playerdata.player, EnumPacketClient.MESSAGE, new Object[]{"quest.newquest", quest.title});
               Server.sendData((EntityPlayerMP)playerdata.player, EnumPacketClient.CHAT, new Object[]{"quest.newquest", ": ", quest.title});
            }

         }
      }
   }

   @CommandNoppesBase.SubCommand(
      desc = "Finish a quest",
      usage = "<player> <quest>"
   )
   public void finish(ICommandSender sender, String[] args) throws CommandException {
      String playername = args[0];

      int questid;
      try {
         questid = Integer.parseInt(args[1]);
      } catch (NumberFormatException var9) {
         throw new CommandException("QuestID must be an integer", new Object[0]);
      }

      List data = PlayerDataController.instance.getPlayersData(sender, playername);
      if(data.isEmpty()) {
         throw new CommandException(String.format("Unknow player \'%s\'", new Object[]{playername}), new Object[0]);
      } else {
         Quest quest = (Quest)QuestController.instance.quests.get(Integer.valueOf(questid));
         if(quest == null) {
            throw new CommandException("Unknown QuestID", new Object[0]);
         } else {
            Iterator var7 = data.iterator();

            while(var7.hasNext()) {
               PlayerData playerdata = (PlayerData)var7.next();
               playerdata.questData.finishedQuests.put(Integer.valueOf(questid), Long.valueOf(System.currentTimeMillis()));
               playerdata.saveNBTData((NBTTagCompound)null);
            }

         }
      }
   }

   @CommandNoppesBase.SubCommand(
      desc = "Stop a started quest",
      usage = "<player> <quest>"
   )
   public void stop(ICommandSender sender, String[] args) throws CommandException {
      String playername = args[0];

      int questid;
      try {
         questid = Integer.parseInt(args[1]);
      } catch (NumberFormatException var9) {
         throw new CommandException("QuestID must be an integer", new Object[0]);
      }

      List data = PlayerDataController.instance.getPlayersData(sender, playername);
      if(data.isEmpty()) {
         throw new CommandException(String.format("Unknow player \'%s\'", new Object[]{playername}), new Object[0]);
      } else {
         Quest quest = (Quest)QuestController.instance.quests.get(Integer.valueOf(questid));
         if(quest == null) {
            throw new CommandException("Unknown QuestID", new Object[0]);
         } else {
            Iterator var7 = data.iterator();

            while(var7.hasNext()) {
               PlayerData playerdata = (PlayerData)var7.next();
               playerdata.questData.activeQuests.remove(Integer.valueOf(questid));
               playerdata.saveNBTData((NBTTagCompound)null);
            }

         }
      }
   }

   @CommandNoppesBase.SubCommand(
      desc = "Removes a quest from finished and active quests",
      usage = "<player> <quest>"
   )
   public void remove(ICommandSender sender, String[] args) throws CommandException {
      String playername = args[0];

      int questid;
      try {
         questid = Integer.parseInt(args[1]);
      } catch (NumberFormatException var9) {
         throw new CommandException("QuestID must be an integer", new Object[0]);
      }

      List data = PlayerDataController.instance.getPlayersData(sender, playername);
      if(data.isEmpty()) {
         throw new CommandException(String.format("Unknow player \'%s\'", new Object[]{playername}), new Object[0]);
      } else {
         Quest quest = (Quest)QuestController.instance.quests.get(Integer.valueOf(questid));
         if(quest == null) {
            throw new CommandException("Unknown QuestID", new Object[0]);
         } else {
            Iterator var7 = data.iterator();

            while(var7.hasNext()) {
               PlayerData playerdata = (PlayerData)var7.next();
               playerdata.questData.activeQuests.remove(Integer.valueOf(questid));
               playerdata.questData.finishedQuests.remove(Integer.valueOf(questid));
               playerdata.saveNBTData((NBTTagCompound)null);
            }

         }
      }
   }

   @CommandNoppesBase.SubCommand(
      desc = "reload quests from disk",
      permission = 4
   )
   public void reload(ICommandSender sender, String[] args) {
      new DialogController();
   }
}

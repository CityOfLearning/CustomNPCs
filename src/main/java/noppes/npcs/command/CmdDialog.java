package noppes.npcs.command;

import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.controllers.Dialog;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.DialogOption;
import noppes.npcs.controllers.PlayerData;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.entity.EntityDialogNpc;

public class CmdDialog extends CommandNoppesBase {

   public String getCommandName() {
      return "dialog";
   }

   public String getDescription() {
      return "Dialog operations";
   }

   @CommandNoppesBase.SubCommand(
      desc = "force read",
      usage = "<player> <dialog>"
   )
   public void read(ICommandSender sender, String[] args) throws CommandException {
      String playername = args[0];

      int diagid;
      try {
         diagid = Integer.parseInt(args[1]);
      } catch (NumberFormatException var8) {
         throw new CommandException("DialogID must be an integer", new Object[0]);
      }

      List data = PlayerDataController.instance.getPlayersData(sender, playername);
      if(data.isEmpty()) {
         throw new CommandException("Unknow player \'%s\'", new Object[]{playername});
      } else {
         Iterator var6 = data.iterator();

         while(var6.hasNext()) {
            PlayerData playerdata = (PlayerData)var6.next();
            playerdata.dialogData.dialogsRead.add(Integer.valueOf(diagid));
            playerdata.saveNBTData((NBTTagCompound)null);
         }

      }
   }

   @CommandNoppesBase.SubCommand(
      desc = "force unread dialog",
      usage = "<player> <dialog>"
   )
   public void unread(ICommandSender sender, String[] args) throws CommandException {
      String playername = args[0];

      int diagid;
      try {
         diagid = Integer.parseInt(args[1]);
      } catch (NumberFormatException var8) {
         throw new CommandException("DialogID must be an integer", new Object[0]);
      }

      List data = PlayerDataController.instance.getPlayersData(sender, playername);
      if(data.isEmpty()) {
         throw new CommandException("Unknow player \'%s\'", new Object[]{playername});
      } else {
         Iterator var6 = data.iterator();

         while(var6.hasNext()) {
            PlayerData playerdata = (PlayerData)var6.next();
            playerdata.dialogData.dialogsRead.remove(Integer.valueOf(diagid));
            playerdata.saveNBTData((NBTTagCompound)null);
         }

      }
   }

   @CommandNoppesBase.SubCommand(
      desc = "reload dialogs from disk",
      permission = 4
   )
   public void reload(ICommandSender sender, String[] args) {
      new DialogController();
   }

   @CommandNoppesBase.SubCommand(
      desc = "show dialog",
      usage = "<player> <dialog> <name>"
   )
   public void show(ICommandSender sender, String[] args) throws CommandException {
      EntityPlayerMP player = CommandBase.getPlayer(sender, args[0]);
      if(player == null) {
         throw new CommandException("Unknow player \'%s\'", new Object[]{args[0]});
      } else {
         int diagid;
         try {
            diagid = Integer.parseInt(args[1]);
         } catch (NumberFormatException var8) {
            throw new CommandException("DialogID must be an integer: " + args[1], new Object[0]);
         }

         Dialog dialog = (Dialog)DialogController.instance.dialogs.get(Integer.valueOf(diagid));
         if(dialog == null) {
            throw new CommandException("Unknown dialog id: " + args[1], new Object[0]);
         } else {
            EntityDialogNpc npc = new EntityDialogNpc(sender.getEntityWorld());
            npc.display.setName(args[2]);
            EntityUtil.Copy(player, npc);
            DialogOption option = new DialogOption();
            option.dialogId = diagid;
            option.title = dialog.title;
            npc.dialogs.put(Integer.valueOf(0), option);
            NoppesUtilServer.openDialog(player, npc, dialog);
         }
      }
   }
}

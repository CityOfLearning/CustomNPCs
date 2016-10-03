package noppes.npcs.command;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.controllers.Faction;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.PlayerData;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.PlayerFactionData;

public class CmdFaction extends CommandNoppesBase {

   public Faction selectedFaction;
   public List data;


   public String getCommandName() {
      return "faction";
   }

   public String getDescription() {
      return "Faction operations";
   }

   public String getUsage() {
      return "<player> <faction> <command>";
   }

   public boolean runSubCommands() {
      return false;
   }

   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
      String playername = args[0];
      String factionname = args[1];
      this.data = PlayerDataController.instance.getPlayersData(sender, playername);
      if(this.data.isEmpty()) {
         throw new CommandException("Unknow player \'%s\'", new Object[]{playername});
      } else {
         try {
            this.selectedFaction = FactionController.getInstance().getFaction(Integer.parseInt(factionname));
         } catch (NumberFormatException var7) {
            this.selectedFaction = FactionController.getInstance().getFactionFromName(factionname);
         }

         if(this.selectedFaction == null) {
            throw new CommandException("Unknow facion \'%s", new Object[]{factionname});
         } else {
            this.processSubCommand(sender, args[2], (String[])Arrays.copyOfRange(args, 3, args.length));
            Iterator e = this.data.iterator();

            while(e.hasNext()) {
               PlayerData playerdata = (PlayerData)e.next();
               playerdata.saveNBTData((NBTTagCompound)null);
            }

         }
      }
   }

   @CommandNoppesBase.SubCommand(
      desc = "Add points",
      usage = "<points>"
   )
   public void add(ICommandSender sender, String[] args) throws CommandException {
      int points;
      try {
         points = Integer.parseInt(args[0]);
      } catch (NumberFormatException var8) {
         throw new CommandException("Must be an integer", new Object[0]);
      }

      int factionid = this.selectedFaction.id;
      Iterator var5 = this.data.iterator();

      while(var5.hasNext()) {
         PlayerData playerdata = (PlayerData)var5.next();
         PlayerFactionData playerfactiondata = playerdata.factionData;
         playerfactiondata.increasePoints(factionid, points);
      }

   }

   @CommandNoppesBase.SubCommand(
      desc = "Substract points",
      usage = "<points>"
   )
   public void subtract(ICommandSender sender, String[] args) throws CommandException {
      int points;
      try {
         points = Integer.parseInt(args[0]);
      } catch (NumberFormatException var8) {
         throw new CommandException("Must be an integer", new Object[0]);
      }

      int factionid = this.selectedFaction.id;
      Iterator var5 = this.data.iterator();

      while(var5.hasNext()) {
         PlayerData playerdata = (PlayerData)var5.next();
         PlayerFactionData playerfactiondata = playerdata.factionData;
         playerfactiondata.increasePoints(factionid, -points);
      }

   }

   @CommandNoppesBase.SubCommand(
      desc = "Reset points to default"
   )
   public void reset(ICommandSender sender, String[] args) {
      Iterator var3 = this.data.iterator();

      while(var3.hasNext()) {
         PlayerData playerdata = (PlayerData)var3.next();
         playerdata.factionData.factionData.put(Integer.valueOf(this.selectedFaction.id), Integer.valueOf(this.selectedFaction.defaultPoints));
      }

   }

   @CommandNoppesBase.SubCommand(
      desc = "Set points",
      usage = "<points>"
   )
   public void set(ICommandSender sender, String[] args) throws CommandException {
      int points;
      try {
         points = Integer.parseInt(args[0]);
      } catch (NumberFormatException var7) {
         throw new CommandException("Must be an integer", new Object[0]);
      }

      Iterator ex = this.data.iterator();

      while(ex.hasNext()) {
         PlayerData playerdata = (PlayerData)ex.next();
         PlayerFactionData playerfactiondata = playerdata.factionData;
         playerfactiondata.factionData.put(Integer.valueOf(this.selectedFaction.id), Integer.valueOf(points));
      }

   }

   @CommandNoppesBase.SubCommand(
      desc = "Drop relationship"
   )
   public void drop(ICommandSender sender, String[] args) {
      Iterator var3 = this.data.iterator();

      while(var3.hasNext()) {
         PlayerData playerdata = (PlayerData)var3.next();
         playerdata.factionData.factionData.remove(Integer.valueOf(this.selectedFaction.id));
      }

   }

   public List addTabCompletionOptions(ICommandSender par1, String[] args, BlockPos pos) {
      return args.length == 3?CommandBase.getListOfStringsMatchingLastWord(args, new String[]{"add", "subtract", "set", "reset", "drop", "create"}):null;
   }
}

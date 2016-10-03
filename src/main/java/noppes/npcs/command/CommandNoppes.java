package noppes.npcs.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.command.CmdDialog;
import noppes.npcs.command.CmdFaction;
import noppes.npcs.command.CmdHelp;
import noppes.npcs.command.CmdNPC;
import noppes.npcs.command.CmdQuest;
import noppes.npcs.command.CmdScene;
import noppes.npcs.command.CmdSchematics;
import noppes.npcs.command.CmdScript;
import noppes.npcs.command.CmdSlay;

public class CommandNoppes extends CommandBase {

   public Map map = new HashMap();
   public CmdHelp help = new CmdHelp(this);


   public CommandNoppes() {
      this.registerCommand(this.help);
      this.registerCommand(new CmdScript());
      this.registerCommand(new CmdScene());
      this.registerCommand(new CmdSlay());
      this.registerCommand(new CmdQuest());
      this.registerCommand(new CmdDialog());
      this.registerCommand(new CmdSchematics());
      this.registerCommand(new CmdFaction());
      this.registerCommand(new CmdNPC());
   }

   public void registerCommand(CommandNoppesBase command) {
      String name = command.getCommandName().toLowerCase();
      if(this.map.containsKey(name)) {
         throw new CustomNPCsException("Already a subcommand with the name: " + name, new Object[0]);
      } else {
         this.map.put(name, command);
      }
   }

   public String getCommandName() {
      return "noppes";
   }

   public String getCommandUsage(ICommandSender sender) {
      return "Use as /noppes subcommand";
   }

   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
      if(args.length == 0) {
         this.help.processCommand(sender, args);
      } else {
         CommandNoppesBase command = this.getCommand(args);
         if(command == null) {
            throw new CommandException("Unknown command " + args[0], new Object[0]);
         } else {
            args = (String[])Arrays.copyOfRange(args, 1, args.length);
            if(!command.subcommands.isEmpty() && command.runSubCommands()) {
               if(args.length == 0) {
                  this.help.processCommand(sender, new String[]{command.getCommandName()});
               } else {
                  command.processSubCommand(sender, args[0], (String[])Arrays.copyOfRange(args, 1, args.length));
               }
            } else if(!sender.canCommandSenderUseCommand(command.getRequiredPermissionLevel(), "commands.noppes." + command.getCommandName().toLowerCase())) {
               throw new CommandException("You are not allowed to use this command", new Object[0]);
            } else {
               command.canRun(sender, command.getUsage(), args);
               command.processCommand(sender, args);
            }
         }
      }
   }

   public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
      if(args.length == 1) {
         return new ArrayList(this.map.keySet());
      } else {
         CommandNoppesBase command = this.getCommand(args);
         return (List)(command == null?null:(args.length == 2 && command.runSubCommands()?new ArrayList(command.subcommands.keySet()):command.addTabCompletionOptions(sender, (String[])Arrays.copyOfRange(args, 1, args.length), pos)));
      }
   }

   public CommandNoppesBase getCommand(String[] args) {
      return args.length == 0?null:(CommandNoppesBase)this.map.get(args[0].toLowerCase());
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }
}

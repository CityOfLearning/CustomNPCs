package noppes.npcs.command;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.command.CommandNoppes;

public class CmdHelp extends CommandNoppesBase {

   private CommandNoppes parent;


   public CmdHelp(CommandNoppes parent) {
      this.parent = parent;
   }

   public String getCommandName() {
      return "help";
   }

   public String getDescription() {
      return "help [command]";
   }

   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
      if(args.length == 0) {
         this.sendMessage(sender, "------Noppes Commands------", new Object[0]);
         Iterator command1 = this.parent.map.entrySet().iterator();

         while(command1.hasNext()) {
            Entry m1 = (Entry)command1.next();
            this.sendMessage(sender, (String)m1.getKey() + ": " + ((CommandNoppesBase)m1.getValue()).getCommandUsage(sender), new Object[0]);
         }

      } else {
         CommandNoppesBase command = this.parent.getCommand(args);
         if(command == null) {
            throw new CommandException("Unknown command " + args[0], new Object[0]);
         } else if(command.subcommands.isEmpty()) {
            sender.addChatMessage(new ChatComponentTranslation(command.getCommandUsage(sender), new Object[0]));
         } else {
            Method m = null;
            if(args.length > 1) {
               m = (Method)command.subcommands.get(args[1].toLowerCase());
            }

            if(m == null) {
               this.sendMessage(sender, "------" + command.getCommandName() + " SubCommands------", new Object[0]);
               Iterator sc = command.subcommands.entrySet().iterator();

               while(sc.hasNext()) {
                  Entry entry = (Entry)sc.next();
                  sender.addChatMessage(new ChatComponentTranslation((String)entry.getKey() + ": " + ((CommandNoppesBase.SubCommand)((Method)entry.getValue()).getAnnotation(CommandNoppesBase.SubCommand.class)).desc(), new Object[0]));
               }
            } else {
               this.sendMessage(sender, "------" + command.getCommandName() + "." + args[1].toLowerCase() + " Command------", new Object[0]);
               CommandNoppesBase.SubCommand sc1 = (CommandNoppesBase.SubCommand)m.getAnnotation(CommandNoppesBase.SubCommand.class);
               sender.addChatMessage(new ChatComponentTranslation(sc1.desc(), new Object[0]));
               if(!sc1.usage().isEmpty()) {
                  sender.addChatMessage(new ChatComponentTranslation("Usage: " + sc1.usage(), new Object[0]));
               }
            }

         }
      }
   }
}

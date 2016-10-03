package noppes.npcs.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.controllers.ScriptController;

public class CmdScript extends CommandNoppesBase {

   @CommandNoppesBase.SubCommand(
      desc = "Reload scripts and saved data from disks script folder."
   )
   public Boolean reload(ICommandSender sender, String[] args) {
      if(ScriptController.Instance.loadStoredData()) {
         sender.addChatMessage(new ChatComponentText("Reload succesful"));
      } else {
         sender.addChatMessage(new ChatComponentText("Failed reloading stored data"));
      }

      return Boolean.valueOf(true);
   }

   public String getCommandName() {
      return "script";
   }

   public String getDescription() {
      return "Commands for scripts";
   }
}

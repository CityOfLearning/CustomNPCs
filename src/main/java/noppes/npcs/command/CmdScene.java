package noppes.npcs.command;

import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.entity.data.DataScenes;

public class CmdScene extends CommandNoppesBase {

   public String getCommandName() {
      return "scene";
   }

   public String getDescription() {
      return "Scene operations";
   }

   @CommandNoppesBase.SubCommand(
      desc = "Get/Set scene time",
      usage = "[name] [time]"
   )
   public void time(ICommandSender sender, String[] args) throws CommandException {
      if(args.length == 0) {
         this.sendMessage(sender, "Active scenes:", new Object[0]);
         Iterator state = DataScenes.StartedScenes.entrySet().iterator();

         while(state.hasNext()) {
            Entry entry = (Entry)state.next();
            this.sendMessage(sender, "Scene %s time is %s", new Object[]{entry.getKey(), Integer.valueOf(((DataScenes.SceneState)entry.getValue()).ticks)});
         }
      } else if(args.length == 1) {
         int state2 = Integer.parseInt(args[0]);

         DataScenes.SceneState state1;
         for(Iterator entry1 = DataScenes.StartedScenes.values().iterator(); entry1.hasNext(); state1.ticks = state2) {
            state1 = (DataScenes.SceneState)entry1.next();
         }

         this.sendMessage(sender, "All Scene times are set to " + state2, new Object[0]);
      } else {
         DataScenes.SceneState state3 = (DataScenes.SceneState)DataScenes.StartedScenes.get(args[1].toLowerCase());
         if(state3 == null) {
            throw new CommandException("Unknown scene name %s", new Object[]{args[1]});
         }

         state3.ticks = Integer.parseInt(args[0]);
         this.sendMessage(sender, "Scene %s set to %s", new Object[]{args[1], Integer.valueOf(state3.ticks)});
      }

   }

   @CommandNoppesBase.SubCommand(
      desc = "Reset scene",
      usage = "[name]"
   )
   public void reset(ICommandSender sender, String[] args) {
      DataScenes.Reset(sender, args.length == 0?null:args[0]);
   }

   @CommandNoppesBase.SubCommand(
      desc = "Start scene",
      usage = "<name>"
   )
   public void start(ICommandSender sender, String[] args) {
      DataScenes.Start(sender, args[0]);
   }

   @CommandNoppesBase.SubCommand(
      desc = "Pause scene",
      usage = "[name]"
   )
   public void pause(ICommandSender sender, String[] args) {
      DataScenes.Pause(sender, args.length == 0?null:args[0]);
   }
}

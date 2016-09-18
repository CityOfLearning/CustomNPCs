
package noppes.npcs.command;

import java.util.Map;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.entity.data.DataScenes;

public class CmdScene extends CommandNoppesBase {
	@Override
	public String getCommandName() {
		return "scene";
	}

	@Override
	public String getDescription() {
		return "Scene operations";
	}

	@SubCommand(desc = "Pause scene", usage = "[name]")
	public void pause(ICommandSender sender, String[] args) {
		DataScenes.Pause(sender, (args.length == 0) ? null : args[0]);
	}

	@SubCommand(desc = "Reset scene", usage = "[name]")
	public void reset(ICommandSender sender, String[] args) {
		DataScenes.Reset(sender, (args.length == 0) ? null : args[0]);
	}

	@SubCommand(desc = "Start scene", usage = "<name>")
	public void start(ICommandSender sender, String[] args) {
		DataScenes.Start(sender, args[0]);
	}

	@SubCommand(desc = "Get/Set scene time", usage = "[name] [time]")
	public void time(ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0) {
			sendMessage(sender, "Active scenes:", new Object[0]);
			for (Map.Entry<String, DataScenes.SceneState> entry : DataScenes.StartedScenes.entrySet()) {
				sendMessage(sender, "Scene %s time is %s", entry.getKey(), entry.getValue().ticks);
			}
		} else if (args.length == 1) {
			int ticks = Integer.parseInt(args[0]);
			for (DataScenes.SceneState state : DataScenes.StartedScenes.values()) {
				state.ticks = ticks;
			}
			sendMessage(sender, "All Scene times are set to " + ticks, new Object[0]);
		} else {
			DataScenes.SceneState state2 = DataScenes.StartedScenes.get(args[1].toLowerCase());
			if (state2 == null) {
				throw new CommandException("Unknown scene name %s", new Object[] { args[1] });
			}
			state2.ticks = Integer.parseInt(args[0]);
			sendMessage(sender, "Scene %s set to %s", args[1], state2.ticks);
		}
	}
}

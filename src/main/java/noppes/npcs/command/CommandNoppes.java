
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

public class CommandNoppes extends CommandBase {
	public Map<String, CommandNoppesBase> map;
	public CmdHelp help;

	public CommandNoppes() {
		map = new HashMap<String, CommandNoppesBase>();
		registerCommand(help = new CmdHelp(this));
		registerCommand(new CmdScript());
		registerCommand(new CmdScene());
		registerCommand(new CmdSlay());
		registerCommand(new CmdQuest());
		registerCommand(new CmdDialog());
		registerCommand(new CmdSchematics());
		registerCommand(new CmdFaction());
		registerCommand(new CmdNPC());
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		if (args.length == 1) {
			return new ArrayList(map.keySet());
		}
		CommandNoppesBase command = getCommand(args);
		if (command == null) {
			return null;
		}
		if ((args.length == 2) && command.runSubCommands()) {
			return new ArrayList(command.subcommands.keySet());
		}
		return command.addTabCompletionOptions(sender, Arrays.copyOfRange(args, 1, args.length), pos);
	}

	public CommandNoppesBase getCommand(String[] args) {
		if (args.length == 0) {
			return null;
		}
		return map.get(args[0].toLowerCase());
	}

	@Override
	public String getCommandName() {
		return "noppes";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "Use as /noppes subcommand";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0) {
			help.processCommand(sender, args);
			return;
		}
		CommandNoppesBase command = getCommand(args);
		if (command == null) {
			throw new CommandException("Unknown command " + args[0], new Object[0]);
		}
		args = Arrays.copyOfRange(args, 1, args.length);
		if (command.subcommands.isEmpty() || !command.runSubCommands()) {
			if (!sender.canCommandSenderUseCommand(command.getRequiredPermissionLevel(),
					"commands.noppes." + command.getCommandName().toLowerCase())) {
				throw new CommandException("You are not allowed to use this command", new Object[0]);
			}
			command.canRun(sender, command.getUsage(), args);
			command.processCommand(sender, args);
		} else {
			if (args.length == 0) {
				help.processCommand(sender, new String[] { command.getCommandName() });
				return;
			}
			command.processSubCommand(sender, args[0], Arrays.copyOfRange(args, 1, args.length));
		}
	}

	public void registerCommand(CommandNoppesBase command) {
		String name = command.getCommandName().toLowerCase();
		if (map.containsKey(name)) {
			throw new CustomNPCsException("Already a subcommand with the name: " + name, new Object[0]);
		}
		map.put(name, command);
	}
}

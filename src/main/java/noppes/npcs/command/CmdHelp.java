
package noppes.npcs.command;

import java.lang.reflect.Method;
import java.util.Map;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import noppes.npcs.api.CommandNoppesBase;

public class CmdHelp extends CommandNoppesBase {
	private CommandNoppes parent;

	public CmdHelp(CommandNoppes parent) {
		this.parent = parent;
	}

	@Override
	public String getCommandName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "help [command]";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0) {
			sendMessage(sender, "------Noppes Commands------", new Object[0]);
			for (Map.Entry<String, CommandNoppesBase> entry : parent.map.entrySet()) {
				sendMessage(sender, entry.getKey() + ": " + entry.getValue().getCommandUsage(sender), new Object[0]);
			}
			return;
		}
		CommandNoppesBase command = parent.getCommand(args);
		if (command == null) {
			throw new CommandException("Unknown command " + args[0], new Object[0]);
		}
		if (command.subcommands.isEmpty()) {
			sender.addChatMessage(new ChatComponentTranslation(command.getCommandUsage(sender), new Object[0]));
			return;
		}
		Method m = null;
		if (args.length > 1) {
			m = command.subcommands.get(args[1].toLowerCase());
		}
		if (m == null) {
			sendMessage(sender, "------" + command.getCommandName() + " SubCommands------", new Object[0]);
			for (Map.Entry<String, Method> entry2 : command.subcommands.entrySet()) {
				sender.addChatMessage(new ChatComponentTranslation(
						entry2.getKey() + ": " + entry2.getValue().getAnnotation(SubCommand.class).desc(),
						new Object[0]));
			}
		} else {
			sendMessage(sender, "------" + command.getCommandName() + "." + args[1].toLowerCase() + " Command------",
					new Object[0]);
			SubCommand sc = m.getAnnotation(SubCommand.class);
			sender.addChatMessage(new ChatComponentTranslation(sc.desc(), new Object[0]));
			if (!sc.usage().isEmpty()) {
				sender.addChatMessage(new ChatComponentTranslation("Usage: " + sc.usage(), new Object[0]));
			}
		}
	}
}


package noppes.npcs.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;

public abstract class CommandNoppesBase extends CommandBase {
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD })
	public @interface SubCommand {
		String desc();

		String name() default "";

		int permission() default 2;

		String usage() default "";
	}

	public Map<String, Method> subcommands;

	public CommandNoppesBase() {
		subcommands = new HashMap<>();
		for (Method m : this.getClass().getDeclaredMethods()) {
			SubCommand sc = m.getAnnotation(SubCommand.class);
			if (sc != null) {
				String name = sc.name();
				if (name.equals("")) {
					name = m.getName();
				}
				subcommands.put(name.toLowerCase(), m);
			}
		}
	}

	public void canRun(ICommandSender sender, String usage, String[] args) throws CommandException {
		String[] np = usage.split(" ");
		List<String> required = new ArrayList<>();
		for (int i = 0; i < np.length; ++i) {
			String command = np[i];
			if (command.startsWith("<")) {
				required.add(command);
			}
			if (command.equals("<player>")) {
				CommandBase.getPlayer(sender, args[i]);
			}
		}
		if (args.length < required.size()) {
			throw new CommandException("Missing parameter: " + required.get(args.length), new Object[0]);
		}
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return getDescription();
	}

	public abstract String getDescription();

	public String getUsage() {
		return "";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
	}

	public void processSubCommand(ICommandSender sender, String command, String[] args) throws CommandException {
		Method m = subcommands.get(command.toLowerCase());
		if (m == null) {
			throw new CommandException("Unknown subcommand " + command, new Object[0]);
		}
		SubCommand sc = m.getAnnotation(SubCommand.class);
		if (!sender.canCommandSenderUseCommand(sc.permission(),
				"commands.noppes." + getCommandName().toLowerCase() + "." + command.toLowerCase())) {
			throw new CommandException("You are not allowed to use this command", new Object[0]);
		}
		canRun(sender, sc.usage(), args);
		try {
			m.invoke(this, sender, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean runSubCommands() {
		return !subcommands.isEmpty();
	}

	protected void sendMessage(ICommandSender sender, String message, Object... obs) {
		sender.addChatMessage(new ChatComponentTranslation(message, obs));
	}
}

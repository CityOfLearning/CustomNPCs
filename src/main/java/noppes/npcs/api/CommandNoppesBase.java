//

//

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
		subcommands = new HashMap<String, Method>();
		for (final Method m : this.getClass().getDeclaredMethods()) {
			final SubCommand sc = m.getAnnotation(SubCommand.class);
			if (sc != null) {
				String name = sc.name();
				if (name.equals("")) {
					name = m.getName();
				}
				subcommands.put(name.toLowerCase(), m);
			}
		}
	}

	public void canRun(final ICommandSender sender, final String usage, final String[] args) throws CommandException {
		final String[] np = usage.split(" ");
		final List<String> required = new ArrayList<String>();
		for (int i = 0; i < np.length; ++i) {
			final String command = np[i];
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
	public String getCommandUsage(final ICommandSender sender) {
		return getDescription();
	}

	public abstract String getDescription();

	public String getUsage() {
		return "";
	}

	@Override
	public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
	}

	public void processSubCommand(final ICommandSender sender, final String command, final String[] args)
			throws CommandException {
		final Method m = subcommands.get(command.toLowerCase());
		if (m == null) {
			throw new CommandException("Unknown subcommand " + command, new Object[0]);
		}
		final SubCommand sc = m.getAnnotation(SubCommand.class);
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

	protected void sendMessage(final ICommandSender sender, final String message, final Object... obs) {
		sender.addChatMessage(new ChatComponentTranslation(message, obs));
	}
}

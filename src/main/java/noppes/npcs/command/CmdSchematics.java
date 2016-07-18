//

//

package noppes.npcs.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import noppes.npcs.Schematic;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.controllers.SchematicController;

public class CmdSchematics extends CommandNoppesBase {
	@Override
	public List addTabCompletionOptions(final ICommandSender par1, final String[] args, final BlockPos pos) {
		if (args[0].equalsIgnoreCase("build") && (args.length == 2)) {
			final List<String> list = SchematicController.Instance.list();
			return CommandBase.getListOfStringsMatchingLastWord(args, list.toArray(new String[list.size()]));
		}
		return null;
	}

	@SubCommand(desc = "Build the schematic", usage = "<name> [rotation] [[world:]x,y,z]]", permission = 4)
	public void build(final ICommandSender sender, final String[] args) throws CommandException {
		final String name = args[0];
		final Schematic schem = SchematicController.Instance.load(name);
		if (schem == null) {
			throw new CommandException("Unknown schematic: " + name, new Object[0]);
		}
		sendMessage(sender, "width: " + schem.width + ", length: " + schem.length + ", height: " + schem.height,
				new Object[0]);
		BlockPos pos = sender.getPosition();
		World world = sender.getEntityWorld();
		int rotation = 0;
		if (args.length > 1) {
			try {
				rotation = Integer.parseInt(args[1]);
			} catch (NumberFormatException ex) {
			}
		}
		if (args.length > 2) {
			String location = args[2];
			if (location.contains(":")) {
				final String[] par = location.split(":");
				location = par[1];
				world = getWorld(par[0]);
				if (world == null) {
					throw new CommandException("'%s' is an unknown world", new Object[] { par[0] });
				}
			}
			if (location.contains(",")) {
				final String[] par = location.split(",");
				if (par.length != 3) {
					throw new CommandException("Location should be x,y,z", new Object[0]);
				}
				try {
					pos = CommandBase.parseBlockPos(sender, par, 0, false);
				} catch (NumberInvalidException e) {
					throw new CommandException("Location should be in numbers", new Object[0]);
				}
			}
		}
		if ((pos.getX() == 0) && (pos.getY() == 0) && (pos.getZ() == 0)) {
			throw new CommandException("Location needed", new Object[0]);
		}
		schem.init(pos, world, rotation);
		SchematicController.Instance.build(schem, sender);
	}

	@Override
	public String getCommandName() {
		return "schema";
	}

	@Override
	public String getDescription() {
		return "Schematic operation";
	}

	public World getWorld(final String t) {
		final WorldServer[] worldServers;
		worldServers = MinecraftServer.getServer().worldServers;
		for (final WorldServer w : worldServers) {
			if ((w != null) && (w.provider.getDimensionId() + "").equalsIgnoreCase(t)) {
				return w;
			}
		}
		return null;
	}

	@SubCommand(desc = "Gives info about the current build", permission = 4)
	public void info(final ICommandSender sender, final String[] args) {
		SchematicController.Instance.info(sender);
	}

	@SubCommand(desc = "Lists available schematics", permission = 4)
	public void list(final ICommandSender sender, final String[] args) throws CommandException {
		final List<String> list = SchematicController.Instance.list();
		if (list.isEmpty()) {
			throw new CommandException("No available schematics", new Object[0]);
		}
		String s = "";
		for (final String file : list) {
			s = s + file + ", ";
		}
		sendMessage(sender, s, new Object[0]);
	}

	@SubCommand(desc = "Stops the current build", permission = 4)
	public void stop(final ICommandSender sender, final String[] args) {
		SchematicController.Instance.stop(sender);
	}
}

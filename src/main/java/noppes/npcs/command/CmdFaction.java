//

//

package noppes.npcs.command;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.controllers.Faction;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.PlayerData;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.PlayerFactionData;

public class CmdFaction extends CommandNoppesBase {
	public Faction selectedFaction;
	public List<PlayerData> data;

	@SubCommand(desc = "Add points", usage = "<points>")
	public void add(final ICommandSender sender, final String[] args) throws CommandException {
		int points;
		try {
			points = Integer.parseInt(args[0]);
		} catch (NumberFormatException ex) {
			throw new CommandException("Must be an integer", new Object[0]);
		}
		final int factionid = selectedFaction.id;
		for (final PlayerData playerdata : data) {
			final PlayerFactionData playerfactiondata = playerdata.factionData;
			playerfactiondata.increasePoints(factionid, points);
		}
	}

	@Override
	public List addTabCompletionOptions(final ICommandSender par1, final String[] args, final BlockPos pos) {
		if (args.length == 3) {
			return CommandBase.getListOfStringsMatchingLastWord(args,
					new String[] { "add", "subtract", "set", "reset", "drop", "create" });
		}
		return null;
	}

	@SubCommand(desc = "Drop relationship")
	public void drop(final ICommandSender sender, final String[] args) {
		for (final PlayerData playerdata : data) {
			playerdata.factionData.factionData.remove(selectedFaction.id);
		}
	}

	@Override
	public String getCommandName() {
		return "faction";
	}

	@Override
	public String getDescription() {
		return "Faction operations";
	}

	@Override
	public String getUsage() {
		return "<player> <faction> <command>";
	}

	@Override
	public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
		final String playername = args[0];
		final String factionname = args[1];
		data = PlayerDataController.instance.getPlayersData(sender, playername);
		if (data.isEmpty()) {
			throw new CommandException("Unknow player '%s'", new Object[] { playername });
		}
		try {
			selectedFaction = FactionController.getInstance().getFaction(Integer.parseInt(factionname));
		} catch (NumberFormatException e) {
			selectedFaction = FactionController.getInstance().getFactionFromName(factionname);
		}
		if (selectedFaction == null) {
			throw new CommandException("Unknow facion '%s", new Object[] { factionname });
		}
		processSubCommand(sender, args[2], Arrays.copyOfRange(args, 3, args.length));
		for (final PlayerData playerdata : data) {
			playerdata.saveNBTData(null);
		}
	}

	@SubCommand(desc = "Reset points to default")
	public void reset(final ICommandSender sender, final String[] args) {
		for (final PlayerData playerdata : data) {
			playerdata.factionData.factionData.put(selectedFaction.id, selectedFaction.defaultPoints);
		}
	}

	@Override
	public boolean runSubCommands() {
		return false;
	}

	@SubCommand(desc = "Set points", usage = "<points>")
	public void set(final ICommandSender sender, final String[] args) throws CommandException {
		int points;
		try {
			points = Integer.parseInt(args[0]);
		} catch (NumberFormatException ex) {
			throw new CommandException("Must be an integer", new Object[0]);
		}
		for (final PlayerData playerdata : data) {
			final PlayerFactionData playerfactiondata = playerdata.factionData;
			playerfactiondata.factionData.put(selectedFaction.id, points);
		}
	}

	@SubCommand(desc = "Substract points", usage = "<points>")
	public void subtract(final ICommandSender sender, final String[] args) throws CommandException {
		int points;
		try {
			points = Integer.parseInt(args[0]);
		} catch (NumberFormatException ex) {
			throw new CommandException("Must be an integer", new Object[0]);
		}
		final int factionid = selectedFaction.id;
		for (final PlayerData playerdata : data) {
			final PlayerFactionData playerfactiondata = playerdata.factionData;
			playerfactiondata.increasePoints(factionid, -points);
		}
	}
}

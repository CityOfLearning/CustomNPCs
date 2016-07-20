//

//

package noppes.npcs.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.controllers.PlayerData;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.dialog.Dialog;
import noppes.npcs.controllers.dialog.DialogController;
import noppes.npcs.controllers.dialog.DialogOption;
import noppes.npcs.entity.EntityDialogNpc;

public class CmdDialog extends CommandNoppesBase {
	@Override
	public String getCommandName() {
		return "dialog";
	}

	@Override
	public String getDescription() {
		return "Dialog operations";
	}

	@SubCommand(desc = "force read", usage = "<player> <dialog>")
	public void read(final ICommandSender sender, final String[] args) throws CommandException {
		final String playername = args[0];
		int diagid;
		try {
			diagid = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
			throw new CommandException("DialogID must be an integer", new Object[0]);
		}
		final List<PlayerData> data = PlayerDataController.instance.getPlayersData(sender, playername);
		if (data.isEmpty()) {
			throw new CommandException("Unknow player '%s'", new Object[] { playername });
		}
		for (final PlayerData playerdata : data) {
			playerdata.dialogData.dialogsRead.add(diagid);
			playerdata.saveNBTData(null);
		}
	}

	@SubCommand(desc = "reload dialogs from disk", permission = 4)
	public void reload(final ICommandSender sender, final String[] args) {
		new DialogController();
	}

	@SubCommand(desc = "show dialog", usage = "<player> <dialog> <name>")
	public void show(final ICommandSender sender, final String[] args) throws CommandException {
		final EntityPlayer player = CommandBase.getPlayer(sender, args[0]);
		if (player == null) {
			throw new CommandException("Unknow player '%s'", new Object[] { args[0] });
		}
		int diagid;
		try {
			diagid = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
			throw new CommandException("DialogID must be an integer: " + args[1], new Object[0]);
		}
		final Dialog dialog = DialogController.instance.dialogs.get(diagid);
		if (dialog == null) {
			throw new CommandException("Unknown dialog id: " + args[1], new Object[0]);
		}
		final EntityDialogNpc npc = new EntityDialogNpc(sender.getEntityWorld());
		npc.display.setName(args[2]);
		EntityUtil.Copy(player, npc);
		final DialogOption option = new DialogOption();
		option.dialogId = diagid;
		option.title = dialog.title;
		npc.dialogs.put(0, option);
		NoppesUtilServer.openDialog(player, npc, dialog);
	}

	@SubCommand(desc = "force unread dialog", usage = "<player> <dialog>")
	public void unread(final ICommandSender sender, final String[] args) throws CommandException {
		final String playername = args[0];
		int diagid;
		try {
			diagid = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
			throw new CommandException("DialogID must be an integer", new Object[0]);
		}
		final List<PlayerData> data = PlayerDataController.instance.getPlayersData(sender, playername);
		if (data.isEmpty()) {
			throw new CommandException("Unknow player '%s'", new Object[] { playername });
		}
		for (final PlayerData playerdata : data) {
			playerdata.dialogData.dialogsRead.remove(diagid);
			playerdata.saveNBTData(null);
		}
	}
}

//

//

package noppes.npcs.command;

import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.Server;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.PlayerData;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.Quest;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.QuestData;

public class CmdQuest extends CommandNoppesBase {
	@SubCommand(desc = "Finish a quest", usage = "<player> <quest>")
	public void finish(final ICommandSender sender, final String[] args) throws CommandException {
		final String playername = args[0];
		int questid;
		try {
			questid = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
			throw new CommandException("QuestID must be an integer", new Object[0]);
		}
		final List<PlayerData> data = PlayerDataController.instance.getPlayersData(sender, playername);
		if (data.isEmpty()) {
			throw new CommandException(String.format("Unknow player '%s'", playername), new Object[0]);
		}
		final Quest quest = QuestController.instance.quests.get(questid);
		if (quest == null) {
			throw new CommandException("Unknown QuestID", new Object[0]);
		}
		for (final PlayerData playerdata : data) {
			playerdata.questData.finishedQuests.put(questid, System.currentTimeMillis());
			playerdata.saveNBTData(null);
		}
	}

	@Override
	public String getCommandName() {
		return "quest";
	}

	@Override
	public String getDescription() {
		return "Quest operations";
	}

	@SubCommand(desc = "reload quests from disk", permission = 4)
	public void reload(final ICommandSender sender, final String[] args) {
		new DialogController();
	}

	@SubCommand(desc = "Removes a quest from finished and active quests", usage = "<player> <quest>")
	public void remove(final ICommandSender sender, final String[] args) throws CommandException {
		final String playername = args[0];
		int questid;
		try {
			questid = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
			throw new CommandException("QuestID must be an integer", new Object[0]);
		}
		final List<PlayerData> data = PlayerDataController.instance.getPlayersData(sender, playername);
		if (data.isEmpty()) {
			throw new CommandException(String.format("Unknow player '%s'", playername), new Object[0]);
		}
		final Quest quest = QuestController.instance.quests.get(questid);
		if (quest == null) {
			throw new CommandException("Unknown QuestID", new Object[0]);
		}
		for (final PlayerData playerdata : data) {
			playerdata.questData.activeQuests.remove(questid);
			playerdata.questData.finishedQuests.remove(questid);
			playerdata.saveNBTData(null);
		}
	}

	@SubCommand(desc = "Start a quest", usage = "<player> <quest>")
	public void start(final ICommandSender sender, final String[] args) throws CommandException {
		final String playername = args[0];
		int questid;
		try {
			questid = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
			throw new CommandException("QuestID must be an integer", new Object[0]);
		}
		final List<PlayerData> data = PlayerDataController.instance.getPlayersData(sender, playername);
		if (data.isEmpty()) {
			throw new CommandException("Unknow player '%s'", new Object[] { playername });
		}
		final Quest quest = QuestController.instance.quests.get(questid);
		if (quest == null) {
			throw new CommandException("Unknown QuestID", new Object[0]);
		}
		for (final PlayerData playerdata : data) {
			final QuestData questdata = new QuestData(quest);
			playerdata.questData.activeQuests.put(questid, questdata);
			playerdata.saveNBTData(null);
			Server.sendData((EntityPlayerMP) playerdata.player, EnumPacketClient.MESSAGE, "quest.newquest",
					quest.title);
			Server.sendData((EntityPlayerMP) playerdata.player, EnumPacketClient.CHAT, "quest.newquest", ": ",
					quest.title);
		}
	}

	@SubCommand(desc = "Stop a started quest", usage = "<player> <quest>")
	public void stop(final ICommandSender sender, final String[] args) throws CommandException {
		final String playername = args[0];
		int questid;
		try {
			questid = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
			throw new CommandException("QuestID must be an integer", new Object[0]);
		}
		final List<PlayerData> data = PlayerDataController.instance.getPlayersData(sender, playername);
		if (data.isEmpty()) {
			throw new CommandException(String.format("Unknow player '%s'", playername), new Object[0]);
		}
		final Quest quest = QuestController.instance.quests.get(questid);
		if (quest == null) {
			throw new CommandException("Unknown QuestID", new Object[0]);
		}
		for (final PlayerData playerdata : data) {
			playerdata.questData.activeQuests.remove(questid);
			playerdata.saveNBTData(null);
		}
	}
}

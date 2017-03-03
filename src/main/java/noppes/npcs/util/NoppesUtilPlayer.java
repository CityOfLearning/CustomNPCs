
package noppes.npcs.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.oredict.OreDictionary;
import noppes.npcs.CustomNpcs;
import noppes.npcs.CustomTeleporter;
import noppes.npcs.EventHooks;
import noppes.npcs.QuestLogData;
import noppes.npcs.Server;
import noppes.npcs.api.constants.EnumOptionType;
import noppes.npcs.api.event.RoleEvent;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.containers.ContainerNPCBankInterface;
import noppes.npcs.containers.ContainerNPCFollower;
import noppes.npcs.containers.ContainerNPCFollowerHire;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.bank.Bank;
import noppes.npcs.controllers.bank.BankController;
import noppes.npcs.controllers.bank.BankData;
import noppes.npcs.controllers.bank.PlayerBankData;
import noppes.npcs.controllers.dialog.Dialog;
import noppes.npcs.controllers.dialog.DialogController;
import noppes.npcs.controllers.dialog.DialogOption;
import noppes.npcs.controllers.lines.Line;
import noppes.npcs.controllers.quest.PlayerQuestController;
import noppes.npcs.controllers.quest.PlayerQuestData;
import noppes.npcs.controllers.quest.QuestData;
import noppes.npcs.controllers.transport.PlayerTransportData;
import noppes.npcs.controllers.transport.TransportController;
import noppes.npcs.controllers.transport.TransportLocation;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleDialog;
import noppes.npcs.roles.RoleFollower;

public class NoppesUtilPlayer {
	public static void bankUnlock(EntityPlayerMP player, EntityNPCInterface npc) {
		if (npc.advanced.role != 3) {
			return;
		}
		Container con = player.openContainer;
		if ((con == null) || !(con instanceof ContainerNPCBankInterface)) {
			return;
		}
		ContainerNPCBankInterface container = (ContainerNPCBankInterface) con;
		Bank bank = BankController.getInstance().getBank(container.bankid);
		ItemStack item = bank.currencyInventory.getStackInSlot(container.slot);
		if (item == null) {
			return;
		}
		int price = item.stackSize;
		ItemStack currency = container.currencyMatrix.getStackInSlot(0);
		if ((currency == null) || (price > currency.stackSize)) {
			return;
		}
		if ((currency.stackSize - price) == 0) {
			container.currencyMatrix.setInventorySlotContents(0, null);
		} else {
			currency = currency.splitStack(price);
		}
		player.closeContainer();
		PlayerBankData data = PlayerDataController.instance.getBankData(player, bank.id);
		BankData bankData = data.getBank(bank.id);
		if ((bankData.unlockedSlots + 1) <= bank.maxSlots) {
			BankData bankData2 = bankData;
			++bankData2.unlockedSlots;
		}
		RoleEvent.BankUnlockedEvent event = new RoleEvent.BankUnlockedEvent(player, npc.wrappedNPC, container.slot);
		EventHooks.onNPCRole(npc, event);
		bankData.openBankGui(player, npc, bank.id, container.slot);
	}

	public static void bankUpgrade(EntityPlayerMP player, EntityNPCInterface npc) {
		if (npc.advanced.role != 3) {
			return;
		}
		Container con = player.openContainer;
		if ((con == null) || !(con instanceof ContainerNPCBankInterface)) {
			return;
		}
		ContainerNPCBankInterface container = (ContainerNPCBankInterface) con;
		Bank bank = BankController.getInstance().getBank(container.bankid);
		ItemStack item = bank.upgradeInventory.getStackInSlot(container.slot);
		if (item == null) {
			return;
		}
		int price = item.stackSize;
		ItemStack currency = container.currencyMatrix.getStackInSlot(0);
		if ((currency == null) || (price > currency.stackSize)) {
			return;
		}
		if ((currency.stackSize - price) == 0) {
			container.currencyMatrix.setInventorySlotContents(0, null);
		} else {
			currency = currency.splitStack(price);
		}
		player.closeContainer();
		PlayerBankData data = PlayerDataController.instance.getBankData(player, bank.id);
		BankData bankData = data.getBank(bank.id);
		bankData.upgradedSlots.put(container.slot, true);
		RoleEvent.BankUpgradedEvent event = new RoleEvent.BankUpgradedEvent(player, npc.wrappedNPC, container.slot);
		EventHooks.onNPCRole(npc, event);
		bankData.openBankGui(player, npc, bank.id, container.slot);
	}

	public static void changeFollowerState(EntityPlayerMP player, EntityNPCInterface npc) {
		if (npc.advanced.role != 2) {
			return;
		}
		RoleFollower role = (RoleFollower) npc.roleInterface;
		EntityPlayer owner = role.owner;
		if ((owner == null) || !owner.getName().equals(player.getName())) {
			return;
		}
		role.isFollowing = !role.isFollowing;
	}

	private static boolean compareItemDetails(ItemStack item, ItemStack item2, boolean ignoreDamage,
			boolean ignoreNBT) {
		return (item.getItem() == item2.getItem())
				&& (ignoreDamage || (item.getItemDamage() == -1) || (item.getItemDamage() == item2.getItemDamage()))
				&& (ignoreNBT || (item.getTagCompound() == null)
						|| ((item2.getTagCompound() != null) && item.getTagCompound().equals(item2.getTagCompound())))
				&& (ignoreNBT || (item2.getTagCompound() == null) || (item.getTagCompound() != null));
	}

	public static boolean compareItems(EntityPlayer player, ItemStack item, boolean ignoreDamage, boolean ignoreNBT) {
		int size = 0;
		for (ItemStack is : player.inventory.mainInventory) {
			if ((is != null) && compareItems(item, is, ignoreDamage, ignoreNBT)) {
				size += is.stackSize;
			}
		}
		return size >= item.stackSize;
	}

	public static boolean compareItems(ItemStack item, ItemStack item2, boolean ignoreDamage, boolean ignoreNBT) {
		if ((item2 == null) || (item == null)) {
			return false;
		}
		OreDictionary.itemMatches(item, item2, false);
		int[] ids = OreDictionary.getOreIDs(item);
		if (ids.length > 0) {
			for (int id : ids) {
				boolean match1 = false;
				boolean match2 = false;
				for (ItemStack is : OreDictionary.getOres(OreDictionary.getOreName(id))) {
					if (compareItemDetails(item, is, ignoreDamage, ignoreNBT)) {
						match1 = true;
					}
					if (compareItemDetails(item2, is, ignoreDamage, ignoreNBT)) {
						match2 = true;
					}
				}
				if (match1 && match2) {
					return true;
				}
			}
		}
		return compareItemDetails(item, item2, ignoreDamage, ignoreNBT);
	}

	public static void consumeItem(EntityPlayer player, ItemStack item, boolean ignoreDamage, boolean ignoreNBT) {
		if (item == null) {
			return;
		}
		int size = item.stackSize;
		for (int i = 0; i < player.inventory.mainInventory.length; ++i) {
			ItemStack is = player.inventory.mainInventory[i];
			if (is != null) {
				if (compareItems(item, is, ignoreDamage, ignoreNBT)) {
					if (size < is.stackSize) {
						player.inventory.mainInventory[i].splitStack(size);
						break;
					}
					size -= is.stackSize;
					player.inventory.mainInventory[i] = null;
				}
			}
		}
	}

	public static void dialogSelected(int dialogId, int optionId, EntityPlayerMP player, EntityNPCInterface npc) {
		if ((dialogId < 0) && (npc.advanced.role == 7)) {
			String text = ((RoleDialog) npc.roleInterface).optionsTexts.get(optionId);
			if ((text != null) && !text.isEmpty()) {
				Dialog d = new Dialog();
				d.text = text;
				NoppesUtilServer.openDialog(player, npc, d);
			}
			return;
		}
		Dialog dialog = DialogController.instance.dialogs.get(dialogId);
		if (dialog == null) {
			return;
		}
		if (!dialog.hasDialogs(player) && !dialog.hasOtherOptions()) {
			return;
		}
		DialogOption option = dialog.options.get(optionId);
		if (option == null) {
			return;
		}
		if (EventHooks.onNPCDialogOption(npc, player, dialog, option)) {
			Server.sendData(player, EnumPacketClient.GUI_CLOSE, new Object[0]);
			return;
		}
		if (((option.optionType == EnumOptionType.DIALOG_OPTION)
				&& (!option.isAvailable(player) || !option.hasDialog()))
				|| (option.optionType == EnumOptionType.DISABLED)
				|| (option.optionType == EnumOptionType.QUIT_OPTION)) {
			return;
		}
		if (option.optionType == EnumOptionType.ROLE_OPTION) {
			if (npc.roleInterface != null) {
				npc.roleInterface.interact(player);
			} else {
				Server.sendData(player, EnumPacketClient.GUI_CLOSE, new Object[0]);
			}
		} else if (option.optionType == EnumOptionType.DIALOG_OPTION) {
			NoppesUtilServer.openDialog(player, npc, option.getDialog());
		} else if (option.optionType == EnumOptionType.COMMAND_BLOCK) {
			Server.sendData(player, EnumPacketClient.GUI_CLOSE, new Object[0]);
			NoppesUtilServer.runCommand(npc, npc.getName(), option.command, player);
		} else {
			Server.sendData(player, EnumPacketClient.GUI_CLOSE, new Object[0]);
		}
	}

	public static void extendFollower(EntityPlayerMP player, EntityNPCInterface npc) {
		if (npc.advanced.role != 2) {
			return;
		}
		Container con = player.openContainer;
		if ((con == null) || !(con instanceof ContainerNPCFollower)) {
			return;
		}
		ContainerNPCFollower container = (ContainerNPCFollower) con;
		RoleFollower role = (RoleFollower) npc.roleInterface;
		followerBuy(role, container.currencyMatrix, player, npc);
	}

	private static void followerBuy(RoleFollower role, IInventory currencyInv, EntityPlayerMP player,
			EntityNPCInterface npc) {
		ItemStack currency = currencyInv.getStackInSlot(0);
		if (currency == null) {
			return;
		}
		HashMap<ItemStack, Integer> cd = new HashMap<>();
		for (int i : role.inventory.items.keySet()) {
			ItemStack is = role.inventory.items.get(i);
			if ((is != null) && (is.getItem() == currency.getItem())) {
				if (is.getHasSubtypes() && (is.getItemDamage() != currency.getItemDamage())) {
					continue;
				}
				int days = 1;
				if (role.rates.containsKey(i)) {
					days = role.rates.get(i);
				}
				cd.put(is, days);
			}
		}
		if (cd.size() == 0) {
			return;
		}
		int stackSize = currency.stackSize;
		int days2 = 0;
		int possibleDays = 0;
		int possibleSize = stackSize;
		while (true) {
			for (ItemStack item : cd.keySet()) {
				int rDays = cd.get(item);
				int rValue = item.stackSize;
				if (rValue > stackSize) {
					continue;
				}
				int newStackSize = stackSize % rValue;
				int size = stackSize - newStackSize;
				int posDays = (size / rValue) * rDays;
				if (possibleDays > posDays) {
					continue;
				}
				possibleDays = posDays;
				possibleSize = newStackSize;
			}
			if (stackSize == possibleSize) {
				break;
			}
			stackSize = possibleSize;
			days2 += possibleDays;
			possibleDays = 0;
		}
		RoleEvent.FollowerHireEvent event = new RoleEvent.FollowerHireEvent(player, npc.wrappedNPC, days2);
		if (EventHooks.onNPCRole(npc, event)) {
			return;
		}
		if (event.days == 0) {
			return;
		}
		if (stackSize <= 0) {
			currencyInv.setInventorySlotContents(0, (ItemStack) null);
		} else {
			currency = currency.splitStack(stackSize);
		}
		npc.say(player,
				new Line(NoppesStringUtils.formatText(role.dialogHire.replace("{days}", days2 + ""), player, npc)));
		role.setOwner(player);
		role.addDays(days2);
	}

	public static void hireFollower(EntityPlayerMP player, EntityNPCInterface npc) {
		if (npc.advanced.role != 2) {
			return;
		}
		Container con = player.openContainer;
		if ((con == null) || !(con instanceof ContainerNPCFollowerHire)) {
			return;
		}
		ContainerNPCFollowerHire container = (ContainerNPCFollowerHire) con;
		RoleFollower role = (RoleFollower) npc.roleInterface;
		followerBuy(role, container.currencyMatrix, player, npc);
	}

	public static void questCompletion(EntityPlayerMP player, int questId) {
		PlayerQuestData playerdata = PlayerDataController.instance.getPlayerData(player).questData;
		QuestData data = playerdata.activeQuests.get(questId);
		if (data == null) {
			return;
		}
		if (!data.quest.questInterface.isCompleted(player)) {
			return;
		}
		EventHooks.onQuestTurnedIn(player, data.quest);
		data.quest.questInterface.handleComplete(player);
		if (data.quest.rewardExp > 0) {
			player.worldObj.playSoundAtEntity(player, "random.orb", 0.1f,
					0.5f * (((player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.7f) + 1.8f));
			player.addExperience(data.quest.rewardExp);
		}
		data.quest.factionOptions.addPoints(player);
		if (data.quest.mail.isValid()) {
			PlayerDataController.instance.addPlayerMessage(player.getName(), data.quest.mail);
		}
		if (!data.quest.randomReward) {
			for (ItemStack item : data.quest.rewardItems.items.values()) {
				NoppesUtilServer.GivePlayerItem(player, player, item);
			}
		} else {
			List<ItemStack> list = new ArrayList<>();
			for (ItemStack item2 : data.quest.rewardItems.items.values()) {
				if ((item2 != null) && (item2.getItem() != null)) {
					list.add(item2);
				}
			}
			if (!list.isEmpty()) {
				NoppesUtilServer.GivePlayerItem(player, player, list.get(player.getRNG().nextInt(list.size())));
			}
		}
		if (!data.quest.command.isEmpty()) {
			NoppesUtilServer.runCommand(player, "QuestCompletion", data.quest.command);
		}
		PlayerQuestController.setQuestFinished(data.quest, player);
		if (data.quest.hasNewQuest()) {
			PlayerQuestController.addActiveQuest(data.quest.getNextQuest(), player);
		}
	}

	public static void sendData(EnumPlayerPacket enu, Object... obs) {
		PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
		try {
			if (!Server.fillBuffer((ByteBuf) buffer, enu, obs)) {
				return;
			}
			CustomNpcs.ChannelPlayer.sendToServer(new FMLProxyPacket(buffer, "CustomNPCsPlayer"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendQuestLogData(EntityPlayerMP player) {
		if (!PlayerQuestController.hasActiveQuests(player)) {
			return;
		}
		QuestLogData data = new QuestLogData();
		data.setData(player);
		Server.sendData(player, EnumPacketClient.GUI_DATA, data.writeNBT());
	}

	public static void teleportPlayer(EntityPlayerMP player, BlockPos pos, int dimension) {
		if (player.dimension != dimension) {
			MinecraftServer server = MinecraftServer.getServer();
			WorldServer wor = server.worldServerForDimension(dimension);
			if (wor == null) {
				player.addChatMessage(new ChatComponentText("Broken transporter. Dimenion does not exist"));
				return;
			}
			player.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), player.rotationYaw, player.rotationPitch);
			server.getConfigurationManager().transferPlayerToDimension(player, dimension, new CustomTeleporter(wor));
			player.playerNetServerHandler.setPlayerLocation(pos.getX(), pos.getY(), pos.getZ(), player.rotationYaw,
					player.rotationPitch);
			if (!wor.playerEntities.contains(player)) {
				wor.spawnEntityInWorld(player);
			}
		} else {
			player.playerNetServerHandler.setPlayerLocation(pos.getX(), pos.getY(), pos.getZ(), player.rotationYaw,
					player.rotationPitch);
		}
		player.worldObj.updateEntityWithOptionalForce(player, false);
	}

	public static void transport(EntityPlayerMP player, EntityNPCInterface npc, String location) {
		TransportLocation loc = TransportController.getInstance().getTransport(location);
		PlayerTransportData playerdata = PlayerDataController.instance.getPlayerData(player).transportData;
		if ((loc == null) || (!loc.isDefault() && !playerdata.transports.contains(loc.id))) {
			return;
		}
		RoleEvent.TransporterUseEvent event = new RoleEvent.TransporterUseEvent(player, npc.wrappedNPC);
		if (EventHooks.onNPCRole(npc, event)) {
			return;
		}
		teleportPlayer(player, loc.pos, loc.dimension);
	}
}

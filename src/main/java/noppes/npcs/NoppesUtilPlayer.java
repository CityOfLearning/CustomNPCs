//

//

package noppes.npcs;

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
import noppes.npcs.api.constants.EnumOptionType;
import noppes.npcs.api.event.RoleEvent;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.containers.ContainerNPCBankInterface;
import noppes.npcs.containers.ContainerNPCFollower;
import noppes.npcs.containers.ContainerNPCFollowerHire;
import noppes.npcs.controllers.Bank;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.BankData;
import noppes.npcs.controllers.Dialog;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.DialogOption;
import noppes.npcs.controllers.Line;
import noppes.npcs.controllers.PlayerBankData;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.PlayerQuestController;
import noppes.npcs.controllers.PlayerQuestData;
import noppes.npcs.controllers.PlayerTransportData;
import noppes.npcs.controllers.QuestData;
import noppes.npcs.controllers.TransportController;
import noppes.npcs.controllers.TransportLocation;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleDialog;
import noppes.npcs.roles.RoleFollower;

public class NoppesUtilPlayer {
	public static void bankUnlock(final EntityPlayerMP player, final EntityNPCInterface npc) {
		if (npc.advanced.role != 3) {
			return;
		}
		final Container con = player.openContainer;
		if ((con == null) || !(con instanceof ContainerNPCBankInterface)) {
			return;
		}
		final ContainerNPCBankInterface container = (ContainerNPCBankInterface) con;
		final Bank bank = BankController.getInstance().getBank(container.bankid);
		final ItemStack item = bank.currencyInventory.getStackInSlot(container.slot);
		if (item == null) {
			return;
		}
		final int price = item.stackSize;
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
		final PlayerBankData data = PlayerDataController.instance.getBankData(player, bank.id);
		final BankData bankData = data.getBank(bank.id);
		if ((bankData.unlockedSlots + 1) <= bank.maxSlots) {
			final BankData bankData2 = bankData;
			++bankData2.unlockedSlots;
		}
		final RoleEvent.BankUnlockedEvent event = new RoleEvent.BankUnlockedEvent(player, npc.wrappedNPC,
				container.slot);
		EventHooks.onNPCRole(npc, event);
		bankData.openBankGui(player, npc, bank.id, container.slot);
	}

	public static void bankUpgrade(final EntityPlayerMP player, final EntityNPCInterface npc) {
		if (npc.advanced.role != 3) {
			return;
		}
		final Container con = player.openContainer;
		if ((con == null) || !(con instanceof ContainerNPCBankInterface)) {
			return;
		}
		final ContainerNPCBankInterface container = (ContainerNPCBankInterface) con;
		final Bank bank = BankController.getInstance().getBank(container.bankid);
		final ItemStack item = bank.upgradeInventory.getStackInSlot(container.slot);
		if (item == null) {
			return;
		}
		final int price = item.stackSize;
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
		final PlayerBankData data = PlayerDataController.instance.getBankData(player, bank.id);
		final BankData bankData = data.getBank(bank.id);
		bankData.upgradedSlots.put(container.slot, true);
		final RoleEvent.BankUpgradedEvent event = new RoleEvent.BankUpgradedEvent(player, npc.wrappedNPC,
				container.slot);
		EventHooks.onNPCRole(npc, event);
		bankData.openBankGui(player, npc, bank.id, container.slot);
	}

	public static void changeFollowerState(final EntityPlayerMP player, final EntityNPCInterface npc) {
		if (npc.advanced.role != 2) {
			return;
		}
		final RoleFollower role = (RoleFollower) npc.roleInterface;
		final EntityPlayer owner = role.owner;
		if ((owner == null) || !owner.getName().equals(player.getName())) {
			return;
		}
		role.isFollowing = !role.isFollowing;
	}

	private static boolean compareItemDetails(final ItemStack item, final ItemStack item2, final boolean ignoreDamage,
			final boolean ignoreNBT) {
		return (item.getItem() == item2.getItem())
				&& (ignoreDamage || (item.getItemDamage() == -1) || (item.getItemDamage() == item2.getItemDamage()))
				&& (ignoreNBT || (item.getTagCompound() == null)
						|| ((item2.getTagCompound() != null) && item.getTagCompound().equals(item2.getTagCompound())))
				&& (ignoreNBT || (item2.getTagCompound() == null) || (item.getTagCompound() != null));
	}

	public static boolean compareItems(final EntityPlayer player, final ItemStack item, final boolean ignoreDamage,
			final boolean ignoreNBT) {
		int size = 0;
		for (final ItemStack is : player.inventory.mainInventory) {
			if ((is != null) && compareItems(item, is, ignoreDamage, ignoreNBT)) {
				size += is.stackSize;
			}
		}
		return size >= item.stackSize;
	}

	public static boolean compareItems(final ItemStack item, final ItemStack item2, final boolean ignoreDamage,
			final boolean ignoreNBT) {
		if ((item2 == null) || (item == null)) {
			return false;
		}
		OreDictionary.itemMatches(item, item2, false);
		final int[] ids = OreDictionary.getOreIDs(item);
		if (ids.length > 0) {
			for (final int id : ids) {
				boolean match1 = false;
				boolean match2 = false;
				for (final ItemStack is : OreDictionary.getOres(OreDictionary.getOreName(id))) {
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

	public static void consumeItem(final EntityPlayer player, final ItemStack item, final boolean ignoreDamage,
			final boolean ignoreNBT) {
		if (item == null) {
			return;
		}
		int size = item.stackSize;
		for (int i = 0; i < player.inventory.mainInventory.length; ++i) {
			final ItemStack is = player.inventory.mainInventory[i];
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

	public static void dialogSelected(final int dialogId, final int optionId, final EntityPlayerMP player,
			final EntityNPCInterface npc) {
		if ((dialogId < 0) && (npc.advanced.role == 7)) {
			final String text = ((RoleDialog) npc.roleInterface).optionsTexts.get(optionId);
			if ((text != null) && !text.isEmpty()) {
				final Dialog d = new Dialog();
				d.text = text;
				NoppesUtilServer.openDialog(player, npc, d);
			}
			return;
		}
		final Dialog dialog = DialogController.instance.dialogs.get(dialogId);
		if (dialog == null) {
			return;
		}
		if (!dialog.hasDialogs(player) && !dialog.hasOtherOptions()) {
			return;
		}
		final DialogOption option = dialog.options.get(optionId);
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

	public static void extendFollower(final EntityPlayerMP player, final EntityNPCInterface npc) {
		if (npc.advanced.role != 2) {
			return;
		}
		final Container con = player.openContainer;
		if ((con == null) || !(con instanceof ContainerNPCFollower)) {
			return;
		}
		final ContainerNPCFollower container = (ContainerNPCFollower) con;
		final RoleFollower role = (RoleFollower) npc.roleInterface;
		followerBuy(role, container.currencyMatrix, player, npc);
	}

	private static void followerBuy(final RoleFollower role, final IInventory currencyInv, final EntityPlayerMP player,
			final EntityNPCInterface npc) {
		ItemStack currency = currencyInv.getStackInSlot(0);
		if (currency == null) {
			return;
		}
		final HashMap<ItemStack, Integer> cd = new HashMap<ItemStack, Integer>();
		for (final int i : role.inventory.items.keySet()) {
			final ItemStack is = role.inventory.items.get(i);
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
			for (final ItemStack item : cd.keySet()) {
				final int rDays = cd.get(item);
				final int rValue = item.stackSize;
				if (rValue > stackSize) {
					continue;
				}
				final int newStackSize = stackSize % rValue;
				final int size = stackSize - newStackSize;
				final int posDays = (size / rValue) * rDays;
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
		final RoleEvent.FollowerHireEvent event = new RoleEvent.FollowerHireEvent(player, npc.wrappedNPC, days2);
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

	public static void hireFollower(final EntityPlayerMP player, final EntityNPCInterface npc) {
		if (npc.advanced.role != 2) {
			return;
		}
		final Container con = player.openContainer;
		if ((con == null) || !(con instanceof ContainerNPCFollowerHire)) {
			return;
		}
		final ContainerNPCFollowerHire container = (ContainerNPCFollowerHire) con;
		final RoleFollower role = (RoleFollower) npc.roleInterface;
		followerBuy(role, container.currencyMatrix, player, npc);
	}

	public static void questCompletion(final EntityPlayerMP player, final int questId) {
		final PlayerQuestData playerdata = PlayerDataController.instance.getPlayerData(player).questData;
		final QuestData data = playerdata.activeQuests.get(questId);
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
			for (final ItemStack item : data.quest.rewardItems.items.values()) {
				NoppesUtilServer.GivePlayerItem(player, player, item);
			}
		} else {
			final List<ItemStack> list = new ArrayList<ItemStack>();
			for (final ItemStack item2 : data.quest.rewardItems.items.values()) {
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

	public static void sendData(final EnumPlayerPacket enu, final Object... obs) {
		final PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
		try {
			if (!Server.fillBuffer((ByteBuf) buffer, enu, obs)) {
				return;
			}
			CustomNpcs.ChannelPlayer.sendToServer(new FMLProxyPacket(buffer, "CustomNPCsPlayer"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendQuestLogData(final EntityPlayerMP player) {
		if (!PlayerQuestController.hasActiveQuests(player)) {
			return;
		}
		final QuestLogData data = new QuestLogData();
		data.setData(player);
		Server.sendData(player, EnumPacketClient.GUI_DATA, data.writeNBT());
	}

	public static void teleportPlayer(final EntityPlayerMP player, final BlockPos pos, final int dimension) {
		if (player.dimension != dimension) {
			final MinecraftServer server = MinecraftServer.getServer();
			final WorldServer wor = server.worldServerForDimension(dimension);
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

	public static void transport(final EntityPlayerMP player, final EntityNPCInterface npc, final String location) {
		final TransportLocation loc = TransportController.getInstance().getTransport(location);
		final PlayerTransportData playerdata = PlayerDataController.instance.getPlayerData(player).transportData;
		if ((loc == null) || (!loc.isDefault() && !playerdata.transports.contains(loc.id))) {
			return;
		}
		final RoleEvent.TransporterUseEvent event = new RoleEvent.TransporterUseEvent(player, npc.wrappedNPC);
		if (EventHooks.onNPCRole(npc, event)) {
			return;
		}
		teleportPlayer(player, loc.pos, loc.dimension);
	}
}

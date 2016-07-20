//

//

package noppes.npcs;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

import io.netty.buffer.ByteBuf;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.constants.EnumPlayerData;
import noppes.npcs.containers.ContainerManageBanks;
import noppes.npcs.containers.ContainerManageRecipes;
import noppes.npcs.controllers.PlayerData;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.controllers.bank.Bank;
import noppes.npcs.controllers.bank.BankController;
import noppes.npcs.controllers.bank.PlayerBankData;
import noppes.npcs.controllers.dialog.Dialog;
import noppes.npcs.controllers.dialog.DialogCategory;
import noppes.npcs.controllers.dialog.DialogController;
import noppes.npcs.controllers.dialog.DialogOption;
import noppes.npcs.controllers.dialog.PlayerDialogData;
import noppes.npcs.controllers.faction.Faction;
import noppes.npcs.controllers.faction.FactionController;
import noppes.npcs.controllers.faction.PlayerFactionData;
import noppes.npcs.controllers.quest.PlayerQuestController;
import noppes.npcs.controllers.quest.PlayerQuestData;
import noppes.npcs.controllers.quest.Quest;
import noppes.npcs.controllers.quest.QuestCategory;
import noppes.npcs.controllers.quest.QuestController;
import noppes.npcs.controllers.recipies.RecipeCarpentry;
import noppes.npcs.controllers.recipies.RecipeController;
import noppes.npcs.controllers.transport.PlayerTransportData;
import noppes.npcs.controllers.transport.TransportCategory;
import noppes.npcs.controllers.transport.TransportController;
import noppes.npcs.controllers.transport.TransportLocation;
import noppes.npcs.entity.EntityDialogNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleTransporter;

public class NoppesUtilServer {
	private static HashMap<String, Quest> editingQuests;

	static {
		NoppesUtilServer.editingQuests = new HashMap<String, Quest>();
	}

	public static void consumeItemStack(int i, EntityPlayer player) {
		ItemStack item = player.inventory.getCurrentItem();
		if (player.capabilities.isCreativeMode || (item == null)) {
			return;
		}
		ItemStack itemStack = item;
		--itemStack.stackSize;
		if (item.stackSize <= 0) {
			player.destroyCurrentEquippedItem();
		}
	}

	public static void createMobSpawner(BlockPos pos, NBTTagCompound comp, EntityPlayer player) {
		MobSpawnerBaseLogic logic;
		comp.removeTag("Pos");
		ServerCloneController.Instance.cleanTags(comp);
		if (comp.getString("id").equalsIgnoreCase("entityhorse")) {
			player.addChatMessage(new ChatComponentTranslation(
					"Currently you cant create horse spawner, its a minecraft bug", new Object[0]));
			return;
		}
		player.worldObj.setBlockState(pos, Blocks.mob_spawner.getDefaultState());
		TileEntityMobSpawner tile = (TileEntityMobSpawner) player.worldObj.getTileEntity(pos);
		MobSpawnerBaseLogic mobSpawnerBaseLogic = logic = tile.getSpawnerBaseLogic();
		mobSpawnerBaseLogic.getClass();
		logic.setRandomEntity(mobSpawnerBaseLogic.new WeightedRandomMinecart(comp, comp.getString("id")));
	}

	public static void deleteNpc(EntityNPCInterface npc, EntityPlayer player) {
		Server.sendAssociatedData((Entity) npc, EnumPacketClient.DELETE_NPC, npc.getEntityId());
	}

	public static BlockPos GetClosePos(BlockPos origin, World world) {
		for (int x = -1; x < 2; ++x) {
			for (int z = -1; z < 2; ++z) {
				for (int y = 2; y >= -2; --y) {
					BlockPos pos = origin.add(x, y, z);
					if (World.doesBlockHaveSolidTopSurface(world, pos) && world.isAirBlock(pos.up())
							&& world.isAirBlock(pos.up(2))) {
						return pos.up();
					}
				}
			}
		}
		return world.getTopSolidOrLiquidBlock(origin);
	}

	public static DataOutputStream getDataOutputStream(ByteArrayOutputStream stream) throws IOException {
		return new DataOutputStream(new GZIPOutputStream(stream));
	}

	public static EntityNPCInterface getEditingNpc(EntityPlayer player) {
		PlayerData data = PlayerDataController.instance.getPlayerData(player);
		return data.editingNpc;
	}

	public static Quest getEditingQuest(EntityPlayer player) {
		return NoppesUtilServer.editingQuests.get(player.getName());
	}

	public static EntityPlayer getPlayer(UUID id) {
		List<EntityPlayerMP> list = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		for (EntityPlayer player : list) {
			if (id.equals(player.getUniqueID())) {
				return player;
			}
		}
		return null;
	}

	private static ArrayList<String> getScrollData(EntityPlayer player, EnumGuiType gui, EntityNPCInterface npc) {
		if (gui == EnumGuiType.PlayerTransporter) {
			RoleTransporter role = (RoleTransporter) npc.roleInterface;
			ArrayList<String> list = new ArrayList<String>();
			TransportLocation location = role.getLocation();
			String name = role.getLocation().name;
			for (TransportLocation loc : location.category.getDefaultLocations()) {
				if (!list.contains(loc.name)) {
					list.add(loc.name);
				}
			}
			PlayerTransportData playerdata = PlayerDataController.instance.getPlayerData(player).transportData;
			for (int i : playerdata.transports) {
				TransportLocation loc2 = TransportController.getInstance().getTransport(i);
				if ((loc2 != null) && location.category.locations.containsKey(loc2.id) && !list.contains(loc2.name)) {
					list.add(loc2.name);
				}
			}
			list.remove(name);
			return list;
		}
		return null;
	}

	public static void GivePlayerItem(Entity entity, EntityPlayer player, ItemStack item) {
		if (entity.worldObj.isRemote || (item == null)) {
			return;
		}
		item = item.copy();
		float f = 0.7f;
		double d = (entity.worldObj.rand.nextFloat() * f) + (1.0f - f);
		double d2 = (entity.worldObj.rand.nextFloat() * f) + (1.0f - f);
		double d3 = (entity.worldObj.rand.nextFloat() * f) + (1.0f - f);
		EntityItem entityitem = new EntityItem(entity.worldObj, entity.posX + d, entity.posY + d2, entity.posZ + d3,
				item);
		entityitem.setPickupDelay(2);
		entity.worldObj.spawnEntityInWorld(entityitem);
		int i = item.stackSize;
		if (player.inventory.addItemStackToInventory(item)) {
			entity.worldObj.playSoundAtEntity(entityitem, "random.pop", 0.2f,
					(((entity.worldObj.rand.nextFloat() - entity.worldObj.rand.nextFloat()) * 0.7f) + 1.0f) * 2.0f);
			player.onItemPickup(entityitem, i);
			if (item.stackSize <= 0) {
				entityitem.setDead();
			}
		}
	}

	public static boolean isOp(EntityPlayer player) {
		return MinecraftServer.getServer().getConfigurationManager().canSendCommands(player.getGameProfile());
	}

	public static void NotifyOPs(String message, Object... obs) {
		MinecraftServer minecraftserver = MinecraftServer.getServer();
		ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(message, obs);
		chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.GRAY);
		chatcomponenttranslation.getChatStyle().setItalic(true);
		for (EntityPlayer entityplayer : minecraftserver.getConfigurationManager().playerEntityList) {
			if (entityplayer.sendCommandFeedback() && isOp(entityplayer)) {
				entityplayer.addChatMessage(chatcomponenttranslation);
			}
		}
		if (minecraftserver.worldServers[0].getGameRules().getBoolean("logAdminCommands")) {
			LogWriter.info(chatcomponenttranslation.getUnformattedText());
		}
	}

	public static void openDialog(EntityPlayer player, EntityNPCInterface npc, Dialog dia) {
		Dialog dialog = dia.copy(player);
		if (EventHooks.onNPCDialog(npc, player, dialog)) {
			return;
		}
		if (npc instanceof EntityDialogNpc) {
			dialog.hideNPC = true;
			Server.sendData((EntityPlayerMP) player, EnumPacketClient.DIALOG_DUMMY, npc.getName(),
					dialog.writeToNBT(new NBTTagCompound()));
		} else {
			Server.sendData((EntityPlayerMP) player, EnumPacketClient.DIALOG, npc.getEntityId(),
					dialog.writeToNBT(new NBTTagCompound()));
		}
		dia.factionOptions.addPoints(player);
		if (dialog.hasQuest()) {
			PlayerQuestController.addActiveQuest(dialog.getQuest(), player);
		}
		if (!dialog.command.isEmpty()) {
			runCommand(npc, npc.getName(), dialog.command, player);
		}
		if (dialog.mail.isValid()) {
			PlayerDataController.instance.addPlayerMessage(player.getName(), dialog.mail);
		}
		PlayerDialogData data = PlayerDataController.instance.getPlayerData(player).dialogData;
		if (!data.dialogsRead.contains(dialog.id) && (dialog.id >= 0)) {
			data.dialogsRead.add(dialog.id);
		}
		setEditingNpc(player, npc);
	}

	public static void removePlayerData(ByteBuf buffer, EntityPlayerMP player) throws IOException {
		int id = buffer.readInt();
		if (EnumPlayerData.values().length <= id) {
			return;
		}
		String name = Server.readString(buffer);
		if ((name == null) || name.isEmpty()) {
			return;
		}
		EnumPlayerData type = EnumPlayerData.values()[id];
		EntityPlayer pl = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(name);
		PlayerData playerdata = null;
		if (pl == null) {
			playerdata = PlayerDataController.instance.getDataFromUsername(name);
		} else {
			playerdata = PlayerDataController.instance.getPlayerData(pl);
		}
		if (type == EnumPlayerData.Players) {
			File file = new File(PlayerDataController.instance.getSaveDir(), playerdata.uuid + ".json");
			if (file.exists()) {
				file.delete();
			}
			if (pl != null) {
				playerdata.setNBT(new NBTTagCompound());
				sendPlayerData(type, player, name);
				playerdata.saveNBTData(null);
				return;
			}
		}
		if (type == EnumPlayerData.Quest) {
			PlayerQuestData data = playerdata.questData;
			int questId = buffer.readInt();
			data.activeQuests.remove(questId);
			data.finishedQuests.remove(questId);
			playerdata.saveNBTData(null);
		}
		if (type == EnumPlayerData.Dialog) {
			PlayerDialogData data2 = playerdata.dialogData;
			data2.dialogsRead.remove(buffer.readInt());
			playerdata.saveNBTData(null);
		}
		if (type == EnumPlayerData.Transport) {
			PlayerTransportData data3 = playerdata.transportData;
			data3.transports.remove(buffer.readInt());
			playerdata.saveNBTData(null);
		}
		if (type == EnumPlayerData.Bank) {
			PlayerBankData data4 = playerdata.bankData;
			data4.banks.remove(buffer.readInt());
			playerdata.saveNBTData(null);
		}
		if (type == EnumPlayerData.Factions) {
			PlayerFactionData data5 = playerdata.factionData;
			data5.factionData.remove(buffer.readInt());
			playerdata.saveNBTData(null);
		}
		sendPlayerData(type, player, name);
	}

	public static void runCommand(EntityPlayer player, String name, String command) {
		runCommand(player, name, command, player);
	}

	public static void runCommand(ICommandSender executer, String name, String command, EntityPlayer player) {
		runCommand(executer.getEntityWorld(), executer.getPosition(), name, command, player);
	}

	public static void runCommand(World world, BlockPos pos, String name, String command, EntityPlayer player) {
		if (player != null) {
			command = command.replace("@dp", player.getName());
		}
		command = command.replace("@npc", name);
		TileEntityCommandBlock tile = new TileEntityCommandBlock();
		tile.setWorldObj(world);
		tile.setPos(pos);
		CommandBlockLogic logic = tile.getCommandBlockLogic();
		logic.setCommand(command);
		logic.setName("@" + name);
		logic.trigger(world);
	}

	public static TileEntity saveTileEntity(EntityPlayerMP player, NBTTagCompound compound) {
		int x = compound.getInteger("x");
		int y = compound.getInteger("y");
		int z = compound.getInteger("z");
		TileEntity tile = player.worldObj.getTileEntity(new BlockPos(x, y, z));
		if (tile != null) {
			tile.readFromNBT(compound);
		}
		return tile;
	}

	public static void sendBank(EntityPlayerMP player, Bank bank) {
		NBTTagCompound compound = new NBTTagCompound();
		bank.writeEntityToNBT(compound);
		Server.sendData(player, EnumPacketClient.GUI_DATA, compound);
		if (player.openContainer instanceof ContainerManageBanks) {
			((ContainerManageBanks) player.openContainer).setBank(bank);
		}
		player.updateCraftingInventory(player.openContainer, player.openContainer.getInventory());
	}

	public static void sendBankDataAll(EntityPlayerMP player) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (Bank bank : BankController.getInstance().banks.values()) {
			map.put(bank.name, bank.id);
		}
		sendScrollData(player, map);
	}

	public static void sendDialogData(EntityPlayerMP player, DialogCategory category) {
		if (category == null) {
			return;
		}
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (Dialog dialog : category.dialogs.values()) {
			map.put(dialog.title, dialog.id);
		}
		sendScrollData(player, map);
	}

	private static void sendExtraData(EntityPlayer player, EntityNPCInterface npc, EnumGuiType gui, int i, int j,
			int k) {
		if ((gui == EnumGuiType.PlayerFollower) || (gui == EnumGuiType.PlayerFollowerHire)
				|| (gui == EnumGuiType.PlayerTrader) || (gui == EnumGuiType.PlayerTransporter)) {
			sendRoleData(player, npc);
		}
	}

	public static void sendFactionDataAll(EntityPlayerMP player) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (Faction faction : FactionController.getInstance().factions.values()) {
			map.put(faction.name, faction.id);
		}
		sendScrollData(player, map);
	}

	public static void sendGuiClose(EntityPlayerMP player, int i, NBTTagCompound comp) {
		Server.sendData(player, EnumPacketClient.GUI_CLOSE, i, comp);
	}

	public static void sendGuiError(EntityPlayer player, int i) {
		Server.sendData((EntityPlayerMP) player, EnumPacketClient.GUI_ERROR, i, new NBTTagCompound());
	}

	public static void sendNearbyNpcs(EntityPlayerMP player) {
		List<EntityNPCInterface> npcs = player.worldObj.getEntitiesWithinAABB((Class) EntityNPCInterface.class,
				player.getEntityBoundingBox().expand(120.0, 120.0, 120.0));
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (EntityNPCInterface npc : npcs) {
			if (npc.isDead) {
				continue;
			}
			float distance = player.getDistanceToEntity(npc);
			DecimalFormat df = new DecimalFormat("#.#");
			String s = df.format(distance);
			if (distance < 10.0f) {
				s = "0" + s;
			}
			map.put(s + " : " + npc.display.getName(), npc.getEntityId());
		}
		sendScrollData(player, map);
	}

	public static void sendNpcDialogs(EntityPlayer player) {
		EntityNPCInterface npc = getEditingNpc(player);
		if (npc == null) {
			return;
		}
		for (int pos : npc.dialogs.keySet()) {
			DialogOption option = npc.dialogs.get(pos);
			if (option != null) {
				if (!option.hasDialog()) {
					continue;
				}
				NBTTagCompound compound = option.writeNBT();
				compound.setInteger("Position", pos);
				Server.sendData((EntityPlayerMP) player, EnumPacketClient.GUI_DATA, compound);
			}
		}
	}

	public static void sendOpenGui(EntityPlayer player, EnumGuiType gui, EntityNPCInterface npc) {
		sendOpenGui(player, gui, npc, 0, 0, 0);
	}

	public static void sendOpenGui(EntityPlayer player, EnumGuiType gui, EntityNPCInterface npc, int i, int j, int k) {
		if (!(player instanceof EntityPlayerMP)) {
			return;
		}
		setEditingNpc(player, npc);
		sendExtraData(player, npc, gui, i, j, k);
		if (CustomNpcs.proxy.getServerGuiElement(gui.ordinal(), player, player.worldObj, i, j, k) != null) {
			player.openGui(CustomNpcs.instance, gui.ordinal(), player.worldObj, i, j, k);
			return;
		}
		Server.sendDataChecked((EntityPlayerMP) player, EnumPacketClient.GUI, gui.ordinal(), i, j, k);
		ArrayList<String> list = getScrollData(player, gui, npc);
		if ((list == null) || list.isEmpty()) {
			return;
		}
		Server.sendData((EntityPlayerMP) player, EnumPacketClient.SCROLL_LIST, list);
	}

	public static void sendPlayerData(EnumPlayerData type, EntityPlayerMP player, String name) throws IOException {
		Map<String, Integer> map = new HashMap<String, Integer>();
		if (type == EnumPlayerData.Players) {
			PlayerDataController.instance.getSaveDir();
			for (String username : PlayerDataController.instance.getUsernameData().keySet()) {
				map.put(username, 0);
			}
		} else {
			PlayerData playerdata = PlayerDataController.instance.getDataFromUsername(name);
			if (type == EnumPlayerData.Dialog) {
				PlayerDialogData data = playerdata.dialogData;
				for (int questId : data.dialogsRead) {
					Dialog dialog = DialogController.instance.dialogs.get(questId);
					if (dialog == null) {
						continue;
					}
					map.put(dialog.category.title + ": " + dialog.title, questId);
				}
			} else if (type == EnumPlayerData.Quest) {
				PlayerQuestData data2 = playerdata.questData;
				for (int questId : data2.activeQuests.keySet()) {
					Quest quest = QuestController.instance.quests.get(questId);
					if (quest == null) {
						continue;
					}
					map.put(quest.category.title + ": " + quest.title + "(Active quest)", questId);
				}
				for (int questId : data2.finishedQuests.keySet()) {
					Quest quest = QuestController.instance.quests.get(questId);
					if (quest == null) {
						continue;
					}
					map.put(quest.category.title + ": " + quest.title + "(Finished quest)", questId);
				}
			} else if (type == EnumPlayerData.Transport) {
				PlayerTransportData data3 = playerdata.transportData;
				for (int questId : data3.transports) {
					TransportLocation location = TransportController.getInstance().getTransport(questId);
					if (location == null) {
						continue;
					}
					map.put(location.category.title + ": " + location.name, questId);
				}
			} else if (type == EnumPlayerData.Bank) {
				PlayerBankData data4 = playerdata.bankData;
				for (int bankId : data4.banks.keySet()) {
					Bank bank = BankController.getInstance().banks.get(bankId);
					if (bank == null) {
						continue;
					}
					map.put(bank.name, bankId);
				}
			} else if (type == EnumPlayerData.Factions) {
				PlayerFactionData data5 = playerdata.factionData;
				for (int factionId : data5.factionData.keySet()) {
					Faction faction = FactionController.getInstance().factions.get(factionId);
					if (faction == null) {
						continue;
					}
					map.put(faction.name + "(" + data5.getFactionPoints(factionId) + ")", factionId);
				}
			}
		}
		sendScrollData(player, map);
	}

	public static void sendQuestCategoryData(EntityPlayerMP player) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (QuestCategory category : QuestController.instance.categories.values()) {
			map.put(category.title, category.id);
		}
		sendScrollData(player, map);
	}

	public static void sendQuestData(EntityPlayerMP player, QuestCategory category) {
		if (category == null) {
			return;
		}
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (Quest quest : category.quests.values()) {
			map.put(quest.title, quest.id);
		}
		sendScrollData(player, map);
	}

	public static void sendRecipeData(EntityPlayerMP player, int size) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		if (size == 3) {
			for (RecipeCarpentry recipe : RecipeController.instance.globalRecipes.values()) {
				map.put(recipe.name, recipe.id);
			}
		} else {
			for (RecipeCarpentry recipe : RecipeController.instance.anvilRecipes.values()) {
				map.put(recipe.name, recipe.id);
			}
		}
		sendScrollData(player, map);
	}

	public static void sendRoleData(EntityPlayer player, EntityNPCInterface npc) {
		if (npc.advanced.role == 0) {
			return;
		}
		NBTTagCompound comp = new NBTTagCompound();
		npc.roleInterface.writeToNBT(comp);
		comp.setInteger("EntityId", npc.getEntityId());
		comp.setInteger("Role", npc.advanced.role);
		Server.sendData((EntityPlayerMP) player, EnumPacketClient.ROLE, comp);
	}

	public static void sendScrollData(EntityPlayerMP player, Map<String, Integer> map) {
		Map<String, Integer> send = new HashMap<String, Integer>();
		for (String key : map.keySet()) {
			send.put(key, map.get(key));
			if (send.size() == 100) {
				Server.sendData(player, EnumPacketClient.SCROLL_DATA_PART, send);
				send = new HashMap<String, Integer>();
			}
		}
		Server.sendData(player, EnumPacketClient.SCROLL_DATA, send);
	}

	public static void sendTransportCategoryData(EntityPlayerMP player) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (TransportCategory category : TransportController.getInstance().categories.values()) {
			map.put(category.title, category.id);
		}
		sendScrollData(player, map);
	}

	public static void sendTransportData(EntityPlayerMP player, int categoryid) {
		TransportCategory category = TransportController.getInstance().categories.get(categoryid);
		if (category == null) {
			return;
		}
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (TransportLocation transport : category.locations.values()) {
			map.put(transport.name, transport.id);
		}
		sendScrollData(player, map);
	}

	public static void setEditingNpc(EntityPlayer player, EntityNPCInterface npc) {
		PlayerData data = PlayerDataController.instance.getPlayerData(player);
		data.editingNpc = npc;
		if (npc != null) {
			Server.sendDataChecked((EntityPlayerMP) player, EnumPacketClient.EDIT_NPC, npc.getEntityId());
		}
	}

	public static void setEditingQuest(EntityPlayer player, Quest quest) {
		NoppesUtilServer.editingQuests.put(player.getName(), quest);
	}

	public static DialogOption setNpcDialog(int slot, int dialogId, EntityPlayer player) throws IOException {
		EntityNPCInterface npc = getEditingNpc(player);
		if (npc == null) {
			return null;
		}
		if (!npc.dialogs.containsKey(slot)) {
			npc.dialogs.put(slot, new DialogOption());
		}
		DialogOption option = npc.dialogs.get(slot);
		option.dialogId = dialogId;
		if (option.hasDialog()) {
			option.title = option.getDialog().title;
		}
		return option;
	}

	public static void setRecipeGui(EntityPlayerMP player, RecipeCarpentry recipe) {
		if (recipe == null) {
			return;
		}
		if (!(player.openContainer instanceof ContainerManageRecipes)) {
			return;
		}
		ContainerManageRecipes container = (ContainerManageRecipes) player.openContainer;
		container.setRecipe(recipe);
		Server.sendData(player, EnumPacketClient.GUI_DATA, recipe.writeNBT());
	}

	public static Entity spawnClone(NBTTagCompound compound, int x, int y, int z, World worldObj) {
		ServerCloneController.Instance.cleanTags(compound);
		compound.setTag("Pos", NBTTags.nbtDoubleList(x + 0.5, y + 1, z + 0.5));
		Entity entity = EntityList.createEntityFromNBT(compound, worldObj);
		if (entity == null) {
			return null;
		}
		if (entity instanceof EntityNPCInterface) {
			EntityNPCInterface npc = (EntityNPCInterface) entity;
			npc.ai.setStartPos(new BlockPos(npc));
		}
		worldObj.spawnEntityInWorld(entity);
		return entity;
	}

	public static void spawnParticle(Entity entity, String particle, int dimension) {
		Server.sendAssociatedData(entity, EnumPacketClient.PARTICLE, entity.posX, entity.posY, entity.posZ,
				entity.height, entity.width, particle);
	}
}

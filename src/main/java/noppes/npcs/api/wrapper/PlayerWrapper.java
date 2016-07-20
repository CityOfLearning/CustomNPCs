//

//

package noppes.npcs.api.wrapper;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSettings;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.Server;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.PlayerData;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.dialog.Dialog;
import noppes.npcs.controllers.dialog.DialogController;
import noppes.npcs.controllers.dialog.DialogOption;
import noppes.npcs.controllers.dialog.PlayerDialogData;
import noppes.npcs.controllers.quest.PlayerQuestData;
import noppes.npcs.controllers.quest.Quest;
import noppes.npcs.controllers.quest.QuestController;
import noppes.npcs.controllers.quest.QuestData;
import noppes.npcs.entity.EntityDialogNpc;
import noppes.npcs.util.ValueUtil;

public class PlayerWrapper<T extends EntityPlayerMP> extends EntityLivingBaseWrapper<T> implements IPlayer {
	public PlayerWrapper(final T player) {
		super(player);
	}

	@Override
	public void addFactionPoints(final int faction, final int points) {
		final PlayerData data = PlayerDataController.instance.getPlayerData(entity);
		data.factionData.increasePoints(faction, points);
	}

	@Override
	public void finishQuest(final int id) {
		final Quest quest = QuestController.instance.quests.get(id);
		if (quest == null) {
			return;
		}
		final PlayerData data = PlayerDataController.instance.getPlayerData(entity);
		data.questData.finishedQuests.put(id, System.currentTimeMillis());
	}

	@Override
	public String getDisplayName() {
		return entity.getDisplayNameString();
	}

	@Override
	public int getExpLevel() {
		return entity.experienceLevel;
	}

	@Override
	public int getFactionPoints(final int faction) {
		final PlayerData data = PlayerDataController.instance.getPlayerData(entity);
		return data.factionData.getFactionPoints(faction);
	}

	@Override
	public int getGamemode() {
		return entity.theItemInWorldManager.getGameType().getID();
	}

	@Override
	public IItemStack[] getInventory() {
		final IItemStack[] items = new IItemStack[36];
		for (int i = 0; i < entity.inventory.mainInventory.length; ++i) {
			final ItemStack item = entity.inventory.mainInventory[i];
			if (item != null) {
				items[i] = new ItemStackWrapper(item);
			}
		}
		return items;
	}

	@Override
	public String getName() {
		return entity.getName();
	}

	@Override
	public int getType() {
		return 1;
	}

	@Override
	public boolean giveItem(final IItemStack item) {
		final Item mcItem = item.getMCItemStack().getItem();
		if (mcItem == null) {
			return false;
		}
		final int damage = item.getItemDamage();
		final int amount = item.getStackSize();
		final ItemStack givenItem = new ItemStack(mcItem, amount, damage);
		givenItem.setStackDisplayName(item.getDisplayName());
		final boolean bo = entity.inventory.addItemStackToInventory(givenItem);
		if (bo) {
			entity.worldObj.playSoundAtEntity(entity, "random.pop", 0.2f,
					(((entity.getRNG().nextFloat() - entity.getRNG().nextFloat()) * 0.7f) + 1.0f) * 2.0f);
			entity.inventoryContainer.detectAndSendChanges();
		}
		return bo;
	}

	@Override
	public boolean giveItem(final String id, final int damage, final int amount) {
		final Item item = Item.itemRegistry.getObject(new ResourceLocation(id));
		if (item == null) {
			return false;
		}
		final ItemStack mcStack = new ItemStack(item);
		final IItemStack itemStack = new ItemStackWrapper(mcStack);
		itemStack.setStackSize(amount);
		itemStack.setItemDamage(damage);
		return this.giveItem(itemStack);
	}

	@Override
	public boolean hasAchievement(final String achievement) {
		final StatBase statbase = StatList.getOneShotStat(achievement);
		return (statbase != null) && (statbase instanceof Achievement)
				&& entity.getStatFile().hasAchievementUnlocked((Achievement) statbase);
	}

	@Override
	public boolean hasActiveQuest(final int id) {
		final PlayerQuestData data = PlayerDataController.instance.getPlayerData(entity).questData;
		return data.activeQuests.containsKey(id);
	}

	@Override
	public boolean hasFinishedQuest(final int id) {
		final PlayerQuestData data = PlayerDataController.instance.getPlayerData(entity).questData;
		return data.finishedQuests.containsKey(id);
	}

	@Override
	public boolean hasPermission(final String permission) {
		return CustomNpcsPermissions.hasPermissionString(entity, permission);
	}

	@Override
	public boolean hasReadDialog(final int id) {
		final PlayerDialogData data = PlayerDataController.instance.getPlayerData(entity).dialogData;
		return data.dialogsRead.contains(id);
	}

	@Override
	public int inventoryItemCount(final IItemStack item) {
		int i = 0;
		for (final ItemStack is : entity.inventory.mainInventory) {
			if ((is != null) && is.isItemEqual(item.getMCItemStack())) {
				i += is.stackSize;
			}
		}
		return i;
	}

	@Override
	public void message(final String message) {
		entity.addChatMessage(
				new ChatComponentTranslation(NoppesStringUtils.formatText(message, entity), new Object[0]));
	}

	@Override
	public void removeAllItems(final IItemStack item) {
		for (int i = 0; i < entity.inventory.mainInventory.length; ++i) {
			final ItemStack is = entity.inventory.mainInventory[i];
			if ((is != null) && is.isItemEqual(item.getMCItemStack())) {
				entity.inventory.mainInventory[i] = null;
			}
		}
	}

	@Override
	public boolean removeItem(final IItemStack item, int amount) {
		final int count = this.inventoryItemCount(item);
		if (amount > count) {
			return false;
		}
		if (count == amount) {
			this.removeAllItems(item);
		} else {
			for (int i = 0; i < entity.inventory.mainInventory.length; ++i) {
				final ItemStack is = entity.inventory.mainInventory[i];
				if ((is != null) && is.isItemEqual(item.getMCItemStack())) {
					if (amount <= is.stackSize) {
						is.splitStack(amount);
						break;
					}
					entity.inventory.mainInventory[i] = null;
					amount -= is.stackSize;
				}
			}
		}
		return true;
	}

	@Override
	public boolean removeItem(final String id, final int damage, final int amount) {
		final Item item = Item.itemRegistry.getObject(new ResourceLocation(id));
		return (item != null) && this.removeItem(new ItemStackWrapper(new ItemStack(item, 1, damage)), amount);
	}

	@Override
	public void removeQuest(final int id) {
		final Quest quest = QuestController.instance.quests.get(id);
		if (quest == null) {
			return;
		}
		final PlayerData data = PlayerDataController.instance.getPlayerData(entity);
		data.questData.activeQuests.remove(id);
		data.questData.finishedQuests.remove(id);
	}

	@Override
	public void resetSpawnpoint() {
		entity.setSpawnPoint((BlockPos) null, false);
	}

	@Override
	public void setExpLevel(final int level) {
		entity.experienceLevel = level;
		entity.addExperienceLevel(0);
	}

	@Override
	public void setGamemode(final int type) {
		entity.setGameType(WorldSettings.getGameTypeById(type));
	}

	@Override
	public void setPosition(final double x, final double y, final double z) {
		NoppesUtilPlayer.teleportPlayer(entity, new BlockPos(x, y, z), entity.dimension);
	}

	@Override
	public void setSpawnpoint(int x, int y, int z) {
		x = ValueUtil.CorrectInt(x, -30000000, 30000000);
		z = ValueUtil.CorrectInt(z, -30000000, 30000000);
		y = ValueUtil.CorrectInt(y, 0, 256);
		entity.setSpawnPoint(new BlockPos(x, y, z), true);
	}

	@Override
	public void showDialog(final int id, final String name) {
		final Dialog dialog = DialogController.instance.dialogs.get(id);
		if (dialog == null) {
			throw new CustomNPCsException("Unknown Dialog id: " + id, new Object[0]);
		}
		if (!dialog.availability.isAvailable(entity)) {
			return;
		}
		final EntityDialogNpc npc = new EntityDialogNpc(getWorld().getMCWorld());
		npc.display.setName(name);
		EntityUtil.Copy(entity, npc);
		final DialogOption option = new DialogOption();
		option.dialogId = id;
		option.title = dialog.title;
		npc.dialogs.put(0, option);
		NoppesUtilServer.openDialog(entity, npc, dialog);
	}

	@Override
	public void startQuest(final int id) {
		final Quest quest = QuestController.instance.quests.get(id);
		if (quest == null) {
			return;
		}
		final PlayerData data = PlayerDataController.instance.getPlayerData(entity);
		final QuestData questdata = new QuestData(quest);
		data.questData.activeQuests.put(id, questdata);
		Server.sendData(entity, EnumPacketClient.MESSAGE, "quest.newquest", quest.title);
		Server.sendData(entity, EnumPacketClient.CHAT, "quest.newquest", ": ", quest.title);
	}

	@Override
	public void stopQuest(final int id) {
		final Quest quest = QuestController.instance.quests.get(id);
		if (quest == null) {
			return;
		}
		final PlayerData data = PlayerDataController.instance.getPlayerData(entity);
		data.questData.activeQuests.remove(id);
	}

	@Override
	public boolean typeOf(final int type) {
		return (type == 1) || super.typeOf(type);
	}
}

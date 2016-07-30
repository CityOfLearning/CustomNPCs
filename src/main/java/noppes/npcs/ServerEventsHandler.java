//

//

package noppes.npcs;

import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noppes.npcs.api.constants.EnumQuestType;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.PlayerData;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.controllers.lines.Line;
import noppes.npcs.controllers.quest.PlayerQuestData;
import noppes.npcs.controllers.quest.QuestData;
import noppes.npcs.controllers.recipies.RecipeCarpentry;
import noppes.npcs.controllers.recipies.RecipeController;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.quests.QuestKill;
import noppes.npcs.roles.RoleFollower;

public class ServerEventsHandler {
	public static EntityVillager Merchant;
	public static Entity mounted;

	private void doFactionPoints(EntityPlayer player, EntityNPCInterface npc) {
		npc.advanced.factions.addPoints(player);
	}

	private void doQuest(EntityPlayer player, EntityLivingBase entity, boolean all) {
		PlayerQuestData playerdata = PlayerDataController.instance.getPlayerData(player).questData;
		boolean change = false;
		String entityName = EntityList.getEntityString(entity);
		for (QuestData data : playerdata.activeQuests.values()) {
			if ((data.quest.type != EnumQuestType.KILL) && (data.quest.type != EnumQuestType.AREA_KILL)) {
				continue;
			}
			if ((data.quest.type == EnumQuestType.AREA_KILL) && all) {
				List<EntityPlayer> list = player.worldObj.getEntitiesWithinAABB((Class) EntityPlayer.class,
						entity.getEntityBoundingBox().expand(10.0, 10.0, 10.0));
				for (EntityPlayer pl : list) {
					if (pl != player) {
						doQuest(pl, entity, false);
					}
				}
			}
			String name = entityName;
			QuestKill quest = (QuestKill) data.quest.questInterface;
			if (quest.targets.containsKey(entity.getName())) {
				name = entity.getName();
			} else if (!quest.targets.containsKey(name)) {
				continue;
			}
			HashMap<String, Integer> killed = quest.getKilled(data);
			if (killed.containsKey(name) && (killed.get(name) >= quest.targets.get(name))) {
				continue;
			}
			int amount = 0;
			if (killed.containsKey(name)) {
				amount = killed.get(name);
			}
			killed.put(name, amount + 1);
			quest.setKilled(data, killed);
			change = true;
		}
		if (!change) {
			return;
		}
		playerdata.checkQuestCompletion(player, EnumQuestType.KILL);
	}

	@SubscribeEvent
	public void invoke(EntityInteractEvent event) {
		ItemStack item = event.entityPlayer.getCurrentEquippedItem();
		if (item == null) {
			return;
		}
		boolean isRemote = event.entityPlayer.worldObj.isRemote;
		boolean npcInteracted = event.target instanceof EntityNPCInterface;
		if (!isRemote && CustomNpcs.OpsOnly && !MinecraftServer.getServer().getConfigurationManager()
				.canSendCommands(event.entityPlayer.getGameProfile())) {
			return;
		}
		if ((item.getItem() == CustomItems.wand) && npcInteracted && !isRemote) {
			if (!CustomNpcsPermissions.hasPermission(event.entityPlayer, CustomNpcsPermissions.NPC_GUI)) {
				return;
			}
			event.setCanceled(true);
			NoppesUtilServer.sendOpenGui(event.entityPlayer, EnumGuiType.MainMenuDisplay,
					(EntityNPCInterface) event.target);
		} else if ((item.getItem() == CustomItems.cloner) && !isRemote && !(event.target instanceof EntityPlayer)) {
			NBTTagCompound compound = new NBTTagCompound();
			if (!event.target.writeToNBTOptional(compound)) {
				return;
			}
			PlayerData data = PlayerDataController.instance.getPlayerData(event.entityPlayer);
			ServerCloneController.Instance.cleanTags(compound);
			if (!Server.sendDataChecked((EntityPlayerMP) event.entityPlayer, EnumPacketClient.CLONE, compound)) {
				event.entityPlayer.addChatMessage(new ChatComponentText("Entity too big to clone"));
			}
			data.cloned = compound;
			event.setCanceled(true);
		} else if ((item.getItem() == CustomItems.scripter) && !isRemote && npcInteracted) {
			if (!CustomNpcsPermissions.hasPermission(event.entityPlayer, CustomNpcsPermissions.NPC_GUI)) {
				return;
			}
			NoppesUtilServer.setEditingNpc(event.entityPlayer, (EntityNPCInterface) event.target);
			event.setCanceled(true);
			Server.sendData((EntityPlayerMP) event.entityPlayer, EnumPacketClient.GUI, EnumGuiType.Script.ordinal(), 0,
					0, 0);
		} else if (item.getItem() == CustomItems.mount) {
			if (!CustomNpcsPermissions.hasPermission(event.entityPlayer, CustomNpcsPermissions.TOOL_MOUNTER)) {
				return;
			}
			event.setCanceled(true);
			ServerEventsHandler.mounted = event.target;
			if (isRemote) {
				CustomNpcs.proxy.openGui(MathHelper.floor_double(ServerEventsHandler.mounted.posX),
						MathHelper.floor_double(ServerEventsHandler.mounted.posY),
						MathHelper.floor_double(ServerEventsHandler.mounted.posZ), EnumGuiType.MobSpawnerMounter,
						event.entityPlayer);
			}
		} else if ((item.getItem() == CustomItems.wand) && (event.target instanceof EntityVillager)) {
			if (!CustomNpcsPermissions.hasPermission(event.entityPlayer, CustomNpcsPermissions.EDIT_VILLAGER)) {
				return;
			}
			event.setCanceled(true);
			ServerEventsHandler.Merchant = (EntityVillager) event.target;
			if (!isRemote) {
				EntityPlayerMP player = (EntityPlayerMP) event.entityPlayer;
				player.openGui(CustomNpcs.instance, EnumGuiType.MerchantAdd.ordinal(), player.worldObj, 0, 0, 0);
				MerchantRecipeList merchantrecipelist = ServerEventsHandler.Merchant.getRecipes(player);
				if (merchantrecipelist != null) {
					Server.sendData(player, EnumPacketClient.VILLAGER_LIST, merchantrecipelist);
				}
			}
		}
	}

	@SubscribeEvent
	public void invoke(LivingDeathEvent event) {
		if (event.entityLiving.worldObj.isRemote) {
			return;
		}
		if (event.source.getEntity() != null) {
			if ((event.source.getEntity() instanceof EntityNPCInterface) && (event.entityLiving != null)) {
				EntityNPCInterface npc = (EntityNPCInterface) event.source.getEntity();
				Line line = npc.advanced.getKillLine();
				if (line != null) {
					npc.saySurrounding(line.formatTarget(event.entityLiving));
				}
				EventHooks.onNPCKills(npc, event.entityLiving);
			}
			EntityPlayer player = null;
			if (event.source.getEntity() instanceof EntityPlayer) {
				player = (EntityPlayer) event.source.getEntity();
			} else if ((event.source.getEntity() instanceof EntityNPCInterface)
					&& (((EntityNPCInterface) event.source.getEntity()).advanced.role == 2)) {
				player = ((RoleFollower) ((EntityNPCInterface) event.source.getEntity()).roleInterface).owner;
			}
			if (player != null) {
				doQuest(player, event.entityLiving, true);
				if (event.entityLiving instanceof EntityNPCInterface) {
					doFactionPoints(player, (EntityNPCInterface) event.entityLiving);
				}
			}
		}
		if (event.entityLiving instanceof EntityPlayer) {
			PlayerData data = PlayerDataController.instance.getPlayerData((EntityPlayer) event.entityLiving);
			data.saveNBTData(null);
		}
	}

	@SubscribeEvent
	public void invoke(LivingHurtEvent event) {
		if (!(event.entityLiving instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer) event.entityLiving;
		if (event.source.isUnblockable() || event.source.isFireDamage()) {
			return;
		}
		if (!player.isBlocking()) {
			return;
		}
		ItemStack item = player.getCurrentEquippedItem();
		float damage = item.getItemDamage() + event.ammount;
		item.damageItem((int) event.ammount, player);
		if (damage > item.getMaxDamage()) {
			event.ammount = damage - item.getMaxDamage();
		} else {
			event.ammount = 0.0f;
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void invoke(PlayerInteractEvent event) {
		if (event.pos == null) {
			return;
		}
		EntityPlayer player = event.entityPlayer;
		BlockPos pos = event.pos;
		IBlockState state = player.worldObj.getBlockState(pos);
		Block block = state.getBlock();
		if ((event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) && (player.getHeldItem() != null)
				&& (player.getHeldItem().getItem() == CustomItems.teleporter)) {
			event.setCanceled(true);
		}
		if ((block == Blocks.crafting_table) && (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
				&& !player.worldObj.isRemote) {
			RecipeController controller = RecipeController.instance;
			NBTTagList list = new NBTTagList();
			int i = 0;
			for (RecipeCarpentry recipe : controller.globalRecipes.values()) {
				list.appendTag(recipe.writeNBT());
				if ((++i % 10) == 0) {
					NBTTagCompound compound = new NBTTagCompound();
					compound.setTag("recipes", list);
					Server.sendData((EntityPlayerMP) player, EnumPacketClient.SYNCRECIPES_ADD, compound);
					list = new NBTTagList();
				}
			}
			if ((i % 10) != 0) {
				NBTTagCompound compound2 = new NBTTagCompound();
				compound2.setTag("recipes", list);
				Server.sendData((EntityPlayerMP) player, EnumPacketClient.SYNCRECIPES_ADD, compound2);
			}
			Server.sendData((EntityPlayerMP) player, EnumPacketClient.SYNCRECIPES_WORKBENCH, new Object[0]);
		}
		if ((block == CustomItems.carpentyBench) && (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
				&& !player.worldObj.isRemote) {
			RecipeController controller = RecipeController.instance;
			NBTTagList list = new NBTTagList();
			int i = 0;
			for (RecipeCarpentry recipe : controller.anvilRecipes.values()) {
				list.appendTag(recipe.writeNBT());
				if ((++i % 10) == 0) {
					NBTTagCompound compound = new NBTTagCompound();
					compound.setTag("recipes", list);
					Server.sendData((EntityPlayerMP) player, EnumPacketClient.SYNCRECIPES_ADD, compound);
					list = new NBTTagList();
				}
			}
			if ((i % 10) != 0) {
				NBTTagCompound compound2 = new NBTTagCompound();
				compound2.setTag("recipes", list);
				Server.sendData((EntityPlayerMP) player, EnumPacketClient.SYNCRECIPES_ADD, compound2);
			}
			Server.sendData((EntityPlayerMP) player, EnumPacketClient.SYNCRECIPES_CARPENTRYBENCH, new Object[0]);
		}
	}

	@SubscribeEvent
	public void pickUp(EntityItemPickupEvent event) {
		if (event.entityPlayer.worldObj.isRemote) {
			return;
		}
		PlayerQuestData playerdata = PlayerDataController.instance.getPlayerData(event.entityPlayer).questData;
		playerdata.checkQuestCompletion(event.entityPlayer, EnumQuestType.ITEM);
	}

	@SubscribeEvent
	public void populateChunk(PopulateChunkEvent.Post event) {
		NPCSpawning.performWorldGenSpawning(event.world, event.chunkX, event.chunkZ, event.rand);
	}

	@SubscribeEvent
	public void world(EntityJoinWorldEvent event) {
		if (event.world.isRemote || !(event.entity instanceof EntityPlayer)) {
			return;
		}
		PlayerData data = PlayerDataController.instance.getPlayerData((EntityPlayer) event.entity);
		data.updateCompanion(event.world);
	}
}

//

//

package noppes.npcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import noppes.npcs.blocks.tiles.TileBuilder;
import noppes.npcs.blocks.tiles.TileCopy;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.blocks.tiles.TileScriptedDoor;
import noppes.npcs.constants.EnumCompanionStage;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.constants.EnumPlayerData;
import noppes.npcs.containers.ContainerMail;
import noppes.npcs.controllers.Bank;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.Dialog;
import noppes.npcs.controllers.DialogCategory;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.DialogOption;
import noppes.npcs.controllers.Faction;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.LinkedNpcController;
import noppes.npcs.controllers.PlayerData;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.PlayerMail;
import noppes.npcs.controllers.Quest;
import noppes.npcs.controllers.QuestCategory;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.RecipeCarpentry;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.controllers.SchematicController;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.controllers.SpawnController;
import noppes.npcs.controllers.SpawnData;
import noppes.npcs.controllers.TransportController;
import noppes.npcs.controllers.TransportLocation;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.data.DataScenes;
import noppes.npcs.roles.JobSpawner;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.roles.RoleTrader;
import noppes.npcs.roles.RoleTransporter;
import noppes.npcs.util.IPermission;

public class PacketHandlerServer {
	private boolean allowItem(final ItemStack stack, final EnumPacketServer type) {
		if ((stack == null) || (stack.getItem() == null)) {
			return false;
		}
		final Item item = stack.getItem();
		IPermission permission = null;
		if (item instanceof IPermission) {
			permission = (IPermission) item;
		} else if ((item instanceof ItemBlock) && (((ItemBlock) item).getBlock() instanceof IPermission)) {
			permission = (IPermission) ((ItemBlock) item).getBlock();
		}
		return (permission != null) && permission.isAllowed(type);
	}

	private void handlePacket(final EnumPacketServer type, final ByteBuf buffer, final EntityPlayerMP player,
			EntityNPCInterface npc) throws Exception {
		if (type == EnumPacketServer.Delete) {
			npc.delete();
			NoppesUtilServer.deleteNpc(npc, player);
		} else if (type == EnumPacketServer.SceneStart) {
			DataScenes.Toggle(player, buffer.readInt() + "btn");
		} else if (type == EnumPacketServer.SceneReset) {
			DataScenes.Reset(player, null);
		} else if (type == EnumPacketServer.LinkedAdd) {
			LinkedNpcController.Instance.addData(Server.readString(buffer));
			final List<String> list = new ArrayList<String>();
			for (final LinkedNpcController.LinkedData data : LinkedNpcController.Instance.list) {
				list.add(data.name);
			}
			Server.sendData(player, EnumPacketClient.SCROLL_LIST, list);
		} else if (type == EnumPacketServer.LinkedRemove) {
			LinkedNpcController.Instance.removeData(Server.readString(buffer));
			final List<String> list = new ArrayList<String>();
			for (final LinkedNpcController.LinkedData data : LinkedNpcController.Instance.list) {
				list.add(data.name);
			}
			Server.sendData(player, EnumPacketClient.SCROLL_LIST, list);
		} else if (type == EnumPacketServer.LinkedGetAll) {
			final List<String> list = new ArrayList<String>();
			for (final LinkedNpcController.LinkedData data : LinkedNpcController.Instance.list) {
				list.add(data.name);
			}
			Server.sendData(player, EnumPacketClient.SCROLL_LIST, list);
			if (npc != null) {
				Server.sendData(player, EnumPacketClient.SCROLL_SELECTED, npc.linkedName);
			}
		} else if (type == EnumPacketServer.LinkedSet) {
			npc.linkedName = Server.readString(buffer);
			LinkedNpcController.Instance.loadNpcData(npc);
		} else if (type == EnumPacketServer.NpcMenuClose) {
			npc.reset();
			if (npc.linkedData != null) {
				LinkedNpcController.Instance.saveNpcData(npc);
			}
			NoppesUtilServer.setEditingNpc(player, null);
		} else if (type == EnumPacketServer.BanksGet) {
			NoppesUtilServer.sendBankDataAll(player);
		} else if (type == EnumPacketServer.BankGet) {
			final Bank bank = BankController.getInstance().getBank(buffer.readInt());
			NoppesUtilServer.sendBank(player, bank);
		} else if (type == EnumPacketServer.BankSave) {
			final Bank bank = new Bank();
			bank.readEntityFromNBT(Server.readNBT(buffer));
			BankController.getInstance().saveBank(bank);
			NoppesUtilServer.sendBankDataAll(player);
			NoppesUtilServer.sendBank(player, bank);
		} else if (type == EnumPacketServer.BankRemove) {
			BankController.getInstance().removeBank(buffer.readInt());
			NoppesUtilServer.sendBankDataAll(player);
			NoppesUtilServer.sendBank(player, new Bank());
		} else if (type == EnumPacketServer.RemoteMainMenu) {
			final Entity entity = player.worldObj.getEntityByID(buffer.readInt());
			if ((entity == null) || !(entity instanceof EntityNPCInterface)) {
				return;
			}
			NoppesUtilServer.sendOpenGui((EntityPlayer) player, EnumGuiType.MainMenuDisplay,
					(EntityNPCInterface) entity);
		} else if (type == EnumPacketServer.RemoteDelete) {
			final Entity entity = player.worldObj.getEntityByID(buffer.readInt());
			if ((entity == null) || !(entity instanceof EntityNPCInterface)) {
				return;
			}
			npc = (EntityNPCInterface) entity;
			npc.delete();
			NoppesUtilServer.deleteNpc(npc, player);
			NoppesUtilServer.sendNearbyNpcs(player);
		} else if (type == EnumPacketServer.RemoteNpcsGet) {
			NoppesUtilServer.sendNearbyNpcs(player);
			Server.sendData(player, EnumPacketClient.SCROLL_SELECTED,
					CustomNpcs.FreezeNPCs ? "Unfreeze Npcs" : "Freeze Npcs");
		} else if (type == EnumPacketServer.RemoteFreeze) {
			CustomNpcs.FreezeNPCs = !CustomNpcs.FreezeNPCs;
			Server.sendData(player, EnumPacketClient.SCROLL_SELECTED,
					CustomNpcs.FreezeNPCs ? "Unfreeze Npcs" : "Freeze Npcs");
		} else if (type == EnumPacketServer.RemoteReset) {
			final Entity entity = player.worldObj.getEntityByID(buffer.readInt());
			if ((entity == null) || !(entity instanceof EntityNPCInterface)) {
				return;
			}
			npc = (EntityNPCInterface) entity;
			npc.reset();
		} else if (type == EnumPacketServer.RemoteTpToNpc) {
			final Entity entity = player.worldObj.getEntityByID(buffer.readInt());
			if ((entity == null) || !(entity instanceof EntityNPCInterface)) {
				return;
			}
			npc = (EntityNPCInterface) entity;
			player.playerNetServerHandler.setPlayerLocation(npc.posX, npc.posY, npc.posZ, 0.0f, 0.0f);
		} else if (type == EnumPacketServer.Gui) {
			final EnumGuiType gui = EnumGuiType.values()[buffer.readInt()];
			final int i = buffer.readInt();
			final int j = buffer.readInt();
			final int k = buffer.readInt();
			NoppesUtilServer.sendOpenGui(player, gui, npc, i, j, k);
		} else if (type == EnumPacketServer.RecipesGet) {
			NoppesUtilServer.sendRecipeData(player, buffer.readInt());
		} else if (type == EnumPacketServer.RecipeGet) {
			final RecipeCarpentry recipe = RecipeController.instance.getRecipe(buffer.readInt());
			NoppesUtilServer.setRecipeGui(player, recipe);
		} else if (type == EnumPacketServer.RecipeRemove) {
			final RecipeCarpentry recipe = RecipeController.instance.delete(buffer.readInt());
			NoppesUtilServer.sendRecipeData(player, recipe.isGlobal ? 3 : 4);
			NoppesUtilServer.setRecipeGui(player, new RecipeCarpentry(""));
		} else if (type == EnumPacketServer.RecipeSave) {
			final RecipeCarpentry recipe = RecipeCarpentry.read(Server.readNBT(buffer));
			RecipeController.instance.saveRecipe(recipe);
			NoppesUtilServer.sendRecipeData(player, recipe.isGlobal ? 3 : 4);
			NoppesUtilServer.setRecipeGui(player, recipe);
		} else if (type == EnumPacketServer.NaturalSpawnGetAll) {
			NoppesUtilServer.sendScrollData(player, SpawnController.instance.getScroll());
		} else if (type == EnumPacketServer.NaturalSpawnGet) {
			final SpawnData spawn = SpawnController.instance.getSpawnData(buffer.readInt());
			if (spawn != null) {
				Server.sendData(player, EnumPacketClient.GUI_DATA, spawn.writeNBT(new NBTTagCompound()));
			}
		} else if (type == EnumPacketServer.NaturalSpawnSave) {
			final SpawnData data2 = new SpawnData();
			data2.readNBT(Server.readNBT(buffer));
			SpawnController.instance.saveSpawnData(data2);
			NoppesUtilServer.sendScrollData(player, SpawnController.instance.getScroll());
		} else if (type == EnumPacketServer.NaturalSpawnRemove) {
			SpawnController.instance.removeSpawnData(buffer.readInt());
			NoppesUtilServer.sendScrollData(player, SpawnController.instance.getScroll());
		} else if (type == EnumPacketServer.DialogCategorySave) {
			final DialogCategory category = new DialogCategory();
			category.readNBT(Server.readNBT(buffer));
			DialogController.instance.saveCategory(category);
			NoppesUtilServer.sendScrollData(player, DialogController.instance.getScroll());
		} else if (type == EnumPacketServer.DialogCategoryRemove) {
			DialogController.instance.removeCategory(buffer.readInt());
			NoppesUtilServer.sendScrollData(player, DialogController.instance.getScroll());
		} else if (type == EnumPacketServer.DialogCategoryGet) {
			final DialogCategory category = DialogController.instance.categories.get(buffer.readInt());
			if (category != null) {
				final NBTTagCompound comp = category.writeNBT(new NBTTagCompound());
				comp.removeTag("Dialogs");
				Server.sendData(player, EnumPacketClient.GUI_DATA, comp);
			}
		} else if (type == EnumPacketServer.DialogSave) {
			final int category2 = buffer.readInt();
			final Dialog dialog = new Dialog();
			dialog.readNBT(Server.readNBT(buffer));
			DialogController.instance.saveDialog(category2, dialog);
			if (dialog.category != null) {
				NoppesUtilServer.sendDialogData(player, dialog.category);
			}
		} else if (type == EnumPacketServer.QuestOpenGui) {
			final Quest quest = new Quest();
			final int gui2 = buffer.readInt();
			quest.readNBT(Server.readNBT(buffer));
			NoppesUtilServer.setEditingQuest(player, quest);
			player.openGui(CustomNpcs.instance, gui2, player.worldObj, 0, 0, 0);
		} else if (type == EnumPacketServer.DialogRemove) {
			final Dialog dialog2 = DialogController.instance.dialogs.get(buffer.readInt());
			if ((dialog2 != null) && (dialog2.category != null)) {
				DialogController.instance.removeDialog(dialog2);
				NoppesUtilServer.sendDialogData(player, dialog2.category);
			}
		} else if (type == EnumPacketServer.DialogNpcGet) {
			NoppesUtilServer.sendNpcDialogs(player);
		} else if (type == EnumPacketServer.DialogNpcSet) {
			final int slot = buffer.readInt();
			final int dialog3 = buffer.readInt();
			final DialogOption option = NoppesUtilServer.setNpcDialog(slot, dialog3, player);
			if ((option != null) && option.hasDialog()) {
				final NBTTagCompound compound = option.writeNBT();
				compound.setInteger("Position", slot);
				Server.sendData(player, EnumPacketClient.GUI_DATA, compound);
			}
		} else if (type == EnumPacketServer.DialogNpcRemove) {
			npc.dialogs.remove(buffer.readInt());
		} else if (type == EnumPacketServer.QuestCategoryGet) {
			final QuestCategory category3 = QuestController.instance.categories.get(buffer.readInt());
			if (category3 != null) {
				final NBTTagCompound comp = category3.writeNBT(new NBTTagCompound());
				comp.removeTag("Dialogs");
				Server.sendData(player, EnumPacketClient.GUI_DATA, comp);
			}
		} else if (type == EnumPacketServer.QuestCategorySave) {
			final QuestCategory category3 = new QuestCategory();
			category3.readNBT(Server.readNBT(buffer));
			QuestController.instance.saveCategory(category3);
			NoppesUtilServer.sendQuestCategoryData(player);
		} else if (type == EnumPacketServer.QuestCategoryRemove) {
			QuestController.instance.removeCategory(buffer.readInt());
			NoppesUtilServer.sendQuestCategoryData(player);
		} else if (type == EnumPacketServer.QuestSave) {
			final int category2 = buffer.readInt();
			final Quest quest2 = new Quest();
			quest2.readNBT(Server.readNBT(buffer));
			QuestController.instance.saveQuest(category2, quest2);
			if (quest2.category != null) {
				NoppesUtilServer.sendQuestData(player, quest2.category);
			}
		} else if (type == EnumPacketServer.QuestDialogGetTitle) {
			final Dialog quest3 = DialogController.instance.dialogs.get(buffer.readInt());
			final Dialog quest4 = DialogController.instance.dialogs.get(buffer.readInt());
			final Dialog quest5 = DialogController.instance.dialogs.get(buffer.readInt());
			final NBTTagCompound compound = new NBTTagCompound();
			if (quest3 != null) {
				compound.setString("1", quest3.title);
			}
			if (quest4 != null) {
				compound.setString("2", quest4.title);
			}
			if (quest5 != null) {
				compound.setString("3", quest5.title);
			}
			Server.sendData(player, EnumPacketClient.GUI_DATA, compound);
		} else if (type == EnumPacketServer.QuestRemove) {
			final Quest quest = QuestController.instance.quests.get(buffer.readInt());
			if (quest != null) {
				QuestController.instance.removeQuest(quest);
				NoppesUtilServer.sendQuestData(player, quest.category);
			}
		} else if (type == EnumPacketServer.TransportCategoriesGet) {
			NoppesUtilServer.sendTransportCategoryData(player);
		} else if (type == EnumPacketServer.TransportCategorySave) {
			TransportController.getInstance().saveCategory(Server.readString(buffer), buffer.readInt());
		} else if (type == EnumPacketServer.TransportCategoryRemove) {
			TransportController.getInstance().removeCategory(buffer.readInt());
			NoppesUtilServer.sendTransportCategoryData(player);
		} else if (type == EnumPacketServer.TransportRemove) {
			final int id = buffer.readInt();
			final TransportLocation loc = TransportController.getInstance().removeLocation(id);
			if (loc != null) {
				NoppesUtilServer.sendTransportData(player, loc.category.id);
			}
		} else if (type == EnumPacketServer.TransportsGet) {
			NoppesUtilServer.sendTransportData(player, buffer.readInt());
		} else if (type == EnumPacketServer.TransportSave) {
			final int cat = buffer.readInt();
			final TransportLocation location = TransportController.getInstance().saveLocation(cat,
					Server.readNBT(buffer), player, npc);
			if (location != null) {
				if (npc.advanced.role != 4) {
					return;
				}
				final RoleTransporter role = (RoleTransporter) npc.roleInterface;
				role.setTransport(location);
			}
		} else if (type == EnumPacketServer.TransportGetLocation) {
			if (npc.advanced.role != 4) {
				return;
			}
			final RoleTransporter role2 = (RoleTransporter) npc.roleInterface;
			if (role2.hasTransport()) {
				Server.sendData(player, EnumPacketClient.GUI_DATA, role2.getLocation().writeNBT());
				Server.sendData(player, EnumPacketClient.SCROLL_SELECTED, role2.getLocation().category.title);
			}
		} else if (type == EnumPacketServer.FactionSet) {
			npc.setFaction(buffer.readInt());
		} else if (type == EnumPacketServer.FactionSave) {
			final Faction faction = new Faction();
			faction.readNBT(Server.readNBT(buffer));
			FactionController.getInstance().saveFaction(faction);
			NoppesUtilServer.sendFactionDataAll(player);
			final NBTTagCompound compound2 = new NBTTagCompound();
			faction.writeNBT(compound2);
			Server.sendData(player, EnumPacketClient.GUI_DATA, compound2);
		} else if (type == EnumPacketServer.FactionRemove) {
			FactionController.getInstance().delete(buffer.readInt());
			NoppesUtilServer.sendFactionDataAll(player);
			final NBTTagCompound compound3 = new NBTTagCompound();
			new Faction().writeNBT(compound3);
			Server.sendData(player, EnumPacketClient.GUI_DATA, compound3);
		} else if (type == EnumPacketServer.PlayerDataGet) {
			final int id = buffer.readInt();
			if (EnumPlayerData.values().length <= id) {
				return;
			}
			String name = null;
			final EnumPlayerData datatype = EnumPlayerData.values()[id];
			if (datatype != EnumPlayerData.Players) {
				name = Server.readString(buffer);
			}
			NoppesUtilServer.sendPlayerData(datatype, player, name);
		} else if (type == EnumPacketServer.PlayerDataRemove) {
			NoppesUtilServer.removePlayerData(buffer, player);
		} else if (type == EnumPacketServer.MainmenuDisplayGet) {
			Server.sendData(player, EnumPacketClient.GUI_DATA, npc.display.writeToNBT(new NBTTagCompound()));
		} else if (type == EnumPacketServer.MainmenuDisplaySave) {
			npc.display.readToNBT(Server.readNBT(buffer));
			npc.updateClient = true;
		} else if (type == EnumPacketServer.MainmenuStatsGet) {
			Server.sendData(player, EnumPacketClient.GUI_DATA, npc.stats.writeToNBT(new NBTTagCompound()));
		} else if (type == EnumPacketServer.MainmenuStatsSave) {
			npc.stats.readToNBT(Server.readNBT(buffer));
			npc.updateClient = true;
		} else if (type == EnumPacketServer.MainmenuInvGet) {
			Server.sendData(player, EnumPacketClient.GUI_DATA, npc.inventory.writeEntityToNBT(new NBTTagCompound()));
		} else if (type == EnumPacketServer.MainmenuInvSave) {
			npc.inventory.readEntityFromNBT(Server.readNBT(buffer));
			npc.updateAI = true;
			npc.updateClient = true;
		} else if (type == EnumPacketServer.MainmenuAIGet) {
			Server.sendData(player, EnumPacketClient.GUI_DATA, npc.ai.writeToNBT(new NBTTagCompound()));
		} else if (type == EnumPacketServer.MainmenuAISave) {
			npc.ai.readToNBT(Server.readNBT(buffer));
			npc.setHealth(npc.getMaxHealth());
			npc.updateAI = true;
			npc.updateClient = true;
		} else if (type == EnumPacketServer.MainmenuAdvancedGet) {
			Server.sendData(player, EnumPacketClient.GUI_DATA, npc.advanced.writeToNBT(new NBTTagCompound()));
		} else if (type == EnumPacketServer.MainmenuAdvancedSave) {
			npc.advanced.readToNBT(Server.readNBT(buffer));
			npc.updateAI = true;
			npc.updateClient = true;
		} else if (type == EnumPacketServer.JobSave) {
			final NBTTagCompound original = npc.jobInterface.writeToNBT(new NBTTagCompound());
			final NBTTagCompound compound2 = Server.readNBT(buffer);
			final Set<String> names = compound2.getKeySet();
			for (final String name2 : names) {
				original.setTag(name2, compound2.getTag(name2));
			}
			npc.jobInterface.readFromNBT(original);
			npc.updateClient = true;
		} else if (type == EnumPacketServer.JobGet) {
			if (npc.jobInterface == null) {
				return;
			}
			final NBTTagCompound compound3 = new NBTTagCompound();
			compound3.setBoolean("JobData", true);
			npc.jobInterface.writeToNBT(compound3);
			if (npc.advanced.job == 6) {
				((JobSpawner) npc.jobInterface).cleanCompound(compound3);
			}
			Server.sendData(player, EnumPacketClient.GUI_DATA, compound3);
			if (npc.advanced.job == 6) {
				Server.sendData(player, EnumPacketClient.GUI_DATA, ((JobSpawner) npc.jobInterface).getTitles());
			}
		} else if (type == EnumPacketServer.JobSpawnerAdd) {
			if (npc.advanced.job != 6) {
				return;
			}
			final JobSpawner job = (JobSpawner) npc.jobInterface;
			if (buffer.readBoolean()) {
				final NBTTagCompound compound2 = ServerCloneController.Instance.getCloneData(null,
						Server.readString(buffer), buffer.readInt());
				job.setJobCompound(buffer.readInt(), compound2);
			} else {
				job.setJobCompound(buffer.readInt(), Server.readNBT(buffer));
			}
			Server.sendData(player, EnumPacketClient.GUI_DATA, job.getTitles());
		} else if (type == EnumPacketServer.RoleCompanionUpdate) {
			if (npc.advanced.role != 6) {
				return;
			}
			((RoleCompanion) npc.roleInterface).matureTo(EnumCompanionStage.values()[buffer.readInt()]);
			npc.updateClient = true;
		} else if (type == EnumPacketServer.JobSpawnerRemove) {
			if (npc.advanced.job != 6) {
				return;
			}
		} else if (type == EnumPacketServer.RoleSave) {
			npc.roleInterface.readFromNBT(Server.readNBT(buffer));
			npc.updateClient = true;
		} else if (type == EnumPacketServer.RoleGet) {
			if (npc.roleInterface == null) {
				return;
			}
			final NBTTagCompound compound3 = new NBTTagCompound();
			compound3.setBoolean("RoleData", true);
			Server.sendData(player, EnumPacketClient.GUI_DATA, npc.roleInterface.writeToNBT(compound3));
		} else if (type == EnumPacketServer.MerchantUpdate) {
			final Entity entity = player.worldObj.getEntityByID(buffer.readInt());
			if ((entity == null) || !(entity instanceof EntityVillager)) {
				return;
			}
			final MerchantRecipeList list2 = MerchantRecipeList.readFromBuf(new PacketBuffer(buffer));
			((EntityVillager) entity).setRecipes(list2);
		} else if (type == EnumPacketServer.ModelDataSave) {
			if (npc instanceof EntityCustomNpc) {
				((EntityCustomNpc) npc).modelData.readFromNBT(Server.readNBT(buffer));
			}
		} else if (type == EnumPacketServer.MailOpenSetup) {
			final PlayerMail mail = new PlayerMail();
			mail.readNBT(Server.readNBT(buffer));
			ContainerMail.staticmail = mail;
			player.openGui(CustomNpcs.instance, EnumGuiType.PlayerMailman.ordinal(), player.worldObj, 1, 0, 0);
		} else if (type == EnumPacketServer.TransformSave) {
			final boolean isValid = npc.transform.isValid();
			npc.transform.readOptions(Server.readNBT(buffer));
			if (isValid != npc.transform.isValid()) {
				npc.updateAI = true;
			}
		} else if (type == EnumPacketServer.TransformGet) {
			Server.sendData(player, EnumPacketClient.GUI_DATA, npc.transform.writeOptions(new NBTTagCompound()));
		} else if (type == EnumPacketServer.TransformLoad) {
			if (npc.transform.isValid()) {
				npc.transform.transform(buffer.readBoolean());
			}
		} else if (type == EnumPacketServer.TraderMarketSave) {
			final String market = Server.readString(buffer);
			final boolean bo = buffer.readBoolean();
			if (npc.roleInterface instanceof RoleTrader) {
				if (bo) {
					RoleTrader.setMarket(npc, market);
				} else {
					RoleTrader.save((RoleTrader) npc.roleInterface, market);
				}
			}
		} else if (type == EnumPacketServer.MovingPathGet) {
			Server.sendData(player, EnumPacketClient.GUI_DATA, npc.ai.writeToNBT(new NBTTagCompound()));
		} else if (type == EnumPacketServer.MovingPathSave) {
			npc.ai.setMovingPath(NBTTags.getIntegerArraySet(Server.readNBT(buffer).getTagList("MovingPathNew", 10)));
		} else if (type == EnumPacketServer.SpawnRider) {
			final Entity entity = EntityList.createEntityFromNBT(Server.readNBT(buffer), player.worldObj);
			player.worldObj.spawnEntityInWorld(entity);
			entity.mountEntity(ServerEventsHandler.mounted);
		} else if (type == EnumPacketServer.PlayerRider) {
			player.mountEntity(ServerEventsHandler.mounted);
		} else if (type == EnumPacketServer.SpawnMob) {
			final boolean server = buffer.readBoolean();
			final int x = buffer.readInt();
			final int y = buffer.readInt();
			final int z = buffer.readInt();
			NBTTagCompound compound4;
			if (server) {
				compound4 = ServerCloneController.Instance.getCloneData(player, Server.readString(buffer),
						buffer.readInt());
			} else {
				compound4 = Server.readNBT(buffer);
			}
			if (compound4 == null) {
				return;
			}
			final Entity entity2 = NoppesUtilServer.spawnClone(compound4, x, y, z, player.worldObj);
			if (entity2 == null) {
				player.addChatMessage(new ChatComponentText("Failed to create an entity out of your clone"));
			}
		} else if (type == EnumPacketServer.MobSpawner) {
			final boolean server = buffer.readBoolean();
			final BlockPos pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
			NBTTagCompound compound5;
			if (server) {
				compound5 = ServerCloneController.Instance.getCloneData(player, Server.readString(buffer),
						buffer.readInt());
			} else {
				compound5 = Server.readNBT(buffer);
			}
			if (compound5 != null) {
				NoppesUtilServer.createMobSpawner(pos, compound5, player);
			}
		} else if (type == EnumPacketServer.ClonePreSave) {
			final boolean bo2 = ServerCloneController.Instance.getCloneData(null, Server.readString(buffer),
					buffer.readInt()) != null;
			final NBTTagCompound compound2 = new NBTTagCompound();
			compound2.setBoolean("NameExists", bo2);
			Server.sendData(player, EnumPacketClient.GUI_DATA, compound2);
		} else if (type == EnumPacketServer.CloneSave) {
			final PlayerData data3 = PlayerDataController.instance.getPlayerData(player);
			if (data3.cloned == null) {
				return;
			}
			ServerCloneController.Instance.addClone(data3.cloned, Server.readString(buffer), buffer.readInt());
		} else if (type == EnumPacketServer.CloneRemove) {
			final int tab = buffer.readInt();
			ServerCloneController.Instance.removeClone(Server.readString(buffer), tab);
			final NBTTagList list3 = new NBTTagList();
			for (final String name3 : ServerCloneController.Instance.getClones(tab)) {
				list3.appendTag(new NBTTagString(name3));
			}
			final NBTTagCompound compound5 = new NBTTagCompound();
			compound5.setTag("List", list3);
			Server.sendData(player, EnumPacketClient.GUI_DATA, compound5);
		} else if (type == EnumPacketServer.CloneList) {
			final NBTTagList list4 = new NBTTagList();
			for (final String name4 : ServerCloneController.Instance.getClones(buffer.readInt())) {
				list4.appendTag(new NBTTagString(name4));
			}
			final NBTTagCompound compound2 = new NBTTagCompound();
			compound2.setTag("List", list4);
			Server.sendData(player, EnumPacketClient.GUI_DATA, compound2);
		} else if (type == EnumPacketServer.ScriptDataSave) {
			npc.script.readFromNBT(Server.readNBT(buffer));
			npc.updateAI = true;
			npc.script.hasInited = false;
		} else if (type == EnumPacketServer.ScriptDataGet) {
			final NBTTagCompound compound3 = npc.script.writeToNBT(new NBTTagCompound());
			compound3.setTag("Languages", ScriptController.Instance.nbtLanguages());
			Server.sendData(player, EnumPacketClient.GUI_DATA, compound3);
		} else if (type == EnumPacketServer.DimensionsGet) {
			final HashMap<String, Integer> map = new HashMap<String, Integer>();
			for (final int id2 : DimensionManager.getStaticDimensionIDs()) {
				final WorldProvider provider = DimensionManager.createProviderFor(id2);
				map.put(provider.getDimensionName(), id2);
			}
			NoppesUtilServer.sendScrollData(player, map);
		} else if (type == EnumPacketServer.DimensionTeleport) {
			final int dimension = buffer.readInt();
			final WorldServer world = MinecraftServer.getServer().worldServerForDimension(dimension);
			BlockPos coords = world.getSpawnCoordinate();
			if (coords == null) {
				coords = world.getSpawnPoint();
				if (!world.isAirBlock(coords)) {
					coords = world.getTopSolidOrLiquidBlock(coords);
				} else {
					while (world.isAirBlock(coords) && (coords.getY() > 0)) {
						coords = coords.down();
					}
					if (coords.getY() == 0) {
						coords = world.getTopSolidOrLiquidBlock(coords);
					}
				}
			}
			NoppesUtilPlayer.teleportPlayer(player, coords, dimension);
		} else if (type == EnumPacketServer.ScriptBlockDataGet) {
			final TileEntity tile = player.worldObj
					.getTileEntity(new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()));
			if (!(tile instanceof TileScripted)) {
				return;
			}
			final NBTTagCompound compound2 = ((TileScripted) tile).getNBT(new NBTTagCompound());
			compound2.setTag("Languages", ScriptController.Instance.nbtLanguages());
			Server.sendData(player, EnumPacketClient.GUI_DATA, compound2);
		} else if (type == EnumPacketServer.DialogCategoriesGet) {
			NoppesUtilServer.sendScrollData(player, DialogController.instance.getScroll());
		} else if (type == EnumPacketServer.DialogsGetFromDialog) {
			final Dialog dialog2 = DialogController.instance.dialogs.get(buffer.readInt());
			if (dialog2 == null) {
				return;
			}
			NoppesUtilServer.sendDialogData(player, dialog2.category);
		} else if (type == EnumPacketServer.DialogsGet) {
			NoppesUtilServer.sendDialogData(player, DialogController.instance.categories.get(buffer.readInt()));
		} else if (type == EnumPacketServer.QuestsGetFromQuest) {
			final Quest quest = QuestController.instance.quests.get(buffer.readInt());
			if (quest == null) {
				return;
			}
			NoppesUtilServer.sendQuestData(player, quest.category);
		} else if (type == EnumPacketServer.QuestCategoriesGet) {
			NoppesUtilServer.sendQuestCategoryData(player);
		} else if (type == EnumPacketServer.QuestsGet) {
			final QuestCategory category3 = QuestController.instance.categories.get(buffer.readInt());
			NoppesUtilServer.sendQuestData(player, category3);
		} else if (type == EnumPacketServer.FactionsGet) {
			NoppesUtilServer.sendFactionDataAll(player);
		} else if (type == EnumPacketServer.DialogGet) {
			final Dialog dialog2 = DialogController.instance.dialogs.get(buffer.readInt());
			if (dialog2 != null) {
				final NBTTagCompound compound2 = dialog2.writeToNBT(new NBTTagCompound());
				final Quest quest6 = QuestController.instance.quests.get(dialog2.quest);
				if (quest6 != null) {
					compound2.setString("DialogQuestName", quest6.title);
				}
				Server.sendData(player, EnumPacketClient.GUI_DATA, compound2);
			}
		} else if (type == EnumPacketServer.QuestGet) {
			final Quest quest = QuestController.instance.quests.get(buffer.readInt());
			if (quest != null) {
				final NBTTagCompound compound2 = new NBTTagCompound();
				if (quest.hasNewQuest()) {
					compound2.setString("NextQuestTitle", quest.getNextQuest().title);
				}
				Server.sendData(player, EnumPacketClient.GUI_DATA, quest.writeToNBT(compound2));
			}
		} else if (type == EnumPacketServer.FactionGet) {
			final NBTTagCompound compound3 = new NBTTagCompound();
			final Faction faction2 = FactionController.getInstance().getFaction(buffer.readInt());
			faction2.writeNBT(compound3);
			Server.sendData(player, EnumPacketClient.GUI_DATA, compound3);
		} else if (type == EnumPacketServer.SaveTileEntity) {
			NoppesUtilServer.saveTileEntity(player, Server.readNBT(buffer));
		} else if (type == EnumPacketServer.GetTileEntity) {
			final BlockPos pos2 = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
			final TileEntity tile2 = player.worldObj.getTileEntity(pos2);
			final NBTTagCompound compound5 = new NBTTagCompound();
			tile2.writeToNBT(compound5);
			Server.sendData(player, EnumPacketClient.GUI_DATA, compound5);
		} else if (type == EnumPacketServer.ScriptBlockDataSave) {
			final TileEntity tile = player.worldObj
					.getTileEntity(new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()));
			if (!(tile instanceof TileScripted)) {
				return;
			}
			final TileScripted script = (TileScripted) tile;
			script.setNBT(Server.readNBT(buffer));
			script.hasInited = false;
		} else if (type == EnumPacketServer.ScriptDoorDataSave) {
			final TileEntity tile = player.worldObj
					.getTileEntity(new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()));
			if (!(tile instanceof TileScriptedDoor)) {
				return;
			}
			final TileScriptedDoor script2 = (TileScriptedDoor) tile;
			script2.setNBT(Server.readNBT(buffer));
			script2.hasInited = false;
		} else if (type == EnumPacketServer.ScriptDoorDataGet) {
			final TileEntity tile = player.worldObj
					.getTileEntity(new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()));
			if (!(tile instanceof TileScriptedDoor)) {
				return;
			}
			final NBTTagCompound compound2 = ((TileScriptedDoor) tile).getNBT(new NBTTagCompound());
			compound2.setTag("Languages", ScriptController.Instance.nbtLanguages());
			Server.sendData(player, EnumPacketClient.GUI_DATA, compound2);
		} else if (type == EnumPacketServer.SchematicsTile) {
			final BlockPos pos2 = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
			final TileBuilder tile3 = (TileBuilder) player.worldObj.getTileEntity(pos2);
			if (tile3 == null) {
				return;
			}
			Server.sendData(player, EnumPacketClient.GUI_DATA, tile3.writePartNBT(new NBTTagCompound()));
			Server.sendData(player, EnumPacketClient.SCROLL_LIST, SchematicController.Instance.list());
			if (tile3.hasSchematic()) {
				Server.sendData(player, EnumPacketClient.GUI_DATA, tile3.getSchematic().getNBTSmall());
			}
		} else if (type == EnumPacketServer.SchematicsSet) {
			final BlockPos pos2 = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
			final TileBuilder tile3 = (TileBuilder) player.worldObj.getTileEntity(pos2);
			final String name4 = Server.readString(buffer);
			tile3.setSchematic(SchematicController.Instance.load(name4));
			if (tile3.hasSchematic()) {
				Server.sendData(player, EnumPacketClient.GUI_DATA, tile3.getSchematic().getNBTSmall());
			}
		} else if (type == EnumPacketServer.SchematicsTileSave) {
			final BlockPos pos2 = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
			final TileBuilder tile3 = (TileBuilder) player.worldObj.getTileEntity(pos2);
			tile3.readPartNBT(Server.readNBT(buffer));
		} else if (type == EnumPacketServer.SchematicStore) {
			final String name5 = Server.readString(buffer);
			final TileCopy tile4 = (TileCopy) NoppesUtilServer.saveTileEntity(player, Server.readNBT(buffer));
			if ((tile4 == null) || name5.isEmpty()) {
				return;
			}
			SchematicController.Instance.save(player, name5, tile4.getPos(), tile4.height, tile4.width, tile4.length);
		}
	}

	@SubscribeEvent
	public void onServerPacket(final FMLNetworkEvent.ServerCustomPacketEvent event) {
		final EntityPlayerMP player = ((NetHandlerPlayServer) event.handler).playerEntity;
		if (CustomNpcs.OpsOnly && !NoppesUtilServer.isOp(player)) {
			warn(player, "tried to use custom npcs without being an op");
			return;
		}
		final ByteBuf buffer = event.packet.payload();
		MinecraftServer.getServer().addScheduledTask(new Runnable() {
			@Override
			public void run() {
				EnumPacketServer type = null;
				try {
					type = EnumPacketServer.values()[buffer.readInt()];
					final ItemStack item = player.inventory.getCurrentItem();
					final EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
					if (!type.needsNpc || (npc != null)) {
						if (type.hasPermission()) {
							if (!CustomNpcsPermissions.hasPermission(player, type.permission)) {
								return;
							}
						}
						if (!type.isExempt() && !PacketHandlerServer.this.allowItem(item, type)) {
							PacketHandlerServer.this.warn(player,
									"tried to use custom npcs without a tool in hand, possibly a hacker");
						} else {
							PacketHandlerServer.this.handlePacket(type, buffer, player, npc);
						}
					}
				} catch (Exception e) {
					LogWriter.error("Error with EnumPacketServer." + type, e);
				}
			}
		});
	}

	private void warn(final EntityPlayer player, final String warning) {
		MinecraftServer.getServer().logWarning(player.getName() + ": " + warning);
	}
}

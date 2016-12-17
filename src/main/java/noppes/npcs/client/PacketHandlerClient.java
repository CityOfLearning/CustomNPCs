package noppes.npcs.client;

import java.util.HashMap;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import noppes.npcs.CustomNpcs;
import noppes.npcs.Server;
import noppes.npcs.ServerEventsHandler;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.client.gui.GuiNpcMobSpawnerAdd;
import noppes.npcs.client.gui.player.GuiBook;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.IGuiClose;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.IGuiError;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.recipies.RecipeCarpentry;
import noppes.npcs.controllers.recipies.RecipeController;
import noppes.npcs.entity.EntityDialogNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.PacketHandlerServer;
import noppes.npcs.util.NoppesStringUtils;

public class PacketHandlerClient extends PacketHandlerServer {
	private void client(ByteBuf buffer, EntityPlayer player, EnumPacketClient type) throws Exception {
		if (type == EnumPacketClient.CHATBUBBLE) {
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(buffer.readInt());
			if ((entity == null) || !(entity instanceof EntityNPCInterface)) {
				return;
			}
			EntityNPCInterface npc = (EntityNPCInterface) entity;
			if (npc.messages == null) {
				npc.messages = new RenderChatMessages();
			}
			String text = NoppesStringUtils.formatText(Server.readString(buffer), player, npc);
			npc.messages.addMessage(text, npc);
			if (buffer.readBoolean()) {
				player.addChatMessage(
						new ChatComponentTranslation("<\u00a7a" + npc.getName() + "\u00a7r> " + text, new Object[0]));
			}
		} else if (type == EnumPacketClient.CHAT) {
			String message = "";
			String str;
			while (((str = Server.readString(buffer)) != null) && !str.isEmpty()) {
				message += StatCollector.translateToLocal(str);
			}
			player.addChatMessage(new ChatComponentTranslation(message, new Object[0]));
		} else if (type == EnumPacketClient.MESSAGE) {
			String description = StatCollector.translateToLocal(Server.readString(buffer));
			String message2 = Server.readString(buffer);
			Achievement ach = new QuestAchievement(message2, description);
			Minecraft.getMinecraft().guiAchievement.displayAchievement(ach);
			ObfuscationReflectionHelper.setPrivateValue(GuiAchievement.class, Minecraft.getMinecraft().guiAchievement,
					ach.getDescription(), 4);
			ObfuscationReflectionHelper.setPrivateValue(GuiAchievement.class, Minecraft.getMinecraft().guiAchievement,
					message2, 5);
		} else if (type == EnumPacketClient.SYNCRECIPES_ADD) {
			NBTTagList list = Server.readNBT(buffer).getTagList("recipes", 10);
			if (list == null) {
				return;
			}
			for (int i = 0; i < list.tagCount(); ++i) {
				RecipeCarpentry recipe = RecipeCarpentry.read(list.getCompoundTagAt(i));
				RecipeController.syncRecipes.put(recipe.id, recipe);
			}
		} else if (type == EnumPacketClient.SYNCRECIPES_WORKBENCH) {
			RecipeController.instance.globalRecipes = RecipeController.syncRecipes;
			RecipeController.instance.reloadGlobalRecipes();
			RecipeController.syncRecipes = new HashMap<Integer, RecipeCarpentry>();
		} else if (type == EnumPacketClient.SYNCRECIPES_CARPENTRYBENCH) {
			RecipeController.instance.anvilRecipes = RecipeController.syncRecipes;
			RecipeController.syncRecipes = new HashMap<Integer, RecipeCarpentry>();
		} else if (type == EnumPacketClient.DIALOG) {
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(buffer.readInt());
			if ((entity == null) || !(entity instanceof EntityNPCInterface)) {
				return;
			}
			NoppesUtil.openDialog(Server.readNBT(buffer), (EntityNPCInterface) entity, player);
		} else if (type == EnumPacketClient.DIALOG_DUMMY) {
			EntityDialogNpc npc2 = new EntityDialogNpc(player.worldObj);
			npc2.display.setName(Server.readString(buffer));
			EntityUtil.Copy(player, npc2);
			NoppesUtil.openDialog(Server.readNBT(buffer), npc2, player);
		} else if (type == EnumPacketClient.QUEST_COMPLETION) {
			NoppesUtil.guiQuestCompletion(player, Server.readNBT(buffer));
		} else if (type == EnumPacketClient.EDIT_NPC) {
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(buffer.readInt());
			if ((entity == null) || !(entity instanceof EntityNPCInterface)) {
				NoppesUtil.setLastNpc(null);
			} else {
				NoppesUtil.setLastNpc((EntityNPCInterface) entity);
			}
		} else if (type == EnumPacketClient.PLAY_MUSIC) {
			MusicController.Instance.playMusic(Server.readString(buffer), player);
		} else if (type == EnumPacketClient.PLAY_SOUND) {
			MusicController.Instance.playSound(Server.readString(buffer), buffer.readFloat(), buffer.readFloat(),
					buffer.readFloat());
		} else if (type == EnumPacketClient.UPDATE_NPC) {
			NBTTagCompound compound = Server.readNBT(buffer);
			Entity entity2 = Minecraft.getMinecraft().theWorld.getEntityByID(compound.getInteger("EntityId"));
			if ((entity2 == null) || !(entity2 instanceof EntityNPCInterface)) {
				return;
			}
			((EntityNPCInterface) entity2).readSpawnData(compound);
		} else if (type == EnumPacketClient.ROLE) {
			NBTTagCompound compound = Server.readNBT(buffer);
			Entity entity2 = Minecraft.getMinecraft().theWorld.getEntityByID(compound.getInteger("EntityId"));
			if ((entity2 == null) || !(entity2 instanceof EntityNPCInterface)) {
				return;
			}
			((EntityNPCInterface) entity2).advanced.setRole(compound.getInteger("Role"));
			((EntityNPCInterface) entity2).roleInterface.readFromNBT(compound);
			NoppesUtil.setLastNpc((EntityNPCInterface) entity2);
		} else if (type == EnumPacketClient.GUI) {
			EnumGuiType gui = EnumGuiType.values()[buffer.readInt()];
			CustomNpcs.proxy.openGui(NoppesUtil.getLastNpc(), gui, buffer.readInt(), buffer.readInt(),
					buffer.readInt());
		} else if (type == EnumPacketClient.PARTICLE) {
			NoppesUtil.spawnParticle(buffer);
		} else if (type == EnumPacketClient.DELETE_NPC) {
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(buffer.readInt());
			if ((entity == null) || !(entity instanceof EntityNPCInterface)) {
				return;
			}
			((EntityNPCInterface) entity).delete();
		} else if (type == EnumPacketClient.SCROLL_LIST) {
			NoppesUtil.setScrollList(buffer);
		} else if (type == EnumPacketClient.SCROLL_DATA) {
			NoppesUtil.setScrollData(buffer);
		} else if (type == EnumPacketClient.SCROLL_DATA_PART) {
			NoppesUtil.addScrollData(buffer);
		} else if (type == EnumPacketClient.SCROLL_SELECTED) {
			GuiScreen gui2 = Minecraft.getMinecraft().currentScreen;
			if ((gui2 == null) || !(gui2 instanceof IScrollData)) {
				return;
			}
			String selected = Server.readString(buffer);
			((IScrollData) gui2).setSelected(selected);
		} else if (type == EnumPacketClient.CLONE) {
			NBTTagCompound compound = Server.readNBT(buffer);
			NoppesUtil.openGUI(player, new GuiNpcMobSpawnerAdd(compound));
		} else if (type == EnumPacketClient.GUI_DATA) {
			GuiScreen gui2 = Minecraft.getMinecraft().currentScreen;
			if (gui2 == null) {
				return;
			}
			if ((gui2 instanceof GuiNPCInterface) && ((GuiNPCInterface) gui2).hasSubGui()) {
				gui2 = ((GuiNPCInterface) gui2).getSubGui();
			} else if ((gui2 instanceof GuiContainerNPCInterface) && ((GuiContainerNPCInterface) gui2).hasSubGui()) {
				gui2 = ((GuiContainerNPCInterface) gui2).getSubGui();
			}
			if (gui2 instanceof IGuiData) {
				((IGuiData) gui2).setGuiData(Server.readNBT(buffer));
			}
		} else if (type == EnumPacketClient.GUI_ERROR) {
			GuiScreen gui2 = Minecraft.getMinecraft().currentScreen;
			if ((gui2 == null) || !(gui2 instanceof IGuiError)) {
				return;
			}
			int i = buffer.readInt();
			NBTTagCompound compound2 = Server.readNBT(buffer);
			((IGuiError) gui2).setError(i, compound2);
		} else if (type == EnumPacketClient.GUI_CLOSE) {
			GuiScreen gui2 = Minecraft.getMinecraft().currentScreen;
			if (gui2 == null) {
				return;
			}
			if (gui2 instanceof IGuiClose) {
				int i = buffer.readInt();
				NBTTagCompound compound2 = Server.readNBT(buffer);
				((IGuiClose) gui2).setClose(i, compound2);
			}
			Minecraft mc = Minecraft.getMinecraft();
			mc.displayGuiScreen((GuiScreen) null);
			mc.setIngameFocus();
		} else if (type == EnumPacketClient.VILLAGER_LIST) {
			MerchantRecipeList merchantrecipelist = MerchantRecipeList.readFromBuf(new PacketBuffer(buffer));
			ServerEventsHandler.Merchant.setRecipes(merchantrecipelist);
		} else if (type == EnumPacketClient.OPEN_BOOK) {
			int x = buffer.readInt();
			int y = buffer.readInt();
			int z = buffer.readInt();

			NoppesUtil.openGUI(player,
					new GuiBook(player, ItemStack.loadItemStackFromNBT(Server.readNBT(buffer)), x, y, z));
		}
	}

	@SubscribeEvent
	public void onPacketData(FMLNetworkEvent.ClientCustomPacketEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		ByteBuf buffer = event.packet.payload();
		Minecraft.getMinecraft().addScheduledTask(() -> {
			EnumPacketClient en = null;
			try {
				en = EnumPacketClient.values()[buffer.readInt()];
				PacketHandlerClient.this.client(buffer, player, en);
			} catch (Exception e) {
				CustomNpcs.logger.error("Error with EnumPacketClient." + en, e);
			}
		});
	}
}

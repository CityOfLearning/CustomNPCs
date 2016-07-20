//

//

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
import noppes.npcs.LogWriter;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.PacketHandlerServer;
import noppes.npcs.Server;
import noppes.npcs.ServerEventsHandler;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.client.gui.GuiNpcMobSpawnerAdd;
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

public class PacketHandlerClient extends PacketHandlerServer {
	private void client(final ByteBuf buffer, final EntityPlayer player, final EnumPacketClient type) throws Exception {
		if (type == EnumPacketClient.CHATBUBBLE) {
			final Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(buffer.readInt());
			if ((entity == null) || !(entity instanceof EntityNPCInterface)) {
				return;
			}
			final EntityNPCInterface npc = (EntityNPCInterface) entity;
			if (npc.messages == null) {
				npc.messages = new RenderChatMessages();
			}
			final String text = NoppesStringUtils.formatText(Server.readString(buffer), player, npc);
			npc.messages.addMessage(text, npc);
			if (buffer.readBoolean()) {
				player.addChatMessage(new ChatComponentTranslation("<\u00a7a"+npc.getName()+"\u00a7r> " + text, new Object[0]));
			}
		} else if (type == EnumPacketClient.CHAT) {
			String message = "";
			String str;
			while (((str = Server.readString(buffer)) != null) && !str.isEmpty()) {
				message += StatCollector.translateToLocal(str);
			}
			LogWriter.info("Chat: " + message);
			player.addChatMessage(new ChatComponentTranslation(message, new Object[0]));
		} else if (type == EnumPacketClient.MESSAGE) {
			final String description = StatCollector.translateToLocal(Server.readString(buffer));
			final String message2 = Server.readString(buffer);
			final Achievement ach = new QuestAchievement(message2, description);
			Minecraft.getMinecraft().guiAchievement.displayAchievement(ach);
			LogWriter.info(ach.getDescription());
			ObfuscationReflectionHelper.setPrivateValue(GuiAchievement.class, Minecraft.getMinecraft().guiAchievement,
					ach.getDescription(), 4);
			ObfuscationReflectionHelper.setPrivateValue(GuiAchievement.class, Minecraft.getMinecraft().guiAchievement,
					message2, 5);
		} else if (type == EnumPacketClient.SYNCRECIPES_ADD) {
			final NBTTagList list = Server.readNBT(buffer).getTagList("recipes", 10);
			if (list == null) {
				return;
			}
			for (int i = 0; i < list.tagCount(); ++i) {
				final RecipeCarpentry recipe = RecipeCarpentry.read(list.getCompoundTagAt(i));
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
			final Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(buffer.readInt());
			if ((entity == null) || !(entity instanceof EntityNPCInterface)) {
				return;
			}
			NoppesUtil.openDialog(Server.readNBT(buffer), (EntityNPCInterface) entity, player);
		} else if (type == EnumPacketClient.DIALOG_DUMMY) {
			final EntityDialogNpc npc2 = new EntityDialogNpc(player.worldObj);
			npc2.display.setName(Server.readString(buffer));
			EntityUtil.Copy(player, npc2);
			NoppesUtil.openDialog(Server.readNBT(buffer), npc2, player);
		} else if (type == EnumPacketClient.QUEST_COMPLETION) {
			NoppesUtil.guiQuestCompletion(player, Server.readNBT(buffer));
		} else if (type == EnumPacketClient.EDIT_NPC) {
			final Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(buffer.readInt());
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
			final NBTTagCompound compound = Server.readNBT(buffer);
			final Entity entity2 = Minecraft.getMinecraft().theWorld.getEntityByID(compound.getInteger("EntityId"));
			if ((entity2 == null) || !(entity2 instanceof EntityNPCInterface)) {
				return;
			}
			((EntityNPCInterface) entity2).readSpawnData(compound);
		} else if (type == EnumPacketClient.ROLE) {
			final NBTTagCompound compound = Server.readNBT(buffer);
			final Entity entity2 = Minecraft.getMinecraft().theWorld.getEntityByID(compound.getInteger("EntityId"));
			if ((entity2 == null) || !(entity2 instanceof EntityNPCInterface)) {
				return;
			}
			((EntityNPCInterface) entity2).advanced.setRole(compound.getInteger("Role"));
			((EntityNPCInterface) entity2).roleInterface.readFromNBT(compound);
			NoppesUtil.setLastNpc((EntityNPCInterface) entity2);
		} else if (type == EnumPacketClient.GUI) {
			final EnumGuiType gui = EnumGuiType.values()[buffer.readInt()];
			CustomNpcs.proxy.openGui(NoppesUtil.getLastNpc(), gui, buffer.readInt(), buffer.readInt(),
					buffer.readInt());
		} else if (type == EnumPacketClient.PARTICLE) {
			NoppesUtil.spawnParticle(buffer);
		} else if (type == EnumPacketClient.DELETE_NPC) {
			final Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(buffer.readInt());
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
			final GuiScreen gui2 = Minecraft.getMinecraft().currentScreen;
			if ((gui2 == null) || !(gui2 instanceof IScrollData)) {
				return;
			}
			final String selected = Server.readString(buffer);
			((IScrollData) gui2).setSelected(selected);
		} else if (type == EnumPacketClient.CLONE) {
			final NBTTagCompound compound = Server.readNBT(buffer);
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
			final GuiScreen gui2 = Minecraft.getMinecraft().currentScreen;
			if ((gui2 == null) || !(gui2 instanceof IGuiError)) {
				return;
			}
			final int i = buffer.readInt();
			final NBTTagCompound compound2 = Server.readNBT(buffer);
			((IGuiError) gui2).setError(i, compound2);
		} else if (type == EnumPacketClient.GUI_CLOSE) {
			final GuiScreen gui2 = Minecraft.getMinecraft().currentScreen;
			if (gui2 == null) {
				return;
			}
			if (gui2 instanceof IGuiClose) {
				final int i = buffer.readInt();
				final NBTTagCompound compound2 = Server.readNBT(buffer);
				((IGuiClose) gui2).setClose(i, compound2);
			}
			final Minecraft mc = Minecraft.getMinecraft();
			mc.displayGuiScreen((GuiScreen) null);
			mc.setIngameFocus();
		} else if (type == EnumPacketClient.VILLAGER_LIST) {
			final MerchantRecipeList merchantrecipelist = MerchantRecipeList.readFromBuf(new PacketBuffer(buffer));
			ServerEventsHandler.Merchant.setRecipes(merchantrecipelist);
		} else if (type == EnumPacketClient.CONFIG) {
			final int config = buffer.readInt();
			if (config == 0) {
				final String font = Server.readString(buffer);
				final int size = buffer.readInt();
				final Runnable run = new Runnable() {
					@Override
					public void run() {
						if (!font.isEmpty()) {
							CustomNpcs.FontType = font;
							CustomNpcs.FontSize = size;
							ClientProxy.Font = new ClientProxy.FontContainer(CustomNpcs.FontType, CustomNpcs.FontSize);
							CustomNpcs.Config.updateConfig();
							player.addChatMessage(new ChatComponentTranslation("Font set to %s",
									new Object[] { ClientProxy.Font.getName() }));
						} else {
							player.addChatMessage(new ChatComponentTranslation("Current font is %s",
									new Object[] { ClientProxy.Font.getName() }));
						}
					}
				};
				Minecraft.getMinecraft().addScheduledTask(run);
			}
		}
	}

	@SubscribeEvent
	public void onPacketData(final FMLNetworkEvent.ClientCustomPacketEvent event) {
		final EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		final ByteBuf buffer = event.packet.payload();
		Minecraft.getMinecraft().addScheduledTask(new Runnable() {
			@Override
			public void run() {
				EnumPacketClient en = null;
				try {
					en = EnumPacketClient.values()[buffer.readInt()];
					PacketHandlerClient.this.client(buffer, player, en);
				} catch (Exception e) {
					LogWriter.error("Error with EnumPacketClient." + en, e);
				}
			}
		});
	}
}

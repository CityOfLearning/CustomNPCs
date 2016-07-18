//

//

package noppes.npcs.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noppes.npcs.CommonProxy;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.LogWriter;
import noppes.npcs.ModelData;
import noppes.npcs.ModelPartData;
import noppes.npcs.PacketHandlerPlayer;
import noppes.npcs.blocks.BlockBanner;
import noppes.npcs.blocks.BlockBuilder;
import noppes.npcs.blocks.BlockCarpentryBench;
import noppes.npcs.blocks.BlockCouchWood;
import noppes.npcs.blocks.BlockCouchWool;
import noppes.npcs.blocks.BlockLightable;
import noppes.npcs.blocks.BlockMailbox;
import noppes.npcs.blocks.BlockPlaceholder;
import noppes.npcs.blocks.BlockRotated;
import noppes.npcs.blocks.BlockTallLamp;
import noppes.npcs.blocks.BlockTombstone;
import noppes.npcs.blocks.BlockWallBanner;
import noppes.npcs.blocks.BlockWeaponRack;
import noppes.npcs.blocks.tiles.TileBanner;
import noppes.npcs.blocks.tiles.TileBarrel;
import noppes.npcs.blocks.tiles.TileBeam;
import noppes.npcs.blocks.tiles.TileBigSign;
import noppes.npcs.blocks.tiles.TileBlockAnvil;
import noppes.npcs.blocks.tiles.TileBook;
import noppes.npcs.blocks.tiles.TileCampfire;
import noppes.npcs.blocks.tiles.TileCandle;
import noppes.npcs.blocks.tiles.TileChair;
import noppes.npcs.blocks.tiles.TileCopy;
import noppes.npcs.blocks.tiles.TileCouchWood;
import noppes.npcs.blocks.tiles.TileCouchWool;
import noppes.npcs.blocks.tiles.TileCrate;
import noppes.npcs.blocks.tiles.TileDoor;
import noppes.npcs.blocks.tiles.TileLamp;
import noppes.npcs.blocks.tiles.TileMailbox;
import noppes.npcs.blocks.tiles.TilePedestal;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.blocks.tiles.TileShelf;
import noppes.npcs.blocks.tiles.TileSign;
import noppes.npcs.blocks.tiles.TileStool;
import noppes.npcs.blocks.tiles.TileTable;
import noppes.npcs.blocks.tiles.TileTallLamp;
import noppes.npcs.blocks.tiles.TileTombstone;
import noppes.npcs.blocks.tiles.TileTrading;
import noppes.npcs.blocks.tiles.TileWallBanner;
import noppes.npcs.blocks.tiles.TileWeaponRack;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.client.controllers.PresetController;
import noppes.npcs.client.fx.EntityElementalStaffFX;
import noppes.npcs.client.fx.EntityEnderFX;
import noppes.npcs.client.fx.EntityRainbowFX;
import noppes.npcs.client.gui.GuiBlockBuilder;
import noppes.npcs.client.gui.GuiBlockCopy;
import noppes.npcs.client.gui.GuiBorderBlock;
import noppes.npcs.client.gui.GuiMerchantAdd;
import noppes.npcs.client.gui.GuiNpcDimension;
import noppes.npcs.client.gui.GuiNpcMobSpawner;
import noppes.npcs.client.gui.GuiNpcMobSpawnerMounter;
import noppes.npcs.client.gui.GuiNpcPather;
import noppes.npcs.client.gui.GuiNpcRedstoneBlock;
import noppes.npcs.client.gui.GuiNpcRemoteEditor;
import noppes.npcs.client.gui.GuiNpcWaypoint;
import noppes.npcs.client.gui.GuiScript;
import noppes.npcs.client.gui.GuiScriptBlock;
import noppes.npcs.client.gui.GuiScriptDoor;
import noppes.npcs.client.gui.global.GuiNPCManageBanks;
import noppes.npcs.client.gui.global.GuiNPCManageDialogs;
import noppes.npcs.client.gui.global.GuiNPCManageFactions;
import noppes.npcs.client.gui.global.GuiNPCManageLinkedNpc;
import noppes.npcs.client.gui.global.GuiNPCManageQuest;
import noppes.npcs.client.gui.global.GuiNPCManageTransporters;
import noppes.npcs.client.gui.global.GuiNpcManageRecipes;
import noppes.npcs.client.gui.global.GuiNpcQuestReward;
import noppes.npcs.client.gui.mainmenu.GuiNPCGlobalMainMenu;
import noppes.npcs.client.gui.mainmenu.GuiNPCInv;
import noppes.npcs.client.gui.mainmenu.GuiNpcAI;
import noppes.npcs.client.gui.mainmenu.GuiNpcAdvanced;
import noppes.npcs.client.gui.mainmenu.GuiNpcDisplay;
import noppes.npcs.client.gui.mainmenu.GuiNpcStats;
import noppes.npcs.client.gui.player.GuiBigSign;
import noppes.npcs.client.gui.player.GuiCrate;
import noppes.npcs.client.gui.player.GuiMailbox;
import noppes.npcs.client.gui.player.GuiMailmanWrite;
import noppes.npcs.client.gui.player.GuiNPCBankChest;
import noppes.npcs.client.gui.player.GuiNPCTrader;
import noppes.npcs.client.gui.player.GuiNpcCarpentryBench;
import noppes.npcs.client.gui.player.GuiNpcFollower;
import noppes.npcs.client.gui.player.GuiNpcFollowerHire;
import noppes.npcs.client.gui.player.GuiTradingBlock;
import noppes.npcs.client.gui.player.GuiTransportSelection;
import noppes.npcs.client.gui.player.companion.GuiNpcCompanionInv;
import noppes.npcs.client.gui.player.companion.GuiNpcCompanionStats;
import noppes.npcs.client.gui.player.companion.GuiNpcCompanionTalents;
import noppes.npcs.client.gui.questtypes.GuiNpcQuestTypeItem;
import noppes.npcs.client.gui.roles.GuiNpcBankSetup;
import noppes.npcs.client.gui.roles.GuiNpcFollowerSetup;
import noppes.npcs.client.gui.roles.GuiNpcItemGiver;
import noppes.npcs.client.gui.roles.GuiNpcTraderSetup;
import noppes.npcs.client.gui.roles.GuiNpcTransporter;
import noppes.npcs.client.model.ModelBipedAlt;
import noppes.npcs.client.model.ModelNPCGolem;
import noppes.npcs.client.model.ModelNpcCrystal;
import noppes.npcs.client.model.ModelNpcDragon;
import noppes.npcs.client.model.ModelNpcSlime;
import noppes.npcs.client.model.ModelPlayerAlt;
import noppes.npcs.client.model.ModelSkirtArmor;
import noppes.npcs.client.renderer.RenderCustomNpc;
import noppes.npcs.client.renderer.RenderNPCInterface;
import noppes.npcs.client.renderer.RenderNPCPony;
import noppes.npcs.client.renderer.RenderNpcCrystal;
import noppes.npcs.client.renderer.RenderNpcDragon;
import noppes.npcs.client.renderer.RenderNpcSlime;
import noppes.npcs.client.renderer.RenderProjectile;
import noppes.npcs.client.renderer.TileEntityItemStackRendererAlt;
import noppes.npcs.client.renderer.blocks.BlockBannerRenderer;
import noppes.npcs.client.renderer.blocks.BlockBarrelRenderer;
import noppes.npcs.client.renderer.blocks.BlockBeamRenderer;
import noppes.npcs.client.renderer.blocks.BlockBigSignRenderer;
import noppes.npcs.client.renderer.blocks.BlockBookRenderer;
import noppes.npcs.client.renderer.blocks.BlockCampfireRenderer;
import noppes.npcs.client.renderer.blocks.BlockCandleRenderer;
import noppes.npcs.client.renderer.blocks.BlockCarpentryBenchRenderer;
import noppes.npcs.client.renderer.blocks.BlockChairRenderer;
import noppes.npcs.client.renderer.blocks.BlockCopyRenderer;
import noppes.npcs.client.renderer.blocks.BlockCouchWoodRenderer;
import noppes.npcs.client.renderer.blocks.BlockCouchWoolRenderer;
import noppes.npcs.client.renderer.blocks.BlockCrateRenderer;
import noppes.npcs.client.renderer.blocks.BlockDoorRenderer;
import noppes.npcs.client.renderer.blocks.BlockLampRenderer;
import noppes.npcs.client.renderer.blocks.BlockMailboxRenderer;
import noppes.npcs.client.renderer.blocks.BlockPedestalRenderer;
import noppes.npcs.client.renderer.blocks.BlockScriptedRenderer;
import noppes.npcs.client.renderer.blocks.BlockShelfRenderer;
import noppes.npcs.client.renderer.blocks.BlockSignRenderer;
import noppes.npcs.client.renderer.blocks.BlockStoolRenderer;
import noppes.npcs.client.renderer.blocks.BlockTableRenderer;
import noppes.npcs.client.renderer.blocks.BlockTallLampRenderer;
import noppes.npcs.client.renderer.blocks.BlockTombstoneRenderer;
import noppes.npcs.client.renderer.blocks.BlockTradingRenderer;
import noppes.npcs.client.renderer.blocks.BlockWallBannerRenderer;
import noppes.npcs.client.renderer.blocks.BlockWeaponRackRenderer;
import noppes.npcs.config.StringCache;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.containers.ContainerCarpentryBench;
import noppes.npcs.containers.ContainerCrate;
import noppes.npcs.containers.ContainerMail;
import noppes.npcs.containers.ContainerManageBanks;
import noppes.npcs.containers.ContainerManageRecipes;
import noppes.npcs.containers.ContainerNPCBankInterface;
import noppes.npcs.containers.ContainerNPCCompanion;
import noppes.npcs.containers.ContainerNPCFollower;
import noppes.npcs.containers.ContainerNPCFollowerHire;
import noppes.npcs.containers.ContainerNPCFollowerSetup;
import noppes.npcs.containers.ContainerNPCInv;
import noppes.npcs.containers.ContainerNPCTrader;
import noppes.npcs.containers.ContainerNPCTraderSetup;
import noppes.npcs.containers.ContainerNpcItemGiver;
import noppes.npcs.containers.ContainerNpcQuestReward;
import noppes.npcs.containers.ContainerNpcQuestTypeItem;
import noppes.npcs.containers.ContainerTradingBlock;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPC64x32;
import noppes.npcs.entity.EntityNPCGolem;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.EntityNpcCrystal;
import noppes.npcs.entity.EntityNpcDragon;
import noppes.npcs.entity.EntityNpcPony;
import noppes.npcs.entity.EntityNpcSlime;
import noppes.npcs.entity.EntityProjectile;
import tconstruct.client.tabs.InventoryTabFactions;
import tconstruct.client.tabs.InventoryTabQuests;
import tconstruct.client.tabs.InventoryTabVanilla;
import tconstruct.client.tabs.TabRegistry;

public class ClientProxy extends CommonProxy {
	public static class FontContainer {
		private StringCache textFont;
		public boolean useCustomFont;

		private FontContainer() {
			textFont = null;
			useCustomFont = true;
		}

		public FontContainer(final String fontType, final int fontSize) {
			textFont = null;
			useCustomFont = true;
			(textFont = new StringCache()).setDefaultFont("Arial", fontSize, true);
			useCustomFont = !fontType.equalsIgnoreCase("minecraft");
			try {
				if (!useCustomFont || fontType.isEmpty() || fontType.equalsIgnoreCase("default")) {
					textFont.setCustomFont(new ResourceLocation("customnpcs", "OpenSans.ttf"), fontSize, true);
				} else {
					textFont.setDefaultFont(fontType, fontSize, true);
				}
			} catch (Exception e) {
				LogWriter.info("Failed loading font so using Arial");
			}
		}

		public FontContainer copy() {
			final FontContainer font = new FontContainer();
			font.textFont = textFont;
			font.useCustomFont = useCustomFont;
			return font;
		}

		public void drawString(final String text, final int x, final int y, final int color) {
			if (useCustomFont) {
				textFont.renderString(text, x, y, color, true);
				textFont.renderString(text, x, y, color, false);
			} else {
				Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, color);
			}
		}

		public String getName() {
			if (!useCustomFont) {
				return "Minecraft";
			}
			return textFont.usedFont().getFontName();
		}

		public int height() {
			if (useCustomFont) {
				return textFont.fontHeight;
			}
			return Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
		}

		public int width(final String text) {
			if (useCustomFont) {
				return textFont.getStringWidth(text);
			}
			return Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
		}
	}

	public static KeyBinding QuestLog;
	public static KeyBinding Scene1;
	public static KeyBinding SceneReset;
	public static KeyBinding Scene2;
	public static KeyBinding Scene3;
	public static FontContainer Font;

	public static void bindTexture(final ResourceLocation location) {
		try {
			if (location == null) {
				return;
			}
			final TextureManager manager = Minecraft.getMinecraft().getTextureManager();
			ITextureObject ob = manager.getTexture(location);
			if (ob == null) {
				ob = new SimpleTexture(location);
				manager.loadTexture(location, ob);
			}
			GlStateManager.bindTexture(ob.getGlTextureId());
		} catch (NullPointerException ex) {
		} catch (ReportedException ex2) {
		}
	}

	private ModelSkirtArmor model;

	public ClientProxy() {
		model = new ModelSkirtArmor();
	}

	private void blockIgnoreBlockstate(final Block block, final IProperty... properties) {
		ModelLoader.setCustomStateMapper(block, new StateMap.Builder().ignore(properties).build());
	}

	private void createFolders() {
		final File file = new File(CustomNpcs.Dir, "assets/customnpcs");
		if (!file.exists()) {
			file.mkdirs();
		}
		File check = new File(file, "sounds");
		if (!check.exists()) {
			check.mkdir();
		}
		final File json = new File(file, "sounds.json");
		if (!json.exists()) {
			try {
				json.createNewFile();
				final BufferedWriter writer = new BufferedWriter(new FileWriter(json));
				writer.write("{\n\n}");
				writer.close();
			} catch (IOException ex) {
			}
		}
		check = new File(file, "textures");
		if (!check.exists()) {
			check.mkdir();
		}
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager())
				.registerReloadListener(new CustomNpcResourceListener());
	}

	@Override
	public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x,
			final int y, final int z) {
		if (ID > EnumGuiType.values().length) {
			return null;
		}
		final EnumGuiType gui = EnumGuiType.values()[ID];
		final EntityNPCInterface npc = NoppesUtil.getLastNpc();
		final Container container = getContainer(gui, player, x, y, z, npc);
		return getGui(npc, gui, container, x, y, z);
	}

	private GuiScreen getGui(final EntityNPCInterface npc, final EnumGuiType gui, final Container container,
			final int x, final int y, final int z) {
		if (gui == EnumGuiType.MainMenuDisplay) {
			if (npc != null) {
				return new GuiNpcDisplay(npc);
			}
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Unable to find npc"));
		} else {
			if (gui == EnumGuiType.MainMenuStats) {
				return new GuiNpcStats(npc);
			}
			if (gui == EnumGuiType.MainMenuInv) {
				return new GuiNPCInv(npc, (ContainerNPCInv) container);
			}
			if (gui == EnumGuiType.TradingBlock) {
				return new GuiTradingBlock((ContainerTradingBlock) container);
			}
			if (gui == EnumGuiType.MainMenuAdvanced) {
				return new GuiNpcAdvanced(npc);
			}
			if (gui == EnumGuiType.QuestReward) {
				return new GuiNpcQuestReward(npc, (ContainerNpcQuestReward) container);
			}
			if (gui == EnumGuiType.QuestItem) {
				return new GuiNpcQuestTypeItem(npc, (ContainerNpcQuestTypeItem) container);
			}
			if (gui == EnumGuiType.MovingPath) {
				return new GuiNpcPather(npc);
			}
			if (gui == EnumGuiType.ManageFactions) {
				return new GuiNPCManageFactions(npc);
			}
			if (gui == EnumGuiType.ManageLinked) {
				return new GuiNPCManageLinkedNpc(npc);
			}
			if (gui == EnumGuiType.BuilderBlock) {
				return new GuiBlockBuilder(x, y, z);
			}
			if (gui == EnumGuiType.ManageTransport) {
				return new GuiNPCManageTransporters(npc);
			}
			if (gui == EnumGuiType.ManageRecipes) {
				return new GuiNpcManageRecipes(npc, (ContainerManageRecipes) container);
			}
			if (gui == EnumGuiType.ManageDialogs) {
				return new GuiNPCManageDialogs(npc);
			}
			if (gui == EnumGuiType.ManageQuests) {
				return new GuiNPCManageQuest(npc);
			}
			if (gui == EnumGuiType.ManageBanks) {
				return new GuiNPCManageBanks(npc, (ContainerManageBanks) container);
			}
			if (gui == EnumGuiType.MainMenuGlobal) {
				return new GuiNPCGlobalMainMenu(npc);
			}
			if (gui == EnumGuiType.MainMenuAI) {
				return new GuiNpcAI(npc);
			}
			if (gui == EnumGuiType.PlayerFollowerHire) {
				return new GuiNpcFollowerHire(npc, (ContainerNPCFollowerHire) container);
			}
			if (gui == EnumGuiType.PlayerFollower) {
				return new GuiNpcFollower(npc, (ContainerNPCFollower) container);
			}
			if (gui == EnumGuiType.PlayerTrader) {
				return new GuiNPCTrader(npc, (ContainerNPCTrader) container);
			}
			if ((gui == EnumGuiType.PlayerBankSmall) || (gui == EnumGuiType.PlayerBankUnlock)
					|| (gui == EnumGuiType.PlayerBankUprade) || (gui == EnumGuiType.PlayerBankLarge)) {
				return new GuiNPCBankChest(npc, (ContainerNPCBankInterface) container);
			}
			if (gui == EnumGuiType.PlayerTransporter) {
				return new GuiTransportSelection(npc);
			}
			if (gui == EnumGuiType.Script) {
				return new GuiScript(npc);
			}
			if (gui == EnumGuiType.ScriptBlock) {
				return new GuiScriptBlock(x, y, z);
			}
			if (gui == EnumGuiType.ScriptDoor) {
				return new GuiScriptDoor(x, y, z);
			}
			if (gui == EnumGuiType.PlayerAnvil) {
				return new GuiNpcCarpentryBench((ContainerCarpentryBench) container);
			}
			if (gui == EnumGuiType.SetupFollower) {
				return new GuiNpcFollowerSetup(npc, (ContainerNPCFollowerSetup) container);
			}
			if (gui == EnumGuiType.SetupItemGiver) {
				return new GuiNpcItemGiver(npc, (ContainerNpcItemGiver) container);
			}
			if (gui == EnumGuiType.SetupTrader) {
				return new GuiNpcTraderSetup(npc, (ContainerNPCTraderSetup) container);
			}
			if (gui == EnumGuiType.SetupTransporter) {
				return new GuiNpcTransporter(npc);
			}
			if (gui == EnumGuiType.SetupBank) {
				return new GuiNpcBankSetup(npc);
			}
			if ((gui == EnumGuiType.NpcRemote) && (Minecraft.getMinecraft().currentScreen == null)) {
				return new GuiNpcRemoteEditor();
			}
			if (gui == EnumGuiType.PlayerMailman) {
				return new GuiMailmanWrite((ContainerMail) container, x == 1, y == 1);
			}
			if (gui == EnumGuiType.PlayerMailbox) {
				return new GuiMailbox();
			}
			if (gui == EnumGuiType.MerchantAdd) {
				return new GuiMerchantAdd();
			}
			if (gui == EnumGuiType.Crate) {
				return new GuiCrate((ContainerCrate) container);
			}
			if (gui == EnumGuiType.NpcDimensions) {
				return new GuiNpcDimension();
			}
			if (gui == EnumGuiType.Border) {
				return new GuiBorderBlock(x, y, z);
			}
			if (gui == EnumGuiType.BigSign) {
				return new GuiBigSign(x, y, z);
			}
			if (gui == EnumGuiType.RedstoneBlock) {
				return new GuiNpcRedstoneBlock(x, y, z);
			}
			if (gui == EnumGuiType.MobSpawner) {
				return new GuiNpcMobSpawner(x, y, z);
			}
			if (gui == EnumGuiType.CopyBlock) {
				return new GuiBlockCopy(x, y, z);
			}
			if (gui == EnumGuiType.MobSpawnerMounter) {
				return new GuiNpcMobSpawnerMounter(x, y, z);
			}
			if (gui == EnumGuiType.Waypoint) {
				return new GuiNpcWaypoint(x, y, z);
			}
			if (gui == EnumGuiType.Companion) {
				return new GuiNpcCompanionStats(npc);
			}
			if (gui == EnumGuiType.CompanionTalent) {
				return new GuiNpcCompanionTalents(npc);
			}
			if (gui == EnumGuiType.CompanionInv) {
				return new GuiNpcCompanionInv(npc, (ContainerNPCCompanion) container);
			}
		}
		return null;
	}

	@Override
	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}

	@Override
	public ModelBiped getSkirtModel() {
		return model;
	}

	@Override
	public boolean hasClient() {
		return true;
	}

	@Override
	public void load() {
		ClientProxy.Font = new FontContainer(CustomNpcs.FontType, CustomNpcs.FontSize);
		createFolders();
		CustomNpcs.Channel.register(new PacketHandlerClient());
		CustomNpcs.ChannelPlayer.register(new PacketHandlerPlayer());
		new MusicController();
		new TileEntityItemStackRendererAlt();
		MinecraftForge.EVENT_BUS.register(new ClientTickHandler());
		final Minecraft mc = Minecraft.getMinecraft();
		ClientProxy.QuestLog = new KeyBinding("Quest Log", 38, "key.categories.gameplay");
		if (CustomNpcs.SceneButtonsEnabled) {
			ClientProxy.Scene1 = new KeyBinding("Scene1 start/pause", 79, "key.categories.gameplay");
			ClientProxy.Scene2 = new KeyBinding("Scene2 start/pause", 80, "key.categories.gameplay");
			ClientProxy.Scene3 = new KeyBinding("Scene3 start/pause", 81, "key.categories.gameplay");
			ClientProxy.SceneReset = new KeyBinding("Scene reset", 82, "key.categories.gameplay");
			ClientRegistry.registerKeyBinding(ClientProxy.Scene1);
			ClientRegistry.registerKeyBinding(ClientProxy.Scene2);
			ClientRegistry.registerKeyBinding(ClientProxy.Scene3);
			ClientRegistry.registerKeyBinding(ClientProxy.SceneReset);
		}
		ClientRegistry.registerKeyBinding(ClientProxy.QuestLog);
		mc.gameSettings.loadOptions();
		new PresetController(CustomNpcs.Dir);
		if (CustomNpcs.EnableUpdateChecker) {
			final VersionChecker checker = new VersionChecker();
			checker.start();
		}
		blockIgnoreBlockstate(CustomItems.pedestal, BlockRotated.DAMAGE);
		blockIgnoreBlockstate(CustomItems.beam, BlockRotated.DAMAGE);
		blockIgnoreBlockstate(CustomItems.crate, BlockRotated.DAMAGE);
		blockIgnoreBlockstate(CustomItems.book, BlockRotated.DAMAGE);
		blockIgnoreBlockstate(CustomItems.stool, BlockRotated.DAMAGE);
		blockIgnoreBlockstate(CustomItems.chair, BlockRotated.DAMAGE);
		blockIgnoreBlockstate(CustomItems.sign, BlockRotated.DAMAGE);
		blockIgnoreBlockstate(CustomItems.barrel, BlockRotated.DAMAGE);
		blockIgnoreBlockstate(CustomItems.couchWood, BlockCouchWood.DAMAGE);
		blockIgnoreBlockstate(CustomItems.couchWool, BlockCouchWool.DAMAGE);
		blockIgnoreBlockstate(CustomItems.tombstone, BlockTombstone.DAMAGE);
		blockIgnoreBlockstate(CustomItems.bigsign, BlockRotated.DAMAGE);
		blockIgnoreBlockstate(CustomItems.table, BlockRotated.DAMAGE);
		blockIgnoreBlockstate(CustomItems.shelf, BlockRotated.DAMAGE);
		blockIgnoreBlockstate(CustomItems.wallBanner, BlockWallBanner.DAMAGE);
		blockIgnoreBlockstate(CustomItems.banner, BlockBanner.DAMAGE, BlockBanner.TOP);
		blockIgnoreBlockstate(CustomItems.weaponsRack, BlockWeaponRack.DAMAGE, BlockWeaponRack.IS_TOP);
		blockIgnoreBlockstate(CustomItems.tallLamp, BlockTallLamp.DAMAGE, BlockTallLamp.IS_TOP);
		blockIgnoreBlockstate(CustomItems.mailbox, BlockMailbox.ROTATION, BlockMailbox.TYPE);
		blockIgnoreBlockstate(CustomItems.carpentyBench, BlockCarpentryBench.ROTATION, BlockCarpentryBench.TYPE);
		blockIgnoreBlockstate(CustomItems.lamp, BlockRotated.DAMAGE, BlockLightable.LIT);
		blockIgnoreBlockstate(CustomItems.lamp_unlit, BlockRotated.DAMAGE, BlockLightable.LIT);
		blockIgnoreBlockstate(CustomItems.candle, BlockRotated.DAMAGE, BlockLightable.LIT);
		blockIgnoreBlockstate(CustomItems.candle_unlit, BlockRotated.DAMAGE, BlockLightable.LIT);
		blockIgnoreBlockstate(CustomItems.campfire, BlockRotated.DAMAGE, BlockLightable.LIT);
		blockIgnoreBlockstate(CustomItems.campfire_unlit, BlockRotated.DAMAGE, BlockLightable.LIT);
		blockIgnoreBlockstate(CustomItems.scriptedDoor, BlockDoor.POWERED);
		blockIgnoreBlockstate(CustomItems.builder, BlockBuilder.ROTATION);
		ModelLoader.setCustomStateMapper(CustomItems.placeholder, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(final IBlockState state) {
				final LinkedHashMap linkedhashmap = Maps.newLinkedHashMap((Map) state.getProperties());
				final IProperty iproperty = BlockPlaceholder.DAMAGE;
				final String s = iproperty.getName((Comparable) linkedhashmap.remove(BlockPlaceholder.DAMAGE));
				return new ModelResourceLocation("customnpcs:npcPlaceholder_" + s, getPropertyString(linkedhashmap));
			}
		});
	}

	@Override
	public void openGui(final EntityNPCInterface npc, final EnumGuiType gui) {
		this.openGui(npc, gui, 0, 0, 0);
	}

	@Override
	public void openGui(final EntityNPCInterface npc, final EnumGuiType gui, final int x, final int y, final int z) {
		final Minecraft minecraft = Minecraft.getMinecraft();
		final Container container = getContainer(gui, minecraft.thePlayer, x, y, z, npc);
		final GuiScreen guiscreen = getGui(npc, gui, container, x, y, z);
		if (guiscreen != null) {
			minecraft.displayGuiScreen(guiscreen);
		}
	}

	@Override
	public void openGui(final EntityPlayer player, final Object guiscreen) {
		final Minecraft minecraft = Minecraft.getMinecraft();
		if (!player.worldObj.isRemote || !(guiscreen instanceof GuiScreen)) {
			return;
		}
		if (guiscreen != null) {
			minecraft.displayGuiScreen((GuiScreen) guiscreen);
		}
	}

	@Override
	public void openGui(final int i, final int j, final int k, final EnumGuiType gui, final EntityPlayer player) {
		final Minecraft minecraft = Minecraft.getMinecraft();
		if (minecraft.thePlayer != player) {
			return;
		}
		final GuiScreen guiscreen = getGui(null, gui, null, i, j, k);
		if (guiscreen != null) {
			minecraft.displayGuiScreen(guiscreen);
		}
	}

	@Override
	public void postload() {
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
		if (CustomNpcs.InventoryGuiEnabled) {
			MinecraftForge.EVENT_BUS.register(new TabRegistry());
			if (TabRegistry.getTabList().size() < 2) {
				TabRegistry.registerTab(new InventoryTabVanilla());
			}
			TabRegistry.registerTab(new InventoryTabFactions());
			TabRegistry.registerTab(new InventoryTabQuests());
		}
		RenderingRegistry.registerEntityRenderingHandler(EntityNpcPony.class, new RenderNPCPony());
		RenderingRegistry.registerEntityRenderingHandler(EntityNpcCrystal.class,
				new RenderNpcCrystal(new ModelNpcCrystal(0.5f)));
		RenderingRegistry.registerEntityRenderingHandler(EntityNpcDragon.class,
				new RenderNpcDragon(new ModelNpcDragon(0.0f), 0.5f));
		RenderingRegistry.registerEntityRenderingHandler(EntityNpcSlime.class,
				new RenderNpcSlime(new ModelNpcSlime(16), new ModelNpcSlime(0), 0.25f));
		RenderingRegistry.registerEntityRenderingHandler(EntityProjectile.class, new RenderProjectile());
		RenderingRegistry.registerEntityRenderingHandler(EntityCustomNpc.class,
				new RenderCustomNpc(new ModelPlayerAlt(0.0f, false)));
		RenderingRegistry.registerEntityRenderingHandler(EntityNPC64x32.class,
				new RenderCustomNpc(new ModelBipedAlt(0.0f)));
		RenderingRegistry.registerEntityRenderingHandler(EntityNPCGolem.class,
				new RenderNPCInterface(new ModelNPCGolem(0.0f), 0.0f));
		ClientRegistry.bindTileEntitySpecialRenderer((Class) TileBlockAnvil.class,
				(TileEntitySpecialRenderer) new BlockCarpentryBenchRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer((Class) TileMailbox.class,
				(TileEntitySpecialRenderer) new BlockMailboxRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer((Class) TileScripted.class,
				(TileEntitySpecialRenderer) new BlockScriptedRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer((Class) TileDoor.class,
				(TileEntitySpecialRenderer) new BlockDoorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer((Class) TileCopy.class,
				(TileEntitySpecialRenderer) new BlockCopyRenderer());
		if (!CustomNpcs.DisableExtraBlock) {
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TileBanner.class,
					(TileEntitySpecialRenderer) new BlockBannerRenderer());
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TileWallBanner.class,
					(TileEntitySpecialRenderer) new BlockWallBannerRenderer());
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TileTallLamp.class,
					(TileEntitySpecialRenderer) new BlockTallLampRenderer());
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TileChair.class,
					(TileEntitySpecialRenderer) new BlockChairRenderer());
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TileWeaponRack.class,
					(TileEntitySpecialRenderer) new BlockWeaponRackRenderer());
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TileCrate.class,
					(TileEntitySpecialRenderer) new BlockCrateRenderer());
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TileCouchWool.class,
					(TileEntitySpecialRenderer) new BlockCouchWoolRenderer());
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TileCouchWood.class,
					(TileEntitySpecialRenderer) new BlockCouchWoodRenderer());
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TileTable.class,
					(TileEntitySpecialRenderer) new BlockTableRenderer());
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TileCandle.class,
					(TileEntitySpecialRenderer) new BlockCandleRenderer());
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TileLamp.class,
					(TileEntitySpecialRenderer) new BlockLampRenderer());
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TileStool.class,
					(TileEntitySpecialRenderer) new BlockStoolRenderer());
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TileBigSign.class,
					(TileEntitySpecialRenderer) new BlockBigSignRenderer());
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TileBarrel.class,
					(TileEntitySpecialRenderer) new BlockBarrelRenderer());
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TileCampfire.class,
					(TileEntitySpecialRenderer) new BlockCampfireRenderer());
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TileTombstone.class,
					(TileEntitySpecialRenderer) new BlockTombstoneRenderer());
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TileShelf.class,
					(TileEntitySpecialRenderer) new BlockShelfRenderer());
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TileSign.class,
					(TileEntitySpecialRenderer) new BlockSignRenderer());
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TileBeam.class,
					(TileEntitySpecialRenderer) new BlockBeamRenderer());
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TileBook.class,
					(TileEntitySpecialRenderer) new BlockBookRenderer());
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TilePedestal.class,
					(TileEntitySpecialRenderer) new BlockPedestalRenderer());
			ClientRegistry.bindTileEntitySpecialRenderer((Class) TileTrading.class,
					(TileEntitySpecialRenderer) new BlockTradingRenderer());
		}
	}

	@Override
	public void registerBlock(final Block block, final String name, final int meta,
			final Class<? extends ItemBlock> itemclass, final boolean seperateMetadata) {
		super.registerBlock(block, name, meta, itemclass, seperateMetadata);
		registerItems(GameRegistry.findItem("customnpcs", name), name, meta, seperateMetadata);
	}

	@Override
	public void registerItem(final Item item, final String name, final int meta) {
		final ModelResourceLocation location = new ModelResourceLocation("customnpcs:" + name, "inventory");
		ModelLoader.setCustomModelResourceLocation(item, meta, location);
	}

	public void registerItems(final Item item, final String name, final int meta, final boolean seperate) {
		if (meta > 0) {
			item.setHasSubtypes(true);
			for (int i = 0; i <= meta; ++i) {
				String metaname = name;
				if (seperate) {
					metaname = metaname + "_" + i;
					ModelBakery.addVariantName(item, new String[] { "customnpcs:" + metaname });
				}
				registerItem(item, metaname, i);
			}
		} else {
			registerItem(item, name, 0);
		}
	}

	@Override
	public void spawnParticle(final EntityLivingBase player, final String string, final Object... ob) {
		if (string.equals("Spell")) {
			final int color = (Integer) ob[0];
			for (int number = (Integer) ob[1], i = 0; i < number; ++i) {
				final Random rand = player.getRNG();
				final double x = (rand.nextDouble() - 0.5) * player.width;
				final double y = player.getEyeHeight();
				final double z = (rand.nextDouble() - 0.5) * player.width;
				final double f = (rand.nextDouble() - 0.5) * 2.0;
				final double f2 = -rand.nextDouble();
				final double f3 = (rand.nextDouble() - 0.5) * 2.0;
				Minecraft.getMinecraft().effectRenderer
						.addEffect(new EntityElementalStaffFX(player, x, y, z, f, f2, f3, color));
			}
		} else if (string.equals("Block")) {
			final BlockPos pos = (BlockPos) ob[0];
			final int id = (Integer) ob[1];
			final Block block = Block.getBlockById(id & 0xFFF);
			Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(pos,
					block.getStateFromMeta((id >> 12) & 0xFF));
		} else if (string.equals("ModelData")) {
			final ModelData data = (ModelData) ob[0];
			final ModelPartData particles = (ModelPartData) ob[1];
			final EntityCustomNpc npc = (EntityCustomNpc) player;
			final Minecraft minecraft = Minecraft.getMinecraft();
			final double height = npc.getYOffset() + data.getBodyY();
			final Random rand2 = npc.getRNG();
			if (particles.type == 0) {
				for (int j = 0; j < 2; ++j) {
					final EntityEnderFX fx = new EntityEnderFX(npc, (rand2.nextDouble() - 0.5) * player.width,
							(rand2.nextDouble() * player.height) - height - 0.25,
							(rand2.nextDouble() - 0.5) * player.width, (rand2.nextDouble() - 0.5) * 2.0,
							-rand2.nextDouble(), (rand2.nextDouble() - 0.5) * 2.0, particles);
					minecraft.effectRenderer.addEffect(fx);
				}
			} else if (particles.type == 1) {
				for (int j = 0; j < 2; ++j) {
					final double x2 = player.posX + ((rand2.nextDouble() - 0.5) * 0.9);
					final double y2 = (player.posY + (rand2.nextDouble() * 1.9)) - 0.25 - height;
					final double z2 = player.posZ + ((rand2.nextDouble() - 0.5) * 0.9);
					final double f4 = (rand2.nextDouble() - 0.5) * 2.0;
					final double f5 = -rand2.nextDouble();
					final double f6 = (rand2.nextDouble() - 0.5) * 2.0;
					minecraft.effectRenderer.addEffect(new EntityRainbowFX(player.worldObj, x2, y2, z2, f4, f5, f6));
				}
			}
		}
	}

	@Override
	public void spawnParticle(final EnumParticleTypes particle, final double x, final double y, final double z,
			final double motionX, final double motionY, final double motionZ, final float scale) {
		final Minecraft mc = Minecraft.getMinecraft();
		final double xx = mc.getRenderViewEntity().posX - x;
		final double yy = mc.getRenderViewEntity().posY - y;
		final double zz = mc.getRenderViewEntity().posZ - z;
		if (((xx * xx) + (yy * yy) + (zz * zz)) > 256.0) {
			return;
		}
		final EntityFX fx = mc.effectRenderer.spawnEffectParticle(particle.getParticleID(), x, y, z, motionX, motionY,
				motionZ, new int[0]);
		if (fx == null) {
			return;
		}
		if (particle == EnumParticleTypes.FLAME) {
			ObfuscationReflectionHelper.setPrivateValue((Class) EntityFlameFX.class, fx, scale, 0);
		} else if (particle == EnumParticleTypes.SMOKE_NORMAL) {
			ObfuscationReflectionHelper.setPrivateValue((Class) EntitySmokeFX.class, fx, scale, 0);
		}
	}
}

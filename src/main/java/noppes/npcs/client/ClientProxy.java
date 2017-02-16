package noppes.npcs.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
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
import noppes.npcs.blocks.BlockBanner;
import noppes.npcs.blocks.BlockBuilder;
import noppes.npcs.blocks.BlockCarpentryBench;
import noppes.npcs.blocks.BlockCouchWood;
import noppes.npcs.blocks.BlockCouchWool;
import noppes.npcs.blocks.BlockLightable;
import noppes.npcs.blocks.BlockMailbox;
import noppes.npcs.blocks.BlockRotated;
import noppes.npcs.blocks.BlockTallLamp;
import noppes.npcs.blocks.BlockTombstone;
import noppes.npcs.blocks.BlockWallBanner;
import noppes.npcs.blocks.BlockWeaponRack;
import noppes.npcs.blocks.tiles.TileBanner;
import noppes.npcs.blocks.tiles.TileBarrel;
import noppes.npcs.blocks.tiles.TileBeam;
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
import noppes.npcs.client.gui.player.GuiTextSign;
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
import noppes.npcs.client.renderer.RenderCustomNpc;
import noppes.npcs.client.renderer.RenderNPCInterface;
import noppes.npcs.client.renderer.RenderNPCPony;
import noppes.npcs.client.renderer.RenderNpcCrystal;
import noppes.npcs.client.renderer.RenderNpcDragon;
import noppes.npcs.client.renderer.RenderNpcSlime;
import noppes.npcs.client.renderer.TileEntityItemStackRendererAlt;
import noppes.npcs.client.renderer.blocks.BlockBannerRenderer;
import noppes.npcs.client.renderer.blocks.BlockBarrelRenderer;
import noppes.npcs.client.renderer.blocks.BlockBeamRenderer;
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
import noppes.npcs.model.ModelData;
import noppes.npcs.model.ModelPartData;
import noppes.npcs.packets.PacketHandlerPlayer;
import tconstruct.client.tabs.InventoryTabFactions;
import tconstruct.client.tabs.InventoryTabQuests;
import tconstruct.client.tabs.InventoryTabVanilla;
import tconstruct.client.tabs.TabRegistry;

public class ClientProxy extends CommonProxy {

	public static KeyBinding QuestLog;
	public static KeyBinding Scene1;
	public static KeyBinding SceneReset;
	public static KeyBinding Scene2;
	public static KeyBinding Scene3;

	public static void bindTexture(ResourceLocation location) {
		try {
			if (location == null) {
				return;
			}
			TextureManager manager = Minecraft.getMinecraft().getTextureManager();
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

	public ClientProxy() {
	}

	private void blockIgnoreBlockstate(Block block, IProperty... properties) {
		ModelLoader.setCustomStateMapper(block, new StateMap.Builder().ignore(properties).build());
	}

	private void createFolders() {
		File file = new File(CustomNpcs.Dir, "assets/customnpcs");
		if (!file.exists()) {
			file.mkdirs();
		}
		File check = new File(file, "sounds");
		if (!check.exists()) {
			check.mkdir();
		}
		File json = new File(file, "sounds.json");
		if (!json.exists()) {
			try {
				json.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(json));
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
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID > EnumGuiType.values().length) {
			return null;
		}
		EnumGuiType gui = EnumGuiType.values()[ID];
		EntityNPCInterface npc = NoppesUtil.getLastNpc();
		Container container = getContainer(gui, player, x, y, z, npc);
		return getGui(npc, gui, container, x, y, z);
	}

	private GuiScreen getGui(EntityNPCInterface npc, EnumGuiType gui, Container container, int x, int y, int z) {
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
				return new GuiTextSign(x, y, z);
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
	public boolean hasClient() {
		return true;
	}

	@Override
	public void load() {
		createFolders();
		CustomNpcs.Channel.register(new PacketHandlerClient());
		CustomNpcs.ChannelPlayer.register(new PacketHandlerPlayer());
		new MusicController();
		new TileEntityItemStackRendererAlt();
		MinecraftForge.EVENT_BUS.register(new ClientTickHandler());

		Minecraft mc = Minecraft.getMinecraft();

		QuestLog = new KeyBinding("Quest Log", 38, "key.categories.gameplay");
		if (CustomNpcs.SceneButtonsEnabled) {
			Scene1 = new KeyBinding("Scene1 start/pause", 79, "key.categories.gameplay");
			Scene2 = new KeyBinding("Scene2 start/pause", 80, "key.categories.gameplay");
			Scene3 = new KeyBinding("Scene3 start/pause", 81, "key.categories.gameplay");
			SceneReset = new KeyBinding("Scene reset", 82, "key.categories.gameplay");

			ClientRegistry.registerKeyBinding(Scene1);
			ClientRegistry.registerKeyBinding(Scene2);
			ClientRegistry.registerKeyBinding(Scene3);
			ClientRegistry.registerKeyBinding(SceneReset);
		}
		ClientRegistry.registerKeyBinding(QuestLog);
		mc.gameSettings.loadOptions();

		new PresetController(CustomNpcs.Dir);

		blockIgnoreBlockstate(CustomItems.pedestal, new IProperty[] { BlockRotated.DAMAGE });
		blockIgnoreBlockstate(CustomItems.beam, new IProperty[] { BlockRotated.DAMAGE });
		blockIgnoreBlockstate(CustomItems.crate, new IProperty[] { BlockRotated.DAMAGE });
		blockIgnoreBlockstate(CustomItems.book, new IProperty[] { BlockRotated.DAMAGE });
		blockIgnoreBlockstate(CustomItems.stool, new IProperty[] { BlockRotated.DAMAGE });
		blockIgnoreBlockstate(CustomItems.chair, new IProperty[] { BlockRotated.DAMAGE });
		blockIgnoreBlockstate(CustomItems.sign, new IProperty[] { BlockRotated.DAMAGE });
		blockIgnoreBlockstate(CustomItems.barrel, new IProperty[] { BlockRotated.DAMAGE });
		blockIgnoreBlockstate(CustomItems.couchWood, new IProperty[] { BlockCouchWood.DAMAGE });
		blockIgnoreBlockstate(CustomItems.couchWool, new IProperty[] { BlockCouchWool.DAMAGE });
		blockIgnoreBlockstate(CustomItems.tombstone, new IProperty[] { BlockTombstone.DAMAGE });
		blockIgnoreBlockstate(CustomItems.table, new IProperty[] { BlockRotated.DAMAGE });
		blockIgnoreBlockstate(CustomItems.shelf, new IProperty[] { BlockRotated.DAMAGE });

		blockIgnoreBlockstate(CustomItems.wallBanner, new IProperty[] { BlockWallBanner.DAMAGE });
		blockIgnoreBlockstate(CustomItems.banner, new IProperty[] { BlockBanner.DAMAGE, BlockBanner.TOP });
		blockIgnoreBlockstate(CustomItems.weaponsRack,
				new IProperty[] { BlockWeaponRack.DAMAGE, BlockWeaponRack.IS_TOP });
		blockIgnoreBlockstate(CustomItems.tallLamp, new IProperty[] { BlockTallLamp.DAMAGE, BlockTallLamp.IS_TOP });
		blockIgnoreBlockstate(CustomItems.mailbox, new IProperty[] { BlockMailbox.ROTATION, BlockMailbox.TYPE });
		blockIgnoreBlockstate(CustomItems.carpentyBench,
				new IProperty[] { BlockCarpentryBench.ROTATION, BlockCarpentryBench.TYPE });

		blockIgnoreBlockstate(CustomItems.lamp, new IProperty[] { BlockRotated.DAMAGE, BlockLightable.LIT });
		blockIgnoreBlockstate(CustomItems.lamp_unlit, new IProperty[] { BlockRotated.DAMAGE, BlockLightable.LIT });
		blockIgnoreBlockstate(CustomItems.candle, new IProperty[] { BlockRotated.DAMAGE, BlockLightable.LIT });
		blockIgnoreBlockstate(CustomItems.candle_unlit, new IProperty[] { BlockRotated.DAMAGE, BlockLightable.LIT });
		blockIgnoreBlockstate(CustomItems.campfire, new IProperty[] { BlockRotated.DAMAGE, BlockLightable.LIT });
		blockIgnoreBlockstate(CustomItems.campfire_unlit, new IProperty[] { BlockRotated.DAMAGE, BlockLightable.LIT });

		blockIgnoreBlockstate(CustomItems.scriptedDoor, new IProperty[] { BlockDoor.POWERED });

		blockIgnoreBlockstate(CustomItems.builder, new IProperty[] { BlockBuilder.ROTATION });
	}

	@Override
	public void openGui(EntityNPCInterface npc, EnumGuiType gui) {
		this.openGui(npc, gui, 0, 0, 0);
	}

	@Override
	public void openGui(EntityNPCInterface npc, EnumGuiType gui, int x, int y, int z) {
		Minecraft minecraft = Minecraft.getMinecraft();
		Container container = getContainer(gui, minecraft.thePlayer, x, y, z, npc);
		GuiScreen guiscreen = getGui(npc, gui, container, x, y, z);
		if (guiscreen != null) {
			minecraft.displayGuiScreen(guiscreen);
		}
	}

	@Override
	public void openGui(EntityPlayer player, Object guiscreen) {
		Minecraft minecraft = Minecraft.getMinecraft();
		if (!player.worldObj.isRemote || !(guiscreen instanceof GuiScreen)) {
			return;
		}
		if (guiscreen != null) {
			minecraft.displayGuiScreen((GuiScreen) guiscreen);
		}
	}

	@Override
	public void openGui(int i, int j, int k, EnumGuiType gui, EntityPlayer player) {
		Minecraft minecraft = Minecraft.getMinecraft();
		if (minecraft.thePlayer != player) {
			return;
		}
		GuiScreen guiscreen = getGui(null, gui, null, i, j, k);
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
				new RenderNpcCrystal(new ModelNpcCrystal(0.5F)));
		RenderingRegistry.registerEntityRenderingHandler(EntityNpcDragon.class,
				new RenderNpcDragon(new ModelNpcDragon(0.0F), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityNpcSlime.class,
				new RenderNpcSlime(new ModelNpcSlime(16), new ModelNpcSlime(0), 0.25F));
		RenderingRegistry.registerEntityRenderingHandler(EntityCustomNpc.class,
				new RenderCustomNpc(new ModelPlayerAlt(0.0F, false)));
		RenderingRegistry.registerEntityRenderingHandler(EntityNPC64x32.class,
				new RenderCustomNpc(new ModelBipedAlt(0.0F)));
		RenderingRegistry.registerEntityRenderingHandler(EntityNPCGolem.class,
				new RenderNPCInterface(new ModelNPCGolem(0.0F), 0.0F));

		ClientRegistry.bindTileEntitySpecialRenderer(TileBlockAnvil.class, new BlockCarpentryBenchRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMailbox.class, new BlockMailboxRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileScripted.class, new BlockScriptedRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileDoor.class, new BlockDoorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCopy.class, new BlockCopyRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileBanner.class, new BlockBannerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileWallBanner.class, new BlockWallBannerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileTallLamp.class, new BlockTallLampRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileChair.class, new BlockChairRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileWeaponRack.class, new BlockWeaponRackRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCrate.class, new BlockCrateRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCouchWool.class, new BlockCouchWoolRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCouchWood.class, new BlockCouchWoodRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileTable.class, new BlockTableRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCandle.class, new BlockCandleRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileLamp.class, new BlockLampRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileStool.class, new BlockStoolRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileBarrel.class, new BlockBarrelRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCampfire.class, new BlockCampfireRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileTombstone.class, new BlockTombstoneRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileShelf.class, new BlockShelfRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileSign.class, new BlockSignRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileBeam.class, new BlockBeamRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileBook.class, new BlockBookRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePedestal.class, new BlockPedestalRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileTrading.class, new BlockTradingRenderer());

	}

	@Override
	public void registerBlock(Block block, String name, int meta, Class<? extends ItemBlock> itemclass,
			boolean seperateMetadata) {
		super.registerBlock(block, name, meta, itemclass, seperateMetadata);
		registerItems(GameRegistry.findItem("customnpcs", name), name, meta, seperateMetadata);
	}

	@Override
	public void registerItem(Item item, String name, int meta) {
		ModelResourceLocation location = new ModelResourceLocation("customnpcs:" + name, "inventory");
		ModelLoader.setCustomModelResourceLocation(item, meta, location);
	}

	public void registerItems(Item item, String name, int meta, boolean seperate) {
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
	public void spawnParticle(EntityLivingBase player, String string, Object... ob) {
		if (string.equals("Spell")) {
			for (int number = (Integer) ob[1], i = 0; i < number; ++i) {
				Random rand = player.getRNG();
				rand.nextDouble();
				player.getEyeHeight();
				rand.nextDouble();
				rand.nextDouble();
				rand.nextDouble();
				rand.nextDouble();
			}
		} else if (string.equals("Block")) {
			BlockPos pos = (BlockPos) ob[0];
			int id = (Integer) ob[1];
			Block block = Block.getBlockById(id & 0xFFF);
			Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(pos,
					block.getStateFromMeta((id >> 12) & 0xFF));
		} else if (string.equals("ModelData")) {
			ModelData data = (ModelData) ob[0];
			ModelPartData particles = (ModelPartData) ob[1];
			EntityCustomNpc npc = (EntityCustomNpc) player;
			Minecraft minecraft = Minecraft.getMinecraft();
			double height = npc.getYOffset() + data.getBodyY();
			Random rand2 = npc.getRNG();
			if (particles.type == 0) {
				for (int j = 0; j < 2; ++j) {
					EntityEnderFX fx = new EntityEnderFX(npc, (rand2.nextDouble() - 0.5) * player.width,
							(rand2.nextDouble() * player.height) - height - 0.25,
							(rand2.nextDouble() - 0.5) * player.width, (rand2.nextDouble() - 0.5) * 2.0,
							-rand2.nextDouble(), (rand2.nextDouble() - 0.5) * 2.0, particles);
					minecraft.effectRenderer.addEffect(fx);
				}
			} else if (particles.type == 1) {
				for (int j = 0; j < 2; ++j) {
					double x2 = player.posX + ((rand2.nextDouble() - 0.5) * 0.9);
					double y2 = (player.posY + (rand2.nextDouble() * 1.9)) - 0.25 - height;
					double z2 = player.posZ + ((rand2.nextDouble() - 0.5) * 0.9);
					double f4 = (rand2.nextDouble() - 0.5) * 2.0;
					double f5 = -rand2.nextDouble();
					double f6 = (rand2.nextDouble() - 0.5) * 2.0;
					minecraft.effectRenderer.addEffect(new EntityRainbowFX(player.worldObj, x2, y2, z2, f4, f5, f6));
				}
			}
		}
	}

	@Override
	public void spawnParticle(EnumParticleTypes particle, double x, double y, double z, double motionX, double motionY,
			double motionZ, float scale) {
		Minecraft mc = Minecraft.getMinecraft();
		double xx = mc.getRenderViewEntity().posX - x;
		double yy = mc.getRenderViewEntity().posY - y;
		double zz = mc.getRenderViewEntity().posZ - z;
		if (((xx * xx) + (yy * yy) + (zz * zz)) > 256.0) {
			return;
		}
		EntityFX fx = mc.effectRenderer.spawnEffectParticle(particle.getParticleID(), x, y, z, motionX, motionY,
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

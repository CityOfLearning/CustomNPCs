package noppes.npcs;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.BlockVine;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import noppes.npcs.command.CommandNoppes;
import noppes.npcs.config.ConfigLoader;
import noppes.npcs.config.ConfigProp;
import noppes.npcs.controllers.ChunkController;
import noppes.npcs.controllers.GlobalDataController;
import noppes.npcs.controllers.LinkedNpcController;
import noppes.npcs.controllers.MassBlockController;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.controllers.bank.BankController;
import noppes.npcs.controllers.dialog.DialogController;
import noppes.npcs.controllers.faction.FactionController;
import noppes.npcs.controllers.quest.QuestController;
import noppes.npcs.controllers.recipies.RecipeController;
import noppes.npcs.controllers.script.ScriptController;
import noppes.npcs.controllers.spawn.SpawnController;
import noppes.npcs.controllers.transport.TransportController;
import noppes.npcs.entity.EntityChairMount;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPC64x32;
import noppes.npcs.entity.EntityNPCGolem;
import noppes.npcs.entity.EntityNpcCrystal;
import noppes.npcs.entity.EntityNpcDragon;
import noppes.npcs.entity.EntityNpcPony;
import noppes.npcs.entity.EntityNpcSlime;
import noppes.npcs.entity.old.EntityNPCDwarfFemale;
import noppes.npcs.entity.old.EntityNPCDwarfMale;
import noppes.npcs.entity.old.EntityNPCElfFemale;
import noppes.npcs.entity.old.EntityNPCElfMale;
import noppes.npcs.entity.old.EntityNPCEnderman;
import noppes.npcs.entity.old.EntityNPCFurryFemale;
import noppes.npcs.entity.old.EntityNPCFurryMale;
import noppes.npcs.entity.old.EntityNPCHumanFemale;
import noppes.npcs.entity.old.EntityNPCHumanMale;
import noppes.npcs.entity.old.EntityNPCOrcFemale;
import noppes.npcs.entity.old.EntityNPCOrcMale;
import noppes.npcs.entity.old.EntityNPCVillager;
import noppes.npcs.entity.old.EntityNpcEnderchibi;
import noppes.npcs.entity.old.EntityNpcMonsterFemale;
import noppes.npcs.entity.old.EntityNpcMonsterMale;
import noppes.npcs.entity.old.EntityNpcNagaFemale;
import noppes.npcs.entity.old.EntityNpcNagaMale;
import noppes.npcs.entity.old.EntityNpcSkeleton;

@Mod(modid = "customnpcs", name = "CustomNpcs", version = "dyn1.8.9", acceptedMinecraftVersions = "[1.8.9]")
public class CustomNpcs {
	@ConfigProp(info = "Disable Chat Bubbles")
	public static boolean EnableChatBubbles;
	private static int NewEntityStartId;
	@ConfigProp(info = "Navigation search range for NPCs. Not recommended to increase if you have a slow pc or on a server")
	public static int NpcNavRange;
	@ConfigProp(info = "Set to true if you want the dialog command option to be able to use op commands like tp etc")
	public static boolean NpcUseOpCommands;
	@ConfigProp
	public static boolean InventoryGuiEnabled;
	@ConfigProp
	public static boolean SceneButtonsEnabled;
	public static long ticks;
	@SidedProxy(clientSide = "noppes.npcs.client.ClientProxy", serverSide = "noppes.npcs.CommonProxy")
	public static CommonProxy proxy;
	public static CustomNpcs instance;
	public static boolean FreezeNPCs;
	@ConfigProp(info = "Only ops can create and edit npcs")
	public static boolean OpsOnly;
	@ConfigProp(info = "Default interact line. Leave empty to not have one")
	public static String DefaultInteractLine;
	@ConfigProp
	public static boolean DisableEnchants;
	@ConfigProp(info = "Start Id for enchants. IDs can only range from 0-256")
	public static int EnchantStartId;
	@ConfigProp(info = "Number of chunk loading npcs that can be active at the same time")
	public static int ChuckLoaders;
	public static File Dir;
	@ConfigProp(info = "Enables leaves decay")
	public static boolean LeavesDecayEnabled;
	@ConfigProp(info = "Enables Vine Growth")
	public static boolean VineGrowthEnabled;
	@ConfigProp(info = "Enables Ice Melting")
	public static boolean IceMeltsEnabled;
	@ConfigProp(info = "Type 0 = Normal, Type 1 = Solid")
	public static int HeadWearType;
	public static FMLEventChannel Channel;
	public static FMLEventChannel ChannelPlayer;
	public static ConfigLoader Config;
	public static CommandNoppes NoppesCommand;

	public static Logger logger;

	static {
		CustomNpcs.EnableChatBubbles = true;
		CustomNpcs.NewEntityStartId = 0;
		CustomNpcs.NpcNavRange = 32;
		CustomNpcs.NpcUseOpCommands = false;
		CustomNpcs.InventoryGuiEnabled = true;
		CustomNpcs.SceneButtonsEnabled = true;
		CustomNpcs.FreezeNPCs = false;
		CustomNpcs.OpsOnly = false;
		CustomNpcs.DefaultInteractLine = "Hello @p";
		CustomNpcs.DisableEnchants = false;
		CustomNpcs.EnchantStartId = 100;
		CustomNpcs.ChuckLoaders = 20;
		CustomNpcs.LeavesDecayEnabled = true;
		CustomNpcs.VineGrowthEnabled = true;
		CustomNpcs.IceMeltsEnabled = true;
		CustomNpcs.HeadWearType = 1;
		CustomNpcs.NoppesCommand = new CommandNoppes();
	}

	public static File getWorldSaveDirectory() {
		try {
			MinecraftServer server = MinecraftServer.getServer();
			if (server == null) {
				return null;
			}
			File saves = new File(".");
			if (!server.isDedicatedServer()) {
				saves = new File(Minecraft.getMinecraft().mcDataDir, "saves");
			}
			File savedir = new File(new File(saves, server.getFolderName()), "customnpcs");
			if (!savedir.exists()) {
				savedir.mkdir();
			}
			return savedir;
		} catch (Exception e) {
			return null;
		}
	}

	public CustomNpcs() {
		CustomNpcs.instance = this;
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent ev) {
		ForgeModContainer.fullBoundingBoxLadders = true;
		new RecipeController();
		CustomNpcs.proxy.postload();
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent ev) {
		logger = ev.getModLog();
		CustomNpcs.Channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("CustomNPCs");
		CustomNpcs.ChannelPlayer = NetworkRegistry.INSTANCE.newEventDrivenChannel("CustomNPCsPlayer");
		MinecraftServer server = MinecraftServer.getServer();
		String dir = "";
		if (server != null) {
			dir = new File(".").getAbsolutePath();
		} else {
			dir = Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
		}
		(CustomNpcs.Dir = new File(dir, "customnpcs")).mkdir();
		(CustomNpcs.Config = new ConfigLoader(this.getClass(), new File(dir, "config"), "CustomNpcs")).loadConfig();
		if (CustomNpcs.NpcNavRange < 16) {
			CustomNpcs.NpcNavRange = 16;
		}
		CustomItems.load();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, CustomNpcs.proxy);
		MinecraftForge.EVENT_BUS.register(new ServerEventsHandler());
		MinecraftForge.EVENT_BUS.register(new ScriptController());
		MinecraftForge.EVENT_BUS.register(new ServerTickHandler());
		registerNpc(EntityNPCHumanMale.class, "npchumanmale");
		registerNpc(EntityNPCVillager.class, "npcvillager");
		registerNpc(EntityNpcPony.class, "npcpony");
		registerNpc(EntityNPCHumanFemale.class, "npchumanfemale");
		registerNpc(EntityNPCDwarfMale.class, "npcdwarfmale");
		registerNpc(EntityNPCFurryMale.class, "npcfurrymale");
		registerNpc(EntityNpcMonsterMale.class, "npczombiemale");
		registerNpc(EntityNpcMonsterFemale.class, "npczombiefemale");
		registerNpc(EntityNpcSkeleton.class, "npcskeleton");
		registerNpc(EntityNPCDwarfFemale.class, "npcdwarffemale");
		registerNpc(EntityNPCFurryFemale.class, "npcfurryfemale");
		registerNpc(EntityNPCOrcMale.class, "npcorcfmale");
		registerNpc(EntityNPCOrcFemale.class, "npcorcfemale");
		registerNpc(EntityNPCElfMale.class, "npcelfmale");
		registerNpc(EntityNPCElfFemale.class, "npcelffemale");
		registerNpc(EntityNpcCrystal.class, "npccrystal");
		registerNpc(EntityNpcEnderchibi.class, "npcenderchibi");
		registerNpc(EntityNpcNagaMale.class, "npcnagamale");
		registerNpc(EntityNpcNagaFemale.class, "npcnagafemale");
		registerNpc(EntityNpcSlime.class, "NpcSlime");
		registerNpc(EntityNpcDragon.class, "NpcDragon");
		registerNpc(EntityNPCEnderman.class, "npcEnderman");
		registerNpc(EntityNPCGolem.class, "npcGolem");
		registerNpc(EntityCustomNpc.class, "CustomNpc");
		registerNpc(EntityNPC64x32.class, "CustomNpc64x32");

		registerNewEntity(EntityChairMount.class, "CustomNpcChairMount", 64, 10, false);

		ArrayList<BiomeGenBase> list = new ArrayList<>();
		for (BiomeGenBase base : BiomeGenBase.getBiomeGenArray()) {
			if (base != null) {
				list.add(base);
			}
		}
		ForgeChunkManager.setForcedChunkLoadingCallback(this, new ChunkController());
		CustomNpcs.proxy.load();
	}

	private void registerNewEntity(Class<? extends Entity> cl, String name, int range, int update, boolean velocity) {
		EntityRegistry.registerModEntity(cl, name, NewEntityStartId++, this, range, update, velocity);
	}

	private void registerNpc(Class<? extends Entity> cl, String name) {
		// EntityList.stringToClassMapping.put(name, cl);
		EntityRegistry.registerModEntity(cl, name, CustomNpcs.NewEntityStartId++, this, 64, 3, true);
	}

	@Mod.EventHandler
	public void serverstart(FMLServerStartingEvent event) {
		event.registerServerCommand(CustomNpcs.NoppesCommand);
	}

	@Mod.EventHandler
	public void setAboutToStart(FMLServerAboutToStartEvent event) {
		ChunkController.instance.clear();
		new QuestController();
		new PlayerDataController();
		new FactionController();
		new TransportController();
		new GlobalDataController();
		new SpawnController();
		new LinkedNpcController();
		new MassBlockController();
		ScriptController.Instance.loadStoredData();
		ScriptController.HasStart = false;
		Set<ResourceLocation> names = Block.blockRegistry.getKeys();
		for (ResourceLocation name : names) {
			Block block = Block.blockRegistry.getObject(name);
			if (block instanceof BlockLeavesBase) {
				block.setTickRandomly(CustomNpcs.LeavesDecayEnabled);
			}
			if (block instanceof BlockVine) {
				block.setTickRandomly(CustomNpcs.VineGrowthEnabled);
			}
			if (block instanceof BlockIce) {
				block.setTickRandomly(CustomNpcs.IceMeltsEnabled);
			}
		}
	}

	@Mod.EventHandler
	public void started(FMLServerStartedEvent event) {
		RecipeController.instance.load();
		new DialogController();
		new BankController();
		QuestController.instance.load();
		ScriptController.HasStart = true;
		ServerCloneController.Instance = new ServerCloneController();
	}

	@Mod.EventHandler
	public void stopped(FMLServerStoppedEvent event) {
		ServerCloneController.Instance = null;
	}
}

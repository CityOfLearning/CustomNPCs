package noppes.npcs;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.BlockVine;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import noppes.npcs.CommonProxy;
import noppes.npcs.CustomItems;
import noppes.npcs.ServerEventsHandler;
import noppes.npcs.ServerTickHandler;
import noppes.npcs.command.CommandNoppes;
import noppes.npcs.config.ConfigLoader;
import noppes.npcs.config.ConfigProp;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.ChunkController;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.GlobalDataController;
import noppes.npcs.controllers.LinkedNpcController;
import noppes.npcs.controllers.MassBlockController;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.controllers.SpawnController;
import noppes.npcs.controllers.TransportController;
import noppes.npcs.enchants.EnchantInterface;
import noppes.npcs.entity.EntityChairMount;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPC64x32;
import noppes.npcs.entity.EntityNPCGolem;
import noppes.npcs.entity.EntityNpcCrystal;
import noppes.npcs.entity.EntityNpcDragon;
import noppes.npcs.entity.EntityNpcPony;
import noppes.npcs.entity.EntityNpcSlime;
import noppes.npcs.entity.EntityProjectile;
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

@Mod(
   modid = "customnpcs",
   name = "CustomNpcs",
   version = "1.8.9_beta",
   acceptedMinecraftVersions = "[1.8.9]"
)
public class CustomNpcs {

   @ConfigProp(
      info = "Disable Chat Bubbles"
   )
   public static boolean EnableChatBubbles = true;
   private static int NewEntityStartId = 0;
   @ConfigProp(
      info = "Navigation search range for NPCs. Not recommended to increase if you have a slow pc or on a server"
   )
   public static int NpcNavRange = 32;
   @ConfigProp(
      info = "Set to true if you want the dialog command option to be able to use op commands like tp etc"
   )
   public static boolean NpcUseOpCommands = false;
   @ConfigProp
   public static boolean InventoryGuiEnabled = true;
   @ConfigProp
   public static boolean DisableExtraItems = false;
   @ConfigProp
   public static boolean DisableExtraBlock = false;
   @ConfigProp
   public static boolean SceneButtonsEnabled = true;
   public static long ticks;
   @SidedProxy(
      clientSide = "noppes.npcs.client.ClientProxy",
      serverSide = "noppes.npcs.CommonProxy"
   )
   public static CommonProxy proxy;
   @ConfigProp(
      info = "Enables CustomNpcs startup update message"
   )
   public static boolean EnableUpdateChecker = true;
   public static CustomNpcs instance;
   public static boolean FreezeNPCs = false;
   @ConfigProp(
      info = "Only ops can create and edit npcs"
   )
   public static boolean OpsOnly = false;
   @ConfigProp(
      info = "Default interact line. Leave empty to not have one"
   )
   public static String DefaultInteractLine = "Hello @p";
   @ConfigProp
   public static boolean DisableEnchants = false;
   @ConfigProp(
      info = "Start Id for enchants. IDs can only range from 0-256"
   )
   public static int EnchantStartId = 100;
   @ConfigProp(
      info = "Number of chunk loading npcs that can be active at the same time"
   )
   public static int ChuckLoaders = 20;
   public static File Dir;
   @ConfigProp(
      info = "Set to false if you want to disable guns"
   )
   public static boolean GunsEnabled = true;
   @ConfigProp(
      info = "Enables leaves decay"
   )
   public static boolean LeavesDecayEnabled = true;
   @ConfigProp(
      info = "Enables Vine Growth"
   )
   public static boolean VineGrowthEnabled = true;
   @ConfigProp(
      info = "Enables Ice Melting"
   )
   public static boolean IceMeltsEnabled = true;
   @ConfigProp(
      info = "Normal players can use soulstone on animals"
   )
   public static boolean SoulStoneAnimals = true;
   @ConfigProp(
      info = "Type 0 = Normal, Type 1 = Solid"
   )
   public static int HeadWearType = 1;
   @ConfigProp(
      info = "When set to Minecraft it will use minecrafts font, when Default it will use OpenSans. Can only use fonts installed on your PC"
   )
   public static String FontType = "Default";
   @ConfigProp(
      info = "Font size for custom fonts (doesn\'t work with minecrafts font)"
   )
   public static int FontSize = 18;
   public static FMLEventChannel Channel;
   public static FMLEventChannel ChannelPlayer;
   public static ConfigLoader Config;
   public static CommandNoppes NoppesCommand = new CommandNoppes();


   public CustomNpcs() {
      instance = this;
   }

   @EventHandler
   public void load(FMLPreInitializationEvent ev) {
      Channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("CustomNPCs");
      ChannelPlayer = NetworkRegistry.INSTANCE.newEventDrivenChannel("CustomNPCsPlayer");
      MinecraftServer server = MinecraftServer.getServer();
      String dir = "";
      if(server != null) {
         dir = (new File(".")).getAbsolutePath();
      } else {
         dir = Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
      }

      Dir = new File(dir, "customnpcs");
      Dir.mkdir();
      Config = new ConfigLoader(this.getClass(), new File(dir, "config"), "CustomNpcs");
      Config.loadConfig();
      if(NpcNavRange < 16) {
         NpcNavRange = 16;
      }

      EnchantInterface.load();
      CustomItems.load();
      NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
      MinecraftForge.EVENT_BUS.register(new ServerEventsHandler());
      MinecraftForge.EVENT_BUS.register(new ScriptController());
      FMLCommonHandler.instance().bus().register(new ServerTickHandler());
      PixelmonHelper.load();
      this.registerNpc(EntityNPCHumanMale.class, "npchumanmale");
      this.registerNpc(EntityNPCVillager.class, "npcvillager");
      this.registerNpc(EntityNpcPony.class, "npcpony");
      this.registerNpc(EntityNPCHumanFemale.class, "npchumanfemale");
      this.registerNpc(EntityNPCDwarfMale.class, "npcdwarfmale");
      this.registerNpc(EntityNPCFurryMale.class, "npcfurrymale");
      this.registerNpc(EntityNpcMonsterMale.class, "npczombiemale");
      this.registerNpc(EntityNpcMonsterFemale.class, "npczombiefemale");
      this.registerNpc(EntityNpcSkeleton.class, "npcskeleton");
      this.registerNpc(EntityNPCDwarfFemale.class, "npcdwarffemale");
      this.registerNpc(EntityNPCFurryFemale.class, "npcfurryfemale");
      this.registerNpc(EntityNPCOrcMale.class, "npcorcfmale");
      this.registerNpc(EntityNPCOrcFemale.class, "npcorcfemale");
      this.registerNpc(EntityNPCElfMale.class, "npcelfmale");
      this.registerNpc(EntityNPCElfFemale.class, "npcelffemale");
      this.registerNpc(EntityNpcCrystal.class, "npccrystal");
      this.registerNpc(EntityNpcEnderchibi.class, "npcenderchibi");
      this.registerNpc(EntityNpcNagaMale.class, "npcnagamale");
      this.registerNpc(EntityNpcNagaFemale.class, "npcnagafemale");
      this.registerNpc(EntityNpcSlime.class, "NpcSlime");
      this.registerNpc(EntityNpcDragon.class, "NpcDragon");
      this.registerNpc(EntityNPCEnderman.class, "npcEnderman");
      this.registerNpc(EntityNPCGolem.class, "npcGolem");
      this.registerNpc(EntityCustomNpc.class, "CustomNpc");
      this.registerNpc(EntityNPC64x32.class, "CustomNpc64x32");
      this.registerNewEntity(EntityChairMount.class, "CustomNpcChairMount", 64, 10, false);
      this.registerNewEntity(EntityProjectile.class, "throwableitem", 64, 3, true);
      ArrayList list = new ArrayList();
      BiomeGenBase[] var5 = BiomeGenBase.getBiomeGenArray();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         BiomeGenBase base = var5[var7];
         if(base != null) {
            list.add(base);
         }
      }

      ForgeChunkManager.setForcedChunkLoadingCallback(this, new ChunkController());
      proxy.load();
   }

   @EventHandler
   public void load(FMLInitializationEvent ev) {
      ForgeModContainer.fullBoundingBoxLadders = true;
      new RecipeController();
      proxy.postload();
   }

   @EventHandler
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
      Set names = Block.blockRegistry.getKeys();
      Iterator var3 = names.iterator();

      while(var3.hasNext()) {
         ResourceLocation name = (ResourceLocation)var3.next();
         Block block = (Block)Block.blockRegistry.getObject(name);
         if(block instanceof BlockLeavesBase) {
            block.setTickRandomly(LeavesDecayEnabled);
         }

         if(block instanceof BlockVine) {
            block.setTickRandomly(VineGrowthEnabled);
         }

         if(block instanceof BlockIce) {
            block.setTickRandomly(IceMeltsEnabled);
         }
      }

   }

   @EventHandler
   public void started(FMLServerStartedEvent event) {
      RecipeController.instance.load();
      new DialogController();
      new BankController();
      QuestController.instance.load();
      ScriptController.HasStart = true;
      ServerCloneController.Instance = new ServerCloneController();
   }

   @EventHandler
   public void stopped(FMLServerStoppedEvent event) {
      ServerCloneController.Instance = null;
   }

   @EventHandler
   public void serverstart(FMLServerStartingEvent event) {
      event.registerServerCommand(NoppesCommand);
   }

   private void registerNpc(Class cl, String name) {
      EntityList.stringToClassMapping.put(name, cl);
      EntityRegistry.registerModEntity(cl, name, NewEntityStartId++, this, 64, 3, true);
   }

   private void registerNewEntity(Class cl, String name, int range, int update, boolean velocity) {
      EntityRegistry.registerModEntity(cl, name, NewEntityStartId++, this, range, update, velocity);
   }

   public static File getWorldSaveDirectory() {
      try {
         MinecraftServer e = MinecraftServer.getServer();
         if(e == null) {
            return null;
         } else {
            File saves = new File(".");
            if(!e.isDedicatedServer()) {
               saves = new File(Minecraft.getMinecraft().mcDataDir, "saves");
            }

            File savedir = new File(new File(saves, e.getFolderName()), "customnpcs");
            if(!savedir.exists()) {
               savedir.mkdir();
            }

            return savedir;
         }
      } catch (Exception var3) {
         return null;
      }
   }

}

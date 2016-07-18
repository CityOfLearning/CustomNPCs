//

//

package noppes.npcs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import noppes.npcs.blocks.BlockBanner;
import noppes.npcs.blocks.BlockBarrel;
import noppes.npcs.blocks.BlockBeam;
import noppes.npcs.blocks.BlockBigSign;
import noppes.npcs.blocks.BlockBook;
import noppes.npcs.blocks.BlockBorder;
import noppes.npcs.blocks.BlockBuilder;
import noppes.npcs.blocks.BlockCampfire;
import noppes.npcs.blocks.BlockCandle;
import noppes.npcs.blocks.BlockCarpentryBench;
import noppes.npcs.blocks.BlockChair;
import noppes.npcs.blocks.BlockCopy;
import noppes.npcs.blocks.BlockCouchWood;
import noppes.npcs.blocks.BlockCouchWool;
import noppes.npcs.blocks.BlockCrate;
import noppes.npcs.blocks.BlockCrystal;
import noppes.npcs.blocks.BlockLamp;
import noppes.npcs.blocks.BlockMailbox;
import noppes.npcs.blocks.BlockNpcRedstone;
import noppes.npcs.blocks.BlockPedestal;
import noppes.npcs.blocks.BlockPlaceholder;
import noppes.npcs.blocks.BlockScripted;
import noppes.npcs.blocks.BlockScriptedDoor;
import noppes.npcs.blocks.BlockShelf;
import noppes.npcs.blocks.BlockSign;
import noppes.npcs.blocks.BlockStool;
import noppes.npcs.blocks.BlockTable;
import noppes.npcs.blocks.BlockTallLamp;
import noppes.npcs.blocks.BlockTombstone;
import noppes.npcs.blocks.BlockTrading;
import noppes.npcs.blocks.BlockWallBanner;
import noppes.npcs.blocks.BlockWaypoint;
import noppes.npcs.blocks.BlockWeaponRack;
import noppes.npcs.blocks.tiles.TileBanner;
import noppes.npcs.blocks.tiles.TileBarrel;
import noppes.npcs.blocks.tiles.TileBeam;
import noppes.npcs.blocks.tiles.TileBigSign;
import noppes.npcs.blocks.tiles.TileBlockAnvil;
import noppes.npcs.blocks.tiles.TileBook;
import noppes.npcs.blocks.tiles.TileBorder;
import noppes.npcs.blocks.tiles.TileBuilder;
import noppes.npcs.blocks.tiles.TileCampfire;
import noppes.npcs.blocks.tiles.TileCandle;
import noppes.npcs.blocks.tiles.TileChair;
import noppes.npcs.blocks.tiles.TileCopy;
import noppes.npcs.blocks.tiles.TileCouchWood;
import noppes.npcs.blocks.tiles.TileCouchWool;
import noppes.npcs.blocks.tiles.TileCrate;
import noppes.npcs.blocks.tiles.TileLamp;
import noppes.npcs.blocks.tiles.TileMailbox;
import noppes.npcs.blocks.tiles.TilePedestal;
import noppes.npcs.blocks.tiles.TileRedstoneBlock;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.blocks.tiles.TileScriptedDoor;
import noppes.npcs.blocks.tiles.TileShelf;
import noppes.npcs.blocks.tiles.TileSign;
import noppes.npcs.blocks.tiles.TileStool;
import noppes.npcs.blocks.tiles.TileTable;
import noppes.npcs.blocks.tiles.TileTallLamp;
import noppes.npcs.blocks.tiles.TileTombstone;
import noppes.npcs.blocks.tiles.TileTrading;
import noppes.npcs.blocks.tiles.TileWallBanner;
import noppes.npcs.blocks.tiles.TileWaypoint;
import noppes.npcs.blocks.tiles.TileWeaponRack;
import noppes.npcs.constants.EnumNpcToolMaterial;
import noppes.npcs.items.ItemBlockLight;
import noppes.npcs.items.ItemMounter;
import noppes.npcs.items.ItemMusic;
import noppes.npcs.items.ItemMusicBanjo;
import noppes.npcs.items.ItemMusicClarinet;
import noppes.npcs.items.ItemMusicOracina;
import noppes.npcs.items.ItemMusicViolin;
import noppes.npcs.items.ItemNpcBlock;
import noppes.npcs.items.ItemNpcCloner;
import noppes.npcs.items.ItemNpcColored;
import noppes.npcs.items.ItemNpcInterface;
import noppes.npcs.items.ItemNpcMovingPath;
import noppes.npcs.items.ItemNpcScripter;
import noppes.npcs.items.ItemNpcWand;
import noppes.npcs.items.ItemOrb;
import noppes.npcs.items.ItemPlaceholder;
import noppes.npcs.items.ItemScriptedDoor;
import noppes.npcs.items.ItemTeleporter;
import noppes.npcs.items.ItemWand;

public class CustomItems {
	public static Item wand;
	public static Item cloner;
	public static Item scripter;
	public static Item moving;
	public static Item mount;
	public static Item teleporter;
	public static Item scriptedDoorTool;
	public static Item banjo;
	public static Item violin;
	public static Item violinbow;
	public static Item harp;
	public static Item guitar;
	public static Item frenchHorn;
	public static Item clarinet;
	public static Item ocarina;
	public static Item coinWood;
	public static Item coinStone;
	public static Item coinIron;
	public static Item coinGold;
	public static Item coinDiamond;
	public static Item coinBronze;
	public static Item coinEmerald;
	public static Item moneyBag;
	public static Item orb;
	public static Item mana;
	public static Item bronze_ingot;
	public static Item demonic_ingot;
	public static Item mithril_ingot;
	public static Item letter;
	public static Item bag;
	public static Item satchel;
	public static Block redstoneBlock;
	public static Block carpentyBench;
	public static Block mailbox;
	public static Block waypoint;
	public static Block border;
	public static Block scripted;
	public static Block scriptedDoor;
	public static Block builder;
	public static Block copy;
	public static Block trading;
	public static Block banner;
	public static Block wallBanner;
	public static Block tallLamp;
	public static Block blood;
	public static Block book;
	public static Block chair;
	public static Block crate;
	public static Block weaponsRack;
	public static Block pedestal;
	public static Block couchWool;
	public static Block couchWood;
	public static Block table;
	public static Block stool;
	public static Block bigsign;
	public static Block barrel;
	public static Block tombstone;
	public static Block shelf;
	public static Block sign;
	public static Block beam;
	public static Block crystal;
	public static Block placeholder;
	public static Block lamp;
	public static Block campfire;
	public static Block candle;
	public static Block lamp_unlit;
	public static Block campfire_unlit;
	public static Block candle_unlit;
	public static CreativeTabNpcs tab;
	public static CreativeTabNpcs tabBlocks;

	static {
		CustomItems.tab = new CreativeTabNpcs("cnpcs");
		CustomItems.tabBlocks = new CreativeTabNpcs("cnpcsb");
	}

	public static void load() {
		GameRegistry.registerTileEntity(TileRedstoneBlock.class, "TileRedstoneBlock");
		GameRegistry.registerTileEntity(TileBlockAnvil.class, "TileBlockAnvil");
		GameRegistry.registerTileEntity(TileMailbox.class, "TileMailbox");
		GameRegistry.registerTileEntity(TileWaypoint.class, "TileWaypoint");
		GameRegistry.registerTileEntity(TileBanner.class, "TileNPCBanner");
		GameRegistry.registerTileEntity(TileScripted.class, "TileNPCScripted");
		GameRegistry.registerTileEntity(TileScriptedDoor.class, "TileNPCScriptedDoor");
		GameRegistry.registerTileEntity(TileBuilder.class, "TileNPCBuilder");
		GameRegistry.registerTileEntity(TileCopy.class, "TileNPCCopy");
		GameRegistry.registerTileEntity(TileTrading.class, "TileNPCTrading");
		if (!CustomNpcs.DisableExtraBlock) {
			GameRegistry.registerTileEntity(TileWallBanner.class, "TileNPCWallBanner");
			GameRegistry.registerTileEntity(TileTallLamp.class, "TileNPCTallLamp");
			GameRegistry.registerTileEntity(TileChair.class, "TileNPCChair");
			GameRegistry.registerTileEntity(TileCrate.class, "TileNPCCrate");
			GameRegistry.registerTileEntity(TileWeaponRack.class, "TileNPCWeaponRack");
			GameRegistry.registerTileEntity(TileCouchWool.class, "TileNPCCouchWool");
			GameRegistry.registerTileEntity(TileCouchWood.class, "TileNPCCouchWood");
			GameRegistry.registerTileEntity(TileTable.class, "TileNPCTable");
			GameRegistry.registerTileEntity(TileLamp.class, "TileNPCLamp");
			GameRegistry.registerTileEntity(TileCandle.class, "TileNPCCandle");
			GameRegistry.registerTileEntity(TileBorder.class, "TileNPCBorder");
			GameRegistry.registerTileEntity(TileStool.class, "TileNPCStool");
			GameRegistry.registerTileEntity(TileBigSign.class, "TileNPCBigSign");
			GameRegistry.registerTileEntity(TileBarrel.class, "TileNPCBarrel");
			GameRegistry.registerTileEntity(TileCampfire.class, "TileNPCCampfire");
			GameRegistry.registerTileEntity(TileTombstone.class, "TileNPCTombstone");
			GameRegistry.registerTileEntity(TileShelf.class, "TileNPCShelf");
			GameRegistry.registerTileEntity(TileSign.class, "TileNPCSign");
			GameRegistry.registerTileEntity(TileBeam.class, "TileNPCBeam");
			GameRegistry.registerTileEntity(TileBook.class, "TileNPCBook");
			GameRegistry.registerTileEntity(TilePedestal.class, "TileNPCPedestal");
		}
		CustomItems.wand = new ItemNpcWand().setUnlocalizedName("npcWand").setFull3D();
		CustomItems.cloner = new ItemNpcCloner().setUnlocalizedName("npcMobCloner").setFull3D();
		CustomItems.scripter = new ItemNpcScripter().setUnlocalizedName("npcScripter").setFull3D();
		CustomItems.moving = new ItemNpcMovingPath().setUnlocalizedName("npcMovingPath").setFull3D();
		CustomItems.mount = new ItemMounter().setUnlocalizedName("npcMounter").setFull3D();
		CustomItems.teleporter = new ItemTeleporter().setUnlocalizedName("npcTeleporter").setFull3D();
		CustomItems.redstoneBlock = new BlockNpcRedstone().setHardness(50.0f).setResistance(2000.0f)
				.setUnlocalizedName("npcRedstoneBlock").setCreativeTab(CustomItems.tab);
		CustomItems.carpentyBench = new BlockCarpentryBench().setUnlocalizedName("npcCarpentyBench").setHardness(5.0f)
				.setResistance(10.0f).setStepSound(Block.soundTypeWood).setCreativeTab(CustomItems.tabBlocks);
		CustomItems.mailbox = new BlockMailbox().setUnlocalizedName("npcMailbox").setHardness(5.0f).setResistance(10.0f)
				.setStepSound(Block.soundTypeMetal).setCreativeTab(CustomItems.tabBlocks);
		CustomItems.waypoint = new BlockWaypoint().setUnlocalizedName("npcLocationBlock").setHardness(5.0f)
				.setResistance(10.0f).setStepSound(Block.soundTypeMetal).setCreativeTab(CustomItems.tab);
		CustomItems.border = new BlockBorder().setUnlocalizedName("npcBorder").setHardness(5.0f).setResistance(10.0f)
				.setStepSound(Block.soundTypeWood).setCreativeTab(CustomItems.tab);
		CustomItems.scripted = new BlockScripted().setUnlocalizedName("npcScripted").setHardness(5.0f)
				.setResistance(10.0f).setStepSound(Block.soundTypeStone).setCreativeTab(CustomItems.tab);
		CustomItems.scriptedDoor = new BlockScriptedDoor().setUnlocalizedName("npcScriptedDoor").setHardness(5.0f)
				.setResistance(10.0f);
		CustomItems.scriptedDoorTool = new ItemScriptedDoor(CustomItems.scriptedDoor)
				.setUnlocalizedName("npcScriptedDoorTool").setFull3D();
		CustomItems.builder = new BlockBuilder().setUnlocalizedName("npcBuilderBlock").setHardness(5.0f)
				.setResistance(10.0f).setStepSound(Block.soundTypeStone).setCreativeTab(CustomItems.tab);
		CustomItems.copy = new BlockCopy().setUnlocalizedName("npcCopyBlock").setHardness(5.0f).setResistance(10.0f)
				.setStepSound(Block.soundTypeStone).setCreativeTab(CustomItems.tab);
		CustomItems.trading = new BlockTrading().setUnlocalizedName("npcTradingBlock").setHardness(5.0f)
				.setResistance(10.0f).setStepSound(Block.soundTypeStone).setCreativeTab(CustomItems.tab);
		CustomNpcs.proxy.registerBlock(CustomItems.redstoneBlock, "npcRedstoneBlock", 0, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(CustomItems.carpentyBench, "npcCarpentyBench", 1, ItemNpcBlock.class);
		CustomNpcs.proxy.registerBlock(CustomItems.mailbox, "npcMailbox", 2, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(CustomItems.waypoint, "npcWaypoint", 0, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(CustomItems.border, "npcBorder", 0, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(CustomItems.scripted, "npcScripted", 0, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(CustomItems.scriptedDoor, "npcScriptedDoor", 0, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(CustomItems.builder, "npcBuilderBlock", 0, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(CustomItems.copy, "npcCopyBlock", 0, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(CustomItems.trading, "npcTradingBlock", 0, ItemBlock.class);
		((ItemNpcBlock) Item.getItemFromBlock(CustomItems.carpentyBench)).names = new String[] {
				"tile.npcCarpentyBench", "tile.anvil" };
		if (!CustomNpcs.DisableExtraBlock) {
			CustomItems.banner = new BlockBanner().setUnlocalizedName("npcBanner").setHardness(5.0f)
					.setResistance(10.0f).setStepSound(Block.soundTypeMetal).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.wallBanner = new BlockWallBanner().setUnlocalizedName("npcWallBanner").setHardness(5.0f)
					.setResistance(10.0f).setStepSound(Block.soundTypeMetal).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.tallLamp = new BlockTallLamp().setUnlocalizedName("npcTallLamp").setHardness(5.0f)
					.setResistance(10.0f).setStepSound(Block.soundTypeMetal).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.chair = new BlockChair().setUnlocalizedName("npcChair").setHardness(5.0f).setResistance(10.0f)
					.setStepSound(Block.soundTypeWood).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.crate = new BlockCrate().setUnlocalizedName("npcCrate").setHardness(5.0f).setResistance(10.0f)
					.setStepSound(Block.soundTypeWood).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.weaponsRack = new BlockWeaponRack().setUnlocalizedName("npcWeaponRack").setHardness(5.0f)
					.setResistance(10.0f).setStepSound(Block.soundTypeWood).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.couchWool = new BlockCouchWool().setUnlocalizedName("npcCouchWool").setHardness(5.0f)
					.setResistance(10.0f).setStepSound(Block.soundTypeWood).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.table = new BlockTable().setUnlocalizedName("npcTable").setHardness(5.0f).setResistance(10.0f)
					.setStepSound(Block.soundTypeWood).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.couchWood = new BlockCouchWood().setUnlocalizedName("npcCouchWood").setHardness(5.0f)
					.setResistance(10.0f).setStepSound(Block.soundTypeWood).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.lamp = new BlockLamp(true).setUnlocalizedName("npcLamp").setHardness(5.0f).setResistance(10.0f)
					.setStepSound(Block.soundTypeWood).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.candle = new BlockCandle(true).setUnlocalizedName("npcCandle").setHardness(5.0f)
					.setResistance(10.0f).setStepSound(Block.soundTypeWood).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.stool = new BlockStool().setUnlocalizedName("npcStool").setHardness(5.0f).setResistance(10.0f)
					.setStepSound(Block.soundTypeWood).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.placeholder = new BlockPlaceholder().setUnlocalizedName("npcPlaceholder").setHardness(5.0f)
					.setResistance(10.0f).setStepSound(Block.soundTypeWood).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.bigsign = new BlockBigSign().setUnlocalizedName("npcBigSign").setHardness(5.0f)
					.setResistance(10.0f).setStepSound(Block.soundTypeWood).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.barrel = new BlockBarrel().setUnlocalizedName("npcBarrel").setHardness(5.0f)
					.setResistance(10.0f).setStepSound(Block.soundTypeWood).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.campfire = new BlockCampfire(true).setUnlocalizedName("npcCampfire").setHardness(5.0f)
					.setResistance(10.0f).setStepSound(Block.soundTypeStone).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.tombstone = new BlockTombstone().setUnlocalizedName("npcTombstone").setHardness(5.0f)
					.setResistance(10.0f).setStepSound(Block.soundTypeStone).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.shelf = new BlockShelf().setUnlocalizedName("npcShelf").setHardness(5.0f).setResistance(10.0f)
					.setStepSound(Block.soundTypeWood).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.sign = new BlockSign().setUnlocalizedName("npcSign").setHardness(5.0f).setResistance(10.0f)
					.setStepSound(Block.soundTypeWood).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.beam = new BlockBeam().setUnlocalizedName("npcBeam").setHardness(5.0f).setResistance(10.0f)
					.setStepSound(Block.soundTypeWood).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.book = new BlockBook().setHardness(5.0f).setResistance(10.0f).setStepSound(Block.soundTypeWood)
					.setCreativeTab(CustomItems.tabBlocks);
			CustomItems.pedestal = new BlockPedestal().setUnlocalizedName("npcPedestal").setHardness(5.0f)
					.setResistance(10.0f).setStepSound(Block.soundTypeWood).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.crystal = new BlockCrystal().setUnlocalizedName("npcCrystal").setHardness(5.0f)
					.setResistance(10.0f).setStepSound(Block.soundTypeGlass).setCreativeTab(CustomItems.tabBlocks);
			CustomItems.campfire_unlit = new BlockCampfire(false).setUnlocalizedName("npcCampfire").setHardness(5.0f)
					.setResistance(10.0f).setStepSound(Block.soundTypeStone);
			CustomItems.lamp_unlit = new BlockLamp(false).setUnlocalizedName("npcLamp").setHardness(5.0f)
					.setResistance(10.0f).setStepSound(Block.soundTypeWood);
			CustomItems.candle_unlit = new BlockCandle(false).setUnlocalizedName("npcCandle").setHardness(5.0f)
					.setResistance(10.0f).setStepSound(Block.soundTypeWood);
			CustomNpcs.proxy.registerBlock(CustomItems.banner, "npcBanner", 4, ItemBlock.class);
			CustomNpcs.proxy.registerBlock(CustomItems.wallBanner, "npcWallBanner", 4, ItemBlock.class);
			CustomNpcs.proxy.registerBlock(CustomItems.tallLamp, "npcTallLamp", 4, ItemBlock.class);
			CustomNpcs.proxy.registerBlock(CustomItems.chair, "npcChair", 5, ItemBlock.class);
			CustomNpcs.proxy.registerBlock(CustomItems.crate, "npcCrate", 5, ItemBlock.class);
			CustomNpcs.proxy.registerBlock(CustomItems.weaponsRack, "npcWeaponRack", 5, ItemBlock.class);
			CustomNpcs.proxy.registerBlock(CustomItems.couchWool, "npcCouchWool", 5, ItemBlock.class);
			CustomNpcs.proxy.registerBlock(CustomItems.couchWood, "npcCouchWood", 5, ItemBlock.class);
			CustomNpcs.proxy.registerBlock(CustomItems.table, "npcTable", 5, ItemBlock.class);
			CustomNpcs.proxy.registerBlock(CustomItems.stool, "npcStool", 5, ItemBlock.class);
			CustomNpcs.proxy.registerBlock(CustomItems.placeholder, "npcPlaceholder", 15, ItemPlaceholder.class, true);
			CustomNpcs.proxy.registerBlock(CustomItems.bigsign, "npcBigSign", 0, ItemBlock.class);
			CustomNpcs.proxy.registerBlock(CustomItems.barrel, "npcBarrel", 5, ItemBlock.class);
			CustomNpcs.proxy.registerBlock(CustomItems.tombstone, "npcTombstone", 2, ItemBlock.class);
			CustomNpcs.proxy.registerBlock(CustomItems.shelf, "npcShelf", 5, ItemBlock.class);
			CustomNpcs.proxy.registerBlock(CustomItems.sign, "npcSign", 5, ItemBlock.class);
			CustomNpcs.proxy.registerBlock(CustomItems.beam, "npcBeam", 5, ItemBlock.class);
			CustomNpcs.proxy.registerBlock(CustomItems.book, "npcBook", 0, ItemBlock.class);
			CustomNpcs.proxy.registerBlock(CustomItems.pedestal, "npcPedestal", 4, ItemBlock.class);
			CustomNpcs.proxy.registerBlock(CustomItems.crystal, "npcCrystalBlock", 15, ItemNpcColored.class);
			CustomNpcs.proxy.registerBlock(CustomItems.campfire, "npcCampfire", 0, ItemBlock.class);
			CustomNpcs.proxy.registerBlock(CustomItems.candle, "npcCandle", 0, ItemBlockLight.class);
			CustomNpcs.proxy.registerBlock(CustomItems.lamp, "npcLamp", 0, ItemBlockLight.class);
			CustomNpcs.proxy.registerBlock(CustomItems.campfire_unlit, "npcCampfireUnlit", 0, ItemBlock.class);
			CustomNpcs.proxy.registerBlock(CustomItems.candle_unlit, "npcCandleUnlit", 0, ItemBlock.class);
			CustomNpcs.proxy.registerBlock(CustomItems.lamp_unlit, "npcLampUnlit", 0, ItemBlock.class);
		}
		CustomItems.banjo = new ItemMusicBanjo().setUnlocalizedName("npcBanjo").setFull3D().setMaxStackSize(1);
		CustomItems.violin = new ItemMusicViolin().setUnlocalizedName("npcViolin").setFull3D().setMaxStackSize(1);
		CustomItems.violinbow = new ItemNpcInterface().setUnlocalizedName("npcViolinBow").setFull3D().setMaxStackSize(1)
				.setCreativeTab(CustomItems.tabBlocks);
		CustomItems.harp = new ItemMusicViolin().setUnlocalizedName("npcHarp").setFull3D().setMaxStackSize(1);
		CustomItems.guitar = new ItemMusicBanjo().setUnlocalizedName("npcGuitar").setFull3D().setMaxStackSize(1);
		CustomItems.frenchHorn = new ItemMusic().setRotated().setUnlocalizedName("npcFrenchHorn").setFull3D()
				.setMaxStackSize(1);
		CustomItems.ocarina = new ItemMusicOracina().setUnlocalizedName("npcOcarina").setFull3D().setMaxStackSize(1);
		CustomItems.clarinet = new ItemMusicClarinet().setUnlocalizedName("npcClarinet").setFull3D().setMaxStackSize(1);
		if (!CustomNpcs.DisableExtraItems) {
//			CustomItems.tabArmor = new CreativeTabNpcs("cnpcsa");
//			CustomItems.tabWeapon = new CreativeTabNpcs("cnpcsw");
			
			final Item.ToolMaterial bronze = EnumHelper.addToolMaterial("BRONZE", 2, 170, 5.0f, 2.0f, 15);
			final Item.ToolMaterial emerald = EnumHelper.addToolMaterial("REALEMERALD", 3, 1000, 8.0f, 4.0f, 10);
			final Item.ToolMaterial demonic = EnumHelper.addToolMaterial("DEMONIC", 3, 100, 8.0f, 6.0f, 22);
			final Item.ToolMaterial frost = EnumHelper.addToolMaterial("FROST", 2, 59, 6.0f, 3.0f, 5);
			final Item.ToolMaterial mithril = EnumHelper.addToolMaterial("MITHRIL", 3, 3000, 8.0f, 3.0f, 10);
			CustomItems.orb = new ItemOrb(26937).setUnlocalizedName("npcOrb").setCreativeTab(CustomItems.tabBlocks);
			new ItemOrb(26939).setUnlocalizedName("npcBrokenOrb").setCreativeTab(CustomItems.tabBlocks);
			new ItemWand(26801).setUnlocalizedName("npcMagicWand").setFull3D().setMaxStackSize(1);
			new ItemNpcInterface(26802).setUnlocalizedName("npcChickenSword").setFull3D().setMaxStackSize(1)
					.setCreativeTab(CustomItems.tabBlocks);
			new ItemNpcInterface(26803).setUnlocalizedName("npcHandCuffs").setFull3D().setMaxStackSize(1)
					.setCreativeTab(CustomItems.tabBlocks);
			new ItemFlintAndSteel().setUnlocalizedName("npcLighter").setMaxStackSize(1)
					.setCreativeTab(CustomItems.tabBlocks);
			final ItemArmor.ArmorMaterial armorMithril = EnumHelper.addArmorMaterial("MITHRIL", "", 40,
					new int[] { 3, 8, 6, 3 }, 20);
			final ItemArmor.ArmorMaterial armorBronze = EnumHelper.addArmorMaterial("BRONZE", "", 7,
					new int[] { 2, 6, 5, 2 }, 20);
			final ItemArmor.ArmorMaterial armorEmerald = EnumHelper.addArmorMaterial("EMERALD", "", 35,
					new int[] { 5, 7, 4, 5 }, 5);
			CustomItems.coinWood = new ItemNpcInterface(26717).setUnlocalizedName("npcCoinWooden")
					.setCreativeTab(CustomItems.tabBlocks);
			CustomItems.coinStone = new ItemNpcInterface(26718).setUnlocalizedName("npcCoinStone")
					.setCreativeTab(CustomItems.tabBlocks);
			CustomItems.coinBronze = new ItemNpcInterface(26719).setUnlocalizedName("npcCoinBronze")
					.setCreativeTab(CustomItems.tabBlocks);
			CustomItems.coinIron = new ItemNpcInterface(26720).setUnlocalizedName("npcCoinIron")
					.setCreativeTab(CustomItems.tabBlocks);
			CustomItems.coinGold = new ItemNpcInterface(26721).setUnlocalizedName("npcCoinGold")
					.setCreativeTab(CustomItems.tabBlocks);
			CustomItems.coinDiamond = new ItemNpcInterface(26722).setUnlocalizedName("npcCoinDiamond")
					.setCreativeTab(CustomItems.tabBlocks);
			CustomItems.coinEmerald = new ItemNpcInterface(26723).setUnlocalizedName("npcCoinEmerald")
					.setCreativeTab(CustomItems.tabBlocks);
			new ItemNpcInterface().setUnlocalizedName("npcAncientCoin").setCreativeTab(CustomItems.tabBlocks);
			CustomItems.letter = new ItemNpcInterface(26950).setUnlocalizedName("npcLetter")
					.setCreativeTab(CustomItems.tabBlocks);
			new ItemNpcInterface(26951).setUnlocalizedName("npcPlans").setCreativeTab(CustomItems.tabBlocks);
			CustomItems.satchel = new ItemNpcInterface(26952).setUnlocalizedName("npcSatchel")
					.setCreativeTab(CustomItems.tabBlocks);
			CustomItems.bag = new ItemNpcInterface(26953).setUnlocalizedName("npcBag")
					.setCreativeTab(CustomItems.tabBlocks);
			new ItemNpcInterface(26954).setUnlocalizedName("npcCrystal").setCreativeTab(CustomItems.tabBlocks);
			
			new ItemNpcInterface(26964).setUnlocalizedName("npcKey").setCreativeTab(CustomItems.tabBlocks);
			new ItemNpcInterface(26965).setUnlocalizedName("npcKey2").setCreativeTab(CustomItems.tabBlocks);
			final Item sapphire = new ItemNpcInterface(26970).setUnlocalizedName("npcSaphire")
					.setCreativeTab(CustomItems.tabBlocks);
			final Item ruby = new ItemNpcInterface(26971).setUnlocalizedName("npcRuby")
					.setCreativeTab(CustomItems.tabBlocks);
			final Item amethyst = new ItemNpcInterface(26972).setUnlocalizedName("npcAmethyst")
					.setCreativeTab(CustomItems.tabBlocks);
			OreDictionary.registerOre("gemSaphire", sapphire);
			OreDictionary.registerOre("gemRuby", ruby);
			OreDictionary.registerOre("gemAmethyst", amethyst);
			CustomItems.bronze_ingot = new ItemNpcInterface(26973).setUnlocalizedName("npcBronzeIngot")
					.setCreativeTab(CustomItems.tabBlocks);
			CustomItems.demonic_ingot = new ItemNpcInterface(26973).setUnlocalizedName("npcDemonicIngot")
					.setCreativeTab(CustomItems.tabBlocks);
			CustomItems.mithril_ingot = new ItemNpcInterface(26973).setUnlocalizedName("npcMithrilIngot")
					.setCreativeTab(CustomItems.tabBlocks);
			armorMithril.customCraftingMaterial = CustomItems.mithril_ingot;
			bronze.customCraftingMaterial = CustomItems.bronze_ingot;
			emerald.customCraftingMaterial = Items.emerald;
			demonic.customCraftingMaterial = CustomItems.demonic_ingot;
			frost.customCraftingMaterial = Item.getItemFromBlock(Blocks.ice);
			mithril.customCraftingMaterial = CustomItems.mithril_ingot;
			OreDictionary.registerOre("ingotBronze", CustomItems.bronze_ingot);
			OreDictionary.registerOre("ingotDemonic", CustomItems.demonic_ingot);
			OreDictionary.registerOre("ingotMithril", CustomItems.mithril_ingot);
			CustomItems.tabBlocks.item = Item.getItemFromBlock(CustomItems.couchWool);
			CustomItems.tabBlocks.meta = 1;
		}
		CustomItems.tab.item = CustomItems.wand;
	}
}

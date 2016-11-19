
package noppes.npcs;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
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
import noppes.npcs.blocks.BlockLamp;
import noppes.npcs.blocks.BlockMailbox;
import noppes.npcs.blocks.BlockNpcRedstone;
import noppes.npcs.blocks.BlockPedestal;
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
import noppes.npcs.items.ItemBlockLight;
import noppes.npcs.items.ItemMounter;
import noppes.npcs.items.ItemMusic;
import noppes.npcs.items.ItemMusicBanjo;
import noppes.npcs.items.ItemMusicClarinet;
import noppes.npcs.items.ItemMusicOracina;
import noppes.npcs.items.ItemMusicViolin;
import noppes.npcs.items.ItemNpcBlock;
import noppes.npcs.items.ItemNpcCloner;
import noppes.npcs.items.ItemNpcInterface;
import noppes.npcs.items.ItemNpcMovingPath;
import noppes.npcs.items.ItemNpcScripter;
import noppes.npcs.items.ItemNpcWand;
import noppes.npcs.items.ItemScriptedDoor;
import noppes.npcs.items.ItemTeleporter;

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
	public static Block lamp;
	public static Block campfire;
	public static Block candle;
	public static Block lamp_unlit;
	public static Block campfire_unlit;
	public static Block candle_unlit;

	public static CreativeTabNpcs tabBlocks = new CreativeTabNpcs("cnpcsb");
	public static CreativeTabNpcs tab = new CreativeTabNpcs("cnpcs");

	public static void load() {
		// core items
		GameRegistry.registerTileEntity(TileRedstoneBlock.class, "TileRedstoneBlock");
		GameRegistry.registerTileEntity(TileBlockAnvil.class, "TileBlockAnvil");
		GameRegistry.registerTileEntity(TileMailbox.class, "TileMailbox");
		GameRegistry.registerTileEntity(TileWaypoint.class, "TileWaypoint");
		GameRegistry.registerTileEntity(TileScripted.class, "TileNPCScripted");
		GameRegistry.registerTileEntity(TileScriptedDoor.class, "TileNPCScriptedDoor");
		GameRegistry.registerTileEntity(TileBuilder.class, "TileNPCBuilder");
		GameRegistry.registerTileEntity(TileCopy.class, "TileNPCCopy");
		GameRegistry.registerTileEntity(TileTrading.class, "TileNPCTrading");
		GameRegistry.registerTileEntity(TileBorder.class, "TileNPCBorder");
		CustomItems.wand = new ItemNpcWand().setUnlocalizedName("npcWand").setFull3D();
		CustomItems.cloner = new ItemNpcCloner().setUnlocalizedName("npcMobCloner").setFull3D();
		CustomItems.scripter = new ItemNpcScripter().setUnlocalizedName("npcScripter").setFull3D();
		CustomItems.moving = new ItemNpcMovingPath().setUnlocalizedName("npcMovingPath").setFull3D();
		CustomItems.mount = new ItemMounter().setUnlocalizedName("npcMounter").setFull3D();
		CustomItems.teleporter = new ItemTeleporter().setUnlocalizedName("npcTeleporter").setFull3D();
		CustomItems.redstoneBlock = new BlockNpcRedstone().setHardness(50.0f).setResistance(2000.0f)
				.setUnlocalizedName("npcRedstoneBlock").setCreativeTab(CustomItems.tab);
		CustomItems.carpentyBench = new BlockCarpentryBench().setUnlocalizedName("npcCarpentyBench").setHardness(5.0f)
				.setResistance(10.0f).setStepSound(Block.soundTypeWood).setCreativeTab(CustomItems.tab);
		CustomItems.mailbox = new BlockMailbox().setUnlocalizedName("npcMailbox").setHardness(5.0f).setResistance(10.0f)
				.setStepSound(Block.soundTypeMetal).setCreativeTab(CustomItems.tab);
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

		CustomItems.banjo = new ItemMusicBanjo().setUnlocalizedName("npcBanjo").setFull3D().setMaxStackSize(1);
		CustomItems.violin = new ItemMusicViolin().setUnlocalizedName("npcViolin").setFull3D().setMaxStackSize(1);
		CustomItems.violinbow = new ItemNpcInterface().setUnlocalizedName("npcViolinBow").setFull3D().setMaxStackSize(1)
				.setCreativeTab(CustomItems.tab);
		CustomItems.harp = new ItemMusicViolin().setUnlocalizedName("npcHarp").setFull3D().setMaxStackSize(1);
		CustomItems.guitar = new ItemMusicBanjo().setUnlocalizedName("npcGuitar").setFull3D().setMaxStackSize(1);
		CustomItems.frenchHorn = new ItemMusic().setRotated().setUnlocalizedName("npcFrenchHorn").setFull3D()
				.setMaxStackSize(1);
		CustomItems.ocarina = new ItemMusicOracina().setUnlocalizedName("npcOcarina").setFull3D().setMaxStackSize(1);
		CustomItems.clarinet = new ItemMusicClarinet().setUnlocalizedName("npcClarinet").setFull3D().setMaxStackSize(1);

		CustomItems.letter = new ItemNpcInterface(26950).setUnlocalizedName("npcLetter")
				.setCreativeTab(CustomItems.tab);
		CustomItems.satchel = new ItemNpcInterface(26952).setUnlocalizedName("npcSatchel")
				.setCreativeTab(CustomItems.tab);
		CustomItems.bag = new ItemNpcInterface(26953).setUnlocalizedName("npcBag").setCreativeTab(CustomItems.tab);

		CustomItems.tab.item = CustomItems.wand;

		// furniture
		GameRegistry.registerTileEntity(TileWallBanner.class, "TileNPCWallBanner");
		GameRegistry.registerTileEntity(TileBanner.class, "TileNPCBanner");
		GameRegistry.registerTileEntity(TileTallLamp.class, "TileNPCTallLamp");
		GameRegistry.registerTileEntity(TileChair.class, "TileNPCChair");
		GameRegistry.registerTileEntity(TileCrate.class, "TileNPCCrate");
		GameRegistry.registerTileEntity(TileWeaponRack.class, "TileNPCWeaponRack");
		GameRegistry.registerTileEntity(TileCouchWool.class, "TileNPCCouchWool");
		GameRegistry.registerTileEntity(TileCouchWood.class, "TileNPCCouchWood");
		GameRegistry.registerTileEntity(TileTable.class, "TileNPCTable");
		GameRegistry.registerTileEntity(TileLamp.class, "TileNPCLamp");
		GameRegistry.registerTileEntity(TileCandle.class, "TileNPCCandle");
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

		banner = new BlockBanner().setUnlocalizedName("npcBanner").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeMetal).setCreativeTab(tabBlocks);
		wallBanner = new BlockWallBanner().setUnlocalizedName("npcWallBanner").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeMetal).setCreativeTab(tabBlocks);
		tallLamp = new BlockTallLamp().setUnlocalizedName("npcTallLamp").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeMetal).setCreativeTab(tabBlocks);
		chair = new BlockChair().setUnlocalizedName("npcChair").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeWood).setCreativeTab(tabBlocks);
		crate = new BlockCrate().setUnlocalizedName("npcCrate").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeWood).setCreativeTab(tabBlocks);
		weaponsRack = new BlockWeaponRack().setUnlocalizedName("npcWeaponRack").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeWood).setCreativeTab(tabBlocks);
		couchWool = new BlockCouchWool().setUnlocalizedName("npcCouchWool").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeWood).setCreativeTab(tabBlocks);
		table = new BlockTable().setUnlocalizedName("npcTable").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeWood).setCreativeTab(tabBlocks);
		couchWood = new BlockCouchWood().setUnlocalizedName("npcCouchWood").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeWood).setCreativeTab(tabBlocks);
		lamp = new BlockLamp(true).setUnlocalizedName("npcLamp").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeWood).setCreativeTab(tabBlocks);
		candle = new BlockCandle(true).setUnlocalizedName("npcCandle").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeWood).setCreativeTab(tabBlocks);
		stool = new BlockStool().setUnlocalizedName("npcStool").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeWood).setCreativeTab(tabBlocks);
		bigsign = new BlockBigSign().setUnlocalizedName("npcBigSign").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeWood).setCreativeTab(tabBlocks);
		barrel = new BlockBarrel().setUnlocalizedName("npcBarrel").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeWood).setCreativeTab(tabBlocks);
		campfire = new BlockCampfire(true).setUnlocalizedName("npcCampfire").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeStone).setCreativeTab(tabBlocks);
		tombstone = new BlockTombstone().setUnlocalizedName("npcTombstone").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeStone).setCreativeTab(tabBlocks);
		shelf = new BlockShelf().setUnlocalizedName("npcShelf").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeWood).setCreativeTab(tabBlocks);
		sign = new BlockSign().setUnlocalizedName("npcSign").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeWood).setCreativeTab(tabBlocks);
		beam = new BlockBeam().setUnlocalizedName("npcBeam").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeWood).setCreativeTab(tabBlocks);
		book = new BlockBook().setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundTypeWood)
				.setCreativeTab(tabBlocks);
		pedestal = new BlockPedestal().setUnlocalizedName("npcPedestal").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeWood).setCreativeTab(tabBlocks);

		campfire_unlit = new BlockCampfire(false).setUnlocalizedName("npcCampfire").setHardness(5.0F)
				.setResistance(10.0F).setStepSound(Block.soundTypeStone);
		lamp_unlit = new BlockLamp(false).setUnlocalizedName("npcLamp").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeWood);
		candle_unlit = new BlockCandle(false).setUnlocalizedName("npcCandle").setHardness(5.0F).setResistance(10.0F)
				.setStepSound(Block.soundTypeWood);

		CustomNpcs.proxy.registerBlock(banner, "npcBanner", 4, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(wallBanner, "npcWallBanner", 4, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(tallLamp, "npcTallLamp", 4, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(chair, "npcChair", 5, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(crate, "npcCrate", 5, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(weaponsRack, "npcWeaponRack", 5, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(couchWool, "npcCouchWool", 5, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(couchWood, "npcCouchWood", 5, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(table, "npcTable", 5, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(stool, "npcStool", 5, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(bigsign, "npcBigSign", 0, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(barrel, "npcBarrel", 5, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(tombstone, "npcTombstone", 2, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(shelf, "npcShelf", 5, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(sign, "npcSign", 5, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(beam, "npcBeam", 5, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(book, "npcBook", 0, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(pedestal, "npcPedestal", 4, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(campfire, "npcCampfire", 0, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(candle, "npcCandle", 0, ItemBlockLight.class);
		CustomNpcs.proxy.registerBlock(lamp, "npcLamp", 0, ItemBlockLight.class);
		CustomNpcs.proxy.registerBlock(campfire_unlit, "npcCampfireUnlit", 0, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(candle_unlit, "npcCandleUnlit", 0, ItemBlock.class);
		CustomNpcs.proxy.registerBlock(lamp_unlit, "npcLampUnlit", 0, ItemBlock.class);

		tabBlocks.item = Item.getItemFromBlock(couchWool);
		tabBlocks.meta = 1;
	}
}

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
import noppes.npcs.blocks.BlockBorder;
import noppes.npcs.blocks.BlockBuilder;
import noppes.npcs.blocks.BlockCarpentryBench;
import noppes.npcs.blocks.BlockCopy;
import noppes.npcs.blocks.BlockMailbox;
import noppes.npcs.blocks.BlockNpcRedstone;
import noppes.npcs.blocks.BlockScripted;
import noppes.npcs.blocks.BlockScriptedDoor;
import noppes.npcs.blocks.BlockTrading;
import noppes.npcs.blocks.BlockWaypoint;
import noppes.npcs.blocks.tiles.TileBlockAnvil;
import noppes.npcs.blocks.tiles.TileBorder;
import noppes.npcs.blocks.tiles.TileBuilder;
import noppes.npcs.blocks.tiles.TileCopy;
import noppes.npcs.blocks.tiles.TileMailbox;
import noppes.npcs.blocks.tiles.TileRedstoneBlock;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.blocks.tiles.TileScriptedDoor;
import noppes.npcs.blocks.tiles.TileTrading;
import noppes.npcs.blocks.tiles.TileWaypoint;
import noppes.npcs.constants.EnumNpcToolMaterial;
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
	
	public static CreativeTabNpcs tab;

	static {
		CustomItems.tab = new CreativeTabNpcs("cnpcs");
	}

	public static void load() {
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
		CustomItems.bag = new ItemNpcInterface(26953).setUnlocalizedName("npcBag")
				.setCreativeTab(CustomItems.tab);

		CustomItems.tab.item = CustomItems.wand;
	}
}

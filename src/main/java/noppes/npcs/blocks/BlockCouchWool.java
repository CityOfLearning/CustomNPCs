package noppes.npcs.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.blocks.tiles.TileCouchWool;
import noppes.npcs.client.renderer.ITileRenderer;

public class BlockCouchWool extends BlockContainer implements ITileRenderer {
	public static final PropertyInteger DAMAGE = PropertyInteger.create("damage", 0, 5);
	private TileColorable renderTile;

	public BlockCouchWool() {
		super(Material.wood);
	}

	private boolean compareCornerTiles(TileCouchWool tile, BlockPos pos, World world, int meta, boolean isLeft) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() != this) {
			return false;
		}
		int meta2 = state.getValue(DAMAGE).intValue();
		if (meta2 != meta) {
			return false;
		}
		TileEntity tile2 = world.getTileEntity(pos);
		int rotation = (tile.rotation + (!isLeft ? 1 : 3)) % 4;
		return (((tile2 != null) & (tile2 instanceof TileCouchWool))) && (((TileCouchWool) tile2).rotation == rotation);
	}

	private boolean compareTiles(TileCouchWool tile, BlockPos pos, World world, int meta) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() != this) {
			return false;
		}
		int meta2 = state.getValue(DAMAGE).intValue();
		if (meta2 != meta) {
			return false;
		}
		TileEntity tile2 = world.getTileEntity(pos);
		if ((tile2 == null) || (!(tile2 instanceof TileCouchWool))) {
			return false;
		}
		TileCouchWool couch = (TileCouchWool) tile2;
		int rotation = couch.rotation;
		if (tile.rotation == rotation) {
			return true;
		}
		if (couch.hasCornerLeft) {
			rotation += 3;
		} else if (couch.hasCornerRight) {
			rotation++;
		}
		rotation %= 4;
		return tile.rotation == rotation;
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { DAMAGE });
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileCouchWool();
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(DAMAGE).intValue();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
		return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 0.5D, pos.getZ() + 1);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return damageDropped(state);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(DAMAGE, Integer.valueOf(meta));
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
		par3List.add(new ItemStack(par1, 1, 2));
		par3List.add(new ItemStack(par1, 1, 3));
		par3List.add(new ItemStack(par1, 1, 4));
		par3List.add(new ItemStack(par1, 1, 5));
	}

	@Override
	public TileColorable getTile() {
		if (renderTile == null) {
			renderTile = ((TileColorable) createNewTileEntity(null, 0));
		}
		return renderTile;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack item = player.inventory.getCurrentItem();
		if ((item == null) || (item.getItem() != Items.dye)) {
			return BlockChair.MountBlock(par1World, pos, player);
		}
		TileColorable tile = (TileColorable) par1World.getTileEntity(pos);
		int color = EnumDyeColor.byDyeDamage(item.getItemDamage()).getMetadata();
		if (tile.color != color) {
			NoppesUtilServer.consumeItemStack(1, player);
			tile.color = color;
			par1World.markBlockForUpdate(pos);
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity,
			ItemStack stack) {
		int l = MathHelper.floor_double(((entity.rotationYaw * 4.0F) / 360.0F) + 0.5D) & 0x3;
		l %= 4;
		world.setBlockState(pos, state.withProperty(DAMAGE, Integer.valueOf(stack.getItemDamage())), 2);
		TileCouchWool tile = (TileCouchWool) world.getTileEntity(pos);
		tile.rotation = l;
		tile.color = (15 - stack.getItemDamage());

		updateModel(world, pos, tile);
		onNeighborBlockChange(world, pos.east(), state, this);
		onNeighborBlockChange(world, pos.west(), state, this);
		onNeighborBlockChange(world, pos.north(), state, this);
		onNeighborBlockChange(world, pos.south(), state, this);
		updateModel(world, pos, tile);
		world.markBlockForUpdate(pos);
	}

	@Override
	public void onNeighborBlockChange(World worldObj, BlockPos pos, IBlockState state, Block block) {
		if ((worldObj.isRemote) || (block != this)) {
			return;
		}
		TileEntity tile = worldObj.getTileEntity(pos);
		if ((tile == null) || (!(tile instanceof TileCouchWool))) {
			return;
		}
		updateModel(worldObj, pos, (TileCouchWool) tile);
		worldObj.markBlockForUpdate(pos);
	}

	private void updateModel(World world, BlockPos pos, TileCouchWool tile) {
		if (world.isRemote) {
			return;
		}
		int meta = tile.getBlockMetadata();
		if (tile.rotation == 0) {
			tile.hasCornerLeft = compareCornerTiles(tile, pos.north(), world, meta, true);
			tile.hasCornerRight = compareCornerTiles(tile, pos.north(), world, meta, false);
			tile.hasLeft = compareTiles(tile, pos.west(), world, meta);
			tile.hasRight = compareTiles(tile, pos.east(), world, meta);
		} else if (tile.rotation == 2) {
			tile.hasCornerLeft = compareCornerTiles(tile, pos.south(), world, meta, true);
			tile.hasCornerRight = compareCornerTiles(tile, pos.south(), world, meta, false);
			tile.hasLeft = compareTiles(tile, pos.east(), world, meta);
			tile.hasRight = compareTiles(tile, pos.west(), world, meta);
		} else if (tile.rotation == 1) {
			tile.hasCornerLeft = compareCornerTiles(tile, pos.east(), world, meta, true);
			tile.hasCornerRight = compareCornerTiles(tile, pos.east(), world, meta, false);
			tile.hasLeft = compareTiles(tile, pos.north(), world, meta);
			tile.hasRight = compareTiles(tile, pos.south(), world, meta);
		} else if (tile.rotation == 3) {
			tile.hasCornerLeft = compareCornerTiles(tile, pos.west(), world, meta, true);
			tile.hasCornerRight = compareCornerTiles(tile, pos.west(), world, meta, false);
			tile.hasLeft = compareTiles(tile, pos.south(), world, meta);
			tile.hasRight = compareTiles(tile, pos.north(), world, meta);
		}
	}
}
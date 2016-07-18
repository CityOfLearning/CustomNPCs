//

//

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
import noppes.npcs.blocks.tiles.TileCouchWood;
import noppes.npcs.client.renderer.ITileRenderer;

public class BlockCouchWood extends BlockContainer implements ITileRenderer {
	public static final PropertyInteger DAMAGE;
	static {
		DAMAGE = PropertyInteger.create("damage", 0, 5);
	}

	private TileColorable renderTile;

	public BlockCouchWood() {
		super(Material.wood);
	}

	private boolean compareTiles(final TileCouchWood tile, final BlockPos pos, final World world, final int meta) {
		final IBlockState state = world.getBlockState(pos);
		if (state.getBlock() != this) {
			return false;
		}
		final int meta2 = state.getValue(BlockCouchWood.DAMAGE);
		if (meta2 != meta) {
			return false;
		}
		final TileEntity tile2 = world.getTileEntity(pos);
		if ((tile2 == null) || !(tile2 instanceof TileCouchWood)) {
			return false;
		}
		final TileCouchWood couch = (TileCouchWood) tile2;
		return tile.rotation == couch.rotation;
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { BlockCouchWood.DAMAGE });
	}

	@Override
	public TileEntity createNewTileEntity(final World var1, final int var2) {
		return new TileCouchWood();
	}

	@Override
	public int damageDropped(final IBlockState state) {
		return state.getValue(BlockCouchWood.DAMAGE) % 7;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(final World world, final BlockPos pos, final IBlockState state) {
		return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 0.5, pos.getZ() + 1);
	}

	@Override
	public int getMetaFromState(final IBlockState state) {
		return damageDropped(state);
	}

	@Override
	public IBlockState getStateFromMeta(final int meta) {
		return getDefaultState().withProperty(BlockCouchWood.DAMAGE, meta);
	}

	@Override
	public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
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
			renderTile = (TileColorable) createNewTileEntity(null, 0);
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
	public boolean onBlockActivated(final World par1World, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		final ItemStack item = player.inventory.getCurrentItem();
		if ((item == null) || (item.getItem() != Items.dye)) {
			return BlockChair.MountBlock(par1World, pos, player);
		}
		final TileColorable tile = (TileColorable) par1World.getTileEntity(pos);
		final int color = EnumDyeColor.byDyeDamage(item.getItemDamage()).getMetadata();
		if (tile.color != color) {
			NoppesUtilServer.consumeItemStack(1, player);
			tile.color = color;
			par1World.markBlockForUpdate(pos);
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state,
			final EntityLivingBase entity, final ItemStack stack) {
		int l = MathHelper.floor_double(((entity.rotationYaw * 4.0f) / 360.0f) + 0.5) & 0x3;
		l %= 4;
		world.setBlockState(pos, state.withProperty(BlockCouchWood.DAMAGE, stack.getItemDamage()), 2);
		final TileCouchWood tile = (TileCouchWood) world.getTileEntity(pos);
		tile.rotation = l;
		tile.color = 15 - stack.getItemDamage();
		updateModel(world, pos, tile);
		onNeighborBlockChange(world, pos.east(), state, this);
		onNeighborBlockChange(world, pos.west(), state, this);
		onNeighborBlockChange(world, pos.north(), state, this);
		onNeighborBlockChange(world, pos.south(), state, this);
		updateModel(world, pos, tile);
		world.markBlockForUpdate(pos);
	}

	@Override
	public void onNeighborBlockChange(final World worldObj, final BlockPos pos, final IBlockState state,
			final Block block) {
		if (worldObj.isRemote || (block != this)) {
			return;
		}
		final TileEntity tile = worldObj.getTileEntity(pos);
		if ((tile == null) || !(tile instanceof TileCouchWood)) {
			return;
		}
		updateModel(worldObj, pos, (TileCouchWood) tile);
		worldObj.markBlockForUpdate(pos);
	}

	private void updateModel(final World world, final BlockPos pos, final TileCouchWood tile) {
		if (world.isRemote) {
			return;
		}
		final int meta = tile.getBlockMetadata();
		if (tile.rotation == 0) {
			tile.hasLeft = compareTiles(tile, pos.west(), world, meta);
			tile.hasRight = compareTiles(tile, pos.east(), world, meta);
		} else if (tile.rotation == 2) {
			tile.hasLeft = compareTiles(tile, pos.east(), world, meta);
			tile.hasRight = compareTiles(tile, pos.west(), world, meta);
		} else if (tile.rotation == 1) {
			tile.hasLeft = compareTiles(tile, pos.north(), world, meta);
			tile.hasRight = compareTiles(tile, pos.south(), world, meta);
		} else if (tile.rotation == 3) {
			tile.hasLeft = compareTiles(tile, pos.south(), world, meta);
			tile.hasRight = compareTiles(tile, pos.north(), world, meta);
		}
	}
}

package noppes.npcs.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import noppes.npcs.blocks.tiles.TileChair;
import noppes.npcs.blocks.tiles.TileCouchWood;
import noppes.npcs.entity.EntityChairMount;

public class BlockCouchWood extends BlockRotated {

	public BlockCouchWood() {
		super(Material.wood);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		((TileCouchWood) world.getTileEntity(pos)).killMount();
		world.removeTileEntity(pos);
		super.breakBlock(world, pos, state);
	}

	private boolean compareTiles(TileCouchWood tile, BlockPos pos, World world, int meta) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() != this) {
			return false;
		}
		int meta2 = state.getValue(DAMAGE).intValue();
		if (meta2 != meta) {
			return false;
		}
		TileEntity tile2 = world.getTileEntity(pos);
		if ((tile2 == null) || (!(tile2 instanceof TileCouchWood))) {
			return false;
		}
		TileCouchWood couch = (TileCouchWood) tile2;
		return tile.getRotation() == couch.getRotation();
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileCouchWood();
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(DAMAGE).intValue() % 7;
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
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
		par3List.add(new ItemStack(par1, 1, 2));
		par3List.add(new ItemStack(par1, 1, 3));
		par3List.add(new ItemStack(par1, 1, 4));
		par3List.add(new ItemStack(par1, 1, 5));
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}
		((TileChair) world.getTileEntity(pos)).mount(world, pos, player);
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity,
			ItemStack stack) {
		world.setBlockState(pos, state.withProperty(DAMAGE, Integer.valueOf(stack.getItemDamage())), 2);
		super.onBlockPlacedBy(world, pos, state, entity, stack);
		TileCouchWood tile = (TileCouchWood) world.getTileEntity(pos);

		updateModel(world, pos, tile);
		onNeighborBlockChange(world, pos.east(), state, this);
		onNeighborBlockChange(world, pos.west(), state, this);
		onNeighborBlockChange(world, pos.north(), state, this);
		onNeighborBlockChange(world, pos.south(), state, this);
		updateModel(world, pos, tile);

		EntityChairMount mount = new EntityChairMount(world, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5D);
		// mount.rotationYaw = tile.getRotation() * 90;
		mount.rotationYaw = ((2 + tile.getRotation()) % 4) * 90;
		world.spawnEntityInWorld(mount);
		tile.setMount(mount);

		world.markBlockForUpdate(pos);
	}

	@Override
	public void onNeighborBlockChange(World worldObj, BlockPos pos, IBlockState state, Block block) {
		if ((worldObj.isRemote) || (block != this)) {
			return;
		}
		TileEntity tile = worldObj.getTileEntity(pos);
		if ((tile == null) || (!(tile instanceof TileCouchWood))) {
			return;
		}
		updateModel(worldObj, pos, (TileCouchWood) tile);
		worldObj.markBlockForUpdate(pos);
	}

	private void updateModel(World world, BlockPos pos, TileCouchWood tile) {
		if (world.isRemote) {
			return;
		}
		int meta = tile.getBlockMetadata();
		if (tile.getRotation() == 0) {
			tile.setHasLeft(compareTiles(tile, pos.west(), world, meta));
			tile.setHasRight(compareTiles(tile, pos.east(), world, meta));
		} else if (tile.getRotation() == 2) {
			tile.setHasLeft(compareTiles(tile, pos.east(), world, meta));
			tile.setHasRight(compareTiles(tile, pos.west(), world, meta));
		} else if (tile.getRotation() == 1) {
			tile.setHasLeft(compareTiles(tile, pos.north(), world, meta));
			tile.setHasRight(compareTiles(tile, pos.south(), world, meta));
		} else if (tile.getRotation() == 3) {
			tile.setHasLeft(compareTiles(tile, pos.south(), world, meta));
			tile.setHasRight(compareTiles(tile, pos.north(), world, meta));
		}
	}
}

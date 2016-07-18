//

//

package noppes.npcs.blocks;

import java.util.List;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.blocks.tiles.TileNpcContainer;
import noppes.npcs.blocks.tiles.TileWeaponRack;

public class BlockWeaponRack extends BlockTrigger {
	public static final PropertyInteger DAMAGE;
	public static final PropertyBool IS_TOP;

	static {
		DAMAGE = PropertyInteger.create("damage", 0, 6);
		IS_TOP = PropertyBool.create("istop");
	}

	public BlockWeaponRack() {
		super(Blocks.planks);
		setDefaultState(blockState.getBaseState().withProperty(BlockWeaponRack.IS_TOP, false));
	}

	@Override
	public void breakBlock(final World world, final BlockPos pos, final IBlockState state) {
		final TileNpcContainer tile = (TileNpcContainer) world.getTileEntity(pos);
		if (tile == null) {
			return;
		}
		tile.dropItems(world, pos);
		world.updateComparatorOutputLevel(pos, state.getBlock());
		super.breakBlock(world, pos, state);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { BlockWeaponRack.DAMAGE, BlockWeaponRack.IS_TOP });
	}

	@Override
	public TileEntity createNewTileEntity(final World var1, final int var2) {
		if (var2 < 7) {
			return new TileWeaponRack();
		}
		return null;
	}

	@Override
	public int damageDropped(final IBlockState state) {
		return state.getValue(BlockWeaponRack.DAMAGE);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(final World world, final BlockPos pos, final IBlockState state) {
		setBlockBoundsBasedOnState(world, pos);
		return super.getCollisionBoundingBox(world, pos, state);
	}

	@Override
	public int getMetaFromState(final IBlockState state) {
		return state.getValue(BlockWeaponRack.DAMAGE) + (state.getValue(BlockWeaponRack.IS_TOP) ? 7 : 0);
	}

	@Override
	public IBlockState getStateFromMeta(final int meta) {
		return getDefaultState().withProperty(BlockWeaponRack.DAMAGE, (meta % 7)).withProperty(BlockWeaponRack.IS_TOP,
				(meta >= 7));
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
	public boolean onBlockActivated(final World par1World, BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		if (par1World.isRemote) {
			return true;
		}
		if (state.getValue(BlockWeaponRack.IS_TOP)) {
			pos = pos.down();
		}
		final TileWeaponRack tile = (TileWeaponRack) par1World.getTileEntity(pos);
		float hit = hitX;
		if (tile.rotation == 2) {
			hit = 1.0f - hitX;
		}
		if (tile.rotation == 3) {
			hit = 1.0f - hitZ;
		}
		if (tile.rotation == 1) {
			hit = hitZ;
		}
		final int selected = 2 - (int) (hit / 0.34);
		final ItemStack item = player.getCurrentEquippedItem();
		final ItemStack weapon = tile.getStackInSlot(selected);
		if ((item == null) && (weapon != null)) {
			tile.setInventorySlotContents(selected, null);
			player.inventory.setInventorySlotContents(player.inventory.currentItem, weapon);
			par1World.markBlockForUpdate(pos);
			updateSurrounding(par1World, pos);
		} else {
			if ((item == null) || (item.getItem() == null) || (item.getItem() instanceof ItemBlock)) {
				return true;
			}
			if ((item != null) && (weapon == null)) {
				tile.setInventorySlotContents(selected, item);
				player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack) null);
				par1World.markBlockForUpdate(pos);
				updateSurrounding(par1World, pos);
			}
		}
		return true;
	}

	@Override
	public void onBlockHarvested(final World world, final BlockPos pos, final IBlockState state,
			final EntityPlayer player) {
		if (state.getValue(BlockWeaponRack.IS_TOP) && (world.getBlockState(pos.down()).getBlock() == this)) {
			world.setBlockToAir(pos.down());
		} else if (!(boolean) state.getValue(BlockWeaponRack.IS_TOP)
				&& (world.getBlockState(pos.up()).getBlock() == this)) {
			world.setBlockToAir(pos.up());
		}
	}

	@Override
	public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state,
			final EntityLivingBase entity, final ItemStack stack) {
		if (!world.isAirBlock(pos.up())) {
			world.setBlockToAir(pos);
		} else {
			world.setBlockState(pos, state.withProperty(BlockWeaponRack.DAMAGE, stack.getItemDamage())
					.withProperty(BlockWeaponRack.IS_TOP, false), 2);
			world.setBlockState(pos.up(), state.withProperty(BlockWeaponRack.DAMAGE, stack.getItemDamage())
					.withProperty(BlockWeaponRack.IS_TOP, true), 2);
			int l = MathHelper.floor_double(((entity.rotationYaw * 4.0f) / 360.0f) + 0.5) & 0x3;
			l %= 4;
			final TileColorable tile = (TileColorable) world.getTileEntity(pos);
			tile.rotation = l;
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(final IBlockAccess world, BlockPos pos) {
		boolean isTop = false;
		try {
			isTop = world.getBlockState(pos).getValue(BlockWeaponRack.IS_TOP);
		} catch (IllegalArgumentException ex) {
		}
		if (isTop) {
			pos = pos.down();
		}
		final TileEntity tileentity = world.getTileEntity(pos);
		if (!(tileentity instanceof TileColorable)) {
			super.setBlockBoundsBasedOnState(world, pos);
			return;
		}
		final TileColorable tile = (TileColorable) tileentity;
		float xStart = 0.0f;
		float zStart = 0.0f;
		float xEnd = 1.0f;
		float zEnd = 1.0f;
		if (tile.rotation == 0) {
			zStart = 0.7f;
		} else if (tile.rotation == 2) {
			zEnd = 0.3f;
		} else if (tile.rotation == 3) {
			xStart = 0.7f;
		} else if (tile.rotation == 1) {
			xEnd = 0.3f;
		}
		if (isTop) {
			setBlockBounds(xStart, -1.0f, zStart, xEnd, 0.8f, zEnd);
		} else {
			setBlockBounds(xStart, 0.0f, zStart, xEnd, 1.8f, zEnd);
		}
	}
}

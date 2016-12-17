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
	public static final PropertyInteger DAMAGE = PropertyInteger.create("damage", 0, 6);
	public static final PropertyBool IS_TOP = PropertyBool.create("istop");

	public BlockWeaponRack() {
		super(Blocks.planks);
		setDefaultState(blockState.getBaseState().withProperty(IS_TOP, Boolean.valueOf(false)));
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileNpcContainer tile = (TileNpcContainer) world.getTileEntity(pos);
		if (tile == null) {
			return;
		}
		tile.dropItems(world, pos);

		world.updateComparatorOutputLevel(pos, state.getBlock());
		super.breakBlock(world, pos, state);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { DAMAGE, IS_TOP });
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		if (var2 < 7) {
			return new TileWeaponRack();
		}
		return null;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(DAMAGE).intValue();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
		setBlockBoundsBasedOnState(world, pos);
		return super.getCollisionBoundingBox(world, pos, state);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(DAMAGE).intValue() + (state.getValue(IS_TOP).booleanValue() ? 7 : 0);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(DAMAGE, Integer.valueOf(meta % 7)).withProperty(IS_TOP,
				Boolean.valueOf(meta >= 7));
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
	public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (par1World.isRemote) {
			return true;
		}
		if (state.getValue(IS_TOP).booleanValue()) {
			pos = pos.down();
		}
		TileWeaponRack tile = (TileWeaponRack) par1World.getTileEntity(pos);
		float hit = hitX;
		if (tile.getRotation() == 2) {
			hit = 1.0F - hitX;
		}
		if (tile.getRotation() == 3) {
			hit = 1.0F - hitZ;
		}
		if (tile.getRotation() == 1) {
			hit = hitZ;
		}
		int selected = 2 - (int) (hit / 0.34D);
		ItemStack item = player.getCurrentEquippedItem();
		ItemStack weapon = tile.getStackInSlot(selected);
		if ((item == null) && (weapon != null)) {
			tile.setInventorySlotContents(selected, null);
			player.inventory.setInventorySlotContents(player.inventory.currentItem, weapon);
			par1World.markBlockForUpdate(pos);
			updateSurrounding(par1World, pos);
		} else {
			if ((item == null) || (item.getItem() == null) || ((item.getItem() instanceof ItemBlock))) {
				return true;
			}
			if ((item != null) && (weapon == null)) {
				tile.setInventorySlotContents(selected, item);
				player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
				par1World.markBlockForUpdate(pos);
				updateSurrounding(par1World, pos);
			}
		}
		return true;
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if ((state.getValue(IS_TOP).booleanValue()) && (world.getBlockState(pos.down()).getBlock() == this)) {
			world.setBlockToAir(pos.down());
		} else if ((!state.getValue(IS_TOP).booleanValue()) && (world.getBlockState(pos.up()).getBlock() == this)) {
			world.setBlockToAir(pos.up());
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity,
			ItemStack stack) {
		if (!world.isAirBlock(pos.up())) {
			world.setBlockToAir(pos);
		} else {
			world.setBlockState(pos, state.withProperty(DAMAGE, Integer.valueOf(stack.getItemDamage()))
					.withProperty(IS_TOP, Boolean.valueOf(false)), 2);
			world.setBlockState(pos.up(), state.withProperty(DAMAGE, Integer.valueOf(stack.getItemDamage()))
					.withProperty(IS_TOP, Boolean.valueOf(true)), 2);

			int l = MathHelper.floor_double(((entity.rotationYaw * 4.0F) / 360.0F) + 0.5D) & 0x3;
			l %= 4;

			TileColorable tile = (TileColorable) world.getTileEntity(pos);
			tile.setRotation(l);
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
		boolean isTop = false;
		try {
			isTop = world.getBlockState(pos).getValue(IS_TOP).booleanValue();
		} catch (IllegalArgumentException ex) {
		}
		if (isTop) {
			pos = pos.down();
		}
		TileEntity tileentity = world.getTileEntity(pos);
		if (!(tileentity instanceof TileColorable)) {
			super.setBlockBoundsBasedOnState(world, pos);
			return;
		}
		TileColorable tile = (TileColorable) tileentity;
		float xStart = 0.0F;
		float zStart = 0.0F;
		float xEnd = 1.0F;
		float zEnd = 1.0F;
		if (tile.getRotation() == 0) {
			zStart = 0.7F;
		} else if (tile.getRotation() == 2) {
			zEnd = 0.3F;
		} else if (tile.getRotation() == 3) {
			xStart = 0.7F;
		} else if (tile.getRotation() == 1) {
			xEnd = 0.3F;
		}
		if (isTop) {
			setBlockBounds(xStart, -1.0F, zStart, xEnd, 0.8F, zEnd);
		} else {
			setBlockBounds(xStart, 0.0F, zStart, xEnd, 1.8F, zEnd);
		}
	}
}

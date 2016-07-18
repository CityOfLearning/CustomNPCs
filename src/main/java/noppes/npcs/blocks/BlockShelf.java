//

//

package noppes.npcs.blocks;

import java.util.List;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.blocks.tiles.TileShelf;

public class BlockShelf extends BlockRotated {
	public BlockShelf() {
		super(Blocks.planks);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { BlockRotated.DAMAGE });
	}

	@Override
	public TileEntity createNewTileEntity(final World var1, final int var2) {
		return new TileShelf();
	}

	@Override
	public int damageDropped(final IBlockState state) {
		return state.getValue(BlockRotated.DAMAGE);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(final World world, final BlockPos pos, final IBlockState state) {
		setBlockBoundsBasedOnState(world, pos);
		return new AxisAlignedBB(pos.getX() + minX, pos.getY() + 0.9f, pos.getZ() + minZ, pos.getX() + maxX,
				pos.getY() + 1, pos.getZ() + maxZ);
	}

	@Override
	public int getMetaFromState(final IBlockState state) {
		return damageDropped(state);
	}

	@Override
	public IBlockState getStateFromMeta(final int meta) {
		return getDefaultState().withProperty(BlockRotated.DAMAGE, meta);
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
	public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state,
			final EntityLivingBase entity, final ItemStack stack) {
		world.setBlockState(pos, state.withProperty(BlockRotated.DAMAGE, stack.getItemDamage()), 2);
		super.onBlockPlacedBy(world, pos, state, entity, stack);
	}

	@Override
	public void setBlockBoundsBasedOnState(final IBlockAccess world, final BlockPos pos) {
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
			zStart = 0.3f;
		} else if (tile.rotation == 2) {
			zEnd = 0.7f;
		} else if (tile.rotation == 3) {
			xStart = 0.3f;
		} else if (tile.rotation == 1) {
			xEnd = 0.7f;
		}
		setBlockBounds(xStart, 0.44f, zStart, xEnd, 1.0f, zEnd);
	}
}

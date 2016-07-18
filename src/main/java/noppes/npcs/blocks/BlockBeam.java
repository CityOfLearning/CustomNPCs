//

//

package noppes.npcs.blocks;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.blocks.tiles.TileBeam;
import noppes.npcs.blocks.tiles.TileColorable;

public class BlockBeam extends BlockRotated {
	public BlockBeam() {
		super(Blocks.planks);
	}

	@Override
	public TileEntity createNewTileEntity(final World var1, final int var2) {
		return new TileBeam();
	}

	@Override
	public int damageDropped(final IBlockState state) {
		return state.getValue(BlockRotated.DAMAGE);
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
		super.onBlockPlacedBy(world, pos, state, entity, stack);
		world.setBlockState(pos, state.withProperty(BlockRotated.DAMAGE, stack.getItemDamage()), 2);
	}

	@Override
	public void setBlockBoundsBasedOnState(final IBlockAccess world, final BlockPos pos) {
		final TileEntity tileentity = world.getTileEntity(pos);
		if (!(tileentity instanceof TileColorable)) {
			super.setBlockBoundsBasedOnState(world, pos);
			return;
		}
		final TileColorable tile = (TileColorable) tileentity;
		if (tile.rotation == 0) {
			setBlockBounds(0.33f, 0.33f, 0.25f, 0.67f, 0.67f, 1.0f);
		} else if (tile.rotation == 2) {
			setBlockBounds(0.33f, 0.33f, 0.0f, 0.67f, 0.67f, 0.75f);
		} else if (tile.rotation == 3) {
			setBlockBounds(0.25f, 0.33f, 0.33f, 1.0f, 0.67f, 0.67f);
		} else if (tile.rotation == 1) {
			setBlockBounds(0.0f, 0.33f, 0.33f, 0.75f, 0.67f, 0.67f);
		}
	}
}

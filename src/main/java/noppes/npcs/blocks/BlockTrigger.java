//

//

package noppes.npcs.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.blocks.tiles.TileColorable;

public abstract class BlockTrigger extends BlockRotated {
	protected BlockTrigger(final Block block) {
		super(block);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public int getStrongPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state,
			final EnumFacing side) {
		return getWeakPower(worldIn, pos, state, side);
	}

	@Override
	public int getWeakPower(final IBlockAccess world, final BlockPos pos, final IBlockState state,
			final EnumFacing side) {
		final TileColorable tile = (TileColorable) world.getTileEntity(pos);
		if (tile != null) {
			return tile.powerProvided();
		}
		return 0;
	}

	public void updateSurrounding(final World par1World, final BlockPos pos) {
		par1World.notifyNeighborsOfStateChange(pos, this);
		par1World.notifyNeighborsOfStateChange(pos.down(), this);
		par1World.notifyNeighborsOfStateChange(pos.up(), this);
		par1World.notifyNeighborsOfStateChange(pos.west(), this);
		par1World.notifyNeighborsOfStateChange(pos.east(), this);
		par1World.notifyNeighborsOfStateChange(pos.south(), this);
		par1World.notifyNeighborsOfStateChange(pos.north(), this);
	}
}

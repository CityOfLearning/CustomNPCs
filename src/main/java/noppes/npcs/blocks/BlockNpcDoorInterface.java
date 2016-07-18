//

//

package noppes.npcs.blocks;

import java.util.Random;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.blocks.tiles.TileDoor;

public abstract class BlockNpcDoorInterface extends BlockDoor implements ITileEntityProvider {
	public BlockNpcDoorInterface() {
		super(Material.wood);
		isBlockContainer = true;
	}

	@Override
	public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
	}

	@Override
	public TileEntity createNewTileEntity(final World worldIn, final int meta) {
		return new TileDoor();
	}

	@Override
	public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
		if (state.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER) {
			final IBlockState iblockstate1 = worldIn.getBlockState(pos.up());
			if (iblockstate1.getBlock() == this) {
				state = state.withProperty(BlockDoor.HINGE, iblockstate1.getValue(BlockDoor.HINGE))
						.withProperty(BlockDoor.POWERED, iblockstate1.getValue(BlockDoor.POWERED));
			}
		} else {
			final IBlockState iblockstate1 = worldIn.getBlockState(pos.down());
			if (iblockstate1.getBlock() == this) {
				state = state.withProperty(BlockDoor.FACING, iblockstate1.getValue(BlockDoor.FACING))
						.withProperty(BlockDoor.OPEN, iblockstate1.getValue(BlockDoor.OPEN));
			}
		}
		return state;
	}

	@Override
	public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
		return null;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean hasTileEntity(final IBlockState state) {
		return true;
	}
}

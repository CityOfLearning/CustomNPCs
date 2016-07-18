//

//

package noppes.npcs.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileScriptedDoor;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.util.IPermission;

public class BlockScriptedDoor extends BlockNpcDoorInterface implements IPermission {
	@Override
	public void breakBlock(final World world, final BlockPos pos, final IBlockState state) {
		final BlockPos blockpos1 = (state.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER) ? pos : pos.down();
		final IBlockState iblockstate1 = pos.equals(blockpos1) ? state : world.getBlockState(blockpos1);
		if ((iblockstate1.getBlock() != this) || world.isRemote) {
			return;
		}
		final TileScriptedDoor tile = (TileScriptedDoor) world.getTileEntity(pos);
		EventHooks.onScriptBlockBreak(tile);
	}

	@Override
	public TileEntity createNewTileEntity(final World worldIn, final int meta) {
		return new TileScriptedDoor();
	}

	@Override
	public boolean isAllowed(final EnumPacketServer e) {
		return e == EnumPacketServer.ScriptDoorDataSave;
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		if (world.isRemote) {
			return true;
		}
		final BlockPos blockpos1 = (state.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER) ? pos : pos.down();
		final IBlockState iblockstate1 = pos.equals(blockpos1) ? state : world.getBlockState(blockpos1);
		if (iblockstate1.getBlock() != this) {
			return false;
		}
		final ItemStack currentItem = player.inventory.getCurrentItem();
		if ((currentItem != null)
				&& ((currentItem.getItem() == CustomItems.wand) || (currentItem.getItem() == CustomItems.scripter)
						|| (currentItem.getItem() == CustomItems.scriptedDoorTool))) {
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.ScriptDoor, null, blockpos1.getX(), blockpos1.getY(),
					blockpos1.getZ());
			return true;
		}
		final TileScriptedDoor tile = (TileScriptedDoor) world.getTileEntity(blockpos1);
		if (EventHooks.onScriptBlockInteract(tile, player, side.getIndex(), hitX, hitY, hitZ)) {
			return false;
		}
		toggleDoor(world, blockpos1, iblockstate1.getValue(BlockDoor.OPEN).equals(false));
		return true;
	}

	@Override
	public void onBlockClicked(final World world, final BlockPos pos, final EntityPlayer playerIn) {
		if (world.isRemote) {
			return;
		}
		final IBlockState state = world.getBlockState(pos);
		final BlockPos blockpos1 = (state.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER) ? pos : pos.down();
		final IBlockState iblockstate1 = pos.equals(blockpos1) ? state : world.getBlockState(blockpos1);
		if (iblockstate1.getBlock() != this) {
			return;
		}
		final TileScriptedDoor tile = (TileScriptedDoor) world.getTileEntity(blockpos1);
		EventHooks.onScriptBlockClicked(tile, playerIn);
	}

	@Override
	public void onBlockHarvested(final World world, final BlockPos pos, final IBlockState state,
			final EntityPlayer player) {
		final BlockPos blockpos1 = (state.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER) ? pos : pos.down();
		final IBlockState iblockstate1 = pos.equals(blockpos1) ? state : world.getBlockState(blockpos1);
		if (player.capabilities.isCreativeMode
				&& (iblockstate1.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER)
				&& (iblockstate1.getBlock() == this)) {
			final TileScriptedDoor tile = (TileScriptedDoor) world.getTileEntity(blockpos1);
			if (!world.isRemote) {
				EventHooks.onScriptBlockHarvest(tile, player);
			}
			world.setBlockToAir(blockpos1);
		}
	}

	@Override
	public void onEntityCollidedWithBlock(final World world, final BlockPos pos, final IBlockState state,
			final Entity entityIn) {
		if (world.isRemote) {
			return;
		}
		final TileScriptedDoor tile = (TileScriptedDoor) world.getTileEntity(pos);
		EventHooks.onScriptBlockCollide(tile, entityIn);
	}

	@Override
	public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state,
			final Block neighborBlock) {
		if (state.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.UPPER) {
			final BlockPos blockpos1 = pos.down();
			final IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);
			if (iblockstate1.getBlock() != this) {
				worldIn.setBlockToAir(pos);
			} else if (neighborBlock != this) {
				onNeighborBlockChange(worldIn, blockpos1, iblockstate1, neighborBlock);
			}
		} else {
			boolean flag1 = false;
			final BlockPos blockpos2 = pos.up();
			final IBlockState iblockstate2 = worldIn.getBlockState(blockpos2);
			if (iblockstate2.getBlock() != this) {
				worldIn.setBlockToAir(pos);
				flag1 = true;
			}
			if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) {
				worldIn.setBlockToAir(pos);
				flag1 = true;
				if (iblockstate2.getBlock() == this) {
					worldIn.setBlockToAir(blockpos2);
				}
			}
			if (flag1) {
				if (!worldIn.isRemote) {
					dropBlockAsItem(worldIn, pos, state, 0);
				}
			} else {
				final TileScriptedDoor tile = (TileScriptedDoor) worldIn.getTileEntity(pos);
				if (!worldIn.isRemote) {
					EventHooks.onScriptBlockNeighborChanged(tile);
				}
				final boolean flag2 = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(blockpos2);
				if ((flag2 || neighborBlock.canProvidePower()) && (neighborBlock != this)
						&& (flag2 != iblockstate2.getValue(BlockDoor.POWERED))) {
					worldIn.setBlockState(blockpos2, iblockstate2.withProperty(BlockDoor.POWERED, flag2), 2);
					if (flag2 != state.getValue(BlockDoor.OPEN)) {
						toggleDoor(worldIn, pos, flag2);
					}
				}
				int power = 0;
				for (final EnumFacing enumfacing : EnumFacing.values()) {
					final int p = worldIn.getRedstonePower(pos.offset(enumfacing), enumfacing);
					if (p > power) {
						power = p;
					}
				}
				tile.newPower = power;
			}
		}
	}

	@Override
	public void toggleDoor(final World worldIn, final BlockPos pos, final boolean open) {
		final TileScriptedDoor tile = (TileScriptedDoor) worldIn.getTileEntity(pos);
		if (EventHooks.onScriptBlockDoorToggle(tile)) {
			return;
		}
		super.toggleDoor(worldIn, pos, open);
	}
}

//

//

package noppes.npcs.api.wrapper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import noppes.npcs.api.ITimers;
import noppes.npcs.api.block.IBlockScriptedDoor;
import noppes.npcs.blocks.tiles.TileScriptedDoor;

public class BlockScriptedDoorWrapper extends BlockWrapper implements IBlockScriptedDoor {
	private TileScriptedDoor tile;

	public BlockScriptedDoorWrapper(final World world, final Block block, final BlockPos pos) {
		super(world, block, pos);
		tile = (TileScriptedDoor) super.tile;
	}

	@Override
	public String getBlockModel() {
		return Block.blockRegistry.getNameForObject(tile.blockModel) + "";
	}

	@Override
	public boolean getOpen() {
		final IBlockState state = world.getMCWorld().getBlockState(pos);
		return state.getValue(BlockDoor.OPEN).equals(true);
	}

	@Override
	public ITimers getTimers() {
		return tile.timers;
	}

	@Override
	public void setBlockModel(final String name) {
		Block b = null;
		if (name != null) {
			b = Block.getBlockFromName(name);
		}
		tile.setItemModel(b);
	}

	@Override
	public void setOpen(final boolean open) {
		if ((getOpen() == open) || isRemoved()) {
			return;
		}
		world.getMCWorld().getBlockState(pos);
		((BlockDoor) block).toggleDoor(world.getMCWorld(), pos, open);
	}
}

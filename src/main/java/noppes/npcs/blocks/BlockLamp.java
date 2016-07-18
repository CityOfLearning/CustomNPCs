//

//

package noppes.npcs.blocks;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.blocks.tiles.TileLamp;

public class BlockLamp extends BlockLightable {
	public BlockLamp(final boolean lit) {
		super(Blocks.planks, lit);
		setBlockBounds(0.3f, 0.0f, 0.3f, 0.7f, 0.6f, 0.7f);
	}

	@Override
	public TileEntity createNewTileEntity(final World var1, final int var2) {
		return new TileLamp();
	}

	@Override
	public Block litBlock() {
		return CustomItems.lamp;
	}

	@Override
	public int maxRotation() {
		return 8;
	}

	@Override
	public void setBlockBoundsBasedOnState(final IBlockAccess world, final BlockPos pos) {
		final TileEntity tileentity = world.getTileEntity(pos);
		if (!(tileentity instanceof TileColorable)) {
			super.setBlockBoundsBasedOnState(world, pos);
			return;
		}
		final TileColorable tile = (TileColorable) tileentity;
		if (tile.color == 2) {
			float xOffset = 0.0f;
			float yOffset = 0.0f;
			if (tile.rotation == 0) {
				yOffset = 0.2f;
			} else if (tile.rotation == 4) {
				yOffset = -0.2f;
			} else if (tile.rotation == 6) {
				xOffset = 0.2f;
			} else if (tile.rotation == 2) {
				xOffset = -0.2f;
			}
			setBlockBounds(0.3f + xOffset, 0.2f, 0.3f + yOffset, 0.7f + xOffset, 0.7f, 0.7f + yOffset);
		} else {
			setBlockBounds(0.3f, 0.0f, 0.3f, 0.7f, 0.6f, 0.7f);
		}
	}

	@Override
	public Block unlitBlock() {
		return CustomItems.lamp_unlit;
	}
}

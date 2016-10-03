package noppes.npcs.blocks.tiles;

import net.minecraft.util.AxisAlignedBB;

public class TileTallLamp extends TileColorable {
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1);
	}
}

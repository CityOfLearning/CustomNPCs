//

//

package noppes.npcs.blocks.tiles;

import net.minecraft.util.AxisAlignedBB;

public class TileWallBanner extends TileBanner {
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos.getX(), pos.getY() - 1, pos.getZ(), pos.getX() + 1, pos.getY() + 1,
				pos.getZ() + 1);
	}
}

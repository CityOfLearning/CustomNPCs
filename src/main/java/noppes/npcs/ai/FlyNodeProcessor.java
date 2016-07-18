//

//

package noppes.npcs.ai;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.pathfinder.NodeProcessor;
import noppes.npcs.entity.EntityNPCInterface;

public class FlyNodeProcessor extends NodeProcessor {
	@Override
	public int findPathOptions(final PathPoint[] p_176164_1_, final Entity p_176164_2_, final PathPoint p_176164_3_,
			final PathPoint p_176164_4_, final float p_176164_5_) {
		int i = 0;
		for (final EnumFacing enumfacing : EnumFacing.values()) {
			final PathPoint pathpoint2 = getSafePoint(p_176164_2_, p_176164_3_.xCoord + enumfacing.getFrontOffsetX(),
					p_176164_3_.yCoord + enumfacing.getFrontOffsetY(),
					p_176164_3_.zCoord + enumfacing.getFrontOffsetZ());
			if ((pathpoint2 != null) && !pathpoint2.visited && (pathpoint2.distanceTo(p_176164_4_) < p_176164_5_)) {
				p_176164_1_[i++] = pathpoint2;
			}
		}
		return i;
	}

	private int func_176186_b(final Entity entityIn, final int x, final int y, final int z) {
		final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
		for (int i = x; i < (x + entitySizeX); ++i) {
			for (int j = y; j < (y + entitySizeY); ++j) {
				for (int k = z; k < (z + entitySizeZ); ++k) {
					final Block block = blockaccess.getBlockState(blockpos$mutableblockpos.set(i, j, k)).getBlock();
					if ((block.getMaterial() != Material.water) || !(entityIn instanceof EntityNPCInterface)
							|| !((EntityNPCInterface) entityIn).ai.canSwim) {
						if (block.getMaterial() != Material.air) {
							return 0;
						}
					}
				}
			}
		}
		return -1;
	}

	@Override
	public PathPoint getPathPointTo(final Entity p_176161_1_) {
		return openPoint(MathHelper.floor_double(p_176161_1_.getEntityBoundingBox().minX),
				MathHelper.floor_double(p_176161_1_.getEntityBoundingBox().minY + 0.5),
				MathHelper.floor_double(p_176161_1_.getEntityBoundingBox().minZ));
	}

	@Override
	public PathPoint getPathPointToCoords(final Entity p_176160_1_, final double p_176160_2_, final double p_176160_4_,
			final double p_176160_6_) {
		return openPoint(MathHelper.floor_double(p_176160_2_ - (p_176160_1_.width / 2.0f)),
				MathHelper.floor_double(p_176160_4_ + 0.5),
				MathHelper.floor_double(p_176160_6_ - (p_176160_1_.width / 2.0f)));
	}

	private PathPoint getSafePoint(final Entity p_176185_1_, final int p_176185_2_, final int p_176185_3_,
			final int p_176185_4_) {
		final int l = func_176186_b(p_176185_1_, p_176185_2_, p_176185_3_, p_176185_4_);
		return (l == -1) ? openPoint(p_176185_2_, p_176185_3_, p_176185_4_) : null;
	}
}

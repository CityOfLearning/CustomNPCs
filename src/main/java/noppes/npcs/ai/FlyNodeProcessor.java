
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
	public int findPathOptions(PathPoint[] p_176164_1_, Entity p_176164_2_, PathPoint p_176164_3_,
			PathPoint p_176164_4_, float p_176164_5_) {
		int i = 0;
		for (EnumFacing enumfacing : EnumFacing.values()) {
			PathPoint pathpoint2 = getSafePoint(p_176164_2_, p_176164_3_.xCoord + enumfacing.getFrontOffsetX(),
					p_176164_3_.yCoord + enumfacing.getFrontOffsetY(),
					p_176164_3_.zCoord + enumfacing.getFrontOffsetZ());
			if ((pathpoint2 != null) && !pathpoint2.visited && (pathpoint2.distanceTo(p_176164_4_) < p_176164_5_)) {
				p_176164_1_[i++] = pathpoint2;
			}
		}
		return i;
	}

	private int func_176186_b(Entity entityIn, int x, int y, int z) {
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
		for (int i = x; i < (x + entitySizeX); ++i) {
			for (int j = y; j < (y + entitySizeY); ++j) {
				for (int k = z; k < (z + entitySizeZ); ++k) {
					Block block = blockaccess.getBlockState(blockpos$mutableblockpos.set(i, j, k)).getBlock();
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
	public PathPoint getPathPointTo(Entity p_176161_1_) {
		return openPoint(MathHelper.floor_double(p_176161_1_.getEntityBoundingBox().minX),
				MathHelper.floor_double(p_176161_1_.getEntityBoundingBox().minY + 0.5),
				MathHelper.floor_double(p_176161_1_.getEntityBoundingBox().minZ));
	}

	@Override
	public PathPoint getPathPointToCoords(Entity p_176160_1_, double p_176160_2_, double p_176160_4_,
			double p_176160_6_) {
		return openPoint(MathHelper.floor_double(p_176160_2_ - (p_176160_1_.width / 2.0f)),
				MathHelper.floor_double(p_176160_4_ + 0.5),
				MathHelper.floor_double(p_176160_6_ - (p_176160_1_.width / 2.0f)));
	}

	private PathPoint getSafePoint(Entity p_176185_1_, int p_176185_2_, int p_176185_3_, int p_176185_4_) {
		int l = func_176186_b(p_176185_1_, p_176185_2_, p_176185_3_, p_176185_4_);
		return (l == -1) ? openPoint(p_176185_2_, p_176185_3_, p_176185_4_) : null;
	}
}

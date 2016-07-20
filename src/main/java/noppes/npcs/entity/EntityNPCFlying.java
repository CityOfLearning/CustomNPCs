//

//

package noppes.npcs.entity;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityNPCFlying extends EntityNPCInterface {
	public EntityNPCFlying(World world) {
		super(world);
	}

	@Override
	public boolean canFly() {
		return ai.movementType == 1;
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
		if (!canFly()) {
			super.fall(distance, damageMultiplier);
		}
	}

	@Override
	public boolean isOnLadder() {
		return false;
	}

	@Override
	public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
		if (!canFly()) {
			super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
			return;
		}
		if (isInWater()) {
			moveFlying(p_70612_1_, p_70612_2_, 0.02f);
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.800000011920929;
			motionY *= 0.800000011920929;
			motionZ *= 0.800000011920929;
		} else if (isInLava()) {
			moveFlying(p_70612_1_, p_70612_2_, 0.02f);
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.5;
			motionY *= 0.5;
			motionZ *= 0.5;
		} else {
			float f2 = 0.91f;
			if (onGround) {
				f2 = worldObj.getBlockState(new BlockPos(posX, getEntityBoundingBox().minY - 1.0, posZ))
						.getBlock().slipperiness * 0.91f;
			}
			float f3 = 0.16277136f / (f2 * f2 * f2);
			moveFlying(p_70612_1_, p_70612_2_, onGround ? (0.1f * f3) : 0.02f);
			f2 = 0.91f;
			if (onGround) {
				f2 = worldObj.getBlockState(new BlockPos(posX, getEntityBoundingBox().minY - 1.0, posZ))
						.getBlock().slipperiness * 0.91f;
			}
			moveEntity(motionX, motionY, motionZ);
			motionX *= f2;
			motionY *= f2;
			motionZ *= f2;
		}
		prevLimbSwingAmount = limbSwingAmount;
		double d1 = posX - prevPosX;
		double d2 = posZ - prevPosZ;
		float f4 = MathHelper.sqrt_double((d1 * d1) + (d2 * d2)) * 4.0f;
		if (f4 > 1.0f) {
			f4 = 1.0f;
		}
		limbSwingAmount += (f4 - limbSwingAmount) * 0.4f;
		limbSwing += limbSwingAmount;
	}

	@Override
	protected void updateFallState(double p_180433_1_, boolean p_180433_3_, Block block, BlockPos pos) {
		if (!canFly()) {
			super.updateFallState(p_180433_1_, p_180433_3_, block, pos);
		}
	}
}

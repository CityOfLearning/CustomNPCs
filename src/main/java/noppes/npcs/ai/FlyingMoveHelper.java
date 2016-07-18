//

//

package noppes.npcs.ai;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import noppes.npcs.entity.EntityNPCInterface;

public class FlyingMoveHelper extends EntityMoveHelper {
	private EntityNPCInterface entity;
	private int courseChangeCooldown;

	public FlyingMoveHelper(final EntityNPCInterface entity) {
		super(entity);
		this.entity = entity;
	}

	private boolean isNotColliding(final double p_179926_1_, final double p_179926_3_, final double p_179926_5_,
			final double p_179926_7_) {
		final double d4 = (p_179926_1_ - entity.posX) / p_179926_7_;
		final double d5 = (p_179926_3_ - entity.posY) / p_179926_7_;
		final double d6 = (p_179926_5_ - entity.posZ) / p_179926_7_;
		AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox();
		for (int i = 1; i < p_179926_7_; ++i) {
			axisalignedbb = axisalignedbb.offset(d4, d5, d6);
			if (!entity.worldObj.getCollidingBoundingBoxes(entity, axisalignedbb).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void onUpdateMoveHelper() {
		if (update) {
			final double d0 = posX - entity.posX;
			final double d2 = posY - entity.posY;
			final double d3 = posZ - entity.posZ;
			double d4 = (d0 * d0) + (d2 * d2) + (d3 * d3);
			if (courseChangeCooldown-- <= 0) {
				courseChangeCooldown += entity.getRNG().nextInt(5) + 2;
				d4 = MathHelper.sqrt_double(d4);
				if ((d4 > 1.0) && isNotColliding(posX, posY, posZ, d4)) {
					final double speed = entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
							.getAttributeValue() / 2.5;
					final EntityNPCInterface entity = this.entity;
					entity.motionX += (d0 / d4) * speed;
					final EntityNPCInterface entity2 = this.entity;
					entity2.motionY += (d2 / d4) * speed;
					final EntityNPCInterface entity3 = this.entity;
					entity3.motionZ += (d3 / d4) * speed;
					final EntityNPCInterface entity4 = this.entity;
					final EntityNPCInterface entity5 = this.entity;
					final float n = (-(float) Math.atan2(this.entity.motionX, this.entity.motionZ) * 180.0f)
							/ 3.1415927f;
					entity5.rotationYaw = n;
					entity4.renderYawOffset = n;
				} else {
					update = false;
				}
			}
		}
	}
}

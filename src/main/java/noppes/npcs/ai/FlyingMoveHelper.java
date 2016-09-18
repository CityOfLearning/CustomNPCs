
package noppes.npcs.ai;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import noppes.npcs.entity.EntityNPCInterface;

public class FlyingMoveHelper extends EntityMoveHelper {
	private EntityNPCInterface entity;
	private int courseChangeCooldown;

	public FlyingMoveHelper(EntityNPCInterface entity) {
		super(entity);
		this.entity = entity;
	}

	private boolean isNotColliding(double p_179926_1_, double p_179926_3_, double p_179926_5_, double p_179926_7_) {
		double d4 = (p_179926_1_ - entity.posX) / p_179926_7_;
		double d5 = (p_179926_3_ - entity.posY) / p_179926_7_;
		double d6 = (p_179926_5_ - entity.posZ) / p_179926_7_;
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
			double d0 = posX - entity.posX;
			double d2 = posY - entity.posY;
			double d3 = posZ - entity.posZ;
			double d4 = (d0 * d0) + (d2 * d2) + (d3 * d3);
			if (courseChangeCooldown-- <= 0) {
				courseChangeCooldown += entity.getRNG().nextInt(5) + 2;
				d4 = MathHelper.sqrt_double(d4);
				if ((d4 > 1.0) && isNotColliding(posX, posY, posZ, d4)) {
					double speed = entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()
							/ 2.5;
					EntityNPCInterface entity = this.entity;
					entity.motionX += (d0 / d4) * speed;
					EntityNPCInterface entity2 = this.entity;
					entity2.motionY += (d2 / d4) * speed;
					EntityNPCInterface entity3 = this.entity;
					entity3.motionZ += (d3 / d4) * speed;
					EntityNPCInterface entity4 = this.entity;
					EntityNPCInterface entity5 = this.entity;
					float n = (-(float) Math.atan2(this.entity.motionX, this.entity.motionZ) * 180.0f) / 3.1415927f;
					entity5.rotationYaw = n;
					entity4.renderYawOffset = n;
				} else {
					update = false;
				}
			}
		}
	}
}


package noppes.npcs.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIPounceTarget extends EntityAIBase {
	private EntityNPCInterface npc;
	private EntityLivingBase leapTarget;
	private float leapSpeed;

	public EntityAIPounceTarget(EntityNPCInterface leapingEntity) {
		leapSpeed = 1.3f;
		npc = leapingEntity;
		setMutexBits(4);
	}

	@Override
	public boolean continueExecuting() {
		return !npc.onGround;
	}

	public float getAngleForXYZ(double varX, double varY, double varZ, double horiDist) {
		float g = 0.1f;
		float var1 = leapSpeed * leapSpeed;
		double var2 = g * horiDist;
		double var3 = (g * horiDist * horiDist) + (2.0 * varY * var1);
		double var4 = (var1 * var1) - (g * var3);
		if (var4 < 0.0) {
			return 90.0f;
		}
		float var5 = var1 - MathHelper.sqrt_double(var4);
		float var6 = (float) ((Math.atan2(var5, var2) * 180.0) / 3.141592653589793);
		return var6;
	}

	@Override
	public boolean shouldExecute() {
		if (!npc.onGround) {
			return false;
		}
		leapTarget = npc.getAttackTarget();
		return (leapTarget != null) && npc.getEntitySenses().canSee(leapTarget) && !npc.isInRange(leapTarget, 4.0)
				&& npc.isInRange(leapTarget, 8.0) && (npc.getRNG().nextInt(5) == 0);
	}

	@Override
	public void startExecuting() {
		double varX = leapTarget.posX - npc.posX;
		double varY = leapTarget.getEntityBoundingBox().minY - npc.getEntityBoundingBox().minY;
		double varZ = leapTarget.posZ - npc.posZ;
		float varF = MathHelper.sqrt_double((varX * varX) + (varZ * varZ));
		float angle = getAngleForXYZ(varX, varY, varZ, varF);
		float yaw = (float) ((Math.atan2(varX, varZ) * 180.0) / 3.141592653589793);
		npc.motionX = MathHelper.sin((yaw / 180.0f) * 3.1415927f) * MathHelper.cos((angle / 180.0f) * 3.1415927f);
		npc.motionZ = MathHelper.cos((yaw / 180.0f) * 3.1415927f) * MathHelper.cos((angle / 180.0f) * 3.1415927f);
		npc.motionY = MathHelper.sin(((angle + 1.0f) / 180.0f) * 3.1415927f);
		EntityNPCInterface npc = this.npc;
		npc.motionX *= leapSpeed;
		EntityNPCInterface npc2 = this.npc;
		npc2.motionZ *= leapSpeed;
		EntityNPCInterface npc3 = this.npc;
		npc3.motionY *= leapSpeed;
		this.npc.playSound("metalheads:mob.manic.hurt", 0.7f, 1.0f);
	}
}

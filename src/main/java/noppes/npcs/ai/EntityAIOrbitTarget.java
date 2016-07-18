//

//

package noppes.npcs.ai;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIOrbitTarget extends EntityAIBase {
	private EntityNPCInterface npc;
	private EntityLivingBase targetEntity;
	private double movePosX;
	private double movePosY;
	private double movePosZ;
	private double speed;
	private float distance;
	private int delay;
	private float angle;
	private int direction;
	private float targetDistance;
	private boolean decay;
	private boolean canNavigate;
	private float decayRate;
	private int tick;

	public EntityAIOrbitTarget(final EntityNPCInterface par1EntityCreature, final double par2, final boolean par5) {
		delay = 0;
		angle = 0.0f;
		direction = 1;
		canNavigate = true;
		decayRate = 1.0f;
		tick = 0;
		npc = par1EntityCreature;
		speed = par2;
		decay = par5;
		setMutexBits(AiMutex.PASSIVE + AiMutex.LOOK);
	}

	@Override
	public boolean continueExecuting() {
		return targetEntity.isEntityAlive() && !npc.isInRange(targetEntity, distance / 2.0f)
				&& npc.isInRange(targetEntity, distance * 1.5) && !npc.isInWater() && canNavigate;
	}

	@Override
	public void resetTask() {
		npc.getNavigator().clearPathEntity();
		delay = 60;
		if (npc.getRangedTask() != null) {
			npc.getRangedTask().navOverride(false);
		}
	}

	@Override
	public boolean shouldExecute() {
		final int delay = this.delay - 1;
		this.delay = delay;
		if (delay > 0) {
			return false;
		}
		this.delay = 10;
		targetEntity = npc.getAttackTarget();
		if (targetEntity == null) {
			return false;
		}
		if (decay) {
			distance = npc.ai.getTacticalRange();
		} else {
			distance = npc.stats.ranged.getRange();
		}
		return !npc.isInRange(targetEntity, distance / 2.0f)
				&& ((npc.inventory.getProjectile() != null) || npc.isInRange(targetEntity, distance));
	}

	@Override
	public void startExecuting() {
		canNavigate = true;
		final Random random = npc.getRNG();
		direction = ((random.nextInt(10) > 5) ? 1 : -1);
		decayRate = random.nextFloat() + (distance / 16.0f);
		targetDistance = npc.getDistanceToEntity(targetEntity);
		final double d0 = npc.posX - targetEntity.posX;
		final double d2 = npc.posZ - targetEntity.posZ;
		angle = (float) ((Math.atan2(d2, d0) * 180.0) / 3.141592653589793);
		if (npc.getRangedTask() != null) {
			npc.getRangedTask().navOverride(true);
		}
	}

	@Override
	public void updateTask() {
		npc.getLookHelper().setLookPositionWithEntity(targetEntity, 30.0f, 30.0f);
		if (npc.getNavigator().noPath() && (tick >= 0) && npc.onGround && !npc.isInWater()) {
			final double d0 = targetDistance * MathHelper.cos((angle / 180.0f) * 3.1415927f);
			final double d2 = targetDistance * MathHelper.sin((angle / 180.0f) * 3.1415927f);
			movePosX = targetEntity.posX + d0;
			movePosY = targetEntity.getEntityBoundingBox().maxY;
			movePosZ = targetEntity.posZ + d2;
			npc.getNavigator().tryMoveToXYZ(movePosX, movePosY, movePosZ, speed);
			angle += 15.0f * direction;
			tick = MathHelper
					.ceiling_double_int(npc.getDistance(movePosX, movePosY, movePosZ) / (npc.getSpeed() / 20.0f));
			if (decay) {
				targetDistance -= decayRate;
			}
		}
		if (tick >= 0) {
			--tick;
		}
	}
}

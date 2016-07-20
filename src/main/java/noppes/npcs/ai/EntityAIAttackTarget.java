//

//

package noppes.npcs.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.BlockPos;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIAttackTarget extends EntityAIBase {
	private EntityNPCInterface npc;
	private EntityLivingBase entityTarget;
	private int attackTick;
	private PathEntity entityPathEntity;
	private int delayCounter;
	private boolean navOverride;

	public EntityAIAttackTarget(EntityNPCInterface par1EntityLiving) {
		navOverride = false;
		attackTick = 0;
		npc = par1EntityLiving;
		setMutexBits(navOverride ? AiMutex.PATHING : (AiMutex.LOOK + AiMutex.PASSIVE));
	}

	@Override
	public boolean continueExecuting() {
		entityTarget = npc.getAttackTarget();
		if ((entityTarget == null) || !entityTarget.isEntityAlive()) {
			return false;
		}
		if (!npc.isInRange(entityTarget, npc.stats.aggroRange)) {
			return false;
		}
		int melee = npc.stats.ranged.getMeleeRange();
		return ((melee <= 0) || npc.isInRange(entityTarget, melee))
				&& npc.isWithinHomeDistanceFromPosition(new BlockPos(entityTarget));
	}

	public void navOverride(boolean nav) {
		navOverride = nav;
		setMutexBits(navOverride ? AiMutex.PATHING : (AiMutex.LOOK + AiMutex.PASSIVE));
	}

	@Override
	public void resetTask() {
		entityPathEntity = null;
		entityTarget = null;
		npc.setAttackTarget(null);
		npc.getNavigator().clearPathEntity();
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase entitylivingbase = npc.getAttackTarget();
		if ((entitylivingbase == null) || !entitylivingbase.isEntityAlive()) {
			return false;
		}
		int melee = npc.stats.ranged.getMeleeRange();
		if ((npc.inventory.getProjectile() != null) && ((melee <= 0) || !npc.isInRange(entitylivingbase, melee))) {
			return false;
		}
		entityTarget = entitylivingbase;
		entityPathEntity = npc.getNavigator().getPathToEntityLiving(entitylivingbase);
		return entityPathEntity != null;
	}

	@Override
	public void startExecuting() {
		if (!navOverride) {
			npc.getNavigator().setPath(entityPathEntity, 1.3);
		}
		delayCounter = 0;
	}

	@Override
	public void updateTask() {
		npc.getLookHelper().setLookPositionWithEntity(entityTarget, 30.0f, 30.0f);
		if (!navOverride && (--delayCounter <= 0)) {
			delayCounter = 4 + npc.getRNG().nextInt(7);
			npc.getNavigator().tryMoveToEntityLiving(entityTarget, 1.2999999523162842);
		}
		attackTick = Math.max(attackTick - 1, 0);
		double y = entityTarget.posY;
		if (entityTarget.getEntityBoundingBox() != null) {
			y = entityTarget.getEntityBoundingBox().minY;
		}
		double distance = npc.getDistanceSq(entityTarget.posX, y, entityTarget.posZ);
		double range = (npc.stats.melee.getRange() * npc.stats.melee.getRange()) + entityTarget.width;
		double minRange = (npc.width * 2.0f * npc.width * 2.0f) + entityTarget.width;
		if (minRange > range) {
			range = minRange;
		}
		if ((distance <= range) && (npc.canSee(entityTarget) || (distance < minRange)) && (attackTick <= 0)) {
			attackTick = npc.stats.melee.getDelay();
			npc.swingItem();
			npc.attackEntityAsMob(entityTarget);
		}
	}
}

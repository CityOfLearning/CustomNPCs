
package noppes.npcs.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIRangedAttack extends EntityAIBase {
	private EntityNPCInterface npc;
	private EntityLivingBase attackTarget;
	private int rangedAttackTime;
	private int field_75318_f;
	private int field_70846_g;
	private boolean hasFired;
	private boolean navOverride;

	public EntityAIRangedAttack(IRangedAttackMob par1IRangedAttackMob) {
		rangedAttackTime = 0;
		field_75318_f = 0;
		field_70846_g = 0;
		hasFired = false;
		navOverride = false;
		if (!(par1IRangedAttackMob instanceof EntityLivingBase)) {
			throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
		}
		npc = (EntityNPCInterface) par1IRangedAttackMob;
		rangedAttackTime = npc.stats.ranged.getDelayMin() / 2;
		setMutexBits(navOverride ? AiMutex.PATHING : (AiMutex.LOOK + AiMutex.PASSIVE));
	}

	public boolean hasFired() {
		return hasFired;
	}

	public void navOverride(boolean nav) {
		navOverride = nav;
		setMutexBits(navOverride ? AiMutex.PATHING : (AiMutex.LOOK + AiMutex.PASSIVE));
	}

	@Override
	public void resetTask() {
		attackTarget = null;
		npc.setAttackTarget(null);
		npc.getNavigator().clearPathEntity();
		field_75318_f = 0;
		hasFired = false;
		rangedAttackTime = npc.stats.ranged.getDelayMin() / 2;
	}

	@Override
	public boolean shouldExecute() {
		attackTarget = npc.getAttackTarget();
		return (attackTarget != null) && attackTarget.isEntityAlive()
				&& npc.isInRange(attackTarget, npc.stats.aggroRange) && (npc.inventory.getProjectile() != null)
				&& ((npc.stats.ranged.getMeleeRange() < 1)
						|| !npc.isInRange(attackTarget, npc.stats.ranged.getMeleeRange()));
	}

	@Override
	public void updateTask() {
		npc.getLookHelper().setLookPositionWithEntity(attackTarget, 30.0f, 30.0f);
		double var1 = npc.getDistanceSq(attackTarget.posX, attackTarget.getEntityBoundingBox().minY, attackTarget.posZ);
		float range = npc.stats.ranged.getRange() * npc.stats.ranged.getRange();
		if (!navOverride && npc.ai.directLOS) {
			if (npc.getEntitySenses().canSee(attackTarget)) {
				++field_75318_f;
			} else {
				field_75318_f = 0;
			}
			int v = (npc.ai.tacticalVariant == 0) ? 20 : 5;
			if ((var1 <= range) && (field_75318_f >= v)) {
				npc.getNavigator().clearPathEntity();
			} else {
				npc.getNavigator().tryMoveToEntityLiving(attackTarget, 1.0);
			}
		}
		rangedAttackTime = Math.max(rangedAttackTime - 1, 0);
		if ((rangedAttackTime <= 0) && (var1 <= range)
				&& (npc.getEntitySenses().canSee(attackTarget) || (npc.stats.ranged.getFireType() == 2))) {
			if (field_70846_g++ <= npc.stats.ranged.getBurst()) {
				rangedAttackTime = npc.stats.ranged.getBurstDelay();
			} else {
				field_70846_g = 0;
				hasFired = true;
				rangedAttackTime = npc.stats.ranged.getDelayRNG();
			}
			if (field_70846_g > 1) {
				boolean indirect = false;
				switch (npc.stats.ranged.getFireType()) {
				case 1: {
					indirect = (var1 > (range / 2.0));
					break;
				}
				case 2: {
					indirect = !npc.getEntitySenses().canSee(attackTarget);
					break;
				}
				}
				npc.attackEntityWithRangedAttack(attackTarget, indirect ? 1.0f : 0.0f);
				if (npc.currentAnimation != 6) {
					npc.swingItem();
				}
			}
		}
	}
}

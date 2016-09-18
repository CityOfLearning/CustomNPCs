
package noppes.npcs.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAISprintToTarget extends EntityAIBase {
	private EntityNPCInterface npc;

	public EntityAISprintToTarget(EntityNPCInterface par1EntityLiving) {
		npc = par1EntityLiving;
		setMutexBits(AiMutex.PASSIVE);
	}

	@Override
	public boolean continueExecuting() {
		return npc.isEntityAlive() && npc.onGround && (npc.hurtTime <= 0) && (npc.motionX != 0.0)
				&& (npc.motionZ != 0.0);
	}

	@Override
	public void resetTask() {
		npc.setSprinting(false);
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase runTarget = npc.getAttackTarget();
		if ((runTarget == null) || npc.getNavigator().noPath()) {
			return false;
		}
		switch (npc.ai.onAttack) {
		case 0: {
			return !npc.isInRange(runTarget, 8.0) && npc.onGround;
		}
		case 2: {
			return npc.isInRange(runTarget, 7.0) && npc.onGround;
		}
		default: {
			return false;
		}
		}
	}

	@Override
	public void startExecuting() {
		npc.setSprinting(true);
	}
}

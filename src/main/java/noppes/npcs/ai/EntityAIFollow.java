
package noppes.npcs.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIFollow extends EntityAIBase {
	private EntityNPCInterface npc;
	private EntityLivingBase owner;
	public int updateTick;

	public EntityAIFollow(EntityNPCInterface npc) {
		updateTick = 0;
		this.npc = npc;
		setMutexBits(AiMutex.PASSIVE + AiMutex.LOOK);
	}

	public boolean canExcute() {
		return npc.isEntityAlive() && !npc.isAttacking() && ((owner = npc.getOwner()) != null)
				&& (npc.ai.animationType != 1);
	}

	@Override
	public boolean continueExecuting() {
		return !npc.getNavigator().noPath() && !npc.isInRange(owner, 2.0) && canExcute();
	}

	@Override
	public void resetTask() {
		owner = null;
		npc.getNavigator().clearPathEntity();
	}

	@Override
	public boolean shouldExecute() {
		return canExcute() && !npc.isInRange(owner, npc.followRange());
	}

	@Override
	public void startExecuting() {
		updateTick = 10;
	}

	@Override
	public void updateTask() {
		++updateTick;
		if (updateTick < 10) {
			return;
		}
		updateTick = 0;
		npc.getLookHelper().setLookPositionWithEntity(owner, 10.0f, npc.getVerticalFaceSpeed());
		double distance = npc.getDistanceSqToEntity(owner);
		double speed = 1.0 + (distance / 150.0);
		if (speed > 3.0) {
			speed = 3.0;
		}
		if (owner.isSprinting()) {
			speed += 0.5;
		}
		if (npc.getNavigator().tryMoveToEntityLiving(owner, speed) || npc.isInRange(owner, 16.0)) {
			return;
		}
		npc.tpTo(owner);
	}
}

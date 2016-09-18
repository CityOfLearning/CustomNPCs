
package noppes.npcs.ai;

import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIJob extends EntityAIBase {
	private EntityNPCInterface npc;

	public EntityAIJob(EntityNPCInterface npc) {
		this.npc = npc;
	}

	@Override
	public boolean continueExecuting() {
		return !npc.isKilled() && (npc.jobInterface != null) && npc.jobInterface.aiContinueExecute();
	}

	@Override
	public int getMutexBits() {
		if (npc.jobInterface == null) {
			return super.getMutexBits();
		}
		return npc.jobInterface.getMutexBits();
	}

	@Override
	public void resetTask() {
		if (npc.jobInterface != null) {
			npc.jobInterface.resetTask();
		}
	}

	@Override
	public boolean shouldExecute() {
		return !npc.isKilled() && (npc.jobInterface != null) && npc.jobInterface.aiShouldExecute();
	}

	@Override
	public void startExecuting() {
		npc.jobInterface.aiStartExecuting();
	}

	@Override
	public void updateTask() {
		npc.jobInterface.aiUpdateTask();
	}
}

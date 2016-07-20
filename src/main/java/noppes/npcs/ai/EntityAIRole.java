//

//

package noppes.npcs.ai;

import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIRole extends EntityAIBase {
	private EntityNPCInterface npc;

	public EntityAIRole(EntityNPCInterface npc) {
		this.npc = npc;
	}

	@Override
	public boolean continueExecuting() {
		return !npc.isKilled() && (npc.roleInterface != null) && npc.roleInterface.aiContinueExecute();
	}

	@Override
	public boolean shouldExecute() {
		return !npc.isKilled() && (npc.roleInterface != null) && npc.roleInterface.aiShouldExecute();
	}

	@Override
	public void startExecuting() {
		npc.roleInterface.aiStartExecuting();
	}

	@Override
	public void updateTask() {
		if (npc.roleInterface != null) {
			npc.roleInterface.aiUpdateTask();
		}
	}
}

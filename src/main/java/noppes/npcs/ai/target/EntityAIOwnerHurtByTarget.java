
package noppes.npcs.ai.target;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIOwnerHurtByTarget extends EntityAITarget {
	EntityNPCInterface npc;
	EntityLivingBase theOwnerAttacker;
	private int timer;

	public EntityAIOwnerHurtByTarget(EntityNPCInterface npc) {
		super(npc, false);
		this.npc = npc;
		setMutexBits(AiMutex.PASSIVE);
	}

	@Override
	public boolean shouldExecute() {
		if ((npc.roleInterface == null) || !npc.roleInterface.defendOwner()) {
			return false;
		}
		EntityLivingBase entitylivingbase = npc.getOwner();
		if (entitylivingbase == null) {
			return false;
		}
		theOwnerAttacker = entitylivingbase.getAITarget();
		int i = entitylivingbase.getRevengeTimer();
		return (i != timer) && this.isSuitableTarget(theOwnerAttacker, false);
	}

	@Override
	public void startExecuting() {
		taskOwner.setAttackTarget(theOwnerAttacker);
		EntityLivingBase entitylivingbase = npc.getOwner();
		if (entitylivingbase != null) {
			timer = entitylivingbase.getRevengeTimer();
		}
		super.startExecuting();
	}
}

//

//

package noppes.npcs.ai.target;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIOwnerHurtTarget extends EntityAITarget {
	EntityNPCInterface npc;
	EntityLivingBase theTarget;
	private int field_142050_e;

	public EntityAIOwnerHurtTarget(final EntityNPCInterface npc) {
		super(npc, false);
		this.npc = npc;
		setMutexBits(AiMutex.PASSIVE);
	}

	@Override
	public boolean shouldExecute() {
		if (!npc.isFollower() || (npc.roleInterface == null) || !npc.roleInterface.defendOwner()) {
			return false;
		}
		final EntityLivingBase entitylivingbase = npc.getOwner();
		if (entitylivingbase == null) {
			return false;
		}
		theTarget = entitylivingbase.getLastAttacker();
		final int i = entitylivingbase.getLastAttackerTime();
		return (i != field_142050_e) && this.isSuitableTarget(theTarget, false);
	}

	@Override
	public void startExecuting() {
		taskOwner.setAttackTarget(theTarget);
		final EntityLivingBase entitylivingbase = npc.getOwner();
		if (entitylivingbase != null) {
			field_142050_e = entitylivingbase.getLastAttackerTime();
		}
		super.startExecuting();
	}
}

//

//

package noppes.npcs.ai.target;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIClearTarget extends EntityAITarget {
	private EntityNPCInterface npc;
	private EntityLivingBase target;

	public EntityAIClearTarget(EntityNPCInterface npc) {
		super(npc, false);
		this.npc = npc;
	}

	@Override
	public void resetTask() {
		npc.getNavigator().clearPathEntity();
	}

	@Override
	public boolean shouldExecute() {
		target = taskOwner.getAttackTarget();
		return (target != null)
				&& (((target instanceof EntityPlayer) && ((EntityPlayer) target).capabilities.disableDamage)
						|| ((npc.getOwner() != null) && !npc.isInRange(npc.getOwner(), npc.stats.aggroRange * 2))
						|| !npc.isInRange(target, npc.stats.aggroRange * 2));
	}

	@Override
	public void startExecuting() {
		taskOwner.setAttackTarget((EntityLivingBase) null);
		if (target == taskOwner.getAITarget()) {
			taskOwner.setRevengeTarget((EntityLivingBase) null);
		}
		super.startExecuting();
	}
}

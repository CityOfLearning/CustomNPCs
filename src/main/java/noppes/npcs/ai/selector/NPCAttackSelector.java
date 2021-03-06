
package noppes.npcs.ai.selector;

import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import noppes.npcs.ai.jobs.JobGuard;
import noppes.npcs.entity.EntityNPCInterface;

public class NPCAttackSelector implements Predicate {
	private EntityNPCInterface npc;

	public NPCAttackSelector(EntityNPCInterface npc) {
		this.npc = npc;
	}

	@Override
	public boolean apply(Object ob) {
		return (ob instanceof EntityLivingBase) && isEntityApplicable((EntityLivingBase) ob);
	}

	public boolean isEntityApplicable(EntityLivingBase entity) {
		if (!entity.isEntityAlive() || (entity == npc) || !npc.isInRange(entity, npc.stats.aggroRange)
				|| (entity.getHealth() < 1.0f)) {
			return false;
		}
		if (npc.ai.directLOS && !npc.getEntitySenses().canSee(entity)) {
			return false;
		}
		if (!npc.ai.attackInvisible && entity.isPotionActive(Potion.invisibility) && npc.isInRange(entity, 3.0)) {
			return false;
		}
		if (npc.ai.returnToStart) {
			int allowedDistance = npc.stats.aggroRange * 2;
			if (npc.ai.getMovingType() == 1) {
				allowedDistance += npc.ai.walkingRange;
			}
			double distance = entity.getDistanceSq(npc.getStartXPos(), npc.getStartYPos(), npc.getStartZPos());
			if (npc.ai.getMovingType() == 2) {
				int[] arr = npc.ai.getCurrentMovingPath();
				distance = entity.getDistanceSq(arr[0], arr[1], arr[2]);
			}
			if (distance > (allowedDistance * allowedDistance)) {
				return false;
			}
		}
		if ((npc.advanced.job == 3) && ((JobGuard) npc.jobInterface).isEntityApplicable(entity)) {
			return true;
		}
		if (!(entity instanceof EntityPlayerMP)) {
			if (entity instanceof EntityNPCInterface) {
				if (((EntityNPCInterface) entity).isKilled()) {
					return false;
				}
				if (npc.advanced.attackOtherFactions) {
					return npc.faction.isAggressiveToNpc((EntityNPCInterface) entity);
				}
			}
			return false;
		}
		EntityPlayerMP player = (EntityPlayerMP) entity;
		if (!npc.faction.isAggressiveToPlayer(player) || player.capabilities.disableDamage) {
			return false;
		}
		if ((npc.ai.targetType == 1) && player.isSneaking()) {
			return npc.isInRange(player, npc.ai.specialAggroRange);
		}
		return (npc.advanced.job != 6);
	}
}

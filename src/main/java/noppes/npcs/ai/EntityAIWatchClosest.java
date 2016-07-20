//

//

package noppes.npcs.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIWatchClosest extends EntityAIBase {
	private EntityNPCInterface npc;
	protected Entity closestEntity;
	private float maxDistanceForPlayer;
	private int lookTime;
	private float chance;
	private Class watchedClass;

	public EntityAIWatchClosest(EntityNPCInterface par1EntityLiving, Class par2Class, float par3) {
		npc = par1EntityLiving;
		watchedClass = par2Class;
		maxDistanceForPlayer = par3;
		chance = 0.002f;
		setMutexBits(AiMutex.LOOK);
	}

	@Override
	public boolean continueExecuting() {
		return !npc.isInteracting() && !npc.isAttacking() && closestEntity.isEntityAlive() && npc.isEntityAlive()
				&& npc.isInRange(closestEntity, maxDistanceForPlayer) && (lookTime > 0);
	}

	@Override
	public void resetTask() {
		closestEntity = null;
	}

	@Override
	public boolean shouldExecute() {
		if ((npc.getRNG().nextFloat() >= chance) || npc.isInteracting()) {
			return false;
		}
		if (npc.getAttackTarget() != null) {
			closestEntity = npc.getAttackTarget();
		}
		if (watchedClass == EntityPlayer.class) {
			closestEntity = npc.worldObj.getClosestPlayerToEntity(npc, maxDistanceForPlayer);
		} else {
			closestEntity = npc.worldObj.findNearestEntityWithinAABB(watchedClass,
					npc.getEntityBoundingBox().expand(maxDistanceForPlayer, 3.0, maxDistanceForPlayer), (Entity) npc);
			if (closestEntity != null) {
				return npc.canSee(closestEntity);
			}
		}
		return closestEntity != null;
	}

	@Override
	public void startExecuting() {
		lookTime = 60 + npc.getRNG().nextInt(60);
	}

	@Override
	public void updateTask() {
		npc.getLookHelper().setLookPosition(closestEntity.posX, closestEntity.posY + closestEntity.getEyeHeight(),
				closestEntity.posZ, 10.0f, npc.getVerticalFaceSpeed());
		--lookTime;
	}
}

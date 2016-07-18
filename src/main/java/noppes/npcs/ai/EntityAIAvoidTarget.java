//

//

package noppes.npcs.ai;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.Vec3;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIAvoidTarget extends EntityAIBase {
	private EntityNPCInterface npc;
	private Entity closestLivingEntity;
	private float distanceFromEntity;
	private float health;
	private PathEntity entityPathEntity;
	private PathNavigate entityPathNavigate;
	private Class targetEntityClass;

	public EntityAIAvoidTarget(final EntityNPCInterface par1EntityNPC) {
		npc = par1EntityNPC;
		distanceFromEntity = npc.stats.aggroRange;
		health = npc.getHealth();
		entityPathNavigate = par1EntityNPC.getNavigator();
		setMutexBits(AiMutex.PASSIVE + AiMutex.LOOK);
	}

	@Override
	public boolean continueExecuting() {
		return !entityPathNavigate.noPath();
	}

	@Override
	public void resetTask() {
		closestLivingEntity = null;
		npc.setAttackTarget(null);
	}

	@Override
	public boolean shouldExecute() {
		final EntityLivingBase target = npc.getAttackTarget();
		if (target == null) {
			return false;
		}
		targetEntityClass = target.getClass();
		if (targetEntityClass == EntityPlayer.class) {
			closestLivingEntity = npc.worldObj.getClosestPlayerToEntity(npc, distanceFromEntity);
			if (closestLivingEntity == null) {
				return false;
			}
		} else {
			final List var1 = npc.worldObj.getEntitiesWithinAABB(targetEntityClass,
					npc.getEntityBoundingBox().expand(distanceFromEntity, 3.0, distanceFromEntity));
			if (var1.isEmpty()) {
				return false;
			}
			closestLivingEntity = (Entity) var1.get(0);
		}
		if (!npc.getEntitySenses().canSee(closestLivingEntity) && npc.ai.directLOS) {
			return false;
		}
		final Vec3 var2 = RandomPositionGeneratorAlt.findRandomTargetBlockAwayFrom(npc, 16, 7,
				new Vec3(closestLivingEntity.posX, closestLivingEntity.posY, closestLivingEntity.posZ));
		final boolean var3 = npc.inventory.getProjectile() == null;
		final boolean var4 = var3 ? (health == npc.getHealth())
				: ((npc.getRangedTask() != null) && !npc.getRangedTask().hasFired());
		if (var2 == null) {
			return false;
		}
		if (closestLivingEntity.getDistanceSq(var2.xCoord, var2.yCoord, var2.zCoord) < closestLivingEntity
				.getDistanceSqToEntity(npc)) {
			return false;
		}
		if ((npc.ai.tacticalVariant == 3) && var4) {
			return false;
		}
		entityPathEntity = entityPathNavigate.getPathToXYZ(var2.xCoord, var2.yCoord, var2.zCoord);
		return (entityPathEntity != null) && entityPathEntity.isDestinationSame(var2);
	}

	@Override
	public void startExecuting() {
		entityPathNavigate.setPath(entityPathEntity, 1.0);
	}

	@Override
	public void updateTask() {
		if (npc.isInRange(closestLivingEntity, 7.0)) {
			npc.getNavigator().setSpeed(1.2);
		} else {
			npc.getNavigator().setSpeed(1.0);
		}
		if ((npc.ai.tacticalVariant == 3) && (!npc.isInRange(closestLivingEntity, distanceFromEntity)
				|| npc.isInRange(closestLivingEntity, npc.ai.getTacticalRange()))) {
			health = npc.getHealth();
		}
	}
}

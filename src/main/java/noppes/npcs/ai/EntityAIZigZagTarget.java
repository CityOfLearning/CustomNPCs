//

//

package noppes.npcs.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIZigZagTarget extends EntityAIBase {
	private EntityNPCInterface npc;
	private EntityLivingBase targetEntity;
	private double movePosX;
	private double movePosY;
	private double movePosZ;
	private int entityPosX;
	private int entityPosY;
	private int entityPosZ;
	private double speed;
	private int ticks;

	public EntityAIZigZagTarget(EntityNPCInterface par1EntityCreature, double par2) {
		npc = par1EntityCreature;
		speed = par2;
		ticks = 0;
		setMutexBits(AiMutex.PASSIVE + AiMutex.LOOK);
	}

	@Override
	public void resetTask() {
		npc.getNavigator().clearPathEntity();
		ticks = 0;
	}

	@Override
	public boolean shouldExecute() {
		targetEntity = npc.getAttackTarget();
		return (targetEntity != null) && targetEntity.isEntityAlive()
				&& !npc.isInRange(targetEntity, npc.ai.getTacticalRange());
	}

	@Override
	public void updateTask() {
		npc.getLookHelper().setLookPositionWithEntity(targetEntity, 30.0f, 30.0f);
		if (ticks-- <= 0) {
			PathEntity pathentity = npc.getNavigator().getPathToEntityLiving(targetEntity);
			if ((pathentity != null) && (pathentity.getCurrentPathLength() >= npc.ai.getTacticalRange())) {
				PathPoint pathpoint = pathentity
						.getPathPointFromIndex(MathHelper.floor_double(npc.ai.getTacticalRange() / 2.0));
				entityPosX = pathpoint.xCoord;
				entityPosY = pathpoint.yCoord;
				entityPosZ = pathpoint.zCoord;
				Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(npc, npc.ai.getTacticalRange(), 3,
						new Vec3(entityPosX, entityPosY, entityPosZ));
				if (vec3 != null) {
					if (targetEntity.getDistanceSq(vec3.xCoord, vec3.yCoord, vec3.zCoord) < targetEntity
							.getDistanceSq(entityPosX, entityPosY, entityPosZ)) {
						movePosX = vec3.xCoord;
						movePosY = vec3.yCoord;
						movePosZ = vec3.zCoord;
					}
				} else {
					movePosX = pathpoint.xCoord;
					movePosY = pathpoint.yCoord;
					movePosZ = pathpoint.zCoord;
				}
				npc.getNavigator().tryMoveToXYZ(movePosX, movePosY, movePosZ, speed);
			} else {
				ticks = 10;
			}
		}
	}
}

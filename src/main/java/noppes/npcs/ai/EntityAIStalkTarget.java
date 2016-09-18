
package noppes.npcs.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIStalkTarget extends EntityAIBase {
	private EntityNPCInterface npc;
	private EntityLivingBase targetEntity;
	private Vec3 movePosition;
	private boolean overRide;
	private World theWorld;
	private int delay;
	private int tick;

	public EntityAIStalkTarget(EntityNPCInterface par1EntityCreature) {
		tick = 0;
		npc = par1EntityCreature;
		theWorld = par1EntityCreature.worldObj;
		overRide = false;
		delay = 0;
		setMutexBits(AiMutex.PASSIVE + AiMutex.LOOK);
	}

	private Vec3 findSecludedXYZ(int radius, boolean nearest) {
		Vec3 idealPos = null;
		double dist = targetEntity.getDistanceSqToEntity(npc);
		double u = 0.0;
		double v = 0.0;
		double w = 0.0;
		if (movePosition != null) {
			u = movePosition.xCoord;
			v = movePosition.yCoord;
			w = movePosition.zCoord;
		}
		for (int y = -2; y <= 2; ++y) {
			double k = MathHelper.floor_double(npc.getEntityBoundingBox().minY + y);
			for (int x = -radius; x <= radius; ++x) {
				double j = MathHelper.floor_double(npc.posX + x) + 0.5;
				for (int z = -radius; z <= radius; ++z) {
					double l = MathHelper.floor_double(npc.posZ + z) + 0.5;
					BlockPos pos = new BlockPos(j, k, l);
					if (isOpaque(pos) && !isOpaque(pos.up()) && !isOpaque(pos.up(2))) {
						Vec3 vec1 = new Vec3(targetEntity.posX, targetEntity.posY + targetEntity.getEyeHeight(),
								targetEntity.posZ);
						Vec3 vec2 = new Vec3(j, k + npc.getEyeHeight(), l);
						MovingObjectPosition movingobjectposition = theWorld.rayTraceBlocks(vec1, vec2);
						if (movingobjectposition != null) {
							boolean weight = !nearest || (targetEntity.getDistanceSq(j, k, l) <= dist);
							if (weight && ((j != u) || (k != v) || (l != w))) {
								idealPos = new Vec3(j, k, l);
								if (nearest) {
									dist = targetEntity.getDistanceSq(j, k, l);
								}
							}
						}
					}
				}
			}
		}
		return idealPos;
	}

	private Vec3 hideFromTarget() {
		for (int i = 1; i <= 8; ++i) {
			Vec3 vec = findSecludedXYZ(i, false);
			if (vec != null) {
				return vec;
			}
		}
		return null;
	}

	private boolean isLookingAway() {
		Vec3 vec3 = targetEntity.getLook(1.0f).normalize();
		Vec3 vec4 = new Vec3(npc.posX - targetEntity.posX, (npc.getEntityBoundingBox().minY + (npc.height / 2.0f))
				- (targetEntity.posY + targetEntity.getEyeHeight()), npc.posZ - targetEntity.posZ);
		vec4.lengthVector();
		vec4 = vec4.normalize();
		double d2 = vec3.dotProduct(vec4);
		return d2 < 0.6;
	}

	private boolean isOpaque(BlockPos pos) {
		return theWorld.getBlockState(pos).getBlock().isOpaqueCube();
	}

	@Override
	public void resetTask() {
		npc.getNavigator().clearPathEntity();
		if ((npc.getAttackTarget() == null) && (targetEntity != null)) {
			npc.setAttackTarget(targetEntity);
		}
		if (npc.getRangedTask() != null) {
			npc.getRangedTask().navOverride(false);
		}
	}

	@Override
	public boolean shouldExecute() {
		targetEntity = npc.getAttackTarget();
		return (targetEntity != null) && (tick-- <= 0) && !npc.isInRange(targetEntity, npc.ai.getTacticalRange());
	}

	private Vec3 stalkTarget() {
		for (int i = 8; i >= 1; --i) {
			Vec3 vec = findSecludedXYZ(i, true);
			if (vec != null) {
				return vec;
			}
		}
		return null;
	}

	@Override
	public void startExecuting() {
		if (npc.getRangedTask() != null) {
			npc.getRangedTask().navOverride(true);
		}
	}

	@Override
	public void updateTask() {
		npc.getLookHelper().setLookPositionWithEntity(targetEntity, 30.0f, 30.0f);
		if (npc.getNavigator().noPath() || overRide) {
			if (isLookingAway()) {
				movePosition = stalkTarget();
				if (movePosition != null) {
					npc.getNavigator().tryMoveToXYZ(movePosition.xCoord, movePosition.yCoord, movePosition.zCoord, 1.0);
					overRide = false;
				} else {
					tick = 100;
				}
			} else if (npc.canSee(targetEntity)) {
				movePosition = hideFromTarget();
				if (movePosition != null) {
					npc.getNavigator().tryMoveToXYZ(movePosition.xCoord, movePosition.yCoord, movePosition.zCoord,
							1.33);
					overRide = false;
				} else {
					tick = 100;
				}
			}
		}
		if (delay > 0) {
			--delay;
		}
		if (!isLookingAway() && npc.canSee(targetEntity) && (delay == 0)) {
			overRide = true;
			delay = 60;
		}
	}
}

//

//

package noppes.npcs.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import noppes.npcs.CustomNpcs;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIReturn extends EntityAIBase {
	public static final int MaxTotalTicks = 600;
	private final EntityNPCInterface npc;
	private int stuckTicks;
	private int totalTicks;
	private double endPosX;
	private double endPosY;
	private double endPosZ;
	private boolean wasAttacked;
	private double[] preAttackPos;
	private int stuckCount;

	public EntityAIReturn(final EntityNPCInterface npc) {
		stuckTicks = 0;
		totalTicks = 0;
		wasAttacked = false;
		stuckCount = 0;
		this.npc = npc;
		setMutexBits(AiMutex.PASSIVE);
	}

	@Override
	public boolean continueExecuting() {
		return !npc.isFollower() && !npc.isKilled() && !npc.isAttacking() && !npc.isVeryNearAssignedPlace()
				&& !npc.isInteracting() && (!npc.getNavigator().noPath() || !wasAttacked || isTooFar())
				&& (totalTicks <= 600);
	}

	private boolean isTooFar() {
		int allowedDistance = npc.stats.aggroRange * 2;
		if (npc.ai.getMovingType() == 1) {
			allowedDistance += npc.ai.walkingRange;
		}
		final double x = npc.posX - endPosX;
		final double z = npc.posX - endPosZ;
		return ((x * x) + (z * z)) > (allowedDistance * allowedDistance);
	}

	private void navigate(final boolean towards) {
		if (!wasAttacked) {
			endPosX = npc.getStartXPos();
			endPosY = npc.getStartYPos();
			endPosZ = npc.getStartZPos();
		} else {
			endPosX = preAttackPos[0];
			endPosY = preAttackPos[1];
			endPosZ = preAttackPos[2];
		}
		double posX = endPosX;
		double posY = endPosY;
		double posZ = endPosZ;
		final double range = npc.getDistance(posX, posY, posZ);
		if ((range > CustomNpcs.NpcNavRange) || towards) {
			int distance = (int) range;
			if (distance > CustomNpcs.NpcNavRange) {
				distance = CustomNpcs.NpcNavRange / 2;
			} else {
				distance /= 2;
			}
			if (distance > 2) {
				final Vec3 start = new Vec3(posX, posY, posZ);
				final Vec3 pos = RandomPositionGeneratorAlt.findRandomTargetBlockTowards(npc, distance,
						((distance / 2) > 7) ? 7 : (distance / 2), start);
				if (pos != null) {
					posX = pos.xCoord;
					posY = pos.yCoord;
					posZ = pos.zCoord;
				}
			}
		}
		npc.getNavigator().clearPathEntity();
		npc.getNavigator().tryMoveToXYZ(posX, posY, posZ, 1.0);
	}

	@Override
	public void resetTask() {
		wasAttacked = false;
		npc.getNavigator().clearPathEntity();
	}

	@Override
	public boolean shouldExecute() {
		if (npc.hasOwner() || !npc.ai.returnToStart || npc.isKilled() || !npc.getNavigator().noPath()
				|| npc.isInteracting()) {
			return false;
		}
		if ((npc.ai.findShelter == 0) && (!npc.worldObj.isDaytime() || npc.worldObj.isRaining())
				&& !npc.worldObj.provider.getHasNoSky()) {
			final BlockPos pos = new BlockPos(npc.getStartXPos(), npc.getStartYPos(), npc.getStartZPos());
			if (npc.worldObj.canSeeSky(pos) || (npc.worldObj.getLight(pos) <= 8)) {
				return false;
			}
		} else if ((npc.ai.findShelter == 1) && npc.worldObj.isDaytime()) {
			final BlockPos pos = new BlockPos(npc.getStartXPos(), npc.getStartYPos(), npc.getStartZPos());
			if (npc.worldObj.canSeeSky(pos)) {
				return false;
			}
		}
		if (npc.isAttacking()) {
			if (!wasAttacked) {
				wasAttacked = true;
				preAttackPos = new double[] { npc.posX, npc.posY, npc.posZ };
			}
			return false;
		}
		if (!npc.isAttacking() && wasAttacked) {
			return true;
		}
		if ((npc.ai.getMovingType() == 2)
				&& (npc.ai.getDistanceSqToPathPoint() < (CustomNpcs.NpcNavRange * CustomNpcs.NpcNavRange))) {
			return false;
		}
		if (npc.ai.getMovingType() == 1) {
			npc.getStartXPos();
			npc.getStartZPos();
			return !npc.isInRange(npc.getStartXPos(), -1.0, npc.getStartZPos(), npc.ai.walkingRange);
		}
		return (npc.ai.getMovingType() == 0) && !npc.isVeryNearAssignedPlace();
	}

	@Override
	public void startExecuting() {
		stuckTicks = 0;
		totalTicks = 0;
		stuckCount = 0;
		navigate(false);
	}

	@Override
	public void updateTask() {
		++totalTicks;
		if (totalTicks > 600) {
			npc.setPosition(endPosX, endPosY, endPosZ);
			npc.getNavigator().clearPathEntity();
			return;
		}
		if (stuckTicks > 0) {
			--stuckTicks;
		} else if (npc.getNavigator().noPath()) {
			++stuckCount;
			stuckTicks = 10;
			if (((totalTicks > 30) && wasAttacked && isTooFar()) || (stuckCount > 5)) {
				npc.setPosition(endPosX, endPosY, endPosZ);
				npc.getNavigator().clearPathEntity();
			} else {
				navigate((stuckCount % 2) == 1);
			}
		} else {
			stuckCount = 0;
		}
	}
}

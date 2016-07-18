//

//

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

public class EntityAIAmbushTarget extends EntityAIBase {
	private EntityNPCInterface npc;
	private EntityLivingBase targetEntity;
	private double shelterX;
	private double shelterY;
	private double shelterZ;
	private double movementSpeed;
	private double distance;
	private int delay;
	private World theWorld;

	public EntityAIAmbushTarget(final EntityNPCInterface par1EntityCreature, final double par2) {
		delay = 0;
		npc = par1EntityCreature;
		movementSpeed = par2;
		theWorld = par1EntityCreature.worldObj;
		setMutexBits(AiMutex.PASSIVE + AiMutex.LOOK);
	}

	@Override
	public boolean continueExecuting() {
		final boolean shouldHide = !npc.isInRange(targetEntity, distance);
		final boolean isSeen = npc.canSee(targetEntity);
		return (!npc.getNavigator().noPath() && shouldHide) || (!isSeen && (shouldHide || npc.ai.directLOS));
	}

	private Vec3 findHidingSpot() {
		npc.getRNG();
		Vec3 idealPos = null;
		for (int i = 1; i <= 8; ++i) {
			for (int y = -2; y <= 2; ++y) {
				final double k = MathHelper.floor_double(npc.getEntityBoundingBox().minY + y);
				for (int x = -i; x <= i; ++x) {
					final double j = MathHelper.floor_double(npc.posX + x) + 0.5;
					for (int z = -i; z <= i; ++z) {
						final double l = MathHelper.floor_double(npc.posZ + z) + 0.5;
						if (isOpaque((int) j, (int) k, (int) l) && !isOpaque((int) j, (int) k + 1, (int) l)
								&& isOpaque((int) j, (int) k + 2, (int) l)) {
							final Vec3 vec1 = new Vec3(targetEntity.posX,
									targetEntity.posY + targetEntity.getEyeHeight(), targetEntity.posZ);
							final Vec3 vec2 = new Vec3(j, k + npc.getEyeHeight(), l);
							final MovingObjectPosition movingobjectposition = theWorld.rayTraceBlocks(vec1, vec2);
							if ((movingobjectposition != null) && (shelterX != j) && (shelterY != k)
									&& (shelterZ != l)) {
								idealPos = new Vec3(j, k, l);
							}
						}
					}
				}
			}
			if (idealPos != null) {
				return idealPos;
			}
		}
		delay = 60;
		return null;
	}

	private boolean isOpaque(final int x, final int y, final int z) {
		return theWorld.getBlockState(new BlockPos(x, y, z)).getBlock().isOpaqueCube();
	}

	@Override
	public void resetTask() {
		npc.getNavigator().clearPathEntity();
		if ((npc.getAttackTarget() == null) && (targetEntity != null)) {
			npc.setAttackTarget(targetEntity);
		}
		if (!npc.isInRange(targetEntity, distance)) {
			delay = 60;
		}
	}

	@Override
	public boolean shouldExecute() {
		targetEntity = npc.getAttackTarget();
		distance = npc.ai.getTacticalRange();
		if ((targetEntity == null) || npc.isInRange(targetEntity, distance) || !npc.canSee(targetEntity)
				|| (delay-- > 0)) {
			return false;
		}
		final Vec3 vec3 = findHidingSpot();
		if (vec3 == null) {
			delay = 10;
			return false;
		}
		shelterX = vec3.xCoord;
		shelterY = vec3.yCoord;
		shelterZ = vec3.zCoord;
		return true;
	}

	@Override
	public void startExecuting() {
		npc.getNavigator().tryMoveToXYZ(shelterX, shelterY, shelterZ, movementSpeed);
	}

	@Override
	public void updateTask() {
		npc.getLookHelper().setLookPositionWithEntity(targetEntity, 30.0f, 30.0f);
	}
}

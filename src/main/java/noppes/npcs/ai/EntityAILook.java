//

//

package noppes.npcs.ai;

import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAILook extends EntityAIBase {
	private final EntityNPCInterface npc;
	private int idle;
	private double lookX;
	private double lookZ;
	boolean rotatebody;
	private boolean forced;
	private Entity forcedEntity;

	public EntityAILook(final EntityNPCInterface npc) {
		idle = 0;
		forced = false;
		forcedEntity = null;
		this.npc = npc;
		setMutexBits(AiMutex.LOOK);
	}

	@Override
	public void resetTask() {
		rotatebody = false;
		forced = false;
		forcedEntity = null;
	}

	public void rotate(final Entity entity) {
		forced = true;
		forcedEntity = entity;
	}

	public void rotate(final int degrees) {
		forced = true;
		final EntityNPCInterface npc = this.npc;
		final EntityNPCInterface npc2 = this.npc;
		final EntityNPCInterface npc3 = this.npc;
		final float rotationYawHead = degrees;
		npc3.renderYawOffset = rotationYawHead;
		npc2.rotationYaw = rotationYawHead;
		npc.rotationYawHead = rotationYawHead;
	}

	@Override
	public boolean shouldExecute() {
		return !npc.isAttacking() && npc.getNavigator().noPath() && !npc.isPlayerSleeping() && npc.isEntityAlive();
	}

	@Override
	public void startExecuting() {
		rotatebody = ((npc.ai.getStandingType() == 0) || (npc.ai.getStandingType() == 3));
	}

	@Override
	public void updateTask() {
		Entity lookat = null;
		if (forced && (forcedEntity != null)) {
			lookat = forcedEntity;
		} else if (npc.isInteracting()) {
			final Iterator<EntityLivingBase> ita = npc.interactingEntities.iterator();
			double closestDistance = 12.0;
			while (ita.hasNext()) {
				final EntityLivingBase entity = ita.next();
				final double distance = entity.getDistanceSqToEntity(npc);
				if (distance < closestDistance) {
					closestDistance = entity.getDistanceSqToEntity(npc);
					lookat = entity;
				} else {
					if (distance <= 12.0) {
						continue;
					}
					ita.remove();
				}
			}
		} else if (npc.ai.getStandingType() == 2) {
			lookat = npc.worldObj.getClosestPlayerToEntity(npc, 16.0);
		}
		if (lookat != null) {
			npc.getLookHelper().setLookPositionWithEntity(lookat, 10.0f, npc.getVerticalFaceSpeed());
			return;
		}
		if (rotatebody) {
			if ((idle == 0) && (npc.getRNG().nextFloat() < 0.004f)) {
				double var1 = 6.283185307179586 * npc.getRNG().nextDouble();
				if (npc.ai.getStandingType() == 3) {
					var1 = (0.017453292519943295 * npc.ai.orientation) + 0.6283185307179586
							+ (1.8849555921538759 * npc.getRNG().nextDouble());
				}
				lookX = Math.cos(var1);
				lookZ = Math.sin(var1);
				idle = 20 + npc.getRNG().nextInt(20);
			}
			if (idle > 0) {
				--idle;
				npc.getLookHelper().setLookPosition(npc.posX + lookX, npc.posY + npc.getEyeHeight(), npc.posZ + lookZ,
						10.0f, npc.getVerticalFaceSpeed());
			}
		}
		if ((npc.ai.getStandingType() == 1) && !forced) {
			final EntityNPCInterface npc = this.npc;
			final EntityNPCInterface npc2 = this.npc;
			final EntityNPCInterface npc3 = this.npc;
			final float rotationYawHead = this.npc.ai.orientation;
			npc3.renderYawOffset = rotationYawHead;
			npc2.rotationYaw = rotationYawHead;
			npc.rotationYawHead = rotationYawHead;
		}
	}
}

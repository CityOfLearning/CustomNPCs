//

//

package noppes.npcs.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.Vec3;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIDodgeShoot extends EntityAIBase {
	private EntityNPCInterface entity;
	private double xPosition;
	private double yPosition;
	private double zPosition;

	public EntityAIDodgeShoot(final EntityNPCInterface par1EntityNPCInterface) {
		entity = par1EntityNPCInterface;
		setMutexBits(AiMutex.PASSIVE);
	}

	@Override
	public boolean continueExecuting() {
		return !entity.getNavigator().noPath();
	}

	@Override
	public boolean shouldExecute() {
		final EntityLivingBase var1 = entity.getAttackTarget();
		if ((var1 == null) || !var1.isEntityAlive()) {
			return false;
		}
		if (entity.inventory.getProjectile() == null) {
			return false;
		}
		if (entity.getRangedTask() == null) {
			return false;
		}
		final Vec3 vec = entity.getRangedTask().hasFired() ? RandomPositionGeneratorAlt.findRandomTarget(entity, 4, 1)
				: null;
		if (vec == null) {
			return false;
		}
		xPosition = vec.xCoord;
		yPosition = vec.yCoord;
		zPosition = vec.zCoord;
		return true;
	}

	@Override
	public void startExecuting() {
		entity.getNavigator().tryMoveToXYZ(xPosition, yPosition, zPosition, 1.2);
	}

	@Override
	public void updateTask() {
		if (entity.getAttackTarget() != null) {
			entity.getLookHelper().setLookPositionWithEntity(entity.getAttackTarget(), 30.0f, 30.0f);
		}
	}
}

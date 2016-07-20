//

//

package noppes.npcs.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.Vec3;
import noppes.npcs.constants.AiMutex;

public class EntityAIPanic extends EntityAIBase {
	private EntityCreature theEntityCreature;
	private float speed;
	private double randPosX;
	private double randPosY;
	private double randPosZ;

	public EntityAIPanic(EntityCreature par1EntityCreature, float par2) {
		theEntityCreature = par1EntityCreature;
		speed = par2;
		setMutexBits(AiMutex.PASSIVE);
	}

	@Override
	public boolean continueExecuting() {
		return (theEntityCreature.getAttackTarget() != null) && !theEntityCreature.getNavigator().noPath();
	}

	@Override
	public boolean shouldExecute() {
		if ((theEntityCreature.getAttackTarget() == null) && !theEntityCreature.isBurning()) {
			return false;
		}
		Vec3 var1 = RandomPositionGeneratorAlt.findRandomTarget(theEntityCreature, 5, 4);
		if (var1 == null) {
			return false;
		}
		randPosX = var1.xCoord;
		randPosY = var1.yCoord;
		randPosZ = var1.zCoord;
		return true;
	}

	@Override
	public void startExecuting() {
		theEntityCreature.getNavigator().tryMoveToXYZ(randPosX, randPosY, randPosZ, speed);
	}
}

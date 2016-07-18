//

//

package noppes.npcs.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigateGround;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIWaterNav extends EntityAIBase {
	private EntityNPCInterface theEntity;

	public EntityAIWaterNav(final EntityNPCInterface par1EntityNPCInterface) {
		theEntity = par1EntityNPCInterface;
		((PathNavigateGround) par1EntityNPCInterface.getNavigator()).setCanSwim(true);
	}

	@Override
	public boolean shouldExecute() {
		if (theEntity.isInWater() || theEntity.isInLava()) {
			if (theEntity.ai.canSwim) {
				return true;
			}
			if (theEntity.isCollidedHorizontally) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void updateTask() {
		if (theEntity.getRNG().nextFloat() < 0.8f) {
			theEntity.getJumpHelper().setJumping();
		}
	}
}

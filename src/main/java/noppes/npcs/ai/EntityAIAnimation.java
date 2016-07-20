//

//

package noppes.npcs.ai;

import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIAnimation extends EntityAIBase {
	public static int getWalkingAnimationGuiIndex(int animation) {
		if (animation == 4) {
			return 1;
		}
		if (animation == 6) {
			return 2;
		}
		if (animation == 5) {
			return 3;
		}
		if (animation == 7) {
			return 4;
		}
		if (animation == 8) {
			return 5;
		}
		return 0;
	}

	public static boolean isWalkingAnimation(int animation) {
		return getWalkingAnimationGuiIndex(animation) != 0;
	}

	private EntityNPCInterface npc;
	private boolean isAttacking;
	private boolean isDead;
	private boolean isAtStartpoint;

	private boolean hasPath;

	public int temp;

	public EntityAIAnimation(EntityNPCInterface npc) {
		isAttacking = false;
		isDead = false;
		isAtStartpoint = false;
		hasPath = false;
		temp = 0;
		this.npc = npc;
	}

	private boolean hasNavigation() {
		return isAttacking || (npc.ai.returnToStart && !isAtStartpoint && !npc.isFollower()) || hasPath;
	}

	@Override
	public void resetTask() {
	}

	private void setAnimation(int animation) {
		npc.setCurrentAnimation(animation);
		npc.updateHitbox();
		npc.setPosition(npc.posX, npc.posY, npc.posZ);
	}

	@Override
	public boolean shouldExecute() {
		isDead = !npc.isEntityAlive();
		if (isDead) {
			return npc.currentAnimation != 2;
		}
		if (npc.stats.ranged.getHasAimAnimation() && npc.isAttacking()) {
			return npc.currentAnimation != 6;
		}
		hasPath = !npc.getNavigator().noPath();
		isAttacking = npc.isAttacking();
		isAtStartpoint = (npc.ai.returnToStart && npc.isVeryNearAssignedPlace());
		if (temp != 0) {
			if (!hasNavigation()) {
				return npc.currentAnimation != temp;
			}
			temp = 0;
		}
		if (hasNavigation() && !isWalkingAnimation(npc.currentAnimation)) {
			return npc.currentAnimation != 0;
		}
		return npc.currentAnimation != npc.ai.animationType;
	}

	@Override
	public void updateTask() {
		if (npc.stats.ranged.getHasAimAnimation() && npc.isAttacking()) {
			setAnimation(6);
			return;
		}
		int type = npc.ai.animationType;
		if (isDead) {
			type = 2;
		} else if (!isWalkingAnimation(npc.ai.animationType) && hasNavigation()) {
			type = 0;
		} else if (temp != 0) {
			if (hasNavigation()) {
				temp = 0;
			} else {
				type = temp;
			}
		}
		setAnimation(type);
	}
}

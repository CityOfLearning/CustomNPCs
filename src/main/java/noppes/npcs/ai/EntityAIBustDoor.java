//

//

package noppes.npcs.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIDoorInteract;

public class EntityAIBustDoor extends EntityAIDoorInteract {
	private int breakingTime;
	private int previousBreakProgress;

	public EntityAIBustDoor(EntityLiving par1EntityLiving) {
		super(par1EntityLiving);
		previousBreakProgress = -1;
	}

	@Override
	public boolean continueExecuting() {
		double var1 = theEntity.getDistanceSq(doorPosition);
		return (breakingTime <= 240) && !BlockDoor.isOpen(theEntity.worldObj, doorPosition) && (var1 < 4.0);
	}

	@Override
	public void resetTask() {
		super.resetTask();
		theEntity.worldObj.sendBlockBreakProgress(theEntity.getEntityId(), doorPosition, -1);
	}

	@Override
	public boolean shouldExecute() {
		return super.shouldExecute() && !BlockDoor.isOpen(theEntity.worldObj, doorPosition);
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		breakingTime = 0;
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (theEntity.getRNG().nextInt(20) == 0) {
			theEntity.worldObj.playAuxSFX(1010, doorPosition, 0);
			theEntity.swingItem();
		}
		++breakingTime;
		int var1 = (int) ((breakingTime / 240.0f) * 10.0f);
		if (var1 != previousBreakProgress) {
			theEntity.worldObj.sendBlockBreakProgress(theEntity.getEntityId(), doorPosition, var1);
			previousBreakProgress = var1;
		}
		if (breakingTime == 240) {
			theEntity.worldObj.setBlockToAir(doorPosition);
			theEntity.worldObj.playAuxSFX(1012, doorPosition, 0);
			theEntity.worldObj.playAuxSFX(2001, doorPosition, Block.getIdFromBlock(doorBlock));
		}
	}
}

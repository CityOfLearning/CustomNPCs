package noppes.npcs.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIDoorInteract;

public class EntityAIBustDoor extends EntityAIDoorInteract {

   private int breakingTime;
   private int previousBreakProgress = -1;


   public EntityAIBustDoor(EntityLiving par1EntityLiving) {
      super(par1EntityLiving);
   }

   public boolean shouldExecute() {
      return !super.shouldExecute()?false:!BlockDoor.isOpen(this.theEntity.worldObj, this.doorPosition);
   }

   public void startExecuting() {
      super.startExecuting();
      this.breakingTime = 0;
   }

   public boolean continueExecuting() {
      double var1 = this.theEntity.getDistanceSq(this.doorPosition);
      return this.breakingTime <= 240 && !BlockDoor.isOpen(this.theEntity.worldObj, this.doorPosition) && var1 < 4.0D;
   }

   public void resetTask() {
      super.resetTask();
      this.theEntity.worldObj.sendBlockBreakProgress(this.theEntity.getEntityId(), this.doorPosition, -1);
   }

   public void updateTask() {
      super.updateTask();
      if(this.theEntity.getRNG().nextInt(20) == 0) {
         this.theEntity.worldObj.playAuxSFX(1010, this.doorPosition, 0);
         this.theEntity.swingItem();
      }

      ++this.breakingTime;
      int var1 = (int)((float)this.breakingTime / 240.0F * 10.0F);
      if(var1 != this.previousBreakProgress) {
         this.theEntity.worldObj.sendBlockBreakProgress(this.theEntity.getEntityId(), this.doorPosition, var1);
         this.previousBreakProgress = var1;
      }

      if(this.breakingTime == 240) {
         this.theEntity.worldObj.setBlockToAir(this.doorPosition);
         this.theEntity.worldObj.playAuxSFX(1012, this.doorPosition, 0);
         this.theEntity.worldObj.playAuxSFX(2001, this.doorPosition, Block.getIdFromBlock(this.doorBlock));
      }

   }
}

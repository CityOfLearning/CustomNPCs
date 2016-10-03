package noppes.npcs.entity;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import noppes.npcs.entity.EntityNPCInterface;

public abstract class EntityNPCFlying extends EntityNPCInterface {

   public EntityNPCFlying(World world) {
      super(world);
   }

   public boolean canFly() {
      return this.ai.movementType == 1;
   }

   public void fall(float distance, float damageMultiplier) {
      if(!this.canFly()) {
         super.fall(distance, damageMultiplier);
      }

   }

   protected void updateFallState(double p_180433_1_, boolean p_180433_3_, Block block, BlockPos pos) {
      if(!this.canFly()) {
         super.updateFallState(p_180433_1_, p_180433_3_, block, pos);
      }

   }

   public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
      if(!this.canFly()) {
         super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
      } else {
         if(this.isInWater()) {
            this.moveFlying(p_70612_1_, p_70612_2_, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
         } else if(this.isInLava()) {
            this.moveFlying(p_70612_1_, p_70612_2_, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
         } else {
            float d1 = 0.91F;
            if(this.onGround) {
               d1 = this.worldObj.getBlockState(new BlockPos(this.posX, this.getEntityBoundingBox().minY - 1.0D, this.posZ)).getBlock().slipperiness * 0.91F;
            }

            float f3 = 0.16277136F / (d1 * d1 * d1);
            this.moveFlying(p_70612_1_, p_70612_2_, this.onGround?0.1F * f3:0.02F);
            d1 = 0.91F;
            if(this.onGround) {
               d1 = this.worldObj.getBlockState(new BlockPos(this.posX, this.getEntityBoundingBox().minY - 1.0D, this.posZ)).getBlock().slipperiness * 0.91F;
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double)d1;
            this.motionY *= (double)d1;
            this.motionZ *= (double)d1;
         }

         this.prevLimbSwingAmount = this.limbSwingAmount;
         double d11 = this.posX - this.prevPosX;
         double d0 = this.posZ - this.prevPosZ;
         float f4 = MathHelper.sqrt_double(d11 * d11 + d0 * d0) * 4.0F;
         if(f4 > 1.0F) {
            f4 = 1.0F;
         }

         this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
         this.limbSwing += this.limbSwingAmount;
      }
   }

   public boolean isOnLadder() {
      return false;
   }
}

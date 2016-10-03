package noppes.npcs.ai;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import noppes.npcs.entity.EntityNPCInterface;

public class FlyingMoveHelper extends EntityMoveHelper {

   private EntityNPCInterface entity;
   private int courseChangeCooldown;


   public FlyingMoveHelper(EntityNPCInterface entity) {
      super(entity);
      this.entity = entity;
   }

   public void onUpdateMoveHelper() {
      if(this.update) {
         double d0 = this.posX - this.entity.posX;
         double d1 = this.posY - this.entity.posY;
         double d2 = this.posZ - this.entity.posZ;
         double d3 = d0 * d0 + d1 * d1 + d2 * d2;
         if(this.courseChangeCooldown-- <= 0) {
            this.courseChangeCooldown += this.entity.getRNG().nextInt(5) + 2;
            d3 = (double)MathHelper.sqrt_double(d3);
            if(d3 > 1.0D && this.isNotColliding(this.posX, this.posY, this.posZ, d3)) {
               double speed = this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue() / 2.5D;
               this.entity.motionX += d0 / d3 * speed;
               this.entity.motionY += d1 / d3 * speed;
               this.entity.motionZ += d2 / d3 * speed;
               this.entity.renderYawOffset = this.entity.rotationYaw = -((float)Math.atan2(this.entity.motionX, this.entity.motionZ)) * 180.0F / 3.1415927F;
            } else {
               this.update = false;
            }
         }
      }

   }

   private boolean isNotColliding(double p_179926_1_, double p_179926_3_, double p_179926_5_, double p_179926_7_) {
      double d4 = (p_179926_1_ - this.entity.posX) / p_179926_7_;
      double d5 = (p_179926_3_ - this.entity.posY) / p_179926_7_;
      double d6 = (p_179926_5_ - this.entity.posZ) / p_179926_7_;
      AxisAlignedBB axisalignedbb = this.entity.getEntityBoundingBox();

      for(int i = 1; (double)i < p_179926_7_; ++i) {
         axisalignedbb = axisalignedbb.offset(d4, d5, d6);
         if(!this.entity.worldObj.getCollidingBoundingBoxes(this.entity, axisalignedbb).isEmpty()) {
            return false;
         }
      }

      return true;
   }
}

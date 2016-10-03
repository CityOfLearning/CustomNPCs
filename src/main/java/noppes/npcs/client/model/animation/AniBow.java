package noppes.npcs.client.model.animation;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import noppes.npcs.entity.EntityNPCInterface;

public class AniBow {

   public static void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity, ModelBiped model) {
      float ticks = (float)(entity.ticksExisted - ((EntityNPCInterface)entity).animationStart) / 10.0F;
      if(ticks > 1.0F) {
         ticks = 1.0F;
      }

      model.bipedBody.rotateAngleX = ticks;
      model.bipedHead.rotateAngleX = ticks;
      model.bipedLeftArm.rotateAngleX = ticks;
      model.bipedRightArm.rotateAngleX = ticks;
      model.bipedBody.rotationPointZ = -ticks * 10.0F;
      model.bipedBody.rotationPointY = ticks * 6.0F;
      model.bipedHead.rotationPointZ = -ticks * 10.0F;
      model.bipedHead.rotationPointY = ticks * 6.0F;
      model.bipedLeftArm.rotationPointZ = -ticks * 10.0F;
      model.bipedLeftArm.rotationPointY += ticks * 6.0F;
      model.bipedRightArm.rotationPointZ = -ticks * 10.0F;
      model.bipedRightArm.rotationPointY += ticks * 6.0F;
   }
}

package noppes.npcs.client.model.animation;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;

public class AniWaving {

   public static void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity, ModelBiped base) {
      base.bipedRightArm.rotateAngleX = -0.1F;
      base.bipedRightArm.rotateAngleY = 0.0F;
      base.bipedRightArm.rotateAngleZ = (float)(2.141592653589793D - Math.sin((double)((float)entity.ticksExisted * 0.27F)) * 0.5D);
   }
}

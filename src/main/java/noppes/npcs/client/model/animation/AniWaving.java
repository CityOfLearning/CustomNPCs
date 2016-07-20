//

//

package noppes.npcs.client.model.animation;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;

public class AniWaving {
	public static void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6,
			Entity entity, ModelBiped base) {
		base.bipedRightArm.rotateAngleX = -0.1f;
		base.bipedRightArm.rotateAngleY = 0.0f;
		base.bipedRightArm.rotateAngleZ = (float) (2.141592653589793 - (Math.sin(entity.ticksExisted * 0.27f) * 0.5));
	}
}

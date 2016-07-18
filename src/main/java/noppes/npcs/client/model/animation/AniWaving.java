//

//

package noppes.npcs.client.model.animation;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;

public class AniWaving {
	public static void setRotationAngles(final float par1, final float par2, final float par3, final float par4,
			final float par5, final float par6, final Entity entity, final ModelBiped base) {
		base.bipedRightArm.rotateAngleX = -0.1f;
		base.bipedRightArm.rotateAngleY = 0.0f;
		base.bipedRightArm.rotateAngleZ = (float) (2.141592653589793 - (Math.sin(entity.ticksExisted * 0.27f) * 0.5));
	}
}

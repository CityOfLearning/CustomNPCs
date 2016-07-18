//

//

package noppes.npcs.client.model.animation;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import noppes.npcs.entity.EntityNPCInterface;

public class AniBow {
	public static void setRotationAngles(final float par1, final float par2, final float par3, final float par4,
			final float par5, final float par6, final Entity entity, final ModelBiped model) {
		float ticks = (entity.ticksExisted - ((EntityNPCInterface) entity).animationStart) / 10.0f;
		if (ticks > 1.0f) {
			ticks = 1.0f;
		}
		model.bipedBody.rotateAngleX = ticks;
		model.bipedHead.rotateAngleX = ticks;
		model.bipedLeftArm.rotateAngleX = ticks;
		model.bipedRightArm.rotateAngleX = ticks;
		model.bipedBody.rotationPointZ = -ticks * 10.0f;
		model.bipedBody.rotationPointY = ticks * 6.0f;
		model.bipedHead.rotationPointZ = -ticks * 10.0f;
		model.bipedHead.rotationPointY = ticks * 6.0f;
		model.bipedLeftArm.rotationPointZ = -ticks * 10.0f;
		final ModelRenderer bipedLeftArm = model.bipedLeftArm;
		bipedLeftArm.rotationPointY += ticks * 6.0f;
		model.bipedRightArm.rotationPointZ = -ticks * 10.0f;
		final ModelRenderer bipedRightArm = model.bipedRightArm;
		bipedRightArm.rotationPointY += ticks * 6.0f;
	}
}

//

//

package noppes.npcs.client.model.animation;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class AniDancing {
	public static void setRotationAngles(final float par1, final float par2, final float par3, final float par4,
			final float par5, final float par6, final Entity entity, final ModelBiped model) {
		final float dancing = entity.ticksExisted / 4.0f;
		final float x = (float) Math.sin(dancing);
		final float y = (float) Math.abs(Math.cos(dancing));
		final ModelRenderer bipedHeadwear = model.bipedHeadwear;
		final ModelRenderer bipedHead = model.bipedHead;
		final float n = x * 0.75f;
		bipedHead.rotationPointX = n;
		bipedHeadwear.rotationPointX = n;
		final ModelRenderer bipedHeadwear2 = model.bipedHeadwear;
		final ModelRenderer bipedHead2 = model.bipedHead;
		final float n2 = (y * 1.25f) - 0.02f;
		bipedHead2.rotationPointY = n2;
		bipedHeadwear2.rotationPointY = n2;
		final ModelRenderer bipedHeadwear3 = model.bipedHeadwear;
		final ModelRenderer bipedHead3 = model.bipedHead;
		final float n3 = -y * 0.75f;
		bipedHead3.rotationPointZ = n3;
		bipedHeadwear3.rotationPointZ = n3;
		final ModelRenderer bipedLeftArm = model.bipedLeftArm;
		bipedLeftArm.rotationPointX += x * 0.25f;
		final ModelRenderer bipedLeftArm2 = model.bipedLeftArm;
		bipedLeftArm2.rotationPointY += y * 1.25f;
		final ModelRenderer bipedRightArm = model.bipedRightArm;
		bipedRightArm.rotationPointX += x * 0.25f;
		final ModelRenderer bipedRightArm2 = model.bipedRightArm;
		bipedRightArm2.rotationPointY += y * 1.25f;
		model.bipedBody.rotationPointX = x * 0.25f;
	}
}

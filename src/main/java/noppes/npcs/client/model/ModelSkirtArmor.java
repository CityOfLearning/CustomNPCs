//

//

package noppes.npcs.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelSkirtArmor extends ModelBiped {
	private ModelPlaneRenderer Shape1;

	public ModelSkirtArmor() {
		(Shape1 = new ModelPlaneRenderer(this, 4, 20)).addSidePlane(0.0f, 0.0f, 0.0f, 9, 2);
		final ModelPlaneRenderer part1 = new ModelPlaneRenderer(this, 6, 20);
		part1.addSidePlane(2.0f, 0.0f, 0.0f, 9, 2);
		part1.rotateAngleY = -1.5707964f;
		Shape1.addChild(part1);
		Shape1.setRotationPoint(2.4f, 8.8f, 0.0f);
		setRotation(Shape1, 0.3f, -0.2f, -0.2f);
	}

	@Override
	public void render(final Entity par1Entity, final float par2, final float par3, final float par4, final float par5,
			final float par6, final float par7) {
		setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0f, 0.0f, bipedRightLeg.rotationPointZ * par7);
		GlStateManager.scale(1.6f, 1.04f, 1.6f);
		for (int i = 0; i < 10; ++i) {
			GlStateManager.rotate(36.0f, 0.0f, 1.0f, 0.0f);
			Shape1.render(par7);
		}
		GlStateManager.popMatrix();
	}

	public void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(final float par1, final float par2, final float par3, final float par4,
			final float par5, final float par6, final Entity par7Entity) {
		setRotation(Shape1, 0.3f, -0.2f, -0.2f);
		isSneak = par7Entity.isSneaking();
		super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
		final ModelPlaneRenderer shape1 = Shape1;
		shape1.rotateAngleX += bipedLeftArm.rotateAngleX * 0.02f;
		final ModelPlaneRenderer shape2 = Shape1;
		shape2.rotateAngleZ += bipedLeftArm.rotateAngleX * 0.06f;
		final ModelPlaneRenderer shape3 = Shape1;
		shape3.rotateAngleZ -= (MathHelper.cos(par3 * 0.09f) * 0.02f) - 0.05f;
	}
}

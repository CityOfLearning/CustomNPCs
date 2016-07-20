//

//

package noppes.npcs.client.model.part.legs;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import noppes.npcs.client.model.ModelPlaneRenderer;

public class ModelNagaLegs extends ModelRenderer {
	private ModelRenderer nagaPart1;
	private ModelRenderer nagaPart2;
	private ModelRenderer nagaPart3;
	private ModelRenderer nagaPart4;
	private ModelRenderer nagaPart5;
	public boolean isRiding;
	public boolean isSneaking;
	public boolean isSleeping;
	public boolean isCrawling;

	public ModelNagaLegs(ModelBase base) {
		super(base);
		isRiding = false;
		isSneaking = false;
		isSleeping = false;
		isCrawling = false;
		nagaPart1 = new ModelRenderer(base, 0, 0);
		ModelRenderer legPart = new ModelRenderer(base, 0, 16);
		legPart.addBox(0.0f, -2.0f, -2.0f, 4, 4, 4);
		legPart.setRotationPoint(-4.0f, 0.0f, 0.0f);
		nagaPart1.addChild(legPart);
		legPart = new ModelRenderer(base, 0, 16);
		legPart.mirror = true;
		legPart.addBox(0.0f, -2.0f, -2.0f, 4, 4, 4);
		nagaPart1.addChild(legPart);
		nagaPart2 = new ModelRenderer(base, 0, 0);
		nagaPart2.childModels = nagaPart1.childModels;
		nagaPart3 = new ModelRenderer(base, 0, 0);
		ModelPlaneRenderer plane = new ModelPlaneRenderer(base, 4, 24);
		plane.addBackPlane(0.0f, -2.0f, 0.0f, 4, 4);
		plane.setRotationPoint(-4.0f, 0.0f, 0.0f);
		nagaPart3.addChild(plane);
		plane = new ModelPlaneRenderer(base, 4, 24);
		plane.mirror = true;
		plane.addBackPlane(0.0f, -2.0f, 0.0f, 4, 4);
		nagaPart3.addChild(plane);
		plane = new ModelPlaneRenderer(base, 8, 24);
		plane.addBackPlane(0.0f, -2.0f, 6.0f, 4, 4);
		plane.setRotationPoint(-4.0f, 0.0f, 0.0f);
		nagaPart3.addChild(plane);
		plane = new ModelPlaneRenderer(base, 8, 24);
		plane.mirror = true;
		plane.addBackPlane(0.0f, -2.0f, 6.0f, 4, 4);
		nagaPart3.addChild(plane);
		plane = new ModelPlaneRenderer(base, 4, 26);
		plane.addTopPlane(0.0f, -2.0f, -6.0f, 4, 6);
		plane.setRotationPoint(-4.0f, 0.0f, 0.0f);
		plane.rotateAngleX = 3.1415927f;
		nagaPart3.addChild(plane);
		plane = new ModelPlaneRenderer(base, 4, 26);
		plane.mirror = true;
		plane.addTopPlane(0.0f, -2.0f, -6.0f, 4, 6);
		plane.rotateAngleX = 3.1415927f;
		nagaPart3.addChild(plane);
		plane = new ModelPlaneRenderer(base, 8, 26);
		plane.addTopPlane(0.0f, -2.0f, 0.0f, 4, 6);
		plane.setRotationPoint(-4.0f, 0.0f, 0.0f);
		nagaPart3.addChild(plane);
		plane = new ModelPlaneRenderer(base, 8, 26);
		plane.mirror = true;
		plane.addTopPlane(0.0f, -2.0f, 0.0f, 4, 6);
		nagaPart3.addChild(plane);
		plane = new ModelPlaneRenderer(base, 0, 26);
		plane.rotateAngleX = 1.5707964f;
		plane.addSidePlane(0.0f, 0.0f, -2.0f, 6, 4);
		plane.setRotationPoint(-4.0f, 0.0f, 0.0f);
		nagaPart3.addChild(plane);
		plane = new ModelPlaneRenderer(base, 0, 26);
		plane.rotateAngleX = 1.5707964f;
		plane.addSidePlane(4.0f, 0.0f, -2.0f, 6, 4);
		nagaPart3.addChild(plane);
		nagaPart4 = new ModelRenderer(base, 0, 0);
		nagaPart4.childModels = nagaPart3.childModels;
		nagaPart5 = new ModelRenderer(base, 0, 0);
		legPart = new ModelRenderer(base, 56, 20);
		legPart.addBox(0.0f, 0.0f, -2.0f, 2, 5, 2);
		legPart.setRotationPoint(-2.0f, 0.0f, 0.0f);
		legPart.rotateAngleX = 1.5707964f;
		nagaPart5.addChild(legPart);
		legPart = new ModelRenderer(base, 56, 20);
		legPart.mirror = true;
		legPart.addBox(0.0f, 0.0f, -2.0f, 2, 5, 2);
		legPart.rotateAngleX = 1.5707964f;
		nagaPart5.addChild(legPart);
		addChild(nagaPart1);
		addChild(nagaPart2);
		addChild(nagaPart3);
		addChild(nagaPart4);
		addChild(nagaPart5);
		nagaPart1.setRotationPoint(0.0f, 14.0f, 0.0f);
		nagaPart2.setRotationPoint(0.0f, 18.0f, 0.6f);
		nagaPart3.setRotationPoint(0.0f, 22.0f, -0.3f);
		nagaPart4.setRotationPoint(0.0f, 22.0f, 5.0f);
		nagaPart5.setRotationPoint(0.0f, 22.0f, 10.0f);
	}

	@Override
	public void render(float par7) {
		if (isHidden || !showModel) {
			return;
		}
		nagaPart1.render(par7);
		nagaPart3.render(par7);
		if (!isRiding) {
			nagaPart2.render(par7);
		}
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.74f, 0.7f, 0.85f);
		GlStateManager.translate(nagaPart3.rotateAngleY, 0.66f, 0.06f);
		nagaPart4.render(par7);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translate(nagaPart3.rotateAngleY + nagaPart4.rotateAngleY, 0.0f, 0.0f);
		nagaPart5.render(par7);
		GlStateManager.popMatrix();
	}

	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6,
			Entity entity) {
		nagaPart1.rotateAngleY = MathHelper.cos(par1 * 0.6662f) * 0.26f * par2;
		nagaPart2.rotateAngleY = MathHelper.cos(par1 * 0.6662f) * 0.5f * par2;
		nagaPart3.rotateAngleY = MathHelper.cos(par1 * 0.6662f) * 0.26f * par2;
		nagaPart4.rotateAngleY = -MathHelper.cos(par1 * 0.6662f) * 0.16f * par2;
		nagaPart5.rotateAngleY = -MathHelper.cos(par1 * 0.6662f) * 0.3f * par2;
		nagaPart1.setRotationPoint(0.0f, 14.0f, 0.0f);
		nagaPart2.setRotationPoint(0.0f, 18.0f, 0.6f);
		nagaPart3.setRotationPoint(0.0f, 22.0f, -0.3f);
		nagaPart4.setRotationPoint(0.0f, 22.0f, 5.0f);
		nagaPart5.setRotationPoint(0.0f, 22.0f, 10.0f);
		nagaPart1.rotateAngleX = 0.0f;
		nagaPart2.rotateAngleX = 0.0f;
		nagaPart3.rotateAngleX = 0.0f;
		nagaPart4.rotateAngleX = 0.0f;
		nagaPart5.rotateAngleX = 0.0f;
		if (isSleeping || isCrawling) {
			nagaPart3.rotateAngleX = -1.5707964f;
			nagaPart4.rotateAngleX = -1.5707964f;
			nagaPart5.rotateAngleX = -1.5707964f;
			ModelRenderer nagaPart3 = this.nagaPart3;
			nagaPart3.rotationPointY -= 2.0f;
			this.nagaPart3.rotationPointZ = 0.9f;
			ModelRenderer nagaPart4 = this.nagaPart4;
			nagaPart4.rotationPointY += 4.0f;
			this.nagaPart4.rotationPointZ = 0.9f;
			ModelRenderer nagaPart5 = this.nagaPart5;
			nagaPart5.rotationPointY += 7.0f;
			this.nagaPart5.rotationPointZ = 2.9f;
		}
		if (isRiding) {
			ModelRenderer nagaPart6 = nagaPart1;
			--nagaPart6.rotationPointY;
			nagaPart1.rotateAngleX = -0.19634955f;
			nagaPart1.rotationPointZ = -1.0f;
			ModelRenderer nagaPart7 = nagaPart2;
			nagaPart7.rotationPointY -= 4.0f;
			nagaPart2.rotationPointZ = -1.0f;
			ModelRenderer nagaPart8 = nagaPart3;
			nagaPart8.rotationPointY -= 9.0f;
			ModelRenderer nagaPart9 = nagaPart3;
			--nagaPart9.rotationPointZ;
			ModelRenderer nagaPart10 = nagaPart4;
			nagaPart10.rotationPointY -= 13.0f;
			ModelRenderer nagaPart11 = nagaPart4;
			--nagaPart11.rotationPointZ;
			ModelRenderer nagaPart12 = nagaPart5;
			nagaPart12.rotationPointY -= 9.0f;
			ModelRenderer nagaPart13 = nagaPart5;
			--nagaPart13.rotationPointZ;
			if (isSneaking) {
				ModelRenderer nagaPart14 = nagaPart1;
				nagaPart14.rotationPointZ += 5.0f;
				ModelRenderer nagaPart15 = nagaPart3;
				nagaPart15.rotationPointZ += 5.0f;
				ModelRenderer nagaPart16 = nagaPart4;
				nagaPart16.rotationPointZ += 5.0f;
				ModelRenderer nagaPart17 = nagaPart5;
				nagaPart17.rotationPointZ += 4.0f;
				ModelRenderer nagaPart18 = nagaPart1;
				--nagaPart18.rotationPointY;
				ModelRenderer nagaPart19 = nagaPart2;
				--nagaPart19.rotationPointY;
				ModelRenderer nagaPart20 = nagaPart3;
				--nagaPart20.rotationPointY;
				ModelRenderer nagaPart21 = nagaPart4;
				--nagaPart21.rotationPointY;
				ModelRenderer nagaPart22 = nagaPart5;
				--nagaPart22.rotationPointY;
			}
		} else if (isSneaking) {
			ModelRenderer nagaPart23 = nagaPart1;
			--nagaPart23.rotationPointY;
			ModelRenderer nagaPart24 = nagaPart2;
			--nagaPart24.rotationPointY;
			ModelRenderer nagaPart25 = nagaPart3;
			--nagaPart25.rotationPointY;
			ModelRenderer nagaPart26 = nagaPart4;
			--nagaPart26.rotationPointY;
			ModelRenderer nagaPart27 = nagaPart5;
			--nagaPart27.rotationPointY;
			nagaPart1.rotationPointZ = 5.0f;
			nagaPart2.rotationPointZ = 3.0f;
		}
	}
}

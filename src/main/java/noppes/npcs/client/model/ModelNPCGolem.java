
package noppes.npcs.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import noppes.npcs.entity.EntityNPCInterface;

public class ModelNPCGolem extends ModelBipedAlt {
	private ModelRenderer bipedLowerBody;

	public ModelNPCGolem(float scale) {
		super(scale);
		init(0.0f, 0.0f);
	}

	private float func_78172_a(float par1, float par2) {
		return (Math.abs((par1 % par2) - (par2 * 0.5f)) - (par2 * 0.25f)) / (par2 * 0.25f);
	}

	public void init(float f, float f1) {
		short short1 = 128;
		short short2 = 128;
		float f2 = -7.0f;
		(bipedHead = new ModelRenderer(this).setTextureSize(short1, short2)).setRotationPoint(0.0f, f2, -2.0f);
		bipedHead.setTextureOffset(0, 0).addBox(-4.0f, -12.0f, -5.5f, 8, 10, 8, f);
		bipedHead.setTextureOffset(24, 0).addBox(-1.0f, -5.0f, -7.5f, 2, 4, 2, f);
		(bipedHeadwear = new ModelRenderer(this).setTextureSize(short1, short2)).setRotationPoint(0.0f, f2, -2.0f);
		bipedHeadwear.setTextureOffset(0, 85).addBox(-4.0f, -12.0f, -5.5f, 8, 10, 8, f + 0.5f);
		(bipedBody = new ModelRenderer(this).setTextureSize(short1, short2)).setRotationPoint(0.0f, 0.0f + f2, 0.0f);
		bipedBody.setTextureOffset(0, 40).addBox(-9.0f, -2.0f, -6.0f, 18, 12, 11, f + 0.2f);
		bipedBody.setTextureOffset(0, 21).addBox(-9.0f, -2.0f, -6.0f, 18, 8, 11, f);
		(bipedLowerBody = new ModelRenderer(this).setTextureSize(short1, short2)).setRotationPoint(0.0f, 0.0f + f2,
				0.0f);
		bipedLowerBody.setTextureOffset(0, 70).addBox(-4.5f, 10.0f, -3.0f, 9, 5, 6, f + 0.5f);
		bipedLowerBody.setTextureOffset(30, 70).addBox(-4.5f, 6.0f, -3.0f, 9, 9, 6, f + 0.4f);
		(bipedRightArm = new ModelRenderer(this).setTextureSize(short1, short2)).setRotationPoint(0.0f, f2, 0.0f);
		bipedRightArm.setTextureOffset(60, 21).addBox(-13.0f, -2.5f, -3.0f, 4, 30, 6, f + 0.2f);
		bipedRightArm.setTextureOffset(80, 21).addBox(-13.0f, -2.5f, -3.0f, 4, 20, 6, f);
		bipedRightArm.setTextureOffset(100, 21).addBox(-13.0f, -2.5f, -3.0f, 4, 20, 6, f + 1.0f);
		(bipedLeftArm = new ModelRenderer(this).setTextureSize(short1, short2)).setRotationPoint(0.0f, f2, 0.0f);
		bipedLeftArm.setTextureOffset(60, 58).addBox(9.0f, -2.5f, -3.0f, 4, 30, 6, f + 0.2f);
		bipedLeftArm.setTextureOffset(80, 58).addBox(9.0f, -2.5f, -3.0f, 4, 20, 6, f);
		bipedLeftArm.setTextureOffset(100, 58).addBox(9.0f, -2.5f, -3.0f, 4, 20, 6, f + 1.0f);
		(bipedLeftLeg = new ModelRenderer(this, 0, 22).setTextureSize(short1, short2)).setRotationPoint(-4.0f,
				18.0f + f2, 0.0f);
		bipedLeftLeg.setTextureOffset(37, 0).addBox(-3.5f, -3.0f, -3.0f, 6, 16, 5, f);
		bipedRightLeg = new ModelRenderer(this, 0, 22).setTextureSize(short1, short2);
		bipedRightLeg.mirror = true;
		bipedRightLeg.setTextureOffset(60, 0).addBox(-3.5f, -3.0f, -3.0f, 6, 16, 5, f);
		bipedRightLeg.setRotationPoint(5.0f, 18.0f + f2, 0.0f);
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		super.render(par1Entity, par2, par3, par4, par5, par6, par7);
		bipedLowerBody.render(par7);
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6,
			Entity entity) {
		EntityNPCInterface npc = (EntityNPCInterface) entity;
		isRiding = npc.isRiding();
		if (isSneak && ((npc.currentAnimation == 7) || (npc.currentAnimation == 2))) {
			isSneak = false;
		}
		bipedHead.rotateAngleY = par4 / 57.295776f;
		bipedHead.rotateAngleX = par5 / 57.295776f;
		bipedHeadwear.rotateAngleY = bipedHead.rotateAngleY;
		bipedHeadwear.rotateAngleX = bipedHead.rotateAngleX;
		bipedLeftLeg.rotateAngleX = -1.5f * func_78172_a(par1, 13.0f) * par2;
		bipedRightLeg.rotateAngleX = 1.5f * func_78172_a(par1, 13.0f) * par2;
		bipedLeftLeg.rotateAngleY = 0.0f;
		bipedRightLeg.rotateAngleY = 0.0f;
		float f6 = MathHelper.sin(swingProgress * 3.1415927f);
		float f7 = MathHelper.sin((16.0f - ((1.0f - swingProgress) * (1.0f - swingProgress))) * 3.1415927f);
		if (swingProgress > 0.0) {
			bipedRightArm.rotateAngleZ = 0.0f;
			bipedLeftArm.rotateAngleZ = 0.0f;
			bipedRightArm.rotateAngleY = -(0.1f - (f6 * 0.6f));
			bipedLeftArm.rotateAngleY = 0.1f - (f6 * 0.6f);
			bipedRightArm.rotateAngleX = 0.0f;
			bipedLeftArm.rotateAngleX = 0.0f;
			bipedRightArm.rotateAngleX = -1.5707964f;
			bipedLeftArm.rotateAngleX = -1.5707964f;
			ModelRenderer bipedRightArm = this.bipedRightArm;
			bipedRightArm.rotateAngleX -= (f6 * 1.2f) - (f7 * 0.4f);
			ModelRenderer bipedLeftArm = this.bipedLeftArm;
			bipedLeftArm.rotateAngleX -= (f6 * 1.2f) - (f7 * 0.4f);
		} else if (aimedBow) {
			float f8 = 0.0f;
			float f9 = 0.0f;
			bipedRightArm.rotateAngleZ = 0.0f;
			bipedRightArm.rotateAngleX = -1.5707964f + bipedHead.rotateAngleX;
			ModelRenderer bipedRightArm2 = bipedRightArm;
			bipedRightArm2.rotateAngleX -= (f8 * 1.2f) - (f9 * 0.4f);
			ModelRenderer bipedRightArm3 = bipedRightArm;
			bipedRightArm3.rotateAngleZ += (MathHelper.cos(par3 * 0.09f) * 0.05f) + 0.05f;
			ModelRenderer bipedRightArm4 = bipedRightArm;
			bipedRightArm4.rotateAngleX += MathHelper.sin(par3 * 0.067f) * 0.05f;
			bipedLeftArm.rotateAngleX = (-0.2f - (1.5f * func_78172_a(par1, 13.0f))) * par2;
			bipedBody.rotateAngleY = -(0.1f - (f8 * 0.6f)) + bipedHead.rotateAngleY;
			bipedRightArm.rotateAngleY = -(0.1f - (f8 * 0.6f)) + bipedHead.rotateAngleY;
			bipedLeftArm.rotateAngleY = (0.1f - (f8 * 0.6f)) + bipedHead.rotateAngleY;
		} else {
			bipedRightArm.rotateAngleX = (-0.2f + (1.5f * func_78172_a(par1, 13.0f))) * par2;
			bipedLeftArm.rotateAngleX = (-0.2f - (1.5f * func_78172_a(par1, 13.0f))) * par2;
			bipedBody.rotateAngleY = 0.0f;
			bipedRightArm.rotateAngleY = 0.0f;
			bipedLeftArm.rotateAngleY = 0.0f;
			bipedRightArm.rotateAngleZ = 0.0f;
			bipedLeftArm.rotateAngleZ = 0.0f;
		}
		if (isRiding) {
			ModelRenderer bipedRightArm5 = bipedRightArm;
			bipedRightArm5.rotateAngleX -= 0.62831855f;
			ModelRenderer bipedLeftArm2 = bipedLeftArm;
			bipedLeftArm2.rotateAngleX -= 0.62831855f;
			bipedLeftLeg.rotateAngleX = -1.2566371f;
			bipedRightLeg.rotateAngleX = -1.2566371f;
			bipedLeftLeg.rotateAngleY = 0.31415927f;
			bipedRightLeg.rotateAngleY = -0.31415927f;
		}
	}
}

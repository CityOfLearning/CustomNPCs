//

//

package noppes.npcs.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import noppes.npcs.entity.EntityNPCInterface;

public class ModelPonyArmor extends ModelBase {
	private boolean rainboom;
	public ModelRenderer head;
	public ModelRenderer Body;
	public ModelRenderer BodyBack;
	public ModelRenderer rightarm;
	public ModelRenderer LeftArm;
	public ModelRenderer RightLeg;
	public ModelRenderer LeftLeg;
	public ModelRenderer rightarm2;
	public ModelRenderer LeftArm2;
	public ModelRenderer RightLeg2;
	public ModelRenderer LeftLeg2;
	public boolean isPegasus;
	public boolean isUnicorn;
	public boolean isSleeping;
	public boolean isFlying;
	public boolean isGlow;
	public boolean isSneak;
	public boolean aimedBow;
	public int heldItemRight;

	public ModelPonyArmor(float f) {
		isPegasus = false;
		isUnicorn = false;
		isSleeping = false;
		isFlying = false;
		isGlow = false;
		isSneak = false;
		init(f, 0.0f);
	}

	public void init(float strech, float f) {
		float f2 = 0.0f;
		float f3 = 0.0f;
		float f4 = 0.0f;
		(head = new ModelRenderer(this, 0, 0)).addBox(-4.0f, -4.0f, -6.0f, 8, 8, 8, strech);
		head.setRotationPoint(f2, f3, f4);
		float f5 = 0.0f;
		float f6 = 0.0f;
		float f7 = 0.0f;
		(Body = new ModelRenderer(this, 16, 16)).addBox(-4.0f, 4.0f, -2.0f, 8, 8, 4, strech);
		Body.setRotationPoint(f5, f6 + f, f7);
		(BodyBack = new ModelRenderer(this, 0, 0)).addBox(-4.0f, 4.0f, 6.0f, 8, 8, 8, strech);
		BodyBack.setRotationPoint(f5, f6 + f, f7);
		(rightarm = new ModelRenderer(this, 0, 16)).addBox(-2.0f, 4.0f, -2.0f, 4, 12, 4, strech);
		rightarm.setRotationPoint(-3.0f, 8.0f + f, 0.0f);
		LeftArm = new ModelRenderer(this, 0, 16);
		LeftArm.mirror = true;
		LeftArm.addBox(-2.0f, 4.0f, -2.0f, 4, 12, 4, strech);
		LeftArm.setRotationPoint(3.0f, 8.0f + f, 0.0f);
		(RightLeg = new ModelRenderer(this, 0, 16)).addBox(-2.0f, 4.0f, -2.0f, 4, 12, 4, strech);
		RightLeg.setRotationPoint(-3.0f, 0.0f + f, 0.0f);
		LeftLeg = new ModelRenderer(this, 0, 16);
		LeftLeg.mirror = true;
		LeftLeg.addBox(-2.0f, 4.0f, -2.0f, 4, 12, 4, strech);
		LeftLeg.setRotationPoint(3.0f, 0.0f + f, 0.0f);
		(rightarm2 = new ModelRenderer(this, 0, 16)).addBox(-2.0f, 4.0f, -2.0f, 4, 12, 4, strech * 0.5f);
		rightarm2.setRotationPoint(-3.0f, 8.0f + f, 0.0f);
		LeftArm2 = new ModelRenderer(this, 0, 16);
		LeftArm2.mirror = true;
		LeftArm2.addBox(-2.0f, 4.0f, -2.0f, 4, 12, 4, strech * 0.5f);
		LeftArm2.setRotationPoint(3.0f, 8.0f + f, 0.0f);
		(RightLeg2 = new ModelRenderer(this, 0, 16)).addBox(-2.0f, 4.0f, -2.0f, 4, 12, 4, strech * 0.5f);
		RightLeg2.setRotationPoint(-3.0f, 0.0f + f, 0.0f);
		LeftLeg2 = new ModelRenderer(this, 0, 16);
		LeftLeg2.mirror = true;
		LeftLeg2.addBox(-2.0f, 4.0f, -2.0f, 4, 12, 4, strech * 0.5f);
		LeftLeg2.setRotationPoint(3.0f, 0.0f + f, 0.0f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		head.render(f5);
		Body.render(f5);
		BodyBack.render(f5);
		LeftArm.render(f5);
		rightarm.render(f5);
		LeftLeg.render(f5);
		RightLeg.render(f5);
		LeftArm2.render(f5);
		rightarm2.render(f5);
		LeftLeg2.render(f5);
		RightLeg2.render(f5);
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		EntityNPCInterface npc = (EntityNPCInterface) entity;
		if (!isRiding) {
			isRiding = (npc.currentAnimation == 1);
		}
		if (isSneak && ((npc.currentAnimation == 7) || (npc.currentAnimation == 2))) {
			isSneak = false;
		}
		rainboom = false;
		float f6;
		float f7;
		if (isSleeping) {
			f6 = 1.4f;
			f7 = 0.1f;
		} else {
			f6 = f3 / 57.29578f;
			f7 = f4 / 57.29578f;
		}
		head.rotateAngleY = f6;
		head.rotateAngleX = f7;
		float f8;
		float f9;
		float f10;
		float f11;
		if (!isFlying || !isPegasus) {
			f8 = MathHelper.cos((f * 0.6662f) + 3.141593f) * 0.6f * f1;
			f9 = MathHelper.cos(f * 0.6662f) * 0.6f * f1;
			f10 = MathHelper.cos(f * 0.6662f) * 0.3f * f1;
			f11 = MathHelper.cos((f * 0.6662f) + 3.141593f) * 0.3f * f1;
			rightarm.rotateAngleY = 0.0f;
			LeftArm.rotateAngleY = 0.0f;
			RightLeg.rotateAngleY = 0.0f;
			LeftLeg.rotateAngleY = 0.0f;
			rightarm2.rotateAngleY = 0.0f;
			LeftArm2.rotateAngleY = 0.0f;
			RightLeg2.rotateAngleY = 0.0f;
			LeftLeg2.rotateAngleY = 0.0f;
		} else {
			if (f1 < 0.9999f) {
				rainboom = false;
				f8 = MathHelper.sin(0.0f - (f1 * 0.5f));
				f9 = MathHelper.sin(0.0f - (f1 * 0.5f));
				f10 = MathHelper.sin(f1 * 0.5f);
				f11 = MathHelper.sin(f1 * 0.5f);
			} else {
				rainboom = true;
				f8 = 4.712f;
				f9 = 4.712f;
				f10 = 1.571f;
				f11 = 1.571f;
			}
			rightarm.rotateAngleY = 0.2f;
			LeftArm.rotateAngleY = -0.2f;
			RightLeg.rotateAngleY = -0.2f;
			LeftLeg.rotateAngleY = 0.2f;
			rightarm2.rotateAngleY = 0.2f;
			LeftArm2.rotateAngleY = -0.2f;
			RightLeg2.rotateAngleY = -0.2f;
			LeftLeg2.rotateAngleY = 0.2f;
		}
		if (isSleeping) {
			f8 = 4.712f;
			f9 = 4.712f;
			f10 = 1.571f;
			f11 = 1.571f;
		}
		rightarm.rotateAngleX = f8;
		LeftArm.rotateAngleX = f9;
		RightLeg.rotateAngleX = f10;
		LeftLeg.rotateAngleX = f11;
		rightarm.rotateAngleZ = 0.0f;
		LeftArm.rotateAngleZ = 0.0f;
		rightarm2.rotateAngleX = f8;
		LeftArm2.rotateAngleX = f9;
		RightLeg2.rotateAngleX = f10;
		LeftLeg2.rotateAngleX = f11;
		rightarm2.rotateAngleZ = 0.0f;
		LeftArm2.rotateAngleZ = 0.0f;
		if ((heldItemRight != 0) && !rainboom && !isUnicorn) {
			rightarm.rotateAngleX = (rightarm.rotateAngleX * 0.5f) - 0.3141593f;
			rightarm2.rotateAngleX = (rightarm2.rotateAngleX * 0.5f) - 0.3141593f;
		}
		float f12 = 0.0f;
		if ((f5 > -9990.0f) && !isUnicorn) {
			f12 = MathHelper.sin(MathHelper.sqrt_float(f5) * 3.141593f * 2.0f) * 0.2f;
		}
		Body.rotateAngleY = (float) (f12 * 0.2);
		BodyBack.rotateAngleY = (float) (f12 * 0.2);
		float f13 = MathHelper.sin(Body.rotateAngleY) * 5.0f;
		float f14 = MathHelper.cos(Body.rotateAngleY) * 5.0f;
		float f15 = 4.0f;
		if (isSneak && !isFlying) {
			f15 = 0.0f;
		}
		if (isSleeping) {
			f15 = 2.6f;
		}
		if (rainboom) {
			rightarm.rotationPointZ = f13 + 2.0f;
			rightarm2.rotationPointZ = f13 + 2.0f;
			LeftArm.rotationPointZ = (0.0f - f13) + 2.0f;
			LeftArm2.rotationPointZ = (0.0f - f13) + 2.0f;
		} else {
			rightarm.rotationPointZ = f13 + 1.0f;
			rightarm2.rotationPointZ = f13 + 1.0f;
			LeftArm.rotationPointZ = (0.0f - f13) + 1.0f;
			LeftArm2.rotationPointZ = (0.0f - f13) + 1.0f;
		}
		rightarm.rotationPointX = (0.0f - f14 - 1.0f) + f15;
		rightarm2.rotationPointX = (0.0f - f14 - 1.0f) + f15;
		LeftArm.rotationPointX = (f14 + 1.0f) - f15;
		LeftArm2.rotationPointX = (f14 + 1.0f) - f15;
		RightLeg.rotationPointX = (0.0f - f14 - 1.0f) + f15;
		RightLeg2.rotationPointX = (0.0f - f14 - 1.0f) + f15;
		LeftLeg.rotationPointX = (f14 + 1.0f) - f15;
		LeftLeg2.rotationPointX = (f14 + 1.0f) - f15;
		ModelRenderer rightarm = this.rightarm;
		rightarm.rotateAngleY += Body.rotateAngleY;
		ModelRenderer rightarm2 = this.rightarm2;
		rightarm2.rotateAngleY += Body.rotateAngleY;
		ModelRenderer leftArm = LeftArm;
		leftArm.rotateAngleY += Body.rotateAngleY;
		ModelRenderer leftArm2 = LeftArm2;
		leftArm2.rotateAngleY += Body.rotateAngleY;
		ModelRenderer leftArm3 = LeftArm;
		leftArm3.rotateAngleX += Body.rotateAngleY;
		ModelRenderer leftArm4 = LeftArm2;
		leftArm4.rotateAngleX += Body.rotateAngleY;
		this.rightarm.rotationPointY = 8.0f;
		LeftArm.rotationPointY = 8.0f;
		RightLeg.rotationPointY = 4.0f;
		LeftLeg.rotationPointY = 4.0f;
		this.rightarm2.rotationPointY = 8.0f;
		LeftArm2.rotationPointY = 8.0f;
		RightLeg2.rotationPointY = 4.0f;
		LeftLeg2.rotationPointY = 4.0f;
		if ((f5 > -9990.0f) && !isUnicorn) {
			float f16 = 1.0f - f5;
			f16 *= f16 * f16;
			f16 = 1.0f - f16;
			MathHelper.sin(f16 * 3.141593f);
			MathHelper.sin(f5 * 3.141593f);
		}
		if (isSneak && !isFlying) {
			float f20 = 0.4f;
			float f21 = 7.0f;
			float f22 = -4.0f;
			Body.rotateAngleX = f20;
			Body.rotationPointY = f21;
			Body.rotationPointZ = f22;
			BodyBack.rotateAngleX = f20;
			BodyBack.rotationPointY = f21;
			BodyBack.rotationPointZ = f22;
			ModelRenderer rightLeg = RightLeg;
			rightLeg.rotateAngleX -= 0.0f;
			ModelRenderer leftLeg = LeftLeg;
			leftLeg.rotateAngleX -= 0.0f;
			ModelRenderer rightarm3 = this.rightarm;
			rightarm3.rotateAngleX -= 0.4f;
			ModelRenderer leftArm5 = LeftArm;
			leftArm5.rotateAngleX -= 0.4f;
			RightLeg.rotationPointZ = 10.0f;
			LeftLeg.rotationPointZ = 10.0f;
			RightLeg.rotationPointY = 7.0f;
			LeftLeg.rotationPointY = 7.0f;
			ModelRenderer rightLeg2 = RightLeg2;
			rightLeg2.rotateAngleX -= 0.0f;
			ModelRenderer leftLeg2 = LeftLeg2;
			leftLeg2.rotateAngleX -= 0.0f;
			ModelRenderer rightarm4 = this.rightarm2;
			rightarm4.rotateAngleX -= 0.4f;
			ModelRenderer leftArm6 = LeftArm2;
			leftArm6.rotateAngleX -= 0.4f;
			RightLeg2.rotationPointZ = 10.0f;
			LeftLeg2.rotationPointZ = 10.0f;
			RightLeg2.rotationPointY = 7.0f;
			LeftLeg2.rotationPointY = 7.0f;
			float f23;
			float f24;
			float f25;
			if (isSleeping) {
				f23 = 2.0f;
				f24 = -1.0f;
				f25 = 1.0f;
			} else {
				f23 = 6.0f;
				f24 = -2.0f;
				f25 = 0.0f;
			}
			head.rotationPointY = f23;
			head.rotationPointZ = f24;
			head.rotationPointX = f25;
		} else {
			float f26 = 0.0f;
			float f27 = 0.0f;
			float f28 = 0.0f;
			Body.rotateAngleX = f26;
			Body.rotationPointY = f27;
			Body.rotationPointZ = f28;
			BodyBack.rotateAngleX = f26;
			BodyBack.rotationPointY = f27;
			BodyBack.rotationPointZ = f28;
			RightLeg.rotationPointZ = 10.0f;
			LeftLeg.rotationPointZ = 10.0f;
			RightLeg.rotationPointY = 8.0f;
			LeftLeg.rotationPointY = 8.0f;
			RightLeg2.rotationPointZ = 10.0f;
			LeftLeg2.rotationPointZ = 10.0f;
			RightLeg2.rotationPointY = 8.0f;
			LeftLeg2.rotationPointY = 8.0f;
			MathHelper.cos(f2 * 0.09f);
			MathHelper.sin(f2 * 0.067f);
			float f31 = 0.0f;
			float f32 = 0.0f;
			head.rotationPointY = f31;
			head.rotationPointZ = f32;
		}
		if (isSleeping) {
			this.rightarm.rotationPointZ += 6.0f;
			LeftArm.rotationPointZ += 6.0f;
			RightLeg.rotationPointZ -= 8.0f;
			LeftLeg.rotationPointZ -= 8.0f;
			this.rightarm.rotationPointY += 2.0f;
			LeftArm.rotationPointY += 2.0f;
			RightLeg.rotationPointY += 2.0f;
			LeftLeg.rotationPointY += 2.0f;
			this.rightarm2.rotationPointZ += 6.0f;
			LeftArm2.rotationPointZ += 6.0f;
			RightLeg2.rotationPointZ -= 8.0f;
			LeftLeg2.rotationPointZ -= 8.0f;
			this.rightarm2.rotationPointY += 2.0f;
			LeftArm2.rotationPointY += 2.0f;
			RightLeg2.rotationPointY += 2.0f;
			LeftLeg2.rotationPointY += 2.0f;
		}
		if (aimedBow && !isUnicorn) {
			float f33 = 0.0f;
			float f34 = 0.0f;
			this.rightarm.rotateAngleZ = 0.0f;
			this.rightarm.rotateAngleY = -(0.1f - (f33 * 0.6f)) + head.rotateAngleY;
			this.rightarm.rotateAngleX = 4.712f + head.rotateAngleX;
			ModelRenderer rightarm5 = this.rightarm;
			rightarm5.rotateAngleX -= (f33 * 1.2f) - (f34 * 0.4f);
			ModelRenderer rightarm6 = this.rightarm;
			rightarm6.rotateAngleZ += (MathHelper.cos(f2 * 0.09f) * 0.05f) + 0.05f;
			ModelRenderer rightarm7 = this.rightarm;
			rightarm7.rotateAngleX += MathHelper.sin(f2 * 0.067f) * 0.05f;
			this.rightarm2.rotateAngleZ = 0.0f;
			this.rightarm2.rotateAngleY = -(0.1f - (f33 * 0.6f)) + head.rotateAngleY;
			this.rightarm2.rotateAngleX = 4.712f + head.rotateAngleX;
			ModelRenderer rightarm8 = this.rightarm2;
			rightarm8.rotateAngleX -= (f33 * 1.2f) - (f34 * 0.4f);
			ModelRenderer rightarm9 = this.rightarm2;
			rightarm9.rotateAngleZ += (MathHelper.cos(f2 * 0.09f) * 0.05f) + 0.05f;
			ModelRenderer rightarm10 = this.rightarm2;
			rightarm10.rotateAngleX += MathHelper.sin(f2 * 0.067f) * 0.05f;
			++this.rightarm.rotationPointZ;
			++this.rightarm2.rotationPointZ;
		}
	}
}

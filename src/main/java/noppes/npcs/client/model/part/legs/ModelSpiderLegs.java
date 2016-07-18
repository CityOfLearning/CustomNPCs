//

//

package noppes.npcs.client.model.part.legs;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import noppes.npcs.ModelData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityNPCInterface;

public class ModelSpiderLegs extends ModelRenderer {
	private ModelRenderer spiderLeg1;
	private ModelRenderer spiderLeg2;
	private ModelRenderer spiderLeg3;
	private ModelRenderer spiderLeg4;
	private ModelRenderer spiderLeg5;
	private ModelRenderer spiderLeg6;
	private ModelRenderer spiderLeg7;
	private ModelRenderer spiderLeg8;
	private ModelRenderer spiderBody;
	private ModelRenderer spiderNeck;
	private ModelBiped base;

	public ModelSpiderLegs(final ModelBiped base) {
		super(base);
		this.base = base;
		final float var1 = 0.0f;
		final byte var2 = 15;
		(spiderNeck = new ModelRenderer(base, 0, 0)).addBox(-3.0f, -3.0f, -3.0f, 6, 6, 6, var1);
		spiderNeck.setRotationPoint(0.0f, var2, 2.0f);
		addChild(spiderNeck);
		(spiderBody = new ModelRenderer(base, 0, 12)).addBox(-5.0f, -4.0f, -6.0f, 10, 8, 12, var1);
		spiderBody.setRotationPoint(0.0f, var2, 11.0f);
		addChild(spiderBody);
		(spiderLeg1 = new ModelRenderer(base, 18, 0)).addBox(-15.0f, -1.0f, -1.0f, 16, 2, 2, var1);
		spiderLeg1.setRotationPoint(-4.0f, var2, 4.0f);
		addChild(spiderLeg1);
		(spiderLeg2 = new ModelRenderer(base, 18, 0)).addBox(-1.0f, -1.0f, -1.0f, 16, 2, 2, var1);
		spiderLeg2.setRotationPoint(4.0f, var2, 4.0f);
		addChild(spiderLeg2);
		(spiderLeg3 = new ModelRenderer(base, 18, 0)).addBox(-15.0f, -1.0f, -1.0f, 16, 2, 2, var1);
		spiderLeg3.setRotationPoint(-4.0f, var2, 3.0f);
		addChild(spiderLeg3);
		(spiderLeg4 = new ModelRenderer(base, 18, 0)).addBox(-1.0f, -1.0f, -1.0f, 16, 2, 2, var1);
		spiderLeg4.setRotationPoint(4.0f, var2, 3.0f);
		addChild(spiderLeg4);
		(spiderLeg5 = new ModelRenderer(base, 18, 0)).addBox(-15.0f, -1.0f, -1.0f, 16, 2, 2, var1);
		spiderLeg5.setRotationPoint(-4.0f, var2, 2.0f);
		addChild(spiderLeg5);
		(spiderLeg6 = new ModelRenderer(base, 18, 0)).addBox(-1.0f, -1.0f, -1.0f, 16, 2, 2, var1);
		spiderLeg6.setRotationPoint(4.0f, var2, 2.0f);
		addChild(spiderLeg6);
		(spiderLeg7 = new ModelRenderer(base, 18, 0)).addBox(-15.0f, -1.0f, -1.0f, 16, 2, 2, var1);
		spiderLeg7.setRotationPoint(-4.0f, var2, 1.0f);
		addChild(spiderLeg7);
		(spiderLeg8 = new ModelRenderer(base, 18, 0)).addBox(-1.0f, -1.0f, -1.0f, 16, 2, 2, var1);
		spiderLeg8.setRotationPoint(4.0f, var2, 1.0f);
		addChild(spiderLeg8);
	}

	public void setRotationAngles(final ModelData data, final float par1, final float par2, final float par3,
			final float par4, final float par5, final float par6, final Entity entity) {
		rotateAngleX = 0.0f;
		spiderBody.rotationPointY = 15.0f;
		spiderBody.rotationPointZ = 11.0f;
		spiderNeck.rotateAngleX = 0.0f;
		final float var8 = 0.7853982f;
		spiderLeg1.rotateAngleZ = -var8;
		spiderLeg2.rotateAngleZ = var8;
		spiderLeg3.rotateAngleZ = -var8 * 0.74f;
		spiderLeg4.rotateAngleZ = var8 * 0.74f;
		spiderLeg5.rotateAngleZ = -var8 * 0.74f;
		spiderLeg6.rotateAngleZ = var8 * 0.74f;
		spiderLeg7.rotateAngleZ = -var8;
		spiderLeg8.rotateAngleZ = var8;
		final float var9 = -0.0f;
		final float var10 = 0.3926991f;
		spiderLeg1.rotateAngleY = (var10 * 2.0f) + var9;
		spiderLeg2.rotateAngleY = (-var10 * 2.0f) - var9;
		spiderLeg3.rotateAngleY = (var10 * 1.0f) + var9;
		spiderLeg4.rotateAngleY = (-var10 * 1.0f) - var9;
		spiderLeg5.rotateAngleY = (-var10 * 1.0f) + var9;
		spiderLeg6.rotateAngleY = (var10 * 1.0f) - var9;
		spiderLeg7.rotateAngleY = (-var10 * 2.0f) + var9;
		spiderLeg8.rotateAngleY = (var10 * 2.0f) - var9;
		final float var11 = -(MathHelper.cos((par1 * 0.6662f * 2.0f) + 0.0f) * 0.4f) * par2;
		final float var12 = -(MathHelper.cos((par1 * 0.6662f * 2.0f) + 3.1415927f) * 0.4f) * par2;
		final float var13 = -(MathHelper.cos((par1 * 0.6662f * 2.0f) + 1.5707964f) * 0.4f) * par2;
		final float var14 = -(MathHelper.cos((par1 * 0.6662f * 2.0f) + 4.712389f) * 0.4f) * par2;
		final float var15 = Math.abs(MathHelper.sin((par1 * 0.6662f) + 0.0f) * 0.4f) * par2;
		final float var16 = Math.abs(MathHelper.sin((par1 * 0.6662f) + 3.1415927f) * 0.4f) * par2;
		final float var17 = Math.abs(MathHelper.sin((par1 * 0.6662f) + 1.5707964f) * 0.4f) * par2;
		final float var18 = Math.abs(MathHelper.sin((par1 * 0.6662f) + 4.712389f) * 0.4f) * par2;
		final ModelRenderer spiderLeg1 = this.spiderLeg1;
		spiderLeg1.rotateAngleY += var11;
		final ModelRenderer spiderLeg2 = this.spiderLeg2;
		spiderLeg2.rotateAngleY += -var11;
		final ModelRenderer spiderLeg3 = this.spiderLeg3;
		spiderLeg3.rotateAngleY += var12;
		final ModelRenderer spiderLeg4 = this.spiderLeg4;
		spiderLeg4.rotateAngleY += -var12;
		final ModelRenderer spiderLeg5 = this.spiderLeg5;
		spiderLeg5.rotateAngleY += var13;
		final ModelRenderer spiderLeg6 = this.spiderLeg6;
		spiderLeg6.rotateAngleY += -var13;
		final ModelRenderer spiderLeg7 = this.spiderLeg7;
		spiderLeg7.rotateAngleY += var14;
		final ModelRenderer spiderLeg8 = this.spiderLeg8;
		spiderLeg8.rotateAngleY += -var14;
		final ModelRenderer spiderLeg9 = this.spiderLeg1;
		spiderLeg9.rotateAngleZ += var15;
		final ModelRenderer spiderLeg10 = this.spiderLeg2;
		spiderLeg10.rotateAngleZ += -var15;
		final ModelRenderer spiderLeg11 = this.spiderLeg3;
		spiderLeg11.rotateAngleZ += var16;
		final ModelRenderer spiderLeg12 = this.spiderLeg4;
		spiderLeg12.rotateAngleZ += -var16;
		final ModelRenderer spiderLeg13 = this.spiderLeg5;
		spiderLeg13.rotateAngleZ += var17;
		final ModelRenderer spiderLeg14 = this.spiderLeg6;
		spiderLeg14.rotateAngleZ += -var17;
		final ModelRenderer spiderLeg15 = this.spiderLeg7;
		spiderLeg15.rotateAngleZ += var18;
		final ModelRenderer spiderLeg16 = this.spiderLeg8;
		spiderLeg16.rotateAngleZ += -var18;
		if (base.isSneak) {
			rotationPointZ = 5.0f;
			rotationPointY = -1.0f;
			spiderBody.rotationPointY = 16.0f;
			spiderBody.rotationPointZ = 10.0f;
			spiderNeck.rotateAngleX = -0.3926991f;
		}
		if (((EntityNPCInterface) entity).isPlayerSleeping() || (((EntityNPCInterface) entity).currentAnimation == 7)) {
			rotationPointY = 12.0f * data.getPartConfig(EnumParts.LEG_LEFT).scaleY;
			rotationPointZ = 15.0f * data.getPartConfig(EnumParts.LEG_LEFT).scaleY;
			rotateAngleX = -1.5707964f;
		}
	}
}

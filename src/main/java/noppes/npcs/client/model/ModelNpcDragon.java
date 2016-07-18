//

//

package noppes.npcs.client.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import noppes.npcs.entity.EntityNpcDragon;

public class ModelNpcDragon extends ModelBase {
	private ModelRenderer head;
	private ModelRenderer neck;
	private ModelRenderer jaw;
	private ModelRenderer body;
	private ModelRenderer rearLeg;
	private ModelRenderer frontLeg;
	private ModelRenderer rearLegTip;
	private ModelRenderer frontLegTip;
	private ModelRenderer rearFoot;
	private ModelRenderer frontFoot;
	private ModelRenderer wing;
	private ModelRenderer wingTip;
	private float field_40317_s;

	public ModelNpcDragon(final float f) {
		textureWidth = 256;
		textureHeight = 256;
		setTextureOffset("body.body", 0, 0);
		setTextureOffset("wing.skin", -56, 88);
		setTextureOffset("wingtip.skin", -56, 144);
		setTextureOffset("rearleg.main", 0, 0);
		setTextureOffset("rearfoot.main", 112, 0);
		setTextureOffset("rearlegtip.main", 196, 0);
		setTextureOffset("head.upperhead", 112, 30);
		setTextureOffset("wing.bone", 112, 88);
		setTextureOffset("head.upperlip", 176, 44);
		setTextureOffset("jaw.jaw", 176, 65);
		setTextureOffset("frontleg.main", 112, 104);
		setTextureOffset("wingtip.bone", 112, 136);
		setTextureOffset("frontfoot.main", 144, 104);
		setTextureOffset("neck.box", 192, 104);
		setTextureOffset("frontlegtip.main", 226, 138);
		setTextureOffset("body.scale", 220, 53);
		setTextureOffset("head.scale", 0, 0);
		setTextureOffset("neck.scale", 48, 0);
		setTextureOffset("head.nostril", 112, 0);
		final float f2 = -16.0f;
		(head = new ModelRenderer(this, "head")).addBox("upperlip", -6.0f, -1.0f, -8.0f + f2, 12, 5, 16);
		head.addBox("upperhead", -8.0f, -8.0f, 6.0f + f2, 16, 16, 16);
		head.mirror = true;
		head.addBox("scale", -5.0f, -12.0f, 12.0f + f2, 2, 4, 6);
		head.addBox("nostril", -5.0f, -3.0f, -6.0f + f2, 2, 2, 4);
		head.mirror = false;
		head.addBox("scale", 3.0f, -12.0f, 12.0f + f2, 2, 4, 6);
		head.addBox("nostril", 3.0f, -3.0f, -6.0f + f2, 2, 2, 4);
		(jaw = new ModelRenderer(this, "jaw")).setRotationPoint(0.0f, 4.0f, 8.0f + f2);
		jaw.addBox("jaw", -6.0f, 0.0f, -16.0f, 12, 4, 16);
		head.addChild(jaw);
		(neck = new ModelRenderer(this, "neck")).addBox("box", -5.0f, -5.0f, -5.0f, 10, 10, 10);
		neck.addBox("scale", -1.0f, -9.0f, -3.0f, 2, 4, 6);
		(body = new ModelRenderer(this, "body")).setRotationPoint(0.0f, 4.0f, 8.0f);
		body.addBox("body", -12.0f, 0.0f, -16.0f, 24, 24, 64);
		body.addBox("scale", -1.0f, -6.0f, -10.0f, 2, 6, 12);
		body.addBox("scale", -1.0f, -6.0f, 10.0f, 2, 6, 12);
		body.addBox("scale", -1.0f, -6.0f, 30.0f, 2, 6, 12);
		(wing = new ModelRenderer(this, "wing")).setRotationPoint(-12.0f, 5.0f, 2.0f);
		wing.addBox("bone", -56.0f, -4.0f, -4.0f, 56, 8, 8);
		wing.addBox("skin", -56.0f, 0.0f, 2.0f, 56, 0, 56);
		(wingTip = new ModelRenderer(this, "wingtip")).setRotationPoint(-56.0f, 0.0f, 0.0f);
		wingTip.addBox("bone", -56.0f, -2.0f, -2.0f, 56, 4, 4);
		wingTip.addBox("skin", -56.0f, 0.0f, 2.0f, 56, 0, 56);
		wing.addChild(wingTip);
		(frontLeg = new ModelRenderer(this, "frontleg")).setRotationPoint(-12.0f, 20.0f, 2.0f);
		frontLeg.addBox("main", -4.0f, -4.0f, -4.0f, 8, 24, 8);
		(frontLegTip = new ModelRenderer(this, "frontlegtip")).setRotationPoint(0.0f, 20.0f, -1.0f);
		frontLegTip.addBox("main", -3.0f, -1.0f, -3.0f, 6, 24, 6);
		frontLeg.addChild(frontLegTip);
		(frontFoot = new ModelRenderer(this, "frontfoot")).setRotationPoint(0.0f, 23.0f, 0.0f);
		frontFoot.addBox("main", -4.0f, 0.0f, -12.0f, 8, 4, 16);
		frontLegTip.addChild(frontFoot);
		(rearLeg = new ModelRenderer(this, "rearleg")).setRotationPoint(-16.0f, 16.0f, 42.0f);
		rearLeg.addBox("main", -8.0f, -4.0f, -8.0f, 16, 32, 16);
		(rearLegTip = new ModelRenderer(this, "rearlegtip")).setRotationPoint(0.0f, 32.0f, -4.0f);
		rearLegTip.addBox("main", -6.0f, -2.0f, 0.0f, 12, 32, 12);
		rearLeg.addChild(rearLegTip);
		(rearFoot = new ModelRenderer(this, "rearfoot")).setRotationPoint(0.0f, 31.0f, 4.0f);
		rearFoot.addBox("main", -9.0f, 0.0f, -20.0f, 18, 6, 24);
		rearLegTip.addChild(rearFoot);
	}

	private float func_40307_a(double d) {
		while (d >= 180.0) {
			d -= 360.0;
		}
		while (d < -180.0) {
			d += 360.0;
		}
		return (float) d;
	}

	@Override
	public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
			final float f4, final float f5) {
		final EntityNpcDragon entitydragon = (EntityNpcDragon) entity;
		GlStateManager.pushMatrix();
		final float f6 = entitydragon.field_40173_aw
				+ ((entitydragon.field_40172_ax - entitydragon.field_40173_aw) * field_40317_s);
		jaw.rotateAngleX = (float) (Math.sin(f6 * 3.1415927f * 2.0f) + 1.0) * 0.2f;
		float f7 = (float) (Math.sin((f6 * 3.1415927f * 2.0f) - 1.0f) + 1.0);
		f7 = ((f7 * f7 * 1.0f) + (f7 * 2.0f)) * 0.05f;
		GlStateManager.translate(0.0f, f7 - 2.0f, -3.0f);
		GlStateManager.rotate(f7 * 2.0f, 1.0f, 0.0f, 0.0f);
		float f8 = -30.0f;
		float f9 = 22.0f;
		float f10 = 0.0f;
		final float f11 = 1.5f;
		double[] ad = entitydragon.func_40160_a(6, field_40317_s);
		final float f12 = func_40307_a(
				entitydragon.func_40160_a(5, field_40317_s)[0] - entitydragon.func_40160_a(10, field_40317_s)[0]);
		final float f13 = func_40307_a(entitydragon.func_40160_a(5, field_40317_s)[0] + (f12 / 2.0f));
		f8 += 2.0f;
		float f14 = 0.0f;
		float f15 = f6 * 3.141593f * 2.0f;
		f8 = 20.0f;
		f9 = -12.0f;
		for (int i = 0; i < 5; ++i) {
			final double[] ad2 = entitydragon.func_40160_a(5 - i, field_40317_s);
			f14 = (float) Math.cos((i * 0.45f) + f15) * 0.15f;
			neck.rotateAngleY = ((func_40307_a(ad2[0] - ad[0]) * 3.1415927f) / 180.0f) * f11;
			neck.rotateAngleX = f14 + ((((float) (ad2[1] - ad[1]) * 3.1415927f) / 180.0f) * f11 * 5.0f);
			neck.rotateAngleZ = ((-func_40307_a(ad2[0] - f13) * 3.1415927f) / 180.0f) * f11;
			neck.rotationPointY = f8;
			neck.rotationPointZ = f9;
			neck.rotationPointX = f10;
			f8 += (float) (Math.sin(neck.rotateAngleX) * 10.0);
			f9 -= (float) (Math.cos(neck.rotateAngleY) * Math.cos(neck.rotateAngleX) * 10.0);
			f10 -= (float) (Math.sin(neck.rotateAngleY) * Math.cos(neck.rotateAngleX) * 10.0);
			neck.render(f5);
		}
		head.rotationPointY = f8;
		head.rotationPointZ = f9;
		head.rotationPointX = f10;
		final double[] ad3 = entitydragon.func_40160_a(0, field_40317_s);
		head.rotateAngleY = ((func_40307_a(ad3[0] - ad[0]) * 3.1415927f) / 180.0f) * 1.0f;
		head.rotateAngleZ = ((-func_40307_a(ad3[0] - f13) * 3.1415927f) / 180.0f) * 1.0f;
		head.render(f5);
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0f, 1.0f, 0.0f);
		if (entitydragon.onGround) {
			GlStateManager.rotate(-f12 * f11 * 0.3f, 0.0f, 0.0f, 1.0f);
		} else {
			GlStateManager.rotate(-f12 * f11 * 1.0f, 0.0f, 0.0f, 1.0f);
		}
		GlStateManager.translate(0.0f, -1.0f, 0.0f);
		body.rotateAngleZ = 0.0f;
		body.render(f5);
		if (entitydragon.onGround) {
			for (int j = 0; j < 2; ++j) {
				GlStateManager.enableCull();
				wing.rotateAngleX = 0.25f;
				wing.rotateAngleY = 0.95f;
				wing.rotateAngleZ = -0.5f;
				wingTip.rotateAngleZ = -0.4f;
				frontLeg.rotateAngleX = (MathHelper.cos((float) ((f * 0.6662f) + ((j == 0) ? 0.0 : 3.141592653589793)))
						* 0.6f * f1) + 0.45f + (f7 * 0.5f);
				frontLegTip.rotateAngleX = -1.3f - (f7 * 1.2f);
				frontFoot.rotateAngleX = 0.85f + (f7 * 0.5f);
				frontLeg.render(f5);
				rearLeg.rotateAngleX = (MathHelper.cos((float) ((f * 0.6662f) + ((j == 0) ? 3.141592653589793 : 0.0)))
						* 0.6f * f1) + 0.75f + (f7 * 0.5f);
				rearLegTip.rotateAngleX = -1.6f - (f7 * 0.8f);
				rearLegTip.rotationPointY = 20.0f;
				rearLegTip.rotationPointZ = 2.0f;
				rearFoot.rotateAngleX = 0.85f + (f7 * 0.2f);
				rearLeg.render(f5);
				wing.render(f5);
				GlStateManager.scale(-1.0f, 1.0f, 1.0f);
				if (j == 0) {
					GlStateManager.disableCull();
				}
			}
		} else {
			for (int j = 0; j < 2; ++j) {
				GL11.glEnable(2884);
				final float f16 = f6 * 3.1415927f * 2.0f;
				wing.rotateAngleX = 0.125f - ((float) Math.cos(f16) * 0.2f);
				wing.rotateAngleY = 0.25f;
				wing.rotateAngleZ = (float) (Math.sin(f16) + 0.125) * 0.8f;
				wingTip.rotateAngleZ = -(float) (Math.sin(f16 + 2.0f) + 0.5) * 0.75f;
				rearLegTip.rotationPointY = 32.0f;
				rearLegTip.rotationPointZ = -2.0f;
				rearLeg.rotateAngleX = 1.0f + (f7 * 0.1f);
				rearLegTip.rotateAngleX = 0.5f + (f7 * 0.1f);
				rearFoot.rotateAngleX = 0.75f + (f7 * 0.1f);
				frontLeg.rotateAngleX = 1.3f + (f7 * 0.1f);
				frontLegTip.rotateAngleX = -0.5f - (f7 * 0.1f);
				frontFoot.rotateAngleX = 0.75f + (f7 * 0.1f);
				wing.render(f5);
				frontLeg.render(f5);
				rearLeg.render(f5);
				GlStateManager.scale(-1.0f, 1.0f, 1.0f);
				if (j == 0) {
					GL11.glCullFace(1028);
				}
			}
		}
		GlStateManager.popMatrix();
		GL11.glCullFace(1029);
		GL11.glDisable(2884);
		f14 = -(float) Math.sin(f6 * 3.141593f * 2.0f) * 0.0f;
		f15 = f6 * 3.1415927f * 2.0f;
		f8 = 10.0f;
		f9 = 60.0f;
		f10 = 0.0f;
		ad = entitydragon.func_40160_a(11, field_40317_s);
		for (int k = 0; k < 12; ++k) {
			final double[] ad4 = entitydragon.func_40160_a(12 + k, field_40317_s);
			f14 += (float) (Math.sin((k * 0.45f) + f15) * 0.05000000074505806);
			neck.rotateAngleY = (((func_40307_a(ad4[0] - ad[0]) * f11) + 180.0f) * 3.1415927f) / 180.0f;
			neck.rotateAngleX = f14 + ((((float) (ad4[1] - ad[1]) * 3.1415927f) / 180.0f) * f11 * 5.0f);
			neck.rotateAngleZ = ((func_40307_a(ad4[0] - f13) * 3.1415927f) / 180.0f) * f11;
			neck.rotationPointY = f8;
			neck.rotationPointZ = f9;
			neck.rotationPointX = f10;
			f8 += (float) (Math.sin(neck.rotateAngleX) * 10.0);
			f9 -= (float) (Math.cos(neck.rotateAngleY) * Math.cos(neck.rotateAngleX) * 10.0);
			f10 -= (float) (Math.sin(neck.rotateAngleY) * Math.cos(neck.rotateAngleX) * 10.0);
			neck.render(f5);
		}
		GlStateManager.popMatrix();
	}

	@Override
	public void setLivingAnimations(final EntityLivingBase entityliving, final float f, final float f1,
			final float f2) {
		field_40317_s = f2;
	}
}


package noppes.npcs.client.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.EntityNpcPony;

public class ModelPony extends ModelBase {
	private boolean rainboom;
	private float WingRotateAngleY;
	private float WingRotateAngleZ;
	public ModelRenderer Head;
	public ModelRenderer[] Headpiece;
	public ModelRenderer Helmet;
	public ModelRenderer Body;
	public ModelPlaneRenderer[] Bodypiece;
	public ModelRenderer RightArm;
	public ModelRenderer LeftArm;
	public ModelRenderer RightLeg;
	public ModelRenderer LeftLeg;
	public ModelRenderer unicornarm;
	public ModelPlaneRenderer[] Tail;
	public ModelRenderer[] LeftWing;
	public ModelRenderer[] RightWing;
	public ModelRenderer[] LeftWingExt;
	public ModelRenderer[] RightWingExt;
	public boolean isPegasus;
	public boolean isUnicorn;
	public boolean isFlying;
	public boolean isGlow;
	public boolean isSleeping;
	public boolean isSneak;
	public boolean aimedBow;
	public int heldItemRight;

	public ModelPony(float f) {
		init(f, 0.0f);
	}

	public void init(float strech, float f) {
		float f2 = 0.0f;
		float f3 = 0.0f;
		float f4 = 0.0f;
		(Head = new ModelRenderer(this, 0, 0)).addBox(-4.0f, -4.0f, -6.0f, 8, 8, 8, strech);
		Head.setRotationPoint(f2, f3 + f, f4);
		Headpiece = new ModelRenderer[3];
		(Headpiece[0] = new ModelRenderer(this, 12, 16)).addBox(-4.0f, -6.0f, -1.0f, 2, 2, 2, strech);
		Headpiece[0].setRotationPoint(f2, f3 + f, f4);
		(Headpiece[1] = new ModelRenderer(this, 12, 16)).addBox(2.0f, -6.0f, -1.0f, 2, 2, 2, strech);
		Headpiece[1].setRotationPoint(f2, f3 + f, f4);
		(Headpiece[2] = new ModelRenderer(this, 56, 0)).addBox(-0.5f, -10.0f, -4.0f, 1, 4, 1, strech);
		Headpiece[2].setRotationPoint(f2, f3 + f, f4);
		(Helmet = new ModelRenderer(this, 32, 0)).addBox(-4.0f, -4.0f, -6.0f, 8, 8, 8, strech + 0.5f);
		Helmet.setRotationPoint(f2, f3, f4);
		float f5 = 0.0f;
		float f6 = 0.0f;
		float f7 = 0.0f;
		(Body = new ModelRenderer(this, 16, 16)).addBox(-4.0f, 4.0f, -2.0f, 8, 8, 4, strech);
		Body.setRotationPoint(f5, f6 + f, f7);
		Bodypiece = new ModelPlaneRenderer[13];
		(Bodypiece[0] = new ModelPlaneRenderer(this, 24, 0)).addSidePlane(-4.0f, 4.0f, 2.0f, 8, 8, strech);
		Bodypiece[0].setRotationPoint(f5, f6 + f, f7);
		(Bodypiece[1] = new ModelPlaneRenderer(this, 24, 0)).addSidePlane(4.0f, 4.0f, 2.0f, 8, 8, strech);
		Bodypiece[1].setRotationPoint(f5, f6 + f, f7);
		(Bodypiece[2] = new ModelPlaneRenderer(this, 24, 0)).addTopPlane(-4.0f, 4.0f, 2.0f, 8, 8, strech);
		Bodypiece[2].setRotationPoint(f2, f3 + f, f4);
		(Bodypiece[3] = new ModelPlaneRenderer(this, 24, 0)).addTopPlane(-4.0f, 12.0f, 2.0f, 8, 8, strech);
		Bodypiece[3].setRotationPoint(f2, f3 + f, f4);
		(Bodypiece[4] = new ModelPlaneRenderer(this, 0, 20)).addSidePlane(-4.0f, 4.0f, 10.0f, 8, 4, strech);
		Bodypiece[4].setRotationPoint(f5, f6 + f, f7);
		(Bodypiece[5] = new ModelPlaneRenderer(this, 0, 20)).addSidePlane(4.0f, 4.0f, 10.0f, 8, 4, strech);
		Bodypiece[5].setRotationPoint(f5, f6 + f, f7);
		(Bodypiece[6] = new ModelPlaneRenderer(this, 24, 0)).addTopPlane(-4.0f, 4.0f, 10.0f, 8, 4, strech);
		Bodypiece[6].setRotationPoint(f2, f3 + f, f4);
		(Bodypiece[7] = new ModelPlaneRenderer(this, 24, 0)).addTopPlane(-4.0f, 12.0f, 10.0f, 8, 4, strech);
		Bodypiece[7].setRotationPoint(f2, f3 + f, f4);
		(Bodypiece[8] = new ModelPlaneRenderer(this, 24, 0)).addBackPlane(-4.0f, 4.0f, 14.0f, 8, 8, strech);
		Bodypiece[8].setRotationPoint(f2, f3 + f, f4);
		(Bodypiece[9] = new ModelPlaneRenderer(this, 32, 0)).addTopPlane(-1.0f, 10.0f, 8.0f, 2, 6, strech);
		Bodypiece[9].setRotationPoint(f2, f3 + f, f4);
		(Bodypiece[10] = new ModelPlaneRenderer(this, 32, 0)).addTopPlane(-1.0f, 12.0f, 8.0f, 2, 6, strech);
		Bodypiece[10].setRotationPoint(f2, f3 + f, f4);
		Bodypiece[11] = new ModelPlaneRenderer(this, 32, 0);
		Bodypiece[11].mirror = true;
		Bodypiece[11].addSidePlane(-1.0f, 10.0f, 8.0f, 2, 6, strech);
		Bodypiece[11].setRotationPoint(f2, f3 + f, f4);
		(Bodypiece[12] = new ModelPlaneRenderer(this, 32, 0)).addSidePlane(1.0f, 10.0f, 8.0f, 2, 6, strech);
		Bodypiece[12].setRotationPoint(f2, f3 + f, f4);
		(RightArm = new ModelRenderer(this, 40, 16)).addBox(-2.0f, 4.0f, -2.0f, 4, 12, 4, strech);
		RightArm.setRotationPoint(-3.0f, 8.0f + f, 0.0f);
		LeftArm = new ModelRenderer(this, 40, 16);
		LeftArm.mirror = true;
		LeftArm.addBox(-2.0f, 4.0f, -2.0f, 4, 12, 4, strech);
		LeftArm.setRotationPoint(3.0f, 8.0f + f, 0.0f);
		(RightLeg = new ModelRenderer(this, 40, 16)).addBox(-2.0f, 4.0f, -2.0f, 4, 12, 4, strech);
		RightLeg.setRotationPoint(-3.0f, 0.0f + f, 0.0f);
		LeftLeg = new ModelRenderer(this, 40, 16);
		LeftLeg.mirror = true;
		LeftLeg.addBox(-2.0f, 4.0f, -2.0f, 4, 12, 4, strech);
		LeftLeg.setRotationPoint(3.0f, 0.0f + f, 0.0f);
		(unicornarm = new ModelRenderer(this, 40, 16)).addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4, strech);
		unicornarm.setRotationPoint(-5.0f, 2.0f + f, 0.0f);
		float f8 = 0.0f;
		float f9 = 8.0f;
		float f10 = -14.0f;
		float f11 = 0.0f - f8;
		float f12 = 10.0f - f9;
		float f13 = 0.0f;
		Tail = new ModelPlaneRenderer[10];
		(Tail[0] = new ModelPlaneRenderer(this, 32, 0)).addTopPlane(-2.0f + f8, -7.0f + f9, 16.0f + f10, 4, 4, strech);
		Tail[0].setRotationPoint(f11, f12 + f, f13);
		(Tail[1] = new ModelPlaneRenderer(this, 32, 0)).addTopPlane(-2.0f + f8, 9.0f + f9, 16.0f + f10, 4, 4, strech);
		Tail[1].setRotationPoint(f11, f12 + f, f13);
		(Tail[2] = new ModelPlaneRenderer(this, 32, 0)).addBackPlane(-2.0f + f8, -7.0f + f9, 16.0f + f10, 4, 8, strech);
		Tail[2].setRotationPoint(f11, f12 + f, f13);
		(Tail[3] = new ModelPlaneRenderer(this, 32, 0)).addBackPlane(-2.0f + f8, -7.0f + f9, 20.0f + f10, 4, 8, strech);
		Tail[3].setRotationPoint(f11, f12 + f, f13);
		(Tail[4] = new ModelPlaneRenderer(this, 32, 0)).addBackPlane(-2.0f + f8, 1.0f + f9, 16.0f + f10, 4, 8, strech);
		Tail[4].setRotationPoint(f11, f12 + f, f13);
		(Tail[5] = new ModelPlaneRenderer(this, 32, 0)).addBackPlane(-2.0f + f8, 1.0f + f9, 20.0f + f10, 4, 8, strech);
		Tail[5].setRotationPoint(f11, f12 + f, f13);
		Tail[6] = new ModelPlaneRenderer(this, 36, 0);
		Tail[6].mirror = true;
		Tail[6].addSidePlane(2.0f + f8, -7.0f + f9, 16.0f + f10, 8, 4, strech);
		Tail[6].setRotationPoint(f11, f12 + f, f13);
		(Tail[7] = new ModelPlaneRenderer(this, 36, 0)).addSidePlane(-2.0f + f8, -7.0f + f9, 16.0f + f10, 8, 4, strech);
		Tail[7].setRotationPoint(f11, f12 + f, f13);
		Tail[8] = new ModelPlaneRenderer(this, 36, 0);
		Tail[8].mirror = true;
		Tail[8].addSidePlane(2.0f + f8, 1.0f + f9, 16.0f + f10, 8, 4, strech);
		Tail[8].setRotationPoint(f11, f12 + f, f13);
		(Tail[9] = new ModelPlaneRenderer(this, 36, 0)).addSidePlane(-2.0f + f8, 1.0f + f9, 16.0f + f10, 8, 4, strech);
		Tail[9].setRotationPoint(f11, f12 + f, f13);
		float f14 = 0.0f;
		float f15 = 0.0f;
		float f16 = 0.0f;
		(LeftWing = new ModelRenderer[3])[0] = new ModelRenderer(this, 56, 16);
		LeftWing[0].mirror = true;
		LeftWing[0].addBox(4.0f, 5.0f, 2.0f, 2, 6, 2, strech);
		LeftWing[0].setRotationPoint(f14, f15 + f, f16);
		LeftWing[1] = new ModelRenderer(this, 56, 16);
		LeftWing[1].mirror = true;
		LeftWing[1].addBox(4.0f, 5.0f, 4.0f, 2, 8, 2, strech);
		LeftWing[1].setRotationPoint(f14, f15 + f, f16);
		LeftWing[2] = new ModelRenderer(this, 56, 16);
		LeftWing[2].mirror = true;
		LeftWing[2].addBox(4.0f, 5.0f, 6.0f, 2, 6, 2, strech);
		LeftWing[2].setRotationPoint(f14, f15 + f, f16);
		RightWing = new ModelRenderer[3];
		(RightWing[0] = new ModelRenderer(this, 56, 16)).addBox(-6.0f, 5.0f, 2.0f, 2, 6, 2, strech);
		RightWing[0].setRotationPoint(f14, f15 + f, f16);
		(RightWing[1] = new ModelRenderer(this, 56, 16)).addBox(-6.0f, 5.0f, 4.0f, 2, 8, 2, strech);
		RightWing[1].setRotationPoint(f14, f15 + f, f16);
		(RightWing[2] = new ModelRenderer(this, 56, 16)).addBox(-6.0f, 5.0f, 6.0f, 2, 6, 2, strech);
		RightWing[2].setRotationPoint(f14, f15 + f, f16);
		float f17 = f2 + 4.5f;
		float f18 = f3 + 5.0f;
		float f19 = f4 + 6.0f;
		(LeftWingExt = new ModelRenderer[7])[0] = new ModelRenderer(this, 56, 19);
		LeftWingExt[0].mirror = true;
		LeftWingExt[0].addBox(0.0f, 0.0f, 0.0f, 1, 8, 2, strech + 0.1f);
		LeftWingExt[0].setRotationPoint(f17, f18 + f, f19);
		LeftWingExt[1] = new ModelRenderer(this, 56, 19);
		LeftWingExt[1].mirror = true;
		LeftWingExt[1].addBox(0.0f, 8.0f, 0.0f, 1, 6, 2, strech + 0.1f);
		LeftWingExt[1].setRotationPoint(f17, f18 + f, f19);
		LeftWingExt[2] = new ModelRenderer(this, 56, 19);
		LeftWingExt[2].mirror = true;
		LeftWingExt[2].addBox(0.0f, -1.2f, -0.2f, 1, 8, 2, strech - 0.2f);
		LeftWingExt[2].setRotationPoint(f17, f18 + f, f19);
		LeftWingExt[3] = new ModelRenderer(this, 56, 19);
		LeftWingExt[3].mirror = true;
		LeftWingExt[3].addBox(0.0f, 1.8f, 1.3f, 1, 8, 2, strech - 0.1f);
		LeftWingExt[3].setRotationPoint(f17, f18 + f, f19);
		LeftWingExt[4] = new ModelRenderer(this, 56, 19);
		LeftWingExt[4].mirror = true;
		LeftWingExt[4].addBox(0.0f, 5.0f, 2.0f, 1, 8, 2, strech);
		LeftWingExt[4].setRotationPoint(f17, f18 + f, f19);
		LeftWingExt[5] = new ModelRenderer(this, 56, 19);
		LeftWingExt[5].mirror = true;
		LeftWingExt[5].addBox(0.0f, 0.0f, -0.2f, 1, 6, 2, strech + 0.3f);
		LeftWingExt[5].setRotationPoint(f17, f18 + f, f19);
		LeftWingExt[6] = new ModelRenderer(this, 56, 19);
		LeftWingExt[6].mirror = true;
		LeftWingExt[6].addBox(0.0f, 0.0f, 0.2f, 1, 3, 2, strech + 0.2f);
		LeftWingExt[6].setRotationPoint(f17, f18 + f, f19);
		float f20 = f2 - 4.5f;
		float f21 = f3 + 5.0f;
		float f22 = f4 + 6.0f;
		(RightWingExt = new ModelRenderer[7])[0] = new ModelRenderer(this, 56, 19);
		RightWingExt[0].mirror = true;
		RightWingExt[0].addBox(0.0f, 0.0f, 0.0f, 1, 8, 2, strech + 0.1f);
		RightWingExt[0].setRotationPoint(f20, f21 + f, f22);
		RightWingExt[1] = new ModelRenderer(this, 56, 19);
		RightWingExt[1].mirror = true;
		RightWingExt[1].addBox(0.0f, 8.0f, 0.0f, 1, 6, 2, strech + 0.1f);
		RightWingExt[1].setRotationPoint(f20, f21 + f, f22);
		RightWingExt[2] = new ModelRenderer(this, 56, 19);
		RightWingExt[2].mirror = true;
		RightWingExt[2].addBox(0.0f, -1.2f, -0.2f, 1, 8, 2, strech - 0.2f);
		RightWingExt[2].setRotationPoint(f20, f21 + f, f22);
		RightWingExt[3] = new ModelRenderer(this, 56, 19);
		RightWingExt[3].mirror = true;
		RightWingExt[3].addBox(0.0f, 1.8f, 1.3f, 1, 8, 2, strech - 0.1f);
		RightWingExt[3].setRotationPoint(f20, f21 + f, f22);
		RightWingExt[4] = new ModelRenderer(this, 56, 19);
		RightWingExt[4].mirror = true;
		RightWingExt[4].addBox(0.0f, 5.0f, 2.0f, 1, 8, 2, strech);
		RightWingExt[4].setRotationPoint(f20, f21 + f, f22);
		RightWingExt[5] = new ModelRenderer(this, 56, 19);
		RightWingExt[5].mirror = true;
		RightWingExt[5].addBox(0.0f, 0.0f, -0.2f, 1, 6, 2, strech + 0.3f);
		RightWingExt[5].setRotationPoint(f20, f21 + f, f22);
		RightWingExt[6] = new ModelRenderer(this, 56, 19);
		RightWingExt[6].mirror = true;
		RightWingExt[6].addBox(0.0f, 0.0f, 0.2f, 1, 3, 2, strech + 0.2f);
		RightWingExt[6].setRotationPoint(f20, f21 + f, f22);
		WingRotateAngleY = LeftWingExt[0].rotateAngleY;
		WingRotateAngleZ = LeftWingExt[0].rotateAngleZ;
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		EntityNpcPony pony = (EntityNpcPony) entity;
		if ((pony.textureLocation != pony.checked) && (pony.textureLocation != null)) {
			try {
				IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(pony.textureLocation);
				BufferedImage bufferedimage = ImageIO.read(resource.getInputStream());
				pony.isPegasus = false;
				pony.isUnicorn = false;
				Color color = new Color(bufferedimage.getRGB(0, 0), true);
				Color color2 = new Color(249, 177, 49, 255);
				Color color3 = new Color(136, 202, 240, 255);
				Color color4 = new Color(209, 159, 228, 255);
				Color color5 = new Color(254, 249, 252, 255);
				if (color.equals(color2)) {
				}
				if (color.equals(color3)) {
					pony.isPegasus = true;
				}
				if (color.equals(color4)) {
					pony.isUnicorn = true;
				}
				if (color.equals(color5)) {
					pony.isPegasus = true;
					pony.isUnicorn = true;
				}
				pony.checked = pony.textureLocation;
			} catch (IOException ex) {
			}
		}
		isSleeping = pony.isPlayerSleeping();
		isUnicorn = pony.isUnicorn;
		isPegasus = pony.isPegasus;
		isSneak = pony.isSneaking();
		heldItemRight = ((pony.getHeldItem() != null) ? 1 : 0);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		GlStateManager.pushMatrix();
		if (isSleeping) {
			GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
			GlStateManager.translate(0.0f, -0.5f, -0.9f);
		}
		Head.render(f5);
		Headpiece[0].render(f5);
		Headpiece[1].render(f5);
		if (isUnicorn) {
			Headpiece[2].render(f5);
		}
		Helmet.render(f5);
		Body.render(f5);
		for (int i = 0; i < Bodypiece.length; ++i) {
			Bodypiece[i].render(f5);
		}
		LeftArm.render(f5);
		RightArm.render(f5);
		LeftLeg.render(f5);
		RightLeg.render(f5);
		for (int j = 0; j < Tail.length; ++j) {
			Tail[j].render(f5);
		}
		if (isPegasus) {
			if (isFlying || isSneak) {
				for (int k = 0; k < LeftWingExt.length; ++k) {
					LeftWingExt[k].render(f5);
				}
				for (int l = 0; l < RightWingExt.length; ++l) {
					RightWingExt[l].render(f5);
				}
			} else {
				for (int i2 = 0; i2 < LeftWing.length; ++i2) {
					LeftWing[i2].render(f5);
				}
				for (int j2 = 0; j2 < RightWing.length; ++j2) {
					RightWing[j2].render(f5);
				}
			}
		}
		GlStateManager.popMatrix();
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		EntityNPCInterface npc = (EntityNPCInterface) entity;
		isRiding = npc.isRiding();
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
		Head.rotateAngleY = f6;
		Head.rotateAngleX = f7;
		Headpiece[0].rotateAngleY = f6;
		Headpiece[0].rotateAngleX = f7;
		Headpiece[1].rotateAngleY = f6;
		Headpiece[1].rotateAngleX = f7;
		Headpiece[2].rotateAngleY = f6;
		Headpiece[2].rotateAngleX = f7;
		Helmet.rotateAngleY = f6;
		Helmet.rotateAngleX = f7;
		Headpiece[2].rotateAngleX = f7 + 0.5f;
		float f8;
		float f9;
		float f10;
		float f11;
		if (!isFlying || !isPegasus) {
			f8 = MathHelper.cos((f * 0.6662f) + 3.141593f) * 0.6f * f1;
			f9 = MathHelper.cos(f * 0.6662f) * 0.6f * f1;
			f10 = MathHelper.cos(f * 0.6662f) * 0.3f * f1;
			f11 = MathHelper.cos((f * 0.6662f) + 3.141593f) * 0.3f * f1;
			RightArm.rotateAngleY = 0.0f;
			unicornarm.rotateAngleY = 0.0f;
			LeftArm.rotateAngleY = 0.0f;
			RightLeg.rotateAngleY = 0.0f;
			LeftLeg.rotateAngleY = 0.0f;
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
			RightArm.rotateAngleY = 0.2f;
			LeftArm.rotateAngleY = -0.2f;
			RightLeg.rotateAngleY = -0.2f;
			LeftLeg.rotateAngleY = 0.2f;
		}
		if (isSleeping) {
			f8 = 4.712f;
			f9 = 4.712f;
			f10 = 1.571f;
			f11 = 1.571f;
		}
		RightArm.rotateAngleX = f8;
		unicornarm.rotateAngleX = 0.0f;
		LeftArm.rotateAngleX = f9;
		RightLeg.rotateAngleX = f10;
		LeftLeg.rotateAngleX = f11;
		RightArm.rotateAngleZ = 0.0f;
		unicornarm.rotateAngleZ = 0.0f;
		LeftArm.rotateAngleZ = 0.0f;
		for (int i = 0; i < Tail.length; ++i) {
			if (rainboom) {
				Tail[i].rotateAngleZ = 0.0f;
			} else {
				Tail[i].rotateAngleZ = MathHelper.cos(f * 0.8f) * 0.2f * f1;
			}
		}
		if ((heldItemRight != 0) && !rainboom && !isUnicorn) {
			RightArm.rotateAngleX = (RightArm.rotateAngleX * 0.5f) - 0.3141593f;
		}
		float f12 = 0.0f;
		if ((f5 > -9990.0f) && !isUnicorn) {
			f12 = MathHelper.sin(MathHelper.sqrt_float(f5) * 3.141593f * 2.0f) * 0.2f;
		}
		Body.rotateAngleY = (float) (f12 * 0.2);
		for (int j = 0; j < Bodypiece.length; ++j) {
			Bodypiece[j].rotateAngleY = (float) (f12 * 0.2);
		}
		for (int k = 0; k < LeftWing.length; ++k) {
			LeftWing[k].rotateAngleY = (float) (f12 * 0.2);
		}
		for (int l = 0; l < RightWing.length; ++l) {
			RightWing[l].rotateAngleY = (float) (f12 * 0.2);
		}
		for (int i2 = 0; i2 < Tail.length; ++i2) {
			Tail[i2].rotateAngleY = f12;
		}
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
			RightArm.rotationPointZ = f13 + 2.0f;
			LeftArm.rotationPointZ = (0.0f - f13) + 2.0f;
		} else {
			RightArm.rotationPointZ = f13 + 1.0f;
			LeftArm.rotationPointZ = (0.0f - f13) + 1.0f;
		}
		RightArm.rotationPointX = (0.0f - f14 - 1.0f) + f15;
		LeftArm.rotationPointX = (f14 + 1.0f) - f15;
		RightLeg.rotationPointX = (0.0f - f14 - 1.0f) + f15;
		LeftLeg.rotationPointX = (f14 + 1.0f) - f15;
		ModelRenderer rightArm = RightArm;
		rightArm.rotateAngleY += Body.rotateAngleY;
		ModelRenderer leftArm = LeftArm;
		leftArm.rotateAngleY += Body.rotateAngleY;
		ModelRenderer leftArm2 = LeftArm;
		leftArm2.rotateAngleX += Body.rotateAngleY;
		RightArm.rotationPointY = 8.0f;
		LeftArm.rotationPointY = 8.0f;
		RightLeg.rotationPointY = 4.0f;
		LeftLeg.rotationPointY = 4.0f;
		if (f5 > -9990.0f) {
			float f16 = 1.0f - f5;
			f16 *= f16 * f16;
			f16 = 1.0f - f16;
			float f17 = MathHelper.sin(f16 * 3.141593f);
			float f18 = MathHelper.sin(f5 * 3.141593f);
			float f19 = f18 * -(Head.rotateAngleX - 0.7f) * 0.75f;
			if (isUnicorn) {
				ModelRenderer unicornarm = this.unicornarm;
				unicornarm.rotateAngleX -= (float) ((f17 * 1.2) + f19);
				ModelRenderer unicornarm2 = this.unicornarm;
				unicornarm2.rotateAngleY += Body.rotateAngleY * 2.0f;
				this.unicornarm.rotateAngleZ = f18 * -0.4f;
			} else {
				ModelRenderer unicornarm3 = unicornarm;
				unicornarm3.rotateAngleX -= (float) ((f17 * 1.2) + f19);
				ModelRenderer unicornarm4 = unicornarm;
				unicornarm4.rotateAngleY += Body.rotateAngleY * 2.0f;
				unicornarm.rotateAngleZ = f18 * -0.4f;
			}
		}
		if (isSneak && !isFlying) {
			float f20 = 0.4f;
			float f21 = 7.0f;
			float f22 = -4.0f;
			Body.rotateAngleX = f20;
			Body.rotationPointY = f21;
			Body.rotationPointZ = f22;
			for (int i3 = 0; i3 < Bodypiece.length; ++i3) {
				Bodypiece[i3].rotateAngleX = f20;
				Bodypiece[i3].rotationPointY = f21;
				Bodypiece[i3].rotationPointZ = f22;
			}
			float f23 = 3.5f;
			float f24 = 6.0f;
			for (int i4 = 0; i4 < LeftWingExt.length; ++i4) {
				LeftWingExt[i4].rotateAngleX = (float) (f20 + 2.3561947345733643);
				LeftWingExt[i4].rotationPointY = f21 + f23;
				LeftWingExt[i4].rotationPointZ = f22 + f24;
				LeftWingExt[i4].rotateAngleX = 2.5f;
				LeftWingExt[i4].rotateAngleZ = -6.0f;
			}
			float f25 = 4.5f;
			float f26 = 6.0f;
			for (int i5 = 0; i5 < LeftWingExt.length; ++i5) {
				RightWingExt[i5].rotateAngleX = (float) (f20 + 2.3561947345733643);
				RightWingExt[i5].rotationPointY = f21 + f25;
				RightWingExt[i5].rotationPointZ = f22 + f26;
				RightWingExt[i5].rotateAngleX = 2.5f;
				RightWingExt[i5].rotateAngleZ = 6.0f;
			}
			ModelRenderer rightLeg = RightLeg;
			rightLeg.rotateAngleX -= 0.0f;
			ModelRenderer leftLeg = LeftLeg;
			leftLeg.rotateAngleX -= 0.0f;
			ModelRenderer rightArm2 = RightArm;
			rightArm2.rotateAngleX -= 0.4f;
			ModelRenderer unicornarm5 = unicornarm;
			unicornarm5.rotateAngleX += 0.4f;
			ModelRenderer leftArm3 = LeftArm;
			leftArm3.rotateAngleX -= 0.4f;
			RightLeg.rotationPointZ = 10.0f;
			LeftLeg.rotationPointZ = 10.0f;
			RightLeg.rotationPointY = 7.0f;
			LeftLeg.rotationPointY = 7.0f;
			float f27;
			float f28;
			float f29;
			if (isSleeping) {
				f27 = 2.0f;
				f28 = -1.0f;
				f29 = 1.0f;
			} else {
				f27 = 6.0f;
				f28 = -2.0f;
				f29 = 0.0f;
			}
			Head.rotationPointY = f27;
			Head.rotationPointZ = f28;
			Head.rotationPointX = f29;
			Helmet.rotationPointY = f27;
			Helmet.rotationPointZ = f28;
			Helmet.rotationPointX = f29;
			Headpiece[0].rotationPointY = f27;
			Headpiece[0].rotationPointZ = f28;
			Headpiece[0].rotationPointX = f29;
			Headpiece[1].rotationPointY = f27;
			Headpiece[1].rotationPointZ = f28;
			Headpiece[1].rotationPointX = f29;
			Headpiece[2].rotationPointY = f27;
			Headpiece[2].rotationPointZ = f28;
			Headpiece[2].rotationPointX = f29;
			float f30 = 0.0f;
			float f31 = 8.0f;
			float f32 = -14.0f;
			float f33 = 0.0f - f30;
			float f34 = 9.0f - f31;
			float f35 = -4.0f - f32;
			float f36 = 0.0f;
			for (int i6 = 0; i6 < Tail.length; ++i6) {
				Tail[i6].rotationPointX = f33;
				Tail[i6].rotationPointY = f34;
				Tail[i6].rotationPointZ = f35;
				Tail[i6].rotateAngleX = f36;
			}
		} else {
			float f37 = 0.0f;
			float f38 = 0.0f;
			float f39 = 0.0f;
			Body.rotateAngleX = f37;
			Body.rotationPointY = f38;
			Body.rotationPointZ = f39;
			for (int j2 = 0; j2 < Bodypiece.length; ++j2) {
				Bodypiece[j2].rotateAngleX = f37;
				Bodypiece[j2].rotationPointY = f38;
				Bodypiece[j2].rotationPointZ = f39;
			}
			if (isPegasus) {
				if (!isFlying) {
					for (int k2 = 0; k2 < LeftWing.length; ++k2) {
						LeftWing[k2].rotateAngleX = (float) (f37 + 1.5707964897155762);
						LeftWing[k2].rotationPointY = f38 + 13.0f;
						LeftWing[k2].rotationPointZ = f39 - 3.0f;
					}
					for (int l2 = 0; l2 < RightWing.length; ++l2) {
						RightWing[l2].rotateAngleX = (float) (f37 + 1.5707964897155762);
						RightWing[l2].rotationPointY = f38 + 13.0f;
						RightWing[l2].rotationPointZ = f39 - 3.0f;
					}
				} else {
					float f40 = 5.5f;
					float f41 = 3.0f;
					for (int j3 = 0; j3 < LeftWingExt.length; ++j3) {
						LeftWingExt[j3].rotateAngleX = (float) (f37 + 1.5707964897155762);
						LeftWingExt[j3].rotationPointY = f38 + f40;
						LeftWingExt[j3].rotationPointZ = f39 + f41;
					}
					float f42 = 6.5f;
					float f43 = 3.0f;
					for (int j4 = 0; j4 < RightWingExt.length; ++j4) {
						RightWingExt[j4].rotateAngleX = (float) (f37 + 1.5707964897155762);
						RightWingExt[j4].rotationPointY = f38 + f42;
						RightWingExt[j4].rotationPointZ = f39 + f43;
					}
				}
			}
			RightLeg.rotationPointZ = 10.0f;
			LeftLeg.rotationPointZ = 10.0f;
			RightLeg.rotationPointY = 8.0f;
			LeftLeg.rotationPointY = 8.0f;
			float f44 = (MathHelper.cos(f2 * 0.09f) * 0.05f) + 0.05f;
			float f45 = MathHelper.sin(f2 * 0.067f) * 0.05f;
			ModelRenderer unicornarm6 = unicornarm;
			unicornarm6.rotateAngleZ += f44;
			ModelRenderer unicornarm7 = unicornarm;
			unicornarm7.rotateAngleX += f45;
			if (isPegasus && isFlying) {
				WingRotateAngleY = MathHelper.sin(f2 * 0.067f * 8.0f) * 1.0f;
				WingRotateAngleZ = MathHelper.sin(f2 * 0.067f * 8.0f) * 1.0f;
				for (int k3 = 0; k3 < LeftWingExt.length; ++k3) {
					LeftWingExt[k3].rotateAngleX = 2.5f;
					LeftWingExt[k3].rotateAngleZ = -WingRotateAngleZ - 4.712f - 0.4f;
				}
				for (int l3 = 0; l3 < RightWingExt.length; ++l3) {
					RightWingExt[l3].rotateAngleX = 2.5f;
					RightWingExt[l3].rotateAngleZ = WingRotateAngleZ + 4.712f + 0.4f;
				}
			}
			float f46;
			float f47;
			float f48;
			if (isSleeping) {
				f46 = 2.0f;
				f47 = 1.0f;
				f48 = 1.0f;
			} else {
				f46 = 0.0f;
				f47 = 0.0f;
				f48 = 0.0f;
			}
			Head.rotationPointY = f46;
			Head.rotationPointZ = f47;
			Head.rotationPointX = f48;
			Helmet.rotationPointY = f46;
			Helmet.rotationPointZ = f47;
			Helmet.rotationPointX = f48;
			Headpiece[0].rotationPointY = f46;
			Headpiece[0].rotationPointZ = f47;
			Headpiece[0].rotationPointX = f48;
			Headpiece[1].rotationPointY = f46;
			Headpiece[1].rotationPointZ = f47;
			Headpiece[1].rotationPointX = f48;
			Headpiece[2].rotationPointY = f46;
			Headpiece[2].rotationPointZ = f47;
			Headpiece[2].rotationPointX = f48;
			float f49 = 0.0f;
			float f50 = 8.0f;
			float f51 = -14.0f;
			float f52 = 0.0f - f49;
			float f53 = 9.0f - f50;
			float f54 = 0.0f - f51;
			float f55 = 0.5f * f1;
			for (int k4 = 0; k4 < Tail.length; ++k4) {
				Tail[k4].rotationPointX = f52;
				Tail[k4].rotationPointY = f53;
				Tail[k4].rotationPointZ = f54;
				if (rainboom) {
					Tail[k4].rotateAngleX = 1.571f + (0.1f * MathHelper.sin(f));
				} else {
					Tail[k4].rotateAngleX = f55;
				}
			}
			for (int l4 = 0; l4 < Tail.length; ++l4) {
				if (!rainboom) {
					ModelPlaneRenderer modelPlaneRenderer = Tail[l4];
					modelPlaneRenderer.rotateAngleX += f45;
				}
			}
		}
		LeftWingExt[2].rotateAngleX -= 0.85f;
		LeftWingExt[3].rotateAngleX -= 0.75f;
		LeftWingExt[4].rotateAngleX -= 0.5f;
		LeftWingExt[6].rotateAngleX -= 0.85f;
		RightWingExt[2].rotateAngleX -= 0.85f;
		RightWingExt[3].rotateAngleX -= 0.75f;
		RightWingExt[4].rotateAngleX -= 0.5f;
		RightWingExt[6].rotateAngleX -= 0.85f;
		Bodypiece[9].rotateAngleX += 0.5f;
		Bodypiece[10].rotateAngleX += 0.5f;
		Bodypiece[11].rotateAngleX += 0.5f;
		Bodypiece[12].rotateAngleX += 0.5f;
		if (rainboom) {
			for (int j5 = 0; j5 < Tail.length; ++j5) {
				Tail[j5].rotationPointY += 6.0f;
				++Tail[j5].rotationPointZ;
			}
		}
		if (isSleeping) {
			RightArm.rotationPointZ += 6.0f;
			LeftArm.rotationPointZ += 6.0f;
			RightLeg.rotationPointZ -= 8.0f;
			LeftLeg.rotationPointZ -= 8.0f;
			RightArm.rotationPointY += 2.0f;
			LeftArm.rotationPointY += 2.0f;
			RightLeg.rotationPointY += 2.0f;
			LeftLeg.rotationPointY += 2.0f;
		}
		if (aimedBow) {
			if (isUnicorn) {
				float f56 = 0.0f;
				float f57 = 0.0f;
				unicornarm.rotateAngleZ = 0.0f;
				unicornarm.rotateAngleY = -(0.1f - (f56 * 0.6f)) + Head.rotateAngleY;
				unicornarm.rotateAngleX = 4.712f + Head.rotateAngleX;
				ModelRenderer unicornarm8 = unicornarm;
				unicornarm8.rotateAngleX -= (f56 * 1.2f) - (f57 * 0.4f);
				ModelRenderer unicornarm9 = unicornarm;
				unicornarm9.rotateAngleZ += (MathHelper.cos(f2 * 0.09f) * 0.05f) + 0.05f;
				ModelRenderer unicornarm10 = unicornarm;
				unicornarm10.rotateAngleX += MathHelper.sin(f2 * 0.067f) * 0.05f;
			} else {
				float f58 = 0.0f;
				float f59 = 0.0f;
				RightArm.rotateAngleZ = 0.0f;
				RightArm.rotateAngleY = -(0.1f - (f58 * 0.6f)) + Head.rotateAngleY;
				RightArm.rotateAngleX = 4.712f + Head.rotateAngleX;
				ModelRenderer rightArm3 = RightArm;
				rightArm3.rotateAngleX -= (f58 * 1.2f) - (f59 * 0.4f);
				ModelRenderer rightArm4 = RightArm;
				rightArm4.rotateAngleZ += (MathHelper.cos(f2 * 0.09f) * 0.05f) + 0.05f;
				ModelRenderer rightArm5 = RightArm;
				rightArm5.rotateAngleX += MathHelper.sin(f2 * 0.067f) * 0.05f;
				++RightArm.rotationPointZ;
			}
		}
	}
}

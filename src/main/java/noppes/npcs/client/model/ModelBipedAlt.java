//

//

package noppes.npcs.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import noppes.npcs.ModelData;
import noppes.npcs.ModelPartConfig;
import noppes.npcs.client.model.animation.AniBow;
import noppes.npcs.client.model.animation.AniCrawling;
import noppes.npcs.client.model.animation.AniDancing;
import noppes.npcs.client.model.animation.AniHug;
import noppes.npcs.client.model.animation.AniNo;
import noppes.npcs.client.model.animation.AniWaving;
import noppes.npcs.client.model.animation.AniYes;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.roles.JobPuppet;

public class ModelBipedAlt extends ModelBiped {
	private Map<EnumParts, List<ModelScaleRenderer>> map;

	public ModelBipedAlt(final float scale) {
		super(scale);
		map = new HashMap<EnumParts, List<ModelScaleRenderer>>();
		bipedLeftArm = createScale(bipedLeftArm, EnumParts.ARM_LEFT);
		bipedRightArm = createScale(bipedRightArm, EnumParts.ARM_RIGHT);
		bipedLeftLeg = createScale(bipedLeftLeg, EnumParts.LEG_LEFT);
		bipedRightLeg = createScale(bipedRightLeg, EnumParts.LEG_RIGHT);
		bipedHead = createScale(bipedHead, EnumParts.HEAD);
		bipedHeadwear = createScale(bipedHeadwear, EnumParts.HEAD);
		bipedBody = createScale(bipedBody, EnumParts.BODY);
	}

	private ModelScaleRenderer createScale(final ModelRenderer renderer, final EnumParts part) {
		final int textureX = (Integer) ObfuscationReflectionHelper.getPrivateValue((Class) ModelRenderer.class,
				renderer, 2);
		final int textureY = (Integer) ObfuscationReflectionHelper.getPrivateValue((Class) ModelRenderer.class,
				renderer, 3);
		final ModelScaleRenderer model = new ModelScaleRenderer(this, textureX, textureY, part);
		model.textureHeight = renderer.textureHeight;
		model.textureWidth = renderer.textureWidth;
		model.childModels = renderer.childModels;
		model.cubeList = renderer.cubeList;
		copyModelAngles(renderer, model);
		List<ModelScaleRenderer> list = map.get(part);
		if (list == null) {
			map.put(part, list = new ArrayList<ModelScaleRenderer>());
		}
		list.add(model);
		return model;
	}

	@Override
	public ModelRenderer getRandomModelBox(final Random random) {
		switch (random.nextInt(5)) {
		case 0: {
			return bipedHead;
		}
		case 1: {
			return bipedBody;
		}
		case 2: {
			return bipedLeftArm;
		}
		case 3: {
			return bipedRightArm;
		}
		case 4: {
			return bipedLeftLeg;
		}
		case 5: {
			return bipedRightLeg;
		}
		default: {
			return bipedHead;
		}
		}
	}

	@Override
	public void setRotationAngles(final float par1, final float par2, final float par3, final float par4,
			final float par5, final float par6, final Entity entity) {
		final EntityCustomNpc player = (EntityCustomNpc) entity;
		final ModelData playerdata = player.modelData;
		for (final EnumParts part : map.keySet()) {
			final ModelPartConfig config = playerdata.getPartConfig(part);
			for (final ModelScaleRenderer model : map.get(part)) {
				model.config = config;
			}
		}
		if (!isRiding) {
			isRiding = (player.currentAnimation == 1);
		}
		if (isSneak && ((player.currentAnimation == 7) || player.isPlayerSleeping())) {
			isSneak = false;
		}
		aimedBow = (player.currentAnimation == 6);
		isSneak = player.isSneaking();
		final ModelRenderer bipedBody = this.bipedBody;
		final ModelRenderer bipedBody2 = this.bipedBody;
		final ModelRenderer bipedBody3 = this.bipedBody;
		final float rotationPointX = 0.0f;
		bipedBody3.rotationPointZ = rotationPointX;
		bipedBody2.rotationPointY = rotationPointX;
		bipedBody.rotationPointX = rotationPointX;
		final ModelRenderer bipedBody4 = this.bipedBody;
		final ModelRenderer bipedBody5 = this.bipedBody;
		final ModelRenderer bipedBody6 = this.bipedBody;
		final float rotateAngleX = 0.0f;
		bipedBody6.rotateAngleZ = rotateAngleX;
		bipedBody5.rotateAngleY = rotateAngleX;
		bipedBody4.rotateAngleX = rotateAngleX;
		final ModelRenderer bipedHeadwear = this.bipedHeadwear;
		final ModelRenderer bipedHead = this.bipedHead;
		final float n = 0.0f;
		bipedHead.rotateAngleX = n;
		bipedHeadwear.rotateAngleX = n;
		final ModelRenderer bipedHeadwear2 = this.bipedHeadwear;
		final ModelRenderer bipedHead2 = this.bipedHead;
		final float n2 = 0.0f;
		bipedHead2.rotateAngleZ = n2;
		bipedHeadwear2.rotateAngleZ = n2;
		final ModelRenderer bipedHeadwear3 = this.bipedHeadwear;
		final ModelRenderer bipedHead3 = this.bipedHead;
		final float n3 = 0.0f;
		bipedHead3.rotationPointX = n3;
		bipedHeadwear3.rotationPointX = n3;
		final ModelRenderer bipedHeadwear4 = this.bipedHeadwear;
		final ModelRenderer bipedHead4 = this.bipedHead;
		final float n4 = 0.0f;
		bipedHead4.rotationPointY = n4;
		bipedHeadwear4.rotationPointY = n4;
		final ModelRenderer bipedHeadwear5 = this.bipedHeadwear;
		final ModelRenderer bipedHead5 = this.bipedHead;
		final float n5 = 0.0f;
		bipedHead5.rotationPointZ = n5;
		bipedHeadwear5.rotationPointZ = n5;
		bipedLeftLeg.rotateAngleX = 0.0f;
		bipedLeftLeg.rotateAngleY = 0.0f;
		bipedLeftLeg.rotateAngleZ = 0.0f;
		bipedRightLeg.rotateAngleX = 0.0f;
		bipedRightLeg.rotateAngleY = 0.0f;
		bipedRightLeg.rotateAngleZ = 0.0f;
		bipedLeftArm.rotationPointX = 0.0f;
		bipedLeftArm.rotationPointY = 2.0f;
		bipedLeftArm.rotationPointZ = 0.0f;
		bipedRightArm.rotationPointX = 0.0f;
		bipedRightArm.rotationPointY = 2.0f;
		bipedRightArm.rotationPointZ = 0.0f;
		super.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
		if (player.isPlayerSleeping()) {
			if (this.bipedHead.rotateAngleX < 0.0f) {
				this.bipedHead.rotateAngleX = 0.0f;
				this.bipedHeadwear.rotateAngleX = 0.0f;
			}
		} else if (player.currentAnimation == 9) {
			final ModelRenderer bipedHeadwear6 = this.bipedHeadwear;
			final ModelRenderer bipedHead6 = this.bipedHead;
			final float n6 = 0.7f;
			bipedHead6.rotateAngleX = n6;
			bipedHeadwear6.rotateAngleX = n6;
		} else if (player.currentAnimation == 8) {
			AniHug.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this);
		} else if (player.currentAnimation == 7) {
			AniCrawling.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this);
		} else if (player.currentAnimation == 10) {
			AniWaving.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this);
		} else if (player.currentAnimation == 5) {
			AniDancing.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this);
		} else if (player.currentAnimation == 11) {
			AniBow.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this);
		} else if (player.currentAnimation == 13) {
			AniYes.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this);
		} else if (player.currentAnimation == 12) {
			AniNo.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this);
		} else if (isSneak) {
			this.bipedBody.rotateAngleX = 0.5f / playerdata.getPartConfig(EnumParts.BODY).scaleY;
		}
		if (player.advanced.job == 9) {
			final JobPuppet job = (JobPuppet) player.jobInterface;
			if (job.isActive()) {
				final float pi = 3.1415927f;
				if (!job.head.disabled) {
					final ModelRenderer bipedHeadwear7 = this.bipedHeadwear;
					final ModelRenderer bipedHead7 = this.bipedHead;
					final float n7 = job.head.rotationX * pi;
					bipedHead7.rotateAngleX = n7;
					bipedHeadwear7.rotateAngleX = n7;
					final ModelRenderer bipedHeadwear8 = this.bipedHeadwear;
					final ModelRenderer bipedHead8 = this.bipedHead;
					final float n8 = job.head.rotationY * pi;
					bipedHead8.rotateAngleY = n8;
					bipedHeadwear8.rotateAngleY = n8;
					final ModelRenderer bipedHeadwear9 = this.bipedHeadwear;
					final ModelRenderer bipedHead9 = this.bipedHead;
					final float n9 = job.head.rotationZ * pi;
					bipedHead9.rotateAngleZ = n9;
					bipedHeadwear9.rotateAngleZ = n9;
				}
				if (!job.body.disabled) {
					this.bipedBody.rotateAngleX = job.body.rotationX * pi;
					this.bipedBody.rotateAngleY = job.body.rotationY * pi;
					this.bipedBody.rotateAngleZ = job.body.rotationZ * pi;
				}
				if (!job.larm.disabled) {
					bipedLeftArm.rotateAngleX = job.larm.rotationX * pi;
					bipedLeftArm.rotateAngleY = job.larm.rotationY * pi;
					bipedLeftArm.rotateAngleZ = job.larm.rotationZ * pi;
					if (player.display.getHasLivingAnimation()) {
						final ModelRenderer bipedLeftArm = this.bipedLeftArm;
						bipedLeftArm.rotateAngleZ -= (MathHelper.cos(par3 * 0.09f) * 0.05f) + 0.05f;
						final ModelRenderer bipedLeftArm2 = this.bipedLeftArm;
						bipedLeftArm2.rotateAngleX -= MathHelper.sin(par3 * 0.067f) * 0.05f;
					}
				}
				if (!job.rarm.disabled) {
					bipedRightArm.rotateAngleX = job.rarm.rotationX * pi;
					bipedRightArm.rotateAngleY = job.rarm.rotationY * pi;
					bipedRightArm.rotateAngleZ = job.rarm.rotationZ * pi;
					if (player.display.getHasLivingAnimation()) {
						final ModelRenderer bipedRightArm = this.bipedRightArm;
						bipedRightArm.rotateAngleZ += (MathHelper.cos(par3 * 0.09f) * 0.05f) + 0.05f;
						final ModelRenderer bipedRightArm2 = this.bipedRightArm;
						bipedRightArm2.rotateAngleX += MathHelper.sin(par3 * 0.067f) * 0.05f;
					}
				}
				if (!job.rleg.disabled) {
					bipedRightLeg.rotateAngleX = job.rleg.rotationX * pi;
					bipedRightLeg.rotateAngleY = job.rleg.rotationY * pi;
					bipedRightLeg.rotateAngleZ = job.rleg.rotationZ * pi;
				}
				if (!job.lleg.disabled) {
					bipedLeftLeg.rotateAngleX = job.lleg.rotationX * pi;
					bipedLeftLeg.rotateAngleY = job.lleg.rotationY * pi;
					bipedLeftLeg.rotateAngleZ = job.lleg.rotationZ * pi;
				}
			}
		}
	}
}

//

//

package noppes.npcs.client.layer;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.MathHelper;
import noppes.npcs.ModelPartConfig;
import noppes.npcs.ModelPartData;
import noppes.npcs.client.model.part.legs.ModelDigitigradeLegs;
import noppes.npcs.client.model.part.legs.ModelHorseLegs;
import noppes.npcs.client.model.part.legs.ModelMermaidLegs;
import noppes.npcs.client.model.part.legs.ModelNagaLegs;
import noppes.npcs.client.model.part.legs.ModelSpiderLegs;
import noppes.npcs.client.model.part.tails.ModelDragonTail;
import noppes.npcs.client.model.part.tails.ModelFeatherTail;
import noppes.npcs.client.model.part.tails.ModelRodentTail;
import noppes.npcs.client.model.part.tails.ModelSquirrelTail;
import noppes.npcs.client.model.part.tails.ModelTailFin;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;

public class LayerLegs extends LayerInterface implements LayerPreRender {
	private ModelSpiderLegs spiderLegs;
	private ModelHorseLegs horseLegs;
	private ModelNagaLegs naga;
	private ModelDigitigradeLegs digitigrade;
	private ModelMermaidLegs mermaid;
	private ModelRenderer tail;
	private ModelRenderer dragon;
	private ModelRenderer squirrel;
	private ModelRenderer horse;
	private ModelRenderer fin;
	private ModelRenderer rodent;
	private ModelRenderer feathers;
	float rotationPointZ;
	float rotationPointY;

	public LayerLegs(final RenderLiving render) {
		super(render);
		createParts();
	}

	private void createParts() {
		spiderLegs = new ModelSpiderLegs(model);
		horseLegs = new ModelHorseLegs(model);
		naga = new ModelNagaLegs(model);
		mermaid = new ModelMermaidLegs(model);
		digitigrade = new ModelDigitigradeLegs(model);
		(tail = new ModelRenderer(model, 56, 21)).addBox(-1.0f, 0.0f, 0.0f, 2, 9, 2);
		tail.setRotationPoint(0.0f, 0.0f, 1.0f);
		setRotation(tail, 0.8714253f, 0.0f, 0.0f);
		(horse = new ModelRenderer(model)).setTextureSize(32, 32);
		horse.setRotationPoint(0.0f, -1.0f, 1.0f);
		final ModelRenderer tailBase = new ModelRenderer(model, 0, 26);
		tailBase.setTextureSize(32, 32);
		tailBase.addBox(-1.0f, -1.0f, 0.0f, 2, 2, 3);
		setRotation(tailBase, -1.134464f, 0.0f, 0.0f);
		horse.addChild(tailBase);
		final ModelRenderer tailMiddle = new ModelRenderer(model, 0, 13);
		tailMiddle.setTextureSize(32, 32);
		tailMiddle.addBox(-1.5f, -2.0f, 3.0f, 3, 4, 7);
		setRotation(tailMiddle, -1.134464f, 0.0f, 0.0f);
		horse.addChild(tailMiddle);
		final ModelRenderer tailTip = new ModelRenderer(model, 0, 0);
		tailTip.setTextureSize(32, 32);
		tailTip.addBox(-1.5f, -4.5f, 9.0f, 3, 4, 7);
		setRotation(tailTip, -1.40215f, 0.0f, 0.0f);
		horse.addChild(tailTip);
		horse.rotateAngleX = 0.5f;
		dragon = new ModelDragonTail(model);
		squirrel = new ModelSquirrelTail(model);
		fin = new ModelTailFin(model);
		rodent = new ModelRodentTail(model);
		feathers = new ModelFeatherTail(model);
	}

	@Override
	public void preRender(final EntityCustomNpc player) {
		npc = player;
		playerdata = player.modelData;
		final ModelPartData data = playerdata.getPartData(EnumParts.LEGS);
		final ModelRenderer bipedLeftLeg = model.bipedLeftLeg;
		final ModelRenderer bipedRightLeg = model.bipedRightLeg;
		final boolean b = (data == null) || (data.type != 0);
		bipedRightLeg.isHidden = b;
		bipedLeftLeg.isHidden = b;
	}

	@Override
	public void render(final float par2, final float par3, final float par4, final float par5, final float par6,
			final float par7) {
		GlStateManager.pushMatrix();
		renderLegs(par7);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		renderTails(par7);
		GlStateManager.popMatrix();
	}

	private void renderLegs(final float par7) {
		final ModelPartData data = playerdata.getPartData(EnumParts.LEGS);
		if (data.type <= 0) {
			return;
		}
		final ModelPartConfig config = playerdata.getPartConfig(EnumParts.LEG_LEFT);
		this.preRender(data);
		if (data.type == 1) {
			GlStateManager.translate(0.0f, config.transY * 2.0f, (config.transZ * par7) + 0.04f);
			GlStateManager.scale(config.scaleX, config.scaleY, config.scaleZ);
			naga.render(par7);
		} else if (data.type == 2) {
			GlStateManager.translate(0.0, (config.transY * 1.76f) - (0.1 * config.scaleY), config.transZ * par7);
			GlStateManager.scale(1.06f, 1.06f, 1.06f);
			GlStateManager.scale(config.scaleX, config.scaleY, config.scaleZ);
			spiderLegs.render(par7);
		} else if (data.type == 3) {
			if (config.scaleY >= 1.0f) {
				GlStateManager.translate(0.0f, config.transY * 1.76f, config.transZ * par7);
			} else {
				GlStateManager.translate(0.0f, config.transY * 1.86f, config.transZ * par7);
			}
			GlStateManager.scale(0.79f, 0.9f - (config.scaleY / 10.0f), 0.79f);
			GlStateManager.scale(config.scaleX, config.scaleY, config.scaleZ);
			horseLegs.render(par7);
		} else if (data.type == 4) {
			GlStateManager.translate(0.0f, config.transY * 1.86f, config.transZ * par7);
			GlStateManager.scale(config.scaleX, config.scaleY, config.scaleZ);
			mermaid.render(par7);
		} else if (data.type == 5) {
			GlStateManager.translate(0.0f, config.transY * 1.86f, config.transZ * par7);
			GlStateManager.scale(config.scaleX, config.scaleY, config.scaleZ);
			digitigrade.render(par7);
		}
	}

	private void renderTails(final float par7) {
		final ModelPartData data = playerdata.getPartData(EnumParts.TAIL);
		if (data == null) {
			return;
		}
		final ModelPartConfig config = playerdata.getPartConfig(EnumParts.LEG_LEFT);
		GlStateManager.translate(config.transX * par7, config.transY + (rotationPointY * par7),
				(config.transZ * par7) + (rotationPointZ * par7));
		GlStateManager.translate(0.0f, 0.0f, (config.scaleZ - 1.0f) * 5.0f * par7);
		GlStateManager.scale(config.scaleX, config.scaleY, config.scaleZ);
		this.preRender(data);
		if (data.type == 0) {
			if (data.pattern == 1) {
				tail.rotationPointX = -0.5f;
				final ModelRenderer tail = this.tail;
				tail.rotateAngleY -= 0.2;
				this.tail.render(par7);
				final ModelRenderer tail2 = this.tail;
				++tail2.rotationPointX;
				final ModelRenderer tail3 = this.tail;
				tail3.rotateAngleY += 0.4;
				this.tail.render(par7);
				this.tail.rotationPointX = 0.0f;
			} else {
				tail.render(par7);
			}
		} else if (data.type == 1) {
			dragon.render(par7);
		} else if (data.type == 2) {
			horse.render(par7);
		} else if (data.type == 3) {
			squirrel.render(par7);
		} else if (data.type == 4) {
			fin.render(par7);
		} else if (data.type == 5) {
			rodent.render(par7);
		}
	}

	@Override
	public void rotate(final float par1, final float par2, final float par3, final float par4, final float par5,
			final float par6) {
		rotateLegs(par1, par2, par3, par4, par5, par6);
		rotateTail(par1, par2, par3, par4, par5, par6);
	}

	public void rotateLegs(final float par1, final float par2, final float par3, final float par4, final float par5,
			final float par6) {
		final ModelPartData part = playerdata.getPartData(EnumParts.LEGS);
		if (part.type == 2) {
			spiderLegs.setRotationAngles(playerdata, par1, par2, par3, par4, par5, par6, npc);
		} else if (part.type == 3) {
			horseLegs.setRotationAngles(playerdata, par1, par2, par3, par4, par5, par6, npc);
		} else if (part.type == 1) {
			naga.isRiding = model.isRiding;
			naga.isSleeping = npc.isPlayerSleeping();
			naga.isCrawling = (npc.currentAnimation == 7);
			naga.isSneaking = model.isSneak;
			naga.setRotationAngles(par1, par2, par3, par4, par5, par6, npc);
		} else if (part.type == 4) {
			mermaid.setRotationAngles(par1, par2, par3, par4, par5, par6, npc);
		} else if (part.type == 5) {
			digitigrade.setRotationAngles(par1, par2, par3, par4, par5, par6, npc);
		}
	}

	public void rotateTail(final float par1, final float par2, final float par3, final float par4, final float par5,
			final float par6) {
		final ModelPartData part = playerdata.getPartData(EnumParts.LEGS);
		final ModelPartData partTail = playerdata.getPartData(EnumParts.TAIL);
		final ModelPartConfig config = playerdata.getPartConfig(EnumParts.LEG_LEFT);
		final float rotateAngleY = MathHelper.cos(par1 * 0.6662f) * 0.2f * par2;
		float rotateAngleX = MathHelper.sin(par3 * 0.067f) * 0.05f;
		rotationPointZ = 0.0f;
		rotationPointY = 11.0f;
		if (part.type == 2) {
			rotationPointY = 12.0f + ((config.scaleY - 1.0f) * 3.0f);
			rotationPointZ = 15.0f + ((config.scaleZ - 1.0f) * 10.0f);
			if (npc.isPlayerSleeping() || (npc.currentAnimation == 7)) {
				rotationPointY = 12.0f + (16.0f * config.scaleZ);
				rotationPointZ = 1.0f * config.scaleY;
				rotateAngleX = -0.7853982f;
			}
		} else if (part.type == 3) {
			rotationPointY = 10.0f;
			rotationPointZ = 16.0f + ((config.scaleZ - 1.0f) * 12.0f);
		} else {
			rotationPointZ = (1.0f - config.scaleZ) * 1.0f;
		}
		if (partTail != null) {
			if (partTail.type == 2) {
				rotateAngleX += 0.5;
			}
			if (partTail.type == 0) {
				rotateAngleX += 0.87f;
			}
		}
		rotationPointZ += model.bipedRightLeg.rotationPointZ + 0.5f;
		final ModelRenderer tail = this.tail;
		final ModelRenderer feathers = this.feathers;
		final ModelRenderer dragon = this.dragon;
		final ModelRenderer squirrel = this.squirrel;
		final ModelRenderer horse = this.horse;
		final ModelRenderer fin = this.fin;
		final ModelRenderer rodent = this.rodent;
		final float rotateAngleX2 = rotateAngleX;
		rodent.rotateAngleX = rotateAngleX2;
		fin.rotateAngleX = rotateAngleX2;
		horse.rotateAngleX = rotateAngleX2;
		squirrel.rotateAngleX = rotateAngleX2;
		dragon.rotateAngleX = rotateAngleX2;
		feathers.rotateAngleX = rotateAngleX2;
		tail.rotateAngleX = rotateAngleX2;
		final ModelRenderer tail2 = this.tail;
		final ModelRenderer feathers2 = this.feathers;
		final ModelRenderer dragon2 = this.dragon;
		final ModelRenderer squirrel2 = this.squirrel;
		final ModelRenderer horse2 = this.horse;
		final ModelRenderer fin2 = this.fin;
		final ModelRenderer rodent2 = this.rodent;
		final float rotateAngleY2 = rotateAngleY;
		rodent2.rotateAngleY = rotateAngleY2;
		fin2.rotateAngleY = rotateAngleY2;
		horse2.rotateAngleY = rotateAngleY2;
		squirrel2.rotateAngleY = rotateAngleY2;
		dragon2.rotateAngleY = rotateAngleY2;
		feathers2.rotateAngleY = rotateAngleY2;
		tail2.rotateAngleY = rotateAngleY2;
	}
}

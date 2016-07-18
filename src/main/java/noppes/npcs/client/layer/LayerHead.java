//

//

package noppes.npcs.client.layer;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderLiving;
import noppes.npcs.ModelPartData;
import noppes.npcs.client.model.Model2DRenderer;
import noppes.npcs.client.model.part.head.ModelDuckBeak;
import noppes.npcs.client.model.part.horns.ModelAntennasBack;
import noppes.npcs.client.model.part.horns.ModelAntennasFront;
import noppes.npcs.client.model.part.horns.ModelAntlerHorns;
import noppes.npcs.client.model.part.horns.ModelBullHorns;
import noppes.npcs.constants.EnumParts;

public class LayerHead extends LayerInterface {
	private ModelRenderer small;
	private ModelRenderer medium;
	private ModelRenderer large;
	private ModelRenderer bunnySnout;
	private ModelRenderer beak;
	private Model2DRenderer beard;
	private Model2DRenderer hair;
	private Model2DRenderer mohawk;
	private ModelRenderer bull;
	private ModelRenderer antlers;
	private ModelRenderer antennasBack;
	private ModelRenderer antennasFront;
	private ModelRenderer ears;
	private ModelRenderer bunnyEars;

	public LayerHead(final RenderLiving render) {
		super(render);
		createParts();
	}

	private void createParts() {
		(small = new ModelRenderer(model, 24, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 3, 1);
		small.setRotationPoint(-2.0f, -3.0f, -5.0f);
		(medium = new ModelRenderer(model, 24, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 3, 2);
		medium.setRotationPoint(-2.0f, -3.0f, -6.0f);
		(large = new ModelRenderer(model, 24, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 3, 3);
		large.setRotationPoint(-2.0f, -3.0f, -7.0f);
		(bunnySnout = new ModelRenderer(model, 24, 0)).addBox(1.0f, 1.0f, 0.0f, 4, 2, 1);
		bunnySnout.setRotationPoint(-3.0f, -4.0f, -5.0f);
		final ModelRenderer tooth = new ModelRenderer(model, 24, 3);
		tooth.addBox(2.0f, 3.0f, 0.0f, 2, 1, 1);
		tooth.setRotationPoint(0.0f, 0.0f, 0.0f);
		bunnySnout.addChild(tooth);
		(beak = new ModelDuckBeak(model)).setRotationPoint(0.0f, 0.0f, -4.0f);
		(beard = new Model2DRenderer(model, 56.0f, 20.0f, 8, 12)).setRotationOffset(-3.99f, 11.8f, -4.0f);
		beard.setScale(0.74f);
		(hair = new Model2DRenderer(model, 56.0f, 20.0f, 8, 12)).setRotationOffset(-3.99f, 11.8f, 3.0f);
		hair.setScale(0.75f);
		(mohawk = new Model2DRenderer(model, 0.0f, 0.0f, 64, 64)).setRotationOffset(-9.0f, 0.1f, -0.5f);
		setRotation(mohawk, 0.0f, 1.5707964f, 0.0f);
		mohawk.setScale(0.825f);
		bull = new ModelBullHorns(model);
		antlers = new ModelAntlerHorns(model);
		antennasBack = new ModelAntennasBack(model);
		antennasFront = new ModelAntennasFront(model);
		ears = new ModelRenderer(model);
		final Model2DRenderer right = new Model2DRenderer(model, 56.0f, 0.0f, 8, 4);
		right.setRotationPoint(-7.44f, -7.3f, -0.0f);
		right.setScale(0.234f, 0.234f);
		right.setThickness(1.16f);
		ears.addChild(right);
		final Model2DRenderer left = new Model2DRenderer(model, 56.0f, 0.0f, 8, 4);
		left.setRotationPoint(7.44f, -7.3f, 1.15f);
		left.setScale(0.234f, 0.234f);
		setRotation(left, 0.0f, 3.1415927f, 0.0f);
		left.setThickness(1.16f);
		ears.addChild(left);
		final Model2DRenderer right2 = new Model2DRenderer(model, 56.0f, 4.0f, 8, 4);
		right2.setRotationPoint(-7.44f, -7.3f, 1.14f);
		right2.setScale(0.234f, 0.234f);
		right2.setThickness(1.16f);
		ears.addChild(right2);
		final Model2DRenderer left2 = new Model2DRenderer(model, 56.0f, 4.0f, 8, 4);
		left2.setRotationPoint(7.44f, -7.3f, 2.31f);
		left2.setScale(0.234f, 0.234f);
		setRotation(left2, 0.0f, 3.1415927f, 0.0f);
		left2.setThickness(1.16f);
		ears.addChild(left2);
		bunnyEars = new ModelRenderer(model);
		final ModelRenderer earleft = new ModelRenderer(model, 56, 0);
		earleft.mirror = true;
		earleft.addBox(-1.466667f, -4.0f, 0.0f, 3, 7, 1);
		earleft.setRotationPoint(2.533333f, -11.0f, 0.0f);
		bunnyEars.addChild(earleft);
		final ModelRenderer earright = new ModelRenderer(model, 56, 0);
		earright.addBox(-1.5f, -4.0f, 0.0f, 3, 7, 1);
		earright.setRotationPoint(-2.466667f, -11.0f, 0.0f);
		bunnyEars.addChild(earright);
	}

	@Override
	public void render(final float par2, final float par3, final float par4, final float par5, final float par6,
			final float par7) {
		model.bipedHead.postRender(0.0625f);
		renderSnout(par7);
		renderBeard(par7);
		renderHair(par7);
		renderMohawk(par7);
		renderHorns(par7);
		renderEars(par7);
	}

	private void renderBeard(final float par7) {
		final ModelPartData data = playerdata.getPartData(EnumParts.BEARD);
		if (data == null) {
			return;
		}
		preRender(data);
		beard.render(par7);
	}

	private void renderEars(final float par7) {
		final ModelPartData data = playerdata.getPartData(EnumParts.EARS);
		if (data == null) {
			return;
		}
		preRender(data);
		if (data.type == 0) {
			ears.render(par7);
		} else if (data.type == 1) {
			bunnyEars.render(par7);
		}
	}

	private void renderHair(final float par7) {
		final ModelPartData data = playerdata.getPartData(EnumParts.HAIR);
		if (data == null) {
			return;
		}
		preRender(data);
		hair.render(par7);
	}

	private void renderHorns(final float par7) {
		final ModelPartData data = playerdata.getPartData(EnumParts.HORNS);
		if (data == null) {
			return;
		}
		preRender(data);
		if (data.type == 0) {
			bull.render(par7);
		} else if (data.type == 1) {
			antlers.render(par7);
		} else if ((data.type == 2) && (data.pattern == 0)) {
			antennasBack.render(par7);
		} else if ((data.type == 2) && (data.pattern == 1)) {
			antennasFront.render(par7);
		}
	}

	private void renderMohawk(final float par7) {
		final ModelPartData data = playerdata.getPartData(EnumParts.MOHAWK);
		if (data == null) {
			return;
		}
		preRender(data);
		mohawk.render(par7);
	}

	private void renderSnout(final float par7) {
		final ModelPartData data = playerdata.getPartData(EnumParts.SNOUT);
		if (data == null) {
			return;
		}
		preRender(data);
		if (data.type == 0) {
			small.render(par7);
		} else if (data.type == 1) {
			medium.render(par7);
		} else if (data.type == 2) {
			large.render(par7);
		} else if (data.type == 3) {
			bunnySnout.render(par7);
		} else if (data.type == 4) {
			beak.render(par7);
		}
	}

	@Override
	public void rotate(final float par2, final float par3, final float par4, final float par5, final float par6,
			final float par7) {
		final ModelRenderer head = model.bipedHead;
		if (head.rotateAngleX < 0.0f) {
			beard.rotateAngleX = 0.0f;
			hair.rotateAngleX = -head.rotateAngleX * 1.2f;
			if (head.rotateAngleX > -1.0f) {
				hair.rotationPointY = -head.rotateAngleX * 1.5f;
				hair.rotationPointZ = -head.rotateAngleX * 1.5f;
			}
		} else {
			hair.rotateAngleX = 0.0f;
			hair.rotationPointY = 0.0f;
			hair.rotationPointZ = 0.0f;
			beard.rotateAngleX = -head.rotateAngleX;
		}
	}
}

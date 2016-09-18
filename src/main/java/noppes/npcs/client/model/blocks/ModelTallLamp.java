
package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTallLamp extends ModelBase {
	ModelRenderer Base;
	ModelRenderer MiddleStick;
	ModelRenderer LampShadeStick1;
	ModelRenderer LampShadeStick2;
	ModelRenderer LampShadeStick3;
	ModelRenderer LampShadeStick4;

	public ModelTallLamp() {
		(Base = new ModelRenderer(this, 6, 2)).addBox(-6.0f, 0.0f, -6.0f, 12, 1, 12);
		Base.setRotationPoint(0.0f, 23.0f, 0.0f);
		(MiddleStick = new ModelRenderer(this, 12, 2)).addBox(-1.0f, 0.0f, -1.0f, 2, 28, 2);
		MiddleStick.setRotationPoint(0.0f, -5.0f, 0.0f);
		(LampShadeStick1 = new ModelRenderer(this, 0, 30)).addBox(0.0f, 0.0f, 0.0f, 5, 1, 1);
		LampShadeStick1.setRotationPoint(1.0f, -1.0f, -0.5f);
		(LampShadeStick2 = new ModelRenderer(this, 0, 30)).addBox(0.0f, 0.0f, 0.0f, 5, 1, 1);
		LampShadeStick2.setRotationPoint(-0.5f, -1.0f, -1.0f);
		setRotation(LampShadeStick2, 0.0f, 1.570796f, 0.0f);
		(LampShadeStick3 = new ModelRenderer(this, 0, 30)).addBox(0.0f, 0.0f, 0.0f, 5, 1, 1);
		LampShadeStick3.setRotationPoint(-1.0f, -1.0f, 0.5f);
		setRotation(LampShadeStick3, 0.0f, 3.141593f, 0.0f);
		(LampShadeStick4 = new ModelRenderer(this, 0, 30)).addBox(0.0f, 0.0f, 0.0f, 5, 1, 1);
		LampShadeStick4.setRotationPoint(0.5f, -1.0f, 1.0f);
		setRotation(LampShadeStick4, 0.0f, -1.570796f, 0.0f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Base.render(f5);
		MiddleStick.render(f5);
		LampShadeStick1.render(f5);
		LampShadeStick2.render(f5);
		LampShadeStick3.render(f5);
		LampShadeStick4.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}

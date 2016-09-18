
package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCandleCeiling extends ModelBase {
	ModelRenderer Wax1;
	ModelRenderer Wax2;
	ModelRenderer Wax3;
	ModelRenderer Wax4;
	ModelRenderer TippyTop1;
	ModelRenderer TippyTop2;
	ModelRenderer Middle;
	ModelRenderer BottomBar1;
	ModelRenderer Rod1;
	ModelRenderer Rod2;
	ModelRenderer Rod3;
	ModelRenderer Rod4;
	ModelRenderer Base1;
	ModelRenderer Base2;
	ModelRenderer Base3;
	ModelRenderer Base4;
	ModelRenderer BottomBar3;
	ModelRenderer BottomBar2;
	ModelRenderer BottomBar4;

	public ModelCandleCeiling() {
		(Wax1 = new ModelRenderer(this, 16, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 4, 2);
		Wax1.setRotationPoint(-1.0f, 15.5f, 5.0f);
		(Wax2 = new ModelRenderer(this, 16, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 4, 2);
		Wax2.setRotationPoint(7.0f, 15.5f, 1.0f);
		setRotation(Wax2, 0.0f, 3.141593f, 0.0f);
		(Wax3 = new ModelRenderer(this, 16, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 4, 2);
		Wax3.setRotationPoint(-7.0f, 15.5f, -1.0f);
		(Wax4 = new ModelRenderer(this, 16, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 4, 2);
		Wax4.setRotationPoint(1.0f, 15.5f, -5.0f);
		setRotation(Wax4, 0.0f, 3.141593f, 0.0f);
		(TippyTop1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 1, 4);
		TippyTop1.setRotationPoint(-2.8f, 7.5f, 0.0f);
		setRotation(TippyTop1, 0.0f, 0.7853982f, 0.0f);
		(TippyTop2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 3, 1, 3);
		TippyTop2.setRotationPoint(-2.1f, 8.0f, 0.0f);
		setRotation(TippyTop2, 0.0f, 0.7853982f, 0.0f);
		(Middle = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 13, 1);
		Middle.setRotationPoint(-0.5f, 9.0f, -0.5f);
		(BottomBar1 = new ModelRenderer(this, 0, 4)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 5);
		BottomBar1.setRotationPoint(-0.5f, 21.0f, 0.5f);
		(Rod1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 2, 1);
		Rod1.setRotationPoint(-0.5f, 20.0f, 5.5f);
		(Rod2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 2, 1);
		Rod2.setRotationPoint(5.5f, 20.0f, -0.5f);
		(Rod3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 2, 1);
		Rod3.setRotationPoint(-6.5f, 20.0f, -0.5f);
		(Rod4 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 2, 1);
		Rod4.setRotationPoint(-0.5f, 20.0f, -6.5f);
		(Base1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 1, 4);
		Base1.setRotationPoint(-2.0f, 19.0f, 4.0f);
		(Base2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 1, 4);
		Base2.setRotationPoint(4.0f, 19.0f, -2.0f);
		(Base3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 1, 4);
		Base3.setRotationPoint(-8.0f, 19.0f, -2.0f);
		(Base4 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 1, 4);
		Base4.setRotationPoint(-2.0f, 19.0f, -8.0f);
		(BottomBar3 = new ModelRenderer(this, 0, 4)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 5);
		BottomBar3.setRotationPoint(-0.5f, 21.0f, -0.5f);
		setRotation(BottomBar3, 0.0f, -1.570796f, 0.0f);
		(BottomBar2 = new ModelRenderer(this, 0, 4)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 5);
		BottomBar2.setRotationPoint(0.5f, 21.0f, 0.5f);
		setRotation(BottomBar2, 0.0f, 1.570796f, 0.0f);
		(BottomBar4 = new ModelRenderer(this, 0, 4)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 5);
		BottomBar4.setRotationPoint(0.4f, 21.0f, -0.5f);
		setRotation(BottomBar4, 0.0f, 3.141593f, 0.0f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Wax1.render(f5);
		Wax2.render(f5);
		Wax3.render(f5);
		Wax4.render(f5);
		TippyTop1.render(f5);
		TippyTop2.render(f5);
		Middle.render(f5);
		BottomBar1.render(f5);
		Rod1.render(f5);
		Rod2.render(f5);
		Rod3.render(f5);
		Rod4.render(f5);
		Base1.render(f5);
		Base2.render(f5);
		Base3.render(f5);
		Base4.render(f5);
		BottomBar3.render(f5);
		BottomBar2.render(f5);
		BottomBar4.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}

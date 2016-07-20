//

//

package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLampCeiling extends ModelBase {
	ModelRenderer Base;
	ModelRenderer Top1;
	ModelRenderer Top2;
	ModelRenderer Top3;
	ModelRenderer Chain8;
	ModelRenderer Chain1;
	ModelRenderer Chain2;
	ModelRenderer Chain3;
	ModelRenderer Chain4;
	ModelRenderer Chain5;
	ModelRenderer Chain6;
	ModelRenderer Chain7;
	ModelRenderer TippyTop1;
	ModelRenderer TippyTop2;
	ModelRenderer Shape3;
	ModelRenderer Shape1;
	ModelRenderer Shape2;

	public ModelLampCeiling() {
		(Base = new ModelRenderer(this, 0, 6)).addBox(0.0f, 0.0f, 0.0f, 4, 7, 4);
		Base.setRotationPoint(-2.0f, 17.0f, -2.0f);
		(Top1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 5, 1, 5);
		Top1.setRotationPoint(-2.5f, 17.0f, -2.5f);
		(Top2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 1, 4);
		Top2.setRotationPoint(-2.0f, 16.5f, -2.0f);
		(Top3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 3, 1, 3);
		Top3.setRotationPoint(-1.5f, 16.0f, -1.5f);
		(Chain8 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 2, 1);
		Chain8.setRotationPoint(-0.5f, 14.0f, -1.5f);
		(Chain1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 3, 1);
		Chain1.setRotationPoint(0.5f, 8.0f, -0.5f);
		(Chain2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 3, 1);
		Chain2.setRotationPoint(-1.5f, 8.0f, -0.5f);
		(Chain3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 3, 1);
		Chain3.setRotationPoint(-0.5f, 10.0f, 0.5f);
		(Chain4 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 3, 1);
		Chain4.setRotationPoint(-0.5f, 10.0f, -1.5f);
		(Chain5 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 3, 1);
		Chain5.setRotationPoint(-1.5f, 12.0f, -0.5f);
		(Chain6 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 3, 1);
		Chain6.setRotationPoint(0.5f, 12.0f, -0.5f);
		(Chain7 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 2, 1);
		Chain7.setRotationPoint(-0.5f, 14.0f, 0.5f);
		(TippyTop1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 1, 4);
		TippyTop1.setRotationPoint(-2.8f, 8.0f, 0.0f);
		setRotation(TippyTop1, 0.0f, 0.7853982f, 0.0f);
		(TippyTop2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 3, 1, 3);
		TippyTop2.setRotationPoint(-2.1f, 8.5f, 0.0f);
		setRotation(TippyTop2, 0.0f, 0.7853982f, 0.0f);
		(Shape3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
		Shape3.setRotationPoint(-0.5f, 14.0f, -0.5f);
		(Shape1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
		Shape1.setRotationPoint(-0.5f, 10.0f, -0.5f);
		(Shape2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
		Shape2.setRotationPoint(-0.5f, 12.0f, -0.5f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Base.render(f5);
		Top1.render(f5);
		Top2.render(f5);
		Top3.render(f5);
		Chain8.render(f5);
		Chain1.render(f5);
		Chain2.render(f5);
		Chain3.render(f5);
		Chain4.render(f5);
		Chain5.render(f5);
		Chain6.render(f5);
		Chain7.render(f5);
		TippyTop1.render(f5);
		TippyTop2.render(f5);
		Shape3.render(f5);
		Shape1.render(f5);
		Shape2.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}

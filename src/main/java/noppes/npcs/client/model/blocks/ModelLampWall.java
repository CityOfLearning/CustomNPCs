
package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLampWall extends ModelBase {
	ModelRenderer Base;
	ModelRenderer Top1;
	ModelRenderer Top2;
	ModelRenderer Top3;
	ModelRenderer Shape2;
	ModelRenderer Shape3;
	ModelRenderer Shape4;
	ModelRenderer Shape5;

	public ModelLampWall() {
		(Base = new ModelRenderer(this, 0, 6)).addBox(0.0f, 0.0f, 0.0f, 4, 7, 4);
		Base.setRotationPoint(-2.0f, 14.0f, 1.0f);
		(Top1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 5, 1, 5);
		Top1.setRotationPoint(-2.5f, 14.0f, 0.5f);
		(Top2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 1, 4);
		Top2.setRotationPoint(-2.0f, 13.5f, 1.0f);
		(Top3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 3, 1, 3);
		Top3.setRotationPoint(-1.5f, 13.0f, 1.5f);
		(Shape2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 3);
		Shape2.setRotationPoint(-0.5f, 11.0f, 3.5f);
		(Shape3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 3, 3, 1);
		Shape3.setRotationPoint(0.0f, 9.5f, 6.5f);
		setRotation(Shape3, 0.0f, 0.0f, 0.7853982f);
		(Shape4 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 3, 1);
		Shape4.setRotationPoint(-0.5f, 10.5f, 2.5f);
		(Shape5 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 4, 1);
		Shape5.setRotationPoint(0.0f, 8.7f, 7.0f);
		setRotation(Shape5, 0.0f, 0.0f, 0.7853982f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Base.render(f5);
		Top1.render(f5);
		Top2.render(f5);
		Top3.render(f5);
		Shape2.render(f5);
		Shape3.render(f5);
		Shape4.render(f5);
		Shape5.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}

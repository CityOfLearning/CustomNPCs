//

//

package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelChair extends ModelBase {
	ModelRenderer Leg1;
	ModelRenderer Leg2;
	ModelRenderer Leg3;
	ModelRenderer Leg4;
	ModelRenderer Shape1;
	ModelRenderer Shape2;
	ModelRenderer Shape3;
	ModelRenderer Shape4;
	ModelRenderer Shape5;
	ModelRenderer Shape6;
	ModelRenderer Shape7;
	ModelRenderer Shape8;
	ModelRenderer Shape9;
	ModelRenderer Shape10;
	ModelRenderer Shape11;

	public ModelChair() {
		Leg1 = new ModelRenderer(this, 0, 0);
		Leg1.mirror = true;
		Leg1.addBox(0.0f, 0.0f, 0.0f, 1, 18, 1);
		Leg1.setRotationPoint(4.01f, 6.0f, 5.01f);
		Leg2 = new ModelRenderer(this, 0, 0);
		Leg2.mirror = true;
		Leg2.addBox(0.0f, 0.0f, 0.0f, 1, 9, 1);
		Leg2.setRotationPoint(4.01f, 15.5f, -5.01f);
		(Leg3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 18, 1);
		Leg3.setRotationPoint(-5.01f, 6.0f, 5.01f);
		(Leg4 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 9, 1);
		Leg4.setRotationPoint(-5.01f, 15.5f, -5.01f);
		(Shape1 = new ModelRenderer(this, 8, 2)).addBox(0.0f, 0.0f, 0.0f, 10, 1, 11);
		Shape1.setRotationPoint(-5.0f, 16.0f, -5.0f);
		(Shape2 = new ModelRenderer(this, 4, 4)).addBox(0.0f, 0.0f, 0.0f, 3, 2, 1);
		Shape2.setRotationPoint(-1.5f, 6.51f, 5.5f);
		Shape3 = new ModelRenderer(this, 4, 4);
		Shape3.mirror = true;
		Shape3.addBox(-3.0f, 0.0f, 0.0f, 3, 2, 1);
		Shape3.setRotationPoint(4.0f, 6.5f, 5.0f);
		setRotation(Shape3, 0.0f, 0.2094395f, 0.0f);
		(Shape4 = new ModelRenderer(this, 4, 4)).addBox(0.0f, 0.0f, 0.0f, 3, 2, 1);
		Shape4.setRotationPoint(-4.0f, 6.5f, 5.0f);
		setRotation(Shape4, 0.0f, -0.2094395f, 0.0f);
		(Shape5 = new ModelRenderer(this, 46, 0)).addBox(0.0f, 0.0f, 0.0f, 9, 1, 1);
		Shape5.setRotationPoint(-4.0f, 19.0f, 5.0f);
		(Shape6 = new ModelRenderer(this, 46, 0)).addBox(0.0f, 0.0f, 0.0f, 8, 1, 1);
		Shape6.setRotationPoint(-4.0f, 19.0f, -5.0f);
		(Shape7 = new ModelRenderer(this, 11, 13)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 9);
		Shape7.setRotationPoint(-5.0f, 20.0f, -4.0f);
		Shape8 = new ModelRenderer(this, 11, 13);
		Shape8.mirror = true;
		Shape8.addBox(0.0f, 0.0f, 0.0f, 1, 1, 9);
		Shape8.setRotationPoint(4.0f, 20.0f, -4.0f);
		Shape9 = new ModelRenderer(this, 0, 0);
		Shape9.mirror = true;
		Shape9.addBox(0.0f, 0.0f, 0.0f, 1, 8, 1);
		Shape9.setRotationPoint(2.0f, 8.0f, 5.5f);
		setRotation(Shape9, -0.0523599f, 0.0f, 0.0f);
		(Shape10 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 8, 1);
		Shape10.setRotationPoint(-3.0f, 8.0f, 5.5f);
		setRotation(Shape10, -0.0523599f, 0.0f, 0.0f);
		(Shape11 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 8, 1);
		Shape11.setRotationPoint(-0.5f, 8.0f, 5.6f);
		setRotation(Shape11, -0.0698132f, 0.0f, 0.0f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Leg1.render(f5);
		Leg2.render(f5);
		Leg3.render(f5);
		Leg4.render(f5);
		Shape1.render(f5);
		Shape2.render(f5);
		Shape3.render(f5);
		Shape4.render(f5);
		Shape5.render(f5);
		Shape6.render(f5);
		Shape7.render(f5);
		Shape8.render(f5);
		Shape9.render(f5);
		Shape10.render(f5);
		Shape11.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}

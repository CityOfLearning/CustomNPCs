//

//

package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelStool extends ModelBase {
	ModelRenderer Base;
	ModelRenderer Leg1;
	ModelRenderer Leg2;
	ModelRenderer Leg3;
	ModelRenderer Leg4;
	ModelRenderer Shape1;
	ModelRenderer Shape2;
	ModelRenderer Shape3;
	ModelRenderer Shape4;

	public ModelStool() {
		(Base = new ModelRenderer(this, 9, 3)).addBox(-5.0f, 0.0f, -5.0f, 10, 1, 10);
		Base.setRotationPoint(0.0f, 16.0f, 0.0f);
		(Leg1 = new ModelRenderer(this, 0, 12)).addBox(-1.0f, 0.0f, 0.0f, 2, 8, 1);
		Leg1.setRotationPoint(2.0f, 17.0f, 2.0f);
		setRotation(Leg1, 0.3316126f, 0.7853982f, 0.0f);
		(Leg2 = new ModelRenderer(this, 0, 12)).addBox(-1.0f, 0.0f, 0.0f, 2, 8, 1);
		Leg2.setRotationPoint(2.0f, 17.0f, -2.0f);
		setRotation(Leg2, 0.3316126f, 2.356194f, -0.0081449f);
		(Leg3 = new ModelRenderer(this, 0, 12)).addBox(-1.0f, 0.0f, 0.0f, 2, 8, 1);
		Leg3.setRotationPoint(-2.0f, 17.0f, 2.0f);
		setRotation(Leg3, 0.3316126f, -0.7853982f, 0.0f);
		(Leg4 = new ModelRenderer(this, 0, 12)).addBox(-1.0f, 0.0f, 0.0f, 2, 8, 1);
		Leg4.setRotationPoint(-2.0f, 17.0f, -2.0f);
		setRotation(Leg4, 0.3316126f, -2.356194f, 0.0f);
		(Shape1 = new ModelRenderer(this, 0, 11)).addBox(-3.0f, 0.0f, 0.0f, 6, 1, 1);
		Shape1.setRotationPoint(2.4f, 19.0f, 0.0f);
		setRotation(Shape1, 0.0f, 1.570796f, 0.0f);
		(Shape2 = new ModelRenderer(this, 0, 11)).addBox(-3.0f, 0.0f, 0.0f, 6, 1, 1);
		Shape2.setRotationPoint(0.0f, 19.0f, 2.4f);
		(Shape3 = new ModelRenderer(this, 0, 11)).addBox(-3.0f, 0.0f, 0.0f, 6, 1, 1);
		Shape3.setRotationPoint(0.0f, 19.0f, -3.4f);
		(Shape4 = new ModelRenderer(this, 0, 11)).addBox(-3.0f, 0.0f, 0.0f, 6, 1, 1);
		Shape4.setRotationPoint(-3.4f, 19.0f, 0.0f);
		setRotation(Shape4, 0.0f, 1.570796f, 0.0f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Base.render(f5);
		Leg1.render(f5);
		Leg2.render(f5);
		Leg3.render(f5);
		Leg4.render(f5);
		Shape1.render(f5);
		Shape2.render(f5);
		Shape3.render(f5);
		Shape4.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}

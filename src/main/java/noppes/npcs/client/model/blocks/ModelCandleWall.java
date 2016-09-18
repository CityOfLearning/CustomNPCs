
package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCandleWall extends ModelBase {
	ModelRenderer Base;
	ModelRenderer Bar1;
	ModelRenderer Bar2;
	ModelRenderer Bar3;
	ModelRenderer Bar4;
	ModelRenderer Wax;
	ModelRenderer Wall2;
	ModelRenderer Wall1;
	ModelRenderer Bar5;
	ModelRenderer Bar6;

	public ModelCandleWall() {
		(Base = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 1, 4);
		Base.setRotationPoint(-2.0f, 13.0f, -4.0f);
		(Bar1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 6);
		Bar1.setRotationPoint(-3.0f, 12.0f, -5.0f);
		(Bar2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 6);
		Bar2.setRotationPoint(2.0f, 12.0f, -5.0f);
		(Bar3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 1, 1);
		Bar3.setRotationPoint(-2.0f, 12.0f, -5.0f);
		(Bar4 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 1, 1);
		Bar4.setRotationPoint(-2.0f, 12.0f, 0.0f);
		(Wax = new ModelRenderer(this, 16, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 4, 2);
		Wax.setRotationPoint(-1.0f, 9.0f, -3.0f);
		(Wall2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 3, 3, 1);
		Wall2.setRotationPoint(0.0f, 13.7f, -7.5f);
		setRotation(Wall2, 0.0f, 0.0f, 0.7853982f);
		(Wall1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 4, 1);
		Wall1.setRotationPoint(0.0f, 13.0f, -8.0f);
		setRotation(Wall1, 0.0f, 0.0f, 0.7853982f);
		(Bar5 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 2, 1);
		Bar5.setRotationPoint(-0.5f, 13.5f, -2.5f);
		(Bar6 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 5);
		Bar6.setRotationPoint(-0.5f, 15.5f, -6.5f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Base.render(f5);
		Bar1.render(f5);
		Bar2.render(f5);
		Bar3.render(f5);
		Bar4.render(f5);
		Wax.render(f5);
		Wall2.render(f5);
		Wall1.render(f5);
		Bar5.render(f5);
		Bar6.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}

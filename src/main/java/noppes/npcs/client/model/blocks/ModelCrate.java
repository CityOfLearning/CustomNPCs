//

//

package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCrate extends ModelBase {
	ModelRenderer sticky1;
	ModelRenderer sticky2;
	ModelRenderer sticky3;
	ModelRenderer sticky4;
	ModelRenderer core;
	ModelRenderer sticky1top;
	ModelRenderer sticky2top;
	ModelRenderer sticky3top;
	ModelRenderer sticky4top;
	ModelRenderer sidestick2;
	ModelRenderer sidestick3;
	ModelRenderer sidestick1;
	ModelRenderer sidestick4;
	ModelRenderer sidestuff2;
	ModelRenderer sidestuff1;
	ModelRenderer sidestuff3;
	ModelRenderer sidestuff4;
	ModelRenderer Shape1;
	ModelRenderer Shape2;

	public ModelCrate() {
		(sticky1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 2, 12);
		sticky1.setRotationPoint(6.0f, 22.0f, -6.0f);
		(sticky2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 12, 2, 2);
		sticky2.setRotationPoint(-6.0f, 22.0f, -8.0f);
		(sticky3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 2, 12);
		sticky3.setRotationPoint(-8.0f, 22.0f, -6.0f);
		(sticky4 = new ModelRenderer(this, 32, 0)).addBox(0.0f, 0.0f, 0.0f, 12, 2, 2);
		sticky4.setRotationPoint(-6.0f, 22.0f, 6.0f);
		(core = new ModelRenderer(this, 0, 0)).addBox(-8.0f, 0.0f, -8.0f, 16, 16, 16, -1.0f);
		core.setRotationPoint(0.0f, 8.0f, 0.0f);
		(sticky1top = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 2, 12);
		sticky1top.setRotationPoint(6.0f, 8.0f, -6.0f);
		(sticky2top = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 12, 2, 2);
		sticky2top.setRotationPoint(-6.0f, 8.0f, 6.0f);
		(sticky3top = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 2, 12);
		sticky3top.setRotationPoint(-8.0f, 8.0f, -6.0f);
		(sticky4top = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 12, 2, 2);
		sticky4top.setRotationPoint(-6.0f, 8.0f, -8.0f);
		(sidestick1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 16, 2);
		sidestick1.setRotationPoint(-8.0f, 8.0f, 6.0f);
		(sidestick2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 16, 2);
		sidestick2.setRotationPoint(6.0f, 8.0f, 6.0f);
		(sidestick3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 16, 2);
		sidestick3.setRotationPoint(-8.0f, 8.0f, -8.0f);
		(sidestick4 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 16, 2);
		sidestick4.setRotationPoint(6.0f, 8.0f, -8.0f);
		(sidestuff1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 1.0f, 0.0f, 1, 18, 2);
		sidestuff1.setRotationPoint(6.0f, 8.5f, -6.5f);
		setRotation(sidestuff1, -0.7853982f, 1.570796f, 0.0f);
		(sidestuff2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, -1.0f, 0.0f, 1, 18, 2);
		sidestuff2.setRotationPoint(-7.5f, 9.5f, 5.0f);
		setRotation(sidestuff2, -0.7853982f, 0.0f, 0.0f);
		(sidestuff3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 1.0f, 0.0f, 1, 18, 2);
		sidestuff3.setRotationPoint(7.5f, 8.5f, -6.0f);
		setRotation(sidestuff3, -0.7853982f, 3.141593f, 0.0f);
		(sidestuff4 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 1.0f, 0.0f, 1, 18, 2);
		sidestuff4.setRotationPoint(-6.0f, 8.5f, 6.5f);
		setRotation(sidestuff4, -0.7853982f, -1.570796f, 0.0f);
		(Shape1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 18, 1, 2);
		Shape1.setRotationPoint(-5.5f, 22.5f, -7.0f);
		setRotation(Shape1, 0.0f, -0.7853982f, 0.0f);
		(Shape2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 18, 1, 2);
		Shape2.setRotationPoint(-5.5f, 8.5f, -7.0f);
		setRotation(Shape2, 0.0f, -0.7853982f, 0.0f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		sticky1.render(f5);
		sticky2.render(f5);
		sticky3.render(f5);
		sticky4.render(f5);
		core.render(f5);
		sticky1top.render(f5);
		sticky2top.render(f5);
		sticky3top.render(f5);
		sticky4top.render(f5);
		sidestick1.render(f5);
		sidestick2.render(f5);
		sidestick3.render(f5);
		sidestick4.render(f5);
		sidestuff1.render(f5);
		sidestuff2.render(f5);
		sidestuff3.render(f5);
		sidestuff4.render(f5);
		Shape1.render(f5);
		Shape2.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}

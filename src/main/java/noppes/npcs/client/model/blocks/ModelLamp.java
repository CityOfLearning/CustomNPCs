package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLamp extends ModelBase {
	ModelRenderer Base;
	ModelRenderer Top1;
	ModelRenderer Top2;
	ModelRenderer Top3;
	ModelRenderer Handle;
	ModelRenderer Shape1;

	public ModelLamp() {
		Base = new ModelRenderer(this, 0, 6);
		Base.addBox(0.0F, 0.0F, 0.0F, 4, 7, 4);
		Base.setRotationPoint(-2.0F, 16.0F, -2.0F);

		Top1 = new ModelRenderer(this, 0, 0);
		Top1.addBox(0.0F, 0.0F, 0.0F, 5, 1, 5);
		Top1.setRotationPoint(-2.5F, 16.0F, -2.5F);

		Top2 = new ModelRenderer(this, 0, 0);
		Top2.addBox(0.0F, 0.0F, 0.0F, 4, 1, 4);
		Top2.setRotationPoint(-2.0F, 15.5F, -2.0F);

		Top3 = new ModelRenderer(this, 0, 0);
		Top3.addBox(0.0F, 0.0F, 0.0F, 3, 1, 3);
		Top3.setRotationPoint(-1.5F, 15.0F, -1.5F);

		Handle = new ModelRenderer(this, 24, 0);
		Handle.addBox(0.0F, 0.0F, 0.0F, 3, 0, 3);
		Handle.setRotationPoint(0.0F, 15.0F, 0.0F);
		setRotation(Handle, 0.296706F, 0.1745329F, 0.0F);

		Shape1 = new ModelRenderer(this, 0, 17);
		Shape1.addBox(-2.0F, 0.0F, -2.0F, 4, 1, 4);
		Shape1.setRotationPoint(0.0F, 23.0F, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Base.render(f5);
		Top1.render(f5);
		Top2.render(f5);
		Top3.render(f5);
		Handle.render(f5);
		Shape1.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}

//

//

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
		(Base = new ModelRenderer(this, 0, 6)).addBox(0.0f, 0.0f, 0.0f, 4, 7, 4);
		Base.setRotationPoint(-2.0f, 16.0f, -2.0f);
		(Top1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 5, 1, 5);
		Top1.setRotationPoint(-2.5f, 16.0f, -2.5f);
		(Top2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 1, 4);
		Top2.setRotationPoint(-2.0f, 15.5f, -2.0f);
		(Top3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 3, 1, 3);
		Top3.setRotationPoint(-1.5f, 15.0f, -1.5f);
		(Handle = new ModelRenderer(this, 24, 0)).addBox(0.0f, 0.0f, 0.0f, 3, 0, 3);
		Handle.setRotationPoint(0.0f, 15.0f, 0.0f);
		setRotation(Handle, 0.296706f, 0.1745329f, 0.0f);
		(Shape1 = new ModelRenderer(this, 0, 17)).addBox(-2.0f, 0.0f, -2.0f, 4, 1, 4);
		Shape1.setRotationPoint(0.0f, 23.0f, 0.0f);
	}

	@Override
	public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
			final float f4, final float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Base.render(f5);
		Top1.render(f5);
		Top2.render(f5);
		Top3.render(f5);
		Handle.render(f5);
		Shape1.render(f5);
	}

	private void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}

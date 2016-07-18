//

//

package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelShelf extends ModelBase {
	public ModelRenderer SupportLeft2;
	ModelRenderer Top;
	public ModelRenderer SupportLeft1;
	public ModelRenderer SupportRight1;
	public ModelRenderer SupportRight2;

	public ModelShelf() {
		SupportLeft2 = new ModelRenderer(this, 0, 0);
		SupportLeft2.mirror = true;
		SupportLeft2.addBox(0.0f, 0.0f, 0.0f, 2, 10, 2);
		SupportLeft2.setRotationPoint(-7.498f, 9.5f, -0.5f);
		setRotation(SupportLeft2, 0.7853982f, 0.0f, 0.0f);
		(Top = new ModelRenderer(this, 5, 0)).addBox(0.0f, 0.0f, 0.0f, 16, 2, 11);
		Top.setRotationPoint(-8.0f, 8.0f, -3.0f);
		SupportLeft1 = new ModelRenderer(this, 0, 0);
		SupportLeft1.mirror = true;
		SupportLeft1.addBox(0.0f, 0.0f, 0.0f, 2, 7, 2);
		SupportLeft1.setRotationPoint(-7.5f, 10.0f, 6.0f);
		(SupportRight1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 7, 2);
		SupportRight1.setRotationPoint(5.5f, 10.0f, 6.0f);
		(SupportRight2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 10, 2);
		SupportRight2.setRotationPoint(5.498f, 9.5f, -0.5f);
		setRotation(SupportRight2, 0.7853982f, 0.0f, 0.0f);
	}

	@Override
	public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
			final float f4, final float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		SupportLeft2.render(f5);
		Top.render(f5);
		SupportLeft1.render(f5);
		SupportRight1.render(f5);
		SupportRight2.render(f5);
	}

	private void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}

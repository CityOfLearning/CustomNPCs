//

//

package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMailboxUS extends ModelBase {
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
	ModelRenderer Shape12;
	ModelRenderer Shape13;

	public ModelMailboxUS() {
		textureWidth = 64;
		textureHeight = 128;
		(Shape1 = new ModelRenderer(this, 0, 48)).addBox(0.0f, 0.0f, 0.0f, 16, 14, 16);
		Shape1.setRotationPoint(-8.0f, 8.0f, -8.0f);
		(Shape2 = new ModelRenderer(this, 0, 79)).addBox(0.0f, 0.0f, 0.0f, 1, 2, 1);
		Shape2.setRotationPoint(-8.0f, 22.0f, -8.0f);
		(Shape3 = new ModelRenderer(this, 5, 79)).addBox(0.0f, 0.0f, 0.0f, 1, 2, 1);
		Shape3.setRotationPoint(-8.0f, 22.0f, 7.0f);
		(Shape4 = new ModelRenderer(this, 10, 79)).addBox(0.0f, 0.0f, 0.0f, 1, 2, 1);
		Shape4.setRotationPoint(7.0f, 22.0f, -8.0f);
		(Shape5 = new ModelRenderer(this, 15, 79)).addBox(0.0f, 0.0f, 0.0f, 1, 2, 1);
		Shape5.setRotationPoint(7.0f, 22.0f, 7.0f);
		(Shape6 = new ModelRenderer(this, 0, 14)).addBox(0.0f, 0.0f, 0.0f, 16, 3, 7);
		Shape6.setRotationPoint(-8.0f, 5.0f, 0.0f);
		(Shape7 = new ModelRenderer(this, 0, 6)).addBox(0.0f, 0.0f, 0.0f, 16, 2, 6);
		Shape7.setRotationPoint(-8.0f, 3.0f, 0.0f);
		(Shape8 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 16, 1, 5);
		Shape8.setRotationPoint(-8.0f, 2.0f, 0.0f);
		(Shape9 = new ModelRenderer(this, 0, 37)).addBox(0.0f, 0.0f, 0.0f, 1, 3, 7);
		Shape9.setRotationPoint(-8.0f, 5.0f, -7.0f);
		(Shape10 = new ModelRenderer(this, 16, 37)).addBox(0.0f, 0.0f, 0.0f, 1, 3, 7);
		Shape10.setRotationPoint(7.0f, 5.0f, -7.0f);
		(Shape11 = new ModelRenderer(this, 0, 29)).addBox(0.0f, 0.0f, 0.0f, 1, 2, 6);
		Shape11.setRotationPoint(-8.0f, 3.0f, -6.0f);
		(Shape12 = new ModelRenderer(this, 14, 29)).addBox(0.0f, 0.0f, 0.0f, 1, 2, 6);
		Shape12.setRotationPoint(7.0f, 3.0f, -6.0f);
		(Shape13 = new ModelRenderer(this, 0, 25)).addBox(0.0f, 0.0f, 0.0f, 16, 1, 3);
		Shape13.setRotationPoint(-8.0f, 2.0f, -3.0f);
	}

	@Override
	public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
			final float f4, final float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
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
		Shape12.render(f5);
		Shape13.render(f5);
	}
}

//

//

package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelInk extends ModelBase {
	ModelRenderer InkMid;
	ModelRenderer InkTop;
	ModelRenderer InkBottom;
	ModelRenderer Shape1;
	ModelRenderer InkBottom2;

	public ModelInk() {
		(InkMid = new ModelRenderer(this, 0, 25)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
		InkMid.setRotationPoint(5.0f, 21.0f, 3.5f);
		(InkTop = new ModelRenderer(this, 0, 22)).addBox(0.0f, 0.0f, 0.0f, 2, 1, 2);
		InkTop.setRotationPoint(4.5f, 20.0f, 3.0f);
		(InkBottom = new ModelRenderer(this, 3, 16)).addBox(0.0f, 0.0f, 0.0f, 3, 1, 3);
		InkBottom.setRotationPoint(4.0f, 22.0f, 2.5f);
		(Shape1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 0, 13, 3);
		Shape1.setRotationPoint(5.5f, 10.0f, 2.5f);
		(InkBottom2 = new ModelRenderer(this, 0, 27)).addBox(0.0f, 0.0f, 0.0f, 3, 1, 3);
		InkBottom2.setRotationPoint(4.0f, 23.0f, 2.5f);
	}

	@Override
	public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
			final float f4, final float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Shape1.render(f5);
		InkMid.render(f5);
		InkTop.render(f5);
		InkBottom2.render(f5);
		InkBottom.render(f5);
	}
}

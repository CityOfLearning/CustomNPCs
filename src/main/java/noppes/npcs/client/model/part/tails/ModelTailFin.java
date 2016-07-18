//

//

package noppes.npcs.client.model.part.tails;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ModelTailFin extends ModelRenderer {
	public ModelTailFin(final ModelBiped base) {
		super(base);
		final ModelRenderer Shape1 = new ModelRenderer(base, 0, 0);
		Shape1.addBox(-2.0f, -2.0f, -2.0f, 3, 3, 8);
		Shape1.setRotationPoint(0.5f, 0.0f, 1.0f);
		setRotation(Shape1, -0.669215f, 0.0f, 0.0f);
		addChild(Shape1);
		final ModelRenderer Shape2 = new ModelRenderer(base, 2, 2);
		Shape2.addBox(-1.0f, -1.0f, 1.0f, 3, 2, 6);
		Shape2.setRotationPoint(-0.5f, 3.0f, 4.5f);
		setRotation(Shape2, -0.2602503f, 0.0f, 0.0f);
		addChild(Shape2);
		final ModelRenderer Shape3 = new ModelRenderer(base, 0, 11);
		Shape3.addBox(-1.0f, -1.0f, -1.0f, 3, 1, 6);
		Shape3.setRotationPoint(0.5f, 5.0f, 12.0f);
		setRotation(Shape3, 0.0f, 1.07818f, 0.0f);
		addChild(Shape3);
		final ModelRenderer Shape4 = new ModelRenderer(base, 0, 11);
		Shape4.mirror = true;
		Shape4.addBox(-2.0f, 0.0f, -1.0f, 3, 1, 6);
		Shape4.setRotationPoint(-0.5f, 4.0f, 12.0f);
		setRotation(Shape4, 0.0f, -1.003822f, 0.0f);
		addChild(Shape4);
	}

	private void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
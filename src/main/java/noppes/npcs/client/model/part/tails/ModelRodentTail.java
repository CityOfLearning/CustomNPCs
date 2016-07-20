//

//

package noppes.npcs.client.model.part.tails;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ModelRodentTail extends ModelRenderer {
	ModelRenderer Shape1;
	ModelRenderer Shape2;

	public ModelRodentTail(ModelBiped base) {
		super(base);
		(Shape1 = new ModelRenderer(base, 0, 0)).addBox(-0.5333334f, -0.4666667f, -1.0f, 1, 1, 6);
		Shape1.setRotationPoint(0.0f, 0.0f, 2.0f);
		setRotation(Shape1, -0.9294653f, 0.0f, 0.0f);
		addChild(Shape1);
		(Shape2 = new ModelRenderer(base, 1, 1)).addBox(-0.5f, -0.1666667f, 1.0f, 1, 1, 5);
		Shape2.setRotationPoint(0.0f, 3.0f, 4.0f);
		setRotation(Shape2, -0.4833219f, 0.0f, 0.0f);
		addChild(Shape2);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}

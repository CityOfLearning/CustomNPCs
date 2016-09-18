
package noppes.npcs.client.model.part.tails;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ModelFeatherTail extends ModelRenderer {
	ModelRenderer feather1;
	ModelRenderer feather2;
	ModelRenderer feather3;
	ModelRenderer feather4;
	ModelRenderer feather5;

	public ModelFeatherTail(ModelBiped base) {
		super(base);
		int x = 56;
		int y = 16;
		(feather1 = new ModelRenderer(base, x, y)).addBox(-1.5f, 0.0f, 0.0f, 3, 8, 0);
		feather1.setRotationPoint(1.0f, -0.5f, 2.0f);
		setRotation(feather1, 1.482807f, 0.2602503f, 0.1487144f);
		addChild(feather1);
		(feather2 = new ModelRenderer(base, x, y)).addBox(-1.5f, 0.0f, 0.0f, 3, 8, 0);
		feather2.setRotationPoint(0.0f, -0.5f, 1.0f);
		setRotation(feather2, 1.200559f, 0.3717861f, 0.1858931f);
		addChild(feather2);
		feather3 = new ModelRenderer(base, x, y);
		feather3.mirror = true;
		feather3.addBox(-1.5f, -0.5f, 0.0f, 3, 8, 0);
		feather3.setRotationPoint(-1.0f, 0.0f, 2.0f);
		setRotation(feather3, 1.256389f, -0.4089647f, -0.4833219f);
		addChild(feather3);
		(feather4 = new ModelRenderer(base, x, y)).addBox(-1.5f, 0.0f, 0.0f, 3, 8, 0);
		feather4.setRotationPoint(0.0f, -0.5f, 2.0f);
		setRotation(feather4, 1.786329f, 0.0f, 0.0f);
		addChild(feather4);
		feather5 = new ModelRenderer(base, x, y);
		feather5.mirror = true;
		feather5.addBox(-1.5f, 0.0f, 0.0f, 3, 8, 0);
		feather5.setRotationPoint(-1.0f, -0.5f, 2.0f);
		setRotation(feather5, 1.570073f, -0.2602503f, -0.2230717f);
		addChild(feather5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}

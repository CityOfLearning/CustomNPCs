//

//

package noppes.npcs.client.model.part.head;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class ModelDuckBeak extends ModelRenderer {
	ModelRenderer Top3;
	ModelRenderer Top2;
	ModelRenderer Bottom;
	ModelRenderer Left;
	ModelRenderer Right;
	ModelRenderer Middle;
	ModelRenderer Top;

	public ModelDuckBeak(final ModelBiped base) {
		super(base);
		(Top3 = new ModelRenderer(base, 14, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 1, 3);
		Top3.setRotationPoint(-1.0f, -2.0f, -5.0f);
		setRotation(Top3, 0.3346075f, 0.0f, 0.0f);
		addChild(Top3);
		(Top2 = new ModelRenderer(base, 0, 0)).addBox(0.0f, 0.0f, -0.4f, 4, 1, 3);
		Top2.setRotationPoint(-2.0f, -3.0f, -2.0f);
		setRotation(Top2, 0.3346075f, 0.0f, 0.0f);
		addChild(Top2);
		(Bottom = new ModelRenderer(base, 24, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 1, 5);
		Bottom.setRotationPoint(-1.0f, -1.0f, -5.0f);
		addChild(Bottom);
		Left = new ModelRenderer(base, 0, 4);
		Left.mirror = true;
		Left.addBox(0.0f, 0.0f, 0.0f, 1, 3, 2);
		Left.setRotationPoint(0.98f, -3.0f, -2.0f);
		addChild(Left);
		(Right = new ModelRenderer(base, 0, 4)).addBox(0.0f, 0.0f, 0.0f, 1, 3, 2);
		Right.setRotationPoint(-1.98f, -3.0f, -2.0f);
		addChild(Right);
		(Middle = new ModelRenderer(base, 3, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 1, 3);
		Middle.setRotationPoint(-1.0f, -2.0f, -5.0f);
		addChild(Middle);
		(Top = new ModelRenderer(base, 6, 4)).addBox(0.0f, 0.0f, 0.0f, 2, 2, 1);
		Top.setRotationPoint(-1.0f, -4.4f, -1.0f);
		addChild(Top);
	}

	@Override
	public void render(final float f) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0f, 0.0f, -1.0f * f);
		GlStateManager.scale(0.82f, 0.82f, 0.7f);
		super.render(f);
		GlStateManager.popMatrix();
	}

	private void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}

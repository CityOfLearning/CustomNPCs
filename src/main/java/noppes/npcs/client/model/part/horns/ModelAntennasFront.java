//

//

package noppes.npcs.client.model.part.horns;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ModelAntennasFront extends ModelRenderer {
	public ModelAntennasFront(final ModelBiped base) {
		super(base);
		final ModelRenderer rightantenna1 = new ModelRenderer(base, 60, 27);
		rightantenna1.addBox(0.0f, 0.0f, 0.0f, 1, 4, 1);
		rightantenna1.setRotationPoint(2.0f, -9.4f, -2.0f);
		setRotation(rightantenna1, 0.4014257f, 0.0698132f, 0.0698132f);
		addChild(rightantenna1);
		final ModelRenderer leftantenna1 = new ModelRenderer(base, 56, 27);
		leftantenna1.mirror = true;
		leftantenna1.addBox(-1.0f, 0.0f, 0.0f, 1, 4, 1);
		leftantenna1.setRotationPoint(-2.0f, -9.4f, -2.0f);
		setRotation(leftantenna1, 0.4014257f, -0.0698132f, -0.0698132f);
		addChild(leftantenna1);
		final ModelRenderer rightantenna2 = new ModelRenderer(base, 60, 27);
		rightantenna2.addBox(0.0f, 0.0f, 0.0f, 1, 4, 1);
		rightantenna2.setRotationPoint(3.0f, -10.2f, -5.3f);
		setRotation(rightantenna2, 1.22173f, -0.2094395f, 0.0698132f);
		addChild(rightantenna2);
		final ModelRenderer leftantenna2 = new ModelRenderer(base, 56, 27);
		leftantenna2.mirror = true;
		leftantenna2.addBox(-1.0f, 0.0f, 0.0f, 1, 4, 1);
		leftantenna2.setRotationPoint(-3.0f, -10.2f, -5.3f);
		setRotation(leftantenna2, 1.22173f, 0.2094395f, -0.0698132f);
		addChild(leftantenna2);
	}

	private void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}

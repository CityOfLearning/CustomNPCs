//

//

package noppes.npcs.client.model.part.head;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import noppes.npcs.client.model.Model2DRenderer;
import noppes.npcs.client.model.ModelScaleRenderer;
import noppes.npcs.constants.EnumParts;

public class ModelHeadwear extends ModelScaleRenderer {
	public ModelHeadwear(final ModelBase base) {
		super(base, EnumParts.HEAD);
		final Model2DRenderer right = new Model2DRenderer(base, 32.0f, 8.0f, 8, 8);
		right.setRotationPoint(-4.641f, 0.8f, 4.64f);
		right.setScale(0.58f);
		right.setThickness(0.65f);
		setRotation(right, 0.0f, 1.5707964f, 0.0f);
		addChild(right);
		final Model2DRenderer left = new Model2DRenderer(base, 48.0f, 8.0f, 8, 8);
		left.setRotationPoint(4.639f, 0.8f, -4.64f);
		left.setScale(0.58f);
		left.setThickness(0.65f);
		setRotation(left, 0.0f, -1.5707964f, 0.0f);
		addChild(left);
		final Model2DRenderer front = new Model2DRenderer(base, 40.0f, 8.0f, 8, 8);
		front.setRotationPoint(-4.64f, 0.801f, -4.641f);
		front.setScale(0.58f);
		front.setThickness(0.65f);
		setRotation(front, 0.0f, 0.0f, 0.0f);
		addChild(front);
		final Model2DRenderer back = new Model2DRenderer(base, 56.0f, 8.0f, 8, 8);
		back.setRotationPoint(4.64f, 0.801f, 4.639f);
		back.setScale(0.58f);
		back.setThickness(0.65f);
		setRotation(back, 0.0f, 3.1415927f, 0.0f);
		addChild(back);
		final Model2DRenderer top = new Model2DRenderer(base, 40.0f, 0.0f, 8, 8);
		top.setRotationPoint(-4.64f, -8.5f, -4.64f);
		top.setScale(0.5799f);
		top.setThickness(0.65f);
		setRotation(top, -1.5707964f, 0.0f, 0.0f);
		addChild(top);
		final Model2DRenderer bottom = new Model2DRenderer(base, 48.0f, 0.0f, 8, 8);
		bottom.setRotationPoint(-4.64f, 0.0f, -4.64f);
		bottom.setScale(0.5799f);
		bottom.setThickness(0.65f);
		setRotation(bottom, -1.5707964f, 0.0f, 0.0f);
		addChild(bottom);
	}

	@Override
	public void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}

//

//

package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSign extends ModelBase {
	public ModelRenderer Sign;
	ModelRenderer Chain2;
	ModelRenderer Bar;
	ModelRenderer Chain1;

	public ModelSign() {
		(Sign = new ModelRenderer(this, 0, 22)).addBox(0.0f, 0.0f, 0.0f, 14, 9, 1);
		Sign.setRotationPoint(-7.0f, 12.0f, -0.5f);
		setRotation(Sign, 0.0174533f, 0.0f, 0.0f);
		(Chain2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 2, 2);
		Chain2.setRotationPoint(5.0f, 11.0f, -1.0f);
		(Bar = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 16, 1, 1);
		Bar.setRotationPoint(-8.0f, 10.0f, -0.5f);
		(Chain1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 2, 2);
		Chain1.setRotationPoint(-6.0f, 11.0f, -1.0f);
	}

	@Override
	public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
			final float f4, final float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Chain2.render(f5);
		Bar.render(f5);
		Chain1.render(f5);
	}

	private void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}

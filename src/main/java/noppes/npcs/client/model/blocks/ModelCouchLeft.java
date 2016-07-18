//

//

package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCouchLeft extends ModelBase {
	ModelRenderer Leg1;
	ModelRenderer Leg2;
	ModelRenderer Leg3;
	ModelRenderer Leg4;
	ModelRenderer Back;
	ModelRenderer Side;
	ModelRenderer Bottom;

	public ModelCouchLeft() {
		(Leg1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 1, 2);
		Leg1.setRotationPoint(-8.0f, 23.0f, 6.0f);
		(Leg2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 2);
		Leg2.setRotationPoint(7.0f, 23.0f, -6.0f);
		(Leg3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 1, 2);
		Leg3.setRotationPoint(-8.0f, 23.0f, -6.0f);
		(Leg4 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 2);
		Leg4.setRotationPoint(7.0f, 23.0f, 6.0f);
		(Back = new ModelRenderer(this, 1, 15)).addBox(0.0f, 0.0f, 0.0f, 14, 15, 1);
		Back.setRotationPoint(-6.0f, 6.0f, 7.0f);
		(Side = new ModelRenderer(this, 1, 28)).addBox(0.0f, 0.0f, 0.0f, 2, 11, 14);
		Side.setRotationPoint(-8.0f, 12.0f, -6.0f);
		(Bottom = new ModelRenderer(this, 3, 1)).addBox(0.0f, 0.0f, 0.0f, 14, 2, 14);
		Bottom.setRotationPoint(-6.0f, 21.0f, -6.0f);
	}

	@Override
	public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
			final float f4, final float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Leg1.render(f5);
		Leg2.render(f5);
		Leg3.render(f5);
		Leg4.render(f5);
		Back.render(f5);
		Side.render(f5);
		Bottom.render(f5);
	}
}

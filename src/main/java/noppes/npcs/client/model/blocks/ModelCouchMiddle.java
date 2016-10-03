package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCouchMiddle extends ModelBase {
	ModelRenderer Leg1;
	ModelRenderer Leg2;
	ModelRenderer Leg3;
	ModelRenderer Leg4;
	ModelRenderer Bottom;
	ModelRenderer Back;

	public ModelCouchMiddle() {
		Leg1 = new ModelRenderer(this, 0, 0);
		Leg1.addBox(0.0F, 0.0F, 0.0F, 1, 1, 2);
		Leg1.setRotationPoint(7.0F, 23.0F, 6.0F);

		Leg2 = new ModelRenderer(this, 0, 0);
		Leg2.addBox(0.0F, 0.0F, 0.0F, 1, 1, 2);
		Leg2.setRotationPoint(7.0F, 23.0F, -6.0F);

		Leg3 = new ModelRenderer(this, 0, 0);
		Leg3.addBox(0.0F, 0.0F, 0.0F, 1, 1, 2);
		Leg3.setRotationPoint(-8.0F, 23.0F, -6.0F);

		Leg4 = new ModelRenderer(this, 0, 0);
		Leg4.addBox(0.0F, 0.0F, 0.0F, 1, 1, 2);
		Leg4.setRotationPoint(-8.0F, 23.0F, 6.0F);

		Bottom = new ModelRenderer(this, 2, 0);
		Bottom.addBox(0.0F, 0.0F, 0.0F, 16, 2, 14);
		Bottom.setRotationPoint(-8.0F, 21.0F, -6.0F);

		Back = new ModelRenderer(this, 14, 15);
		Back.addBox(0.0F, 0.0F, 0.0F, 16, 15, 1);
		Back.setRotationPoint(-8.0F, 6.0F, 7.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Leg1.render(f5);
		Leg2.render(f5);
		Leg3.render(f5);
		Leg4.render(f5);
		Bottom.render(f5);
		Back.render(f5);
	}
}

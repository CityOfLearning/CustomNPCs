package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTallLampTop extends ModelBase {
	ModelRenderer LampShade1;
	ModelRenderer LampShade3;
	ModelRenderer LampShade2;
	ModelRenderer LampShade4;

	public ModelTallLampTop() {
		LampShade1 = new ModelRenderer(this, 0, 0);
		LampShade1.addBox(-0.5F, -6.0F, -6.0F, 1, 12, 12);
		LampShade1.setRotationPoint(6.0F, -1.0F, 0.0F);

		LampShade3 = new ModelRenderer(this, 0, 0);
		LampShade3.addBox(-6.0F, -6.0F, -0.5F, 12, 12, 1);
		LampShade3.setRotationPoint(0.0F, -1.0F, -6.0F);

		LampShade2 = new ModelRenderer(this, 0, 0);
		LampShade2.addBox(-0.5F, -6.0F, -6.0F, 1, 12, 12);
		LampShade2.setRotationPoint(-6.0F, -1.0F, 0.0F);

		LampShade4 = new ModelRenderer(this, 0, 0);
		LampShade4.addBox(-6.0F, -6.0F, -0.5F, 12, 12, 1);
		LampShade4.setRotationPoint(0.0F, -1.0F, 6.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		LampShade1.render(f5);
		LampShade3.render(f5);
		LampShade2.render(f5);
		LampShade4.render(f5);
	}
}

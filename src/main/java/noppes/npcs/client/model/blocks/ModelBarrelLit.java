package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBarrelLit extends ModelBase {
	ModelRenderer Top;
	ModelRenderer Bottom;

	public ModelBarrelLit() {
		Top = new ModelRenderer(this, 0, 0);
		Top.addBox(0.0F, 0.0F, 0.0F, 16, 0, 16);
		Top.setRotationPoint(-8.0F, 9.0F, -8.0F);

		Bottom = new ModelRenderer(this, 0, 0);
		Bottom.addBox(0.0F, 0.0F, 0.0F, 16, 0, 16);
		Bottom.setRotationPoint(-8.0F, 23.0F, -8.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Bottom.render(f5);
		Top.render(f5);
	}
}

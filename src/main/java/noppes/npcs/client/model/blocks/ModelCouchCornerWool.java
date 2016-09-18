
package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCouchCornerWool extends ModelBase {
	ModelRenderer Wool1;
	ModelRenderer Wool2;
	ModelRenderer Wool3;
	ModelRenderer Wool4;

	public ModelCouchCornerWool() {
		(Wool1 = new ModelRenderer(this, 11, 3)).addBox(0.0f, 0.0f, 0.0f, 13, 5, 2);
		Wool1.setRotationPoint(-7.0f, 16.0f, -8.0f);
		(Wool2 = new ModelRenderer(this, 2, 4)).addBox(0.0f, 0.0f, 0.0f, 2, 10, 13);
		Wool2.setRotationPoint(-7.0f, 6.0f, -8.0f);
		(Wool3 = new ModelRenderer(this, 14, 15)).addBox(0.0f, 0.0f, 0.0f, 15, 10, 2);
		Wool3.setRotationPoint(-7.0f, 6.0f, 5.0f);
		(Wool4 = new ModelRenderer(this, 0, 45)).addBox(0.0f, 0.0f, 0.0f, 15, 5, 13);
		Wool4.setRotationPoint(-7.0f, 16.0f, -6.0f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Wool1.render(f5);
		Wool2.render(f5);
		Wool3.render(f5);
		Wool4.render(f5);
	}
}

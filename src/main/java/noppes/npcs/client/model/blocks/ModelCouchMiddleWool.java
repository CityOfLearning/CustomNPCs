
package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCouchMiddleWool extends ModelBase {
	ModelRenderer Wool1;
	ModelRenderer Wool2;

	public ModelCouchMiddleWool() {
		(Wool1 = new ModelRenderer(this, 3, 0)).addBox(0.0f, 0.0f, 0.0f, 16, 5, 13);
		Wool1.setRotationPoint(-8.0f, 16.0f, -6.0f);
		(Wool2 = new ModelRenderer(this, 14, 0)).addBox(0.0f, 0.0f, 0.0f, 16, 10, 2);
		Wool2.setRotationPoint(-8.0f, 6.0f, 5.0f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Wool1.render(f5);
		Wool2.render(f5);
	}
}

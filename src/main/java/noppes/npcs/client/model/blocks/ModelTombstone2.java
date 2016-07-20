//

//

package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTombstone2 extends ModelBase {
	ModelRenderer Top;
	ModelRenderer mid;

	public ModelTombstone2() {
		(Top = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 10, 1, 4);
		Top.setRotationPoint(-5.0f, 9.0f, -2.0f);
		(mid = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 12, 14, 4);
		mid.setRotationPoint(-6.0f, 10.0f, -2.0f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Top.render(f5);
		mid.render(f5);
	}
}

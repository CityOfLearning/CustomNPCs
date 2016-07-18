//

//

package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBarrelLit extends ModelBase {
	ModelRenderer Top;
	ModelRenderer Bottom;

	public ModelBarrelLit() {
		(Top = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 16, 0, 16);
		Top.setRotationPoint(-8.0f, 9.0f, -8.0f);
		(Bottom = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 16, 0, 16);
		Bottom.setRotationPoint(-8.0f, 23.0f, -8.0f);
	}

	@Override
	public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
			final float f4, final float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Bottom.render(f5);
		Top.render(f5);
	}
}

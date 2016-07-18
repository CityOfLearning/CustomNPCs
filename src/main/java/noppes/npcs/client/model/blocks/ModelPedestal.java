//

//

package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelPedestal extends ModelBase {
	ModelRenderer Main_Block;
	ModelRenderer Front;

	public ModelPedestal() {
		(Main_Block = new ModelRenderer(this, 1, 0)).addBox(-7.0f, 0.0f, -8.0f, 14, 3, 16);
		Main_Block.setRotationPoint(0.0f, 16.0f, 0.0f);
		(Front = new ModelRenderer(this, 16, 8)).addBox(-8.0f, 0.0f, -8.0f, 16, 5, 16);
		Front.setRotationPoint(0.0f, 19.0f, 0.0f);
	}

	@Override
	public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
			final float f4, final float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		GlStateManager.pushMatrix();
		GlStateManager.scale(1.0f, 1.0f, 0.5f);
		Main_Block.render(f5);
		GlStateManager.scale(1.0f, 1.0f, 1.25f);
		Front.render(f5);
		GlStateManager.popMatrix();
	}
}

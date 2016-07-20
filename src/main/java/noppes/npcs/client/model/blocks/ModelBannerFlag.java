//

//

package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBannerFlag extends ModelBase {
	ModelRenderer Flag;

	public ModelBannerFlag() {
		textureWidth = 32;
		textureHeight = 32;
		(Flag = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 15, 27, 0);
		Flag.setRotationPoint(-7.5f, -7.0f, -2.0f);
		Flag.setTextureSize(32, 32);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Flag.render(f5);
	}
}

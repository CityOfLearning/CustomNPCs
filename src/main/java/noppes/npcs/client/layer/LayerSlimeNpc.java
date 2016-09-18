
package noppes.npcs.client.layer;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;

public class LayerSlimeNpc implements LayerRenderer {
	private RenderLiving renderer;
	private ModelBase slimeModel;

	public LayerSlimeNpc(RenderLiving renderer) {
		slimeModel = new ModelSlime(0);
		this.renderer = renderer;
	}

	@Override
	public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_,
			float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
		if (p_177141_1_.isInvisible()) {
			return;
		}
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		slimeModel.setModelAttributes(renderer.getMainModel());
		slimeModel.render(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
		GlStateManager.disableBlend();
		GlStateManager.disableNormalize();
	}

	@Override
	public boolean shouldCombineTextures() {
		return true;
	}
}

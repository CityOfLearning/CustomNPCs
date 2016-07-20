//

//

package noppes.npcs.client.layer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.client.renderer.RenderCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

@SideOnly(Side.CLIENT)
public class LayerGlow implements LayerRenderer {
	private RenderCustomNpc renderer;

	public LayerGlow(RenderCustomNpc p_i46117_1_) {
		renderer = p_i46117_1_;
	}

	@Override
	public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_,
			float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
		render((EntityNPCInterface) p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_,
				p_177141_7_, p_177141_8_);
	}

	public void render(EntityNPCInterface npc, float p_177201_2_, float p_177201_3_, float p_177201_4_,
			float p_177201_5_, float p_177201_6_, float p_177201_7_, float p_177201_8_) {
		if (npc.display.getOverlayTexture().isEmpty()) {
			return;
		}
		if (npc.textureGlowLocation == null) {
			npc.textureGlowLocation = new ResourceLocation(npc.display.getOverlayTexture());
		}
		renderer.bindTexture(npc.textureGlowLocation);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(1, 1);
		GlStateManager.disableLighting();
		GlStateManager.depthFunc(514);
		char c0 = '\uf0f0';
		int i = c0 % 65536;
		int j = c0 / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i / 1.0f, j / 1.0f);
		GlStateManager.enableLighting();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		renderer.getMainModel().render(npc, p_177201_2_, p_177201_3_, p_177201_5_, p_177201_6_, p_177201_7_,
				p_177201_8_);
		renderer.setLightmap(npc, p_177201_4_);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.depthFunc(515);
	}

	@Override
	public boolean shouldCombineTextures() {
		return true;
	}
}

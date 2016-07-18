//

//

package noppes.npcs.client.layer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import noppes.npcs.ModelData;
import noppes.npcs.ModelPartData;
import noppes.npcs.client.ClientProxy;
import noppes.npcs.entity.EntityCustomNpc;

public abstract class LayerInterface implements LayerRenderer {
	protected RenderLiving render;
	protected EntityCustomNpc npc;
	protected ModelData playerdata;
	public ModelBiped model;

	public LayerInterface(final RenderLiving render) {
		this.render = render;
		model = (ModelBiped) render.getMainModel();
	}

	private int blend(final int color1, final int color2, final float ratio) {
		if (ratio >= 1.0f) {
			return color2;
		}
		if (ratio <= 0.0f) {
			return color1;
		}
		final int aR = (color1 & 0xFF0000) >> 16;
		final int aG = (color1 & 0xFF00) >> 8;
		final int aB = color1 & 0xFF;
		final int bR = (color2 & 0xFF0000) >> 16;
		final int bG = (color2 & 0xFF00) >> 8;
		final int bB = color2 & 0xFF;
		final int R = (int) (aR + ((bR - aR) * ratio));
		final int G = (int) (aG + ((bG - aG) * ratio));
		final int B = (int) (aB + ((bB - aB) * ratio));
		return (R << 16) | (G << 8) | B;
	}

	@Override
	public void doRenderLayer(final EntityLivingBase entity, final float par2, final float par3, final float par8,
			final float par4, final float par5, final float par6, final float par7) {
		npc = (EntityCustomNpc) entity;
		if (npc.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer)) {
			return;
		}
		playerdata = npc.modelData;
		model = (ModelBiped) render.getMainModel();
		rotate(par2, par3, par4, par5, par6, par7);
		GlStateManager.pushMatrix();
		if (entity.isInvisible()) {
			GlStateManager.color(1.0f, 1.0f, 1.0f, 0.15f);
			GlStateManager.depthMask(false);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 771);
			GlStateManager.alphaFunc(516, 0.003921569f);
		}
		if ((npc.hurtTime > 0) || (npc.deathTime > 0)) {
			GlStateManager.color(1.0f, 0.0f, 0.0f, 0.3f);
		}
		if (npc.isSneaking()) {
			GlStateManager.translate(0.0f, 0.2f, 0.0f);
		}
		GlStateManager.enableRescaleNormal();
		render(par2, par3, par4, par5, par6, par7);
		GlStateManager.disableRescaleNormal();
		if (entity.isInvisible()) {
			GlStateManager.disableBlend();
			GlStateManager.alphaFunc(516, 0.1f);
			GlStateManager.depthMask(true);
		}
		GlStateManager.popMatrix();
	}

	public void preRender(final ModelPartData data) {
		if (data.playerTexture) {
			ClientProxy.bindTexture(npc.textureLocation);
		} else {
			ClientProxy.bindTexture(data.getResource());
		}
		if ((npc.hurtTime > 0) || (npc.deathTime > 0)) {
			return;
		}
		int color = data.color;
		if (npc.display.getTint() != 16777215) {
			if (data.color != 16777215) {
				color = blend(data.color, npc.display.getTint(), 0.5f);
			} else {
				color = npc.display.getTint();
			}
		}
		final float red = ((color >> 16) & 0xFF) / 255.0f;
		final float green = ((color >> 8) & 0xFF) / 255.0f;
		final float blue = (color & 0xFF) / 255.0f;
		GlStateManager.color(red, green, blue, npc.isInvisible() ? 0.15f : 0.99f);
	}

	public abstract void render(final float p0, final float p1, final float p2, final float p3, final float p4,
			final float p5);

	public abstract void rotate(final float p0, final float p1, final float p2, final float p3, final float p4,
			final float p5);

	public void setColor(final ModelPartData data, final EntityLivingBase entity) {
	}

	public void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}

//

//

package noppes.npcs.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.entity.EntityProjectile;

@SideOnly(Side.CLIENT)
public class RenderProjectile extends Render {
	private static final ResourceLocation arrowTextures;
	private static final ResourceLocation RES_ITEM_GLINT;
	static {
		arrowTextures = new ResourceLocation("textures/entity/arrow.png");
		RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	}

	public boolean renderWithColor;

	public RenderProjectile() {
		super(Minecraft.getMinecraft().getRenderManager());
		renderWithColor = true;
	}

	@Override
	public void doRender(final Entity par1Entity, final double par2, final double par4, final double par6,
			final float par8, final float par9) {
		doRenderProjectile((EntityProjectile) par1Entity, par2, par4, par6, par8, par9);
	}

	public void doRenderProjectile(final EntityProjectile par1EntityProjectile, final double par2, final double par4,
			final double par6, final float par8, final float par9) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) par2, (float) par4, (float) par6);
		GlStateManager.enableRescaleNormal();
		final float f = par1EntityProjectile.getDataWatcher().getWatchableObjectInt(23) / 5.0f;
		final ItemStack item = par1EntityProjectile.getItemDisplay();
		GlStateManager.scale(f, f, f);
		final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
		if (par1EntityProjectile.isArrow()) {
			bindEntityTexture(par1EntityProjectile);
			GlStateManager.rotate((par1EntityProjectile.prevRotationYaw
					+ ((par1EntityProjectile.rotationYaw - par1EntityProjectile.prevRotationYaw) * par9)) - 90.0f, 0.0f,
					1.0f, 0.0f);
			GlStateManager.rotate(
					par1EntityProjectile.prevRotationPitch
							+ ((par1EntityProjectile.rotationPitch - par1EntityProjectile.prevRotationPitch) * par9),
					0.0f, 0.0f, 1.0f);
			final byte b0 = 0;
			final float f2 = 0.0f;
			final float f3 = 0.5f;
			final float f4 = (0 + (b0 * 10)) / 32.0f;
			final float f5 = (5 + (b0 * 10)) / 32.0f;
			final float f6 = 0.0f;
			final float f7 = 0.15625f;
			final float f8 = (5 + (b0 * 10)) / 32.0f;
			final float f9 = (10 + (b0 * 10)) / 32.0f;
			final float f10 = 0.05625f;
			GlStateManager.enableRescaleNormal();
			final float f11 = par1EntityProjectile.arrowShake - par9;
			if (f11 > 0.0f) {
				final float f12 = -MathHelper.sin(f11 * 3.0f) * f11;
				GlStateManager.rotate(f12, 0.0f, 0.0f, 1.0f);
			}
			GlStateManager.rotate(45.0f, 1.0f, 0.0f, 0.0f);
			GlStateManager.scale(f10, f10, f10);
			GlStateManager.translate(-4.0f, 0.0f, 0.0f);
			worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
			worldRenderer.pos(-7.0, -2.0, -2.0).tex(f6, f8).normal(f10, 0.0f, 0.0f).endVertex();
			worldRenderer.pos(-7.0, -2.0, 2.0).tex(f7, f8).normal(f10, 0.0f, 0.0f).endVertex();
			worldRenderer.pos(-7.0, 2.0, 2.0).tex(f7, f9).normal(f10, 0.0f, 0.0f).endVertex();
			worldRenderer.pos(-7.0, 2.0, -2.0).tex(f6, f9).normal(f10, 0.0f, 0.0f).endVertex();
			Tessellator.getInstance().draw();
			worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
			worldRenderer.pos(-7.0, 2.0, -2.0).tex(f6, f8).normal(-f10, 0.0f, 0.0f).endVertex();
			worldRenderer.pos(-7.0, 2.0, 2.0).tex(f7, f8).normal(-f10, 0.0f, 0.0f).endVertex();
			worldRenderer.pos(-7.0, -2.0, 2.0).tex(f7, f9).normal(-f10, 0.0f, 0.0f).endVertex();
			worldRenderer.pos(-7.0, -2.0, -2.0).tex(f6, f9).normal(-f10, 0.0f, 0.0f).endVertex();
			Tessellator.getInstance().draw();
			for (int i = 0; i < 4; ++i) {
				GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
				worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
				worldRenderer.pos(-8.0, -2.0, 0.0).tex(f2, f4).normal(0.0f, 0.0f, f10).endVertex();
				worldRenderer.pos(8.0, -2.0, 0.0).tex(f3, f4).normal(0.0f, 0.0f, f10).endVertex();
				worldRenderer.pos(8.0, 2.0, 0.0).tex(f3, f5).normal(0.0f, 0.0f, f10).endVertex();
				worldRenderer.pos(-8.0, 2.0, 0.0).tex(f2, f5).normal(0.0f, 0.0f, f10).endVertex();
				Tessellator.getInstance().draw();
			}
		} else if (par1EntityProjectile.is3D()) {
			GlStateManager.rotate((par1EntityProjectile.prevRotationYaw
					+ ((par1EntityProjectile.rotationYaw - par1EntityProjectile.prevRotationYaw) * par9)) - 180.0f,
					0.0f, 1.0f, 0.0f);
			GlStateManager.rotate(
					par1EntityProjectile.prevRotationPitch
							+ ((par1EntityProjectile.rotationPitch - par1EntityProjectile.prevRotationPitch) * par9),
					1.0f, 0.0f, 0.0f);
			GlStateManager.translate(0.0, -0.125, 0.25);
			if ((item.getItem() instanceof ItemBlock)
					&& (Block.getBlockFromItem(item.getItem()).getRenderType() == 2)) {
				GlStateManager.translate(0.0f, 0.1875f, -0.3125f);
				GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
				GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
				final float f13 = 0.375f;
				GlStateManager.scale(-f13, -f13, f13);
			}
			Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.THIRD_PERSON);
		} else {
			GlStateManager.enableRescaleNormal();
			GlStateManager.scale(0.5f, 0.5f, 0.5f);
			GlStateManager.rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
			GlStateManager.rotate(renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
			bindTexture(TextureMap.locationBlocksTexture);
			Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.NONE);
			GlStateManager.disableRescaleNormal();
		}
		if (par1EntityProjectile.is3D() && par1EntityProjectile.glows()) {
			GlStateManager.disableLighting();
		}
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		GlStateManager.enableLighting();
	}

	protected ResourceLocation func_110779_a(final EntityProjectile par1EntityProjectile) {
		return par1EntityProjectile.isArrow() ? RenderProjectile.arrowTextures : TextureMap.locationBlocksTexture;
	}

	@Override
	protected ResourceLocation getEntityTexture(final Entity par1Entity) {
		return func_110779_a((EntityProjectile) par1Entity);
	}
}

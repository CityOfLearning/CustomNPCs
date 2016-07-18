//

//

package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.blocks.tiles.TileBanner;
import noppes.npcs.client.model.blocks.ModelBanner;
import noppes.npcs.client.model.blocks.ModelBannerFlag;

public class BlockBannerRenderer extends BlockRendererInterface {
	public static final ResourceLocation resourceFlag;
	static {
		resourceFlag = new ResourceLocation("customnpcs", "textures/models/BannerFlag.png");
	}
	private final ModelBanner model;

	private final ModelBannerFlag flag;

	public BlockBannerRenderer() {
		model = new ModelBanner();
		flag = new ModelBannerFlag();
	}

	public void doRender(final double par2, final double par4, final double par6, final int meta,
			final ItemStack iicon) {
		if (iicon.getItem() instanceof ItemBlock) {
			return;
		}
		GlStateManager.pushMatrix();
		bindTexture(TextureMap.locationBlocksTexture);
		GlStateManager.translate((float) par2 + 0.5f, (float) par4 + 1.3f, (float) par6 + 0.5f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(90 * meta, 0.0f, 1.0f, 0.0f);
		GlStateManager.translate(0.0, 0.0, 0.62);
		GlStateManager.depthMask(false);
		final float f2 = 0.05f;
		final Minecraft mc = Minecraft.getMinecraft();
		GlStateManager.scale(f2, f2, -0.005);
		mc.getRenderItem().renderItemAndEffectIntoGUI(iicon, -8, -8);
		GlStateManager.depthMask(true);
		GlStateManager.popMatrix();
	}

	@Override
	public void renderTileEntityAt(final TileEntity var1, final double var2, final double var4, final double var6,
			final float var8, final int p_180535_9_) {
		final TileBanner tile = (TileBanner) var1;
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) var2 + 0.5f, (float) var4 + 1.5f, (float) var6 + 0.5f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(90 * tile.rotation, 0.0f, 1.0f, 0.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		BlockRendererInterface.setMaterialTexture(var1.getBlockMetadata());
		model.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		bindTexture(BlockBannerRenderer.resourceFlag);
		final float[] color = BlockRendererInterface.colorTable[tile.color];
		GlStateManager.color(color[0], color[1], color[2]);
		flag.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		GlStateManager.popMatrix();
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		if ((tile.icon != null) && !playerTooFar(tile)) {
			doRender(var2, var4, var6, tile.rotation, tile.icon);
		}
	}

	@Override
	public int specialRenderDistance() {
		return 26;
	}
}

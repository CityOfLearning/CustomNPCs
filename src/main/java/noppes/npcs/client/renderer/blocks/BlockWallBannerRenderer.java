//

//

package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.blocks.tiles.TileWallBanner;
import noppes.npcs.client.model.blocks.ModelWallBanner;
import noppes.npcs.client.model.blocks.ModelWallBannerFlag;

public class BlockWallBannerRenderer extends BlockRendererInterface {
	private final ModelWallBanner model;
	private final ModelWallBannerFlag flag;

	public BlockWallBannerRenderer() {
		model = new ModelWallBanner();
		flag = new ModelWallBannerFlag();
	}

	public void doRender(final double par2, final double par4, final double par6, final int meta,
			final ItemStack iicon) {
		if (iicon.getItem() instanceof ItemBlock) {
			return;
		}
		GlStateManager.pushMatrix();
		bindTexture(TextureMap.locationBlocksTexture);
		GlStateManager.translate((float) par2 + 0.5f, (float) par4 + 0.2f, (float) par6 + 0.5f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(90 * meta, 0.0f, 1.0f, 0.0f);
		GlStateManager.translate(0.0, 0.0, 1.02);
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
			final float var8, final int blockDamage) {
		final TileWallBanner tile = (TileWallBanner) var1;
		GlStateManager.enableLighting();
		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) var2 + 0.5f, (float) var4 + 0.4f, (float) var6 + 0.5f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(90 * tile.rotation, 0.0f, 1.0f, 0.0f);
		BlockRendererInterface.setMaterialTexture(var1.getBlockMetadata());
		GlStateManager.color(1.0f, 1.0f, 1.0f);
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
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
	}

	@Override
	public int specialRenderDistance() {
		return 26;
	}
}

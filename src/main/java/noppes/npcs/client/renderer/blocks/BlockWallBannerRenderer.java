package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.blocks.tiles.TileWallBanner;
import noppes.npcs.client.model.blocks.ModelWallBanner;
import noppes.npcs.client.model.blocks.ModelWallBannerFlag;

public class BlockWallBannerRenderer extends BlockRendererInterface {
	private final ModelWallBanner model = new ModelWallBanner();
	private final ModelWallBannerFlag flag = new ModelWallBannerFlag();

	public void doRender(double par2, double par4, double par6, int meta, ItemStack iicon) {
		// if ((iicon.getItem() instanceof ItemBlock)) {
		// return;
		// }
		GlStateManager.pushMatrix();
		bindTexture(TextureMap.locationBlocksTexture);
		GlStateManager.translate((float) par2 + 0.5F, (float) par4 + 0.2F, (float) par6 + 0.5F);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(90 * meta, 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(0.0D, 0.0D, 1.02D);
		GlStateManager.depthMask(false);
		float f2 = 0.05F;
		Minecraft mc = Minecraft.getMinecraft();
		GlStateManager.scale(f2, f2, -0.005D);
		mc.getRenderItem().renderItemAndEffectIntoGUI(iicon, -8, -8);
		GlStateManager.depthMask(true);
		GlStateManager.popMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8,
			int blockDamage) {
		TileWallBanner tile = (TileWallBanner) var1;
		GlStateManager.enableLighting();
		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) var2 + 0.5F, (float) var4 + 0.4F, (float) var6 + 0.5F);

		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(90 * tile.getRotation(), 0.0F, 1.0F, 0.0F);

		setMaterialTexture(var1.getBlockMetadata());
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

		bindTexture(BlockBannerRenderer.resourceFlag);
		if (tile.getColor() > colorTable.length) {
			int color = tile.getColor();
			float f3 = ((color >> 24) & 255) / 255.0F;
			float f = ((color >> 16) & 255) / 255.0F;
			float f1 = ((color >> 8) & 255) / 255.0F;
			float f2 = (color & 255) / 255.0F;
			GlStateManager.color(f, f1, f2, f3);
		} else {
			float[] color = colorTable[tile.getColor()];
			GlStateManager.color(color[0], color[1], color[2]);
		}
		flag.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

		GlStateManager.popMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		if ((tile.getIcon() != null) && (!playerTooFar(tile))) {
			doRender(var2, var4, var6, tile.getRotation(), tile.getIcon());
		}
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
	}

	@Override
	public int specialRenderDistance() {
		return 26;
	}
}

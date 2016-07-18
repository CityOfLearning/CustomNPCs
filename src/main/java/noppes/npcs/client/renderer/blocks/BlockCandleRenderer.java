//

//

package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.blocks.tiles.TileCandle;
import noppes.npcs.client.model.blocks.ModelCandle;
import noppes.npcs.client.model.blocks.ModelCandleCeiling;
import noppes.npcs.client.model.blocks.ModelCandleWall;

public class BlockCandleRenderer extends TileEntitySpecialRenderer {
	private static final ResourceLocation resource1;
	static {
		resource1 = new ResourceLocation("customnpcs", "textures/models/Candle.png");
	}
	private final ModelCandle model;
	private final ModelCandleWall modelWall;

	private final ModelCandleCeiling modelCeiling;

	public BlockCandleRenderer() {
		model = new ModelCandle();
		modelWall = new ModelCandleWall();
		modelCeiling = new ModelCandleCeiling();
	}

	@Override
	public void renderTileEntityAt(final TileEntity var1, final double var2, final double var4, final double var6,
			final float var8, final int blockDamage) {
		final TileCandle tile = (TileCandle) var1;
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) var2 + 0.5f, (float) var4 + 1.5f, (float) var6 + 0.5f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(45 * tile.rotation, 0.0f, 1.0f, 0.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(BlockCandleRenderer.resource1);
		if (tile.color == 0) {
			model.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		} else if (tile.color == 1) {
			modelCeiling.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		} else {
			GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
			modelWall.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		}
		GlStateManager.popMatrix();
	}
}

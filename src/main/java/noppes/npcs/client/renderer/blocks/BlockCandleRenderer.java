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
	private static final ResourceLocation resource1 = new ResourceLocation("customnpcs", "textures/models/Candle.png");
	private final ModelCandle model = new ModelCandle();
	private final ModelCandleWall modelWall = new ModelCandleWall();
	private final ModelCandleCeiling modelCeiling = new ModelCandleCeiling();

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8,
			int blockDamage) {
		TileCandle tile = (TileCandle) var1;
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) var2 + 0.5F, (float) var4 + 1.5F, (float) var6 + 0.5F);

		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(45 * tile.rotation, 0.0F, 1.0F, 0.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F);

		Minecraft.getMinecraft().getTextureManager().bindTexture(resource1);
		if (tile.color == 0) {
			model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		} else if (tile.color == 1) {
			modelCeiling.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		} else {
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			modelWall.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		}
		GlStateManager.popMatrix();
	}
}

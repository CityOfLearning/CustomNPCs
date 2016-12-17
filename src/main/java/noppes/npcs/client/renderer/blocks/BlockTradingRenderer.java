
package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.client.model.blocks.ModelTradingTable;

public class BlockTradingRenderer extends BlockRendererInterface {
	private static ResourceLocation resource1;
	private static ResourceLocation resource2;
	static {
		resource1 = new ResourceLocation("customnpcs", "textures/models/TradingTable.png");
		resource2 = new ResourceLocation("customnpcs", "textures/models/TradingArrows.png");
	}

	private ModelTradingTable model;

	public BlockTradingRenderer() {
		model = new ModelTradingTable();
	}

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8,
			int blockDamage) {
		TileColorable tile = (TileColorable) var1;
		GlStateManager.enableLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) var2 + 0.5f, (float) var4 + 1.42f, (float) var6 + 0.5f);
		GlStateManager.scale(1.0f, 0.94f, 1.0f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(90 * tile.getRotation(), 0.0f, 1.0f, 0.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(BlockTradingRenderer.resource1);
		model.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.translate(var2 + 0.05, var4, var6 + 0.05);
		GlStateManager.scale(0.9f, 0.9f, 0.9f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(BlockTradingRenderer.resource2);
		WorldRenderer tessellator = Tessellator.getInstance().getWorldRenderer();
		tessellator.begin(7, DefaultVertexFormats.POSITION_TEX);
		int i = 0;
		int j = 1;
		int k = 1;
		if (tile.getRotation() == 0) {
			i = -1;
			j = 0;
			k = -1;
		} else if (tile.getRotation() == 3) {
			j = -1;
			k = -1;
		} else if (tile.getRotation() == 2) {
			i = 1;
			j = 0;
			k = 1;
		}
		tessellator.pos(0.0, 0.01, 0.0).tex(0.0, 0.0).endVertex();
		tessellator.pos(1.0, 0.01, 0.0).tex(i, j).endVertex();
		tessellator.pos(1.0, 0.01, 1.0).tex(k, k).endVertex();
		tessellator.pos(0.0, 0.01, 1.0).tex(j, i).endVertex();
		Tessellator.getInstance().draw();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}
}

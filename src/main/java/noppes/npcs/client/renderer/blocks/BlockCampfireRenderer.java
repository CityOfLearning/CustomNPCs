//

//

package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.client.model.blocks.ModelCampfire;

public class BlockCampfireRenderer extends BlockRendererInterface {
	private final ModelCampfire model;

	public BlockCampfireRenderer() {
		model = new ModelCampfire();
	}

	@Override
	public void renderTileEntityAt(final TileEntity var1, final double var2, final double var4, final double var6,
			final float var8, final int blockDamage) {
		final TileColorable tile = (TileColorable) var1;
		GlStateManager.enableLighting();
		GlStateManager.pushMatrix();
		GlStateManager.disableBlend();
		GlStateManager.translate((float) var2 + 0.5f, (float) var4 + 1.5f, (float) var6 + 0.5f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(45 * tile.rotation, 0.0f, 1.0f, 0.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(BlockRendererInterface.PlanksOak);
		model.renderLog(0.0625f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(BlockRendererInterface.Stone);
		model.renderRock(0.0625f);
		GlStateManager.popMatrix();
	}
}

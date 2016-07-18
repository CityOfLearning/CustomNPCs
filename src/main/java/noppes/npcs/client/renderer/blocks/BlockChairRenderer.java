//

//

package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.client.model.blocks.ModelChair;

public class BlockChairRenderer extends BlockRendererInterface {
	private final ModelChair model;

	public BlockChairRenderer() {
		model = new ModelChair();
	}

	@Override
	public void renderTileEntityAt(final TileEntity var1, final double var2, final double var4, final double var6,
			final float var8, final int blockDamage) {
		final TileColorable tile = (TileColorable) var1;
		GlStateManager.enableLighting();
		GlStateManager.pushMatrix();
		GlStateManager.disableBlend();
		GlStateManager.translate((float) var2 + 0.5f, (float) var4 + 1.68f, (float) var6 + 0.5f);
		GlStateManager.scale(1.2f, 1.1f, 1.2f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(90 * tile.rotation, 0.0f, 1.0f, 0.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		setWoodTexture(var1.getBlockMetadata());
		model.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		GlStateManager.popMatrix();
	}
}

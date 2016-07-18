//

//

package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.blocks.tiles.TileCouchWood;
import noppes.npcs.client.model.blocks.ModelCouchWoodLeft;
import noppes.npcs.client.model.blocks.ModelCouchWoodMiddle;
import noppes.npcs.client.model.blocks.ModelCouchWoodRight;
import noppes.npcs.client.model.blocks.ModelCouchWoodSingle;

public class BlockCouchWoodRenderer extends BlockRendererInterface {
	private final ModelBase model;
	private final ModelBase modelLeft;
	private final ModelBase modelRight;
	private final ModelBase modelCorner;

	public BlockCouchWoodRenderer() {
		model = new ModelCouchWoodMiddle();
		modelLeft = new ModelCouchWoodLeft();
		modelRight = new ModelCouchWoodRight();
		modelCorner = new ModelCouchWoodSingle();
	}

	@Override
	public void renderTileEntityAt(final TileEntity var1, final double var2, final double var4, final double var6,
			final float var8, final int blockDamage) {
		final TileCouchWood tile = (TileCouchWood) var1;
		GlStateManager.enableLighting();
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) var2 + 0.5f, (float) var4 + 1.5f, (float) var6 + 0.5f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(90 * tile.rotation, 0.0f, 1.0f, 0.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		setWoodTexture(var1.getBlockMetadata());
		if (tile.hasLeft && tile.hasRight) {
			model.render((Entity) null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		} else if (tile.hasLeft) {
			modelLeft.render((Entity) null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		} else if (tile.hasRight) {
			modelRight.render((Entity) null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		} else {
			modelCorner.render((Entity) null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		}
		GlStateManager.popMatrix();
	}
}

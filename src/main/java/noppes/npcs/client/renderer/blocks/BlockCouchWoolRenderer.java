//

//

package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.blocks.tiles.TileCouchWool;
import noppes.npcs.client.model.blocks.ModelCouchCorner;
import noppes.npcs.client.model.blocks.ModelCouchCornerWool;
import noppes.npcs.client.model.blocks.ModelCouchLeft;
import noppes.npcs.client.model.blocks.ModelCouchLeftWool;
import noppes.npcs.client.model.blocks.ModelCouchMiddle;
import noppes.npcs.client.model.blocks.ModelCouchMiddleWool;
import noppes.npcs.client.model.blocks.ModelCouchRight;
import noppes.npcs.client.model.blocks.ModelCouchRightWool;

public class BlockCouchWoolRenderer extends BlockRendererInterface {
	private final ModelBase model;
	private final ModelBase model2;
	private final ModelBase modelLeft;
	private final ModelBase modelLeft2;
	private final ModelBase modelRight;
	private final ModelBase modelRight2;
	private final ModelBase modelCorner;
	private final ModelBase modelCorner2;

	public BlockCouchWoolRenderer() {
		model = new ModelCouchMiddle();
		model2 = new ModelCouchMiddleWool();
		modelLeft = new ModelCouchLeft();
		modelLeft2 = new ModelCouchLeftWool();
		modelRight = new ModelCouchRight();
		modelRight2 = new ModelCouchRightWool();
		modelCorner = new ModelCouchCorner();
		modelCorner2 = new ModelCouchCornerWool();
	}

	@Override
	public void renderTileEntityAt(final TileEntity var1, final double var2, final double var4, final double var6,
			final float var8, final int blockDamage) {
		final TileCouchWool tile = (TileCouchWool) var1;
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) var2 + 0.5f, (float) var4 + 1.5f, (float) var6 + 0.5f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(90 * tile.rotation, 0.0f, 1.0f, 0.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		setWoodTexture(var1.getBlockMetadata());
		if (tile.hasCornerLeft) {
			modelCorner.render((Entity) null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		} else if (tile.hasCornerRight) {
			GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
			modelCorner.render((Entity) null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		} else if (tile.hasLeft && tile.hasRight) {
			model.render((Entity) null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		} else if (tile.hasLeft) {
			modelLeft.render((Entity) null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		} else if (tile.hasRight) {
			modelRight.render((Entity) null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		} else {
			model.render((Entity) null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		}
		bindTexture(BlockTallLampRenderer.resourceTop);
		final float[] color = BlockRendererInterface.colorTable[tile.color];
		GlStateManager.color(color[0], color[1], color[2]);
		if (tile.hasCornerLeft || tile.hasCornerRight) {
			modelCorner2.render((Entity) null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		} else if (tile.hasLeft && tile.hasRight) {
			model2.render((Entity) null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		} else if (tile.hasLeft) {
			modelLeft2.render((Entity) null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		} else if (tile.hasRight) {
			modelRight2.render((Entity) null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		} else {
			model2.render((Entity) null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		}
		GlStateManager.popMatrix();
	}
}

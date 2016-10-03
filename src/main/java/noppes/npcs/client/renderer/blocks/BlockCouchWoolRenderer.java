package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
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
	private final ModelBase model = new ModelCouchMiddle();
	private final ModelBase model2 = new ModelCouchMiddleWool();
	private final ModelBase modelLeft = new ModelCouchLeft();
	private final ModelBase modelLeft2 = new ModelCouchLeftWool();
	private final ModelBase modelRight = new ModelCouchRight();
	private final ModelBase modelRight2 = new ModelCouchRightWool();
	private final ModelBase modelCorner = new ModelCouchCorner();
	private final ModelBase modelCorner2 = new ModelCouchCornerWool();

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8,
			int blockDamage) {
		TileCouchWool tile = (TileCouchWool) var1;
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) var2 + 0.5F, (float) var4 + 1.5F, (float) var6 + 0.5F);

		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(90 * tile.rotation, 0.0F, 1.0F, 0.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F);

		setWoodTexture(var1.getBlockMetadata());
		if (tile.hasCornerLeft) {
			modelCorner.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		} else if (tile.hasCornerRight) {
			GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
			modelCorner.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		} else if ((tile.hasLeft) && (tile.hasRight)) {
			model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		} else if (tile.hasLeft) {
			modelLeft.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		} else if (tile.hasRight) {
			modelRight.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		} else {
			model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		}
		bindTexture(BlockTallLampRenderer.resourceTop);
		float[] color = BlockRendererInterface.colorTable[tile.color];
		GlStateManager.color(color[0], color[1], color[2]);
		if ((tile.hasCornerLeft) || (tile.hasCornerRight)) {
			modelCorner2.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		} else if ((tile.hasLeft) && (tile.hasRight)) {
			model2.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		} else if (tile.hasLeft) {
			modelLeft2.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		} else if (tile.hasRight) {
			modelRight2.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		} else {
			model2.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		}
		GlStateManager.popMatrix();
	}
}

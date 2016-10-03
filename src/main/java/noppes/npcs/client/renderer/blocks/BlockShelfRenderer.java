package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.blocks.tiles.TileShelf;
import noppes.npcs.client.model.blocks.ModelShelf;

public class BlockShelfRenderer extends BlockRendererInterface {
	private final ModelShelf model = new ModelShelf();

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8,
			int blockDamage) {
		TileColorable tile = (TileColorable) var1;
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) var2 + 0.5F, (float) var4 + 1.5F, (float) var6 + 0.5F);

		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(90 * tile.rotation, 0.0F, 1.0F, 0.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F);

		boolean drawLeft = true;
		boolean drawRight = true;

		BlockPos pos = var1.getPos();
		if (pos != BlockPos.ORIGIN) {
			if (tile.rotation == 3) {
				drawLeft = shouldDraw(var1.getWorld(), pos.south(), 3);
				drawRight = shouldDraw(var1.getWorld(), pos.north(), 3);
			} else if (tile.rotation == 1) {
				drawLeft = shouldDraw(var1.getWorld(), pos.north(), 1);
				drawRight = shouldDraw(var1.getWorld(), pos.south(), 1);
			} else if (tile.rotation == 0) {
				drawLeft = shouldDraw(var1.getWorld(), pos.east(), 0);
				drawRight = shouldDraw(var1.getWorld(), pos.west(), 0);
			} else if (tile.rotation == 2) {
				drawLeft = shouldDraw(var1.getWorld(), pos.west(), 2);
				drawRight = shouldDraw(var1.getWorld(), pos.east(), 2);
			}
		}
		model.SupportLeft1.showModel = (model.SupportLeft2.showModel = drawLeft);
		model.SupportRight1.showModel = (model.SupportRight2.showModel = drawRight);

		setWoodTexture(var1.getBlockMetadata());
		model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

		GlStateManager.popMatrix();
	}

	private boolean shouldDraw(World world, BlockPos pos, int rotation) {
		TileEntity tile = world.getTileEntity(pos);
		if ((tile == null) || (!(tile instanceof TileShelf))) {
			return true;
		}
		return ((TileShelf) tile).rotation != rotation;
	}
}

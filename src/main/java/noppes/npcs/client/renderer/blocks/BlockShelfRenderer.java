//

//

package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.blocks.tiles.TileShelf;
import noppes.npcs.client.model.blocks.ModelShelf;

public class BlockShelfRenderer extends BlockRendererInterface {
	private final ModelShelf model;

	public BlockShelfRenderer() {
		model = new ModelShelf();
	}

	@Override
	public void renderTileEntityAt(final TileEntity var1, final double var2, final double var4, final double var6,
			final float var8, final int blockDamage) {
		final TileColorable tile = (TileColorable) var1;
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) var2 + 0.5f, (float) var4 + 1.5f, (float) var6 + 0.5f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(90 * tile.rotation, 0.0f, 1.0f, 0.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		boolean drawLeft = true;
		boolean drawRight = true;
		final BlockPos pos = var1.getPos();
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
		final ModelRenderer supportLeft1 = model.SupportLeft1;
		final ModelRenderer supportLeft2 = model.SupportLeft2;
		final boolean b = drawLeft;
		supportLeft2.showModel = b;
		supportLeft1.showModel = b;
		final ModelRenderer supportRight1 = model.SupportRight1;
		final ModelRenderer supportRight2 = model.SupportRight2;
		final boolean b2 = drawRight;
		supportRight2.showModel = b2;
		supportRight1.showModel = b2;
		setWoodTexture(var1.getBlockMetadata());
		model.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		GlStateManager.popMatrix();
	}

	private boolean shouldDraw(final World world, final BlockPos pos, final int rotation) {
		final TileEntity tile = world.getTileEntity(pos);
		return (tile == null) || !(tile instanceof TileShelf) || (((TileShelf) tile).rotation != rotation);
	}
}

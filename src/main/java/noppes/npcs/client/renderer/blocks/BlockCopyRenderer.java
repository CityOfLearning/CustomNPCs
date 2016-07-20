//

//

package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import noppes.npcs.CustomItems;
import noppes.npcs.Schematic;
import noppes.npcs.blocks.tiles.TileCopy;

public class BlockCopyRenderer extends BlockRendererInterface {
	private static ItemStack item;
	public static Schematic schematic;
	public static BlockPos pos;

	static {
		item = new ItemStack(CustomItems.copy);
		BlockCopyRenderer.schematic = null;
		BlockCopyRenderer.pos = null;
	}

	public void drawSelectionBox(BlockPos pos) {
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		AxisAlignedBB bb = new AxisAlignedBB(BlockPos.ORIGIN, pos);
		GlStateManager.translate(0.001f, 0.001f, 0.001f);
		RenderGlobal.drawOutlinedBoundingBox(bb, 255, 0, 0, 255);
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GlStateManager.disableBlend();
	}

	@Override
	public void renderTileEntityAt(TileEntity var1, double x, double y, double z, float var8, int blockDamage) {
		TileCopy tile = (TileCopy) var1;
		GlStateManager.pushMatrix();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.disableBlend();
		GlStateManager.translate(x, y, z);
		drawSelectionBox(new BlockPos(tile.width, tile.height, tile.length));
		GlStateManager.translate(0.5f, 0.5f, 0.5f);
		GlStateManager.scale(2.0f, 2.0f, 2.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
		Minecraft.getMinecraft().getRenderItem().renderItem(BlockCopyRenderer.item,
				ItemCameraTransforms.TransformType.NONE);
		GlStateManager.popMatrix();
	}
}

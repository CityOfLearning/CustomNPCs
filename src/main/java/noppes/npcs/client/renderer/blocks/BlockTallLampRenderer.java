//

//

package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.blocks.tiles.TileTallLamp;
import noppes.npcs.client.model.blocks.ModelTallLamp;
import noppes.npcs.client.model.blocks.ModelTallLampTop;

public class BlockTallLampRenderer extends BlockRendererInterface {
	public static final ResourceLocation resourceTop;
	static {
		resourceTop = new ResourceLocation("customnpcs", "textures/cache/wool_colored_white.png");
	}
	private final ModelTallLamp model;

	private final ModelTallLampTop top;

	public BlockTallLampRenderer() {
		model = new ModelTallLamp();
		top = new ModelTallLampTop();
	}

	@Override
	public void renderTileEntityAt(final TileEntity var1, final double var2, final double var4, final double var6,
			final float var8, final int blockDamage) {
		final TileTallLamp tile = (TileTallLamp) var1;
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) var2 + 0.5f, (float) var4 + 1.5f, (float) var6 + 0.5f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(90 * tile.rotation, 0.0f, 1.0f, 0.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		BlockRendererInterface.setMaterialTexture(var1.getBlockMetadata());
		model.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		bindTexture(BlockTallLampRenderer.resourceTop);
		final float[] color = BlockRendererInterface.colorTable[tile.color];
		GlStateManager.color(color[0], color[1], color[2]);
		top.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		GlStateManager.popMatrix();
	}
}

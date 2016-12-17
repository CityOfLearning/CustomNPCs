package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.blocks.tiles.TileTallLamp;
import noppes.npcs.client.model.blocks.ModelTallLamp;
import noppes.npcs.client.model.blocks.ModelTallLampTop;

public class BlockTallLampRenderer extends BlockRendererInterface {
	public static final ResourceLocation resourceTop = new ResourceLocation("customnpcs",
			"textures/cache/wool_colored_white.png");
	private final ModelTallLamp model = new ModelTallLamp();
	private final ModelTallLampTop top = new ModelTallLampTop();

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8,
			int blockDamage) {
		TileTallLamp tile = (TileTallLamp) var1;
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) var2 + 0.5F, (float) var4 + 1.5F, (float) var6 + 0.5F);

		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(90 * tile.getRotation(), 0.0F, 1.0F, 0.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F);

		setMaterialTexture(var1.getBlockMetadata());
		model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

		bindTexture(resourceTop);
		if (tile.getColor() > colorTable.length) {
			int color = tile.getColor();
			float f3 = (float) (color >> 24 & 255) / 255.0F;
			float f = (float) (color >> 16 & 255) / 255.0F;
			float f1 = (float) (color >> 8 & 255) / 255.0F;
			float f2 = (float) (color & 255) / 255.0F;
			GlStateManager.color(f, f1, f2, f3);
		} else {
			float[] color = colorTable[tile.getColor()];
			GlStateManager.color(color[0], color[1], color[2]);
		}
		top.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

		GlStateManager.popMatrix();
	}
}

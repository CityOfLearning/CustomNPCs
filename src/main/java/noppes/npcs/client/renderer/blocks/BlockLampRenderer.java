//

//

package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.blocks.tiles.TileLamp;
import noppes.npcs.client.model.blocks.ModelLamp;
import noppes.npcs.client.model.blocks.ModelLampCeiling;
import noppes.npcs.client.model.blocks.ModelLampWall;

public class BlockLampRenderer extends TileEntitySpecialRenderer {
	private static final ResourceLocation resource1;
	static {
		resource1 = new ResourceLocation("customnpcs", "textures/models/Lamp.png");
	}
	private final ModelLamp model;
	private final ModelLampCeiling model2;

	private final ModelLampWall model3;

	public BlockLampRenderer() {
		model = new ModelLamp();
		model2 = new ModelLampCeiling();
		model3 = new ModelLampWall();
	}

	@Override
	public void renderTileEntityAt(final TileEntity var1, final double var2, final double var4, final double var6,
			final float var8, final int blockDamage) {
		final TileLamp tile = (TileLamp) var1;
		GlStateManager.enableLighting();
		GlStateManager.pushMatrix();
		GlStateManager.disableBlend();
		GlStateManager.translate((float) var2 + 0.5f, (float) var4 + 1.5f, (float) var6 + 0.5f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(45 * tile.rotation, 0.0f, 1.0f, 0.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(BlockLampRenderer.resource1);
		if (tile.color == 0) {
			model.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		} else if (tile.color == 1) {
			model2.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		} else {
			model3.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		}
		GlStateManager.popMatrix();
	}
}

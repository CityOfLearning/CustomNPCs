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
	private static final ResourceLocation resource1 = new ResourceLocation("customnpcs", "textures/models/Lamp.png");
	private final ModelLamp model = new ModelLamp();
	private final ModelLampCeiling model2 = new ModelLampCeiling();
	private final ModelLampWall model3 = new ModelLampWall();

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8,
			int blockDamage) {
		TileLamp tile = (TileLamp) var1;
		GlStateManager.enableLighting();
		GlStateManager.pushMatrix();
		GlStateManager.disableBlend();
		GlStateManager.translate((float) var2 + 0.5F, (float) var4 + 1.5F, (float) var6 + 0.5F);

		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(45 * tile.rotation, 0.0F, 1.0F, 0.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F);

		Minecraft.getMinecraft().getTextureManager().bindTexture(resource1);
		if (tile.color == 0) {
			model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		} else if (tile.color == 1) {
			model2.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		} else {
			model3.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		}
		GlStateManager.popMatrix();
	}
}

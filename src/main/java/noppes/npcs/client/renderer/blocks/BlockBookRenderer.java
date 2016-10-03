package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.client.model.blocks.ModelInk;

public class BlockBookRenderer extends BlockRendererInterface {
	private final ModelInk ink = new ModelInk();
	private final ResourceLocation resource = new ResourceLocation("textures/entity/enchanting_table_book.png");
	private final ResourceLocation resource2 = new ResourceLocation("customnpcs:textures/models/Ink.png");
	private final ModelBook book = new ModelBook();

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8,
			int blockDamage) {
		TileColorable tile = (TileColorable) var1;
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) var2 + 0.5F, (float) var4 + 1.5F, (float) var6 + 0.5F);

		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate((90 * tile.rotation) - 90, 0.0F, 1.0F, 0.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F);

		TextureManager manager = Minecraft.getMinecraft().getTextureManager();
		manager.bindTexture(resource2);
		if (!playerTooFar(tile)) {
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 771);
		}
		ink.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		manager.bindTexture(resource);
		GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.translate(-1.49F, -0.18F, 0.0F);
		book.render(null, 0.0F, 0.0F, 1.0F, 1.24F, 1.0F, 0.0625F);
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}
}

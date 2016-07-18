//

//

package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.client.model.blocks.ModelInk;

public class BlockBookRenderer extends BlockRendererInterface {
	private final ModelInk ink;
	private final ResourceLocation resource;
	private final ResourceLocation resource2;
	private final ModelBook book;

	public BlockBookRenderer() {
		ink = new ModelInk();
		resource = new ResourceLocation("textures/entity/enchanting_table_book.png");
		resource2 = new ResourceLocation("customnpcs:textures/models/Ink.png");
		book = new ModelBook();
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
		GlStateManager.rotate((90 * tile.rotation) - 90, 0.0f, 1.0f, 0.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		final TextureManager manager = Minecraft.getMinecraft().getTextureManager();
		manager.bindTexture(resource2);
		if (!playerTooFar(tile)) {
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 771);
		}
		ink.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		manager.bindTexture(resource);
		GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.translate(-1.49f, -0.18f, 0.0f);
		book.render((Entity) null, 0.0f, 0.0f, 1.0f, 1.24f, 1.0f, 0.0625f);
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}
}

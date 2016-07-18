//

//

package noppes.npcs.client.renderer.blocks;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.blocks.tiles.TileBigSign;
import noppes.npcs.client.TextBlockClient;
import noppes.npcs.client.model.blocks.ModelBigSign;

public class BlockBigSignRenderer extends BlockRendererInterface {
	private static final ResourceLocation resource;
	static {
		resource = new ResourceLocation("customnpcs:textures/models/BigSign.png");
	}

	private final ModelBigSign model;

	public BlockBigSignRenderer() {
		model = new ModelBigSign();
	}

	@Override
	public void renderTileEntityAt(final TileEntity var1, final double var2, final double var4, final double var6,
			final float var8, final int blockDamage) {
		final TileBigSign tile = (TileBigSign) var1;
		final Minecraft mc = Minecraft.getMinecraft();
		GlStateManager.enableLighting();
		if ((tile.block == null) || tile.hasChanged) {
			tile.block = new TextBlockClient(tile.getText(), 112, true, new Object[] { mc.thePlayer });
			tile.hasChanged = false;
		}
		GlStateManager.pushMatrix();
		GlStateManager.disableBlend();
		float xOffset = 0.0f;
		float yOffset = 0.0f;
		if (tile.rotation == 1) {
			xOffset = -0.44f;
		} else if (tile.rotation == 3) {
			xOffset = 0.44f;
		} else if (tile.rotation == 2) {
			yOffset = -0.44f;
		} else if (tile.rotation == 0) {
			yOffset = 0.44f;
		}
		GlStateManager.translate((float) var2 + 0.5f + xOffset, (float) var4 + 0.5f, (float) var6 + 0.5f + yOffset);
		final float f1 = 0.6666667f;
		GlStateManager.rotate(90 * tile.rotation, 0.0f, 1.0f, 0.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		if ((tile.rotation % 2) == 0) {
			GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
		}
		GlStateManager.pushMatrix();
		GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		final TextureManager manager = Minecraft.getMinecraft().getTextureManager();
		manager.bindTexture(BlockBigSignRenderer.resource);
		model.renderSign();
		GlStateManager.popMatrix();
		if (!tile.block.lines.isEmpty() && !playerTooFar(tile)) {
			final float f2 = 0.0133f * f1;
			GlStateManager.translate(0.0f, 0.5f, 0.065f);
			GlStateManager.scale(f2, -f2, f2);
			GL11.glNormal3f(0.0f, 0.0f, -1.0f * f2);
			GlStateManager.depthMask(false);
			final FontRenderer fontrenderer = getFontRenderer();
			float lineOffset = 0.0f;
			if (tile.block.lines.size() < 14) {
				lineOffset = (14.0f - tile.block.lines.size()) / 2.0f;
			}
			for (int i = 0; i < tile.block.lines.size(); ++i) {
				final String text = tile.block.lines.get(i).getFormattedText();
				fontrenderer.drawString(text, -fontrenderer.getStringWidth(text) / 2,
						(int) ((lineOffset + i) * (fontrenderer.FONT_HEIGHT - 0.3)), 0);
				if (i == 12) {
					break;
				}
			}
			GlStateManager.depthMask(true);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		}
		GlStateManager.popMatrix();
	}
}

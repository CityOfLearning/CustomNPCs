//

//

package noppes.npcs.client.renderer.blocks;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.blocks.tiles.TileTombstone;
import noppes.npcs.client.TextBlockClient;
import noppes.npcs.client.model.blocks.ModelTombstone1;
import noppes.npcs.client.model.blocks.ModelTombstone2;
import noppes.npcs.client.model.blocks.ModelTombstone3;

public class BlockTombstoneRenderer extends BlockRendererInterface {
	private final ModelTombstone1 model;
	private final ModelTombstone2 model2;
	private final ModelTombstone3 model3;

	public BlockTombstoneRenderer() {
		model = new ModelTombstone1();
		model2 = new ModelTombstone2();
		model3 = new ModelTombstone3();
	}

	private void renderText(final TileTombstone tile, final int meta) {
		if ((tile.block == null) || tile.hasChanged) {
			tile.block = new TextBlockClient(tile.getText(), 94, true,
					new Object[] { Minecraft.getMinecraft().thePlayer });
			tile.hasChanged = false;
		}
		if (!tile.block.lines.isEmpty()) {
			GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
			final float f3 = 0.00665f;
			GlStateManager.translate(0.0f, -0.64f, (meta == 0) ? 0.095f : 0.126f);
			GlStateManager.scale(f3, -f3, f3);
			GL11.glNormal3f(0.0f, 0.0f, -1.0f * f3);
			GlStateManager.depthMask(false);
			final FontRenderer fontrenderer = getFontRenderer();
			float lineOffset = 0.0f;
			if (tile.block.lines.size() < 11) {
				lineOffset = (11.0f - tile.block.lines.size()) / 2.0f;
			}
			for (int i = 0; i < tile.block.lines.size(); ++i) {
				final String text = tile.block.lines.get(i).getFormattedText();
				fontrenderer.drawString(text, -fontrenderer.getStringWidth(text) / 2,
						(int) ((lineOffset + i) * (fontrenderer.FONT_HEIGHT - 0.3)), 16777215);
				if (i == 13) {
					break;
				}
			}
			GlStateManager.depthMask(true);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		}
	}

	@Override
	public void renderTileEntityAt(final TileEntity var1, final double var2, final double var4, final double var6,
			final float var8, final int blockDamage) {
		final TileTombstone tile = (TileTombstone) var1;
		final int meta = tile.getBlockMetadata();
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) var2 + 0.5f, (float) var4 + 1.5f, (float) var6 + 0.5f);
		if (meta == 2) {
			GlStateManager.scale(1.0f, 1.0f, 1.14f);
		}
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(90 * tile.rotation, 0.0f, 1.0f, 0.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(BlockRendererInterface.Stone);
		if (meta == 0) {
			model.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		} else if (meta == 1) {
			model2.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		} else {
			model3.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		}
		if ((meta < 2) && !playerTooFar(tile)) {
			renderText(tile, meta);
		}
		GlStateManager.popMatrix();
	}
}

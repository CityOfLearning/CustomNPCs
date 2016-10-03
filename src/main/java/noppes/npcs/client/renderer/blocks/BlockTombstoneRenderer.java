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
	private final ModelTombstone1 model = new ModelTombstone1();
	private final ModelTombstone2 model2 = new ModelTombstone2();
	private final ModelTombstone3 model3 = new ModelTombstone3();

	private void renderText(TileTombstone tile, int meta) {
		if ((tile.block == null) || (tile.hasChanged)) {
			tile.block = new TextBlockClient(tile.getText(), 94, true,
					new Object[] { Minecraft.getMinecraft().thePlayer });
			tile.hasChanged = false;
		}
		if (!tile.block.lines.isEmpty()) {
			GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
			float f3 = 0.00665F;
			GlStateManager.translate(0.0F, -0.64F, meta == 0 ? 0.095F : 0.126F);
			GlStateManager.scale(f3, -f3, f3);
			GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
			GlStateManager.depthMask(false);
			FontRenderer fontrenderer = getFontRenderer();

			float lineOffset = 0.0F;
			if (tile.block.lines.size() < 11) {
				lineOffset = (11.0F - tile.block.lines.size()) / 2.0F;
			}
			for (int i = 0; i < tile.block.lines.size(); i++) {
				String text = tile.block.lines.get(i).getFormattedText();
				fontrenderer.drawString(text, -fontrenderer.getStringWidth(text) / 2,
						(int) ((lineOffset + i) * (fontrenderer.FONT_HEIGHT - 0.3D)), 16777215);
				if (i == 13) {
					break;
				}
			}
			GlStateManager.depthMask(true);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8,
			int blockDamage) {
		TileTombstone tile = (TileTombstone) var1;
		int meta = tile.getBlockMetadata();
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) var2 + 0.5F, (float) var4 + 1.5F, (float) var6 + 0.5F);
		if (meta == 2) {
			GlStateManager.scale(1.0F, 1.0F, 1.14F);
		}
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(90 * tile.rotation, 0.0F, 1.0F, 0.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F);

		Minecraft.getMinecraft().getTextureManager().bindTexture(Stone);
		if (meta == 0) {
			model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		} else if (meta == 1) {
			model2.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		} else {
			model3.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		}
		if ((meta < 2) && (!playerTooFar(tile))) {
			renderText(tile, meta);
		}
		GlStateManager.popMatrix();
	}
}

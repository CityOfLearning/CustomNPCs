//

//

package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.blocks.tiles.TileSign;
import noppes.npcs.client.model.blocks.ModelSign;

public class BlockSignRenderer extends BlockRendererInterface {
	private final ModelSign model;

	public BlockSignRenderer() {
		model = new ModelSign();
	}

	public void doRender(final double par2, final double par4, final double par6, final int meta,
			final ItemStack iicon) {
		if (iicon.getItem() instanceof ItemBlock) {
			return;
		}
		GlStateManager.pushMatrix();
		bindTexture(TextureMap.locationBlocksTexture);
		GlStateManager.translate(0.0, 1.0199999809265137, -3.57);
		GlStateManager.depthMask(false);
		final float f2 = 0.024f;
		final Minecraft mc = Minecraft.getMinecraft();
		GlStateManager.scale(-f2, f2, f2);
		mc.getRenderItem().renderItemAndEffectIntoGUI(iicon, -8, -8);
		GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
		GlStateManager.translate(0.0, 0.0, (-3.57 / f2) * 2.0);
		mc.getRenderItem().renderItemAndEffectIntoGUI(iicon, -8, -8);
		GlStateManager.depthMask(true);
		GlStateManager.popMatrix();
	}

	@Override
	public void renderTileEntityAt(final TileEntity var1, final double var2, final double var4, final double var6,
			final float var8, final int blockDamage) {
		final TileSign tile = (TileSign) var1;
		GlStateManager.enableRescaleNormal();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) var2 + 0.5f, (float) var4 + 1.62f, (float) var6 + 0.5f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate((90 * tile.rotation) + 90, 0.0f, 1.0f, 0.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(BlockRendererInterface.Steel);
		model.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		setWoodTexture(tile.getBlockMetadata());
		model.Sign.render(0.0625f);
		if ((tile.icon != null) && !playerTooFar(tile)) {
			doRender(var2, var4, var6, tile.rotation, tile.icon);
		}
		GlStateManager.popMatrix();
	}
}

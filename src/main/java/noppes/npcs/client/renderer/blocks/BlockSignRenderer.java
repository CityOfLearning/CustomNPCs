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
	private final ModelSign model = new ModelSign();

	public void doRender(double par2, double par4, double par6, int meta, ItemStack iicon) {
		if ((iicon.getItem() instanceof ItemBlock)) {
			return;
		}
		GlStateManager.pushMatrix();
		bindTexture(TextureMap.locationBlocksTexture);
		GlStateManager.translate(0.0D, 1.0199999809265137D, -3.57D);
		GlStateManager.depthMask(false);
		float f2 = 0.024F;
		Minecraft mc = Minecraft.getMinecraft();
		GlStateManager.scale(-f2, f2, f2);
		mc.getRenderItem().renderItemAndEffectIntoGUI(iicon, -8, -8);
		GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(0.0D, 0.0D, (-3.57D / f2) * 2.0D);
		mc.getRenderItem().renderItemAndEffectIntoGUI(iicon, -8, -8);
		GlStateManager.depthMask(true);
		GlStateManager.popMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8,
			int blockDamage) {
		TileSign tile = (TileSign) var1;
		GlStateManager.enableRescaleNormal();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) var2 + 0.5F, (float) var4 + 1.62F, (float) var6 + 0.5F);

		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate((90 * tile.rotation) + 90, 0.0F, 1.0F, 0.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F);

		Minecraft.getMinecraft().getTextureManager().bindTexture(Steel);
		model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

		setWoodTexture(tile.getBlockMetadata());
		model.Sign.render(0.0625F);
		if ((tile.icon != null) && (!playerTooFar(tile))) {
			doRender(var2, var4, var6, tile.rotation, tile.icon);
		}
		GlStateManager.popMatrix();
	}
}

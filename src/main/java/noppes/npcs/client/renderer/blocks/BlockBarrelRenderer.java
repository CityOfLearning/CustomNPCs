//

//

package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.client.model.blocks.ModelBarrel;
import noppes.npcs.client.model.blocks.ModelBarrelLit;

public class BlockBarrelRenderer extends BlockRendererInterface {
	private static final ResourceLocation resource1;
	static {
		resource1 = new ResourceLocation("customnpcs", "textures/models/Barrel.png");
	}
	private final ModelBarrel model;

	private final ModelBarrelLit modelLit;

	public BlockBarrelRenderer() {
		model = new ModelBarrel();
		modelLit = new ModelBarrelLit();
	}

	@Override
	public void renderTileEntityAt(final TileEntity var1, final double var2, final double var4, final double var6,
			final float var8, final int blockDamage) {
		final TileColorable tile = (TileColorable) var1;
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) var2 + 0.5f, (float) var4 + 1.42f, (float) var6 + 0.5f);
		GlStateManager.scale(1.0f, 0.94f, 1.0f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(45 * tile.rotation, 0.0f, 1.0f, 0.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		GlStateManager.enableCull();
		setWoodTexture(var1.getBlockMetadata());
		model.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(BlockBarrelRenderer.resource1);
		modelLit.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		GlStateManager.popMatrix();
	}
}

package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.client.model.blocks.ModelBarrel;
import noppes.npcs.client.model.blocks.ModelBarrelLit;

public class BlockBarrelRenderer extends BlockRendererInterface {
	private static final ResourceLocation resource1 = new ResourceLocation("customnpcs", "textures/models/Barrel.png");
	private final ModelBarrel model = new ModelBarrel();
	private final ModelBarrelLit modelLit = new ModelBarrelLit();

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8,
			int blockDamage) {
		TileColorable tile = (TileColorable) var1;
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) var2 + 0.5F, (float) var4 + 1.42F, (float) var6 + 0.5F);
		GlStateManager.scale(1.0F, 0.94F, 1.0F);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(45 * tile.getRotation(), 0.0F, 1.0F, 0.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F);

		GlStateManager.enableCull();
		setWoodTexture(var1.getBlockMetadata());
		model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(resource1);
		modelLit.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

		GlStateManager.popMatrix();
	}
}

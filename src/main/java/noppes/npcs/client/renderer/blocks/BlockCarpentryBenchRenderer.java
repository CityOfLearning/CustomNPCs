//

//

package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.model.blocks.ModelAnvil;
import noppes.npcs.client.model.blocks.ModelCarpentryBench;

public class BlockCarpentryBenchRenderer extends TileEntitySpecialRenderer {
	private static ResourceLocation resource3;
	private static ResourceLocation field_110631_g;
	static {
		resource3 = new ResourceLocation("customnpcs", "textures/models/Steel.png");
		field_110631_g = new ResourceLocation("customnpcs", "textures/models/CarpentryBench.png");
	}
	private ModelCarpentryBench model;

	private ModelAnvil anvil;

	public BlockCarpentryBenchRenderer() {
		model = new ModelCarpentryBench();
		anvil = new ModelAnvil();
	}

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8,
			int blockDamage) {
		int rotation = 0;
		boolean anvilModel = var1.getBlockMetadata() == 1;
		if (var1.getPos() != BlockPos.ORIGIN) {
			rotation = var1.getBlockMetadata() % 4;
			anvilModel = (var1.getBlockMetadata() >= 4);
		}
		GlStateManager.pushMatrix();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.translate((float) var2 + 0.5f, (float) var4 + 1.4f, (float) var6 + 0.5f);
		GlStateManager.scale(0.95f, 0.95f, 0.95f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(90 * rotation, 0.0f, 1.0f, 0.0f);
		if (anvilModel) {
			bindTexture(BlockCarpentryBenchRenderer.resource3);
			anvil.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		} else {
			bindTexture(BlockCarpentryBenchRenderer.field_110631_g);
			model.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		}
		GlStateManager.popMatrix();
	}
}

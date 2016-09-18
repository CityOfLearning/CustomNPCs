
package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.model.blocks.ModelMailboxUS;
import noppes.npcs.client.model.blocks.ModelMailboxWow;

public class BlockMailboxRenderer extends TileEntitySpecialRenderer {
	private static ResourceLocation text1;
	private static ResourceLocation text2;
	static {
		text1 = new ResourceLocation("customnpcs", "textures/models/mailbox1.png");
		text2 = new ResourceLocation("customnpcs", "textures/models/mailbox2.png");
	}
	private ModelMailboxUS model;

	private ModelMailboxWow model2;

	public BlockMailboxRenderer() {
		model = new ModelMailboxUS();
		model2 = new ModelMailboxWow();
	}

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8,
			int blockDamage) {
		int meta = 0;
		int type = var1.getBlockMetadata();
		if (var1.getPos() != BlockPos.ORIGIN) {
			meta = (var1.getBlockMetadata() | 0x4);
			type = var1.getBlockMetadata() >> 2;
		}
		GlStateManager.pushMatrix();
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.translate((float) var2 + 0.5f, (float) var4 + 1.5f, (float) var6 + 0.5f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(90 * meta, 0.0f, 1.0f, 0.0f);
		if (type == 0) {
			bindTexture(BlockMailboxRenderer.text1);
			model.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		}
		if (type == 1) {
			bindTexture(BlockMailboxRenderer.text2);
			model2.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		}
		GlStateManager.popMatrix();
	}
}

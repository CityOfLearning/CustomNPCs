package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.client.model.blocks.ModelTable;

public class BlockTableRenderer extends BlockRendererInterface {
	private static final ResourceLocation resource1 = new ResourceLocation("customnpcs",
			"textures/cache/planks_oak.png");
	private static final ResourceLocation resource2 = new ResourceLocation("customnpcs",
			"textures/cache/planks_big_oak.png");
	private static final ResourceLocation resource3 = new ResourceLocation("customnpcs",
			"textures/cache/planks_spruce.png");
	private static final ResourceLocation resource4 = new ResourceLocation("customnpcs",
			"textures/cache/planks_birch.png");
	private static final ResourceLocation resource5 = new ResourceLocation("customnpcs",
			"textures/cache/planks_acacia.png");
	private static final ResourceLocation resource6 = new ResourceLocation("customnpcs",
			"textures/cache/planks_jungle.png");
	private final ModelTable model = new ModelTable();

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8,
			int blockDamage) {
		TileColorable tile = (TileColorable) var1;
		GlStateManager.pushMatrix();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.translate((float) var2 + 0.5F, (float) var4 + 1.5F, (float) var6 + 0.5F);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F);

		BlockPos pos = var1.getPos();
		if (pos != BlockPos.ORIGIN) {
			boolean south = var1.getWorld().getBlockState(pos.east()).getBlock() == CustomItems.table;
			boolean north = var1.getWorld().getBlockState(pos.west()).getBlock() == CustomItems.table;
			boolean east = var1.getWorld().getBlockState(pos.south()).getBlock() == CustomItems.table;
			boolean west = var1.getWorld().getBlockState(pos.north()).getBlock() == CustomItems.table;

			model.Shape1.showModel = ((!south) && (!east));
			model.Shape3.showModel = ((!north) && (!west));
			model.Shape4.showModel = ((!north) && (!east));
			model.Shape5.showModel = ((!south) && (!west));
		} else {
			model.Shape1.showModel = (model.Shape3.showModel = model.Shape4.showModel = model.Shape5.showModel = true);
		}
		setWoodTexture(var1.getBlockMetadata());
		model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.rotate(90 * tile.getRotation(), 0.0F, 1.0F, 0.0F);
		model.Table.render(0.0625F);

		GlStateManager.popMatrix();
	}
}

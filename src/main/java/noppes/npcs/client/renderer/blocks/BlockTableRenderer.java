//

//

package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.client.model.blocks.ModelTable;

public class BlockTableRenderer extends BlockRendererInterface {
	private static final ResourceLocation resource1;
	private static final ResourceLocation resource2;
	private static final ResourceLocation resource3;
	private static final ResourceLocation resource4;
	private static final ResourceLocation resource5;
	private static final ResourceLocation resource6;
	static {
		resource1 = new ResourceLocation("customnpcs", "textures/cache/planks_oak.png");
		resource2 = new ResourceLocation("customnpcs", "textures/cache/planks_big_oak.png");
		resource3 = new ResourceLocation("customnpcs", "textures/cache/planks_spruce.png");
		resource4 = new ResourceLocation("customnpcs", "textures/cache/planks_birch.png");
		resource5 = new ResourceLocation("customnpcs", "textures/cache/planks_acacia.png");
		resource6 = new ResourceLocation("customnpcs", "textures/cache/planks_jungle.png");
	}

	private final ModelTable model;

	public BlockTableRenderer() {
		model = new ModelTable();
	}

	@Override
	public void renderTileEntityAt(final TileEntity var1, final double var2, final double var4, final double var6,
			final float var8, final int blockDamage) {
		final TileColorable tile = (TileColorable) var1;
		GlStateManager.pushMatrix();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.translate((float) var2 + 0.5f, (float) var4 + 1.5f, (float) var6 + 0.5f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		final BlockPos pos = var1.getPos();
		if (pos != BlockPos.ORIGIN) {
			final boolean south = var1.getWorld().getBlockState(pos.east()).getBlock() == CustomItems.table;
			final boolean north = var1.getWorld().getBlockState(pos.west()).getBlock() == CustomItems.table;
			final boolean east = var1.getWorld().getBlockState(pos.south()).getBlock() == CustomItems.table;
			final boolean west = var1.getWorld().getBlockState(pos.north()).getBlock() == CustomItems.table;
			model.Shape1.showModel = (!south && !east);
			model.Shape3.showModel = (!north && !west);
			model.Shape4.showModel = (!north && !east);
			model.Shape5.showModel = (!south && !west);
		} else {
			final ModelRenderer shape1 = model.Shape1;
			final ModelRenderer shape2 = model.Shape3;
			final ModelRenderer shape3 = model.Shape4;
			final ModelRenderer shape4 = model.Shape5;
			final boolean b = true;
			shape4.showModel = b;
			shape3.showModel = b;
			shape2.showModel = b;
			shape1.showModel = b;
		}
		setWoodTexture(var1.getBlockMetadata());
		model.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		GlStateManager.rotate(90 * tile.rotation, 0.0f, 1.0f, 0.0f);
		model.Table.render(0.0625f);
		GlStateManager.popMatrix();
	}
}

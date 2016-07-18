//

//

package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.blocks.tiles.TilePedestal;
import noppes.npcs.client.model.blocks.ModelPedestal;

public class BlockPedestalRenderer extends BlockRendererInterface {
	private static final ResourceLocation resource;
	static {
		resource = new ResourceLocation("customnpcs:textures/models/npcPedestal.png");
	}

	private final ModelPedestal model;

	public BlockPedestalRenderer() {
		model = new ModelPedestal();
	}

	private void doRender(final ItemStack itemstack) {
		if ((itemstack == null) || (itemstack.getItem() == null) || (itemstack.getItem() instanceof ItemBlock)) {
			return;
		}
		final Item item = itemstack.getItem();
		GlStateManager.translate(0.0, 0.6, 0.0);
		GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
		if (item instanceof ItemSword) {
			GlStateManager.rotate(180.0f, -1.0f, 0.0f, 0.0f);
		}
		final Minecraft minecraft = Minecraft.getMinecraft();
		final IBakedModel model = minecraft.getRenderItem().getItemModelMesher().getItemModel(itemstack);
		final ItemTransformVec3f p_175034_1_ = model.getItemCameraTransforms().thirdPerson;
		GlStateManager.scale(p_175034_1_.scale.x + ItemCameraTransforms.field_181696_h,
				p_175034_1_.scale.y + ItemCameraTransforms.field_181697_i,
				p_175034_1_.scale.z + ItemCameraTransforms.field_181698_j);
		GlStateManager.rotate(45.0f, 0.0f, 0.0f, 1.0f);
		minecraft.getRenderItem().renderItem(itemstack, ItemCameraTransforms.TransformType.NONE);
	}

	@Override
	public void renderTileEntityAt(final TileEntity var1, final double var2, final double var4, final double var6,
			final float var8, final int blockDamage) {
		final TilePedestal tile = (TilePedestal) var1;
		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.pushMatrix();
		GlStateManager.enableLighting();
		GlStateManager.translate((float) var2 + 0.5f, (float) var4 + 1.5f, (float) var6 + 0.5f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(90 * tile.rotation, 0.0f, 1.0f, 0.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		BlockRendererInterface.setMaterialTexture(var1.getBlockMetadata());
		model.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		GlStateManager.scale(1.0f, 0.99f, 1.0f);
		final TextureManager manager = Minecraft.getMinecraft().getTextureManager();
		manager.bindTexture(BlockPedestalRenderer.resource);
		model.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		if (!playerTooFar(tile)) {
			doRender(tile.getStackInSlot(0));
		}
		GlStateManager.popMatrix();
		GlStateManager.color(1.0f, 1.0f, 1.0f);
	}

	@Override
	public int specialRenderDistance() {
		return 40;
	}
}

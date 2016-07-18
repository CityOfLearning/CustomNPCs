//

//

package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.blocks.tiles.TileWeaponRack;
import noppes.npcs.client.model.blocks.ModelWeaponRack;

public class BlockWeaponRackRenderer extends BlockRendererInterface {
	private final ModelWeaponRack model;

	public BlockWeaponRackRenderer() {
		model = new ModelWeaponRack();
	}

	private void doRender(final ItemStack itemstack, final int pos) {
		if ((itemstack == null) || (itemstack.getItem() == null) || (itemstack.getItem() instanceof ItemBlock)) {
			return;
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(-0.37f + (pos * 0.37f), 0.6f, 0.33f);
		final Minecraft minecraft = Minecraft.getMinecraft();
		final IBakedModel model = minecraft.getRenderItem().getItemModelMesher().getItemModel(itemstack);
		final ItemTransformVec3f p_175034_1_ = model.getItemCameraTransforms().thirdPerson;
		itemstack.getItem();
		GlStateManager.rotate(p_175034_1_.rotation.x, 0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
		GlStateManager.scale(p_175034_1_.scale.x + ItemCameraTransforms.field_181696_h,
				p_175034_1_.scale.y + ItemCameraTransforms.field_181697_i,
				p_175034_1_.scale.z + ItemCameraTransforms.field_181698_j);
		GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(45.0f, 0.0f, 0.0f, 1.0f);
		minecraft.getRenderItem().renderItem(itemstack, ItemCameraTransforms.TransformType.NONE);
		GlStateManager.popMatrix();
	}

	@Override
	public void renderTileEntityAt(final TileEntity var1, final double var2, final double var4, final double var6,
			final float var8, final int blockDamage) {
		final TileWeaponRack tile = (TileWeaponRack) var1;
		GlStateManager.enableLighting();
		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) var2 + 0.5f, (float) var4 + 1.34f, (float) var6 + 0.5f);
		GlStateManager.scale(0.9f, 0.9f, 0.9f);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(90 * tile.rotation, 0.0f, 1.0f, 0.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		setWoodTexture(var1.getBlockMetadata());
		model.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		if (!playerTooFar(tile)) {
			for (int i = 0; i < 3; ++i) {
				doRender(tile.getStackInSlot(i), i);
			}
		}
		GlStateManager.popMatrix();
	}

	@Override
	public int specialRenderDistance() {
		return 26;
	}
}

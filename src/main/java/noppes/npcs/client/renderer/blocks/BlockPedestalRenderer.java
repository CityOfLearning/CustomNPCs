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
	private static final ResourceLocation resource = new ResourceLocation("customnpcs:textures/models/npcPedestal.png");
	private final ModelPedestal model = new ModelPedestal();

	private void doRender(ItemStack itemstack) {
		if ((itemstack == null) || (itemstack.getItem() == null) || ((itemstack.getItem() instanceof ItemBlock))) {
			return;
		}
		Item item = itemstack.getItem();
		GlStateManager.translate(0.0D, 0.6D, 0.0D);
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		if ((item instanceof ItemSword)) {
			GlStateManager.rotate(180.0F, -1.0F, 0.0F, 0.0F);
		}
		Minecraft minecraft = Minecraft.getMinecraft();
		IBakedModel model = minecraft.getRenderItem().getItemModelMesher().getItemModel(itemstack);
		ItemTransformVec3f p_175034_1_ = model.getItemCameraTransforms().thirdPerson;
		GlStateManager.scale(p_175034_1_.scale.x + ItemCameraTransforms.field_181696_h,
				p_175034_1_.scale.y + ItemCameraTransforms.field_181697_i,
				p_175034_1_.scale.z + ItemCameraTransforms.field_181698_j);

		GlStateManager.rotate(45.0F, 0.0F, 0.0F, 1.0F);
		minecraft.getRenderItem().renderItem(itemstack, ItemCameraTransforms.TransformType.NONE);
	}

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8,
			int blockDamage) {
		TilePedestal tile = (TilePedestal) var1;
		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.pushMatrix();
		GlStateManager.enableLighting();
		GlStateManager.translate((float) var2 + 0.5F, (float) var4 + 1.5F, (float) var6 + 0.5F);

		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(90 * tile.getRotation(), 0.0F, 1.0F, 0.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F);

		setMaterialTexture(var1.getBlockMetadata());
		model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.scale(1.0F, 0.99F, 1.0F);
		TextureManager manager = Minecraft.getMinecraft().getTextureManager();
		manager.bindTexture(resource);
		model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		if (!playerTooFar(tile)) {
			doRender(tile.getStackInSlot(0));
		}
		GlStateManager.popMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F);
	}

	@Override
	public int specialRenderDistance() {
		return 40;
	}
}

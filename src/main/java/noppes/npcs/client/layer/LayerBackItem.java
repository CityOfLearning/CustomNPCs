
package noppes.npcs.client.layer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import noppes.npcs.api.wrapper.ItemStackWrapper;

public class LayerBackItem extends LayerInterface {
	public LayerBackItem(RenderLiving render) {
		super(render);
	}

	@Override
	public void render(float par2, float par3, float par4, float par5, float par6, float par7) {
		Minecraft minecraft = Minecraft.getMinecraft();
		ItemStack itemstack = ItemStackWrapper.MCItem(npc.inventory.getRightHand());
		if ((itemstack == null) || npc.isAttacking()) {
			return;
		}
		Item item = itemstack.getItem();
		if (item instanceof ItemBlock) {
			return;
		}
		model.bipedBody.postRender(par7);
		GlStateManager.translate(0.0, 0.36, 0.14);
		GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
		if (item instanceof ItemSword) {
			GlStateManager.rotate(180.0f, -1.0f, 0.0f, 0.0f);
		}
		IBakedModel model = minecraft.getRenderItem().getItemModelMesher().getItemModel(itemstack);
		ItemTransformVec3f p_175034_1_ = model.getItemCameraTransforms().thirdPerson;
		GlStateManager.scale(p_175034_1_.scale.x + ItemCameraTransforms.field_181696_h,
				p_175034_1_.scale.y + ItemCameraTransforms.field_181697_i,
				p_175034_1_.scale.z + ItemCameraTransforms.field_181698_j);
		minecraft.getItemRenderer().renderItem(npc, itemstack, ItemCameraTransforms.TransformType.NONE);
	}

	@Override
	public void rotate(float par1, float par2, float par3, float par4, float par5, float par6) {
	}
}

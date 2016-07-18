//

//

package noppes.npcs.client.layer;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import noppes.npcs.client.renderer.RenderCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class LayerLeftHeldItem extends LayerHeldItem {
	private final RenderCustomNpc renderer;

	public LayerLeftHeldItem(final RenderCustomNpc renderer) {
		super(renderer);
		this.renderer = renderer;
	}

	@Override
	public void doRenderLayer(final EntityLivingBase p_177141_1_, final float p_177141_2_, final float p_177141_3_,
			final float p_177141_4_, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_,
			final float p_177141_8_) {
		final EntityNPCInterface npc = (EntityNPCInterface) p_177141_1_;
		final ItemStack offhand = npc.getOffHand();
		if (offhand == null) {
			return;
		}
		GlStateManager.pushMatrix();
		final IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(offhand);
		if (model instanceof IPerspectiveAwareModel) {
			final Pair<? extends IBakedModel, Matrix4f> pair = ((IPerspectiveAwareModel) model)
					.handlePerspective(ItemCameraTransforms.TransformType.THIRD_PERSON);
			if (pair.getRight() != null) {
				GlStateManager.translate(pair.getRight().m03 * -4.0f, 0.0f, 0.0f);
			}
		}
		final ModelRenderer right = renderer.npcmodel.bipedRightArm;
		renderer.npcmodel.bipedRightArm = renderer.npcmodel.bipedLeftArm;
		GlStateManager.translate(0.125, 0.0, 0.0);
		npc.inventory.renderOffhand = offhand;
		super.doRenderLayer(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_,
				p_177141_8_);
		renderer.npcmodel.bipedRightArm = right;
		npc.inventory.renderOffhand = null;
		GlStateManager.popMatrix();
	}
}

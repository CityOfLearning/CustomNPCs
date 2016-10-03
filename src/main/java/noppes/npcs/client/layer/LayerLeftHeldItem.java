package noppes.npcs.client.layer;

import javax.vecmath.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import noppes.npcs.client.renderer.RenderCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import org.apache.commons.lang3.tuple.Pair;

public class LayerLeftHeldItem extends LayerHeldItem {

   private final RenderCustomNpc renderer;


   public LayerLeftHeldItem(RenderCustomNpc renderer) {
      super(renderer);
      this.renderer = renderer;
   }

   public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
      EntityNPCInterface npc = (EntityNPCInterface)p_177141_1_;
      ItemStack offhand = npc.getOffHand();
      if(offhand != null) {
         GlStateManager.pushMatrix();
         IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(offhand);
         if(model instanceof IPerspectiveAwareModel) {
            Pair right = ((IPerspectiveAwareModel)model).handlePerspective(TransformType.THIRD_PERSON);
            if(right.getRight() != null) {
               GlStateManager.translate(((Matrix4f)right.getRight()).m03 * -4.0F, 0.0F, 0.0F);
            }
         }

         ModelRenderer right1 = this.renderer.npcmodel.bipedRightArm;
         this.renderer.npcmodel.bipedRightArm = this.renderer.npcmodel.bipedLeftArm;
         GlStateManager.translate(0.125D, 0.0D, 0.0D);
         npc.inventory.renderOffhand = offhand;
         super.doRenderLayer(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
         this.renderer.npcmodel.bipedRightArm = right1;
         npc.inventory.renderOffhand = null;
         GlStateManager.popMatrix();
      }
   }
}

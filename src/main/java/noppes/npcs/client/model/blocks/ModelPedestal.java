package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelPedestal extends ModelBase {

   ModelRenderer Main_Block = new ModelRenderer(this, 1, 0);
   ModelRenderer Front;


   public ModelPedestal() {
      this.Main_Block.addBox(-7.0F, 0.0F, -8.0F, 14, 3, 16);
      this.Main_Block.setRotationPoint(0.0F, 16.0F, 0.0F);
      this.Front = new ModelRenderer(this, 16, 8);
      this.Front.addBox(-8.0F, 0.0F, -8.0F, 16, 5, 16);
      this.Front.setRotationPoint(0.0F, 19.0F, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      GlStateManager.pushMatrix();
      GlStateManager.scale(1.0F, 1.0F, 0.5F);
      this.Main_Block.render(f5);
      GlStateManager.scale(1.0F, 1.0F, 1.25F);
      this.Front.render(f5);
      GlStateManager.popMatrix();
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }
}

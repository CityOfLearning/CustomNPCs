package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBannerFlag extends ModelBase {

   ModelRenderer Flag;


   public ModelBannerFlag() {
      this.textureWidth = 32;
      this.textureHeight = 32;
      this.Flag = new ModelRenderer(this, 0, 0);
      this.Flag.addBox(0.0F, 0.0F, 0.0F, 15, 27, 0);
      this.Flag.setRotationPoint(-7.5F, -7.0F, -2.0F);
      this.Flag.setTextureSize(32, 32);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.Flag.render(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }
}

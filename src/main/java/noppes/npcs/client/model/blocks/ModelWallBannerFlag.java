package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWallBannerFlag extends ModelBase {

   ModelRenderer Flag;


   public ModelWallBannerFlag() {
      this.textureWidth = 32;
      this.textureHeight = 32;
      this.Flag = new ModelRenderer(this, 0, 0);
      this.Flag.addBox(0.0F, 0.0F, 0.0F, 15, 27, 0);
      this.Flag.setRotationPoint(-7.5F, -7.0F, 4.5F);
      this.Flag.setTextureSize(32, 32);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.Flag.render(f5);
   }
}

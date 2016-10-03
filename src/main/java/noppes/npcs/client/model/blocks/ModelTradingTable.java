package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;

public class ModelTradingTable extends ModelBase {

   ModelRenderer Paper_1;
   ModelRenderer Base1;
   ModelRenderer Base2;
   ModelRenderer Pole;
   ModelRenderer Support;
   ModelRenderer Cup_1;
   ModelRenderer Cup_2;


   public ModelTradingTable() {
      this.textureWidth = 32;
      this.textureHeight = 32;
      this.Paper_1 = new ModelRenderer(this, 14, 0);
      this.Paper_1.addBox(0.0F, 0.0F, 0.0F, 3, 0, 4);
      this.Paper_1.setRotationPoint(-7.0F, 23.8F, 0.5F);
      this.Paper_1.setTextureSize(32, 32);
      this.Paper_1.mirror = true;
      this.setRotation(this.Paper_1, 0.0F, 0.1858931F, 0.0F);
      this.Base1 = new ModelRenderer(this, 0, 0);
      this.Base1.addBox(-1.0F, 0.5F, 0.0F, 1, 1, 4);
      this.Base1.setRotationPoint(5.0F, 23.0F, -4.0F);
      this.Base1.setTextureSize(32, 32);
      this.Base1.mirror = true;
      this.setRotation(this.Base1, 0.0F, 0.1919862F, 0.0F);
      this.Base2 = new ModelRenderer(this, 0, 0);
      this.Base2.addBox(-1.5F, 0.5F, 1.0F, 2, 1, 2);
      this.Base2.setRotationPoint(5.0F, 23.0F, -4.0F);
      this.Base2.setTextureSize(32, 32);
      this.Base2.mirror = true;
      this.setRotation(this.Base2, 0.0F, 0.1919862F, 0.0F);
      this.Pole = new ModelRenderer(this, 0, 0);
      this.Pole.addBox(-1.0F, -4.5F, 1.5F, 1, 5, 1);
      this.Pole.setRotationPoint(5.0F, 23.0F, -4.0F);
      this.Pole.setTextureSize(32, 32);
      this.Pole.mirror = true;
      this.setRotation(this.Pole, 0.0F, 0.1919862F, 0.0F);
      this.Support = new ModelRenderer(this, 0, 0);
      this.Support.addBox(-1.0F, -5.5F, -1.0F, 1, 1, 6);
      this.Support.setRotationPoint(5.0F, 23.0F, -4.0F);
      this.Support.setTextureSize(32, 32);
      this.Support.mirror = true;
      this.setRotation(this.Support, 0.0F, 0.1919862F, 0.0F);
      this.Cup_1 = new ModelRenderer(this, 0, 0);
      this.Cup_1.addBox(-1.5F, -3.0F, -1.5F, 2, 1, 2);
      this.Cup_1.setRotationPoint(5.0F, 23.0F, -4.0F);
      this.Cup_1.setTextureSize(32, 32);
      this.Cup_1.mirror = true;
      this.setRotation(this.Cup_1, 0.0F, 0.1919862F, 0.0F);
      this.Cup_2 = new ModelRenderer(this, 0, 0);
      this.Cup_2.addBox(-1.5F, -3.0F, 3.5F, 2, 1, 2);
      this.Cup_2.setRotationPoint(5.0F, 23.0F, -4.0F);
      this.Cup_2.setTextureSize(32, 32);
      this.Cup_2.mirror = true;
      this.setRotation(this.Cup_2, 0.0F, 0.1919862F, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.Paper_1.render(f5);
      this.Base1.render(f5);
      this.Base2.render(f5);
      this.Pole.render(f5);
      this.Support.render(f5);
      this.Cup_1.render(f5);
      this.Cup_2.render(f5);
      GlStateManager.disableTexture2D();
      GlStateManager.disableLighting();
      GlStateManager.disableCull();
      GlStateManager.disableBlend();
      AxisAlignedBB bb = new AxisAlignedBB(0.34D, 1.15D, 0.035D, 0.34D, 1.25D, 0.035D);
      RenderGlobal.drawOutlinedBoundingBox(bb, 0, 0, 0, 255);
      bb = new AxisAlignedBB(0.28D, 1.15D, -0.28D, 0.28D, 1.25D, -0.28D);
      RenderGlobal.drawOutlinedBoundingBox(bb, 0, 0, 0, 255);
      GlStateManager.enableTexture2D();
      GlStateManager.enableCull();
      GlStateManager.disableBlend();
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }
}

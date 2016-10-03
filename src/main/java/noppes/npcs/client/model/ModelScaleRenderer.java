package noppes.npcs.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import noppes.npcs.ModelPartConfig;
import noppes.npcs.constants.EnumParts;
import org.lwjgl.opengl.GL11;

public class ModelScaleRenderer extends ModelRenderer {

   public boolean compiled;
   public int displayList;
   public ModelPartConfig config;
   public EnumParts part;


   public ModelScaleRenderer(ModelBase par1ModelBase, EnumParts part) {
      super(par1ModelBase);
      this.part = part;
   }

   public ModelScaleRenderer(ModelBase par1ModelBase, int par2, int par3, EnumParts part) {
      this(par1ModelBase, part);
      this.setTextureOffset(par2, par3);
   }

   public void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void render(float par1) {
      if(this.showModel && !this.isHidden) {
         if(!this.compiled) {
            this.compileDisplayList(par1);
         }

         GlStateManager.pushMatrix();
         this.postRender(par1);
         GlStateManager.callList(this.displayList);
         if(this.childModels != null) {
            for(int i = 0; i < this.childModels.size(); ++i) {
               ((ModelRenderer)this.childModels.get(i)).render(par1);
            }
         }

         GlStateManager.popMatrix();
      }
   }

   public void postRender(float par1) {
      if(this.config != null) {
         GlStateManager.translate(this.config.transX, this.config.transY, this.config.transZ);
      }

      super.postRender(par1);
      if(this.config != null) {
         GlStateManager.scale(this.config.scaleX, this.config.scaleY, this.config.scaleZ);
      }

   }

   public void postRenderNoScale(float par1) {
      GlStateManager.translate(this.config.transX, this.config.transY, this.config.transZ);
      super.postRender(par1);
   }

   public void parentRender(float par1) {
      super.render(par1);
   }

   public void compileDisplayList(float par1) {
      this.displayList = GLAllocation.generateDisplayLists(1);
      GL11.glNewList(this.displayList, 4864);
      WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();

      for(int i = 0; i < this.cubeList.size(); ++i) {
         ((ModelBox)this.cubeList.get(i)).render(worldrenderer, par1);
      }

      GL11.glEndList();
      this.compiled = true;
   }
}

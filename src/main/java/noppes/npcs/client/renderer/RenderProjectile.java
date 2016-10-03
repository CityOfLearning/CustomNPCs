package noppes.npcs.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.entity.EntityProjectile;

@SideOnly(Side.CLIENT)
public class RenderProjectile extends Render {

   public boolean renderWithColor = true;
   private static final ResourceLocation arrowTextures = new ResourceLocation("textures/entity/arrow.png");
   private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");


   public RenderProjectile() {
      super(Minecraft.getMinecraft().getRenderManager());
   }

   public void doRenderProjectile(EntityProjectile par1EntityProjectile, double par2, double par4, double par6, float par8, float par9) {
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)par2, (float)par4, (float)par6);
      GlStateManager.enableRescaleNormal();
      float f = (float)par1EntityProjectile.getDataWatcher().getWatchableObjectInt(23) / 5.0F;
      ItemStack item = par1EntityProjectile.getItemDisplay();
      GlStateManager.scale(f, f, f);
      WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
      if(par1EntityProjectile.isArrow()) {
         this.bindEntityTexture(par1EntityProjectile);
         GlStateManager.rotate(par1EntityProjectile.prevRotationYaw + (par1EntityProjectile.rotationYaw - par1EntityProjectile.prevRotationYaw) * par9 - 90.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(par1EntityProjectile.prevRotationPitch + (par1EntityProjectile.rotationPitch - par1EntityProjectile.prevRotationPitch) * par9, 0.0F, 0.0F, 1.0F);
         byte f8 = 0;
         float f2 = 0.0F;
         float f3 = 0.5F;
         float f4 = (float)(0 + f8 * 10) / 32.0F;
         float f5 = (float)(5 + f8 * 10) / 32.0F;
         float f6 = 0.0F;
         float f7 = 0.15625F;
         float f81 = (float)(5 + f8 * 10) / 32.0F;
         float f9 = (float)(10 + f8 * 10) / 32.0F;
         float f10 = 0.05625F;
         GlStateManager.enableRescaleNormal();
         float f11 = (float)par1EntityProjectile.arrowShake - par9;
         if(f11 > 0.0F) {
            float i = -MathHelper.sin(f11 * 3.0F) * f11;
            GlStateManager.rotate(i, 0.0F, 0.0F, 1.0F);
         }

         GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.scale(f10, f10, f10);
         GlStateManager.translate(-4.0F, 0.0F, 0.0F);
         worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
         worldRenderer.pos(-7.0D, -2.0D, -2.0D).tex((double)f6, (double)f81).normal(f10, 0.0F, 0.0F).endVertex();
         worldRenderer.pos(-7.0D, -2.0D, 2.0D).tex((double)f7, (double)f81).normal(f10, 0.0F, 0.0F).endVertex();
         worldRenderer.pos(-7.0D, 2.0D, 2.0D).tex((double)f7, (double)f9).normal(f10, 0.0F, 0.0F).endVertex();
         worldRenderer.pos(-7.0D, 2.0D, -2.0D).tex((double)f6, (double)f9).normal(f10, 0.0F, 0.0F).endVertex();
         Tessellator.getInstance().draw();
         worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
         worldRenderer.pos(-7.0D, 2.0D, -2.0D).tex((double)f6, (double)f81).normal(-f10, 0.0F, 0.0F).endVertex();
         worldRenderer.pos(-7.0D, 2.0D, 2.0D).tex((double)f7, (double)f81).normal(-f10, 0.0F, 0.0F).endVertex();
         worldRenderer.pos(-7.0D, -2.0D, 2.0D).tex((double)f7, (double)f9).normal(-f10, 0.0F, 0.0F).endVertex();
         worldRenderer.pos(-7.0D, -2.0D, -2.0D).tex((double)f6, (double)f9).normal(-f10, 0.0F, 0.0F).endVertex();
         Tessellator.getInstance().draw();

         for(int var25 = 0; var25 < 4; ++var25) {
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(-8.0D, -2.0D, 0.0D).tex((double)f2, (double)f4).normal(0.0F, 0.0F, f10).endVertex();
            worldRenderer.pos(8.0D, -2.0D, 0.0D).tex((double)f3, (double)f4).normal(0.0F, 0.0F, f10).endVertex();
            worldRenderer.pos(8.0D, 2.0D, 0.0D).tex((double)f3, (double)f5).normal(0.0F, 0.0F, f10).endVertex();
            worldRenderer.pos(-8.0D, 2.0D, 0.0D).tex((double)f2, (double)f5).normal(0.0F, 0.0F, f10).endVertex();
            Tessellator.getInstance().draw();
         }
      } else if(par1EntityProjectile.is3D()) {
         GlStateManager.rotate(par1EntityProjectile.prevRotationYaw + (par1EntityProjectile.rotationYaw - par1EntityProjectile.prevRotationYaw) * par9 - 180.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(par1EntityProjectile.prevRotationPitch + (par1EntityProjectile.rotationPitch - par1EntityProjectile.prevRotationPitch) * par9, 1.0F, 0.0F, 0.0F);
         GlStateManager.translate(0.0D, -0.125D, 0.25D);
         if(item.getItem() instanceof ItemBlock && Block.getBlockFromItem(item.getItem()).getRenderType() == 2) {
            GlStateManager.translate(0.0F, 0.1875F, -0.3125F);
            GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
            float var26 = 0.375F;
            GlStateManager.scale(-var26, -var26, var26);
         }

         Minecraft.getMinecraft().getRenderItem().renderItem(item, TransformType.THIRD_PERSON);
      } else {
         GlStateManager.enableRescaleNormal();
         GlStateManager.scale(0.5F, 0.5F, 0.5F);
         GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
         this.bindTexture(TextureMap.locationBlocksTexture);
         Minecraft.getMinecraft().getRenderItem().renderItem(item, TransformType.NONE);
         GlStateManager.disableRescaleNormal();
      }

      if(par1EntityProjectile.is3D() && par1EntityProjectile.glows()) {
         GlStateManager.disableLighting();
      }

      GlStateManager.disableRescaleNormal();
      GlStateManager.popMatrix();
      GlStateManager.enableLighting();
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderProjectile((EntityProjectile)par1Entity, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation func_110779_a(EntityProjectile par1EntityProjectile) {
      return par1EntityProjectile.isArrow()?arrowTextures:TextureMap.locationBlocksTexture;
   }

   protected ResourceLocation getEntityTexture(Entity par1Entity) {
      return this.func_110779_a((EntityProjectile)par1Entity);
   }

}

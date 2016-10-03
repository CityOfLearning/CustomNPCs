package noppes.npcs.client.renderer;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import java.io.File;
import java.security.MessageDigest;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.ImageDownloadAlt;
import noppes.npcs.client.renderer.ImageBufferDownloadAlt;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import org.lwjgl.opengl.GL11;

public class RenderNPCInterface extends RenderLiving {

   public static int LastTextureTick;


   public RenderNPCInterface(ModelBase model, float f) {
      super(Minecraft.getMinecraft().getRenderManager(), model, f);
   }

   public void renderName(EntityNPCInterface npc, double d, double d1, double d2) {
      if(npc != null && this.canRenderName(npc)) {
         if(npc.isInRange(this.renderManager.livingPlayer, npc.isSneaking()?32.0D:64.0D)) {
            float scale;
            if(npc.messages != null) {
               scale = npc.baseHeight / 5.0F * (float)npc.display.getSize();
               float height = npc.height * (1.2F + (!npc.display.showName()?0.0F:(npc.display.getTitle().isEmpty()?0.15F:0.25F)));
               npc.messages.renderMessages(d, d1 + (double)height, d2, 0.666667F * scale, npc.isInRange(this.renderManager.livingPlayer, 4.0D));
            }

            scale = npc.baseHeight / 5.0F * (float)npc.display.getSize();
            boolean height1 = false;
            if(npc.display.showName()) {
               String s = npc.getName();
               if(!npc.display.getTitle().isEmpty()) {
                  this.renderLivingLabel(npc, d, d1 + (double)npc.height - (double)(0.06F * scale), d2, 64, new Object[]{"<" + npc.display.getTitle() + ">", Float.valueOf(0.6F), s, Float.valueOf(1.0F)});
                  height1 = true;
               } else {
                  this.renderLivingLabel(npc, d, d1 + (double)npc.height - (double)(0.06F * scale), d2, 64, new Object[]{s, Float.valueOf(1.0F)});
                  height1 = true;
               }
            }

         }
      }
   }

   public void doRenderShadowAndFire(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      EntityNPCInterface npc = (EntityNPCInterface)par1Entity;
      this.shadowSize = npc.width;
      if(!npc.isKilled()) {
         super.doRenderShadowAndFire(par1Entity, par2, par4, par6, par8, par9);
      }

   }

   protected void renderLivingLabel(EntityNPCInterface npc, double d, double d1, double d2, int i, Object ... obs) {
      FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
      i = npc.getBrightnessForRender(0.0F);
      int j = i % 65536;
      int k = i / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
      float f1 = npc.baseHeight / 5.0F * (float)npc.display.getSize();
      float f2 = 0.01666667F * f1;
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)d + 0.0F, (float)d1, (float)d2);
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      WorldRenderer tessellator = Tessellator.getInstance().getWorldRenderer();
      float height = f1 / 6.5F;

      for(j = 0; j < obs.length; j += 2) {
         float scale = ((Float)obs[j + 1]).floatValue();
         height += f1 / 6.5F * scale;
         GlStateManager.pushMatrix();
         GlStateManager.disableLighting();
         GlStateManager.depthMask(false);
         GlStateManager.enableBlend();
         GlStateManager.disableTexture2D();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         String s = obs[j].toString();
         GlStateManager.translate(0.0F, height, 0.0F);
         GlStateManager.scale(-f2 * scale, -f2 * scale, f2 * scale);
         tessellator.begin(7, DefaultVertexFormats.POSITION_COLOR);
         int size = fontrenderer.getStringWidth(s) / 2;
         tessellator.pos((double)(-size - 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
         tessellator.pos((double)(-size - 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
         tessellator.pos((double)(size + 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
         tessellator.pos((double)(size + 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
         Tessellator.getInstance().draw();
         GlStateManager.enableTexture2D();
         int color = npc.faction.color;
         if(npc.isInRange(this.renderManager.livingPlayer, 4.0D)) {
            GlStateManager.disableDepth();
            fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, color + 1426063360);
         }

         GlStateManager.enableDepth();
         GlStateManager.depthMask(true);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, color);
         GlStateManager.popMatrix();
      }

      GlStateManager.enableLighting();
      GlStateManager.disableBlend();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.popMatrix();
   }

   protected void renderColor(EntityNPCInterface npc) {
      if(npc.hurtTime <= 0 && npc.deathTime <= 0) {
         float red = (float)(npc.display.getTint() >> 16 & 255) / 255.0F;
         float green = (float)(npc.display.getTint() >> 8 & 255) / 255.0F;
         float blue = (float)(npc.display.getTint() & 255) / 255.0F;
         GlStateManager.color(red, green, blue, 1.0F);
      }

   }

   private void renderLiving(EntityNPCInterface npc, double d, double d1, double d2, float xoffset, float yoffset, float zoffset) {
      xoffset = xoffset / 5.0F * (float)npc.display.getSize();
      yoffset = yoffset / 5.0F * (float)npc.display.getSize();
      zoffset = zoffset / 5.0F * (float)npc.display.getSize();
      super.renderLivingAt(npc, d + (double)xoffset, d1 + (double)yoffset, d2 + (double)zoffset);
   }

   protected void rotateCorpse(EntityNPCInterface npc, float f, float f1, float f2) {
      if(npc.isEntityAlive() && npc.isPlayerSleeping()) {
         GlStateManager.rotate((float)npc.ai.orientation, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(this.getDeathMaxRotation(npc), 0.0F, 0.0F, 1.0F);
         GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
      } else if(npc.isEntityAlive() && npc.currentAnimation == 7) {
         GlStateManager.rotate(270.0F - f1, 0.0F, 1.0F, 0.0F);
         float scale = (float)((EntityCustomNpc)npc).display.getSize() / 5.0F;
         GlStateManager.translate(-scale + ((EntityCustomNpc)npc).modelData.getLegsY() * scale, 0.14F, 0.0F);
         GlStateManager.rotate(270.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
      } else {
         super.rotateCorpse(npc, f, f1, f2);
      }

   }

   protected void preRenderCallback(EntityNPCInterface npc, float f) {
      this.renderColor(npc);
      int size = npc.display.getSize();
      GlStateManager.scale(npc.scaleX / 5.0F * (float)size, npc.scaleY / 5.0F * (float)size, npc.scaleZ / 5.0F * (float)size);
   }

   public void doRender(EntityNPCInterface npc, double d, double d1, double d2, float f, float f1) {
      if(!npc.isKilled() || !npc.stats.hideKilledBody || npc.deathTime <= 20) {
         if((npc.display.getBossbar() == 1 || npc.display.getBossbar() == 2 && npc.isAttacking()) && !npc.isKilled() && npc.deathTime <= 20 && npc.canSee(Minecraft.getMinecraft().thePlayer)) {
            BossStatus.setBossStatus(npc, true);
         }

         if(npc.ai.getStandingType() == 3 && !npc.isWalking() && !npc.isInteracting()) {
            npc.prevRenderYawOffset = npc.renderYawOffset = (float)npc.ai.orientation;
         }

         super.doRender(npc, d, d1, d2, f, f1);
      }
   }

   protected void renderModel(EntityNPCInterface npc, float par2, float par3, float par4, float par5, float par6, float par7) {
      super.renderModel(npc, par2, par3, par4, par5, par6, par7);
      if(!npc.display.getOverlayTexture().isEmpty()) {
         GlStateManager.depthFunc(515);
         if(npc.textureGlowLocation == null) {
            npc.textureGlowLocation = new ResourceLocation(npc.display.getOverlayTexture());
         }

         this.bindTexture(npc.textureGlowLocation);
         float f1 = 1.0F;
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(1, 1);
         GlStateManager.disableLighting();
         if(npc.isInvisible()) {
            GlStateManager.depthMask(false);
         } else {
            GlStateManager.depthMask(true);
         }

         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.pushMatrix();
         GlStateManager.scale(1.001F, 1.001F, 1.001F);
         this.mainModel.render(npc, par2, par3, par4, par5, par6, par7);
         GlStateManager.popMatrix();
         GlStateManager.enableLighting();
         GlStateManager.color(1.0F, 1.0F, 1.0F, f1);
         GlStateManager.depthFunc(515);
         GlStateManager.disableBlend();
      }

   }

   protected float handleRotationFloat(EntityNPCInterface npc, float par2) {
      return !npc.isKilled() && npc.display.getHasLivingAnimation()?super.handleRotationFloat(npc, par2):0.0F;
   }

   protected void renderLivingAt(EntityNPCInterface npc, double d, double d1, double d2) {
      this.shadowSize = (float)npc.display.getSize() / 10.0F;
      float xOffset = 0.0F;
      float yOffset = npc.currentAnimation == 0?npc.ai.bodyOffsetY / 10.0F - 0.5F:0.0F;
      float zOffset = 0.0F;
      if(npc.isEntityAlive()) {
         if(npc.isPlayerSleeping()) {
            xOffset = (float)(-Math.cos(Math.toRadians((double)(180 - npc.ai.orientation))));
            zOffset = (float)(-Math.sin(Math.toRadians((double)npc.ai.orientation)));
            yOffset += 0.14F;
         } else if(npc.isRiding()) {
            yOffset -= 0.5F - ((EntityCustomNpc)npc).modelData.getLegsY() * 0.8F;
         }
      }

      this.renderLiving(npc, d, d1, d2, xOffset, yOffset, zOffset);
   }

   public ResourceLocation getEntityTexture(EntityNPCInterface npc) {
      if(npc.textureLocation == null) {
         if(npc.display.skinType == 0) {
            npc.textureLocation = new ResourceLocation(npc.display.getSkinTexture());
         } else {
            if(LastTextureTick < 5) {
               return DefaultPlayerSkin.getDefaultSkinLegacy();
            }

            if(npc.display.skinType == 1 && npc.display.playerProfile != null) {
               Minecraft var10 = Minecraft.getMinecraft();
               Map var11 = var10.getSkinManager().loadSkinFromCache(npc.display.playerProfile);
               if(var11.containsKey(Type.SKIN)) {
                  npc.textureLocation = var10.getSkinManager().loadSkin((MinecraftProfileTexture)var11.get(Type.SKIN), Type.SKIN);
               }
            } else if(npc.display.skinType == 2) {
               try {
                  MessageDigest ex = MessageDigest.getInstance("MD5");
                  byte[] hash = ex.digest(npc.display.getSkinUrl().getBytes("UTF-8"));
                  StringBuilder sb = new StringBuilder(2 * hash.length);
                  byte[] var5 = hash;
                  int var6 = hash.length;

                  for(int var7 = 0; var7 < var6; ++var7) {
                     byte b = var5[var7];
                     sb.append(String.format("%02x", new Object[]{Integer.valueOf(b & 255)}));
                  }

                  npc.textureLocation = new ResourceLocation("skins/" + sb.toString());
                  this.loadSkin((File)null, npc.textureLocation, npc.display.getSkinUrl());
               } catch (Exception var9) {
                  ;
               }
            }
         }
      }

      return npc.textureLocation == null?DefaultPlayerSkin.getDefaultSkinLegacy():npc.textureLocation;
   }

   private void loadSkin(File file, ResourceLocation resource, String par1Str) {
      TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
      ImageDownloadAlt object = new ImageDownloadAlt(file, par1Str, DefaultPlayerSkin.getDefaultSkinLegacy(), new ImageBufferDownloadAlt());
      texturemanager.loadTexture(resource, object);
   }
}

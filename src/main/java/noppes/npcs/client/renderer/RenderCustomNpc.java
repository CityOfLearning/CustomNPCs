package noppes.npcs.client.renderer;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.NPCRendererHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import noppes.npcs.client.layer.LayerArms;
import noppes.npcs.client.layer.LayerBody;
import noppes.npcs.client.layer.LayerHead;
import noppes.npcs.client.layer.LayerHeadwear;
import noppes.npcs.client.layer.LayerLeftHeldItem;
import noppes.npcs.client.layer.LayerLegs;
import noppes.npcs.client.layer.LayerNpcCloak;
import noppes.npcs.client.layer.LayerPreRender;
import noppes.npcs.client.model.ModelBipedAlt;
import noppes.npcs.client.renderer.RenderNPCInterface;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class RenderCustomNpc extends RenderNPCInterface {

   private float partialTicks;
   private EntityLivingBase entity;
   private RendererLivingEntity renderEntity;
   public ModelBiped npcmodel;


   public RenderCustomNpc(ModelBiped model) {
      super(model, 0.5F);
      this.npcmodel = (ModelBiped)this.mainModel;
      this.addLayer(new LayerHeadwear(this));
      this.addLayer(new LayerHead(this));
      this.addLayer(new LayerArms(this));
      this.addLayer(new LayerLegs(this));
      this.addLayer(new LayerBody(this));
      this.addLayer(new LayerHeldItem(this));
      this.addLayer(new LayerLeftHeldItem(this));
      this.addLayer(new LayerNpcCloak(this));
      this.addLayer(new LayerCustomHead(this.npcmodel.bipedHead));
      LayerBipedArmor armor = new LayerBipedArmor(this);
      this.addLayer(armor);
      ObfuscationReflectionHelper.setPrivateValue(LayerArmorBase.class, armor, new ModelBipedAlt(0.5F), 1);
      ObfuscationReflectionHelper.setPrivateValue(LayerArmorBase.class, armor, new ModelBipedAlt(1.0F), 2);
   }

   public void doRender(EntityCustomNpc npc, double d, double d1, double d2, float f, float partialTicks) {
      this.partialTicks = partialTicks;
      this.entity = npc.modelData.getEntity(npc);
      if(this.entity != null) {
         Render list = this.renderManager.getEntityRenderObject(this.entity);
         if(list instanceof RendererLivingEntity) {
            this.renderEntity = (RendererLivingEntity)list;
         } else {
            this.renderEntity = null;
            this.entity = null;
         }
      } else {
         this.renderEntity = null;
         List list1 = this.layerRenderers;
         Iterator var11 = list1.iterator();

         while(var11.hasNext()) {
            LayerRenderer layer = (LayerRenderer)var11.next();
            if(layer instanceof LayerPreRender) {
               ((LayerPreRender)layer).preRender(npc);
            }
         }
      }

      this.npcmodel.heldItemRight = npc.getHeldItem() != null?1:0;
      this.npcmodel.heldItemLeft = npc.getOffHand() != null?1:0;
      super.doRender((EntityNPCInterface)npc, d, d1, d2, f, partialTicks);
   }

   protected void renderModel(EntityCustomNpc npc, float par2, float par3, float par4, float par5, float par6, float par7) {
      if(this.renderEntity != null) {
         boolean flag = !npc.isInvisible();
         boolean flag1 = !flag && !npc.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer);
         if(!flag && !flag1) {
            return;
         }

         if(flag1) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
            GlStateManager.depthMask(false);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.alphaFunc(516, 0.003921569F);
         }

         ModelBase model = this.renderEntity.mainModel;
         if(PixelmonHelper.isPixelmon(this.entity)) {
            model = (ModelBase)PixelmonHelper.getModel(this.entity);
         }

         model.setLivingAnimations(this.entity, par2, par3, this.partialTicks);
         model.setRotationAngles(par2, par3, par4, par5, par6, par7, this.entity);
         model.setModelAttributes(this.mainModel);
         model.isChild = this.entity.isChild();
         NPCRendererHelper.RenderModel(this.entity, par2, par3, par4, par5, par6, par7, this.renderEntity, model, this.getEntityTexture(npc));
         if(!npc.display.getOverlayTexture().isEmpty()) {
            GlStateManager.depthFunc(515);
            if(npc.textureGlowLocation == null) {
               npc.textureGlowLocation = new ResourceLocation(npc.display.getOverlayTexture());
            }

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
            NPCRendererHelper.RenderModel(this.entity, par2, par3, par4, par5, par6, par7, this.renderEntity, model, npc.textureGlowLocation);
            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, f1);
            GlStateManager.depthFunc(515);
            GlStateManager.disableBlend();
         }

         if(flag1) {
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
         }
      } else {
         super.renderModel((EntityNPCInterface)npc, par2, par3, par4, par5, par6, par7);
      }

   }

   protected void renderLayers(EntityCustomNpc livingEntity, float p_177093_2_, float p_177093_3_, float p_177093_4_, float p_177093_5_, float p_177093_6_, float p_177093_7_, float p_177093_8_) {
      if(this.entity != null && this.renderEntity != null) {
         NPCRendererHelper.DrawLayers(this.entity, p_177093_2_, p_177093_3_, p_177093_4_, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_, this.renderEntity);
      } else {
         super.renderLayers(livingEntity, p_177093_2_, p_177093_3_, p_177093_4_, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_);
      }

   }

   protected void preRenderCallback(EntityCustomNpc npc, float f) {
      if(this.renderEntity != null) {
         this.renderColor(npc);
         int size = npc.display.getSize();
         if(this.entity instanceof EntityNPCInterface) {
            ((EntityNPCInterface)this.entity).display.setSize(5);
         }

         NPCRendererHelper.preRenderCallback(this.entity, f, this.renderEntity);
         npc.display.setSize(size);
         GlStateManager.scale(0.2F * (float)npc.display.getSize(), 0.2F * (float)npc.display.getSize(), 0.2F * (float)npc.display.getSize());
      } else {
         super.preRenderCallback((EntityNPCInterface)npc, f);
      }

   }

   protected float handleRotationFloat(EntityCustomNpc par1EntityLivingBase, float par2) {
      return this.renderEntity != null?NPCRendererHelper.handleRotationFloat(this.entity, par2, this.renderEntity):super.handleRotationFloat((EntityNPCInterface)par1EntityLivingBase, par2);
   }
}

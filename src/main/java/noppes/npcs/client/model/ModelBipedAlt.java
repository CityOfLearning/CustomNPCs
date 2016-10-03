package noppes.npcs.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import noppes.npcs.ModelData;
import noppes.npcs.ModelPartConfig;
import noppes.npcs.client.model.ModelScaleRenderer;
import noppes.npcs.client.model.animation.AniBow;
import noppes.npcs.client.model.animation.AniCrawling;
import noppes.npcs.client.model.animation.AniDancing;
import noppes.npcs.client.model.animation.AniHug;
import noppes.npcs.client.model.animation.AniNo;
import noppes.npcs.client.model.animation.AniWaving;
import noppes.npcs.client.model.animation.AniYes;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.roles.JobPuppet;

public class ModelBipedAlt extends ModelBiped {

   private Map map = new HashMap();


   public ModelBipedAlt(float scale) {
      super(scale);
      this.bipedLeftArm = this.createScale(this.bipedLeftArm, EnumParts.ARM_LEFT);
      this.bipedRightArm = this.createScale(this.bipedRightArm, EnumParts.ARM_RIGHT);
      this.bipedLeftLeg = this.createScale(this.bipedLeftLeg, EnumParts.LEG_LEFT);
      this.bipedRightLeg = this.createScale(this.bipedRightLeg, EnumParts.LEG_RIGHT);
      this.bipedHead = this.createScale(this.bipedHead, EnumParts.HEAD);
      this.bipedHeadwear = this.createScale(this.bipedHeadwear, EnumParts.HEAD);
      this.bipedBody = this.createScale(this.bipedBody, EnumParts.BODY);
   }

   private ModelScaleRenderer createScale(ModelRenderer renderer, EnumParts part) {
      int textureX = ((Integer)ObfuscationReflectionHelper.getPrivateValue(ModelRenderer.class, renderer, 2)).intValue();
      int textureY = ((Integer)ObfuscationReflectionHelper.getPrivateValue(ModelRenderer.class, renderer, 3)).intValue();
      ModelScaleRenderer model = new ModelScaleRenderer(this, textureX, textureY, part);
      model.textureHeight = renderer.textureHeight;
      model.textureWidth = renderer.textureWidth;
      model.childModels = renderer.childModels;
      model.cubeList = renderer.cubeList;
      copyModelAngles(renderer, model);
      Object list = (List)this.map.get(part);
      if(list == null) {
         this.map.put(part, list = new ArrayList());
      }

      ((List)list).add(model);
      return model;
   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
      EntityCustomNpc player = (EntityCustomNpc)entity;
      ModelData playerdata = player.modelData;
      Iterator job = this.map.keySet().iterator();

      while(job.hasNext()) {
         EnumParts pi = (EnumParts)job.next();
         ModelPartConfig config = playerdata.getPartConfig(pi);

         ModelScaleRenderer model;
         for(Iterator var13 = ((List)this.map.get(pi)).iterator(); var13.hasNext(); model.config = config) {
            model = (ModelScaleRenderer)var13.next();
         }
      }

      if(!this.isRiding) {
         this.isRiding = player.currentAnimation == 1;
      }

      if(this.isSneak && (player.currentAnimation == 7 || player.isPlayerSleeping())) {
         this.isSneak = false;
      }

      this.aimedBow = player.currentAnimation == 6;
      this.isSneak = player.isSneaking();
      this.bipedBody.rotationPointX = this.bipedBody.rotationPointY = this.bipedBody.rotationPointZ = 0.0F;
      this.bipedBody.rotateAngleX = this.bipedBody.rotateAngleY = this.bipedBody.rotateAngleZ = 0.0F;
      this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX = 0.0F;
      this.bipedHeadwear.rotateAngleZ = this.bipedHead.rotateAngleZ = 0.0F;
      this.bipedHeadwear.rotationPointX = this.bipedHead.rotationPointX = 0.0F;
      this.bipedHeadwear.rotationPointY = this.bipedHead.rotationPointY = 0.0F;
      this.bipedHeadwear.rotationPointZ = this.bipedHead.rotationPointZ = 0.0F;
      this.bipedLeftLeg.rotateAngleX = 0.0F;
      this.bipedLeftLeg.rotateAngleY = 0.0F;
      this.bipedLeftLeg.rotateAngleZ = 0.0F;
      this.bipedRightLeg.rotateAngleX = 0.0F;
      this.bipedRightLeg.rotateAngleY = 0.0F;
      this.bipedRightLeg.rotateAngleZ = 0.0F;
      this.bipedLeftArm.rotationPointX = 0.0F;
      this.bipedLeftArm.rotationPointY = 2.0F;
      this.bipedLeftArm.rotationPointZ = 0.0F;
      this.bipedRightArm.rotationPointX = 0.0F;
      this.bipedRightArm.rotationPointY = 2.0F;
      this.bipedRightArm.rotationPointZ = 0.0F;
      super.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
      if(player.isPlayerSleeping()) {
         if(this.bipedHead.rotateAngleX < 0.0F) {
            this.bipedHead.rotateAngleX = 0.0F;
            this.bipedHeadwear.rotateAngleX = 0.0F;
         }
      } else if(player.currentAnimation == 9) {
         this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX = 0.7F;
      } else if(player.currentAnimation == 8) {
         AniHug.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this);
      } else if(player.currentAnimation == 7) {
         AniCrawling.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this);
      } else if(player.currentAnimation == 10) {
         AniWaving.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this);
      } else if(player.currentAnimation == 5) {
         AniDancing.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this);
      } else if(player.currentAnimation == 11) {
         AniBow.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this);
      } else if(player.currentAnimation == 13) {
         AniYes.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this);
      } else if(player.currentAnimation == 12) {
         AniNo.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this);
      } else if(this.isSneak) {
         this.bipedBody.rotateAngleX = 0.5F / playerdata.getPartConfig(EnumParts.BODY).scaleY;
      }

      if(player.advanced.job == 9) {
         JobPuppet job1 = (JobPuppet)player.jobInterface;
         if(job1.isActive()) {
            float pi1 = 3.1415927F;
            if(!job1.head.disabled) {
               this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX = job1.head.rotationX * pi1;
               this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY = job1.head.rotationY * pi1;
               this.bipedHeadwear.rotateAngleZ = this.bipedHead.rotateAngleZ = job1.head.rotationZ * pi1;
            }

            if(!job1.body.disabled) {
               this.bipedBody.rotateAngleX = job1.body.rotationX * pi1;
               this.bipedBody.rotateAngleY = job1.body.rotationY * pi1;
               this.bipedBody.rotateAngleZ = job1.body.rotationZ * pi1;
            }

            if(!job1.larm.disabled) {
               this.bipedLeftArm.rotateAngleX = job1.larm.rotationX * pi1;
               this.bipedLeftArm.rotateAngleY = job1.larm.rotationY * pi1;
               this.bipedLeftArm.rotateAngleZ = job1.larm.rotationZ * pi1;
               if(player.display.getHasLivingAnimation()) {
                  this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
                  this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
               }
            }

            if(!job1.rarm.disabled) {
               this.bipedRightArm.rotateAngleX = job1.rarm.rotationX * pi1;
               this.bipedRightArm.rotateAngleY = job1.rarm.rotationY * pi1;
               this.bipedRightArm.rotateAngleZ = job1.rarm.rotationZ * pi1;
               if(player.display.getHasLivingAnimation()) {
                  this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
                  this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
               }
            }

            if(!job1.rleg.disabled) {
               this.bipedRightLeg.rotateAngleX = job1.rleg.rotationX * pi1;
               this.bipedRightLeg.rotateAngleY = job1.rleg.rotationY * pi1;
               this.bipedRightLeg.rotateAngleZ = job1.rleg.rotationZ * pi1;
            }

            if(!job1.lleg.disabled) {
               this.bipedLeftLeg.rotateAngleX = job1.lleg.rotationX * pi1;
               this.bipedLeftLeg.rotateAngleY = job1.lleg.rotationY * pi1;
               this.bipedLeftLeg.rotateAngleZ = job1.lleg.rotationZ * pi1;
            }
         }
      }

   }

   public ModelRenderer getRandomModelBox(Random random) {
      switch(random.nextInt(5)) {
      case 0:
         return this.bipedHead;
      case 1:
         return this.bipedBody;
      case 2:
         return this.bipedLeftArm;
      case 3:
         return this.bipedRightArm;
      case 4:
         return this.bipedLeftLeg;
      case 5:
         return this.bipedRightLeg;
      default:
         return this.bipedHead;
      }
   }
}

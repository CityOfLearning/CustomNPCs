package noppes.npcs.api.wrapper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IEntityLivingBase;
import noppes.npcs.api.wrapper.EntityWrapper;
import noppes.npcs.api.wrapper.ItemStackWrapper;

public class EntityLivingBaseWrapper extends EntityWrapper implements IEntityLivingBase {

   public EntityLivingBaseWrapper(EntityLivingBase entity) {
      super(entity);
   }

   public float getHealth() {
      return ((EntityLivingBase)this.entity).getHealth();
   }

   public void setHealth(float health) {
      ((EntityLivingBase)this.entity).setHealth(health);
   }

   public float getMaxHealth() {
      return ((EntityLivingBase)this.entity).getMaxHealth();
   }

   public void setMaxHealth(float health) {
      if(health >= 0.0F) {
         ((EntityLivingBase)this.entity).getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)health);
      }
   }

   public boolean isAttacking() {
      return ((EntityLivingBase)this.entity).getAITarget() != null;
   }

   public void setAttackTarget(IEntityLivingBase living) {
      if(living == null) {
         ((EntityLivingBase)this.entity).setRevengeTarget((EntityLivingBase)null);
      } else {
         ((EntityLivingBase)this.entity).setRevengeTarget(living.getMCEntity());
      }

   }

   public IEntityLivingBase getAttackTarget() {
      return (IEntityLivingBase)NpcAPI.Instance().getIEntity(((EntityLivingBase)this.entity).getAITarget());
   }

   public IEntityLivingBase getLastAttacked() {
      return (IEntityLivingBase)NpcAPI.Instance().getIEntity(((EntityLivingBase)this.entity).getLastAttacker());
   }

   public boolean canSeeEntity(IEntity entity) {
      return ((EntityLivingBase)this.entity).canEntityBeSeen(entity.getMCEntity());
   }

   public void swingHand() {
      ((EntityLivingBase)this.entity).swingItem();
   }

   public void addPotionEffect(int effect, int duration, int strength, boolean hideParticles) {
      if(effect >= 0 && effect < Potion.potionTypes.length && Potion.potionTypes[effect] != null) {
         if(strength < 0) {
            strength = 0;
         } else if(strength > 255) {
            strength = 255;
         }

         if(duration < 0) {
            duration = 0;
         } else if(duration > 1000000) {
            duration = 1000000;
         }

         if(!Potion.potionTypes[effect].isInstant()) {
            duration *= 20;
         }

         if(duration == 0) {
            ((EntityLivingBase)this.entity).removePotionEffect(effect);
         } else {
            ((EntityLivingBase)this.entity).addPotionEffect(new PotionEffect(effect, duration, strength, false, hideParticles));
         }

      }
   }

   public void clearPotionEffects() {
      ((EntityLivingBase)this.entity).clearActivePotions();
   }

   public int getPotionEffect(int effect) {
      PotionEffect pf = ((EntityLivingBase)this.entity).getActivePotionEffect(Potion.potionTypes[effect]);
      return pf == null?-1:pf.getAmplifier();
   }

   public IItemStack getHeldItem() {
      ItemStack item = ((EntityLivingBase)this.entity).getHeldItem();
      return item == null?null:new ItemStackWrapper(item);
   }

   public void setHeldItem(IItemStack item) {
      ((EntityLivingBase)this.entity).setCurrentItemOrArmor(0, item == null?null:item.getMCItemStack());
   }

   public IItemStack getArmor(int slot) {
      ItemStack item = ((EntityLivingBase)this.entity).getEquipmentInSlot(slot + 1);
      return item == null?null:new ItemStackWrapper(item);
   }

   public void setArmor(int slot, IItemStack item) {
      ((EntityLivingBase)this.entity).setCurrentItemOrArmor(slot + 1, item == null?null:item.getMCItemStack());
   }

   public int getType() {
      return 5;
   }

   public boolean typeOf(int type) {
      return type == 5?true:super.typeOf(type);
   }

   public boolean isChild() {
      return ((EntityLivingBase)this.entity).isChild();
   }
}

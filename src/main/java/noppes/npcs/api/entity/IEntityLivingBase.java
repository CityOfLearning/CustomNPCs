package noppes.npcs.api.entity;

import net.minecraft.entity.EntityLivingBase;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.entity.IEntity;

public interface IEntityLivingBase extends IEntity {

   float getHealth();

   void setHealth(float var1);

   float getMaxHealth();

   void setMaxHealth(float var1);

   boolean isAttacking();

   void setAttackTarget(IEntityLivingBase var1);

   IEntityLivingBase getAttackTarget();

   IEntityLivingBase getLastAttacked();

   boolean canSeeEntity(IEntity var1);

   void swingHand();

   IItemStack getHeldItem();

   void setHeldItem(IItemStack var1);

   IItemStack getArmor(int var1);

   void setArmor(int var1, IItemStack var2);

   void addPotionEffect(int var1, int var2, int var3, boolean var4);

   void clearPotionEffects();

   int getPotionEffect(int var1);

   boolean isChild();

   EntityLivingBase getMCEntity();
}

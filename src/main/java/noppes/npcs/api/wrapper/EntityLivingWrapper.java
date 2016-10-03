package noppes.npcs.api.wrapper;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IEntityLiving;
import noppes.npcs.api.entity.IEntityLivingBase;
import noppes.npcs.api.wrapper.EntityLivingBaseWrapper;

public class EntityLivingWrapper extends EntityLivingBaseWrapper implements IEntityLiving {

   public EntityLivingWrapper(EntityLiving entity) {
      super(entity);
   }

   public void navigateTo(double x, double y, double z, double speed) {
      ((EntityLiving)this.entity).getNavigator().clearPathEntity();
      ((EntityLiving)this.entity).getNavigator().tryMoveToXYZ(x, y, z, speed * 0.7D);
   }

   public void clearNavigation() {
      ((EntityLiving)this.entity).getNavigator().clearPathEntity();
   }

   public boolean isNavigating() {
      return !((EntityLiving)this.entity).getNavigator().noPath();
   }

   public boolean isAttacking() {
      return super.isAttacking() || ((EntityLiving)this.entity).getAttackTarget() != null;
   }

   public void setAttackTarget(IEntityLivingBase living) {
      if(living == null) {
         ((EntityLiving)this.entity).setAttackTarget((EntityLivingBase)null);
      } else {
         ((EntityLiving)this.entity).setAttackTarget(living.getMCEntity());
      }

      super.setAttackTarget(living);
   }

   public IEntityLivingBase getAttackTarget() {
      IEntityLivingBase base = (IEntityLivingBase)NpcAPI.Instance().getIEntity(((EntityLiving)this.entity).getAttackTarget());
      return base != null?base:super.getAttackTarget();
   }

   public boolean canSeeEntity(IEntity entity) {
      return ((EntityLiving)this.entity).getEntitySenses().canSee(entity.getMCEntity());
   }
}

package noppes.npcs.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIRangedAttack extends EntityAIBase {

   private final EntityNPCInterface npc;
   private EntityLivingBase attackTarget;
   private int rangedAttackTime = 0;
   private int field_75318_f = 0;
   private int field_70846_g = 0;
   private int attackTick = 0;
   private boolean hasFired = false;
   private boolean navOverride = false;


   public EntityAIRangedAttack(IRangedAttackMob par1IRangedAttackMob) {
      if(!(par1IRangedAttackMob instanceof EntityLivingBase)) {
         throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
      } else {
         this.npc = (EntityNPCInterface)par1IRangedAttackMob;
         this.rangedAttackTime = this.npc.stats.ranged.getDelayMin() / 2;
         this.setMutexBits(this.navOverride?AiMutex.PATHING:AiMutex.LOOK + AiMutex.PASSIVE);
      }
   }

   public boolean shouldExecute() {
      this.attackTarget = this.npc.getAttackTarget();
      return this.attackTarget != null && this.attackTarget.isEntityAlive() && this.npc.isInRange(this.attackTarget, (double)this.npc.stats.aggroRange) && this.npc.inventory.getProjectile() != null?this.npc.stats.ranged.getMeleeRange() < 1 || !this.npc.isInRange(this.attackTarget, (double)this.npc.stats.ranged.getMeleeRange()):false;
   }

   public void resetTask() {
      this.attackTarget = null;
      this.npc.setAttackTarget((EntityLivingBase)null);
      this.npc.getNavigator().clearPathEntity();
      this.field_75318_f = 0;
      this.hasFired = false;
      this.rangedAttackTime = this.npc.stats.ranged.getDelayMin() / 2;
   }

   public void updateTask() {
      this.npc.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
      double var1 = this.npc.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
      float range = (float)(this.npc.stats.ranged.getRange() * this.npc.stats.ranged.getRange());
      if(!this.navOverride && this.npc.ai.directLOS) {
         if(this.npc.getEntitySenses().canSee(this.attackTarget)) {
            ++this.field_75318_f;
         } else {
            this.field_75318_f = 0;
         }

         int indirect = this.npc.ai.tacticalVariant == 0?20:5;
         if(var1 <= (double)range && this.field_75318_f >= indirect) {
            this.npc.getNavigator().clearPathEntity();
         } else {
            this.npc.getNavigator().tryMoveToEntityLiving(this.attackTarget, 1.0D);
         }
      }

      this.rangedAttackTime = Math.max(this.rangedAttackTime - 1, 0);
      if(this.rangedAttackTime <= 0 && var1 <= (double)range && (this.npc.getEntitySenses().canSee(this.attackTarget) || this.npc.stats.ranged.getFireType() == 2)) {
         if(this.field_70846_g++ <= this.npc.stats.ranged.getBurst()) {
            this.rangedAttackTime = this.npc.stats.ranged.getBurstDelay();
         } else {
            this.field_70846_g = 0;
            this.hasFired = true;
            this.rangedAttackTime = this.npc.stats.ranged.getDelayRNG();
         }

         if(this.field_70846_g > 1) {
            boolean var5 = false;
            switch(this.npc.stats.ranged.getFireType()) {
            case 1:
               var5 = var1 > (double)range / 2.0D;
               break;
            case 2:
               var5 = !this.npc.getEntitySenses().canSee(this.attackTarget);
            }

            this.npc.attackEntityWithRangedAttack(this.attackTarget, var5?1.0F:0.0F);
            if(this.npc.currentAnimation != 6) {
               this.npc.swingItem();
            }
         }
      }

   }

   public boolean hasFired() {
      return this.hasFired;
   }

   public void navOverride(boolean nav) {
      this.navOverride = nav;
      this.setMutexBits(this.navOverride?AiMutex.PATHING:AiMutex.LOOK + AiMutex.PASSIVE);
   }
}

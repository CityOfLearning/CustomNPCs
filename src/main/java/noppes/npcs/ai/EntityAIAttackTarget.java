package noppes.npcs.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIAttackTarget extends EntityAIBase {

   private World worldObj;
   private EntityNPCInterface npc;
   private EntityLivingBase entityTarget;
   private int attackTick = 0;
   private PathEntity entityPathEntity;
   private int delayCounter;
   private boolean navOverride = false;


   public EntityAIAttackTarget(EntityNPCInterface par1EntityLiving) {
      this.npc = par1EntityLiving;
      this.worldObj = par1EntityLiving.worldObj;
      this.setMutexBits(this.navOverride?AiMutex.PATHING:AiMutex.LOOK + AiMutex.PASSIVE);
   }

   public boolean shouldExecute() {
      EntityLivingBase entitylivingbase = this.npc.getAttackTarget();
      if(entitylivingbase != null && entitylivingbase.isEntityAlive()) {
         int melee = this.npc.stats.ranged.getMeleeRange();
         if(this.npc.inventory.getProjectile() != null && (melee <= 0 || !this.npc.isInRange(entitylivingbase, (double)melee))) {
            return false;
         } else {
            this.entityTarget = entitylivingbase;
            this.entityPathEntity = this.npc.getNavigator().getPathToEntityLiving(entitylivingbase);
            return this.entityPathEntity != null;
         }
      } else {
         return false;
      }
   }

   public boolean continueExecuting() {
      this.entityTarget = this.npc.getAttackTarget();
      if(this.entityTarget != null && this.entityTarget.isEntityAlive()) {
         if(!this.npc.isInRange(this.entityTarget, (double)this.npc.stats.aggroRange)) {
            return false;
         } else {
            int melee = this.npc.stats.ranged.getMeleeRange();
            return melee > 0 && !this.npc.isInRange(this.entityTarget, (double)melee)?false:this.npc.isWithinHomeDistanceFromPosition(new BlockPos(this.entityTarget));
         }
      } else {
         return false;
      }
   }

   public void startExecuting() {
      if(!this.navOverride) {
         this.npc.getNavigator().setPath(this.entityPathEntity, 1.3D);
      }

      this.delayCounter = 0;
   }

   public void resetTask() {
      this.entityPathEntity = null;
      this.entityTarget = null;
      this.npc.setAttackTarget((EntityLivingBase)null);
      this.npc.getNavigator().clearPathEntity();
   }

   public void updateTask() {
      this.npc.getLookHelper().setLookPositionWithEntity(this.entityTarget, 30.0F, 30.0F);
      if(!this.navOverride && --this.delayCounter <= 0) {
         this.delayCounter = 4 + this.npc.getRNG().nextInt(7);
         this.npc.getNavigator().tryMoveToEntityLiving(this.entityTarget, 1.2999999523162842D);
      }

      this.attackTick = Math.max(this.attackTick - 1, 0);
      double y = this.entityTarget.posY;
      if(this.entityTarget.getEntityBoundingBox() != null) {
         y = this.entityTarget.getEntityBoundingBox().minY;
      }

      double distance = this.npc.getDistanceSq(this.entityTarget.posX, y, this.entityTarget.posZ);
      double range = (double)((float)(this.npc.stats.melee.getRange() * this.npc.stats.melee.getRange()) + this.entityTarget.width);
      double minRange = (double)(this.npc.width * 2.0F * this.npc.width * 2.0F + this.entityTarget.width);
      if(minRange > range) {
         range = minRange;
      }

      if(distance <= range && (this.npc.canSee(this.entityTarget) || distance < minRange) && this.attackTick <= 0) {
         this.attackTick = this.npc.stats.melee.getDelay();
         this.npc.swingItem();
         this.npc.attackEntityAsMob(this.entityTarget);
      }

   }

   public void navOverride(boolean nav) {
      this.navOverride = nav;
      this.setMutexBits(this.navOverride?AiMutex.PATHING:AiMutex.LOOK + AiMutex.PASSIVE);
   }
}

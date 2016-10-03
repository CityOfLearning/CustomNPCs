package noppes.npcs.ai;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.Vec3;
import noppes.npcs.ai.RandomPositionGeneratorAlt;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIAvoidTarget extends EntityAIBase {

   private EntityNPCInterface npc;
   private Entity closestLivingEntity;
   private float distanceFromEntity;
   private float health;
   private PathEntity entityPathEntity;
   private PathNavigate entityPathNavigate;
   private Class targetEntityClass;


   public EntityAIAvoidTarget(EntityNPCInterface par1EntityNPC) {
      this.npc = par1EntityNPC;
      this.distanceFromEntity = (float)this.npc.stats.aggroRange;
      this.health = this.npc.getHealth();
      this.entityPathNavigate = par1EntityNPC.getNavigator();
      this.setMutexBits(AiMutex.PASSIVE + AiMutex.LOOK);
   }

   public boolean shouldExecute() {
      EntityLivingBase target = this.npc.getAttackTarget();
      if(target == null) {
         return false;
      } else {
         this.targetEntityClass = target.getClass();
         if(this.targetEntityClass == EntityPlayer.class) {
            this.closestLivingEntity = this.npc.worldObj.getClosestPlayerToEntity(this.npc, (double)this.distanceFromEntity);
            if(this.closestLivingEntity == null) {
               return false;
            }
         } else {
            List var2 = this.npc.worldObj.getEntitiesWithinAABB(this.targetEntityClass, this.npc.getEntityBoundingBox().expand((double)this.distanceFromEntity, 3.0D, (double)this.distanceFromEntity));
            if(var2.isEmpty()) {
               return false;
            }

            this.closestLivingEntity = (Entity)var2.get(0);
         }

         if(!this.npc.getEntitySenses().canSee(this.closestLivingEntity) && this.npc.ai.directLOS) {
            return false;
         } else {
            Vec3 var21 = RandomPositionGeneratorAlt.findRandomTargetBlockAwayFrom(this.npc, 16, 7, new Vec3(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));
            boolean var3 = this.npc.inventory.getProjectile() == null;
            boolean var4 = var3?this.health == this.npc.getHealth():this.npc.getRangedTask() != null && !this.npc.getRangedTask().hasFired();
            if(var21 == null) {
               return false;
            } else if(this.closestLivingEntity.getDistanceSq(var21.xCoord, var21.yCoord, var21.zCoord) < this.closestLivingEntity.getDistanceSqToEntity(this.npc)) {
               return false;
            } else if(this.npc.ai.tacticalVariant == 3 && var4) {
               return false;
            } else {
               this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(var21.xCoord, var21.yCoord, var21.zCoord);
               return this.entityPathEntity == null?false:this.entityPathEntity.isDestinationSame(var21);
            }
         }
      }
   }

   public boolean continueExecuting() {
      return !this.entityPathNavigate.noPath();
   }

   public void startExecuting() {
      this.entityPathNavigate.setPath(this.entityPathEntity, 1.0D);
   }

   public void resetTask() {
      this.closestLivingEntity = null;
      this.npc.setAttackTarget((EntityLivingBase)null);
   }

   public void updateTask() {
      if(this.npc.isInRange(this.closestLivingEntity, 7.0D)) {
         this.npc.getNavigator().setSpeed(1.2D);
      } else {
         this.npc.getNavigator().setSpeed(1.0D);
      }

      if(this.npc.ai.tacticalVariant == 3 && (!this.npc.isInRange(this.closestLivingEntity, (double)this.distanceFromEntity) || this.npc.isInRange(this.closestLivingEntity, (double)this.npc.ai.getTacticalRange()))) {
         this.health = this.npc.getHealth();
      }

   }
}

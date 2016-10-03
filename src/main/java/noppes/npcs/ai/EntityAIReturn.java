package noppes.npcs.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import noppes.npcs.CustomNpcs;
import noppes.npcs.ai.RandomPositionGeneratorAlt;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIReturn extends EntityAIBase {

   public static final int MaxTotalTicks = 600;
   private final EntityNPCInterface npc;
   private int stuckTicks = 0;
   private int totalTicks = 0;
   private double endPosX;
   private double endPosY;
   private double endPosZ;
   private boolean wasAttacked = false;
   private double[] preAttackPos;
   private int stuckCount = 0;


   public EntityAIReturn(EntityNPCInterface npc) {
      this.npc = npc;
      this.setMutexBits(AiMutex.PASSIVE);
   }

   public boolean shouldExecute() {
      if(!this.npc.hasOwner() && this.npc.ai.returnToStart && !this.npc.isKilled() && this.npc.getNavigator().noPath() && !this.npc.isInteracting()) {
         BlockPos x;
         if(this.npc.ai.findShelter == 0 && (!this.npc.worldObj.isDaytime() || this.npc.worldObj.isRaining()) && !this.npc.worldObj.provider.getHasNoSky()) {
            x = new BlockPos((double)this.npc.getStartXPos(), this.npc.getStartYPos(), (double)this.npc.getStartZPos());
            if(this.npc.worldObj.canSeeSky(x) || this.npc.worldObj.getLight(x) <= 8) {
               return false;
            }
         } else if(this.npc.ai.findShelter == 1 && this.npc.worldObj.isDaytime()) {
            x = new BlockPos((double)this.npc.getStartXPos(), this.npc.getStartYPos(), (double)this.npc.getStartZPos());
            if(this.npc.worldObj.canSeeSky(x)) {
               return false;
            }
         }

         if(this.npc.isAttacking()) {
            if(!this.wasAttacked) {
               this.wasAttacked = true;
               this.preAttackPos = new double[]{this.npc.posX, this.npc.posY, this.npc.posZ};
            }

            return false;
         } else if(!this.npc.isAttacking() && this.wasAttacked) {
            return true;
         } else if(this.npc.ai.getMovingType() == 2 && this.npc.ai.getDistanceSqToPathPoint() < (double)(CustomNpcs.NpcNavRange * CustomNpcs.NpcNavRange)) {
            return false;
         } else if(this.npc.ai.getMovingType() == 1) {
            double x1 = this.npc.posX - (double)this.npc.getStartXPos();
            double z = this.npc.posX - (double)this.npc.getStartZPos();
            return !this.npc.isInRange((double)this.npc.getStartXPos(), -1.0D, (double)this.npc.getStartZPos(), (double)this.npc.ai.walkingRange);
         } else {
            return this.npc.ai.getMovingType() == 0?!this.npc.isVeryNearAssignedPlace():false;
         }
      } else {
         return false;
      }
   }

   public boolean continueExecuting() {
      return !this.npc.isFollower() && !this.npc.isKilled() && !this.npc.isAttacking() && !this.npc.isVeryNearAssignedPlace() && !this.npc.isInteracting()?(this.npc.getNavigator().noPath() && this.wasAttacked && !this.isTooFar()?false:this.totalTicks <= 600):false;
   }

   public void updateTask() {
      ++this.totalTicks;
      if(this.totalTicks > 600) {
         this.npc.setPosition(this.endPosX, this.endPosY, this.endPosZ);
         this.npc.getNavigator().clearPathEntity();
      } else {
         if(this.stuckTicks > 0) {
            --this.stuckTicks;
         } else if(this.npc.getNavigator().noPath()) {
            ++this.stuckCount;
            this.stuckTicks = 10;
            if((this.totalTicks <= 30 || !this.wasAttacked || !this.isTooFar()) && this.stuckCount <= 5) {
               this.navigate(this.stuckCount % 2 == 1);
            } else {
               this.npc.setPosition(this.endPosX, this.endPosY, this.endPosZ);
               this.npc.getNavigator().clearPathEntity();
            }
         } else {
            this.stuckCount = 0;
         }

      }
   }

   private boolean isTooFar() {
      int allowedDistance = this.npc.stats.aggroRange * 2;
      if(this.npc.ai.getMovingType() == 1) {
         allowedDistance += this.npc.ai.walkingRange;
      }

      double x = this.npc.posX - this.endPosX;
      double z = this.npc.posX - this.endPosZ;
      return x * x + z * z > (double)(allowedDistance * allowedDistance);
   }

   public void startExecuting() {
      this.stuckTicks = 0;
      this.totalTicks = 0;
      this.stuckCount = 0;
      this.navigate(false);
   }

   private void navigate(boolean towards) {
      if(!this.wasAttacked) {
         this.endPosX = (double)this.npc.getStartXPos();
         this.endPosY = this.npc.getStartYPos();
         this.endPosZ = (double)this.npc.getStartZPos();
      } else {
         this.endPosX = this.preAttackPos[0];
         this.endPosY = this.preAttackPos[1];
         this.endPosZ = this.preAttackPos[2];
      }

      double posX = this.endPosX;
      double posY = this.endPosY;
      double posZ = this.endPosZ;
      double range = this.npc.getDistance(posX, posY, posZ);
      if(range > (double)CustomNpcs.NpcNavRange || towards) {
         int distance = (int)range;
         if(distance > CustomNpcs.NpcNavRange) {
            distance = CustomNpcs.NpcNavRange / 2;
         } else {
            distance /= 2;
         }

         if(distance > 2) {
            Vec3 start = new Vec3(posX, posY, posZ);
            Vec3 pos = RandomPositionGeneratorAlt.findRandomTargetBlockTowards(this.npc, distance, distance / 2 > 7?7:distance / 2, start);
            if(pos != null) {
               posX = pos.xCoord;
               posY = pos.yCoord;
               posZ = pos.zCoord;
            }
         }
      }

      this.npc.getNavigator().clearPathEntity();
      this.npc.getNavigator().tryMoveToXYZ(posX, posY, posZ, 1.0D);
   }

   public void resetTask() {
      this.wasAttacked = false;
      this.npc.getNavigator().clearPathEntity();
   }
}

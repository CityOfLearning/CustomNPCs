package noppes.npcs.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIZigZagTarget extends EntityAIBase {

   private EntityNPCInterface npc;
   private EntityLivingBase targetEntity;
   private PathEntity pathentity;
   private double movePosX;
   private double movePosY;
   private double movePosZ;
   private int entityPosX;
   private int entityPosY;
   private int entityPosZ;
   private double speed;
   private int ticks;


   public EntityAIZigZagTarget(EntityNPCInterface par1EntityCreature, double par2) {
      this.npc = par1EntityCreature;
      this.speed = par2;
      this.ticks = 0;
      this.setMutexBits(AiMutex.PASSIVE + AiMutex.LOOK);
   }

   public boolean shouldExecute() {
      this.targetEntity = this.npc.getAttackTarget();
      return this.targetEntity != null && this.targetEntity.isEntityAlive()?!this.npc.isInRange(this.targetEntity, (double)this.npc.ai.getTacticalRange()):false;
   }

   public void resetTask() {
      this.npc.getNavigator().clearPathEntity();
      this.ticks = 0;
   }

   public void updateTask() {
      this.npc.getLookHelper().setLookPositionWithEntity(this.targetEntity, 30.0F, 30.0F);
      if(this.ticks-- <= 0) {
         PathEntity pathentity = this.npc.getNavigator().getPathToEntityLiving(this.targetEntity);
         if(pathentity != null && pathentity.getCurrentPathLength() >= this.npc.ai.getTacticalRange()) {
            PathPoint pathpoint = pathentity.getPathPointFromIndex(MathHelper.floor_double((double)this.npc.ai.getTacticalRange() / 2.0D));
            this.entityPosX = pathpoint.xCoord;
            this.entityPosY = pathpoint.yCoord;
            this.entityPosZ = pathpoint.zCoord;
            Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.npc, this.npc.ai.getTacticalRange(), 3, new Vec3((double)this.entityPosX, (double)this.entityPosY, (double)this.entityPosZ));
            if(vec3 != null) {
               if(this.targetEntity.getDistanceSq(vec3.xCoord, vec3.yCoord, vec3.zCoord) < this.targetEntity.getDistanceSq((double)this.entityPosX, (double)this.entityPosY, (double)this.entityPosZ)) {
                  this.movePosX = vec3.xCoord;
                  this.movePosY = vec3.yCoord;
                  this.movePosZ = vec3.zCoord;
               }
            } else {
               this.movePosX = (double)pathpoint.xCoord;
               this.movePosY = (double)pathpoint.yCoord;
               this.movePosZ = (double)pathpoint.zCoord;
            }

            this.npc.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.speed);
         } else {
            this.ticks = 10;
         }
      }

   }
}

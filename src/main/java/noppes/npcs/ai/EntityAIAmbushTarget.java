package noppes.npcs.ai;

import java.util.Random;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIAmbushTarget extends EntityAIBase {

   private EntityNPCInterface npc;
   private EntityLivingBase targetEntity;
   private double shelterX;
   private double shelterY;
   private double shelterZ;
   private double movementSpeed;
   private double distance;
   private int delay = 0;
   private World theWorld;
   private int tick;


   public EntityAIAmbushTarget(EntityNPCInterface par1EntityCreature, double par2) {
      this.npc = par1EntityCreature;
      this.movementSpeed = par2;
      this.theWorld = par1EntityCreature.worldObj;
      this.setMutexBits(AiMutex.PASSIVE + AiMutex.LOOK);
   }

   public boolean shouldExecute() {
      this.targetEntity = this.npc.getAttackTarget();
      this.distance = (double)this.npc.ai.getTacticalRange();
      if(this.targetEntity != null && !this.npc.isInRange(this.targetEntity, this.distance) && this.npc.canSee(this.targetEntity) && this.delay-- <= 0) {
         Vec3 vec3 = this.findHidingSpot();
         if(vec3 == null) {
            this.delay = 10;
            return false;
         } else {
            this.shelterX = vec3.xCoord;
            this.shelterY = vec3.yCoord;
            this.shelterZ = vec3.zCoord;
            return true;
         }
      } else {
         return false;
      }
   }

   public boolean continueExecuting() {
      boolean shouldHide = !this.npc.isInRange(this.targetEntity, this.distance);
      boolean isSeen = this.npc.canSee(this.targetEntity);
      return !this.npc.getNavigator().noPath() && shouldHide || !isSeen && (shouldHide || this.npc.ai.directLOS);
   }

   public void startExecuting() {
      this.npc.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
   }

   public void resetTask() {
      this.npc.getNavigator().clearPathEntity();
      if(this.npc.getAttackTarget() == null && this.targetEntity != null) {
         this.npc.setAttackTarget(this.targetEntity);
      }

      if(!this.npc.isInRange(this.targetEntity, this.distance)) {
         this.delay = 60;
      }

   }

   public void updateTask() {
      this.npc.getLookHelper().setLookPositionWithEntity(this.targetEntity, 30.0F, 30.0F);
   }

   private Vec3 findHidingSpot() {
      Random random = this.npc.getRNG();
      Vec3 idealPos = null;

      for(int i = 1; i <= 8; ++i) {
         for(int y = -2; y <= 2; ++y) {
            double k = (double)MathHelper.floor_double(this.npc.getEntityBoundingBox().minY + (double)y);

            for(int x = -i; x <= i; ++x) {
               double j = (double)MathHelper.floor_double(this.npc.posX + (double)x) + 0.5D;

               for(int z = -i; z <= i; ++z) {
                  double l = (double)MathHelper.floor_double(this.npc.posZ + (double)z) + 0.5D;
                  if(this.isOpaque((int)j, (int)k, (int)l) && !this.isOpaque((int)j, (int)k + 1, (int)l) && this.isOpaque((int)j, (int)k + 2, (int)l)) {
                     Vec3 vec1 = new Vec3(this.targetEntity.posX, this.targetEntity.posY + (double)this.targetEntity.getEyeHeight(), this.targetEntity.posZ);
                     Vec3 vec2 = new Vec3(j, k + (double)this.npc.getEyeHeight(), l);
                     MovingObjectPosition movingobjectposition = this.theWorld.rayTraceBlocks(vec1, vec2);
                     if(movingobjectposition != null && this.shelterX != j && this.shelterY != k && this.shelterZ != l) {
                        idealPos = new Vec3(j, k, l);
                     }
                  }
               }
            }
         }

         if(idealPos != null) {
            return idealPos;
         }
      }

      this.delay = 60;
      return null;
   }

   private boolean isOpaque(int x, int y, int z) {
      return this.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock().isOpaqueCube();
   }
}

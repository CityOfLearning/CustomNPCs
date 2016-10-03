package noppes.npcs.ai;

import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIAnimation extends EntityAIBase {

   private EntityNPCInterface npc;
   private boolean isAttacking = false;
   private boolean isDead = false;
   private boolean isAtStartpoint = false;
   private boolean hasPath = false;
   private int tick = 4;
   public int temp = 0;


   public EntityAIAnimation(EntityNPCInterface npc) {
      this.npc = npc;
   }

   public boolean shouldExecute() {
      this.isDead = !this.npc.isEntityAlive();
      if(this.isDead) {
         return this.npc.currentAnimation != 2;
      } else if(this.npc.stats.ranged.getHasAimAnimation() && this.npc.isAttacking()) {
         return this.npc.currentAnimation != 6;
      } else {
         this.hasPath = !this.npc.getNavigator().noPath();
         this.isAttacking = this.npc.isAttacking();
         this.isAtStartpoint = this.npc.ai.returnToStart && this.npc.isVeryNearAssignedPlace();
         if(this.temp != 0) {
            if(!this.hasNavigation()) {
               return this.npc.currentAnimation != this.temp;
            }

            this.temp = 0;
         }

         return this.hasNavigation() && !isWalkingAnimation(this.npc.currentAnimation)?this.npc.currentAnimation != 0:this.npc.currentAnimation != this.npc.ai.animationType;
      }
   }

   public void updateTask() {
      if(this.npc.stats.ranged.getHasAimAnimation() && this.npc.isAttacking()) {
         this.setAnimation(6);
      } else {
         int type = this.npc.ai.animationType;
         if(this.isDead) {
            type = 2;
         } else if(!isWalkingAnimation(this.npc.ai.animationType) && this.hasNavigation()) {
            type = 0;
         } else if(this.temp != 0) {
            if(this.hasNavigation()) {
               this.temp = 0;
            } else {
               type = this.temp;
            }
         }

         this.setAnimation(type);
      }
   }

   public void resetTask() {}

   public static int getWalkingAnimationGuiIndex(int animation) {
      return animation == 4?1:(animation == 6?2:(animation == 5?3:(animation == 7?4:(animation == 8?5:0))));
   }

   public static boolean isWalkingAnimation(int animation) {
      return getWalkingAnimationGuiIndex(animation) != 0;
   }

   private void setAnimation(int animation) {
      this.npc.setCurrentAnimation(animation);
      this.npc.updateHitbox();
      this.npc.setPosition(this.npc.posX, this.npc.posY, this.npc.posZ);
   }

   private boolean hasNavigation() {
      return this.isAttacking || this.npc.ai.returnToStart && !this.isAtStartpoint && !this.npc.isFollower() || this.hasPath;
   }
}

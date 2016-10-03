package noppes.npcs.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigateGround;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIWaterNav extends EntityAIBase {

   private EntityNPCInterface theEntity;


   public EntityAIWaterNav(EntityNPCInterface par1EntityNPCInterface) {
      this.theEntity = par1EntityNPCInterface;
      ((PathNavigateGround)par1EntityNPCInterface.getNavigator()).setCanSwim(true);
   }

   public boolean shouldExecute() {
      if(this.theEntity.isInWater() || this.theEntity.isInLava()) {
         if(this.theEntity.ai.canSwim) {
            return true;
         }

         if(this.theEntity.isCollidedHorizontally) {
            return true;
         }
      }

      return false;
   }

   public void updateTask() {
      if(this.theEntity.getRNG().nextFloat() < 0.8F) {
         this.theEntity.getJumpHelper().setJumping();
      }

   }
}

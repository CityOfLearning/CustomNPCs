package noppes.npcs.ai.target;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIClearTarget extends EntityAITarget {

   private EntityNPCInterface npc;
   private EntityLivingBase target;


   public EntityAIClearTarget(EntityNPCInterface npc) {
      super(npc, false);
      this.npc = npc;
   }

   public boolean shouldExecute() {
      this.target = this.taskOwner.getAttackTarget();
      return this.target == null?false:(this.target instanceof EntityPlayer && ((EntityPlayer)this.target).capabilities.disableDamage?true:(this.npc.getOwner() != null && !this.npc.isInRange(this.npc.getOwner(), (double)(this.npc.stats.aggroRange * 2))?true:!this.npc.isInRange(this.target, (double)(this.npc.stats.aggroRange * 2))));
   }

   public void startExecuting() {
      this.taskOwner.setAttackTarget((EntityLivingBase)null);
      if(this.target == this.taskOwner.getAITarget()) {
         this.taskOwner.setRevengeTarget((EntityLivingBase)null);
      }

      super.startExecuting();
   }

   public void resetTask() {
      this.npc.getNavigator().clearPathEntity();
   }
}

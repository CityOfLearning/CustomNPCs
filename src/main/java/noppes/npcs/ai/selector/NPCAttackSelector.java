package noppes.npcs.ai.selector;

import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import noppes.npcs.constants.EnumCompanionJobs;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobGuard;
import noppes.npcs.roles.JobSpawner;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.roles.companion.CompanionGuard;

public class NPCAttackSelector implements Predicate {

   private EntityNPCInterface npc;


   public NPCAttackSelector(EntityNPCInterface npc) {
      this.npc = npc;
   }

   public boolean isEntityApplicable(EntityLivingBase entity) {
      if(entity.isEntityAlive() && entity != this.npc && this.npc.isInRange(entity, (double)this.npc.stats.aggroRange) && entity.getHealth() >= 1.0F) {
         if(this.npc.ai.directLOS && !this.npc.getEntitySenses().canSee(entity)) {
            return false;
         } else if(!this.npc.ai.attackInvisible && entity.isPotionActive(Potion.invisibility) && this.npc.isInRange(entity, 3.0D)) {
            return false;
         } else {
            if(!this.npc.isFollower() && this.npc.ai.returnToStart) {
               int player = this.npc.stats.aggroRange * 2;
               if(this.npc.ai.getMovingType() == 1) {
                  player += this.npc.ai.walkingRange;
               }

               double distance = entity.getDistanceSq((double)this.npc.getStartXPos(), this.npc.getStartYPos(), (double)this.npc.getStartZPos());
               if(this.npc.ai.getMovingType() == 2) {
                  int[] arr = this.npc.ai.getCurrentMovingPath();
                  distance = entity.getDistanceSq((double)arr[0], (double)arr[1], (double)arr[2]);
               }

               if(distance > (double)(player * player)) {
                  return false;
               }
            }

            if(this.npc.advanced.job == 3 && ((JobGuard)this.npc.jobInterface).isEntityApplicable(entity)) {
               return true;
            } else {
               if(this.npc.advanced.role == 6) {
                  RoleCompanion player1 = (RoleCompanion)this.npc.roleInterface;
                  if(player1.job == EnumCompanionJobs.GUARD && ((CompanionGuard)player1.jobInterface).isEntityApplicable(entity)) {
                     return true;
                  }
               }

               if(entity instanceof EntityPlayerMP) {
                  EntityPlayerMP player2 = (EntityPlayerMP)entity;
                  return this.npc.faction.isAggressiveToPlayer(player2) && !player2.capabilities.disableDamage?(this.npc.ai.targetType == 1 && player2.isSneaking()?this.npc.isInRange(player2, (double)this.npc.ai.specialAggroRange):(PixelmonHelper.Enabled && this.npc.advanced.job == 6 && ((JobSpawner)this.npc.jobInterface).hasPixelmon()?PixelmonHelper.canBattle((EntityPlayerMP)entity, this.npc):true)):false;
               } else {
                  if(entity instanceof EntityNPCInterface) {
                     if(((EntityNPCInterface)entity).isKilled()) {
                        return false;
                     }

                     if(this.npc.advanced.attackOtherFactions) {
                        return this.npc.faction.isAggressiveToNpc((EntityNPCInterface)entity);
                     }
                  }

                  return false;
               }
            }
         }
      } else {
         return false;
      }
   }

   public boolean apply(Object ob) {
      return !(ob instanceof EntityLivingBase)?false:this.isEntityApplicable((EntityLivingBase)ob);
   }
}

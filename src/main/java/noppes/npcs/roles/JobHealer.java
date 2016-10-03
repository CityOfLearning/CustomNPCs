package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import noppes.npcs.NBTTags;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobInterface;
import noppes.npcs.util.ValueUtil;

public class JobHealer extends JobInterface {

   private int healTicks = 0;
   public int range = 8;
   public byte type = 2;
   public int speed = 20;
   public HashMap effects = new HashMap();
   private List affected = new ArrayList();


   public JobHealer(EntityNPCInterface npc) {
      super(npc);
   }

   public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setInteger("HealerRange", this.range);
      nbttagcompound.setByte("HealerType", this.type);
      nbttagcompound.setTag("BeaconEffects", NBTTags.nbtIntegerIntegerMap(this.effects));
      nbttagcompound.setInteger("HealerSpeed", this.speed);
      return nbttagcompound;
   }

   public void readFromNBT(NBTTagCompound nbttagcompound) {
      this.range = nbttagcompound.getInteger("HealerRange");
      this.type = nbttagcompound.getByte("HealerType");
      this.effects = NBTTags.getIntegerIntegerMap(nbttagcompound.getTagList("BeaconEffects", 10));
      this.speed = ValueUtil.CorrectInt(nbttagcompound.getInteger("HealerSpeed"), 10, Integer.MAX_VALUE);
   }

   public boolean aiShouldExecute() {
      ++this.healTicks;
      if(this.healTicks < this.speed) {
         return false;
      } else {
         this.healTicks = 0;
         this.affected = this.npc.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.npc.getEntityBoundingBox().expand((double)this.range, (double)this.range / 2.0D, (double)this.range));
         return !this.affected.isEmpty();
      }
   }

   public boolean aiContinueExecute() {
      return false;
   }

   public void aiStartExecuting() {
      Iterator var1 = this.affected.iterator();

      while(var1.hasNext()) {
         EntityLivingBase entity = (EntityLivingBase)var1.next();
         boolean isEnemy = false;
         if(entity instanceof EntityPlayer) {
            isEnemy = this.npc.faction.isAggressiveToPlayer((EntityPlayer)entity);
         } else if(entity instanceof EntityNPCInterface) {
            isEnemy = this.npc.faction.isAggressiveToNpc((EntityNPCInterface)entity);
         } else {
            isEnemy = entity instanceof EntityMob;
         }

         if(entity != this.npc && (this.type == 2 || !isEnemy || this.type == 1 || isEnemy || this.type == 0)) {
            Iterator var4 = this.effects.keySet().iterator();

            while(var4.hasNext()) {
               Integer potionEffect = (Integer)var4.next();
               entity.addPotionEffect(new PotionEffect(potionEffect.intValue(), 100, ((Integer)this.effects.get(potionEffect)).intValue()));
            }
         }
      }

      this.affected.clear();
   }
}

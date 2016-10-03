package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NBTTags;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobInterface;

public class JobGuard extends JobInterface {

   public List targets = new ArrayList();


   public JobGuard(EntityNPCInterface npc) {
      super(npc);
   }

   public boolean isEntityApplicable(Entity entity) {
      return !(entity instanceof EntityPlayer) && !(entity instanceof EntityNPCInterface)?this.targets.contains("entity." + EntityList.getEntityString(entity) + ".name"):false;
   }

   public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setTag("GuardTargets", NBTTags.nbtStringList(this.targets));
      return nbttagcompound;
   }

   public void readFromNBT(NBTTagCompound nbttagcompound) {
      this.targets = NBTTags.getStringList(nbttagcompound.getTagList("GuardTargets", 10));
      Iterator var2;
      Object entity;
      String name;
      Class cl;
      if(nbttagcompound.getBoolean("GuardAttackAnimals")) {
         var2 = EntityList.stringToClassMapping.keySet().iterator();

         while(var2.hasNext()) {
            entity = var2.next();
            name = "entity." + entity + ".name";
            cl = (Class)EntityList.stringToClassMapping.get(entity);
            if(EntityAnimal.class.isAssignableFrom(cl) && !this.targets.contains(name)) {
               this.targets.add(name);
            }
         }
      }

      if(nbttagcompound.getBoolean("GuardAttackMobs")) {
         var2 = EntityList.stringToClassMapping.keySet().iterator();

         while(var2.hasNext()) {
            entity = var2.next();
            name = "entity." + entity + ".name";
            cl = (Class)EntityList.stringToClassMapping.get(entity);
            if(EntityMob.class.isAssignableFrom(cl) && !EntityCreeper.class.isAssignableFrom(cl) && !this.targets.contains(name)) {
               this.targets.add(name);
            }
         }
      }

      if(nbttagcompound.getBoolean("GuardAttackCreepers")) {
         var2 = EntityList.stringToClassMapping.keySet().iterator();

         while(var2.hasNext()) {
            entity = var2.next();
            name = "entity." + entity + ".name";
            cl = (Class)EntityList.stringToClassMapping.get(entity);
            if(EntityCreeper.class.isAssignableFrom(cl) && !this.targets.contains(name)) {
               this.targets.add(name);
            }
         }
      }

   }
}

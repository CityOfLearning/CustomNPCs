package noppes.npcs;

import java.lang.reflect.Method;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelDataShared;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.entity.EntityNPCInterface;

public class ModelData extends ModelDataShared {

   public EntityLivingBase getEntity(EntityNPCInterface npc) {
      if(this.entityClass == null) {
         return null;
      } else {
         if(this.entity == null) {
            try {
               this.entity = (EntityLivingBase)this.entityClass.getConstructor(new Class[]{World.class}).newInstance(new Object[]{npc.worldObj});
               this.entity.readEntityFromNBT(this.extra);
               if(this.entity instanceof EntityLiving) {
                  EntityLiving e = (EntityLiving)this.entity;

                  for(int i = 0; i < 5; ++i) {
                     e.setCurrentItemOrArmor(0, npc.getEquipmentInSlot(i));
                  }
               }

               if(PixelmonHelper.isPixelmon(this.entity) && npc.worldObj.isRemote) {
                  if(this.extra.hasKey("Name")) {
                     PixelmonHelper.setName(this.entity, this.extra.getString("Name"));
                  } else {
                     PixelmonHelper.setName(this.entity, "Abra");
                  }
               }
            } catch (Exception var4) {
               ;
            }
         }

         return this.entity;
      }
   }

   public ModelData copy() {
      ModelData data = new ModelData();
      data.readFromNBT(this.writeToNBT());
      return data;
   }

   public void setExtra(EntityLivingBase entity, String key, String value) {
      key = key.toLowerCase();
      if(key.equals("breed") && EntityList.getEntityString(entity).equals("tgvstyle.Dog")) {
         try {
            Method e = entity.getClass().getMethod("getBreedID", new Class[0]);
            Enum breed = (Enum)e.invoke(entity, new Object[0]);
            e = entity.getClass().getMethod("setBreedID", new Class[]{breed.getClass()});
            e.invoke(entity, new Object[]{((Enum[])breed.getClass().getEnumConstants())[Integer.parseInt(value)]});
            NBTTagCompound comp = new NBTTagCompound();
            entity.writeEntityToNBT(comp);
            this.extra.setString("EntityData21", comp.getString("EntityData21"));
         } catch (Exception var7) {
            var7.printStackTrace();
         }
      }

      if(key.equalsIgnoreCase("name") && PixelmonHelper.isPixelmon(entity)) {
         this.extra.setString("Name", value);
      }

      this.clearEntity();
   }
}

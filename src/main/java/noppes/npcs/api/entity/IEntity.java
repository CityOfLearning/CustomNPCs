package noppes.npcs.api.entity;

import net.minecraft.entity.Entity;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.entity.data.IData;

public interface IEntity {

   double getX();

   void setX(double var1);

   double getY();

   void setY(double var1);

   double getZ();

   void setZ(double var1);

   int getBlockX();

   int getBlockY();

   int getBlockZ();

   void setPosition(double var1, double var3, double var5);

   void setRotation(float var1);

   float getRotation();

   IEntity getMount();

   void setMount(IEntity var1);

   IEntity getRider();

   void setRider(IEntity var1);

   void knockback(int var1, float var2);

   boolean isSneaking();

   boolean isSprinting();

   void dropItem(IItemStack var1);

   boolean inWater();

   boolean inFire();

   boolean inLava();

   IData getTempdata();

   IData getStoreddata();

   boolean isAlive();

   long getAge();

   void despawn();

   boolean isBurning();

   void setBurning(int var1);

   void extinguish();

   IWorld getWorld();

   String getTypeName();

   int getType();

   boolean typeOf(int var1);

   Entity getMCEntity();

   String getUUID();
}

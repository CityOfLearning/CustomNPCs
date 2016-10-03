package noppes.npcs.api.entity;

import net.minecraft.entity.EntityLiving;
import noppes.npcs.api.entity.IEntityLivingBase;

public interface IEntityLiving extends IEntityLivingBase {

   boolean isNavigating();

   void clearNavigation();

   void navigateTo(double var1, double var3, double var5, double var7);

   EntityLiving getMCEntity();
}

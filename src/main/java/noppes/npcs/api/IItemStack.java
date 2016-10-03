package noppes.npcs.api;

import net.minecraft.item.ItemStack;
import noppes.npcs.api.entity.IEntityLiving;

public interface IItemStack {

   int getStackSize();

   void setStackSize(int var1);

   int getItemDamage();

   void setItemDamage(int var1);

   void damageItem(int var1, IEntityLiving var2);

   void setTag(String var1, Object var2);

   boolean hasTag(String var1);

   Object getTag(String var1);

   boolean isEnchanted();

   boolean hasEnchant(int var1);

   boolean isBlock();

   boolean hasCustomName();

   void setCustomName(String var1);

   String getDisplayName();

   String getItemName();

   String getName();

   ItemStack getMCItemStack();
}

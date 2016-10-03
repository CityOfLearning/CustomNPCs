package noppes.npcs.roles;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.entity.data.INPCJob;
import noppes.npcs.entity.EntityNPCInterface;

public abstract class JobInterface implements INPCJob {

   public EntityNPCInterface npc;
   public boolean overrideMainHand = false;
   public boolean overrideOffHand = false;
   protected IItemStack mainhand = null;
   protected IItemStack offhand = null;


   public JobInterface(EntityNPCInterface npc) {
      this.npc = npc;
   }

   public abstract NBTTagCompound writeToNBT(NBTTagCompound var1);

   public abstract void readFromNBT(NBTTagCompound var1);

   public void killed() {}

   public void delete() {}

   public boolean aiShouldExecute() {
      return false;
   }

   public boolean aiContinueExecute() {
      return this.aiShouldExecute();
   }

   public void aiStartExecuting() {}

   public void aiUpdateTask() {}

   public void reset() {}

   public void resetTask() {}

   public IItemStack getMainhand() {
      return this.mainhand;
   }

   public IItemStack getOffhand() {
      return this.offhand;
   }

   public int getMutexBits() {
      return 0;
   }

   public ItemStack stringToItem(String s) {
      if(s.isEmpty()) {
         return null;
      } else {
         int damage = 0;
         if(s.contains(" - ")) {
            String[] item = s.split(" - ");
            if(item.length == 2) {
               try {
                  damage = Integer.parseInt(item[1]);
               } catch (NumberFormatException var5) {
                  ;
               }

               s = item[0];
            }
         }

         Item item1 = Item.getByNameOrId(s);
         return item1 == null?null:new ItemStack(item1, 1, damage);
      }
   }

   public String itemToString(ItemStack item) {
      return item != null && item.getItem() != null?Item.itemRegistry.getNameForObject(item.getItem()) + " - " + item.getItemDamage():null;
   }

   public int getType() {
      return this.npc.advanced.job;
   }
}

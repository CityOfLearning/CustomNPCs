package noppes.npcs.api.wrapper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTBase.NBTPrimitive;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.entity.IEntityLiving;

public class ItemStackWrapper implements IItemStack {

   public ItemStack item;


   public ItemStackWrapper(ItemStack item) {
      this.item = item;
   }

   public int getStackSize() {
      return this.item.stackSize;
   }

   public void setStackSize(int size) {
      if(size < 0) {
         size = 1;
      } else if(size > 64) {
         size = 64;
      }

      this.item.stackSize = size;
   }

   public int getItemDamage() {
      return this.item.getItemDamage();
   }

   public void setItemDamage(int value) {
      this.item.setItemDamage(value);
   }

   public void setTag(String key, Object value) {
      if(value instanceof Number) {
         this.getTag().setDouble(key, ((Number)value).doubleValue());
      } else if(value instanceof String) {
         this.getTag().setString(key, (String)value);
      }

   }

   public boolean hasTag(String key) {
      return this.getTag().hasKey(key);
   }

   public Object getTag(String key) {
      NBTBase tag = this.getTag().getTag(key);
      return tag == null?null:(tag instanceof NBTPrimitive?Double.valueOf(((NBTPrimitive)tag).getDouble()):(tag instanceof NBTTagString?((NBTTagString)tag).getString():tag));
   }

   public boolean isEnchanted() {
      return this.item.isItemEnchanted();
   }

   public boolean hasEnchant(int id) {
      if(!this.isEnchanted()) {
         return false;
      } else {
         NBTTagList list = this.item.getEnchantmentTagList();

         for(int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound compound = list.getCompoundTagAt(i);
            if(compound.getShort("id") == id) {
               return true;
            }
         }

         return false;
      }
   }

   private NBTTagCompound getTag() {
      if(!this.item.hasTagCompound()) {
         this.item.setTagCompound(new NBTTagCompound());
      }

      return this.item.getTagCompound();
   }

   public boolean isBlock() {
      Block block = Block.getBlockFromItem(this.item.getItem());
      return block != null && block != Blocks.air;
   }

   public boolean hasCustomName() {
      return this.item.hasDisplayName();
   }

   public void setCustomName(String name) {
      this.item.setStackDisplayName(name);
   }

   public String getDisplayName() {
      return this.item.getDisplayName();
   }

   public String getItemName() {
      return this.item.getItem().getItemStackDisplayName(this.item);
   }

   public String getName() {
      return Item.itemRegistry.getNameForObject(this.item.getItem()) + "";
   }

   public ItemStack getMCItemStack() {
      return this.item;
   }

   public static ItemStack MCItem(IItemStack item) {
      return item == null?null:item.getMCItemStack();
   }

   public void damageItem(int damage, IEntityLiving living) {
      this.item.damageItem(damage, living == null?null:living.getMCEntity());
   }
}

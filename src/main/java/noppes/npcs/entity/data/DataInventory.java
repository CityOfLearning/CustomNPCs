package noppes.npcs.entity.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.entity.data.INPCInventory;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.ValueUtil;

public class DataInventory implements IInventory, INPCInventory {

   public Map drops = new HashMap();
   public Map dropchance = new HashMap();
   public Map weapons = new HashMap();
   public Map armor = new HashMap();
   private int minExp = 0;
   private int maxExp = 0;
   public int lootMode = 0;
   private EntityNPCInterface npc;
   public ItemStack renderOffhand = null;


   public DataInventory(EntityNPCInterface npc) {
      this.npc = npc;
   }

   public NBTTagCompound writeEntityToNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setInteger("MinExp", this.minExp);
      nbttagcompound.setInteger("MaxExp", this.maxExp);
      nbttagcompound.setTag("NpcInv", NBTTags.nbtIItemStackMap(this.drops));
      nbttagcompound.setTag("Armor", NBTTags.nbtIItemStackMap(this.armor));
      nbttagcompound.setTag("Weapons", NBTTags.nbtIItemStackMap(this.weapons));
      nbttagcompound.setTag("DropChance", NBTTags.nbtIntegerIntegerMap(this.dropchance));
      nbttagcompound.setInteger("LootMode", this.lootMode);
      return nbttagcompound;
   }

   public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
      this.minExp = nbttagcompound.getInteger("MinExp");
      this.maxExp = nbttagcompound.getInteger("MaxExp");
      this.drops = NBTTags.getIItemStackMap(nbttagcompound.getTagList("NpcInv", 10));
      this.armor = NBTTags.getIItemStackMap(nbttagcompound.getTagList("Armor", 10));
      this.weapons = NBTTags.getIItemStackMap(nbttagcompound.getTagList("Weapons", 10));
      this.dropchance = NBTTags.getIntegerIntegerMap(nbttagcompound.getTagList("DropChance", 10));
      this.lootMode = nbttagcompound.getInteger("LootMode");
   }

   public IItemStack getArmor(int slot) {
      return (IItemStack)this.armor.get(Integer.valueOf(slot));
   }

   public void setArmor(int slot, IItemStack item) {
      this.armor.put(Integer.valueOf(slot), item);
      this.npc.updateClient = true;
   }

   public IItemStack getRightHand() {
      return (IItemStack)this.weapons.get(Integer.valueOf(0));
   }

   public void setRightHand(IItemStack item) {
      this.weapons.put(Integer.valueOf(0), item);
      this.npc.updateClient = true;
   }

   public IItemStack getProjectile() {
      return (IItemStack)this.weapons.get(Integer.valueOf(1));
   }

   public void setProjectile(IItemStack item) {
      this.weapons.put(Integer.valueOf(1), item);
      this.npc.updateAI = true;
   }

   public IItemStack getLeftHand() {
      return (IItemStack)this.weapons.get(Integer.valueOf(2));
   }

   public void setLeftHand(IItemStack item) {
      this.weapons.put(Integer.valueOf(2), item);
      this.npc.updateClient = true;
   }

   public IItemStack getDropItem(int slot) {
      if(slot >= 0 && slot <= 8) {
         IItemStack item = (IItemStack)this.npc.inventory.drops.get(Integer.valueOf(slot));
         return item == null?null:new ItemStackWrapper(item.getMCItemStack());
      } else {
         throw new CustomNPCsException("Bad slot number: " + slot, new Object[0]);
      }
   }

   public void setDropItem(int slot, IItemStack item, int chance) {
      if(slot >= 0 && slot <= 8) {
         chance = ValueUtil.CorrectInt(chance, 1, 100);
         if(item == null) {
            this.dropchance.remove(Integer.valueOf(slot));
            this.drops.remove(Integer.valueOf(slot));
         } else {
            this.dropchance.put(Integer.valueOf(slot), Integer.valueOf(chance));
            this.drops.put(Integer.valueOf(slot), item);
         }

      } else {
         throw new CustomNPCsException("Bad slot number: " + slot, new Object[0]);
      }
   }

   public void dropStuff(Entity entity, DamageSource damagesource) {
      ArrayList list = new ArrayList();
      Iterator enchant = this.drops.keySet().iterator();

      int exp;
      while(enchant.hasNext()) {
         exp = ((Integer)enchant.next()).intValue();
         IItemStack var2 = (IItemStack)this.drops.get(Integer.valueOf(exp));
         if(var2 != null) {
            int player = 100;
            if(this.dropchance.containsKey(Integer.valueOf(exp))) {
               player = ((Integer)this.dropchance.get(Integer.valueOf(exp))).intValue();
            }

            int stack = this.npc.worldObj.rand.nextInt(100) + player;
            if(stack >= 100) {
               EntityItem i = this.getEntityItem(var2.getMCItemStack().copy());
               if(i != null) {
                  list.add(i);
               }
            }
         }
      }

      int enchant1 = 0;
      if(damagesource.getSourceOfDamage() instanceof EntityPlayer) {
         enchant1 = EnchantmentHelper.getLootingModifier((EntityLivingBase)damagesource.getSourceOfDamage());
      }

      if(!ForgeHooks.onLivingDrops(this.npc, damagesource, list, enchant1, true)) {
         Iterator exp1 = list.iterator();

         while(exp1.hasNext()) {
            EntityItem var21 = (EntityItem)exp1.next();
            if(this.lootMode == 1 && entity instanceof EntityPlayer) {
               EntityPlayer player1 = (EntityPlayer)entity;
               var21.setPickupDelay(2);
               this.npc.worldObj.spawnEntityInWorld(var21);
               ItemStack stack1 = var21.getEntityItem();
               int i1 = stack1.stackSize;
               if(player1.inventory.addItemStackToInventory(stack1)) {
                  this.npc.worldObj.playSoundAtEntity(var21, "random.pop", 0.2F, ((this.npc.getRNG().nextFloat() - this.npc.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                  player1.onItemPickup(var21, i1);
                  if(stack1.stackSize <= 0) {
                     var21.setDead();
                  }
               }
            } else {
               this.npc.worldObj.spawnEntityInWorld(var21);
            }
         }
      }

      exp = this.getExpRNG();

      while(exp > 0) {
         int var22 = EntityXPOrb.getXPSplit(exp);
         exp -= var22;
         if(this.lootMode == 1 && entity instanceof EntityPlayer) {
            this.npc.worldObj.spawnEntityInWorld(new EntityXPOrb(entity.worldObj, entity.posX, entity.posY, entity.posZ, var22));
         } else {
            this.npc.worldObj.spawnEntityInWorld(new EntityXPOrb(this.npc.worldObj, this.npc.posX, this.npc.posY, this.npc.posZ, var22));
         }
      }

   }

   public EntityItem getEntityItem(ItemStack itemstack) {
      if(itemstack == null) {
         return null;
      } else {
         EntityItem entityitem = new EntityItem(this.npc.worldObj, this.npc.posX, this.npc.posY - 0.30000001192092896D + (double)this.npc.getEyeHeight(), this.npc.posZ, itemstack);
         entityitem.setPickupDelay(40);
         float f2 = this.npc.getRNG().nextFloat() * 0.5F;
         float f4 = this.npc.getRNG().nextFloat() * 3.141593F * 2.0F;
         entityitem.motionX = (double)(-MathHelper.sin(f4) * f2);
         entityitem.motionZ = (double)(MathHelper.cos(f4) * f2);
         entityitem.motionY = 0.20000000298023224D;
         return entityitem;
      }
   }

   public int getSizeInventory() {
      return 15;
   }

   public ItemStack getStackInSlot(int i) {
      return i < 4?ItemStackWrapper.MCItem(this.getArmor(i)):(i < 7?ItemStackWrapper.MCItem((IItemStack)this.weapons.get(Integer.valueOf(i - 4))):ItemStackWrapper.MCItem((IItemStack)this.drops.get(Integer.valueOf(i - 7))));
   }

   public ItemStack decrStackSize(int par1, int par2) {
      byte i = 0;
      Map var3;
      if(par1 >= 7) {
         var3 = this.drops;
         par1 -= 7;
      } else if(par1 >= 4) {
         var3 = this.weapons;
         par1 -= 4;
         i = 1;
      } else {
         var3 = this.armor;
         i = 2;
      }

      ItemStack var4 = null;
      if(var3.get(Integer.valueOf(par1)) != null) {
         if(((IItemStack)var3.get(Integer.valueOf(par1))).getMCItemStack().stackSize <= par2) {
            var4 = ((IItemStack)var3.get(Integer.valueOf(par1))).getMCItemStack();
            var3.put(Integer.valueOf(par1), (Object)null);
         } else {
            var4 = ((IItemStack)var3.get(Integer.valueOf(par1))).getMCItemStack().splitStack(par2);
            if(((IItemStack)var3.get(Integer.valueOf(par1))).getMCItemStack().stackSize == 0) {
               var3.put(Integer.valueOf(par1), (Object)null);
            }
         }
      }

      if(i == 1) {
         this.weapons = var3;
      }

      if(i == 2) {
         this.armor = var3;
      }

      return var4;
   }

   public ItemStack removeStackFromSlot(int par1) {
      byte i = 0;
      Map var2;
      if(par1 >= 7) {
         var2 = this.drops;
         par1 -= 7;
      } else if(par1 >= 4) {
         var2 = this.weapons;
         par1 -= 4;
         i = 1;
      } else {
         var2 = this.armor;
         i = 2;
      }

      if(var2.get(Integer.valueOf(par1)) != null) {
         ItemStack var3 = ((IItemStack)var2.get(Integer.valueOf(par1))).getMCItemStack();
         var2.put(Integer.valueOf(par1), (Object)null);
         if(i == 1) {
            this.weapons = var2;
         }

         if(i == 2) {
            this.armor = var2;
         }

         return var3;
      } else {
         return null;
      }
   }

   public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
      byte i = 0;
      Map var3;
      if(par1 >= 7) {
         var3 = this.drops;
         par1 -= 7;
      } else if(par1 >= 4) {
         var3 = this.weapons;
         par1 -= 4;
         i = 1;
      } else {
         var3 = this.armor;
         i = 2;
      }

      if(par2ItemStack == null) {
         var3.put(Integer.valueOf(par1), (Object)null);
      } else {
         var3.put(Integer.valueOf(par1), new ItemStackWrapper(par2ItemStack));
      }

      if(i == 1) {
         this.weapons = var3;
      }

      if(i == 2) {
         this.armor = var3;
      }

   }

   public int getInventoryStackLimit() {
      return 64;
   }

   public boolean isUseableByPlayer(EntityPlayer var1) {
      return true;
   }

   public boolean isItemValidForSlot(int i, ItemStack itemstack) {
      return true;
   }

   public String getName() {
      return "NPC Inventory";
   }

   public void markDirty() {}

   public boolean hasCustomName() {
      return true;
   }

   public IChatComponent getDisplayName() {
      return null;
   }

   public void openInventory(EntityPlayer player) {}

   public void closeInventory(EntityPlayer player) {}

   public int getField(int id) {
      return 0;
   }

   public void setField(int id, int value) {}

   public int getFieldCount() {
      return 0;
   }

   public void clear() {}

   public int getExpMin() {
      return this.npc.inventory.minExp;
   }

   public int getExpMax() {
      return this.npc.inventory.maxExp;
   }

   public int getExpRNG() {
      int exp = this.minExp;
      if(this.maxExp - this.minExp > 0) {
         exp += this.npc.worldObj.rand.nextInt(this.maxExp - this.minExp);
      }

      return exp;
   }

   public void setExp(int min, int max) {
      this.minExp = Math.min(this.minExp, this.maxExp);
      this.npc.inventory.minExp = this.minExp;
      this.npc.inventory.maxExp = this.maxExp;
   }
}

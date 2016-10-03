package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.api.event.RoleEvent;
import noppes.npcs.containers.ContainerNpcInterface;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleTrader;

public class ContainerNPCTrader extends ContainerNpcInterface {

   public RoleTrader role;
   private EntityNPCInterface npc;


   public ContainerNPCTrader(EntityNPCInterface npc, EntityPlayer player) {
      super(player);
      this.npc = npc;
      this.role = (RoleTrader)npc.roleInterface;

      int j1;
      int var8;
      for(j1 = 0; j1 < 18; ++j1) {
         byte l1 = 53;
         var8 = l1 + j1 % 3 * 72;
         byte y = 7;
         int var9 = y + j1 / 3 * 21;
         ItemStack item = (ItemStack)this.role.inventoryCurrency.items.get(Integer.valueOf(j1));
         ItemStack item2 = (ItemStack)this.role.inventoryCurrency.items.get(Integer.valueOf(j1 + 18));
         if(item == null) {
            item2 = null;
         }

         this.addSlotToContainer(new Slot(this.role.inventorySold, j1, var8, var9));
      }

      for(j1 = 0; j1 < 3; ++j1) {
         for(var8 = 0; var8 < 9; ++var8) {
            this.addSlotToContainer(new Slot(player.inventory, var8 + j1 * 9 + 9, 32 + var8 * 18, 140 + j1 * 18));
         }
      }

      for(j1 = 0; j1 < 9; ++j1) {
         this.addSlotToContainer(new Slot(player.inventory, j1, 32 + j1 * 18, 198));
      }

   }

   public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i) {
      return null;
   }

   public ItemStack slotClick(int i, int j, int par3, EntityPlayer entityplayer) {
      if(par3 == 6) {
         par3 = 0;
      }

      if(i >= 0 && i < 18) {
         if(j == 1) {
            return null;
         } else {
            Slot slot = (Slot)this.inventorySlots.get(i);
            if(slot != null && slot.getStack() != null) {
               ItemStack item = slot.getStack();
               if(!this.canGivePlayer(item, entityplayer)) {
                  return null;
               } else {
                  ItemStack currency = this.role.inventoryCurrency.getStackInSlot(i);
                  ItemStack currency2 = this.role.inventoryCurrency.getStackInSlot(i + 18);
                  if(!this.canBuy(currency, currency2, entityplayer)) {
                     return null;
                  } else {
                     RoleEvent.TraderEvent event = new RoleEvent.TraderEvent(entityplayer, this.npc.wrappedNPC, item, currency, currency2);
                     if(EventHooks.onNPCRole(this.npc, event)) {
                        return null;
                     } else {
                        if(event.currency1 != null) {
                           currency = event.currency1.getMCItemStack();
                        }

                        if(event.currency2 != null) {
                           currency2 = event.currency2.getMCItemStack();
                        }

                        if(!this.canBuy(currency, currency2, entityplayer)) {
                           return null;
                        } else {
                           NoppesUtilPlayer.consumeItem(entityplayer, currency, this.role.ignoreDamage, this.role.ignoreNBT);
                           NoppesUtilPlayer.consumeItem(entityplayer, currency2, this.role.ignoreDamage, this.role.ignoreNBT);
                           ItemStack soldItem = null;
                           if(event.sold != null) {
                              soldItem = event.sold.getMCItemStack();
                              this.givePlayer(soldItem, entityplayer);
                           }

                           return soldItem;
                        }
                     }
                  }
               }
            } else {
               return null;
            }
         }
      } else {
         return super.slotClick(i, j, par3, entityplayer);
      }
   }

   public boolean canBuy(ItemStack currency, ItemStack currency2, EntityPlayer player) {
      if(currency == null && currency2 == null) {
         return true;
      } else {
         if(currency == null) {
            currency = currency2;
            currency2 = null;
         }

         if(NoppesUtilPlayer.compareItems(currency, currency2, this.role.ignoreDamage, this.role.ignoreNBT)) {
            currency = currency.copy();
            currency.stackSize += currency2.stackSize;
            currency2 = null;
         }

         return currency2 == null?NoppesUtilPlayer.compareItems(player, currency, this.role.ignoreDamage, this.role.ignoreNBT):NoppesUtilPlayer.compareItems(player, currency, this.role.ignoreDamage, this.role.ignoreNBT) && NoppesUtilPlayer.compareItems(player, currency2, this.role.ignoreDamage, this.role.ignoreNBT);
      }
   }

   private boolean canGivePlayer(ItemStack item, EntityPlayer entityplayer) {
      ItemStack itemstack3 = entityplayer.inventory.getItemStack();
      if(itemstack3 == null) {
         return true;
      } else {
         if(NoppesUtilPlayer.compareItems(itemstack3, item, false, false)) {
            int k1 = item.stackSize;
            if(k1 > 0 && k1 + itemstack3.stackSize <= itemstack3.getMaxStackSize()) {
               return true;
            }
         }

         return false;
      }
   }

   private void givePlayer(ItemStack item, EntityPlayer entityplayer) {
      ItemStack itemstack3 = entityplayer.inventory.getItemStack();
      if(itemstack3 == null) {
         entityplayer.inventory.setItemStack(item);
      } else if(NoppesUtilPlayer.compareItems(itemstack3, item, false, false)) {
         int k1 = item.stackSize;
         if(k1 > 0 && k1 + itemstack3.stackSize <= itemstack3.getMaxStackSize()) {
            itemstack3.stackSize += k1;
         }
      }

   }
}

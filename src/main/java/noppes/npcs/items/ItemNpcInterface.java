package noppes.npcs.items;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.items.ItemRenderInterface;

public class ItemNpcInterface extends Item implements ItemRenderInterface {

   private boolean damageAble;


   public ItemNpcInterface(int par1) {
      this();
   }

   public ItemNpcInterface() {
      this.damageAble = true;
      this.setCreativeTab(CustomItems.tab);
   }

   public void setUnDamageable() {
      this.damageAble = false;
   }

   public void renderSpecial() {
      GlStateManager.scale(0.66F, 0.66F, 0.66F);
      GlStateManager.translate(0.0F, 0.3F, 0.0F);
   }

   public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
      subItems.add(new ItemStack(itemIn, 1, 0));
   }

   public int getItemEnchantability() {
      return super.getItemEnchantability();
   }

   public Item setUnlocalizedName(String name) {
      super.setUnlocalizedName(name);
      GameRegistry.registerItem(this, name);
      if(this.hasSubtypes) {
         ArrayList list = new ArrayList();
         this.getSubItems(this, (CreativeTabs)null, list);
         Iterator var3 = list.iterator();

         while(var3.hasNext()) {
            ItemStack stack = (ItemStack)var3.next();
            CustomNpcs.proxy.registerItem(this, name, stack.getItemDamage());
         }
      } else {
         CustomNpcs.proxy.registerItem(this, name, 0);
      }

      return this;
   }

   public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving) {
      if(par2EntityLiving.getHealth() <= 0.0F) {
         return false;
      } else {
         if(this.damageAble) {
            par1ItemStack.damageItem(1, par3EntityLiving);
         }

         return true;
      }
   }

   public boolean hasItem(EntityPlayer player, Item item) {
      return player.inventory.hasItem(item);
   }

   public boolean consumeItem(EntityPlayer player, Item item) {
      return player.inventory.consumeInventoryItem(item);
   }
}

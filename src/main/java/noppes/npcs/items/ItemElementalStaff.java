package noppes.npcs.items;

import java.awt.Color;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.constants.EnumNpcToolMaterial;
import noppes.npcs.items.ItemStaff;

public class ItemElementalStaff extends ItemStaff {

   public ItemElementalStaff(int par1, EnumNpcToolMaterial material) {
      super(par1, material);
      this.setHasSubtypes(true);
   }

   public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
      float[] color = EntitySheep.getDyeRgb(EnumDyeColor.byDyeDamage(par1ItemStack.getItemDamage()));
      return (new Color(color[0], color[1], color[2])).getRGB();
   }

   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      for(int var4 = 0; var4 < 16; ++var4) {
         par3List.add(new ItemStack(par1, 1, var4));
      }

   }

   public ItemStack getProjectile(ItemStack stack) {
      return new ItemStack(CustomItems.orb, 1, stack.getItemDamage());
   }

   public void spawnParticle(ItemStack stack, EntityPlayer player) {
      CustomNpcs.proxy.spawnParticle(player, "Spell", new Object[]{Integer.valueOf(stack.getItemDamage()), Integer.valueOf(4)});
   }
}

package noppes.npcs.items;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;

public class ItemNpcArmor extends ItemArmor {

   private String texture;


   public ItemNpcArmor(int par1, ArmorMaterial par2EnumArmorMaterial, int par4, String texture) {
      super(par2EnumArmorMaterial, 0, par4);
      this.texture = texture;
      this.setCreativeTab(CustomItems.tabArmor);
   }

   public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
      return this.armorType == 2?"customnpcs:textures/armor/" + this.texture + "_2.png":"customnpcs:textures/armor/" + this.texture + "_1.png";
   }

   public Item setUnlocalizedName(String name) {
      GameRegistry.registerItem(this, name);
      CustomNpcs.proxy.registerItem(this, name, 0);
      return super.setUnlocalizedName(name);
   }
}

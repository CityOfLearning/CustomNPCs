package noppes.npcs.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemPlaceholder extends ItemBlock {

   public ItemPlaceholder(Block p_i45328_1_) {
      super(p_i45328_1_);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
   }

   public int getMetadata(int damage) {
      return damage;
   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      return super.getUnlocalizedName(par1ItemStack) + "_" + par1ItemStack.getItemDamage();
   }
}

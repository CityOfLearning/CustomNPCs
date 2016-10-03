package noppes.npcs.items;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import noppes.npcs.CustomItems;
import noppes.npcs.items.ItemNpcInterface;

public class ItemWand extends ItemNpcInterface {

   public ItemWand(int par1) {
      super(par1);
      this.setCreativeTab(CustomItems.tabMisc);
   }

   public boolean hasEffect(ItemStack par1ItemStack) {
      return true;
   }

   public void renderSpecial() {
      GlStateManager.scale(0.54F, 0.54F, 0.54F);
      GlStateManager.translate(0.1F, 0.5F, 0.1F);
   }
}

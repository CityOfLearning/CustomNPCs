package noppes.npcs.items;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item.ToolMaterial;
import noppes.npcs.items.ItemNpcWeaponInterface;

public class ItemDagger extends ItemNpcWeaponInterface {

   public ItemDagger(int par1, ToolMaterial tool) {
      super(par1, tool);
   }

   public void renderSpecial() {
      GlStateManager.scale(0.6F, 0.6F, 0.6F);
      GlStateManager.translate(0.14F, 0.22F, 0.06F);
   }
}

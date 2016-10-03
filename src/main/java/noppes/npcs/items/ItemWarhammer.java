package noppes.npcs.items;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item.ToolMaterial;
import noppes.npcs.items.ItemNpcWeaponInterface;

public class ItemWarhammer extends ItemNpcWeaponInterface {

   public ItemWarhammer(int par1, ToolMaterial tool) {
      super(par1, tool);
   }

   public void renderSpecial() {
      GlStateManager.scale(1.2F, 1.4F, 1.0F);
      GlStateManager.translate(0.2F, -0.08F, 0.08F);
   }
}

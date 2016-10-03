package noppes.npcs.items;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item.ToolMaterial;
import noppes.npcs.items.ItemNpcWeaponInterface;

public class ItemGlaive extends ItemNpcWeaponInterface {

   public ItemGlaive(int par1, ToolMaterial tool) {
      super(par1, tool);
   }

   public void renderSpecial() {
      GlStateManager.translate(0.03F, -0.4F, 0.08F);
   }
}

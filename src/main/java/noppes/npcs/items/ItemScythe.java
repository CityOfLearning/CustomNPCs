package noppes.npcs.items;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item.ToolMaterial;
import noppes.npcs.items.ItemNpcWeaponInterface;

public class ItemScythe extends ItemNpcWeaponInterface {

   public ItemScythe(int par1, ToolMaterial tool) {
      super(par1, tool);
   }

   public void renderSpecial() {
      GlStateManager.scale(1.0F, 1.3F, 1.0F);
      GlStateManager.translate(0.0F, -0.2F, -0.16F);
      GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
   }
}

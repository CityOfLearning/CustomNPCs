package noppes.npcs.items;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item.ToolMaterial;
import noppes.npcs.items.ItemNpcWeaponInterface;

public class ItemLeafBlade extends ItemNpcWeaponInterface {

   public ItemLeafBlade(int par1, ToolMaterial tool) {
      super(par1, tool);
   }

   public void renderSpecial() {
      GlStateManager.scale(0.8F, 0.8F, 0.8F);
      GlStateManager.translate(-0.2F, 0.28F, -0.12F);
      GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-16.0F, 0.0F, 0.0F, 1.0F);
   }
}

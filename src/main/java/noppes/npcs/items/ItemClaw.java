package noppes.npcs.items;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item.ToolMaterial;
import noppes.npcs.items.ItemNpcWeaponInterface;

public class ItemClaw extends ItemNpcWeaponInterface {

   public ItemClaw(int par1, ToolMaterial material) {
      super(par1, material);
   }

   public void renderSpecial() {
      GlStateManager.scale(0.6F, 0.6F, 0.6F);
      GlStateManager.translate(-0.6F, 0.2F, -0.2F);
      GlStateManager.rotate(90.0F, 0.0F, 0.0F, -1.0F);
      GlStateManager.rotate(6.0F, 1.0F, 0.0F, 0.0F);
   }
}

package noppes.npcs.items;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item.ToolMaterial;
import noppes.npcs.items.ItemNpcWeaponInterface;

public class ItemBattleAxe extends ItemNpcWeaponInterface {

   public ItemBattleAxe(int par1, ToolMaterial tool) {
      super(par1, tool);
   }

   public void renderSpecial() {
      GlStateManager.scale(1.0F, 0.8F, 1.0F);
      GlStateManager.translate(-0.04F, 0.2F, -0.16F);
      GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
   }
}

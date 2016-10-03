package noppes.npcs.items;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item.ToolMaterial;
import noppes.npcs.items.ItemKunai;

public class ItemKunaiReversed extends ItemKunai {

   public ItemKunaiReversed(int par1, ToolMaterial tool) {
      super(par1, tool);
   }

   public void renderSpecial() {
      GlStateManager.scale(0.4F, 0.4F, 0.4F);
      GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.translate(-0.4F, -0.9F, 0.2F);
   }
}

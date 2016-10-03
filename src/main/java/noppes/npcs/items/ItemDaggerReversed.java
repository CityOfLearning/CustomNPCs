package noppes.npcs.items;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item.ToolMaterial;
import noppes.npcs.items.ItemDagger;

public class ItemDaggerReversed extends ItemDagger {

   private ItemDagger dagger;


   public ItemDaggerReversed(int par1, ItemDagger dagger, ToolMaterial tool) {
      super(par1, tool);
      this.dagger = dagger;
   }

   public void renderSpecial() {
      GlStateManager.scale(0.6F, 0.6F, 0.6F);
      GlStateManager.translate(0.16F, 0.6F, -0.16F);
      GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
   }
}

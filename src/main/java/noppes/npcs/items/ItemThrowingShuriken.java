package noppes.npcs.items;

import net.minecraft.client.renderer.GlStateManager;
import noppes.npcs.items.ItemThrowingWeapon;

public class ItemThrowingShuriken extends ItemThrowingWeapon {

   public ItemThrowingShuriken(int par1) {
      super(par1);
   }

   public void renderSpecial() {
      GlStateManager.scale(0.5F, 0.5F, 0.5F);
      GlStateManager.translate(-0.1F, 0.3F, 0.0F);
   }

   public boolean shouldRotateAroundWhenRendering() {
      return true;
   }
}

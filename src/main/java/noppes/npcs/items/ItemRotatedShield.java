package noppes.npcs.items;

import net.minecraft.client.renderer.GlStateManager;
import noppes.npcs.constants.EnumNpcToolMaterial;
import noppes.npcs.items.ItemShield;

public class ItemRotatedShield extends ItemShield {

   public ItemRotatedShield(int par1, EnumNpcToolMaterial material) {
      super(par1, material);
   }

   public void renderSpecial() {
      GlStateManager.scale(0.6F, 0.6F, 0.6F);
      GlStateManager.translate(0.4F, 1.0F, -0.18F);
      GlStateManager.rotate(-6.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(120.0F, 0.0F, 0.0F, 1.0F);
   }
}

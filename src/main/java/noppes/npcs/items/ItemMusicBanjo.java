package noppes.npcs.items;

import net.minecraft.client.renderer.GlStateManager;
import noppes.npcs.items.ItemMusic;

public class ItemMusicBanjo extends ItemMusic {

   public void renderSpecial() {
      GlStateManager.scale(0.85F, 0.85F, 0.85F);
      GlStateManager.translate(0.1F, 0.4F, -0.14F);
      GlStateManager.rotate(-90.0F, -1.0F, 0.0F, 0.0F);
   }
}


package noppes.npcs.items;

import net.minecraft.client.renderer.GlStateManager;

public class ItemMusicBanjo extends ItemMusic {
	@Override
	public void renderSpecial() {
		GlStateManager.scale(0.85f, 0.85f, 0.85f);
		GlStateManager.translate(0.1f, 0.4f, -0.14f);
		GlStateManager.rotate(-90.0f, -1.0f, 0.0f, 0.0f);
	}
}

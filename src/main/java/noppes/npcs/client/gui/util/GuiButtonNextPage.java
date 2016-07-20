//

//

package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonNextPage extends GuiNpcButton {
	private static ResourceLocation field_110405_a;
	static {
		field_110405_a = new ResourceLocation("textures/gui/book.png");
	}

	private boolean field_146151_o;

	public GuiButtonNextPage(int par1, int par2, int par3, boolean par4) {
		super(par1, par2, par3, 23, 13, "");
		field_146151_o = par4;
	}

	@Override
	public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
		if (visible) {
			boolean flag = (p_146112_2_ >= xPosition) && (p_146112_3_ >= yPosition)
					&& (p_146112_2_ < (xPosition + width)) && (p_146112_3_ < (yPosition + height));
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			p_146112_1_.getTextureManager().bindTexture(GuiButtonNextPage.field_110405_a);
			int k = 0;
			int l = 192;
			if (flag) {
				k += 23;
			}
			if (!field_146151_o) {
				l += 13;
			}
			this.drawTexturedModalRect(xPosition, yPosition, k, l, 23, 13);
		}
	}
}

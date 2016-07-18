//

//

package noppes.npcs.client.gui.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiHoverText extends GuiScreen {
	protected static final ResourceLocation buttonTextures;
	static {
		buttonTextures = new ResourceLocation("customnpcs:textures/gui/info.png");
	}
	private int x;
	private int y;
	public int id;

	private String text;

	public GuiHoverText(final int id, final String text, final int x, final int y) {
		this.text = text;
		this.id = id;
		this.x = x;
		this.y = y;
	}

	@Override
	public void drawScreen(final int par1, final int par2, final float par3) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(GuiHoverText.buttonTextures);
		this.drawTexturedModalRect(x, y, 0, 0, 12, 12);
		if (inArea(x, y, 12, 12, par1, par2)) {
			final List<String> lines = new ArrayList<String>();
			lines.add(text);
			this.drawHoveringText(lines, x + 8, y + 6, fontRendererObj);
			GlStateManager.disableLighting();
		}
	}

	public boolean inArea(final int x, final int y, final int width, final int height, final int mouseX,
			final int mouseY) {
		return (mouseX >= x) && (mouseX <= (x + width)) && (mouseY >= y) && (mouseY <= (y + height));
	}
}

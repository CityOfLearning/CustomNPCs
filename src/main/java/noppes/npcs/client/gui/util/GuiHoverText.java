
package noppes.npcs.client.gui.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiHoverText extends GuiScreen {
	protected static ResourceLocation buttonTextures;
	static {
		buttonTextures = new ResourceLocation("customnpcs:textures/gui/info.png");
	}
	private int x;
	private int y;
	public int id;

	private String text;

	public GuiHoverText(int id, String text, int x, int y) {
		this.text = text;
		this.id = id;
		this.x = x;
		this.y = y;
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(GuiHoverText.buttonTextures);
		this.drawTexturedModalRect(x, y, 0, 0, 12, 12);
		if (inArea(x, y, 12, 12, par1, par2)) {
			List<String> lines = new ArrayList<>();
			lines.add(text);
			this.drawHoveringText(lines, x + 8, y + 6, fontRendererObj);
			GlStateManager.disableLighting();
		}
	}

	public boolean inArea(int x, int y, int width, int height, int mouseX, int mouseY) {
		return (mouseX >= x) && (mouseX <= (x + width)) && (mouseY >= y) && (mouseY <= (y + height));
	}
}

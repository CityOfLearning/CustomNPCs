//

//

package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiMenuSideButton extends GuiNpcButton {
	public static ResourceLocation resource;
	static {
		resource = new ResourceLocation("customnpcs", "textures/gui/menusidebutton.png");
	}

	public boolean active;

	public GuiMenuSideButton(int id, int x, int y, int width, int height, String s) {
		super(id, x, y, width, height, s);
		active = false;
	}

	public GuiMenuSideButton(int id, int x, int y, String s) {
		this(id, x, y, 200, 20, s);
	}

	@Override
	public void drawButton(Minecraft minecraft, int x, int y) {
		if (!visible) {
			return;
		}
		FontRenderer fontrenderer = minecraft.fontRendererObj;
		minecraft.renderEngine.bindTexture(GuiMenuSideButton.resource);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		int width = this.width + (active ? 2 : 0);
		hovered = ((x >= xPosition) && (y >= yPosition) && (x < (xPosition + width)) && (y < (yPosition + height)));
		int k = getHoverState(hovered);
		this.drawTexturedModalRect(xPosition, yPosition, 0, k * 22, width, height);
		mouseDragged(minecraft, x, y);
		String text = "";
		float maxWidth = width * 0.75f;
		if (fontrenderer.getStringWidth(displayString) > maxWidth) {
			for (int h = 0; h < displayString.length(); ++h) {
				char c = displayString.charAt(h);
				if (fontrenderer.getStringWidth(text + c) > maxWidth) {
					break;
				}
				text += c;
			}
			text += "...";
		} else {
			text = displayString;
		}
		if (active) {
			drawCenteredString(fontrenderer, text, xPosition + (width / 2), yPosition + ((height - 8) / 2), 16777120);
		} else if (hovered) {
			drawCenteredString(fontrenderer, text, xPosition + (width / 2), yPosition + ((height - 8) / 2), 16777120);
		} else {
			drawCenteredString(fontrenderer, text, xPosition + (width / 2), yPosition + ((height - 8) / 2), 14737632);
		}
	}

	@Override
	public int getHoverState(boolean flag) {
		if (active) {
			return 0;
		}
		return 1;
	}

	@Override
	protected void mouseDragged(Minecraft minecraft, int mouseX, int mouseY) {
	}

	@Override
	public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
		return !active && visible && hovered;
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {
	}
}

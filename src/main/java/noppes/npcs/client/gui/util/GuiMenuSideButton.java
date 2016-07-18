//

//

package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiMenuSideButton extends GuiNpcButton {
	public static final ResourceLocation resource;
	static {
		resource = new ResourceLocation("customnpcs", "textures/gui/menusidebutton.png");
	}

	public boolean active;

	public GuiMenuSideButton(final int i, final int j, final int k, final int l, final int i1, final String s) {
		super(i, j, k, l, i1, s);
		active = false;
	}

	public GuiMenuSideButton(final int i, final int j, final int k, final String s) {
		this(i, j, k, 200, 20, s);
	}

	@Override
	public void drawButton(final Minecraft minecraft, final int i, final int j) {
		if (!visible) {
			return;
		}
		final FontRenderer fontrenderer = minecraft.fontRendererObj;
		minecraft.renderEngine.bindTexture(GuiMenuSideButton.resource);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		final int width = this.width + (active ? 2 : 0);
		hovered = ((i >= xPosition) && (j >= yPosition) && (i < (xPosition + width)) && (j < (yPosition + height)));
		final int k = getHoverState(hovered);
		this.drawTexturedModalRect(xPosition, yPosition, 0, k * 22, width, height);
		mouseDragged(minecraft, i, j);
		String text = "";
		final float maxWidth = width * 0.75f;
		if (fontrenderer.getStringWidth(displayString) > maxWidth) {
			for (int h = 0; h < displayString.length(); ++h) {
				final char c = displayString.charAt(h);
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
	public int getHoverState(final boolean flag) {
		if (active) {
			return 0;
		}
		return 1;
	}

	@Override
	protected void mouseDragged(final Minecraft minecraft, final int i, final int j) {
	}

	@Override
	public boolean mousePressed(final Minecraft minecraft, final int i, final int j) {
		return !active && visible && hovered;
	}

	@Override
	public void mouseReleased(final int i, final int j) {
	}
}

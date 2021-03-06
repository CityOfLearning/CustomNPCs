
package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonBiDirectional extends GuiNpcButton {
	public static ResourceLocation resource;

	static {
		resource = new ResourceLocation("customnpcs:textures/gui/arrowbuttons.png");
	}

	public GuiButtonBiDirectional(int id, int x, int y, int width, int height, String[] arr, int current) {
		super(id, x, y, width, height, arr, current);
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (!visible) {
			return;
		}
		boolean hover = (mouseX >= xPosition) && (mouseY >= yPosition) && (mouseX < (xPosition + width))
				&& (mouseY < (yPosition + height));
		boolean hoverL = (mouseX >= xPosition) && (mouseY >= yPosition) && (mouseX < (xPosition + 14))
				&& (mouseY < (yPosition + height));
		boolean hoverR = !hoverL && (mouseX >= ((xPosition + width) - 14)) && (mouseY >= yPosition)
				&& (mouseX < (xPosition + width)) && (mouseY < (yPosition + height));
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(GuiButtonBiDirectional.resource);
		this.drawTexturedModalRect(xPosition, yPosition, 0, hoverL ? 40 : 20, 11, 20);
		this.drawTexturedModalRect((xPosition + width) - 11, yPosition, 11, ((hover && !hoverL) || hoverR) ? 40 : 20,
				11, 20);
		int l = 16777215;
		if (packedFGColour != 0) {
			l = packedFGColour;
		} else if (!enabled) {
			l = 10526880;
		} else if (hover) {
			l = 16777120;
		}
		String text = "";
		float maxWidth = width - 36;
		if (mc.fontRendererObj.getStringWidth(displayString) > maxWidth) {
			for (int h = 0; h < displayString.length(); ++h) {
				char c = displayString.charAt(h);
				text += c;
				if (mc.fontRendererObj.getStringWidth(text) > maxWidth) {
					break;
				}
			}
			text += "...";
		} else {
			text = displayString;
		}
		if (hover) {
			text = "\u00A7n" + text;
		}
		drawCenteredString(mc.fontRendererObj, text, xPosition + (width / 2), yPosition + ((height - 8) / 2), l);
	}

	@Override
	public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
		int value = getValue();
		boolean bo = super.mousePressed(minecraft, mouseX, mouseY);
		if (bo && (display != null) && (display.length != 0)) {
			boolean hoverL = (mouseX >= xPosition) && (mouseY >= yPosition) && (mouseX < (xPosition + 14))
					&& (mouseY < (yPosition + height));
			boolean hoverR = !hoverL && (mouseX >= (xPosition + 14)) && (mouseY >= yPosition)
					&& (mouseX < (xPosition + width)) && (mouseY < (yPosition + height));
			if (hoverR) {
				value = (value + 1) % display.length;
			}
			if (hoverL) {
				if (value <= 0) {
					value = display.length;
				}
				--value;
			}
			setDisplay(value);
		}
		return bo;
	}
}

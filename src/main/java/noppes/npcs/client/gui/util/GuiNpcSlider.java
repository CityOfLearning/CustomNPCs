//

//

package noppes.npcs.client.gui.util;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import noppes.npcs.NoppesStringUtils;

public class GuiNpcSlider extends GuiButton {
	private ISliderListener listener;
	public int id;
	public float sliderValue;
	public boolean dragging;

	public GuiNpcSlider(final GuiScreen parent, final int id, final int xPos, final int yPos, final float sliderValue) {
		this(parent, id, xPos, yPos, "", sliderValue);
		if (listener != null) {
			listener.mouseDragged(this);
		}
	}

	public GuiNpcSlider(final GuiScreen parent, final int id, final int xPos, final int yPos, final int width,
			final int height, final float sliderValue) {
		this(parent, id, xPos, yPos, "", sliderValue);
		this.width = width;
		this.height = height;
		if (listener != null) {
			listener.mouseDragged(this);
		}
	}

	public GuiNpcSlider(final GuiScreen parent, final int id, final int xPos, final int yPos,
			final String displayString, final float sliderValue) {
		super(id, xPos, yPos, 150, 20, NoppesStringUtils.translate(displayString));
		this.sliderValue = 1.0f;
		this.id = id;
		this.sliderValue = sliderValue;
		if (parent instanceof ISliderListener) {
			listener = (ISliderListener) parent;
		}
	}

	public String getDisplayString() {
		return displayString;
	}

	@Override
	public int getHoverState(final boolean par1) {
		return 0;
	}

	@Override
	public void mouseDragged(final Minecraft mc, final int par2, final int par3) {
		if (!visible) {
			return;
		}
		mc.getTextureManager().bindTexture(GuiButton.buttonTextures);
		if (dragging) {
			sliderValue = (par2 - (xPosition + 4)) / (width - 8);
			if (sliderValue < 0.0f) {
				sliderValue = 0.0f;
			}
			if (sliderValue > 1.0f) {
				sliderValue = 1.0f;
			}
			if (listener != null) {
				listener.mouseDragged(this);
			}
			if (!Mouse.isButtonDown(0)) {
				mouseReleased(0, 0);
			}
		}
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.drawTexturedModalRect(xPosition + (int) (sliderValue * (width - 8)), yPosition, 0, 66, 4, 20);
		this.drawTexturedModalRect(xPosition + (int) (sliderValue * (width - 8)) + 4, yPosition, 196, 66, 4, 20);
	}

	@Override
	public boolean mousePressed(final Minecraft par1Minecraft, final int par2, final int par3) {
		if (enabled && visible && (par2 >= xPosition) && (par3 >= yPosition) && (par2 < (xPosition + width))
				&& (par3 < (yPosition + height))) {
			sliderValue = (par2 - (xPosition + 4)) / (width - 8);
			if (sliderValue < 0.0f) {
				sliderValue = 0.0f;
			}
			if (sliderValue > 1.0f) {
				sliderValue = 1.0f;
			}
			if (listener != null) {
				listener.mousePressed(this);
			}
			return dragging = true;
		}
		return false;
	}

	@Override
	public void mouseReleased(final int par1, final int par2) {
		dragging = false;
		if (listener != null) {
			listener.mouseReleased(this);
		}
	}

	public void setString(final String str) {
		displayString = NoppesStringUtils.translate(str);
	}
}

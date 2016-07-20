//

//

package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;

public class GuiNpcButton extends GuiButton {
	protected String[] display;
	private int displayValue;
	public int id;

	public GuiNpcButton(final int id, final int x, final int y, final int width, final int height, final String string) {
		super(id, x, y, width, height, StatCollector.translateToLocal(string));
		displayValue = 0;
		this.id = id;
	}

	public GuiNpcButton(final int id, final int x, final int y, final int width, final int height, final String[] display,
			final int val) {
		this(id, x, y, width, height, (display.length == 0) ? "" : display[val % display.length]);
		this.display = display;
		displayValue = ((display.length == 0) ? 0 : (val % display.length));
	}

	public GuiNpcButton(final int id, final int x, final int y, final String s) {
		super(id, x, y, StatCollector.translateToLocal(s));
		displayValue = 0;
		this.id = id;
	}

	public GuiNpcButton(final int id, final int x, final int y, final String[] display, final int val) {
		this(id, x, y, display[val]);
		this.display = display;
		displayValue = val;
	}

	public int getValue() {
		return displayValue;
	}

	public boolean getVisible() {
		return visible;
	}

	public int getWidth() {
		return width;
	}

	@Override
	public boolean mousePressed(final Minecraft minecraft, final int mouseX, final int mouseY) {
		final boolean bo = super.mousePressed(minecraft, mouseX, mouseY);
		if (bo && (display != null) && (display.length != 0)) {
			displayValue = (displayValue + 1) % display.length;
			setDisplayText(display[displayValue]);
		}
		return bo;
	}

	public void setDisplay(final int value) {
		displayValue = value;
		setDisplayText(display[value]);
	}

	public void setDisplayText(final String text) {
		displayString = StatCollector.translateToLocal(text);
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	public void setTextColor(final int color) {
		packedFGColour = color;
	}

	public void setVisible(final boolean visible) {
		this.visible = visible;
	}
}

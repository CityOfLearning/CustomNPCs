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

	public GuiNpcButton(int id, int x, int y, int width, int height, String string) {
		super(id, x, y, width, height, StatCollector.translateToLocal(string));
		displayValue = 0;
		this.id = id;
	}

	public GuiNpcButton(int id, int x, int y, int width, int height, String[] display, int val) {
		this(id, x, y, width, height, (display.length == 0) ? "" : display[val % display.length]);
		this.display = display;
		displayValue = ((display.length == 0) ? 0 : (val % display.length));
	}

	public GuiNpcButton(int id, int x, int y, String s) {
		super(id, x, y, StatCollector.translateToLocal(s));
		displayValue = 0;
		this.id = id;
	}

	public GuiNpcButton(int id, int x, int y, String[] display, int val) {
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
	public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
		boolean bo = super.mousePressed(minecraft, mouseX, mouseY);
		if (bo && (display != null) && (display.length != 0)) {
			displayValue = (displayValue + 1) % display.length;
			setDisplayText(display[displayValue]);
		}
		return bo;
	}

	public void setDisplay(int value) {
		displayValue = value;
		setDisplayText(display[value]);
	}

	public void setDisplayText(String text) {
		displayString = StatCollector.translateToLocal(text);
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setTextColor(int color) {
		packedFGColour = color;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}

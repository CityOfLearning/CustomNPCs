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

	public GuiNpcButton(final int i, final int j, final int k, final int l, final int m, final String string) {
		super(i, j, k, l, m, StatCollector.translateToLocal(string));
		displayValue = 0;
		id = i;
	}

	public GuiNpcButton(final int i, final int j, final int k, final int l, final int m, final String[] display,
			final int val) {
		this(i, j, k, l, m, (display.length == 0) ? "" : display[val % display.length]);
		this.display = display;
		displayValue = ((display.length == 0) ? 0 : (val % display.length));
	}

	public GuiNpcButton(final int i, final int j, final int k, final String s) {
		super(i, j, k, StatCollector.translateToLocal(s));
		displayValue = 0;
		id = i;
	}

	public GuiNpcButton(final int i, final int j, final int k, final String[] display, final int val) {
		this(i, j, k, display[val]);
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
	public boolean mousePressed(final Minecraft minecraft, final int i, final int j) {
		final boolean bo = super.mousePressed(minecraft, i, j);
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

	public void setEnabled(final boolean bo) {
		enabled = bo;
	}

	public void setTextColor(final int color) {
		packedFGColour = color;
	}

	public void setVisible(final boolean b) {
		visible = b;
	}
}

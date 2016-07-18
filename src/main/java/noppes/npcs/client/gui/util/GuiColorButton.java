//

//

package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;

public class GuiColorButton extends GuiNpcButton {
	public int color;

	public GuiColorButton(final int id, final int x, final int y, final int color) {
		super(id, x, y, 50, 20, "");
		this.color = color;
	}

	@Override
	public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
		if (!visible) {
			return;
		}
		drawRect(xPosition, yPosition, xPosition + 50, yPosition + 20, -16777216 + color);
	}
}

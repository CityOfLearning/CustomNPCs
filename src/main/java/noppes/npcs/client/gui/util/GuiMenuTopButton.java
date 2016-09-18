
package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiMenuTopButton extends GuiNpcButton {
	public static ResourceLocation resource;
	static {
		resource = new ResourceLocation("customnpcs", "textures/gui/menutopbutton.png");
	}
	protected int height;
	public boolean active;
	public boolean hover;
	public boolean rotated;

	public IButtonListener listener;

	public GuiMenuTopButton(int id, GuiButton parent, String s) {
		this(id, parent.xPosition + parent.width, parent.yPosition, s);
	}

	public GuiMenuTopButton(int id, GuiButton parent, String s, IButtonListener listener) {
		this(id, parent, s);
		this.listener = listener;
	}

	public GuiMenuTopButton(int id, int x, int y, String s) {
		super(id, x, y, StatCollector.translateToLocal(s));
		hover = false;
		rotated = false;
		active = false;
		width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(displayString) + 12;
		height = 20;
	}

	@Override
	public void drawButton(Minecraft minecraft, int x, int y) {
		if (!getVisible()) {
			return;
		}
		GlStateManager.pushMatrix();
		minecraft.renderEngine.bindTexture(GuiMenuTopButton.resource);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		int height = this.height - (active ? 0 : 2);
		hover = ((x >= xPosition) && (y >= yPosition) && (x < (xPosition + getWidth())) && (y < (yPosition + height)));
		int k = getHoverState(hover);
		this.drawTexturedModalRect(xPosition, yPosition, 0, k * 20, getWidth() / 2, height);
		this.drawTexturedModalRect(xPosition + (getWidth() / 2), yPosition, 200 - (getWidth() / 2), k * 20,
				getWidth() / 2, height);
		mouseDragged(minecraft, x, y);
		FontRenderer fontrenderer = minecraft.fontRendererObj;
		if (rotated) {
			GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
		}
		if (active) {
			drawCenteredString(fontrenderer, displayString, xPosition + (getWidth() / 2),
					yPosition + ((height - 8) / 2), 16777120);
		} else if (hover) {
			drawCenteredString(fontrenderer, displayString, xPosition + (getWidth() / 2),
					yPosition + ((height - 8) / 2), 16777120);
		} else {
			drawCenteredString(fontrenderer, displayString, xPosition + (getWidth() / 2),
					yPosition + ((height - 8) / 2), 14737632);
		}
		GlStateManager.popMatrix();
	}

	@Override
	public int getHoverState(boolean flag) {
		byte byte0 = 1;
		if (active) {
			byte0 = 0;
		} else if (flag) {
			byte0 = 2;
		}
		return byte0;
	}

	@Override
	protected void mouseDragged(Minecraft minecraft, int mouseX, int mouseY) {
	}

	@Override
	public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
		boolean bo = !active && getVisible() && hover;
		if (bo && (listener != null)) {
			listener.actionPerformed(this);
			return false;
		}
		return bo;
	}

	@Override
	public void mouseReleased(int i, int j) {
	}
}

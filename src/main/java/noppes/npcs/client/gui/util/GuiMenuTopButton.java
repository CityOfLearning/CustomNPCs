//

//

package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiMenuTopButton extends GuiNpcButton {
	public static final ResourceLocation resource;
	static {
		resource = new ResourceLocation("customnpcs", "textures/gui/menutopbutton.png");
	}
	protected int height;
	public boolean active;
	public boolean hover;
	public boolean rotated;

	public IButtonListener listener;

	public GuiMenuTopButton(final int i, final GuiButton parent, final String s) {
		this(i, parent.xPosition + parent.width, parent.yPosition, s);
	}

	public GuiMenuTopButton(final int i, final GuiButton parent, final String s, final IButtonListener listener) {
		this(i, parent, s);
		this.listener = listener;
	}

	public GuiMenuTopButton(final int i, final int j, final int k, final String s) {
		super(i, j, k, StatCollector.translateToLocal(s));
		hover = false;
		rotated = false;
		active = false;
		width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(displayString) + 12;
		height = 20;
	}

	@Override
	public void drawButton(final Minecraft minecraft, final int i, final int j) {
		if (!getVisible()) {
			return;
		}
		GlStateManager.pushMatrix();
		minecraft.renderEngine.bindTexture(GuiMenuTopButton.resource);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		final int height = this.height - (active ? 0 : 2);
		hover = ((i >= xPosition) && (j >= yPosition) && (i < (xPosition + getWidth())) && (j < (yPosition + height)));
		final int k = getHoverState(hover);
		this.drawTexturedModalRect(xPosition, yPosition, 0, k * 20, getWidth() / 2, height);
		this.drawTexturedModalRect(xPosition + (getWidth() / 2), yPosition, 200 - (getWidth() / 2), k * 20,
				getWidth() / 2, height);
		mouseDragged(minecraft, i, j);
		final FontRenderer fontrenderer = minecraft.fontRendererObj;
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
	public int getHoverState(final boolean flag) {
		byte byte0 = 1;
		if (active) {
			byte0 = 0;
		} else if (flag) {
			byte0 = 2;
		}
		return byte0;
	}

	@Override
	protected void mouseDragged(final Minecraft minecraft, final int i, final int j) {
	}

	@Override
	public boolean mousePressed(final Minecraft minecraft, final int i, final int j) {
		final boolean bo = !active && getVisible() && hover;
		if (bo && (listener != null)) {
			listener.actionPerformed(this);
			return false;
		}
		return bo;
	}

	@Override
	public void mouseReleased(final int i, final int j) {
	}
}

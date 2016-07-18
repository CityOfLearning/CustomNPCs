//

//

package noppes.npcs.client.gui.util;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiMenuTopIconButton extends GuiMenuTopButton {
	private static final ResourceLocation resource;
	protected static RenderItem itemRender;
	static {
		resource = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
	}

	private ItemStack item;

	public GuiMenuTopIconButton(final int i, final GuiButton parent, final String s, final IButtonListener listener,
			final ItemStack item) {
		super(i, parent, s, listener);
		width = 28;
		height = 28;
		this.item = item;
	}

	public GuiMenuTopIconButton(final int i, final GuiButton parent, final String s, final ItemStack item) {
		super(i, parent, s);
		width = 28;
		height = 28;
		this.item = item;
	}

	public GuiMenuTopIconButton(final int i, final int x, final int y, final String s, final IButtonListener listener,
			final ItemStack item) {
		super(i, x, y, s);
		width = 28;
		height = 28;
		this.item = item;
		this.listener = listener;
	}

	public GuiMenuTopIconButton(final int i, final int x, final int y, final String s, final ItemStack item) {
		super(i, x, y, s);
		width = 28;
		height = 28;
		this.item = item;
		GuiMenuTopIconButton.itemRender = Minecraft.getMinecraft().getRenderItem();
	}

	@Override
	public void drawButton(final Minecraft minecraft, final int i, final int j) {
		if (!getVisible()) {
			return;
		}
		if (item.getItem() == null) {
			item = new ItemStack(Blocks.dirt);
		}
		hover = ((i >= xPosition) && (j >= yPosition) && (i < (xPosition + getWidth())) && (j < (yPosition + height)));
		final Minecraft mc = Minecraft.getMinecraft();
		if (hover && !active) {
			final int x = i + mc.fontRendererObj.getStringWidth(displayString);
			GlStateManager.translate(x, yPosition + 2, 0.0f);
			drawHoveringText(Arrays.asList(displayString), 0, 0, mc.fontRendererObj);
			GlStateManager.translate((-x), (-(yPosition + 2)), 0.0f);
		}
		mc.getTextureManager().bindTexture(GuiMenuTopIconButton.resource);
		GlStateManager.pushMatrix();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		this.drawTexturedModalRect(xPosition, yPosition + (active ? 2 : 0), 0, active ? 32 : 0, 28, 28);
		zLevel = 100.0f;
		GuiMenuTopIconButton.itemRender.zLevel = 100.0f;
		GlStateManager.enableLighting();
		GlStateManager.enableRescaleNormal();
		RenderHelper.enableGUIStandardItemLighting();
		GuiMenuTopIconButton.itemRender.renderItemAndEffectIntoGUI(item, xPosition + 6, yPosition + 10);
		GuiMenuTopIconButton.itemRender.renderItemOverlays(mc.fontRendererObj, item, xPosition + 6, yPosition + 10);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GuiMenuTopIconButton.itemRender.zLevel = 0.0f;
		zLevel = 0.0f;
		GlStateManager.popMatrix();
	}

	protected void drawHoveringText(final List<String> p_146283_1_, final int p_146283_2_, final int p_146283_3_,
			final FontRenderer font) {
		if (!p_146283_1_.isEmpty()) {
			GlStateManager.disableRescaleNormal();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			int k = 0;
			for (final String s : p_146283_1_) {
				final int l = font.getStringWidth(s);
				if (l > k) {
					k = l;
				}
			}
			int j2 = p_146283_2_ + 12;
			int k2 = p_146283_3_ - 12;
			int i1 = 8;
			if (p_146283_1_.size() > 1) {
				i1 += 2 + ((p_146283_1_.size() - 1) * 10);
			}
			if ((j2 + k) > width) {
				j2 -= 28 + k;
			}
			if ((k2 + i1 + 6) > height) {
				k2 = height - i1 - 6;
			}
			zLevel = 300.0f;
			GuiMenuTopIconButton.itemRender.zLevel = 300.0f;
			final int j3 = -267386864;
			drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j3, j3);
			drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j3, j3);
			drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j3, j3);
			drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j3, j3);
			drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j3, j3);
			final int k3 = 1347420415;
			final int l2 = ((k3 & 0xFEFEFE) >> 1) | (k3 & 0xFF000000);
			drawGradientRect(j2 - 3, (k2 - 3) + 1, (j2 - 3) + 1, (k2 + i1 + 3) - 1, k3, l2);
			drawGradientRect(j2 + k + 2, (k2 - 3) + 1, j2 + k + 3, (k2 + i1 + 3) - 1, k3, l2);
			drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, (k2 - 3) + 1, k3, k3);
			drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l2, l2);
			for (int i2 = 0; i2 < p_146283_1_.size(); ++i2) {
				final String s2 = p_146283_1_.get(i2);
				font.drawStringWithShadow(s2, j2, k2, -1);
				if (i2 == 0) {
					k2 += 2;
				}
				k2 += 10;
			}
			zLevel = 0.0f;
			GuiMenuTopIconButton.itemRender.zLevel = 0.0f;
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableRescaleNormal();
		}
	}
}

//

//

package tconstruct.client.tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractTab extends GuiButton {
	ResourceLocation texture;
	ItemStack renderStack;
	RenderItem itemRenderer;

	public AbstractTab(final int id, final int posX, final int posY, final ItemStack renderStack) {
		super(id, posX, posY, 28, 32, "");
		texture = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
		this.renderStack = renderStack;
		itemRenderer = Minecraft.getMinecraft().getRenderItem();
	}

	@Override
	public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
		if (visible) {
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			final int yTexPos = enabled ? 3 : 32;
			final int ySize = enabled ? 25 : 32;
			final int xOffset = (id != 2) ? 1 : 0;
			final int yPos = yPosition + (enabled ? 3 : 0);
			mc.renderEngine.bindTexture(texture);
			this.drawTexturedModalRect(xPosition, yPos, xOffset * 28, yTexPos, 28, ySize);
			RenderHelper.enableGUIStandardItemLighting();
			zLevel = 100.0f;
			itemRenderer.zLevel = 100.0f;
			GlStateManager.enableLighting();
			GlStateManager.enableRescaleNormal();
			itemRenderer.renderItemAndEffectIntoGUI(renderStack, xPosition + 6, yPosition + 8);
			itemRenderer.renderItemOverlays(mc.fontRendererObj, renderStack, xPosition + 6, yPosition + 8);
			GlStateManager.disableLighting();
			GlStateManager.disableBlend();
			itemRenderer.zLevel = 0.0f;
			zLevel = 0.0f;
			RenderHelper.disableStandardItemLighting();
		}
	}

	@Override
	public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
		final boolean inWindow = enabled && visible && (mouseX >= xPosition) && (mouseY >= yPosition)
				&& (mouseX < (xPosition + width)) && (mouseY < (yPosition + height));
		if (inWindow) {
			onTabClicked();
		}
		return inWindow;
	}

	public abstract void onTabClicked();

	public abstract boolean shouldAddToList();
}

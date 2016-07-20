//

//

package tconstruct.client.tabs;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import noppes.npcs.CustomItems;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.client.gui.player.GuiFaction;
import noppes.npcs.util.CustomNPCsScheduler;

public class InventoryTabFactions extends AbstractTab {
	public InventoryTabFactions() {
		super(0, 0, 0, new ItemStack(Blocks.standing_banner, 1, 5));
		displayString = NoppesStringUtils.translate("menu.factions");
	}

	@Override
	public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
		if (!enabled || !visible) {
			super.drawButton(minecraft, mouseX, mouseY);
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
		boolean hovered = (mouseX >= xPosition) && (mouseY >= yPosition) && (mouseX < (xPosition + width))
				&& (mouseY < (yPosition + height));
		if (hovered) {
			int x = mouseX + mc.fontRendererObj.getStringWidth(displayString);
			GlStateManager.translate(x, yPosition + 2, 0.0f);
			drawHoveringText(Arrays.asList(displayString), 0, 0, mc.fontRendererObj);
			GlStateManager.translate((-x), (-(yPosition + 2)), 0.0f);
		}
		super.drawButton(minecraft, mouseX, mouseY);
	}

	protected void drawHoveringText(List<String> list, int x, int y, FontRenderer font) {
		if (list.isEmpty()) {
			return;
		}
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		int k = 0;
		for (String s : list) {
			int l = font.getStringWidth(s);
			if (l > k) {
				k = l;
			}
		}
		int j2 = x + 12;
		int k2 = y - 12;
		int i1 = 8;
		if (list.size() > 1) {
			i1 += 2 + ((list.size() - 1) * 10);
		}
		if ((j2 + k) > width) {
			j2 -= 28 + k;
		}
		if ((k2 + i1 + 6) > height) {
			k2 = height - i1 - 6;
		}
		zLevel = 300.0f;
		itemRenderer.zLevel = 300.0f;
		int j3 = -267386864;
		drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j3, j3);
		drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j3, j3);
		drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j3, j3);
		drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j3, j3);
		drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j3, j3);
		int k3 = 1347420415;
		int l2 = ((k3 & 0xFEFEFE) >> 1) | (k3 & 0xFF000000);
		drawGradientRect(j2 - 3, (k2 - 3) + 1, (j2 - 3) + 1, (k2 + i1 + 3) - 1, k3, l2);
		drawGradientRect(j2 + k + 2, (k2 - 3) + 1, j2 + k + 3, (k2 + i1 + 3) - 1, k3, l2);
		drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, (k2 - 3) + 1, k3, k3);
		drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l2, l2);
		for (int i2 = 0; i2 < list.size(); ++i2) {
			String s2 = list.get(i2);
			font.drawStringWithShadow(s2, j2, k2, -1);
			if (i2 == 0) {
				k2 += 2;
			}
			k2 += 10;
		}
		zLevel = 0.0f;
		itemRenderer.zLevel = 0.0f;
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.enableRescaleNormal();
	}

	@Override
	public void onTabClicked() {
		CustomNPCsScheduler.runTack(new Runnable() {
			@Override
			public void run() {
				Minecraft mc = Minecraft.getMinecraft();
				mc.displayGuiScreen(new GuiFaction());
			}
		});
	}

	@Override
	public boolean shouldAddToList() {
		return true;
	}
}

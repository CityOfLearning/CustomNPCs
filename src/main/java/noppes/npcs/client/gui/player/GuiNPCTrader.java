
package noppes.npcs.client.gui.player;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.containers.ContainerNPCTrader;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleTrader;

public class GuiNPCTrader extends GuiContainerNPCInterface {
	private ResourceLocation resource;
	private ResourceLocation slot;
	private RoleTrader role;
	private ContainerNPCTrader container;

	public GuiNPCTrader(EntityNPCInterface npc, ContainerNPCTrader container) {
		super(npc, container);
		resource = new ResourceLocation("customnpcs", "textures/gui/trader.png");
		slot = new ResourceLocation("customnpcs", "textures/gui/slot.png");
		this.container = container;
		role = (RoleTrader) npc.roleInterface;
		closeOnEsc = true;
		ySize = 224;
		xSize = 223;
		title = "role.trader";
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		drawWorldBackground(0);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(resource);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		GlStateManager.enableRescaleNormal();
		mc.renderEngine.bindTexture(slot);
		for (int slot = 0; slot < 18; ++slot) {
			int x = guiLeft + ((slot % 3) * 72) + 10;
			int y = guiTop + ((slot / 3) * 21) + 6;
			ItemStack item = role.inventoryCurrency.items.get(slot);
			ItemStack item2 = role.inventoryCurrency.items.get(slot + 18);
			if (item == null) {
				item = item2;
				item2 = null;
			}
			if (NoppesUtilPlayer.compareItems(item, item2, false, false)) {
				ItemStack copy;
				item = (copy = item.copy());
				copy.stackSize += item2.stackSize;
				item2 = null;
			}
			ItemStack sold = role.inventorySold.items.get(slot);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			mc.renderEngine.bindTexture(this.slot);
			this.drawTexturedModalRect(x + 42, y, 0, 0, 18, 18);
			if ((item != null) && (sold != null)) {
				RenderHelper.enableGUIStandardItemLighting();
				if (item2 != null) {
					itemRender.renderItemAndEffectIntoGUI(item2, x, y + 1);
					itemRender.renderItemOverlays(fontRendererObj, item2, x, y + 1);
				}
				itemRender.renderItemAndEffectIntoGUI(item, x + 18, y + 1);
				itemRender.renderItemOverlays(fontRendererObj, item, x + 18, y + 1);
				RenderHelper.disableStandardItemLighting();
				fontRendererObj.drawString("=", x + 36, y + 5, CustomNpcResourceListener.DefaultTextColor);
			}
		}
		GlStateManager.disableRescaleNormal();
		super.drawGuiContainerBackgroundLayer(f, i, j);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		for (int slot = 0; slot < 18; ++slot) {
			int x = ((slot % 3) * 72) + 10;
			int y = ((slot / 3) * 21) + 6;
			ItemStack item = role.inventoryCurrency.items.get(slot);
			ItemStack item2 = role.inventoryCurrency.items.get(slot + 18);
			if (item == null) {
				item = item2;
				item2 = null;
			}
			if (NoppesUtilPlayer.compareItems(item, item2, role.ignoreDamage, role.ignoreNBT)) {
				ItemStack copy;
				item = (copy = item.copy());
				copy.stackSize += item2.stackSize;
				item2 = null;
			}
			ItemStack sold = role.inventorySold.items.get(slot);
			if (item != null) {
				if (sold != null) {
					if (isPointInRegion(x + 43, y + 1, 16, 16, par1, par2)) {
						if (!container.canBuy(item, item2, player)) {
							GlStateManager.translate(0.0f, 0.0f, 300.0f);
							if ((item != null) && !NoppesUtilPlayer.compareItems(player, item, role.ignoreDamage,
									role.ignoreNBT)) {
								drawGradientRect(x + 17, y, x + 35, y + 18, 1886851088, 1886851088);
							}
							if ((item2 != null) && !NoppesUtilPlayer.compareItems(player, item2, role.ignoreDamage,
									role.ignoreNBT)) {
								drawGradientRect(x - 1, y, x + 17, y + 18, 1886851088, 1886851088);
							}
							String title = StatCollector.translateToLocal("trader.insufficient");
							fontRendererObj.drawString(title, (xSize - fontRendererObj.getStringWidth(title)) / 2, 131,
									14483456);
							GlStateManager.translate(0.0f, 0.0f, -300.0f);
						} else {
							String title = StatCollector.translateToLocal("trader.sufficient");
							fontRendererObj.drawString(title, (xSize - fontRendererObj.getStringWidth(title)) / 2, 131,
									56576);
						}
					}
					if (isPointInRegion(x, y, 16, 16, par1, par2) && (item2 != null)) {
						renderToolTip(item2, par1 - guiLeft, par2 - guiTop);
					}
					if (isPointInRegion(x + 18, y, 16, 16, par1, par2)) {
						renderToolTip(item, par1 - guiLeft, par2 - guiTop);
					}
				}
			}
		}
	}

	@Override
	public void save() {
	}
}

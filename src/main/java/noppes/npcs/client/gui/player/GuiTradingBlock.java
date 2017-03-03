
package noppes.npcs.client.gui.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.containers.ContainerTradingBlock;
import noppes.npcs.util.NoppesUtilPlayer;

public class GuiTradingBlock extends GuiContainerNPCInterface implements IGuiData {
	private ResourceLocation resource;
	public Map<Integer, ItemStack> items;
	private ContainerTradingBlock container;

	public GuiTradingBlock(ContainerTradingBlock container) {
		super(null, container);
		resource = new ResourceLocation("customnpcs", "textures/gui/tradingblock.png");
		items = new HashMap<>();
		this.container = container;
		ySize = 230;
		closeOnEsc = true;
		container.tile.setTrader1(player);
		container.tile.setTrader2(null);
		title = "";
	}

	@Override
	public void actionPerformed(GuiButton guibutton) {
		if (guibutton.id == 0) {
			NoppesUtilPlayer.sendData(EnumPlayerPacket.TradeAccept, new Object[0]);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		drawWorldBackground(0);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc = Minecraft.getMinecraft();
		mc.renderEngine.bindTexture(resource);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		GuiInventory.drawEntityOnScreen(guiLeft + 34, guiTop + 94, 30, (guiLeft + 33) - i, (guiTop + 52) - j,
				mc.thePlayer);
		if ((container.state == 3) || (container.state == 4)) {
			super.drawGuiContainerBackgroundLayer(f, i, j);
			return;
		}
		if (container.tile.getTrader2() != null) {
			GuiInventory.drawEntityOnScreen(guiLeft + 142, guiTop + 94, 30, (guiLeft + 141) - i, (guiTop + 52) - j,
					container.tile.getTrader2());
		}
		ItemStack hover = null;
		for (int k = 0; k < 9; ++k) {
			ItemStack itemstack = items.get(k);
			if (itemstack != null) {
				int x = guiLeft + 8 + ((k % 5) * 18);
				int y = guiTop + 8 + ((k / 5) * 18);
				GlStateManager.enableRescaleNormal();
				RenderHelper.enableGUIStandardItemLighting();
				itemRender.renderItemAndEffectIntoGUI(itemstack, x, y);
				itemRender.renderItemOverlays(fontRendererObj, itemstack, x, y);
				RenderHelper.disableStandardItemLighting();
				GlStateManager.disableRescaleNormal();
				if (isPointInRegion(x - guiLeft, y - guiTop, 16, 16, i, j)) {
					hover = itemstack;
				}
			}
		}
		if (hover != null) {
			renderToolTip(hover, i, j);
		}
		if (container.state == 1) {
			drawSquare(guiLeft + 6, guiTop + 6, 164, 20, 2);
		}
		if (container.state == 2) {
			drawSquare(guiLeft + 6, guiTop + 123, 164, 20, 2);
		}
		super.drawGuiContainerBackgroundLayer(f, i, j);
	}

	private void drawSquare(int x, int y, int width, int height, int thick) {
		drawRect(x, y, x + width, y + thick, -16711936);
		drawRect(x, y, x + thick, y + height, -16711936);
		drawRect(x, y + height, x + width, (y + height) - thick, -16711936);
		drawRect(x + width, y, (x + width) - thick, y + height, -16711936);
	}

	@Override
	public void initGui() {
		super.initGui();
		GuiNpcLabel label = null;
		if (container.tile.getTrader2() == null) {
			addLabel(label = new GuiNpcLabel(0, "trader.waiting", guiLeft + 53, guiTop + 107));
		} else if ((container.state == 0) || (container.state == 1)) {
			addButton(new GuiNpcButton(0, guiLeft + 53, guiTop + 102, 70, 20, "trader.trade"));
		} else if (container.state == 3) {
			addLabel(label = new GuiNpcLabel(0, "trader.complete", guiLeft + 53, guiTop + 107));
		} else if (container.state == 4) {
			addLabel(label = new GuiNpcLabel(0, "trader.cancelled", guiLeft + 53, guiTop + 107));
		}
		if (label != null) {
			label.center(70);
		}
	}

	@Override
	public void save() {
	}

	@Override
	public void setGuiData(NBTTagCompound compound) {
		if (compound.hasKey("Player")) {
			items = new HashMap<>();
			String id = compound.getString("Player");
			if (id.isEmpty()) {
				container.tile.setTrader2(null);
			} else {
				container.tile.setTrader2(player.worldObj.getPlayerEntityByUUID(UUID.fromString(id)));
			}
		} else if (compound.hasKey("State")) {
			container.state = compound.getInteger("State");
		} else {
			items = ContainerTradingBlock.CompToItem(compound);
		}
		initGui();
	}
}

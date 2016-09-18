
package noppes.npcs.client.gui.player;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.containers.ContainerNPCBankInterface;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCBankChest extends GuiContainerNPCInterface implements IGuiData {
	private ResourceLocation resource;
	private ContainerNPCBankInterface container;
	private int availableSlots;
	private int maxSlots;
	private int unlockedSlots;
	private ItemStack currency;

	public GuiNPCBankChest(EntityNPCInterface npc, ContainerNPCBankInterface container) {
		super(npc, container);
		resource = new ResourceLocation("customnpcs", "textures/gui/bankchest.png");
		availableSlots = 0;
		maxSlots = 1;
		unlockedSlots = 1;
		this.container = container;
		title = "";
		allowUserInput = false;
		ySize = 235;
		closeOnEsc = true;
	}

	@Override
	public void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);
		int id = guibutton.id;
		if (id < 6) {
			NoppesUtilPlayer.sendData(EnumPlayerPacket.BankSlotOpen, id, container.bankid);
		}
		if (id == 8) {
			NoppesUtilPlayer.sendData(EnumPlayerPacket.BankUnlock, new Object[0]);
		}
		if (id == 9) {
			NoppesUtilPlayer.sendData(EnumPlayerPacket.BankUpgrade, new Object[0]);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(resource);
		int l = (width - xSize) / 2;
		int i2 = (height - ySize) / 2;
		this.drawTexturedModalRect(l, i2, 0, 0, xSize, 6);
		if (!container.isAvailable()) {
			this.drawTexturedModalRect(l, i2 + 6, 0, 6, xSize, 64);
			this.drawTexturedModalRect(l, i2 + 70, 0, 124, xSize, 98);
			int x = guiLeft + 30;
			int y = guiTop + 8;
			fontRendererObj.drawString(StatCollector.translateToLocal("bank.unlockCosts") + ":", x, y + 4,
					CustomNpcResourceListener.DefaultTextColor);
			drawItem(x + 90, y, currency, i, j);
		} else if (container.isUpgraded()) {
			this.drawTexturedModalRect(l, i2 + 60, 0, 60, xSize, 162);
			this.drawTexturedModalRect(l, i2 + 6, 0, 60, xSize, 64);
		} else if (container.canBeUpgraded()) {
			this.drawTexturedModalRect(l, i2 + 6, 0, 6, xSize, 216);
			int x = guiLeft + 30;
			int y = guiTop + 8;
			fontRendererObj.drawString(StatCollector.translateToLocal("bank.upgradeCosts") + ":", x, y + 4,
					CustomNpcResourceListener.DefaultTextColor);
			drawItem(x + 90, y, currency, i, j);
		} else {
			this.drawTexturedModalRect(l, i2 + 6, 0, 60, xSize, 162);
		}
		if (maxSlots > 1) {
			for (int ii = 0; ii < maxSlots; ++ii) {
				if (availableSlots == ii) {
					break;
				}
				fontRendererObj.drawString("Tab " + (ii + 1), guiLeft - 40, guiTop + 16 + (ii * 24), 16777215);
			}
		}
		super.drawGuiContainerBackgroundLayer(f, i, j);
	}

	private void drawItem(int x, int y, ItemStack item, int mouseX, int mouseY) {
		if (item == null) {
			return;
		}
		GlStateManager.enableRescaleNormal();
		RenderHelper.enableGUIStandardItemLighting();
		itemRender.renderItemAndEffectIntoGUI(item, x, y);
		itemRender.renderItemOverlays(fontRendererObj, item, x, y);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		if (isPointInRegion(x - guiLeft, y - guiTop, 16, 16, mouseX, mouseY)) {
			renderToolTip(item, mouseX, mouseY);
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		availableSlots = 0;
		if (maxSlots > 1) {
			for (int i = 0; i < maxSlots; ++i) {
				GuiNpcButton button = new GuiNpcButton(i, guiLeft - 50, guiTop + 10 + (i * 24), 50, 20,
						"Tab " + (i + 1));
				if (i > unlockedSlots) {
					button.setEnabled(false);
				}
				addButton(button);
				++availableSlots;
			}
			if (availableSlots == 1) {
				buttonList.clear();
			}
		}
		if (!container.isAvailable()) {
			addButton(new GuiNpcButton(8, guiLeft + 48, guiTop + 48, 80, 20,
					StatCollector.translateToLocal("bank.unlock")));
		} else if (container.canBeUpgraded()) {
			addButton(new GuiNpcButton(9, guiLeft + 48, guiTop + 48, 80, 20,
					StatCollector.translateToLocal("bank.upgrade")));
		}
		if (maxSlots > 1) {
			getButton(container.slot).visible = false;
			getButton(container.slot).setEnabled(false);
		}
	}

	@Override
	public void save() {
	}

	@Override
	public void setGuiData(NBTTagCompound compound) {
		maxSlots = compound.getInteger("MaxSlots");
		unlockedSlots = compound.getInteger("UnlockedSlots");
		if (compound.hasKey("Currency")) {
			currency = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("Currency"));
		} else {
			currency = null;
		}
		if (container.currency != null) {
			container.currency.item = currency;
		}
		initGui();
	}
}

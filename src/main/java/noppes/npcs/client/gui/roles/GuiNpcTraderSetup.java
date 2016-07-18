//

//

package noppes.npcs.client.gui.roles;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.Client;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButtonYesNo;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.containers.ContainerNPCTraderSetup;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleTrader;

public class GuiNpcTraderSetup extends GuiContainerNPCInterface2 implements ITextfieldListener {
	private final ResourceLocation slot;
	private RoleTrader role;

	public GuiNpcTraderSetup(final EntityNPCInterface npc, final ContainerNPCTraderSetup container) {
		super(npc, container);
		slot = new ResourceLocation("customnpcs", "textures/gui/slot.png");
		ySize = 220;
		menuYOffset = 10;
		role = container.role;
	}

	@Override
	public void actionPerformed(final GuiButton guibutton) {
		if (guibutton.id == 1) {
			role.ignoreDamage = ((GuiNpcButtonYesNo) guibutton).getBoolean();
		}
		if (guibutton.id == 2) {
			role.ignoreNBT = ((GuiNpcButtonYesNo) guibutton).getBoolean();
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int i, final int j) {
		super.drawGuiContainerBackgroundLayer(f, i, j);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		for (int slot = 0; slot < 18; ++slot) {
			final int x = guiLeft + ((slot % 3) * 94) + 7;
			final int y = guiTop + ((slot / 3) * 22) + 4;
			mc.renderEngine.bindTexture(this.slot);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			this.drawTexturedModalRect(x - 1, y, 0, 0, 18, 18);
			this.drawTexturedModalRect(x + 17, y, 0, 0, 18, 18);
			fontRendererObj.drawString("=", x + 36, y + 5, CustomNpcResourceListener.DefaultTextColor);
			mc.renderEngine.bindTexture(this.slot);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			this.drawTexturedModalRect(x + 42, y, 0, 0, 18, 18);
		}
	}

	@Override
	public void drawScreen(final int i, final int j, final float f) {
		guiTop += 10;
		super.drawScreen(i, j, f);
		guiTop -= 10;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.clear();
		setBackground("tradersetup.png");
		addLabel(new GuiNpcLabel(0, "role.marketname", guiLeft + 214, guiTop + 150));
		addTextField(new GuiNpcTextField(0, this, guiLeft + 214, guiTop + 160, 180, 20, role.marketName));
		addLabel(new GuiNpcLabel(1, "gui.ignoreDamage", guiLeft + 260, guiTop + 29));
		addButton(new GuiNpcButtonYesNo(1, guiLeft + 340, guiTop + 24, role.ignoreDamage));
		addLabel(new GuiNpcLabel(2, "gui.ignoreNBT", guiLeft + 260, guiTop + 51));
		addButton(new GuiNpcButtonYesNo(2, guiLeft + 340, guiTop + 46, role.ignoreNBT));
	}

	@Override
	public void save() {
		Client.sendData(EnumPacketServer.TraderMarketSave, role.marketName, false);
		Client.sendData(EnumPacketServer.RoleSave, role.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void unFocused(final GuiNpcTextField guiNpcTextField) {
		final String name = guiNpcTextField.getText();
		if (!name.equalsIgnoreCase(role.marketName)) {
			role.marketName = name;
			Client.sendData(EnumPacketServer.TraderMarketSave, role.marketName, true);
		}
	}
}

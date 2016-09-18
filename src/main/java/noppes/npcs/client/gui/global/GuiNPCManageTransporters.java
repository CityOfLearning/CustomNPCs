
package noppes.npcs.client.gui.global;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.client.Client;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.GuiNPCTransportCategoryEdit;
import noppes.npcs.client.gui.mainmenu.GuiNPCGlobalMainMenu;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNPCStringSlot;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCManageTransporters extends GuiNPCInterface implements IScrollData {
	private GuiNPCStringSlot slot;
	private HashMap<String, Integer> data;
	private boolean selectCategory;

	public GuiNPCManageTransporters(EntityNPCInterface npc) {
		super(npc);
		selectCategory = true;
		Client.sendData(EnumPacketServer.TransportCategoriesGet, new Object[0]);
		drawDefaultBackground = false;
		title = "Transport Categories";
		data = new HashMap<String, Integer>();
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		int id = guibutton.id;
		if ((id == 0) && selectCategory) {
			NoppesUtil.openGUI(player, new GuiNPCTransportCategoryEdit(npc, this, "", -1));
		}
		if (id == 1) {
			if ((slot.selected == null) || slot.selected.isEmpty()) {
				return;
			}
			if (selectCategory) {
				NoppesUtil.openGUI(player,
						new GuiNPCTransportCategoryEdit(npc, this, slot.selected, data.get(slot.selected)));
			}
		}
		if (id == 4) {
			if (selectCategory) {
				close();
				NoppesUtil.openGUI(player, new GuiNPCGlobalMainMenu(npc));
			} else {
				title = "Transport Categories";
				selectCategory = true;
				Client.sendData(EnumPacketServer.TransportCategoriesGet, new Object[0]);
				initGui();
			}
		}
		if (id == 3) {
			if ((slot.selected == null) || slot.selected.isEmpty()) {
				return;
			}
			save();
			if (selectCategory) {
				Client.sendData(EnumPacketServer.TransportCategoryRemove, data.get(slot.selected));
			} else {
				Client.sendData(EnumPacketServer.TransportRemove, data.get(slot.selected));
			}
			initGui();
		}
		if (id == 2) {
			doubleClicked();
		}
	}

	@Override
	public void doubleClicked() {
		if ((slot.selected == null) || slot.selected.isEmpty()) {
			return;
		}
		if (selectCategory) {
			selectCategory = false;
			title = "TransportLocations";
			Client.sendData(EnumPacketServer.TransportsGet, data.get(slot.selected));
			initGui();
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		slot.drawScreen(i, j, f);
		super.drawScreen(i, j, f);
	}

	@Override
	public void handleMouseInput() throws IOException {
		slot.handleMouseInput();
		super.handleMouseInput();
	}

	@Override
	public void initGui() {
		super.initGui();
		Vector<String> list = new Vector<String>();
		(slot = new GuiNPCStringSlot(list, this, false, 18)).registerScrollButtons(4, 5);
		addButton(new GuiNpcButton(0, (width / 2) - 100, height - 52, 65, 20, "gui.add"));
		addButton(new GuiNpcButton(1, (width / 2) - 33, height - 52, 65, 20, "selectServer.edit"));
		getButton(0).setEnabled(selectCategory);
		getButton(1).setEnabled(selectCategory);
		addButton(new GuiNpcButton(3, (width / 2) + 33, height - 52, 65, 20, "gui.remove"));
		addButton(new GuiNpcButton(2, (width / 2) - 100, height - 31, 98, 20, "gui.open"));
		getButton(2).setEnabled(selectCategory);
		addButton(new GuiNpcButton(4, (width / 2) + 2, height - 31, 98, 20, "gui.back"));
	}

	@Override
	public void save() {
	}

	@Override
	public void setData(Vector<String> list, HashMap<String, Integer> data) {
		this.data = data;
		slot.setList(list);
	}

	@Override
	public void setSelected(String selected) {
	}
}

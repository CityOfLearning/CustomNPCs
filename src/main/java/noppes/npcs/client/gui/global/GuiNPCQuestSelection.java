
package noppes.npcs.client.gui.global;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import noppes.npcs.client.Client;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNPCStringSlot;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiSelectionListener;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCQuestSelection extends GuiNPCInterface implements IScrollData {
	private GuiNPCStringSlot slot;
	private GuiScreen parent;
	private HashMap<String, Integer> data;
	private boolean selectCategory;
	public GuiSelectionListener listener;
	private int quest;

	public GuiNPCQuestSelection(EntityNPCInterface npc, GuiScreen parent, int quest) {
		super(npc);
		selectCategory = true;
		drawDefaultBackground = false;
		title = "Select Quest Category";
		this.parent = parent;
		data = new HashMap<>();
		this.quest = quest;
		if (parent instanceof GuiSelectionListener) {
			listener = (GuiSelectionListener) parent;
		}
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		int id = guibutton.id;
		if (id == 2) {
			if (selectCategory) {
				close();
				NoppesUtil.openGUI(player, parent);
			} else {
				title = "Select Dialog Category";
				selectCategory = true;
				Client.sendData(EnumPacketServer.QuestCategoriesGet, quest);
			}
		}
		if (id == 4) {
			if ((slot.selected == null) || slot.selected.isEmpty()) {
				return;
			}
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
			title = "Select Quest";
			Client.sendData(EnumPacketServer.QuestsGet, data.get(slot.selected));
		} else {
			quest = data.get(slot.selected);
			close();
			NoppesUtil.openGUI(player, parent);
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		slot.drawScreen(i, j, f);
		super.drawScreen(i, j, f);
	}

	public String getSelected() {
		return slot.selected;
	}

	@Override
	public void handleMouseInput() throws IOException {
		slot.handleMouseInput();
		super.handleMouseInput();
	}

	@Override
	public void initGui() {
		super.initGui();
		Vector<String> list = new Vector<>();
		(slot = new GuiNPCStringSlot(list, this, false, 18)).registerScrollButtons(4, 5);
		addButton(new GuiNpcButton(2, (width / 2) - 100, height - 41, 98, 20, "gui.back"));
		addButton(new GuiNpcButton(4, (width / 2) + 2, height - 41, 98, 20, "mco.template.button.select"));
	}

	@Override
	public void initPacket() {
		if (quest >= 0) {
			Client.sendData(EnumPacketServer.QuestsGetFromQuest, quest);
			selectCategory = false;
			title = "Select Dialog";
		} else {
			Client.sendData(EnumPacketServer.QuestCategoriesGet, quest);
		}
	}

	@Override
	public void save() {
		if ((quest >= 0) && (listener != null)) {
			listener.selected(quest, slot.selected);
		}
	}

	@Override
	public void setData(Vector<String> list, HashMap<String, Integer> data) {
		this.data = data;
		slot.setList(list);
		if (quest >= 0) {
			for (String name : data.keySet()) {
				if (data.get(name) == quest) {
					slot.selected = name;
				}
			}
		}
	}

	@Override
	public void setSelected(String selected) {
	}
}

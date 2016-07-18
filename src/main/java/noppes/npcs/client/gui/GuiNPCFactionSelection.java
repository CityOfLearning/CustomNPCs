//

//

package noppes.npcs.client.gui;

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

public class GuiNPCFactionSelection extends GuiNPCInterface implements IScrollData {
	private GuiNPCStringSlot slot;
	private GuiScreen parent;
	private HashMap<String, Integer> data;
	private int factionId;
	public GuiSelectionListener listener;

	public GuiNPCFactionSelection(final EntityNPCInterface npc, final GuiScreen parent, final int dialog) {
		super(npc);
		data = new HashMap<String, Integer>();
		drawDefaultBackground = false;
		title = "Select Dialog Category";
		this.parent = parent;
		factionId = dialog;
		if (parent instanceof GuiSelectionListener) {
			listener = (GuiSelectionListener) parent;
		}
	}

	@Override
	protected void actionPerformed(final GuiButton guibutton) {
		final int id = guibutton.id;
		if (id == 2) {
			close();
			NoppesUtil.openGUI(player, parent);
		}
		if (id == 4) {
			doubleClicked();
		}
	}

	@Override
	public void doubleClicked() {
		if ((slot.selected == null) || slot.selected.isEmpty()) {
			return;
		}
		factionId = data.get(slot.selected);
		close();
		NoppesUtil.openGUI(player, parent);
	}

	@Override
	public void drawScreen(final int i, final int j, final float f) {
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
		final Vector<String> list = new Vector<String>();
		(slot = new GuiNPCStringSlot(list, this, false, 18)).registerScrollButtons(4, 5);
		addButton(new GuiNpcButton(2, (width / 2) - 100, height - 41, 98, 20, "gui.back"));
		addButton(new GuiNpcButton(4, (width / 2) + 2, height - 41, 98, 20, "mco.template.button.select"));
	}

	@Override
	public void initPacket() {
		Client.sendData(EnumPacketServer.FactionsGet, new Object[0]);
	}

	@Override
	public void save() {
		if ((factionId >= 0) && (listener != null)) {
			listener.selected(factionId, slot.selected);
		}
	}

	@Override
	public void setData(final Vector<String> list, final HashMap<String, Integer> data) {
		this.data = data;
		slot.setList(list);
		if (factionId >= 0) {
			for (final String name : data.keySet()) {
				if (data.get(name) == factionId) {
					slot.selected = name;
				}
			}
		}
	}

	@Override
	public void setSelected(final String selected) {
	}
}

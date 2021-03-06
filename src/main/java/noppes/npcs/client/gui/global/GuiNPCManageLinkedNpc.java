
package noppes.npcs.client.gui.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.SubGuiEditText;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.client.gui.util.ISubGuiListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCManageLinkedNpc extends GuiNPCInterface2 implements IScrollData, ISubGuiListener {
	public static GuiScreen Instance;
	private GuiCustomScroll scroll;
	private List<String> data;

	public GuiNPCManageLinkedNpc(EntityNPCInterface npc) {
		super(npc);
		data = new ArrayList<>();
		GuiNPCManageLinkedNpc.Instance = this;
		Client.sendData(EnumPacketServer.LinkedGetAll, new Object[0]);
	}

	@Override
	public void buttonEvent(GuiButton button) {
		if (button.id == 1) {
			save();
			setSubGui(new SubGuiEditText("New"));
		}
		if ((button.id == 2) && scroll.hasSelected()) {
			Client.sendData(EnumPacketServer.LinkedRemove, scroll.getSelected());
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		addButton(new GuiNpcButton(1, guiLeft + 358, guiTop + 38, 58, 20, "gui.add"));
		addButton(new GuiNpcButton(2, guiLeft + 358, guiTop + 61, 58, 20, "gui.remove"));
		if (scroll == null) {
			(scroll = new GuiCustomScroll(this, 0)).setSize(143, 208);
		}
		scroll.guiLeft = guiLeft + 214;
		scroll.guiTop = guiTop + 4;
		scroll.setList(data);
		addScroll(scroll);
	}

	@Override
	public void save() {
	}

	@Override
	public void setData(Vector<String> list, HashMap<String, Integer> data) {
		this.data = new ArrayList<>(list);
		initGui();
	}

	@Override
	public void setSelected(String selected) {
	}

	@Override
	public void subGuiClosed(SubGuiInterface subgui) {
		if (!((SubGuiEditText) subgui).cancelled) {
			Client.sendData(EnumPacketServer.LinkedAdd, ((SubGuiEditText) subgui).text);
		}
	}
}

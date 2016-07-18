//

//

package noppes.npcs.client.gui.advanced;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCAdvancedLinkedNpc extends GuiNPCInterface2 implements IScrollData, ICustomScrollListener {
	public static GuiScreen Instance;
	private GuiCustomScroll scroll;
	private List<String> data;

	public GuiNPCAdvancedLinkedNpc(final EntityNPCInterface npc) {
		super(npc);
		data = new ArrayList<String>();
		GuiNPCAdvancedLinkedNpc.Instance = this;
	}

	@Override
	public void buttonEvent(final GuiButton button) {
		if (button.id == 1) {
			Client.sendData(EnumPacketServer.LinkedSet, "");
		}
	}

	@Override
	public void customScrollClicked(final int i, final int j, final int k, final GuiCustomScroll guiCustomScroll) {
		Client.sendData(EnumPacketServer.LinkedSet, guiCustomScroll.getSelected());
	}

	@Override
	public void initGui() {
		super.initGui();
		addButton(new GuiNpcButton(1, guiLeft + 358, guiTop + 38, 58, 20, "gui.clear"));
		if (scroll == null) {
			(scroll = new GuiCustomScroll(this, 0)).setSize(143, 208);
		}
		scroll.guiLeft = guiLeft + 137;
		scroll.guiTop = guiTop + 4;
		scroll.setSelected(npc.linkedName);
		scroll.setList(data);
		addScroll(scroll);
	}

	@Override
	public void initPacket() {
		Client.sendData(EnumPacketServer.LinkedGetAll, new Object[0]);
	}

	@Override
	public void save() {
	}

	@Override
	public void setData(final Vector<String> list, final HashMap<String, Integer> data) {
		this.data = new ArrayList<String>(list);
		initGui();
	}

	@Override
	public void setSelected(final String selected) {
		scroll.setSelected(selected);
	}
}

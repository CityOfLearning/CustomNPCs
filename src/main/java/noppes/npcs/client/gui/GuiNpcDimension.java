
package noppes.npcs.client.gui;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;
import noppes.npcs.client.Client;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketServer;

public class GuiNpcDimension extends GuiNPCInterface implements IScrollData {
	private GuiCustomScroll scroll;
	private HashMap<String, Integer> data;

	public GuiNpcDimension() {
		data = new HashMap<String, Integer>();
		xSize = 256;
		setBackground("menubg.png");
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		int id = guibutton.id;
		if (!data.containsKey(scroll.getSelected())) {
			return;
		}
		if (id == 4) {
			Client.sendData(EnumPacketServer.DimensionTeleport, data.get(scroll.getSelected()));
			close();
		}
	}

	@Override
	public void confirmClicked(boolean flag, int i) {
		if (flag) {
			Client.sendData(EnumPacketServer.RemoteDelete, data.get(scroll.getSelected()));
		}
		NoppesUtil.openGUI(player, this);
	}

	@Override
	public void initGui() {
		super.initGui();
		if (scroll == null) {
			(scroll = new GuiCustomScroll(this, 0)).setSize(165, 208);
		}
		scroll.guiLeft = guiLeft + 4;
		scroll.guiTop = guiTop + 4;
		addScroll(scroll);
		String title = StatCollector.translateToLocal("Dimensions");
		int x = (xSize - fontRendererObj.getStringWidth(title)) / 2;
		addLabel(new GuiNpcLabel(0, title, guiLeft + x, guiTop - 8));
		addButton(new GuiNpcButton(4, guiLeft + 170, guiTop + 72, 82, 20, "remote.tp"));
	}

	@Override
	public void initPacket() {
		Client.sendData(EnumPacketServer.DimensionsGet, new Object[0]);
	}

	@Override
	public void keyTyped(char c, int i) {
		if ((i == 1) || isInventoryKey(i)) {
			close();
		}
	}

	@Override
	public void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		scroll.mouseClicked(i, j, k);
	}

	@Override
	public void save() {
	}

	@Override
	public void setData(Vector<String> list, HashMap<String, Integer> data) {
		scroll.setList(list);
		this.data = data;
	}

	@Override
	public void setSelected(String selected) {
		getButton(3).setDisplayText(selected);
	}
}

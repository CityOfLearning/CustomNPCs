//

//

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
	protected void actionPerformed(final GuiButton guibutton) {
		final int id = guibutton.id;
		if (!data.containsKey(scroll.getSelected())) {
			return;
		}
		if (id == 4) {
			Client.sendData(EnumPacketServer.DimensionTeleport, data.get(scroll.getSelected()));
			close();
		}
	}

	@Override
	public void confirmClicked(final boolean flag, final int i) {
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
		final String title = StatCollector.translateToLocal("Dimensions");
		final int x = (xSize - fontRendererObj.getStringWidth(title)) / 2;
		addLabel(new GuiNpcLabel(0, title, guiLeft + x, guiTop - 8));
		addButton(new GuiNpcButton(4, guiLeft + 170, guiTop + 72, 82, 20, "remote.tp"));
	}

	@Override
	public void initPacket() {
		Client.sendData(EnumPacketServer.DimensionsGet, new Object[0]);
	}

	@Override
	public void keyTyped(final char c, final int i) {
		if ((i == 1) || isInventoryKey(i)) {
			close();
		}
	}

	@Override
	public void mouseClicked(final int i, final int j, final int k) {
		super.mouseClicked(i, j, k);
		scroll.mouseClicked(i, j, k);
	}

	@Override
	public void save() {
	}

	@Override
	public void setData(final Vector<String> list, final HashMap<String, Integer> data) {
		scroll.setList(list);
		this.data = data;
	}

	@Override
	public void setSelected(final String selected) {
		getButton(3).setDisplayText(selected);
	}
}

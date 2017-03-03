
package noppes.npcs.client.gui;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.entity.Entity;
import net.minecraft.util.StatCollector;
import noppes.npcs.client.Client;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNpcRemoteEditor extends GuiNPCInterface implements IScrollData, GuiYesNoCallback {
	private GuiCustomScroll scroll;
	private HashMap<String, Integer> data;

	public GuiNpcRemoteEditor() {
		data = new HashMap<>();
		xSize = 256;
		setBackground("menubg.png");
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		int id = guibutton.id;
		if (id == 3) {
			Client.sendData(EnumPacketServer.RemoteFreeze, new Object[0]);
		}
		if (id == 5) {
			for (int ids : data.values()) {
				Client.sendData(EnumPacketServer.RemoteReset, ids);
				Entity entity = player.worldObj.getEntityByID(ids);
				if ((entity != null) && (entity instanceof EntityNPCInterface)) {
					((EntityNPCInterface) entity).reset();
				}
			}
		}
		if (!data.containsKey(scroll.getSelected())) {
			return;
		}
		if (id == 0) {
			Client.sendData(EnumPacketServer.RemoteMainMenu, data.get(scroll.getSelected()));
		}
		if (id == 1) {
			GuiYesNo guiyesno = new GuiYesNo(this, "Confirm", StatCollector.translateToLocal("gui.delete"), 0);
			displayGuiScreen(guiyesno);
		}
		if (id == 2) {
			Client.sendData(EnumPacketServer.RemoteReset, data.get(scroll.getSelected()));
			Entity entity2 = player.worldObj.getEntityByID(data.get(scroll.getSelected()));
			if ((entity2 != null) && (entity2 instanceof EntityNPCInterface)) {
				((EntityNPCInterface) entity2).reset();
			}
		}
		if (id == 4) {
			Client.sendData(EnumPacketServer.RemoteTpToNpc, data.get(scroll.getSelected()));
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
		String title = StatCollector.translateToLocal("remote.title");
		int x = (xSize - fontRendererObj.getStringWidth(title)) / 2;
		addLabel(new GuiNpcLabel(0, title, guiLeft + x, guiTop - 8));
		addButton(new GuiNpcButton(0, guiLeft + 170, guiTop + 6, 82, 20, "selectServer.edit"));
		addButton(new GuiNpcButton(1, guiLeft + 170, guiTop + 28, 82, 20, "selectWorld.deleteButton"));
		addButton(new GuiNpcButton(2, guiLeft + 170, guiTop + 50, 82, 20, "remote.reset"));
		addButton(new GuiNpcButton(4, guiLeft + 170, guiTop + 72, 82, 20, "remote.tp"));
		addButton(new GuiNpcButton(5, guiLeft + 170, guiTop + 110, 82, 20, "remote.resetall"));
		addButton(new GuiNpcButton(3, guiLeft + 170, guiTop + 132, 82, 20, "remote.freeze"));
	}

	@Override
	public void initPacket() {
		Client.sendData(EnumPacketServer.RemoteNpcsGet, new Object[0]);
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

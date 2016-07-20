//

//

package noppes.npcs.client.gui.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.SubGuiColorSelector;
import noppes.npcs.client.gui.SubGuiNpcFactionPoints;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.client.gui.util.ISubGuiListener;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.controllers.faction.Faction;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCManageFactions extends GuiNPCInterface2
		implements IScrollData, ICustomScrollListener, ITextfieldListener, IGuiData, ISubGuiListener {
	private GuiCustomScroll scrollFactions;
	private HashMap<String, Integer> data;
	private Faction faction;
	private String selected;

	public GuiNPCManageFactions(final EntityNPCInterface npc) {
		super(npc);
		data = new HashMap<String, Integer>();
		faction = new Faction();
		selected = null;
		Client.sendData(EnumPacketServer.FactionsGet, new Object[0]);
	}

	@Override
	protected void actionPerformed(final GuiButton guibutton) {
		final GuiNpcButton button = (GuiNpcButton) guibutton;
		if (button.id == 0) {
			save();
			String name;
			for (name = "New"; data.containsKey(name); name += "_") {
			}
			final Faction faction = new Faction(-1, name, 65280, 1000);
			final NBTTagCompound compound = new NBTTagCompound();
			faction.writeNBT(compound);
			Client.sendData(EnumPacketServer.FactionSave, compound);
		}
		if ((button.id == 1) && data.containsKey(scrollFactions.getSelected())) {
			Client.sendData(EnumPacketServer.FactionRemove, data.get(selected));
			scrollFactions.clear();
			faction = new Faction();
			initGui();
		}
		if (button.id == 2) {
			setSubGui(new SubGuiNpcFactionPoints(faction));
		}
		if (button.id == 3) {
			faction.hideFaction = (button.getValue() == 1);
		}
		if (button.id == 4) {
			faction.getsAttacked = (button.getValue() == 1);
		}
		if (button.id == 10) {
			setSubGui(new SubGuiColorSelector(faction.color));
		}
	}

	@Override
	public void customScrollClicked(final int i, final int j, final int k, final GuiCustomScroll guiCustomScroll) {
		if (guiCustomScroll.id == 0) {
			save();
			selected = scrollFactions.getSelected();
			Client.sendData(EnumPacketServer.FactionGet, data.get(selected));
		} else if (guiCustomScroll.id == 1) {
			final HashSet<Integer> set = new HashSet<Integer>();
			for (final String s : guiCustomScroll.getSelectedList()) {
				if (data.containsKey(s)) {
					set.add(data.get(s));
				}
			}
			faction.attackFactions = set;
			save();
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		addButton(new GuiNpcButton(0, guiLeft + 368, guiTop + 8, 45, 20, "gui.add"));
		addButton(new GuiNpcButton(1, guiLeft + 368, guiTop + 32, 45, 20, "gui.remove"));
		if (scrollFactions == null) {
			(scrollFactions = new GuiCustomScroll(this, 0)).setSize(143, 208);
		}
		scrollFactions.guiLeft = guiLeft + 220;
		scrollFactions.guiTop = guiTop + 4;
		addScroll(scrollFactions);
		if (faction.id == -1) {
			return;
		}
		addTextField(new GuiNpcTextField(0, this, guiLeft + 40, guiTop + 4, 136, 20, faction.name));
		getTextField(0).setMaxStringLength(20);
		addLabel(new GuiNpcLabel(0, "gui.name", guiLeft + 8, guiTop + 9));
		addLabel(new GuiNpcLabel(10, "ID", guiLeft + 178, guiTop + 4));
		addLabel(new GuiNpcLabel(11, faction.id + "", guiLeft + 178, guiTop + 14));
		String color;
		for (color = Integer.toHexString(faction.color); color.length() < 6; color = "0" + color) {
		}
		addButton(new GuiNpcButton(10, guiLeft + 40, guiTop + 26, 60, 20, color));
		addLabel(new GuiNpcLabel(1, "gui.color", guiLeft + 8, guiTop + 31));
		getButton(10).setTextColor(faction.color);
		addLabel(new GuiNpcLabel(2, "faction.points", guiLeft + 8, guiTop + 53));
		addButton(new GuiNpcButton(2, guiLeft + 100, guiTop + 48, 45, 20, "selectServer.edit"));
		addLabel(new GuiNpcLabel(3, "faction.hidden", guiLeft + 8, guiTop + 75));
		addButton(new GuiNpcButton(3, guiLeft + 100, guiTop + 70, 45, 20, new String[] { "gui.no", "gui.yes" },
				faction.hideFaction ? 1 : 0));
		addLabel(new GuiNpcLabel(4, "faction.attacked", guiLeft + 8, guiTop + 97));
		addButton(new GuiNpcButton(4, guiLeft + 100, guiTop + 92, 45, 20, new String[] { "gui.no", "gui.yes" },
				faction.getsAttacked ? 1 : 0));
		addLabel(new GuiNpcLabel(6, "faction.hostiles", guiLeft + 8, guiTop + 145));
		final ArrayList<String> hostileList = new ArrayList<String>(scrollFactions.getList());
		hostileList.remove(faction.name);
		final HashSet<String> set = new HashSet<String>();
		for (final String s : data.keySet()) {
			if (!s.equals(faction.name) && faction.attackFactions.contains(data.get(s))) {
				set.add(s);
			}
		}
		final GuiCustomScroll scrollHostileFactions = new GuiCustomScroll(this, 1, true);
		scrollHostileFactions.setSize(163, 58);
		scrollHostileFactions.guiLeft = guiLeft + 4;
		scrollHostileFactions.guiTop = guiTop + 154;
		scrollHostileFactions.setList(hostileList);
		scrollHostileFactions.setSelectedList(set);
		addScroll(scrollHostileFactions);
	}

	@Override
	public void save() {
		if ((selected != null) && data.containsKey(selected) && (faction != null)) {
			final NBTTagCompound compound = new NBTTagCompound();
			faction.writeNBT(compound);
			Client.sendData(EnumPacketServer.FactionSave, compound);
		}
	}

	@Override
	public void setData(final Vector<String> list, final HashMap<String, Integer> data) {
		final String name = scrollFactions.getSelected();
		this.data = data;
		scrollFactions.setList(list);
		if (name != null) {
			scrollFactions.setSelected(name);
		}
	}

	@Override
	public void setGuiData(final NBTTagCompound compound) {
		(faction = new Faction()).readNBT(compound);
		setSelected(faction.name);
		initGui();
	}

	@Override
	public void setSelected(final String selected) {
		this.selected = selected;
		scrollFactions.setSelected(selected);
	}

	@Override
	public void subGuiClosed(final SubGuiInterface subgui) {
		if (subgui instanceof SubGuiColorSelector) {
			faction.color = ((SubGuiColorSelector) subgui).color;
			initGui();
		}
	}

	@Override
	public void unFocused(final GuiNpcTextField guiNpcTextField) {
		if (faction.id == -1) {
			return;
		}
		if (guiNpcTextField.id == 0) {
			final String name = guiNpcTextField.getText();
			if (!name.isEmpty() && !data.containsKey(name)) {
				final String old = faction.name;
				data.remove(faction.name);
				faction.name = name;
				data.put(faction.name, faction.id);
				selected = name;
				scrollFactions.replace(old, faction.name);
			}
		} else if (guiNpcTextField.id == 1) {
			int color = 0;
			try {
				color = Integer.parseInt(guiNpcTextField.getText(), 16);
			} catch (NumberFormatException e) {
				color = 0;
			}
			guiNpcTextField.setTextColor(faction.color = color);
		}
	}
}

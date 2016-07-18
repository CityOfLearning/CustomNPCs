//

//

package noppes.npcs.client.gui.advanced;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.SubGuiNpcFactionOptions;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCFactionSetup extends GuiNPCInterface2 implements IScrollData, ICustomScrollListener {
	private GuiCustomScroll scrollFactions;
	private HashMap<String, Integer> data;

	public GuiNPCFactionSetup(final EntityNPCInterface npc) {
		super(npc);
		data = new HashMap<String, Integer>();
	}

	@Override
	public void buttonEvent(final GuiButton guibutton) {
		final GuiNpcButton button = (GuiNpcButton) guibutton;
		if (button.id == 0) {
			npc.advanced.attackOtherFactions = (button.getValue() == 1);
		}
		if (button.id == 1) {
			npc.advanced.defendFaction = (button.getValue() == 1);
		}
		if (button.id == 12) {
			setSubGui(new SubGuiNpcFactionOptions(npc.advanced.factions));
		}
	}

	@Override
	public void customScrollClicked(final int i, final int j, final int k, final GuiCustomScroll guiCustomScroll) {
		if (guiCustomScroll.id == 0) {
			Client.sendData(EnumPacketServer.FactionSet, data.get(scrollFactions.getSelected()));
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		addLabel(new GuiNpcLabel(0, "faction.attackHostile", guiLeft + 4, guiTop + 25));
		addButton(new GuiNpcButton(0, guiLeft + 144, guiTop + 20, 40, 20, new String[] { "gui.no", "gui.yes" },
				npc.advanced.attackOtherFactions ? 1 : 0));
		addLabel(new GuiNpcLabel(1, "faction.defend", guiLeft + 4, guiTop + 47));
		addButton(new GuiNpcButton(1, guiLeft + 144, guiTop + 42, 40, 20, new String[] { "gui.no", "gui.yes" },
				npc.advanced.defendFaction ? 1 : 0));
		addLabel(new GuiNpcLabel(12, "faction.ondeath", guiLeft + 4, guiTop + 69));
		addButton(new GuiNpcButton(12, guiLeft + 90, guiTop + 64, 80, 20, "faction.points"));
		if (scrollFactions == null) {
			(scrollFactions = new GuiCustomScroll(this, 0)).setSize(180, 200);
		}
		scrollFactions.guiLeft = guiLeft + 200;
		scrollFactions.guiTop = guiTop + 4;
		addScroll(scrollFactions);
		Client.sendData(EnumPacketServer.FactionsGet, new Object[0]);
	}

	@Override
	public void mouseClicked(final int i, final int j, final int k) {
		super.mouseClicked(i, j, k);
		if ((k == 0) && (scrollFactions != null)) {
			scrollFactions.mouseClicked(i, j, k);
		}
	}

	@Override
	public void save() {
		Client.sendData(EnumPacketServer.MainmenuAdvancedSave, npc.advanced.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void setData(final Vector<String> list, final HashMap<String, Integer> data) {
		final String name = npc.getFaction().name;
		this.data = data;
		scrollFactions.setList(list);
		if (name != null) {
			setSelected(name);
		}
	}

	@Override
	public void setSelected(final String selected) {
		scrollFactions.setSelected(selected);
	}
}

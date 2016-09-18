
package noppes.npcs.client.gui.advanced;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.GuiNPCLinesEdit;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcButtonYesNo;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCLinesMenu extends GuiNPCInterface2 {
	public GuiNPCLinesMenu(EntityNPCInterface npc) {
		super(npc);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		int id = guibutton.id;
		if (id == 0) {
			NoppesUtil.openGUI(player, new GuiNPCLinesEdit(npc, npc.advanced.worldLines));
		}
		if (id == 1) {
			NoppesUtil.openGUI(player, new GuiNPCLinesEdit(npc, npc.advanced.attackLines));
		}
		if (id == 2) {
			NoppesUtil.openGUI(player, new GuiNPCLinesEdit(npc, npc.advanced.interactLines));
		}
		if (id == 5) {
			NoppesUtil.openGUI(player, new GuiNPCLinesEdit(npc, npc.advanced.killedLines));
		}
		if (id == 6) {
			NoppesUtil.openGUI(player, new GuiNPCLinesEdit(npc, npc.advanced.killLines));
		}
		if (id == 16) {
			npc.advanced.orderedLines = !((GuiNpcButtonYesNo) guibutton).getBoolean();
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		addButton(new GuiNpcButton(0, guiLeft + 85, guiTop + 20, "lines.world"));
		addButton(new GuiNpcButton(1, guiLeft + 85, guiTop + 43, "lines.attack"));
		addButton(new GuiNpcButton(2, guiLeft + 85, guiTop + 66, "lines.interact"));
		addButton(new GuiNpcButton(5, guiLeft + 85, guiTop + 89, "lines.killed"));
		addButton(new GuiNpcButton(6, guiLeft + 85, guiTop + 112, "lines.kill"));
		addLabel(new GuiNpcLabel(16, "lines.random", guiLeft + 85, guiTop + 157));
		addButton(new GuiNpcButtonYesNo(16, guiLeft + 175, guiTop + 152, !npc.advanced.orderedLines));
	}

	@Override
	public void save() {
		Client.sendData(EnumPacketServer.MainmenuAdvancedSave, npc.advanced.writeToNBT(new NBTTagCompound()));
	}
}

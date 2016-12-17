
package noppes.npcs.client.gui.mainmenu;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.global.GuiNpcManagePlayerData;
import noppes.npcs.client.gui.global.GuiNpcNaturalSpawns;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.NoppesStringUtils;

public class GuiNPCGlobalMainMenu extends GuiNPCInterface2 {
	public GuiNPCGlobalMainMenu(EntityNPCInterface npc) {
		super(npc, 5);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		int id = guibutton.id;
		if (id == 11) {
			NoppesUtil.requestOpenGUI(EnumGuiType.ManageQuests);
		}
		if (id == 2) {
			NoppesUtil.requestOpenGUI(EnumGuiType.ManageBanks);
		}
		if (id == 3) {
			NoppesUtil.requestOpenGUI(EnumGuiType.ManageFactions);
		}
		if (id == 4) {
			NoppesUtil.requestOpenGUI(EnumGuiType.ManageDialogs);
		}
		if (id == 12) {
			NoppesUtil.requestOpenGUI(EnumGuiType.ManageTransport);
		}
		if (id == 13) {
			NoppesUtil.openGUI(player, new GuiNpcManagePlayerData(npc, this));
		}
		if (id == 14) {
			NoppesUtil.requestOpenGUI(EnumGuiType.ManageRecipes, 4, 0, 0);
		}
		if (id == 15) {
			NoppesUtil.openGUI(player, new GuiNpcNaturalSpawns(npc));
		}
		if (id == 16) {
			NoppesUtil.requestOpenGUI(EnumGuiType.ManageLinked);
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		int y = guiTop + 10;
		addButton(new GuiNpcButton(2, guiLeft + 85, y, "global.banks"));
		int i = 3;
		int j = guiLeft + 85;
		y += 22;
		addButton(new GuiNpcButton(i, j, y, "menu.factions"));
		int k = 4;
		int l = guiLeft + 85;
		y += 22;
		addButton(new GuiNpcButton(k, l, y, "dialog.dialogs"));
		int m = 11;
		int j2 = guiLeft + 85;
		y += 22;
		addButton(new GuiNpcButton(m, j2, y, "quest.quests"));
		int i2 = 12;
		int j3 = guiLeft + 85;
		y += 22;
		addButton(new GuiNpcButton(i2, j3, y, "global.transport"));
		int i3 = 13;
		int j4 = guiLeft + 85;
		y += 22;
		addButton(new GuiNpcButton(i3, j4, y, "global.playerdata"));
		int i4 = 14;
		int j5 = guiLeft + 85;
		y += 22;
		addButton(new GuiNpcButton(i4, j5, y, "global.recipes"));
		int i5 = 15;
		int j6 = guiLeft + 85;
		y += 22;
		addButton(new GuiNpcButton(i5, j6, y, NoppesStringUtils.translate("global.naturalspawn", "(WIP)")));
		int i6 = 16;
		int j7 = guiLeft + 85;
		y += 22;
		addButton(new GuiNpcButton(i6, j7, y, "global.linked"));
	}

	@Override
	public void save() {
	}
}


package noppes.npcs.client.gui.global;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.SubGuiMailmanSendSetup;
import noppes.npcs.client.gui.SubGuiNpcCommand;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcButtonYesNo;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.ISubGuiListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.controllers.dialog.Dialog;
import noppes.npcs.controllers.mail.PlayerMail;

public class SubGuiNpcDialogExtra extends SubGuiInterface implements ISubGuiListener {
	private Dialog dialog;
	public GuiScreen parent2;

	public SubGuiNpcDialogExtra(Dialog dialog, GuiScreen parent) {
		parent2 = parent;
		this.dialog = dialog;
		setBackground("menubg.png");
		xSize = 256;
		ySize = 216;
		closeOnEsc = true;
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		GuiNpcButton button = (GuiNpcButton) guibutton;
		if (button.id == 10) {
			setSubGui(new SubGuiNpcCommand(dialog.command));
		}
		if (button.id == 11) {
			dialog.hideNPC = (button.getValue() == 1);
		}
		if (button.id == 12) {
			dialog.showWheel = (button.getValue() == 1);
		}
		if (button.id == 15) {
			dialog.disableEsc = (button.getValue() == 1);
		}
		if (button.id == 13) {
			setSubGui(new SubGuiMailmanSendSetup(dialog.mail, getParent()));
		}
		if (button.id == 14) {
			dialog.mail = new PlayerMail();
			initGui();
		}
		if (button.id == 66) {
			close();
			if (parent2 != null) {
				NoppesUtil.openGUI(player, parent2);
			}
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		int y = guiTop + 4;
		addButton(new GuiNpcButton(13, guiLeft + 4, y, 164, 20, "mailbox.setup"));
		addButton(new GuiNpcButton(14, guiLeft + 170, y, 20, 20, "X"));
		if (!dialog.mail.subject.isEmpty()) {
			getButton(13).setDisplayText(dialog.mail.subject);
		}
		int i = 10;
		int j = guiLeft + 120;
		y += 22;
		addButton(new GuiNpcButton(i, j, y, 50, 20, "selectServer.edit"));
		addLabel(new GuiNpcLabel(10, "advMode.command", guiLeft + 4, y + 5));
		int id = 11;
		int x = guiLeft + 120;
		y += 22;
		addButton(new GuiNpcButtonYesNo(id, x, y, dialog.hideNPC));
		addLabel(new GuiNpcLabel(11, "dialog.hideNPC", guiLeft + 4, y + 5));
		int id2 = 12;
		int x2 = guiLeft + 120;
		y += 22;
		addButton(new GuiNpcButtonYesNo(id2, x2, y, dialog.showWheel));
		addLabel(new GuiNpcLabel(12, "dialog.showWheel", guiLeft + 4, y + 5));
		int id3 = 15;
		int x3 = guiLeft + 120;
		y += 22;
		addButton(new GuiNpcButtonYesNo(id3, x3, y, dialog.disableEsc));
		addLabel(new GuiNpcLabel(15, "dialog.disableEsc", guiLeft + 4, y + 5));
		addButton(new GuiNpcButton(66, guiLeft + 82, guiTop + 192, 98, 20, "gui.done"));
	}

	@Override
	public void subGuiClosed(SubGuiInterface subgui) {
		if (subgui instanceof SubGuiNpcCommand) {
			dialog.command = ((SubGuiNpcCommand) subgui).command;
		}
	}
}

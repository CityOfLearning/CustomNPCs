//

//

package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;

public class SubGuiNpcCommand extends SubGuiInterface implements ITextfieldListener {
	public String command;

	public SubGuiNpcCommand(final String command) {
		this.command = command;
		setBackground("menubg.png");
		xSize = 256;
		ySize = 216;
		closeOnEsc = true;
	}

	@Override
	protected void actionPerformed(final GuiButton guibutton) {
		final int id = guibutton.id;
		if (id == 66) {
			close();
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		addTextField(new GuiNpcTextField(4, this, fontRendererObj, guiLeft + 4, guiTop + 84, 248, 20, command));
		getTextField(4).setMaxStringLength(32767);
		addLabel(new GuiNpcLabel(4, "advMode.command", guiLeft + 4, guiTop + 110));
		addLabel(new GuiNpcLabel(5, "advMode.nearestPlayer", guiLeft + 4, guiTop + 125));
		addLabel(new GuiNpcLabel(6, "advMode.randomPlayer", guiLeft + 4, guiTop + 140));
		addLabel(new GuiNpcLabel(7, "advMode.allPlayers", guiLeft + 4, guiTop + 155));
		addLabel(new GuiNpcLabel(8, "dialog.commandoptionplayer", guiLeft + 4, guiTop + 170));
		addButton(new GuiNpcButton(66, guiLeft + 82, guiTop + 190, 98, 20, "gui.done"));
	}

	@Override
	public void unFocused(final GuiNpcTextField textfield) {
		if (textfield.id == 4) {
			command = textfield.getText();
		}
	}
}

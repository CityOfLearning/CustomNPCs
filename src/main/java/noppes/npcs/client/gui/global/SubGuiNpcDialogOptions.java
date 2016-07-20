//

//

package noppes.npcs.client.gui.global;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.api.constants.EnumOptionType;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.controllers.dialog.Dialog;
import noppes.npcs.controllers.dialog.DialogOption;

public class SubGuiNpcDialogOptions extends SubGuiInterface {
	private Dialog dialog;

	public SubGuiNpcDialogOptions(Dialog dialog) {
		this.dialog = dialog;
		setBackground("menubg.png");
		xSize = 256;
		ySize = 216;
		closeOnEsc = true;
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		int id = guibutton.id;
		if (id < 6) {
			DialogOption option = dialog.options.get(id);
			if (option == null) {
				dialog.options.put(id, option = new DialogOption());
				option.optionColor = SubGuiNpcDialogOption.LastColor;
			}
			setSubGui(new SubGuiNpcDialogOption(option));
		}
		if (id == 66) {
			close();
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		addLabel(new GuiNpcLabel(66, "dialog.options", guiLeft, guiTop + 4));
		getLabel(66).center(xSize);
		for (int i = 0; i < 6; ++i) {
			String optionString = "";
			DialogOption option = dialog.options.get(i);
			if ((option != null) && (option.optionType != EnumOptionType.DISABLED)) {
				optionString += option.title;
			}
			addLabel(new GuiNpcLabel(i + 10, i + 1 + ": ", guiLeft + 4, guiTop + 16 + (i * 32)));
			addLabel(new GuiNpcLabel(i, optionString, guiLeft + 14, guiTop + 12 + (i * 32)));
			addButton(new GuiNpcButton(i, guiLeft + 13, guiTop + 21 + (i * 32), 60, 20, "selectServer.edit"));
		}
		addButton(new GuiNpcButton(66, guiLeft + 82, guiTop + 194, 98, 20, "gui.done"));
	}
}

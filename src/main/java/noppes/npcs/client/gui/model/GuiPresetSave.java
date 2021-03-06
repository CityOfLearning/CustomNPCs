
package noppes.npcs.client.gui.model;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import noppes.npcs.client.controllers.Preset;
import noppes.npcs.client.controllers.PresetController;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.model.ModelData;

public class GuiPresetSave extends SubGuiInterface {
	private ModelData data;

	public GuiPresetSave(GuiScreen parent, ModelData data) {
		this.data = data;
		xSize = 200;
		drawDefaultBackground = true;
	}

	@Override
	protected void actionPerformed(GuiButton btn) {
		super.actionPerformed(btn);
		GuiNpcButton button = (GuiNpcButton) btn;
		if (button.id == 0) {
			String name = getTextField(0).getText().trim();
			if (name.isEmpty()) {
				return;
			}
			Preset preset = new Preset();
			preset.name = name;
			preset.data = data.copy();
			PresetController.instance.addPreset(preset);
		}
		close();
	}

	@Override
	public void initGui() {
		super.initGui();
		addTextField(new GuiNpcTextField(0, this, guiLeft, guiTop + 70, 200, 20, ""));
		addButton(new GuiNpcButton(0, guiLeft, guiTop + 100, 98, 20, "Save"));
		addButton(new GuiNpcButton(1, guiLeft + 100, guiTop + 100, 98, 20, "Cancel"));
	}
}

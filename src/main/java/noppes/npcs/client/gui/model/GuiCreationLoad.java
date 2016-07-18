//

//

package noppes.npcs.client.gui.model;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.client.controllers.Preset;
import noppes.npcs.client.controllers.PresetController;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiCreationLoad extends GuiCreationScreenInterface implements ICustomScrollListener {
	private List<String> list;
	private GuiCustomScroll scroll;

	public GuiCreationLoad(final EntityNPCInterface npc) {
		super(npc);
		list = new ArrayList<String>();
		active = 5;
		xOffset = 60;
		PresetController.instance.load();
	}

	@Override
	protected void actionPerformed(final GuiButton btn) {
		super.actionPerformed(btn);
		if ((btn.id == 10) && scroll.hasSelected()) {
			PresetController.instance.removePreset(scroll.getSelected());
			initGui();
		}
	}

	@Override
	public void customScrollClicked(final int i, final int j, final int k, final GuiCustomScroll scroll) {
		final Preset preset = PresetController.instance.getPreset(scroll.getSelected());
		playerdata.readFromNBT(preset.data.writeToNBT());
		initGui();
	}

	@Override
	public void initGui() {
		super.initGui();
		if (scroll == null) {
			scroll = new GuiCustomScroll(this, 0);
		}
		list.clear();
		for (final Preset preset : PresetController.instance.presets.values()) {
			list.add(preset.name);
		}
		scroll.setList(list);
		scroll.guiLeft = guiLeft;
		scroll.guiTop = guiTop + 45;
		scroll.setSize(100, ySize - 96);
		addScroll(scroll);
		addButton(new GuiNpcButton(10, guiLeft, (guiTop + ySize) - 46, 120, 20, "gui.remove"));
	}
}

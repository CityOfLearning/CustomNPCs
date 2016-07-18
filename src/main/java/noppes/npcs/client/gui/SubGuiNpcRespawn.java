//

//

package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.entity.data.DataStats;

public class SubGuiNpcRespawn extends SubGuiInterface implements ITextfieldListener {
	private DataStats stats;

	public SubGuiNpcRespawn(final DataStats stats) {
		this.stats = stats;
		setBackground("menubg.png");
		xSize = 256;
		ySize = 216;
		closeOnEsc = true;
	}

	@Override
	protected void actionPerformed(final GuiButton guibutton) {
		final int id = guibutton.id;
		final GuiNpcButton button = (GuiNpcButton) guibutton;
		if (button.id == 0) {
			stats.spawnCycle = button.getValue();
			if ((stats.spawnCycle == 3) || (stats.spawnCycle == 4)) {
				stats.respawnTime = 0;
			} else {
				stats.respawnTime = 20;
			}
			initGui();
		} else if (button.id == 4) {
			stats.hideKilledBody = (button.getValue() == 1);
		}
		if (id == 66) {
			close();
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		addLabel(new GuiNpcLabel(0, "stats.respawn", guiLeft + 5, guiTop + 35));
		addButton(new GuiNpcButton(0, guiLeft + 122, guiTop + 30, 60, 20,
				new String[] { "gui.yes", "gui.day", "gui.night", "gui.no", "stats.naturally" }, stats.spawnCycle));
		if (stats.respawnTime > 0) {
			addLabel(new GuiNpcLabel(3, "gui.time", guiLeft + 5, guiTop + 57));
			addTextField(new GuiNpcTextField(2, this, fontRendererObj, guiLeft + 122, guiTop + 53, 50, 18,
					stats.respawnTime + ""));
			getTextField(2).numbersOnly = true;
			getTextField(2).setMinMaxDefault(1, 99999, 20);
			addLabel(new GuiNpcLabel(4, "stats.deadbody", guiLeft + 4, guiTop + 79));
			addButton(new GuiNpcButton(4, guiLeft + 122, guiTop + 74, 51, 20, new String[] { "gui.no", "gui.yes" },
					stats.hideKilledBody ? 1 : 0));
		}
		addButton(new GuiNpcButton(66, guiLeft + 82, guiTop + 190, 98, 20, "gui.done"));
	}

	@Override
	public void unFocused(final GuiNpcTextField textfield) {
		if (textfield.id == 2) {
			stats.respawnTime = textfield.getInteger();
		}
	}
}

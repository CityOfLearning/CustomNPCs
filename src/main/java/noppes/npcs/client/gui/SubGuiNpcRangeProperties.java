
package noppes.npcs.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcButtonYesNo;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.entity.data.DataRanged;
import noppes.npcs.entity.data.DataStats;

public class SubGuiNpcRangeProperties extends SubGuiInterface implements ITextfieldListener {
	private DataRanged ranged;
	private DataStats stats;
	private GuiNpcSoundSelection gui;

	public SubGuiNpcRangeProperties(DataStats stats) {
		ranged = stats.ranged;
		this.stats = stats;
		setBackground("menubg.png");
		xSize = 256;
		ySize = 216;
		closeOnEsc = true;
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		int id = guibutton.id;
		if (id == 7) {
			NoppesUtil.openGUI(player, gui = new GuiNpcSoundSelection(parent, getTextField(7).getText()));
		}
		if (id == 8) {
			NoppesUtil.openGUI(player, gui = new GuiNpcSoundSelection(parent, getTextField(7).getText()));
		}
		if (id == 10) {
			NoppesUtil.openGUI(player, gui = new GuiNpcSoundSelection(parent, getTextField(7).getText()));
		} else if (id == 66) {
			close();
		} else if (id == 9) {
			ranged.setHasAimAnimation(((GuiNpcButtonYesNo) guibutton).getBoolean());
		} else if (id == 13) {
			ranged.setFireType(((GuiNpcButton) guibutton).getValue());
		}
	}

	@Override
	public void elementClicked() {
		getTextField(7).setText(gui.getSelected());
		unFocused(getTextField(7));
	}

	@Override
	public void initGui() {
		super.initGui();
		int y = guiTop + 4;
		addTextField(new GuiNpcTextField(1, this, fontRendererObj, guiLeft + 80, y, 50, 18, ranged.getAccuracy() + ""));
		addLabel(new GuiNpcLabel(1, "stats.accuracy", guiLeft + 5, y + 5));
		getTextField(1).numbersOnly = true;
		getTextField(1).setMinMaxDefault(0, 100, 90);
		addTextField(
				new GuiNpcTextField(8, this, fontRendererObj, guiLeft + 200, y, 50, 18, ranged.getShotCount() + ""));
		addLabel(new GuiNpcLabel(8, "stats.shotcount", guiLeft + 135, y + 5));
		getTextField(8).numbersOnly = true;
		getTextField(8).setMinMaxDefault(1, 10, 1);
		int id = 2;
		FontRenderer fontRendererObj = this.fontRendererObj;
		int i = guiLeft + 80;
		y += 22;
		addTextField(new GuiNpcTextField(id, this, fontRendererObj, i, y, 50, 18, ranged.getRange() + ""));
		addLabel(new GuiNpcLabel(2, "gui.range", guiLeft + 5, y + 5));
		getTextField(2).numbersOnly = true;
		getTextField(2).setMinMaxDefault(1, 64, 2);
		addTextField(new GuiNpcTextField(9, this, this.fontRendererObj, guiLeft + 200, y, 30, 20,
				ranged.getMeleeRange() + ""));
		addLabel(new GuiNpcLabel(16, "stats.meleerange", guiLeft + 135, y + 5));
		getTextField(9).numbersOnly = true;
		getTextField(9).setMinMaxDefault(0, stats.aggroRange, 5);
		int id2 = 3;
		FontRenderer fontRendererObj2 = this.fontRendererObj;
		int j = guiLeft + 80;
		y += 22;
		addTextField(new GuiNpcTextField(id2, this, fontRendererObj2, j, y, 50, 18, ranged.getDelayMin() + ""));
		addLabel(new GuiNpcLabel(3, "stats.mindelay", guiLeft + 5, y + 5));
		getTextField(3).numbersOnly = true;
		getTextField(3).setMinMaxDefault(1, 9999, 20);
		addTextField(new GuiNpcTextField(4, this, this.fontRendererObj, guiLeft + 200, y, 50, 18,
				ranged.getDelayMax() + ""));
		addLabel(new GuiNpcLabel(4, "stats.maxdelay", guiLeft + 135, y + 5));
		getTextField(4).numbersOnly = true;
		getTextField(4).setMinMaxDefault(1, 9999, 20);
		int id3 = 6;
		FontRenderer fontRendererObj3 = this.fontRendererObj;
		int k = guiLeft + 80;
		y += 22;
		addTextField(new GuiNpcTextField(id3, this, fontRendererObj3, k, y, 50, 18, ranged.getBurst() + ""));
		addLabel(new GuiNpcLabel(6, "stats.burstcount", guiLeft + 5, y + 5));
		getTextField(6).numbersOnly = true;
		getTextField(6).setMinMaxDefault(1, 100, 20);
		addTextField(new GuiNpcTextField(5, this, this.fontRendererObj, guiLeft + 200, y, 50, 18,
				ranged.getBurstDelay() + ""));
		addLabel(new GuiNpcLabel(5, "stats.burstspeed", guiLeft + 135, y + 5));
		getTextField(5).numbersOnly = true;
		getTextField(5).setMinMaxDefault(0, 30, 0);
		int id4 = 7;
		FontRenderer fontRendererObj4 = this.fontRendererObj;
		int l = guiLeft + 80;
		y += 22;
		addTextField(new GuiNpcTextField(id4, this, fontRendererObj4, l, y, 100, 20, ranged.getSound(0)));
		addLabel(new GuiNpcLabel(7, "stats.firesound", guiLeft + 5, y + 5));
		addButton(new GuiNpcButton(7, guiLeft + 187, y, 60, 20, "mco.template.button.select"));
		int id5 = 11;
		FontRenderer fontRendererObj5 = this.fontRendererObj;
		int m = guiLeft + 80;
		y += 22;
		addTextField(new GuiNpcTextField(id5, this, fontRendererObj5, m, y, 100, 20, ranged.getSound(1)));
		addLabel(new GuiNpcLabel(11, "stats.hitsound", guiLeft + 5, y + 5));
		addButton(new GuiNpcButton(11, guiLeft + 187, y, 60, 20, "mco.template.button.select"));
		int id6 = 10;
		FontRenderer fontRendererObj6 = this.fontRendererObj;
		int i2 = guiLeft + 80;
		y += 22;
		addTextField(new GuiNpcTextField(id6, this, fontRendererObj6, i2, y, 100, 20, ranged.getSound(2)));
		addLabel(new GuiNpcLabel(10, "stats.groundsound", guiLeft + 5, y + 5));
		addButton(new GuiNpcButton(10, guiLeft + 187, y, 60, 20, "mco.template.button.select"));
		int id7 = 9;
		int x = guiLeft + 100;
		y += 22;
		addButton(new GuiNpcButtonYesNo(id7, x, y, ranged.getHasAimAnimation()));
		addLabel(new GuiNpcLabel(9, "stats.aimWhileShooting", guiLeft + 5, y + 5));
		int i3 = 13;
		int j2 = guiLeft + 100;
		y += 22;
		addButton(new GuiNpcButton(i3, j2, y, 80, 20, new String[] { "gui.no", "gui.whendistant", "gui.whenhidden" },
				ranged.getFireType()));
		addLabel(new GuiNpcLabel(13, "stats.indirect", guiLeft + 5, y + 5));
		addButton(new GuiNpcButton(66, guiLeft + 190, guiTop + 190, 60, 20, "gui.done"));
	}

	@Override
	public void unFocused(GuiNpcTextField textfield) {
		if (textfield.id == 1) {
			ranged.setAccuracy(textfield.getInteger());
		} else if (textfield.id == 2) {
			ranged.setRange(textfield.getInteger());
		} else if (textfield.id == 3) {
			ranged.setDelay(textfield.getInteger(), ranged.getDelayMax());
			initGui();
		} else if (textfield.id == 4) {
			ranged.setDelay(ranged.getDelayMin(), textfield.getInteger());
			initGui();
		} else if (textfield.id == 5) {
			ranged.setBurstDelay(textfield.getInteger());
		} else if (textfield.id == 6) {
			ranged.setBurst(textfield.getInteger());
		} else if (textfield.id == 7) {
			ranged.setSound(0, textfield.getText());
		} else if (textfield.id == 8) {
			ranged.setShotCount(textfield.getInteger());
		} else if (textfield.id == 9) {
			ranged.setMeleeRange(textfield.getInteger());
		}
	}
}

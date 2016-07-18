//

//

package noppes.npcs.client.gui.questtypes;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.controllers.Quest;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.quests.QuestLocation;

public class GuiNpcQuestTypeLocation extends SubGuiInterface implements ITextfieldListener {
	private QuestLocation quest;

	public GuiNpcQuestTypeLocation(final EntityNPCInterface npc, final Quest q, final GuiScreen parent) {
		this.npc = npc;
		title = "Quest Location Setup";
		quest = (QuestLocation) q.questInterface;
		setBackground("menubg.png");
		xSize = 256;
		ySize = 216;
		closeOnEsc = true;
	}

	@Override
	protected void actionPerformed(final GuiButton guibutton) {
		super.actionPerformed(guibutton);
		if (guibutton.id == 0) {
			close();
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		addLabel(new GuiNpcLabel(0, "Fill in the name of your Location Quest Block", guiLeft + 4, guiTop + 50));
		addTextField(new GuiNpcTextField(0, this, fontRendererObj, guiLeft + 4, guiTop + 70, 180, 20, quest.location));
		addTextField(new GuiNpcTextField(1, this, fontRendererObj, guiLeft + 4, guiTop + 92, 180, 20, quest.location2));
		addTextField(
				new GuiNpcTextField(2, this, fontRendererObj, guiLeft + 4, guiTop + 114, 180, 20, quest.location3));
		addButton(new GuiNpcButton(0, guiLeft + 150, guiTop + 190, 98, 20, "gui.back"));
	}

	@Override
	public void save() {
	}

	@Override
	public void unFocused(final GuiNpcTextField textfield) {
		if (textfield.id == 0) {
			quest.location = textfield.getText();
		}
		if (textfield.id == 1) {
			quest.location2 = textfield.getText();
		}
		if (textfield.id == 2) {
			quest.location3 = textfield.getText();
		}
	}
}

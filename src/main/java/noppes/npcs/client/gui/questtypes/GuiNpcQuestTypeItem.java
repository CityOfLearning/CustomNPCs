//

//

package noppes.npcs.client.gui.questtypes;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.global.GuiNPCManageQuest;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcButtonYesNo;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.containers.ContainerNpcQuestTypeItem;
import noppes.npcs.controllers.quest.Quest;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.quests.QuestItem;

public class GuiNpcQuestTypeItem extends GuiContainerNPCInterface implements ITextfieldListener {
	private static ResourceLocation field_110422_t;
	static {
		field_110422_t = new ResourceLocation("customnpcs", "textures/gui/followersetup.png");
	}

	private Quest quest;

	public GuiNpcQuestTypeItem(EntityNPCInterface npc, ContainerNpcQuestTypeItem container) {
		super(npc, container);
		quest = GuiNPCManageQuest.quest;
		title = "";
		ySize = 202;
		closeOnEsc = false;
	}

	@Override
	public void actionPerformed(GuiButton guibutton) {
		if (guibutton.id == 0) {
			((QuestItem) quest.questInterface).leaveItems = (((GuiNpcButton) guibutton).getValue() == 1);
		}
		if (guibutton.id == 1) {
			((QuestItem) quest.questInterface).ignoreDamage = ((GuiNpcButtonYesNo) guibutton).getBoolean();
		}
		if (guibutton.id == 2) {
			((QuestItem) quest.questInterface).ignoreNBT = ((GuiNpcButtonYesNo) guibutton).getBoolean();
		}
		if (guibutton.id == 5) {
			NoppesUtil.openGUI(player, GuiNPCManageQuest.Instance);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		drawWorldBackground(0);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(GuiNpcQuestTypeItem.field_110422_t);
		int l = (width - xSize) / 2;
		int i2 = (height - ySize) / 2;
		this.drawTexturedModalRect(l, i2, 0, 0, xSize, ySize);
		super.drawGuiContainerBackgroundLayer(f, i, j);
	}

	@Override
	public void initGui() {
		super.initGui();
		addLabel(new GuiNpcLabel(0, "quest.takeitems", guiLeft + 4, guiTop + 8));
		addButton(new GuiNpcButton(0, guiLeft + 90, guiTop + 3, 60, 20, new String[] { "gui.yes", "gui.no" },
				((QuestItem) quest.questInterface).leaveItems ? 1 : 0));
		addLabel(new GuiNpcLabel(1, "gui.ignoreDamage", guiLeft + 4, guiTop + 29));
		addButton(new GuiNpcButtonYesNo(1, guiLeft + 90, guiTop + 24, 50, 20,
				((QuestItem) quest.questInterface).ignoreDamage));
		addLabel(new GuiNpcLabel(2, "gui.ignoreNBT", guiLeft + 62, guiTop + 51));
		addButton(new GuiNpcButtonYesNo(2, guiLeft + 120, guiTop + 46, 50, 20,
				((QuestItem) quest.questInterface).ignoreNBT));
		addButton(new GuiNpcButton(5, guiLeft, guiTop + ySize, 98, 20, "gui.back"));
	}

	@Override
	public void save() {
	}

	@Override
	public void unFocused(GuiNpcTextField textfield) {
		quest.rewardExp = textfield.getInteger();
	}
}

//

//

package noppes.npcs.client.gui.player;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.TextBlockClient;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.ITopButtonListener;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.controllers.quest.Quest;

public class GuiQuestCompletion extends GuiNPCInterface implements ITopButtonListener {
	private Quest quest;
	private ResourceLocation resource;

	public GuiQuestCompletion(Quest quest) {
		resource = new ResourceLocation("customnpcs", "textures/gui/smallbg.png");
		xSize = 176;
		ySize = 222;
		this.quest = quest;
		drawDefaultBackground = false;
		title = "";
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.id == 0) {
			NoppesUtilPlayer.sendData(EnumPlayerPacket.QuestCompletion, quest.id);
			close();
		}
	}

	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
	}

	private void drawQuestText() {
		TextBlockClient block = new TextBlockClient(quest.completeText, 172, true, new Object[] { player });
		for (int i = 0; i < block.lines.size(); ++i) {
			String text = block.lines.get(i).getFormattedText();
			fontRendererObj.drawString(text, guiLeft + 4, guiTop + 16 + (i * fontRendererObj.FONT_HEIGHT),
					CustomNpcResourceListener.DefaultTextColor);
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(resource);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		drawHorizontalLine(guiLeft + 4, guiLeft + 170, guiTop + 13,
				-16777216 + CustomNpcResourceListener.DefaultTextColor);
		drawQuestText();
		super.drawScreen(i, j, f);
	}

	@Override
	public void initGui() {
		super.initGui();
		String questTitle = quest.title;
		int left = (xSize - fontRendererObj.getStringWidth(questTitle)) / 2;
		addLabel(new GuiNpcLabel(0, questTitle, guiLeft + left, guiTop + 4));
		addButton(new GuiNpcButton(0, guiLeft + 38, (guiTop + ySize) - 24, 100, 20,
				StatCollector.translateToLocal("quest.complete")));
	}

	@Override
	public void keyTyped(char c, int i) {
		if ((i == 1) || isInventoryKey(i)) {
			close();
		}
	}

	@Override
	public void save() {
		NoppesUtilPlayer.sendData(EnumPlayerPacket.QuestCompletion, quest.id);
	}
}

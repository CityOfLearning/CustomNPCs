
package noppes.npcs.client.gui.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.QuestLogData;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.TextBlockClient;
import noppes.npcs.client.gui.util.GuiButtonNextPage;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiMenuSideButton;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.ITopButtonListener;
import noppes.npcs.constants.EnumPlayerPacket;
import tconstruct.client.tabs.InventoryTabQuests;
import tconstruct.client.tabs.TabRegistry;

public class GuiQuestLog extends GuiNPCInterface implements ITopButtonListener, ICustomScrollListener, IGuiData {
	private ResourceLocation resource;
	private EntityPlayer player;
	private GuiCustomScroll scroll;
	private HashMap<Integer, GuiMenuSideButton> sideButtons;
	private QuestLogData data;
	private boolean noQuests;
	private boolean questDetails;
	private Minecraft mc;

	public GuiQuestLog(EntityPlayer player) {
		resource = new ResourceLocation("customnpcs", "textures/gui/standardbg.png");
		sideButtons = new HashMap<Integer, GuiMenuSideButton>();
		data = new QuestLogData();
		noQuests = false;
		questDetails = true;
		mc = Minecraft.getMinecraft();
		this.player = player;
		xSize = 280;
		ySize = 180;
		NoppesUtilPlayer.sendData(EnumPlayerPacket.QuestLog, new Object[0]);
		drawDefaultBackground = false;
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!(guibutton instanceof GuiButtonNextPage)) {
			return;
		}
		if (guibutton.id == 1) {
			questDetails = false;
			initGui();
		}
		if (guibutton.id == 2) {
			questDetails = true;
			initGui();
		}
	}

	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll scroll) {
		if (!scroll.hasSelected()) {
			return;
		}
		data.selectedQuest = scroll.getSelected();
		initGui();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	private void drawProgress() {
		String complete = data.getComplete();
		if ((complete != null) && !complete.isEmpty()) {
			mc.fontRendererObj.drawString(
					StatCollector.translateToLocalFormatted("quest.completewith", new Object[] { complete }),
					guiLeft + 144, guiTop + 105, CustomNpcResourceListener.DefaultTextColor);
		}
		int yoffset = guiTop + 22;
		for (String process : data.getQuestStatus()) {
			int index = process.lastIndexOf(":");
			if (index > 0) {
				String name = process.substring(0, index);
				String trans = StatCollector.translateToLocal(name);
				if (!trans.equals(name)) {
					name = trans;
				}
				trans = StatCollector.translateToLocal("entity." + name + ".name");
				if (!trans.equals("entity." + name + ".name")) {
					name = trans;
				}
				process = name + process.substring(index);
			}
			mc.fontRendererObj.drawString("- " + process, guiLeft + 144, yoffset,
					CustomNpcResourceListener.DefaultTextColor);
			yoffset += 10;
		}
	}

	private void drawQuestText() {
		TextBlockClient block = new TextBlockClient(data.getQuestText(), 174, true, new Object[] { player });
		for (int i = 0; i < block.lines.size(); ++i) {
			String text = block.lines.get(i).getFormattedText();
			fontRendererObj.drawString(text, guiLeft + 142, guiTop + 20 + (i * fontRendererObj.FONT_HEIGHT),
					CustomNpcResourceListener.DefaultTextColor);
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		if (scroll != null) {
			scroll.visible = !noQuests;
		}
		drawDefaultBackground();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(resource);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 252, 195);
		this.drawTexturedModalRect(guiLeft + 252, guiTop, 188, 0, 67, 195);
		super.drawScreen(i, j, f);
		if (noQuests) {
			mc.fontRendererObj.drawString(StatCollector.translateToLocal("quest.noquests"), guiLeft + 84, guiTop + 80,
					CustomNpcResourceListener.DefaultTextColor);
			return;
		}
		for (GuiMenuSideButton button : sideButtons.values().toArray(new GuiMenuSideButton[sideButtons.size()])) {
			button.drawButton(mc, i, j);
		}
		mc.fontRendererObj.drawString(data.selectedCategory, guiLeft + 5, guiTop + 5,
				CustomNpcResourceListener.DefaultTextColor);
		if (!data.hasSelectedQuest()) {
			return;
		}
		if (questDetails) {
			drawProgress();
			String title = StatCollector.translateToLocal("gui.text");
			mc.fontRendererObj.drawString(title, (guiLeft + 284) - mc.fontRendererObj.getStringWidth(title),
					guiTop + 179, CustomNpcResourceListener.DefaultTextColor);
		} else {
			drawQuestText();
			String title = StatCollector.translateToLocal("quest.objectives");
			mc.fontRendererObj.drawString(title, guiLeft + 168, guiTop + 179,
					CustomNpcResourceListener.DefaultTextColor);
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(guiLeft + 148, guiTop, 0.0f);
		GlStateManager.scale(1.24f, 1.24f, 1.24f);
		fontRendererObj.drawString(data.selectedQuest, (130 - fontRendererObj.getStringWidth(data.selectedQuest)) / 2,
				4, CustomNpcResourceListener.DefaultTextColor);
		GlStateManager.popMatrix();
		drawHorizontalLine(guiLeft + 142, guiLeft + 312, guiTop + 17,
				-16777216 + CustomNpcResourceListener.DefaultTextColor);
	}

	@Override
	public void initGui() {
		super.initGui();
		sideButtons.clear();
		guiTop += 10;
		TabRegistry.updateTabValues(guiLeft, guiTop, InventoryTabQuests.class);
		TabRegistry.addTabsToList(buttonList);
		noQuests = false;
		if (data.categories.isEmpty()) {
			noQuests = true;
			return;
		}
		List<String> categories = new ArrayList<String>();
		categories.addAll(data.categories.keySet());
		Collections.sort(categories, String.CASE_INSENSITIVE_ORDER);
		int i = 0;
		for (String category : categories) {
			if (data.selectedCategory.isEmpty()) {
				data.selectedCategory = category;
			}
			sideButtons.put(i, new GuiMenuSideButton(i, guiLeft - 69, guiTop + 2 + (i * 21), 70, 22, category));
			++i;
		}
		sideButtons.get(categories.indexOf(data.selectedCategory)).active = true;
		if (scroll == null) {
			scroll = new GuiCustomScroll(this, 0);
		}
		scroll.setList(data.categories.get(data.selectedCategory));
		scroll.setSize(134, 174);
		scroll.guiLeft = guiLeft + 5;
		scroll.guiTop = guiTop + 15;
		addScroll(scroll);
		addButton(new GuiButtonNextPage(1, guiLeft + 286, guiTop + 176, true));
		addButton(new GuiButtonNextPage(2, guiLeft + 144, guiTop + 176, false));
		getButton(1).visible = (questDetails && data.hasSelectedQuest());
		getButton(2).visible = (!questDetails && data.hasSelectedQuest());
	}

	@Override
	public void keyTyped(char c, int i) {
		if ((i == 1) || (i == mc.gameSettings.keyBindInventory.getKeyCode())) {
			mc.displayGuiScreen((GuiScreen) null);
			mc.setIngameFocus();
		}
	}

	@Override
	public void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		if (k == 0) {
			if (scroll != null) {
				scroll.mouseClicked(i, j, k);
			}
			for (GuiMenuSideButton button : new ArrayList<GuiMenuSideButton>(sideButtons.values())) {
				if (button.mousePressed(mc, i, j)) {
					sideButtonPressed(button);
				}
			}
		}
	}

	@Override
	public void save() {
	}

	@Override
	public void setGuiData(NBTTagCompound compound) {
		QuestLogData data = new QuestLogData();
		data.readNBT(compound);
		this.data = data;
		initGui();
	}

	private void sideButtonPressed(GuiMenuSideButton button) {
		if (button.active) {
			return;
		}
		NoppesUtil.clickSound();
		data.selectedCategory = button.displayString;
		data.selectedQuest = "";
		initGui();
	}
}

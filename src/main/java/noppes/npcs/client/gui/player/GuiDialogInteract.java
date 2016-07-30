//

//

package noppes.npcs.client.gui.player;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.api.constants.EnumOptionType;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.TextBlockClient;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.controllers.dialog.Dialog;
import noppes.npcs.controllers.dialog.DialogOption;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiDialogInteract extends GuiNPCInterface {
	private Dialog dialog;
	private int selected;
	private List<TextBlockClient> lines;
	private List<Integer> options;
	private int rowStart;
	private int rowTotal;
	private int dialogHeight;
	private ResourceLocation wheel;
	private ResourceLocation[] wheelparts;
	private ResourceLocation indicator;
	private boolean isGrabbed;
	private int selectedX;
	private int selectedY;

	public GuiDialogInteract(EntityNPCInterface npc, Dialog dialog) {
		super(npc);
		selected = 0;
		lines = new ArrayList<TextBlockClient>();
		options = new ArrayList<Integer>();
		rowStart = 0;
		rowTotal = 0;
		dialogHeight = 180;
		isGrabbed = false;
		selectedX = 0;
		selectedY = 0;
		appendDialog(this.dialog = dialog);
		ySize = 238;
		wheel = getResource("wheel.png");
		indicator = getResource("indicator.png");
		wheelparts = new ResourceLocation[] { getResource("wheel1.png"), getResource("wheel2.png"),
				getResource("wheel3.png"), getResource("wheel4.png"), getResource("wheel5.png"),
				getResource("wheel6.png") };
	}

	public void appendDialog(Dialog dialog) {
		closeOnEsc = !dialog.disableEsc;
		this.dialog = dialog;
		options = new ArrayList<Integer>();
		if ((dialog.sound != null) && !dialog.sound.isEmpty()) {
			MusicController.Instance.stopMusic();
			MusicController.Instance.playSound(dialog.sound, (float) npc.posX, (float) npc.posY, (float) npc.posZ);
		}
		lines.add(new TextBlockClient((ICommandSender) npc, dialog.text, 280, 14737632, new Object[] { player, npc }));
		for (int slot : dialog.options.keySet()) {
			DialogOption option = dialog.options.get(slot);
			if (option != null) {
				if (option.optionType == EnumOptionType.DISABLED) {
					continue;
				}
				options.add(slot);
			}
		}
		calculateRowHeight();
		grabMouse(dialog.showWheel);
	}

	private void calculateRowHeight() {
		if (dialog.showWheel) {
			dialogHeight = ySize - 58;
		} else {
			dialogHeight = ySize - (3 * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT) - 4;
			if (dialog.options.size() > 3) {
				dialogHeight -= (dialog.options.size() - 3) * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
			}
		}
		rowTotal = 0;
		for (TextBlockClient block : lines) {
			rowTotal += block.lines.size() + 1;
		}
		int max = dialogHeight / Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
		rowStart = rowTotal - max;
		if (rowStart < 0) {
			rowStart = 0;
		}
	}

	private void closed() {
		NoppesUtilPlayer.sendData(EnumPlayerPacket.CheckQuestCompletion, new Object[0]);
	}

	private void drawLinedOptions(int j) {
		drawHorizontalLine(guiLeft - 60, guiLeft + xSize + 120,
				(guiTop + dialogHeight) - (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT / 3), -1);
		int offset = dialogHeight;
		if (j >= (guiTop + offset)) {
			int selected = (j - (guiTop + offset)) / Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
			if (selected < options.size()) {
				this.selected = selected;
			}
		}
		if (selected >= options.size()) {
			selected = 0;
		}
		if (selected < 0) {
			selected = 0;
		}
		for (int k = 0; k < options.size(); ++k) {
			int id = options.get(k);
			DialogOption option = dialog.options.get(id);
			int y = guiTop + offset + (k * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT);
			if (selected == k) {
				this.drawString(fontRendererObj, ">", guiLeft - 60, y, 14737632);
			}
			this.drawString(fontRendererObj, NoppesStringUtils.formatText(option.title, player, npc), guiLeft - 30, y,
					option.optionColor);
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		if (!dialog.hideNPC) {
			int l = -70;
			int i2 = ySize;
			this.drawNpc(npc, l, i2, 1.4f, 0);
		}
		super.drawScreen(i, j, f);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.enableAlpha();
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0f, 0.5f, 100.065f);
		int count = 0;
		for (TextBlockClient block : new ArrayList<TextBlockClient>(lines)) {
			int size = Minecraft.getMinecraft().fontRendererObj.getStringWidth(block.getName() + ": ");
			this.drawString(block.getName() + ": ", -4 - size, block.color, count);
			for (IChatComponent line : block.lines) {
				this.drawString(line.getFormattedText(), 0, block.color, count);
				++count;
			}
			++count;
		}
		if (!options.isEmpty()) {
			if (!dialog.showWheel) {
				drawLinedOptions(j);
			} else {
				drawWheel();
			}
		}
		GlStateManager.popMatrix();
	}

	@Override
	public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
		Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, color);
	}

	private void drawString(String text, int left, int color, int count) {
		int height = count - rowStart;
		this.drawString(fontRendererObj, text, guiLeft + left,
				guiTop + (height * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT), color);
	}

	private void drawWheel() {
		int yoffset = guiTop + dialogHeight + 14;
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(wheel);
		this.drawTexturedModalRect((width / 2) - 31, yoffset, 0, 0, 63, 40);
		selectedX += Mouse.getDX();
		selectedY += Mouse.getDY();
		int limit = 80;
		if (selectedX > limit) {
			selectedX = limit;
		}
		if (selectedX < -limit) {
			selectedX = -limit;
		}
		if (selectedY > limit) {
			selectedY = limit;
		}
		if (selectedY < -limit) {
			selectedY = -limit;
		}
		selected = 1;
		if (selectedY < -20) {
			++selected;
		}
		if (selectedY > 54) {
			--selected;
		}
		if (selectedX < 0) {
			selected += 3;
		}
		mc.renderEngine.bindTexture(wheelparts[selected]);
		this.drawTexturedModalRect((width / 2) - 31, yoffset, 0, 0, 85, 55);
		for (int slot : dialog.options.keySet()) {
			DialogOption option = dialog.options.get(slot);
			if (option != null) {
				if (option.optionType == EnumOptionType.DISABLED) {
					continue;
				}
				int color = option.optionColor;
				if (slot == selected) {
					color = 8622040;
				}
				if (slot == 0) {
					this.drawString(fontRendererObj, option.title, (width / 2) + 13, yoffset - 6, color);
				}
				if (slot == 1) {
					this.drawString(fontRendererObj, option.title, (width / 2) + 33, yoffset + 12, color);
				}
				if (slot == 2) {
					this.drawString(fontRendererObj, option.title, (width / 2) + 27, yoffset + 32, color);
				}
				if (slot == 3) {
					this.drawString(fontRendererObj, option.title,
							(width / 2) - 13 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(option.title),
							yoffset - 6, color);
				}
				if (slot == 4) {
					this.drawString(fontRendererObj, option.title,
							(width / 2) - 33 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(option.title),
							yoffset + 12, color);
				}
				if (slot != 5) {
					continue;
				}
				this.drawString(fontRendererObj, option.title,
						(width / 2) - 27 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(option.title),
						yoffset + 32, color);
			}
		}
		mc.renderEngine.bindTexture(indicator);
		this.drawTexturedModalRect(((width / 2) + (selectedX / 4)) - 2, (yoffset + 16) - (selectedY / 6), 0, 0, 8, 8);
	}

	public void grabMouse(boolean grab) {
		if (grab && !isGrabbed) {
			Minecraft.getMinecraft().mouseHelper.grabMouseCursor();
			isGrabbed = true;
		} else if (!grab && isGrabbed) {
			Minecraft.getMinecraft().mouseHelper.ungrabMouseCursor();
			isGrabbed = false;
		}
	}

	private void handleDialogSelection() {
		int optionId = -1;
		if (dialog.showWheel) {
			optionId = selected;
		} else if (!options.isEmpty()) {
			optionId = options.get(selected);
		}
		NoppesUtilPlayer.sendData(EnumPlayerPacket.Dialog, dialog.id, optionId);
		if ((dialog == null) || !dialog.hasOtherOptions() || options.isEmpty()) {
			close();
			closed();
			return;
		}
		DialogOption option = dialog.options.get(optionId);
		if ((option == null) || (option.optionType == EnumOptionType.QUIT_OPTION)
				|| (option.optionType == EnumOptionType.DISABLED)) {
			close();
			closed();
			return;
		}
		lines.add(new TextBlockClient(player.getDisplayNameString(), option.title, 280, option.optionColor,
				new Object[] { player, npc }));
		calculateRowHeight();
		NoppesUtil.clickSound();
	}

	@Override
	public void initGui() {
		super.initGui();
		isGrabbed = false;
		grabMouse(dialog.showWheel);
		guiTop = height - ySize;
		calculateRowHeight();
	}

	@Override
	public void keyTyped(char c, int i) {
		if ((i == mc.gameSettings.keyBindForward.getKeyCode()) || (i == 200)) {
			--selected;
		}
		if ((i == mc.gameSettings.keyBindBack.getKeyCode()) || (i == 208)) {
			++selected;
		}
		if (i == 28) {
			handleDialogSelection();
		}
		if (closeOnEsc && ((i == 1) || isInventoryKey(i))) {
			NoppesUtilPlayer.sendData(EnumPlayerPacket.Dialog, dialog.id, -1);
			closed();
			close();
		}
		super.keyTyped(c, i);
	}

	@Override
	public void mouseClicked(int i, int j, int k) {
		if (((selected == -1) && options.isEmpty()) || (selected >= 0)) {
			handleDialogSelection();
		}
	}

	@Override
	public void save() {
	}
}

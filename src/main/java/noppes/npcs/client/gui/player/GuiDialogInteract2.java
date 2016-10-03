package noppes.npcs.client.gui.player;

import com.rabbit.gui.show.Show;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import noppes.npcs.controllers.dialog.Dialog;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiDialogInteract2 extends Show {
	public EntityNPCInterface npc;
	public EntityPlayerSP player;

	public GuiDialogInteract2(EntityNPCInterface npc, Dialog dialog) {
		super();
		// lines = new ArrayList<TextBlockClient>();
		// options = new ArrayList<Integer>();
		this.npc = npc;
		player = Minecraft.getMinecraft().thePlayer;
		// appendDialog(this.dialog = dialog);
	}

	// public void appendDialog(Dialog dialog) {
	// closeOnEsc = !dialog.disableEsc;
	// this.dialog = dialog;
	// options = new ArrayList<Integer>();
	// if ((dialog.sound != null) && !dialog.sound.isEmpty()) {
	// MusicController.Instance.stopMusic();
	// MusicController.Instance.playSound(dialog.sound, (float) npc.posX,
	// (float) npc.posY, (float) npc.posZ);
	// }
	// lines.add(new TextBlockClient((ICommandSender) npc, dialog.text, 280,
	// 14737632, new Object[] { player, npc }));
	// for (int slot : dialog.options.keySet()) {
	// DialogOption option = dialog.options.get(slot);
	// if (option != null) {
	// if (option.optionType == EnumOptionType.DISABLED) {
	// continue;
	// }
	// options.add(slot);
	// }
	// }
	// calculateRowHeight();
	// }
	//
	// private void calculateRowHeight() {
	//
	// dialogHeight = (3 * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT)
	// - 4;
	// if (dialog.options.size() > 3) {
	// dialogHeight -= (dialog.options.size() - 3) *
	// Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
	// }
	// rowTotal = 0;
	// for (TextBlockClient block : lines) {
	// rowTotal += block.lines.size() + 1;
	// }
	// int max = dialogHeight /
	// Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
	// rowStart = rowTotal - max;
	// if (rowStart < 0) {
	// rowStart = 0;
	// }
	// }
	//
	// private void closed() {
	// NoppesUtilPlayer.sendData(EnumPlayerPacket.CheckQuestCompletion, new
	// Object[0]);
	// }
	//
	// private void drawLinedOptions(int j) {
	//// drawHorizontalLine(guiLeft - 60, guiLeft + xSize + 120,
	//// (guiTop + dialogHeight) -
	// (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT / 3), -1);
	// int offset = this.height -
	// Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT * 9;
	// if (j >= offset) {
	// int selected = (j - offset) /
	// Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
	// if (selected < options.size()) {
	// this.selected = selected;
	// }
	// }
	// if (selected >= options.size()) {
	// selected = 0;
	// }
	// if (selected < 0) {
	// selected = 0;
	// }
	// for (int k = 0; k < options.size(); ++k) {
	// int id = options.get(k);
	// DialogOption option = dialog.options.get(id);
	// int y = offset + (k *
	// Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT);
	// if (selected == k) {
	// this.drawString(fontRendererObj, ">", 30, y, 14737632);
	// }
	// this.drawString(fontRendererObj,
	// NoppesStringUtils.formatText(option.title, player, npc), 60, y,
	// option.optionColor);
	// }
	// }
	//
	// @Override
	// public void drawScreen(int i, int j, float f) {
	// super.drawScreen(i, j, f);
	// drawDefaultBackground();
	// GlStateManager.resetColor();
	//
	// GlStateManager.enableBlend();
	// GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	// GlStateManager.enableAlpha();
	// GlStateManager.pushMatrix();
	// GlStateManager.translate(0.0f, 0.5f, 100.065f);
	// int count = 0;
	// for (TextBlockClient block : new ArrayList<TextBlockClient>(lines)) {
	// int size =
	// Minecraft.getMinecraft().fontRendererObj.getStringWidth(block.getName() +
	// ": ");
	// this.drawString(block.getName() + ": ", 60 - size, 80, block.color,
	// count);
	// for (IChatComponent line : block.lines) {
	// this.drawString(line.getFormattedText(), 60, 80, block.color, count);
	// ++count;
	// }
	// ++count;
	// }
	// if (!options.isEmpty()) {
	// drawLinedOptions(j);
	// }
	// GlStateManager.popMatrix();
	// }
	//
	// @Override
	// public void drawString(FontRenderer fontRendererIn, String text, int x,
	// int y, int color) {
	// Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y,
	// color);
	// }
	//
	// private void drawString(String text, int left, int top, int color, int
	// count) {
	// int height = count - rowStart;
	// this.drawString(fontRendererObj, text, left,
	// top + (height * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT),
	// color);
	// }
	//
	// private void handleDialogSelection() {
	// int optionId = -1;
	// if (dialog.showWheel) {
	// optionId = selected;
	// } else if (!options.isEmpty()) {
	// optionId = options.get(selected);
	// }
	// NoppesUtilPlayer.sendData(EnumPlayerPacket.Dialog, dialog.id, optionId);
	// if ((dialog == null) || !dialog.hasOtherOptions() || options.isEmpty()) {
	// close();
	// closed();
	// return;
	// }
	// DialogOption option = dialog.options.get(optionId);
	// if ((option == null) || (option.optionType == EnumOptionType.QUIT_OPTION)
	// || (option.optionType == EnumOptionType.DISABLED)) {
	// close();
	// closed();
	// return;
	// }
	// lines.add(new TextBlockClient(player.getDisplayNameString(),
	// option.title, 280, option.optionColor,
	// new Object[] { player, npc }));
	// calculateRowHeight();
	// NoppesUtil.clickSound();
	// }
	//
	// @Override
	// public void initGui() {
	// super.initGui();
	// calculateRowHeight();
	// }
	//
	// @Override
	// public void keyTyped(char c, int i) throws IOException {
	// if ((i == mc.gameSettings.keyBindForward.getKeyCode()) || (i == 200)) {
	// --selected;
	// }
	// if ((i == mc.gameSettings.keyBindBack.getKeyCode()) || (i == 208)) {
	// ++selected;
	// }
	// if (i == 28) {
	// handleDialogSelection();
	// }
	// if (closeOnEsc && (i == 1)) {
	// NoppesUtilPlayer.sendData(EnumPlayerPacket.Dialog, dialog.id, -1);
	// closed();
	// close();
	// }
	// super.keyTyped(c, i);
	// }
	//
	// @Override
	// public void mouseClicked(int i, int j, int k) {
	// if (((selected == -1) && options.isEmpty()) || (selected >= 0)) {
	// handleDialogSelection();
	// }
	// }
}
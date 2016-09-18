
package noppes.npcs.client.gui.util;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;

public class GuiNpcTextArea extends GuiNpcTextField {
	public boolean inMenu;
	public boolean numbersOnly;
	private int posX;
	private int posY;
	private int width;
	private int height;
	private int cursorCounter;
	private int cursorPosition;
	private int listHeight;
	private float scrolledY;
	private int startClick;
	private boolean clickVerticalBar;
	private boolean wrapLine;

	public GuiNpcTextArea(int id, GuiScreen guiscreen, int i, int j, int k, int l, String s) {
		super(id, guiscreen, i, j, k, l, s);
		inMenu = true;
		numbersOnly = false;
		cursorPosition = 0;
		scrolledY = 0.0f;
		startClick = -1;
		clickVerticalBar = false;
		wrapLine = true;
		posX = i;
		posY = j;
		width = k;
		height = l;
		listHeight = l;
		setMaxStringLength(Integer.MAX_VALUE);
		setText(s);
	}

	private void addScrollY(int scrolled) {
		scrolledY -= (1.0f * scrolled) / height;
		if (scrolledY < 0.0f) {
			scrolledY = 0.0f;
		}
		float max = 1.0f - ((1.0f * (height + 2)) / listHeight);
		if (scrolledY > max) {
			scrolledY = max;
		}
	}

	private void drawCursorVertical(int p_146188_1_, int p_146188_2_, int p_146188_3_, int p_146188_4_) {
		if (p_146188_1_ < p_146188_3_) {
			int i1 = p_146188_1_;
			p_146188_1_ = p_146188_3_;
			p_146188_3_ = i1;
		}
		if (p_146188_2_ < p_146188_4_) {
			int i1 = p_146188_2_;
			p_146188_2_ = p_146188_4_;
			p_146188_4_ = i1;
		}
		if (p_146188_3_ > (posX + width)) {
			p_146188_3_ = posX + width;
		}
		if (p_146188_1_ > (posX + width)) {
			p_146188_1_ = posX + width;
		}
		WorldRenderer tessellator = Tessellator.getInstance().getWorldRenderer();
		GlStateManager.color(0.0f, 0.0f, 255.0f, 255.0f);
		GlStateManager.disableTexture2D();
		GlStateManager.enableColorLogic();
		GlStateManager.colorLogicOp(5387);
		tessellator.begin(7, DefaultVertexFormats.POSITION);
		tessellator.pos(p_146188_1_, p_146188_4_, 0.0).endVertex();
		tessellator.pos(p_146188_3_, p_146188_4_, 0.0).endVertex();
		tessellator.pos(p_146188_3_, p_146188_2_, 0.0).endVertex();
		tessellator.pos(p_146188_1_, p_146188_2_, 0.0).endVertex();
		Tessellator.getInstance().draw();
		GlStateManager.disableColorLogic();
		GlStateManager.enableTexture2D();
	}

	@Override
	public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, color);
	}

	@Override
	public void drawTextBox(int mouseX, int mouseY) {
		drawRect(posX - 1, posY - 1, posX + width + 1, posY + height + 1, -6250336);
		drawRect(posX, posY, posX + width, posY + height, -16777216);
		int color = 14737632;
		boolean flag = isFocused() && (((cursorCounter / 6) % 2) == 0);
		int startLine = getStartLineY();
		int maxLine = (height / Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT) + startLine;
		List<String> lines = getLines();
		int charCount = 0;
		int lineCount = 0;
		int maxSize = width - (isScrolling() ? 14 : 4);
		for (int i = 0; i < lines.size(); ++i) {
			String wholeLine = lines.get(i);
			String line = "";
			for (char c : wholeLine.toCharArray()) {
				if ((Minecraft.getMinecraft().fontRendererObj.getStringWidth(line + c) > maxSize) && wrapLine) {
					if ((lineCount >= startLine) && (lineCount < maxLine)) {
						drawString(null, line, posX + 4, posY + 4
								+ ((lineCount - startLine) * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT),
								color);
					}
					line = "";
					++lineCount;
				}
				if (flag && (charCount == cursorPosition) && (lineCount >= startLine) && (lineCount < maxLine)
						&& canEdit) {
					int xx = posX + Minecraft.getMinecraft().fontRendererObj.getStringWidth(line) + 4;
					int yy = posY + ((lineCount - startLine) * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT)
							+ 4;
					if (getText().length() == cursorPosition) {
						Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("_", xx, yy, color);
					} else {
						drawCursorVertical(xx, yy, xx + 1, yy + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT);
					}
				}
				++charCount;
				line += c;
			}
			if ((lineCount >= startLine) && (lineCount < maxLine)) {
				drawString(null, line, posX + 4,
						posY + 4 + ((lineCount - startLine) * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT),
						color);
				if (flag && (charCount == cursorPosition) && canEdit) {
					int xx2 = posX + Minecraft.getMinecraft().fontRendererObj.getStringWidth(line) + 4;
					int yy2 = posY + ((lineCount - startLine) * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT)
							+ 4;
					if (getText().length() == cursorPosition) {
						Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("_", xx2, yy2, color);
					} else {
						drawCursorVertical(xx2, yy2, xx2 + 1,
								yy2 + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT);
					}
				}
			}
			++lineCount;
			++charCount;
		}
		int k2 = Mouse.getDWheel();
		if ((k2 != 0) && isFocused()) {
			addScrollY((k2 < 0) ? -10 : 10);
		}
		if (Mouse.isButtonDown(0)) {
			if (clickVerticalBar) {
				if (startClick >= 0) {
					addScrollY(startClick - (mouseY - posY));
				}
				if (hoverVerticalScrollBar(mouseX, mouseY)) {
					startClick = mouseY - posY;
				}
				startClick = mouseY - posY;
			}
		} else {
			clickVerticalBar = false;
		}
		listHeight = lineCount * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
		drawVerticalScrollBar();
	}

	private void drawVerticalScrollBar() {
		if (listHeight <= (height - 4)) {
			return;
		}
		Minecraft.getMinecraft().renderEngine.bindTexture(GuiCustomScroll.resource);
		int x = (posX + width) - 6;
		int y = (int) (posY + (scrolledY * height)) + 2;
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		int sbSize = getVerticalBarSize();
		this.drawTexturedModalRect(x, y, width, 9, 5, 1);
		for (int k = 0; k < sbSize; ++k) {
			this.drawTexturedModalRect(x, y + k, width, 10, 5, 1);
		}
		this.drawTexturedModalRect(x, y, width, 11, 5, 1);
	}

	private List<String> getLines() {
		List<String> list = new ArrayList<String>();
		String line = "";
		for (char c : getText().toCharArray()) {
			if ((c == '\r') || (c == '\n')) {
				list.add(line);
				line = "";
			} else {
				line += c;
			}
		}
		list.add(line);
		return list;
	}

	private int getStartLineY() {
		if (!isScrolling()) {
			scrolledY = 0.0f;
		}
		return MathHelper
				.ceiling_double_int((scrolledY * listHeight) / Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT);
	}

	private int getVerticalBarSize() {
		return (int) (((1.0f * height) / listHeight) * (height - 4));
	}

	private boolean hoverVerticalScrollBar(int x, int y) {
		return (listHeight > (height - 4))
				&& ((posY < y) && ((posY + height) > y) && (x < (posX + width)) && (x > (posX + (width - 8))));
	}

	private boolean isScrolling() {
		return listHeight > (height - 4);
	}

	@Override
	public void mouseClicked(int i, int j, int k) {
		isFocused();
		super.mouseClicked(i, j, k);
		if (hoverVerticalScrollBar(i, j)) {
			clickVerticalBar = true;
			startClick = -1;
			return;
		}
		if ((k != 0) || !canEdit) {
			return;
		}
		int x = i - posX;
		int y = ((j - posY - 4) / Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT) + getStartLineY();
		cursorPosition = 0;
		List<String> lines = getLines();
		int charCount = 0;
		int lineCount = 0;
		int maxSize = width - (isScrolling() ? 14 : 4);
		for (int g = 0; g < lines.size(); ++g) {
			String wholeLine = lines.get(g);
			String line = "";
			for (char c : wholeLine.toCharArray()) {
				cursorPosition = charCount;
				if ((Minecraft.getMinecraft().fontRendererObj.getStringWidth(line + c) > maxSize) && wrapLine) {
					++lineCount;
					line = "";
					if (y < lineCount) {
						break;
					}
				}
				if ((lineCount == y) && (x <= Minecraft.getMinecraft().fontRendererObj.getStringWidth(line + c))) {
					return;
				}
				++charCount;
				line += c;
			}
			cursorPosition = charCount;
			++lineCount;
			++charCount;
			if (y < lineCount) {
				break;
			}
		}
		if (y >= lineCount) {
			cursorPosition = getText().length();
		}
	}

	@Override
	public boolean textboxKeyTyped(char c, int i) {
		if (isFocused() && canEdit) {
			String originalText = getText();
			setText(originalText);
			if ((c == '\r') || (c == '\n')) {
				setText(originalText.substring(0, cursorPosition) + c + originalText.substring(cursorPosition));
			}
			setCursorPositionZero();
			moveCursorBy(cursorPosition);
			boolean bo = super.textboxKeyTyped(c, i);
			String newText = getText();
			if (i != 211) {
				cursorPosition += newText.length() - originalText.length();
			}
			if ((i == 203) && (cursorPosition > 0)) {
				--cursorPosition;
			}
			if ((i == 205) && (cursorPosition < newText.length())) {
				++cursorPosition;
			}
			return bo;
		}
		return false;
	}

	@Override
	public void updateCursorCounter() {
		++cursorCounter;
	}
}

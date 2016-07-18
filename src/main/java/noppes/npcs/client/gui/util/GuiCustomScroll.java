//

//

package noppes.npcs.client.gui.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiCustomScroll extends GuiScreen {
	public static final ResourceLocation resource;
	static {
		resource = new ResourceLocation("customnpcs", "textures/gui/misc.png");
	}
	private List<String> list;
	public int id;
	public int guiLeft;
	public int guiTop;
	private int xSize;
	private int ySize;
	public int selected;
	private HashSet<String> selectedList;
	private int hover;
	private int listHeight;
	private int scrollY;
	private int maxScrollY;
	private int scrollHeight;
	private boolean isScrolling;
	private boolean multipleSelection;
	private ICustomScrollListener listener;
	private boolean isSorted;
	public boolean visible;

	private boolean selectable;

	public GuiCustomScroll(final GuiScreen parent, final int id) {
		guiLeft = 0;
		guiTop = 0;
		multipleSelection = false;
		isSorted = true;
		visible = true;
		selectable = true;
		width = 176;
		height = 166;
		xSize = 176;
		ySize = 159;
		selected = -1;
		hover = -1;
		selectedList = new HashSet<String>();
		listHeight = 0;
		scrollY = 0;
		scrollHeight = 0;
		isScrolling = false;
		if (parent instanceof ICustomScrollListener) {
			listener = (ICustomScrollListener) parent;
		}
		list = new ArrayList<String>();
		this.id = id;
	}

	public GuiCustomScroll(final GuiScreen parent, final int id, final boolean multipleSelection) {
		this(parent, id);
		this.multipleSelection = multipleSelection;
	}

	public void clear() {
		list = new ArrayList<String>();
		selected = -1;
		scrollY = 0;
		setSize(xSize, ySize);
	}

	protected void drawItems() {
		for (int i = 0; i < list.size(); ++i) {
			final int j = 4;
			final int k = ((14 * i) + 4) - scrollY;
			if ((k >= 4) && ((k + 12) < ySize)) {
				final int xOffset = (scrollHeight < (ySize - 8)) ? 0 : 10;
				final String displayString = StatCollector.translateToLocal(list.get(i));
				String text = "";
				final float maxWidth = ((xSize + xOffset) - 8) * 0.8f;
				if (fontRendererObj.getStringWidth(displayString) > maxWidth) {
					for (int h = 0; h < displayString.length(); ++h) {
						final char c = displayString.charAt(h);
						text += c;
						if (fontRendererObj.getStringWidth(text) > maxWidth) {
							break;
						}
					}
					if (displayString.length() > text.length()) {
						text += "...";
					}
				} else {
					text = displayString;
				}
				if ((multipleSelection && selectedList.contains(text)) || (!multipleSelection && (selected == i))) {
					drawVerticalLine(j - 2, k - 4, k + 10, -1);
					drawVerticalLine(((j + xSize) - 18) + xOffset, k - 4, k + 10, -1);
					drawHorizontalLine(j - 2, ((j + xSize) - 18) + xOffset, k - 3, -1);
					drawHorizontalLine(j - 2, ((j + xSize) - 18) + xOffset, k + 10, -1);
					fontRendererObj.drawString(text, j, k, 16777215);
				} else if (i == hover) {
					fontRendererObj.drawString(text, j, k, 65280);
				} else {
					fontRendererObj.drawString(text, j, k, 16777215);
				}
			}
		}
	}

	public void drawScreen(int i, int j, final float f, final int mouseScrolled) {
		if (!visible) {
			return;
		}
		drawGradientRect(guiLeft, guiTop, xSize + guiLeft, ySize + guiTop, -1072689136, -804253680);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(GuiCustomScroll.resource);
		if (scrollHeight < (ySize - 8)) {
			drawScrollBar();
		}
		GlStateManager.pushMatrix();
		GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translate(guiLeft, guiTop, 0.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		if (selectable) {
			hover = getMouseOver(i, j);
		}
		drawItems();
		GlStateManager.popMatrix();
		if (scrollHeight < (ySize - 8)) {
			i -= guiLeft;
			j -= guiTop;
			if (Mouse.isButtonDown(0)) {
				if ((i >= (xSize - 11)) && (i < (xSize - 6)) && (j >= 4) && (j < ySize)) {
					isScrolling = true;
				}
			} else {
				isScrolling = false;
			}
			if (isScrolling) {
				scrollY = (((j - 8) * listHeight) / (ySize - 8)) - scrollHeight;
				if (scrollY < 0) {
					scrollY = 0;
				}
				if (scrollY > maxScrollY) {
					scrollY = maxScrollY;
				}
			}
			if (mouseScrolled != 0) {
				scrollY += ((mouseScrolled > 0) ? -14 : 14);
				if (scrollY > maxScrollY) {
					scrollY = maxScrollY;
				}
				if (scrollY < 0) {
					scrollY = 0;
				}
			}
		}
	}

	private void drawScrollBar() {
		final int i = (guiLeft + xSize) - 9;
		int k;
		final int j = k = guiTop + ((scrollY / listHeight) * (ySize - 8)) + 4;
		this.drawTexturedModalRect(i, k, xSize, 9, 5, 1);
		++k;
		while (k < ((j + scrollHeight) - 1)) {
			this.drawTexturedModalRect(i, k, xSize, 10, 5, 1);
			++k;
		}
		this.drawTexturedModalRect(i, k, xSize, 11, 5, 1);
	}

	public List<String> getList() {
		return list;
	}

	private int getMouseOver(int i, int j) {
		i -= guiLeft;
		j -= guiTop;
		if ((i >= 4) && (i < (xSize - 4)) && (j >= 4) && (j < ySize)) {
			for (int j2 = 0; j2 < list.size(); ++j2) {
				if (mouseInOption(i, j, j2)) {
					return j2;
				}
			}
		}
		return -1;
	}

	public String getSelected() {
		if ((selected == -1) || (selected >= list.size())) {
			return null;
		}
		return list.get(selected);
	}

	public HashSet<String> getSelectedList() {
		return selectedList;
	}

	public boolean hasSelected() {
		return selected >= 0;
	}

	public boolean isMouseOver(final int x, final int y) {
		return (x >= guiLeft) && (x <= (guiLeft + xSize)) && (y >= guiTop) && (y <= (guiTop + ySize));
	}

	@Override
	public void mouseClicked(final int i, final int j, final int k) {
		if ((k != 0) || (hover < 0)) {
			return;
		}
		if (multipleSelection) {
			if (selectedList.contains(list.get(hover))) {
				selectedList.remove(list.get(hover));
			} else {
				selectedList.add(list.get(hover));
			}
		} else {
			if (hover >= 0) {
				selected = hover;
			}
			hover = -1;
		}
		if (listener != null) {
			listener.customScrollClicked(i, j, k, this);
		}
	}

	public boolean mouseInOption(final int i, final int j, final int k) {
		final int l = 4;
		final int i2 = ((14 * k) + 4) - scrollY;
		return (i >= (l - 1)) && (i < ((l + xSize) - 11)) && (j >= (i2 - 1)) && (j < (i2 + 8));
	}

	public void replace(final String old, final String name) {
		String select = getSelected();
		list.remove(old);
		list.add(name);
		if (isSorted) {
			Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
		}
		if (old.equals(select)) {
			select = name;
		}
		selected = list.indexOf(select);
		setSize(xSize, ySize);
	}

	public void scrollTo(final String name) {
		final int i = list.indexOf(name);
		if ((i < 0) || (scrollHeight >= (ySize - 8))) {
			return;
		}
		int pos = (int) (((1.0f * i) / list.size()) * listHeight);
		if (pos > maxScrollY) {
			pos = maxScrollY;
		}
		scrollY = pos;
	}

	public void setList(final List<String> list) {
		isSorted = true;
		Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
		this.list = list;
		setSize(xSize, ySize);
	}

	public void setSelected(final String name) {
		selected = list.indexOf(name);
	}

	public void setSelectedList(final HashSet<String> selectedList) {
		this.selectedList = selectedList;
	}

	public void setSize(final int x, final int y) {
		ySize = y;
		xSize = x;
		listHeight = 14 * list.size();
		if (listHeight > 0) {
			scrollHeight = ((ySize - 8) / listHeight) * (ySize - 8);
		} else {
			scrollHeight = Integer.MAX_VALUE;
		}
		maxScrollY = listHeight - (ySize - 8) - 1;
	}

	public GuiCustomScroll setUnselectable() {
		selectable = false;
		return this;
	}

	public void setUnsortedList(final List<String> list) {
		isSorted = false;
		this.list = list;
		setSize(xSize, ySize);
	}
}

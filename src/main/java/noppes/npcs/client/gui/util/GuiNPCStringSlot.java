
package noppes.npcs.client.gui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;

public class GuiNPCStringSlot extends GuiSlot {
	private List<String> list;
	public String selected;
	public HashSet<String> selectedList;
	private boolean multiSelect;
	private GuiNPCInterface parent;
	public int size;

	public GuiNPCStringSlot(Collection<String> list, GuiNPCInterface parent, boolean multiSelect, int size) {
		super(Minecraft.getMinecraft(), parent.width, parent.height, 32, parent.height - 64, size);
		selectedList = new HashSet<>();
		this.parent = parent;
		Collections.sort(this.list = new ArrayList<>(list), String.CASE_INSENSITIVE_ORDER);
		this.multiSelect = multiSelect;
		this.size = size;
	}

	public void clear() {
		list.clear();
	}

	@Override
	protected void drawBackground() {
		parent.drawDefaultBackground();
	}

	@Override
	protected void drawSlot(int i, int j, int k, int l, int var6, int var7) {
		String s = list.get(i);
		parent.drawString(parent.getFontRenderer(), s, j + 50, k + 3, 16777215);
	}

	@Override
	protected void elementClicked(int i, boolean flag, int j, int k) {
		if ((selected != null) && selected.equals(list.get(i)) && flag) {
			parent.doubleClicked();
		}
		selected = list.get(i);
		if (selectedList.contains(selected)) {
			selectedList.remove(selected);
		} else {
			selectedList.add(selected);
		}
		parent.elementClicked();
	}

	@Override
	protected int getContentHeight() {
		return list.size() * size;
	}

	@Override
	protected int getSize() {
		return list.size();
	}

	@Override
	protected boolean isSelected(int i) {
		if (!multiSelect) {
			return (selected != null) && selected.equals(list.get(i));
		}
		return selectedList.contains(list.get(i));
	}

	public void setList(List<String> list) {
		Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
		this.list = list;
		selected = "";
	}
}

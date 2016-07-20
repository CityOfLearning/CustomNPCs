//

//

package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiNpcTextField extends GuiTextField {
	private static GuiNpcTextField activeTextfield;
	static {
		GuiNpcTextField.activeTextfield = null;
	}

	public static boolean isActive() {
		return GuiNpcTextField.activeTextfield != null;
	}

	public static void unfocus() {
		GuiNpcTextField prev = GuiNpcTextField.activeTextfield;
		GuiNpcTextField.activeTextfield = null;
		if (prev != null) {
			prev.unFocused();
		}
	}

	public boolean enabled;
	public boolean inMenu;
	public boolean numbersOnly;
	private ITextfieldListener listener;
	public int id;
	public int min;
	public int max;

	public int def;

	public boolean canEdit;

	private int[] allowedSpecialChars;

	public GuiNpcTextField(int id, GuiScreen parent, FontRenderer fontRenderer, int i, int j, int k, int l, String s) {
		super(id, fontRenderer, i, j, k, l);
		enabled = true;
		inMenu = true;
		numbersOnly = false;
		min = 0;
		max = Integer.MAX_VALUE;
		def = 0;
		canEdit = true;
		allowedSpecialChars = new int[] { 14, 211, 203, 205 };
		setMaxStringLength(500);
		setText((s == null) ? "" : s);
		this.id = id;
		if (parent instanceof ITextfieldListener) {
			listener = (ITextfieldListener) parent;
		}
	}

	public GuiNpcTextField(int id, GuiScreen parent, int i, int j, int k, int l, String s) {
		this(id, parent, Minecraft.getMinecraft().fontRendererObj, i, j, k, l, s);
	}

	private boolean charAllowed(char c, int i) {
		if (!numbersOnly || Character.isDigit(c) || ((c == '-') && (getText().length() == 0))) {
			return true;
		}
		for (int j : allowedSpecialChars) {
			if (j == i) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void drawTextBox() {
		if (enabled) {
			super.drawTextBox();
		}
	}

	public void drawTextBox(int mousX, int mousY) {
		this.drawTextBox();
	}

	public int getInteger() {
		return Integer.parseInt(getText());
	}

	public boolean isEmpty() {
		return getText().trim().length() == 0;
	}

	public boolean isInteger() {
		try {
			Integer.parseInt(getText());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public void mouseClicked(int i, int j, int k) {
		if (!canEdit) {
			return;
		}
		boolean wasFocused = isFocused();
		super.mouseClicked(i, j, k);
		if ((wasFocused != isFocused()) && wasFocused) {
			unFocused();
		}
		if (isFocused()) {
			GuiNpcTextField.activeTextfield = this;
		}
	}

	public void setMinMaxDefault(int i, int j, int k) {
		min = i;
		max = j;
		def = k;
	}

	public GuiNpcTextField setNumbersOnly() {
		numbersOnly = true;
		return this;
	}

	@Override
	public boolean textboxKeyTyped(char c, int i) {
		return charAllowed(c, i) && canEdit && super.textboxKeyTyped(c, i);
	}

	public void unFocused() {
		if (numbersOnly) {
			if (isEmpty() || !isInteger()) {
				setText(def + "");
			} else if (getInteger() < min) {
				setText(min + "");
			} else if (getInteger() > max) {
				setText(max + "");
			}
		}
		if (listener != null) {
			listener.unFocused(this);
		}
		if (this == GuiNpcTextField.activeTextfield) {
			GuiNpcTextField.activeTextfield = null;
		}
	}
}

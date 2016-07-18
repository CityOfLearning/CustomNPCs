//

//

package noppes.npcs.client.gui.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noppes.npcs.containers.ContainerEmpty;
import noppes.npcs.entity.EntityNPCInterface;

public abstract class GuiContainerNPCInterface extends GuiContainer {
	public boolean drawDefaultBackground;
	public int guiLeft;
	public int guiTop;
	public EntityPlayerSP player;
	public EntityNPCInterface npc;
	private HashMap<Integer, GuiNpcButton> buttons;
	private HashMap<Integer, GuiMenuTopButton> topbuttons;
	private HashMap<Integer, GuiNpcTextField> textfields;
	private HashMap<Integer, GuiNpcLabel> labels;
	private HashMap<Integer, GuiCustomScroll> scrolls;
	private HashMap<Integer, GuiNpcSlider> sliders;
	public String title;
	public boolean closeOnEsc;
	private SubGuiInterface subgui;
	public int mouseX;
	public int mouseY;

	public GuiContainerNPCInterface(final EntityNPCInterface npc, final Container cont) {
		super(cont);
		drawDefaultBackground = false;
		buttons = new HashMap<Integer, GuiNpcButton>();
		topbuttons = new HashMap<Integer, GuiMenuTopButton>();
		textfields = new HashMap<Integer, GuiNpcTextField>();
		labels = new HashMap<Integer, GuiNpcLabel>();
		scrolls = new HashMap<Integer, GuiCustomScroll>();
		sliders = new HashMap<Integer, GuiNpcSlider>();
		closeOnEsc = false;
		player = Minecraft.getMinecraft().thePlayer;
		this.npc = npc;
		title = "Npc Mainmenu";
		mc = Minecraft.getMinecraft();
		itemRender = mc.getRenderItem();
		fontRendererObj = mc.fontRendererObj;
	}

	@Override
	protected void actionPerformed(final GuiButton guibutton) {
		if (subgui != null) {
			subgui.buttonEvent(guibutton);
		} else {
			buttonEvent(guibutton);
		}
	}

	public void addButton(final GuiNpcButton button) {
		buttons.put(button.id, button);
		buttonList.add(button);
	}

	public void addLabel(final GuiNpcLabel label) {
		labels.put(label.id, label);
	}

	public void addScroll(final GuiCustomScroll scroll) {
		scroll.setWorldAndResolution(mc, 350, 250);
		scrolls.put(scroll.id, scroll);
	}

	public void addSlider(final GuiNpcSlider slider) {
		sliders.put(slider.id, slider);
		buttonList.add(slider);
	}

	public void addTextField(final GuiNpcTextField tf) {
		textfields.put(tf.id, tf);
	}

	public void addTopButton(final GuiMenuTopButton button) {
		topbuttons.put(button.id, button);
		buttonList.add(button);
	}

	public void buttonEvent(final GuiButton guibutton) {
	}

	public void close() {
		GuiNpcTextField.unfocus();
		save();
		player.closeScreen();
		displayGuiScreen(null);
		mc.setIngameFocus();
	}

	public void closeSubGui(final SubGuiInterface gui) {
		subgui = null;
	}

	public void displayGuiScreen(final GuiScreen gui) {
		mc.displayGuiScreen(gui);
	}

	@Override
	public void drawDefaultBackground() {
		if (drawDefaultBackground && (subgui == null)) {
			super.drawDefaultBackground();
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int i, final int j) {
		drawCenteredString(fontRendererObj, StatCollector.translateToLocal(title), width / 2, guiTop - 8, 16777215);
		for (final GuiNpcLabel label : new ArrayList<GuiNpcLabel>(labels.values())) {
			label.drawLabel(this, fontRendererObj);
		}
		for (final GuiNpcTextField tf : new ArrayList<GuiNpcTextField>(textfields.values())) {
			tf.drawTextBox(i, j);
		}
		for (final GuiCustomScroll scroll : new ArrayList<GuiCustomScroll>(scrolls.values())) {
			scroll.drawScreen(i, j, f, hasSubGui() ? 0 : Mouse.getDWheel());
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {
	}

	public void drawNpc(final int x, final int y) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate(guiLeft + x, guiTop + y, 50.0f);
		float scale = 1.0f;
		if (npc.height > 2.4) {
			scale = 2.0f / npc.height;
		}
		GlStateManager.scale(-30.0f * scale, 30.0f * scale, 30.0f * scale);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		final float f2 = npc.renderYawOffset;
		final float f3 = npc.rotationYaw;
		final float f4 = npc.rotationPitch;
		final float f5 = npc.rotationYawHead;
		final float f6 = (guiLeft + x) - mouseX;
		final float f7 = (guiTop + y) - 50 - mouseY;
		int orientation = 0;
		if (npc != null) {
			orientation = npc.ai.orientation;
			npc.ai.orientation = 0;
		}
		GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(-(float) Math.atan(f7 / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
		npc.renderYawOffset = (float) Math.atan(f6 / 40.0f) * 20.0f;
		npc.rotationYaw = (float) Math.atan(f6 / 40.0f) * 40.0f;
		npc.rotationPitch = -(float) Math.atan(f7 / 40.0f) * 20.0f;
		npc.rotationYawHead = npc.rotationYaw;
		mc.getRenderManager().playerViewY = 180.0f;
		mc.getRenderManager().renderEntityWithPosYaw(npc, 0.0, 0.0, 0.0, 0.0f, 1.0f);
		npc.renderYawOffset = f2;
		npc.rotationYaw = f3;
		npc.rotationPitch = f4;
		npc.rotationYawHead = f5;
		if (npc != null) {
			npc.ai.orientation = orientation;
		}
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
	}

	@Override
	public void drawScreen(final int i, final int j, final float f) {
		mouseX = i;
		mouseY = j;
		final Container container = inventorySlots;
		if (subgui != null) {
			inventorySlots = new ContainerEmpty();
		}
		super.drawScreen(i, j, f);
		zLevel = 0.0f;
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		if (subgui != null) {
			inventorySlots = container;
			RenderHelper.disableStandardItemLighting();
			subgui.drawScreen(i, j, f);
		}
	}

	public GuiNpcButton getButton(final int i) {
		return buttons.get(i);
	}

	public FontRenderer getFontRenderer() {
		return fontRendererObj;
	}

	public GuiNpcLabel getLabel(final int i) {
		return labels.get(i);
	}

	public ResourceLocation getResource(final String texture) {
		return new ResourceLocation("customnpcs", "textures/gui/" + texture);
	}

	public GuiCustomScroll getScroll(final int id) {
		return scrolls.get(id);
	}

	public GuiNpcSlider getSlider(final int i) {
		return sliders.get(i);
	}

	public SubGuiInterface getSubGui() {
		if (hasSubGui() && subgui.hasSubGui()) {
			return subgui.getSubGui();
		}
		return subgui;
	}

	public GuiNpcTextField getTextField(final int i) {
		return textfields.get(i);
	}

	public GuiMenuTopButton getTopButton(final int i) {
		return topbuttons.get(i);
	}

	public boolean hasSubGui() {
		return subgui != null;
	}

	@Override
	public void initGui() {
		super.initGui();
		GuiNpcTextField.unfocus();
		buttonList.clear();
		buttons.clear();
		topbuttons.clear();
		scrolls.clear();
		sliders.clear();
		labels.clear();
		textfields.clear();
		Keyboard.enableRepeatEvents(true);
		if (subgui != null) {
			subgui.setWorldAndResolution(mc, width, height);
			subgui.initGui();
		}
		buttonList.clear();
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
	}

	public void initPacket() {
	}

	@Override
	protected void keyTyped(final char c, final int i) {
		if (subgui != null) {
			subgui.keyTyped(c, i);
		} else {
			for (final GuiNpcTextField tf : new ArrayList<GuiNpcTextField>(textfields.values())) {
				tf.textboxKeyTyped(c, i);
			}
			if (closeOnEsc && ((i == 1)
					|| ((i == mc.gameSettings.keyBindInventory.getKeyCode()) && !GuiNpcTextField.isActive()))) {
				close();
			}
		}
	}

	@Override
	protected void mouseClicked(final int i, final int j, final int k) throws IOException {
		if (subgui != null) {
			subgui.mouseClicked(i, j, k);
		} else {
			for (final GuiNpcTextField tf : new ArrayList<GuiNpcTextField>(textfields.values())) {
				if (tf.enabled) {
					tf.mouseClicked(i, j, k);
				}
			}
			if (k == 0) {
				for (final GuiCustomScroll scroll : new ArrayList<GuiCustomScroll>(scrolls.values())) {
					scroll.mouseClicked(i, j, k);
				}
			}
			mouseEvent(i, j, k);
			super.mouseClicked(i, j, k);
		}
	}

	public void mouseEvent(final int i, final int j, final int k) {
	}

	public abstract void save();

	public void setSubGui(final SubGuiInterface gui) {
		(subgui = gui).setWorldAndResolution(mc, width, height);
		((GuiContainerNPCInterface) (subgui.parent = this)).initGui();
	}

	@Override
	public void setWorldAndResolution(final Minecraft mc, final int width, final int height) {
		super.setWorldAndResolution(mc, width, height);
		initPacket();
	}

	@Override
	public void updateScreen() {
		for (final GuiNpcTextField tf : new ArrayList<GuiNpcTextField>(textfields.values())) {
			if (tf.enabled) {
				tf.updateCursorCounter();
			}
		}
		super.updateScreen();
	}
}

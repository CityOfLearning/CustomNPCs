//

//

package noppes.npcs.client.gui.util;

import java.awt.Toolkit;
import java.awt.Window;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import noppes.npcs.entity.EntityNPCInterface;

public abstract class GuiNPCInterface extends GuiScreen {
	public static Window AWTWindow;
	public EntityPlayerSP player;
	public boolean drawDefaultBackground;
	public EntityNPCInterface npc;
	private Map<Integer, GuiNpcButton> buttons;
	private Map<Integer, GuiMenuTopButton> topbuttons;
	private Map<Integer, GuiMenuSideButton> sidebuttons;
	private Map<Integer, GuiNpcTextField> textfields;
	private Map<Integer, GuiNpcLabel> labels;
	private Map<Integer, GuiCustomScroll> scrolls;
	private Map<Integer, GuiNpcSlider> sliders;
	private Map<Integer, GuiScreen> extra;
	public String title;
	public ResourceLocation background;
	public boolean closeOnEsc;
	public int guiLeft;
	public int guiTop;
	public int xSize;
	public int ySize;
	private SubGuiInterface subgui;
	public int mouseX;
	public int mouseY;
	public float bgScale;
	private GuiButton selectedButton;

	public GuiNPCInterface() {
		this(null);
	}

	public GuiNPCInterface(final EntityNPCInterface npc) {
		drawDefaultBackground = true;
		buttons = new ConcurrentHashMap<Integer, GuiNpcButton>();
		topbuttons = new ConcurrentHashMap<Integer, GuiMenuTopButton>();
		sidebuttons = new ConcurrentHashMap<Integer, GuiMenuSideButton>();
		textfields = new ConcurrentHashMap<Integer, GuiNpcTextField>();
		labels = new ConcurrentHashMap<Integer, GuiNpcLabel>();
		scrolls = new ConcurrentHashMap<Integer, GuiCustomScroll>();
		sliders = new ConcurrentHashMap<Integer, GuiNpcSlider>();
		extra = new ConcurrentHashMap<Integer, GuiScreen>();
		background = null;
		closeOnEsc = false;
		bgScale = 1.0f;
		player = Minecraft.getMinecraft().thePlayer;
		this.npc = npc;
		title = "";
		xSize = 200;
		ySize = 222;
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

	public void addExtra(final GuiHoverText gui) {
		gui.setWorldAndResolution(mc, 350, 250);
		extra.put(gui.id, gui);
	}

	public void addLabel(final GuiNpcLabel label) {
		labels.put(label.id, label);
	}

	public void addScroll(final GuiCustomScroll scroll) {
		scroll.setWorldAndResolution(mc, 350, 250);
		scrolls.put(scroll.id, scroll);
	}

	public void addSideButton(final GuiMenuSideButton button) {
		sidebuttons.put(button.id, button);
		buttonList.add(button);
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
		displayGuiScreen(null);
		mc.setIngameFocus();
		save();
	}

	public void closeSubGui(final SubGuiInterface gui) {
		subgui = null;
	}

	public void displayGuiScreen(final GuiScreen gui) {
		mc.displayGuiScreen(gui);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	public void doubleClicked() {
	}

	@Override
	public void drawDefaultBackground() {
		super.drawDefaultBackground();
	}

	public void drawNpc(final EntityLivingBase entity, final int x, final int y, final float zoomed,
			final int rotation) {
		EntityNPCInterface npc = null;
		if (entity instanceof EntityNPCInterface) {
			npc = (EntityNPCInterface) entity;
		}
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate(guiLeft + x, guiTop + y, 50.0f);
		float scale = 1.0f;
		if (entity.height > 2.4) {
			scale = 2.0f / entity.height;
		}
		GlStateManager.scale(-30.0f * scale * zoomed, 30.0f * scale * zoomed, 30.0f * scale * zoomed);
		GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
		RenderHelper.enableStandardItemLighting();
		final float f2 = entity.renderYawOffset;
		final float f3 = entity.rotationYaw;
		final float f4 = entity.rotationPitch;
		final float f5 = entity.rotationYawHead;
		final float f6 = (guiLeft + x) - mouseX;
		final float f7 = (guiTop + y) - (50.0f * scale * zoomed) - mouseY;
		int orientation = 0;
		if (npc != null) {
			orientation = npc.ai.orientation;
			npc.ai.orientation = rotation;
		}
		GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(-(float) Math.atan(f7 / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
		entity.renderYawOffset = rotation;
		entity.rotationYaw = ((float) Math.atan(f6 / 80.0f) * 40.0f) + rotation;
		entity.rotationPitch = -(float) Math.atan(f7 / 40.0f) * 20.0f;
		entity.rotationYawHead = entity.rotationYaw;
		mc.getRenderManager().playerViewY = 180.0f;
		mc.getRenderManager().renderEntityWithPosYaw(entity, 0.0, 0.0, 0.0, 0.0f, 1.0f);
		final float n = f2;
		entity.renderYawOffset = n;
		entity.prevRenderYawOffset = n;
		final float n2 = f3;
		entity.rotationYaw = n2;
		entity.prevRotationYaw = n2;
		final float n3 = f4;
		entity.rotationPitch = n3;
		entity.prevRotationPitch = n3;
		final float n4 = f5;
		entity.rotationYawHead = n4;
		entity.prevRotationYawHead = n4;
		if (npc != null) {
			npc.ai.orientation = orientation;
		}
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	public void drawNpc(final int x, final int y) {
		this.drawNpc(npc, x, y, 1.0f, 0);
	}

	@Override
	public void drawScreen(final int i, final int j, final float f) {
		if (GuiNPCInterface.AWTWindow != null) {
			if (!GuiNPCInterface.AWTWindow.isVisible()) {
				GuiNPCInterface.AWTWindow.dispose();
				GuiNPCInterface.AWTWindow = null;
			} else if (Display.isActive()) {
				Toolkit.getDefaultToolkit().beep();
				GuiNPCInterface.AWTWindow.setVisible(true);
			}
		}
		mouseX = i;
		mouseY = j;
		if (drawDefaultBackground && (subgui == null)) {
			drawDefaultBackground();
		}
		if ((background != null) && (mc.renderEngine != null)) {
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			GlStateManager.pushMatrix();
			GlStateManager.translate(guiLeft, guiTop, 0.0f);
			GlStateManager.scale(bgScale, bgScale, bgScale);
			mc.renderEngine.bindTexture(background);
			if (xSize > 256) {
				this.drawTexturedModalRect(0, 0, 0, 0, 250, ySize);
				this.drawTexturedModalRect(250, 0, 256 - (xSize - 250), 0, xSize - 250, ySize);
			} else {
				this.drawTexturedModalRect(0, 0, 0, 0, xSize, ySize);
			}
			GlStateManager.popMatrix();
		}
		drawCenteredString(fontRendererObj, title, width / 2, 8, 16777215);
		for (final GuiNpcLabel label : new ArrayList<GuiNpcLabel>(labels.values())) {
			label.drawLabel(this, fontRendererObj);
		}
		for (final GuiNpcTextField tf : new ArrayList<GuiNpcTextField>(textfields.values())) {
			tf.drawTextBox(i, j);
		}
		for (final GuiCustomScroll scroll : new ArrayList<GuiCustomScroll>(scrolls.values())) {
			scroll.drawScreen(i, j, f, (!hasSubGui() && scroll.isMouseOver(i, j)) ? Mouse.getDWheel() : 0);
		}
		for (final GuiScreen gui : new ArrayList<GuiScreen>(extra.values())) {
			gui.drawScreen(i, j, f);
		}
		super.drawScreen(i, j, f);
		if (subgui != null) {
			subgui.drawScreen(i, j, f);
		}
	}

	public void elementClicked() {
		if (subgui != null) {
			subgui.elementClicked();
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

	public GuiMenuSideButton getSideButton(final int i) {
		return sidebuttons.get(i);
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
		if (subgui != null) {
			subgui.setWorldAndResolution(mc, width, height);
			subgui.initGui();
		}
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
		buttonList = Lists.newArrayList();
		buttons = new ConcurrentHashMap<Integer, GuiNpcButton>();
		topbuttons = new ConcurrentHashMap<Integer, GuiMenuTopButton>();
		sidebuttons = new ConcurrentHashMap<Integer, GuiMenuSideButton>();
		textfields = new ConcurrentHashMap<Integer, GuiNpcTextField>();
		labels = new ConcurrentHashMap<Integer, GuiNpcLabel>();
		scrolls = new ConcurrentHashMap<Integer, GuiCustomScroll>();
		sliders = new ConcurrentHashMap<Integer, GuiNpcSlider>();
		extra = new ConcurrentHashMap<Integer, GuiScreen>();
	}

	public void initPacket() {
	}

	public boolean isInventoryKey(final int i) {
		return i == mc.gameSettings.keyBindInventory.getKeyCode();
	}

	@Override
	public void keyTyped(final char c, final int i) {
		if (GuiNPCInterface.AWTWindow != null) {
			return;
		}
		if (subgui != null) {
			subgui.keyTyped(c, i);
		}
		for (final GuiNpcTextField tf : new ArrayList<GuiNpcTextField>(textfields.values())) {
			tf.textboxKeyTyped(c, i);
		}
		if (closeOnEsc && ((i == 1) || (!GuiNpcTextField.isActive() && isInventoryKey(i)))) {
			close();
		}
	}

	@Override
	public void mouseClicked(final int i, final int j, final int k) {
		if (GuiNPCInterface.AWTWindow != null) {
			return;
		}
		if (subgui != null) {
			subgui.mouseClicked(i, j, k);
		} else {
			for (final GuiNpcTextField tf : new ArrayList<GuiNpcTextField>(textfields.values())) {
				if (tf.enabled) {
					tf.mouseClicked(i, j, k);
				}
			}
			mouseEvent(i, j, k);
			if (k == 0) {
				for (final GuiCustomScroll scroll : new ArrayList<GuiCustomScroll>(scrolls.values())) {
					scroll.mouseClicked(i, j, k);
				}
				for (GuiButton guibutton : buttonList) {
					if (guibutton.mousePressed(mc, mouseX, mouseY)) {
						final GuiScreenEvent.ActionPerformedEvent.Pre event = new GuiScreenEvent.ActionPerformedEvent.Pre(
								this, guibutton, buttonList);
						if (MinecraftForge.EVENT_BUS.post(event)) {
							break;
						}
						guibutton = event.button;
						(selectedButton = guibutton).playPressSound(mc.getSoundHandler());
						actionPerformed(guibutton);
						if (equals(mc.currentScreen)) {
							MinecraftForge.EVENT_BUS
									.post(new GuiScreenEvent.ActionPerformedEvent.Post(this, event.button, buttonList));
							break;
						}
						break;
					}
				}
			}
		}
	}

	public void mouseEvent(final int i, final int j, final int k) {
	}

	@Override
	public void mouseReleased(final int mouseX, final int mouseY, final int state) {
		if ((selectedButton != null) && (state == 0)) {
			selectedButton.mouseReleased(mouseX, mouseY);
			selectedButton = null;
		}
	}

	@Override
	public void onGuiClosed() {
		GuiNpcTextField.unfocus();
	}

	public void openLink(final String link) {
		try {
			final Class oclass = Class.forName("java.awt.Desktop");
			final Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
			oclass.getMethod("browse", URI.class).invoke(object, new URI(link));
		} catch (Throwable t) {
		}
	}

	public abstract void save();

	public void setBackground(final String texture) {
		background = new ResourceLocation("customnpcs", "textures/gui/" + texture);
	}

	public void setSubGui(final SubGuiInterface gui) {
		(subgui = gui).setWorldAndResolution(mc, width, height);
		((GuiNPCInterface) (subgui.parent = this)).initGui();
	}

	@Override
	public void setWorldAndResolution(final Minecraft mc, final int width, final int height) {
		super.setWorldAndResolution(mc, width, height);
		initPacket();
	}

	@Override
	public void updateScreen() {
		if (subgui != null) {
			subgui.updateScreen();
		} else {
			for (final GuiNpcTextField tf : new ArrayList<GuiNpcTextField>(textfields.values())) {
				if (tf.enabled) {
					tf.updateCursorCounter();
				}
			}
			super.updateScreen();
		}
	}
}

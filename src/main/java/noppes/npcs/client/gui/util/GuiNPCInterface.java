
package noppes.npcs.client.gui.util;

import java.awt.Toolkit;
import java.awt.Window;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
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

	public GuiNPCInterface(EntityNPCInterface npc) {
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
		closeOnEsc = true;
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
	protected void actionPerformed(GuiButton guibutton) {
		if (subgui != null) {
			subgui.buttonEvent(guibutton);
		} else {
			buttonEvent(guibutton);
		}
	}

	public void addButton(GuiNpcButton button) {
		buttons.put(button.id, button);
		buttonList.add(button);
	}

	public void addExtra(GuiHoverText gui) {
		gui.setWorldAndResolution(mc, 350, 250);
		extra.put(gui.id, gui);
	}

	public void addLabel(GuiNpcLabel label) {
		labels.put(label.id, label);
	}

	public void addScroll(GuiCustomScroll scroll) {
		scroll.setWorldAndResolution(mc, 350, 250);
		scrolls.put(scroll.id, scroll);
	}

	public void addSideButton(GuiMenuSideButton button) {
		sidebuttons.put(button.id, button);
		buttonList.add(button);
	}

	public void addSlider(GuiNpcSlider slider) {
		sliders.put(slider.id, slider);
		buttonList.add(slider);
	}

	public void addTextField(GuiNpcTextField tf) {
		textfields.put(tf.id, tf);
	}

	public void addTopButton(GuiMenuTopButton button) {
		topbuttons.put(button.id, button);
		buttonList.add(button);
	}

	public void buttonEvent(GuiButton guibutton) {
	}

	public void close() {
		displayGuiScreen(null);
		mc.setIngameFocus();
		save();
	}

	public void closeSubGui(SubGuiInterface gui) {
		subgui = null;
	}

	public void displayGuiScreen(GuiScreen gui) {
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

	public void drawNpc(EntityLivingBase entity, int x, int y, float zoomed, int rotation) {
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
		float f2 = entity.renderYawOffset;
		float f3 = entity.rotationYaw;
		float f4 = entity.rotationPitch;
		float f5 = entity.rotationYawHead;
		float f6 = (guiLeft + x) - mouseX;
		float f7 = (guiTop + y) - (50.0f * scale * zoomed) - mouseY;
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
		float n = f2;
		entity.renderYawOffset = n;
		entity.prevRenderYawOffset = n;
		float n2 = f3;
		entity.rotationYaw = n2;
		entity.prevRotationYaw = n2;
		float n3 = f4;
		entity.rotationPitch = n3;
		entity.prevRotationPitch = n3;
		float n4 = f5;
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

	public void drawNpc(int x, int y) {
		this.drawNpc(npc, x, y, 1.0f, 0);
	}

	public void drawScaledTexturedRect(int x, int y, int z, int width, int height) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		renderer.pos(x + width, y + height, z).tex(1, 1).endVertex();
		renderer.pos(x + width, y, z).tex(1, 0).endVertex();
		renderer.pos(x, y, z).tex(0, 0).endVertex();
		renderer.pos(x, y + height, z).tex(0, 1).endVertex();
		tessellator.draw();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (GuiNPCInterface.AWTWindow != null) {
			if (!GuiNPCInterface.AWTWindow.isVisible()) {
				GuiNPCInterface.AWTWindow.dispose();
				GuiNPCInterface.AWTWindow = null;
			} else if (Display.isActive()) {
				Toolkit.getDefaultToolkit().beep();
				GuiNPCInterface.AWTWindow.setVisible(true);
			}
		}
		this.mouseX = mouseX;
		this.mouseY = mouseY;
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
		for (GuiNpcLabel label : new ArrayList<GuiNpcLabel>(labels.values())) {
			label.drawLabel(this, fontRendererObj);
		}
		for (GuiNpcTextField tf : new ArrayList<GuiNpcTextField>(textfields.values())) {
			tf.drawTextBox(mouseX, mouseY);
		}
		for (GuiCustomScroll scroll : new ArrayList<GuiCustomScroll>(scrolls.values())) {
			scroll.drawScreen(mouseX, mouseY, partialTicks,
					(!hasSubGui() && scroll.isMouseOver(mouseX, mouseY)) ? Mouse.getDWheel() : 0);
		}
		for (GuiScreen gui : new ArrayList<GuiScreen>(extra.values())) {
			gui.drawScreen(mouseX, mouseY, partialTicks);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
		if (subgui != null) {
			subgui.drawScreen(mouseX, mouseY, partialTicks);
		}
	}

	public void elementClicked() {
		if (subgui != null) {
			subgui.elementClicked();
		}
	}

	public GuiNpcButton getButton(int id) {
		return buttons.get(id);
	}

	public FontRenderer getFontRenderer() {
		return fontRendererObj;
	}

	public GuiNpcLabel getLabel(int id) {
		return labels.get(id);
	}

	public ResourceLocation getResource(String texture) {
		return new ResourceLocation("customnpcs", "textures/gui/" + texture);
	}

	public GuiCustomScroll getScroll(int id) {
		return scrolls.get(id);
	}

	public GuiMenuSideButton getSideButton(int id) {
		return sidebuttons.get(id);
	}

	public GuiNpcSlider getSlider(int id) {
		return sliders.get(id);
	}

	public SubGuiInterface getSubGui() {
		if (hasSubGui() && subgui.hasSubGui()) {
			return subgui.getSubGui();
		}
		return subgui;
	}

	public GuiNpcTextField getTextField(int id) {
		return textfields.get(id);
	}

	public GuiMenuTopButton getTopButton(int id) {
		return topbuttons.get(id);
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

	public boolean isInventoryKey(int id) {
		return id == mc.gameSettings.keyBindInventory.getKeyCode();
	}

	@Override
	public void keyTyped(char c, int i) {
		if (GuiNPCInterface.AWTWindow != null) {
			return;
		}
		if (subgui != null) {
			subgui.keyTyped(c, i);
		}
		for (GuiNpcTextField tf : new ArrayList<GuiNpcTextField>(textfields.values())) {
			tf.textboxKeyTyped(c, i);
		}
		if (closeOnEsc && ((i == 1) || (!GuiNpcTextField.isActive() && isInventoryKey(i)))) {
			close();
		}
	}

	@Override
	public void mouseClicked(int i, int j, int k) {
		if (GuiNPCInterface.AWTWindow != null) {
			return;
		}
		if (subgui != null) {
			subgui.mouseClicked(i, j, k);
		} else {
			for (GuiNpcTextField tf : new ArrayList<GuiNpcTextField>(textfields.values())) {
				if (tf.enabled) {
					tf.mouseClicked(i, j, k);
				}
			}
			mouseEvent(i, j, k);
			if (k == 0) {
				for (GuiCustomScroll scroll : new ArrayList<GuiCustomScroll>(scrolls.values())) {
					scroll.mouseClicked(i, j, k);
				}
				for (GuiButton guibutton : buttonList) {
					if (guibutton.mousePressed(mc, mouseX, mouseY)) {
						GuiScreenEvent.ActionPerformedEvent.Pre event = new GuiScreenEvent.ActionPerformedEvent.Pre(
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

	public void mouseEvent(int mouseX, int mouseY, int state) {
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		if ((selectedButton != null) && (state == 0)) {
			selectedButton.mouseReleased(mouseX, mouseY);
			selectedButton = null;
		}
	}

	@Override
	public void onGuiClosed() {
		GuiNpcTextField.unfocus();
	}

	public void openLink(String link) {
		try {
			Class oclass = Class.forName("java.awt.Desktop");
			Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
			oclass.getMethod("browse", URI.class).invoke(object, new URI(link));
		} catch (Throwable t) {
		}
	}

	public abstract void save();

	public void setBackground(String texture) {
		background = new ResourceLocation("customnpcs", "textures/gui/" + texture);
	}

	public void setSubGui(SubGuiInterface gui) {
		(subgui = gui).setWorldAndResolution(mc, width, height);
		((GuiNPCInterface) (subgui.parent = this)).initGui();
	}

	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		super.setWorldAndResolution(mc, width, height);
		initPacket();
	}

	@Override
	public void updateScreen() {
		if (subgui != null) {
			subgui.updateScreen();
		} else {
			for (GuiNpcTextField tf : new ArrayList<GuiNpcTextField>(textfields.values())) {
				if (tf.enabled) {
					tf.updateCursorCounter();
				}
			}
			super.updateScreen();
		}
	}
}

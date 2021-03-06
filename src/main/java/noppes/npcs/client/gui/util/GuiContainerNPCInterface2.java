
package noppes.npcs.client.gui.util;

import java.io.IOException;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.entity.EntityNPCInterface;

public abstract class GuiContainerNPCInterface2 extends GuiContainerNPCInterface {
	private ResourceLocation background;
	private ResourceLocation defaultBackground;
	private GuiNpcMenu menu;
	public int menuYOffset;

	public GuiContainerNPCInterface2(EntityNPCInterface npc, Container cont) {
		this(npc, cont, -1);
	}

	public GuiContainerNPCInterface2(EntityNPCInterface npc, Container cont, int activeMenu) {
		super(npc, cont);
		background = new ResourceLocation("customnpcs", "textures/gui/menubg.png");
		defaultBackground = new ResourceLocation("customnpcs", "textures/gui/menubg.png");
		menuYOffset = 0;
		xSize = 420;
		menu = new GuiNpcMenu(this, activeMenu, npc);
		title = "";
	}

	public void delete() {
		npc.delete();
		displayGuiScreen(null);
		mc.setIngameFocus();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		drawDefaultBackground();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(background);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 256, 256);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(defaultBackground);
		this.drawTexturedModalRect((guiLeft + xSize) - 200, guiTop, 26, 0, 200, 220);
		menu.drawElements(fontRendererObj, i, j, mc, f);
		super.drawGuiContainerBackgroundLayer(f, i, j);
	}

	@Override
	public ResourceLocation getResource(String texture) {
		return new ResourceLocation("customnpcs", "textures/gui/" + texture);
	}

	@Override
	public void initGui() {
		super.initGui();
		menu.initGui(guiLeft, guiTop + menuYOffset, xSize);
	}

	@Override
	protected void mouseClicked(int i, int j, int k) throws IOException {
		super.mouseClicked(i, j, k);
		if (!hasSubGui()) {
			menu.mouseClicked(i, j, k);
		}
	}

	public void setBackground(String texture) {
		background = new ResourceLocation("customnpcs", "textures/gui/" + texture);
	}
}

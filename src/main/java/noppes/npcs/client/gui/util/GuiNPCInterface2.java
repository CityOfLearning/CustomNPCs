//

//

package noppes.npcs.client.gui.util;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.entity.EntityNPCInterface;

public abstract class GuiNPCInterface2 extends GuiNPCInterface {
	private ResourceLocation background;
	private GuiNpcMenu menu;

	public GuiNPCInterface2(EntityNPCInterface npc) {
		this(npc, -1);
	}

	public GuiNPCInterface2(EntityNPCInterface npc, int activeMenu) {
		super(npc);
		background = new ResourceLocation("customnpcs:textures/gui/menubg.png");
		xSize = 420;
		ySize = 200;
		menu = new GuiNpcMenu(this, activeMenu, npc);
	}

	public void delete() {
		npc.delete();
		displayGuiScreen(null);
		mc.setIngameFocus();
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		if (drawDefaultBackground) {
			drawDefaultBackground();
		}
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(background);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 200, 220);
		this.drawTexturedModalRect((guiLeft + xSize) - 230, guiTop, 26, 0, 230, 220);
		menu.drawElements(getFontRenderer(), i, j, mc, f);
		boolean bo = drawDefaultBackground;
		drawDefaultBackground = false;
		super.drawScreen(i, j, f);
		drawDefaultBackground = bo;
	}

	@Override
	public void initGui() {
		super.initGui();
		menu.initGui(guiLeft, guiTop, xSize);
	}

	@Override
	public void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		if (!hasSubGui()) {
			menu.mouseClicked(i, j, k);
		}
	}

	@Override
	public abstract void save();
}

//

//

package noppes.npcs.client.gui.player;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.client.gui.util.ITopButtonListener;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiTransportSelection extends GuiNPCInterface implements ITopButtonListener, IScrollData {
	private ResourceLocation resource;
	protected int xSize;
	protected int guiLeft;
	protected int guiTop;
	private GuiCustomScroll scroll;

	public GuiTransportSelection(EntityNPCInterface npc) {
		super(npc);
		resource = new ResourceLocation("customnpcs", "textures/gui/smallbg.png");
		xSize = 176;
		drawDefaultBackground = false;
		title = "";
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		GuiNpcButton button = (GuiNpcButton) guibutton;
		String sel = scroll.getSelected();
		if ((button.id == 0) && (sel != null)) {
			close();
			NoppesUtilPlayer.sendData(EnumPlayerPacket.Transport, sel);
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(resource);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 176, 222);
		super.drawScreen(i, j, f);
	}

	@Override
	public void initGui() {
		super.initGui();
		guiLeft = (width - xSize) / 2;
		guiTop = (height - 222) / 2;
		String name = "";
		addLabel(new GuiNpcLabel(0, name, guiLeft + ((xSize - fontRendererObj.getStringWidth(name)) / 2), guiTop + 10));
		addButton(new GuiNpcButton(0, guiLeft + 10, guiTop + 192, 156, 20,
				StatCollector.translateToLocal("transporter.travel")));
		if (scroll == null) {
			scroll = new GuiCustomScroll(this, 0);
		}
		scroll.setSize(156, 165);
		scroll.guiLeft = guiLeft + 10;
		scroll.guiTop = guiTop + 20;
		addScroll(scroll);
	}

	@Override
	public void keyTyped(char c, int i) {
		if ((i == 1) || isInventoryKey(i)) {
			close();
		}
	}

	@Override
	public void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		scroll.mouseClicked(i, j, k);
	}

	@Override
	public void save() {
	}

	@Override
	public void setData(Vector<String> list, HashMap<String, Integer> data) {
		scroll.setList(list);
	}

	@Override
	public void setSelected(String selected) {
	}
}

//

//

package noppes.npcs.client.gui.util;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.util.StatCollector;
import noppes.npcs.CustomNpcs;
import noppes.npcs.client.Client;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNpcMenu implements GuiYesNoCallback {
	private GuiScreen parent;
	private GuiMenuTopButton[] topButtons;
	private int activeMenu;
	private EntityNPCInterface npc;

	public GuiNpcMenu(final GuiScreen parent, final int activeMenu, final EntityNPCInterface npc) {
		topButtons = new GuiMenuTopButton[0];
		this.parent = parent;
		this.activeMenu = activeMenu;
		this.npc = npc;
	}

	private void close() {
		if (parent instanceof GuiContainerNPCInterface2) {
			((GuiContainerNPCInterface2) parent).close();
		}
		if (parent instanceof GuiNPCInterface2) {
			((GuiNPCInterface2) parent).close();
		}
		if (npc != null) {
			npc.reset();
			Client.sendData(EnumPacketServer.NpcMenuClose, new Object[0]);
		}
	}

	@Override
	public void confirmClicked(final boolean flag, final int i) {
		final Minecraft mc = Minecraft.getMinecraft();
		if (flag) {
			Client.sendData(EnumPacketServer.Delete, new Object[0]);
			mc.displayGuiScreen((GuiScreen) null);
			mc.setIngameFocus();
		} else {
			NoppesUtil.openGUI(mc.thePlayer, parent);
		}
	}

	public void drawElements(final FontRenderer fontRenderer, final int i, final int j, final Minecraft mc,
			final float f) {
		for (final GuiMenuTopButton button : topButtons) {
			button.drawButton(mc, i, j);
		}
	}

	public void initGui(final int guiLeft, final int guiTop, final int width) {
		Keyboard.enableRepeatEvents(true);
		final GuiMenuTopButton display = new GuiMenuTopButton(1, guiLeft + 4, guiTop - 17, "menu.display");
		final GuiMenuTopButton stats = new GuiMenuTopButton(2, display.xPosition + display.getWidth(), guiTop - 17,
				"menu.stats");
		final GuiMenuTopButton ai = new GuiMenuTopButton(6, stats.xPosition + stats.getWidth(), guiTop - 17, "menu.ai");
		final GuiMenuTopButton inv = new GuiMenuTopButton(3, ai.xPosition + ai.getWidth(), guiTop - 17,
				"menu.inventory");
		final GuiMenuTopButton advanced = new GuiMenuTopButton(4, inv.xPosition + inv.getWidth(), guiTop - 17,
				"menu.advanced");
		final GuiMenuTopButton global = new GuiMenuTopButton(5, advanced.xPosition + advanced.getWidth(), guiTop - 17,
				"menu.global");
		final GuiMenuTopButton close = new GuiMenuTopButton(0, (guiLeft + width) - 22, guiTop - 17, "X");
		final GuiMenuTopButton delete = new GuiMenuTopButton(66, (guiLeft + width) - 72, guiTop - 17,
				"selectWorld.deleteButton");
		delete.xPosition = close.xPosition - delete.getWidth();
		topButtons = new GuiMenuTopButton[] { display, stats, ai, inv, advanced, global, close, delete };
		for (final GuiMenuTopButton button : topButtons) {
			button.active = (button.id == activeMenu);
		}
	}

	public void mouseClicked(final int i, final int j, final int k) {
		if (k == 0) {
			final Minecraft mc = Minecraft.getMinecraft();
			for (final GuiMenuTopButton button : topButtons) {
				if (button.mousePressed(mc, i, j)) {
					topButtonPressed(button);
				}
			}
		}
	}

	private void save() {
		GuiNpcTextField.unfocus();
		if (parent instanceof GuiContainerNPCInterface2) {
			((GuiContainerNPCInterface2) parent).save();
		}
		if (parent instanceof GuiNPCInterface2) {
			((GuiNPCInterface2) parent).save();
		}
	}

	private void topButtonPressed(final GuiMenuTopButton button) {
		if (button.displayString.equals(activeMenu)) {
			return;
		}
		final Minecraft mc = Minecraft.getMinecraft();
		NoppesUtil.clickSound();
		final int id = button.id;
		if (id == 0) {
			close();
			return;
		}
		if (id == 66) {
			final GuiYesNo guiyesno = new GuiYesNo(this, "Confirm", StatCollector.translateToLocal("gui.delete"), 0);
			mc.displayGuiScreen(guiyesno);
			return;
		}
		save();
		if (id == 1) {
			CustomNpcs.proxy.openGui(npc, EnumGuiType.MainMenuDisplay);
		} else if (id == 2) {
			CustomNpcs.proxy.openGui(npc, EnumGuiType.MainMenuStats);
		} else if (id == 3) {
			NoppesUtil.requestOpenGUI(EnumGuiType.MainMenuInv);
		} else if (id == 4) {
			CustomNpcs.proxy.openGui(npc, EnumGuiType.MainMenuAdvanced);
		} else if (id == 5) {
			CustomNpcs.proxy.openGui(npc, EnumGuiType.MainMenuGlobal);
		} else if (id == 6) {
			CustomNpcs.proxy.openGui(npc, EnumGuiType.MainMenuAI);
		}
		activeMenu = id;
	}
}

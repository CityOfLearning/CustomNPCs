
package noppes.npcs.client.gui.player.companion;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.constants.EnumCompanionTalent;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleCompanion;

public class GuiNpcCompanionTalents extends GuiNPCInterface {
	public static class GuiTalent extends GuiScreen {
		private static ResourceLocation resource;
		static {
			resource = new ResourceLocation("customnpcs:textures/gui/talent.png");
		}
		private EnumCompanionTalent talent;
		private int x;
		private int y;

		private RoleCompanion role;

		public GuiTalent(RoleCompanion role, EnumCompanionTalent talent, int x, int y) {
			this.talent = talent;
			this.x = x;
			this.y = y;
			this.role = role;
		}

		@Override
		public void drawScreen(int i, int j, float f) {
			Minecraft mc = Minecraft.getMinecraft();
			mc.getTextureManager().bindTexture(GuiTalent.resource);
			ItemStack item = talent.item;
			if (item.getItem() == null) {
				item = new ItemStack(Blocks.dirt);
			}
			GlStateManager.pushMatrix();
			GlStateManager.color(1.0f, 1.0f, 1.0f);
			GlStateManager.enableBlend();
			boolean hover = (x < i) && ((x + 24) > i) && (y < j) && ((y + 24) > j);
			this.drawTexturedModalRect(x, y, 0, hover ? 24 : 0, 24, 24);
			zLevel = 100.0f;
			itemRender.zLevel = 100.0f;
			GlStateManager.enableLighting();
			GlStateManager.enableRescaleNormal();
			RenderHelper.enableGUIStandardItemLighting();
			itemRender.renderItemAndEffectIntoGUI(item, x + 4, y + 4);
			itemRender.renderItemOverlays(mc.fontRendererObj, item, x + 4, y + 4);
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.translate(0.0f, 0.0f, 200.0f);
			drawCenteredString(mc.fontRendererObj, role.getTalentLevel(talent) + "", x + 20, y + 16, 16777215);
			itemRender.zLevel = 0.0f;
			zLevel = 0.0f;
			GlStateManager.popMatrix();
		}
	}

	private RoleCompanion role;
	private Map<Integer, GuiTalent> talents;
	private GuiNpcButton selected;
	private long lastPressedTime;

	private long startPressedTime;

	public GuiNpcCompanionTalents(EntityNPCInterface npc) {
		super(npc);
		talents = new HashMap<Integer, GuiTalent>();
		lastPressedTime = 0L;
		startPressedTime = 0L;
		role = (RoleCompanion) npc.roleInterface;
		closeOnEsc = true;
		setBackground("companion_empty.png");
		xSize = 171;
		ySize = 166;
	}

	@Override
	public void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);
		int id = guibutton.id;
		if (id == 1) {
			CustomNpcs.proxy.openGui(npc, EnumGuiType.Companion);
		}
		if (id == 3) {
			NoppesUtilPlayer.sendData(EnumPlayerPacket.CompanionOpenInv, new Object[0]);
		}
		if (id >= 10) {
			selected = (GuiNpcButton) guibutton;
			long worldTime = mc.theWorld.getWorldTime();
			startPressedTime = worldTime;
			lastPressedTime = worldTime;
			addExperience(1);
		}
	}

	private void addExperience(int exp) {
		EnumCompanionTalent talent = talents.get(selected.id - 10).talent;
		if (!role.canAddExp(-exp) && (role.currentExp <= 0)) {
			return;
		}
		if (exp > role.currentExp) {
			exp = role.currentExp;
		}
		NoppesUtilPlayer.sendData(EnumPlayerPacket.CompanionTalentExp, talent.ordinal(), exp);
		role.talents.put(talent, role.talents.get(talent) + exp);
		role.addExp(-exp);
		getLabel(selected.id - 10).label = role.talents.get(talent) + "/" + role.getNextLevel(talent);
	}

	private void addTalent(int i, EnumCompanionTalent talent) {
		int y = guiTop + 28 + ((i / 2) * 26);
		int x = guiLeft + 4 + ((i % 2) * 84);
		GuiTalent gui = new GuiTalent(role, talent, x, y);
		gui.setWorldAndResolution(mc, width, height);
		talents.put(i, gui);
		if (role.getTalentLevel(talent) < 5) {
			addButton(new GuiNpcButton(i + 10, x + 26, y, 14, 14, "+"));
			y += 8;
		}
		addLabel(new GuiNpcLabel(i, role.talents.get(talent) + "/" + role.getNextLevel(talent), x + 26, y + 8));
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
		if ((selected != null) && ((mc.theWorld.getWorldTime() - startPressedTime) > 4L)
				&& (lastPressedTime < mc.theWorld.getWorldTime()) && ((mc.theWorld.getWorldTime() % 4L) == 0L)) {
			if (selected.mousePressed(mc, i, j) && Mouse.isButtonDown(0)) {
				lastPressedTime = mc.theWorld.getWorldTime();
				if ((lastPressedTime - startPressedTime) < 20L) {
					addExperience(1);
				} else if ((lastPressedTime - startPressedTime) < 40L) {
					addExperience(2);
				} else if ((lastPressedTime - startPressedTime) < 60L) {
					addExperience(4);
				} else if ((lastPressedTime - startPressedTime) < 90L) {
					addExperience(8);
				} else if ((lastPressedTime - startPressedTime) < 140L) {
					addExperience(14);
				} else {
					addExperience(28);
				}
			} else {
				lastPressedTime = 0L;
				selected = null;
			}
		}
		mc.getTextureManager().bindTexture(Gui.icons);
		this.drawTexturedModalRect(guiLeft + 4, guiTop + 20, 10, 64, 162, 5);
		if (role.currentExp > 0) {
			float v = (1.0f * role.currentExp) / role.getMaxExp();
			if (v > 1.0f) {
				v = 1.0f;
			}
			this.drawTexturedModalRect(guiLeft + 4, guiTop + 20, 10, 69, (int) (v * 162.0f), 5);
		}
		String s = role.currentExp + "\\" + role.getMaxExp();
		mc.fontRendererObj.drawString(s, (guiLeft + (xSize / 2)) - (mc.fontRendererObj.getStringWidth(s) / 2),
				guiTop + 10, CustomNpcResourceListener.DefaultTextColor);
		for (GuiTalent talent : talents.values()) {
			talent.drawScreen(i, j, f);
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		talents = new HashMap<Integer, GuiTalent>();
		addLabel(new GuiNpcLabel(0, NoppesStringUtils.translate("quest.exp", ": "), guiLeft + 4, guiTop + 10));
		GuiNpcCompanionStats.addTopMenu(role, this, 2);
		int i = 0;
		for (EnumCompanionTalent e : role.talents.keySet()) {
			addTalent(i++, e);
		}
	}

	@Override
	public void save() {
	}
}

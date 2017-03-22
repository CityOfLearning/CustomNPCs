
package noppes.npcs.client.gui.player.companion;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.ai.roles.RoleCompanion;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiMenuTopIconButton;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumCompanionJobs;
import noppes.npcs.constants.EnumCompanionTalent;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.NoppesStringUtils;
import noppes.npcs.util.NoppesUtilPlayer;

public class GuiNpcCompanionStats extends GuiNPCInterface implements IGuiData {
	public static void addTopMenu(RoleCompanion role, GuiScreen screen, int active) {
		if (screen instanceof GuiNPCInterface) {
			GuiNPCInterface gui = (GuiNPCInterface) screen;
			GuiMenuTopIconButton button;
			gui.addTopButton(button = new GuiMenuTopIconButton(1, gui.guiLeft + 4, gui.guiTop - 27, "menu.stats",
					new ItemStack(CustomItems.letter)));
			gui.addTopButton(
					button = new GuiMenuTopIconButton(2, button, "companion.talent", new ItemStack(CustomItems.bag)));
			if (role.hasInv()) {
				gui.addTopButton(
						button = new GuiMenuTopIconButton(3, button, "inv.inventory", new ItemStack(CustomItems.bag)));
			}
			if (role.job != EnumCompanionJobs.NONE) {
				gui.addTopButton(new GuiMenuTopIconButton(4, button, "job.name", new ItemStack(CustomItems.bag)));
			}
			gui.getTopButton(active).active = true;
		}
		if (screen instanceof GuiContainerNPCInterface) {
			GuiContainerNPCInterface gui2 = (GuiContainerNPCInterface) screen;
			GuiMenuTopIconButton button;
			gui2.addTopButton(button = new GuiMenuTopIconButton(1, gui2.guiLeft + 4, gui2.guiTop - 27, "menu.stats",
					new ItemStack(CustomItems.letter)));
			gui2.addTopButton(
					button = new GuiMenuTopIconButton(2, button, "companion.talent", new ItemStack(CustomItems.bag)));
			if (role.hasInv()) {
				gui2.addTopButton(
						button = new GuiMenuTopIconButton(3, button, "inv.inventory", new ItemStack(CustomItems.bag)));
			}
			if (role.job != EnumCompanionJobs.NONE) {
				gui2.addTopButton(new GuiMenuTopIconButton(4, button, "job.name", new ItemStack(CustomItems.bag)));
			}
			gui2.getTopButton(active).active = true;
		}
	}

	private RoleCompanion role;

	private boolean isEating;

	public GuiNpcCompanionStats(EntityNPCInterface npc) {
		super(npc);
		isEating = false;
		role = (RoleCompanion) npc.roleInterface;
		closeOnEsc = true;
		setBackground("companion.png");
		xSize = 171;
		ySize = 166;
		NoppesUtilPlayer.sendData(EnumPlayerPacket.RoleGet, new Object[0]);
	}

	@Override
	public void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);
		int id = guibutton.id;
		if (id == 2) {
			CustomNpcs.proxy.openGui(npc, EnumGuiType.CompanionTalent);
		}
		if (id == 3) {
			NoppesUtilPlayer.sendData(EnumPlayerPacket.CompanionOpenInv, new Object[0]);
		}
	}

	private int drawHealth(int y) {
		mc.getTextureManager().bindTexture(Gui.icons);
		int max = role.getTotalArmorValue();
		if (role.talents.containsKey(EnumCompanionTalent.ARMOR) || (max > 0)) {
			for (int i = 0; i < 10; ++i) {
				int x = guiLeft + 66 + (i * 10);
				if (((i * 2) + 1) < max) {
					this.drawTexturedModalRect(x, y, 34, 9, 9, 9);
				}
				if (((i * 2) + 1) == max) {
					this.drawTexturedModalRect(x, y, 25, 9, 9, 9);
				}
				if (((i * 2) + 1) > max) {
					this.drawTexturedModalRect(x, y, 16, 9, 9, 9);
				}
			}
			y += 10;
		}
		max = MathHelper.ceiling_float_int(npc.getMaxHealth());
		int k = (int) npc.getHealth();
		float scale = 1.0f;
		if (max > 40) {
			scale = max / 40.0f;
			k /= (int) scale;
			max = 40;
		}
		for (int j = 0; j < max; ++j) {
			int x2 = guiLeft + 66 + ((j % 20) * 5);
			int offset = (j / 20) * 10;
			this.drawTexturedModalRect(x2, y + offset, 52 + ((j % 2) * 5), 9, ((j % 2) == 1) ? 4 : 5, 9);
			if (k > j) {
				this.drawTexturedModalRect(x2, y + offset, 52 + ((j % 2) * 5), 0, ((j % 2) == 1) ? 4 : 5, 9);
			}
		}
		k = role.foodstats.getFoodLevel();
		y += 10;
		if (max > 20) {
			y += 10;
		}
		for (int j = 0; j < 20; ++j) {
			int x2 = guiLeft + 66 + ((j % 20) * 5);
			this.drawTexturedModalRect(x2, y, 16 + ((j % 2) * 5), 27, ((j % 2) == 1) ? 4 : 5, 9);
			if (k > j) {
				this.drawTexturedModalRect(x2, y, 52 + ((j % 2) * 5), 27, ((j % 2) == 1) ? 4 : 5, 9);
			}
		}
		return y;
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
		if (isEating && !role.isEating()) {
			NoppesUtilPlayer.sendData(EnumPlayerPacket.RoleGet, new Object[0]);
		}
		isEating = role.isEating();
		super.drawNpc(34, 150);
		drawHealth(guiTop + 88);
	}

	@Override
	public void initGui() {
		super.initGui();
		int y = guiTop + 10;
		addLabel(new GuiNpcLabel(0, NoppesStringUtils.translate("gui.name", ": ", npc.display.getName()), guiLeft + 4,
				y));
		boolean id = true;
		String translate = NoppesStringUtils.translate("companion.owner", ": ", role.ownerName);
		int x = guiLeft + 4;
		y += 12;
		addLabel(new GuiNpcLabel(id ? 1 : 0, translate, x, y));
		int id2 = 2;
		String translate2 = NoppesStringUtils.translate("companion.age", ": ", (role.ticksActive / 18000L) + " (",
				role.stage.name, ")");
		int x2 = guiLeft + 4;
		y += 12;
		addLabel(new GuiNpcLabel(id2, translate2, x2, y));
		int id3 = 3;
		String translate3 = NoppesStringUtils.translate("companion.strength", ": ", npc.stats.melee.getStrength());
		int x3 = guiLeft + 4;
		y += 12;
		addLabel(new GuiNpcLabel(id3, translate3, x3, y));
		int id4 = 4;
		String translate4 = NoppesStringUtils.translate("companion.level", ": ", role.getTotalLevel());
		int x4 = guiLeft + 4;
		y += 12;
		addLabel(new GuiNpcLabel(id4, translate4, x4, y));
		int id5 = 5;
		String translate5 = NoppesStringUtils.translate("job.name", ": ", "gui.none");
		int x5 = guiLeft + 4;
		y += 12;
		addLabel(new GuiNpcLabel(id5, translate5, x5, y));
		addTopMenu(role, this, 1);
	}

	@Override
	public void save() {
	}

	@Override
	public void setGuiData(NBTTagCompound compound) {
		role.readFromNBT(compound);
	}
}

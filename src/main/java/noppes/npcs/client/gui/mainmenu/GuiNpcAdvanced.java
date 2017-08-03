
package noppes.npcs.client.gui.mainmenu;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.advanced.GuiNPCAdvancedLinkedNpc;
import noppes.npcs.client.gui.advanced.GuiNPCDialogNpcOptions;
import noppes.npcs.client.gui.advanced.GuiNPCFactionSetup;
import noppes.npcs.client.gui.advanced.GuiNPCLinesMenu;
import noppes.npcs.client.gui.advanced.GuiNPCNightSetup;
import noppes.npcs.client.gui.advanced.GuiNPCScenes;
import noppes.npcs.client.gui.advanced.GuiNPCSoundsMenu;
import noppes.npcs.client.gui.roles.GuiJobFarmer;
import noppes.npcs.client.gui.roles.GuiNpcBard;
import noppes.npcs.client.gui.roles.GuiNpcConversation;
import noppes.npcs.client.gui.roles.GuiNpcGuard;
import noppes.npcs.client.gui.roles.GuiNpcHealer;
import noppes.npcs.client.gui.roles.GuiNpcPuppet;
import noppes.npcs.client.gui.roles.GuiNpcSpawner;
import noppes.npcs.client.gui.roles.GuiNpcTransporter;
import noppes.npcs.client.gui.roles.GuiRoleDialog;
import noppes.npcs.client.gui.util.GuiButtonBiDirectional;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.NoppesStringUtils;

public class GuiNpcAdvanced extends GuiNPCInterface2 implements IGuiData {
	private boolean hasChanges;

	public GuiNpcAdvanced(EntityNPCInterface npc) {
		super(npc, 4);
		hasChanges = false;
		Client.sendData(EnumPacketServer.MainmenuAdvancedGet, new Object[0]);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		GuiNpcButton button = (GuiNpcButton) guibutton;
		if (button.id == 3) {
			save();
			Client.sendData(EnumPacketServer.RoleGet, new Object[0]);
		}
		if (button.id == 8) {
			hasChanges = true;
			npc.advanced.setRole(button.getValue());
			getButton(3).setEnabled((npc.advanced.role != 0) && (npc.advanced.role != 5));
		}
		if (button.id == 4) {
			save();
			Client.sendData(EnumPacketServer.JobGet, new Object[0]);
		}
		if (button.id == 5) {
			hasChanges = true;
			npc.advanced.setJob(button.getValue());
			getButton(4).setEnabled((npc.advanced.job != 0) && (npc.advanced.job != 8) && (npc.advanced.job != 10));
		}
		if (button.id == 9) {
			save();
			NoppesUtil.openGUI(player, new GuiNPCFactionSetup(npc));
		}
		if (button.id == 10) {
			save();
			NoppesUtil.openGUI(player, new GuiNPCDialogNpcOptions(npc, this));
		}
		if (button.id == 11) {
			save();
			NoppesUtil.openGUI(player, new GuiNPCSoundsMenu(npc));
		}
		if (button.id == 7) {
			save();
			NoppesUtil.openGUI(player, new GuiNPCLinesMenu(npc));
		}
		if (button.id == 12) {
			save();
			NoppesUtil.openGUI(player, new GuiNPCNightSetup(npc));
		}
		if (button.id == 13) {
			save();
			NoppesUtil.openGUI(player, new GuiNPCAdvancedLinkedNpc(npc));
		}
		if (button.id == 14) {
			save();
			NoppesUtil.openGUI(player, new GuiNPCScenes(npc));
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		int y = guiTop + 8;
		addButton(new GuiNpcButton(3, guiLeft + 85 + 160, y, 52, 20, "selectServer.edit"));
		addButton(new GuiButtonBiDirectional(8, guiLeft + 85, y, 155, 20,
				new String[] { "role.none", "role.trader", "role.follower", "role.bank", "role.transporter",
						"role.mailman", NoppesStringUtils.translate("role.companion", "(WIP)"), "dialog.dialog" },
				npc.advanced.role));
		getButton(3).setEnabled((npc.advanced.role != 0) && (npc.advanced.role != 5));
		int i = 4;
		int j = guiLeft + 85 + 160;
		y += 22;
		addButton(new GuiNpcButton(i, j, y, 52, 20, "selectServer.edit"));
		addButton(new GuiButtonBiDirectional(5, guiLeft + 85, y, 155, 20,
				new String[] { "job.none", "job.bard", "job.healer", "job.guard", "job.itemgiver", "role.follower",
						"job.spawner", "job.conversation", "job.chunkloader", "job.puppet", "job.builder",
						"job.farmer" },
				npc.advanced.job));
		getButton(4).setEnabled((npc.advanced.job != 0) && (npc.advanced.job != 8) && (npc.advanced.job != 10));
		int k = 7;
		int l = guiLeft + 85;
		y += 22;
		addButton(new GuiNpcButton(k, l, y, 214, 20, "advanced.lines"));
		int m = 9;
		int j2 = guiLeft + 85;
		y += 22;
		addButton(new GuiNpcButton(m, j2, y, 214, 20, "menu.factions"));
		int i2 = 10;
		int j3 = guiLeft + 85;
		y += 22;
		addButton(new GuiNpcButton(i2, j3, y, 214, 20, "dialog.dialogs"));
		int i3 = 11;
		int j4 = guiLeft + 85;
		y += 22;
		addButton(new GuiNpcButton(i3, j4, y, 214, 20, "advanced.sounds"));
		int i4 = 12;
		int j5 = guiLeft + 85;
		y += 22;
		addButton(new GuiNpcButton(i4, j5, y, 214, 20, "advanced.night"));
		int i5 = 13;
		int j6 = guiLeft + 85;
		y += 22;
		addButton(new GuiNpcButton(i5, j6, y, 214, 20, "global.linked"));
		int i6 = 14;
		int j7 = guiLeft + 85;
		y += 22;
		addButton(new GuiNpcButton(i6, j7, y, 214, 20, "advanced.scenes"));
	}

	@Override
	public void save() {
		if (hasChanges) {
			Client.sendData(EnumPacketServer.MainmenuAdvancedSave, npc.advanced.writeToNBT(new NBTTagCompound()));
			hasChanges = false;
		}
	}

	@Override
	public void setGuiData(NBTTagCompound compound) {
		if (compound.hasKey("RoleData")) {
			if (npc.roleInterface != null) {
				npc.roleInterface.readFromNBT(compound);
			}
			if (npc.advanced.role == 1) {
				NoppesUtil.requestOpenGUI(EnumGuiType.SetupTrader);
			} else if (npc.advanced.role == 2) {
				NoppesUtil.requestOpenGUI(EnumGuiType.SetupFollower);
			} else if (npc.advanced.role == 3) {
				NoppesUtil.requestOpenGUI(EnumGuiType.SetupBank);
			} else if (npc.advanced.role == 4) {
				displayGuiScreen(new GuiNpcTransporter(npc));
			} else if (npc.advanced.role == 7) {
				NoppesUtil.openGUI(player, new GuiRoleDialog(npc));
			}
		} else if (compound.hasKey("JobData")) {
			if (npc.jobInterface != null) {
				npc.jobInterface.readFromNBT(compound);
			}
			if (npc.advanced.job == 1) {
				NoppesUtil.openGUI(player, new GuiNpcBard(npc));
			} else if (npc.advanced.job == 2) {
				NoppesUtil.openGUI(player, new GuiNpcHealer(npc));
			} else if (npc.advanced.job == 3) {
				NoppesUtil.openGUI(player, new GuiNpcGuard(npc));
			} else if (npc.advanced.job == 4) {
				NoppesUtil.requestOpenGUI(EnumGuiType.SetupItemGiver);
			} else if (npc.advanced.job == 6) {
				NoppesUtil.openGUI(player, new GuiNpcSpawner(npc));
			} else if (npc.advanced.job == 7) {
				NoppesUtil.openGUI(player, new GuiNpcConversation(npc));
			} else if (npc.advanced.job == 9) {
				NoppesUtil.openGUI(player, new GuiNpcPuppet(this, (EntityCustomNpc) npc));
			} else if (npc.advanced.job == 11) {
				NoppesUtil.openGUI(player, new GuiJobFarmer(npc));
			}
		} else {
			npc.advanced.readToNBT(compound);
			initGui();
		}
	}
}

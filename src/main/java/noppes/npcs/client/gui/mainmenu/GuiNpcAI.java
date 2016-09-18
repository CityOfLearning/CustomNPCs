
package noppes.npcs.client.gui.mainmenu;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.SubGuiNpcMovement;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcButtonYesNo;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.data.DataAI;

public class GuiNpcAI extends GuiNPCInterface2 implements ITextfieldListener, IGuiData {
	private String[] tacts;
	private DataAI ai;

	public GuiNpcAI(EntityNPCInterface npc) {
		super(npc, 6);
		tacts = new String[] { "aitactics.rush", "aitactics.stagger", "aitactics.orbit", "aitactics.hitandrun",
				"aitactics.ambush", "aitactics.stalk", "gui.none" };
		ai = npc.ai;
		Client.sendData(EnumPacketServer.MainmenuAIGet, new Object[0]);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		GuiNpcButton button = (GuiNpcButton) guibutton;
		if (button.id == 0) {
			ai.onAttack = button.getValue();
			initGui();
		} else if (button.id == 1) {
			ai.doorInteract = button.getValue();
		} else if (button.id == 2) {
			setSubGui(new SubGuiNpcMovement(ai));
		} else if (button.id == 5) {
			npc.ai.setAvoidsWater(button.getValue() == 1);
		} else if (button.id == 6) {
			ai.returnToStart = (button.getValue() == 1);
		} else if (button.id == 7) {
			ai.canSwim = (button.getValue() == 1);
		} else if (button.id == 9) {
			ai.findShelter = button.getValue();
		} else if (button.id == 10) {
			ai.directLOS = (button.getValue() == 1);
		} else if (button.id == 15) {
			ai.canLeap = (button.getValue() == 1);
		} else if (button.id == 17) {
			ai.tacticalVariant = button.getValue();
			ai.directLOS = ((button.getValue() != 5) && ai.directLOS);
			initGui();
		} else if (button.id == 23) {
			ai.attackInvisible = ((GuiNpcButtonYesNo) button).getBoolean();
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		addLabel(new GuiNpcLabel(0, "ai.enemyresponse", guiLeft + 5, guiTop + 17));
		addButton(new GuiNpcButton(0, guiLeft + 86, guiTop + 10, 60, 20,
				new String[] { "gui.retaliate", "gui.panic", "gui.retreat", "gui.nothing" }, npc.ai.onAttack));
		addLabel(new GuiNpcLabel(1, "ai.door", guiLeft + 5, guiTop + 40));
		addButton(new GuiNpcButton(1, guiLeft + 86, guiTop + 35, 60, 20,
				new String[] { "gui.break", "gui.open", "gui.disabled" }, npc.ai.doorInteract));
		addLabel(new GuiNpcLabel(12, "ai.swim", guiLeft + 5, guiTop + 65));
		addButton(new GuiNpcButton(7, guiLeft + 86, guiTop + 60, 60, 20, new String[] { "gui.no", "gui.yes" },
				npc.ai.canSwim ? 1 : 0));
		addLabel(new GuiNpcLabel(13, "ai.shelter", guiLeft + 5, guiTop + 90));
		addButton(new GuiNpcButton(9, guiLeft + 86, guiTop + 85, 60, 20,
				new String[] { "gui.darkness", "gui.sunlight", "gui.disabled" }, npc.ai.findShelter));
		addLabel(new GuiNpcLabel(14, "ai.clearlos", guiLeft + 5, guiTop + 115));
		addButton(new GuiNpcButton(10, guiLeft + 86, guiTop + 110, 60, 20, new String[] { "gui.no", "gui.yes" },
				npc.ai.directLOS ? 1 : 0));
		addButton(new GuiNpcButtonYesNo(23, guiLeft + 86, guiTop + 135, 60, 20, ai.attackInvisible));
		addLabel(new GuiNpcLabel(23, "stats.attackInvisible", guiLeft + 5, guiTop + 140));
		addLabel(new GuiNpcLabel(10, "ai.avoidwater", guiLeft + 150, guiTop + 17));
		addButton(new GuiNpcButton(5, guiLeft + 230, guiTop + 10, 60, 20, new String[] { "gui.no", "gui.yes" },
				ai.avoidsWater ? 1 : 0));
		addLabel(new GuiNpcLabel(11, "ai.return", guiLeft + 150, guiTop + 40));
		addButton(new GuiNpcButton(6, guiLeft + 230, guiTop + 35, 60, 20, new String[] { "gui.no", "gui.yes" },
				npc.ai.returnToStart ? 1 : 0));
		addLabel(new GuiNpcLabel(17, "ai.leapattarget", guiLeft + 150, guiTop + 65));
		addButton(new GuiNpcButton(15, guiLeft + 230, guiTop + 60, 60, 20, new String[] { "gui.no", "gui.yes" },
				npc.ai.canLeap ? 1 : 0));
		addLabel(new GuiNpcLabel(19, "ai.tacticalvariant", guiLeft + 150, guiTop + 140));
		addButton(new GuiNpcButton(17, guiLeft + 230, guiTop + 135, 60, 20, tacts, ai.tacticalVariant));
		if ((ai.tacticalVariant != 0) && (ai.tacticalVariant != 6)) {
			String label = "";
			switch (ai.tacticalVariant) {
			case 2: {
				label = "gui.orbitdistance";
				break;
			}
			case 3: {
				label = "gui.fightifthisclose";
				break;
			}
			case 4: {
				label = "gui.ambushdistance";
				break;
			}
			case 5: {
				label = "gui.ambushdistance";
				break;
			}
			default: {
				label = "gui.engagedistance";
				break;
			}
			}
			addLabel(new GuiNpcLabel(21, label, guiLeft + 300, guiTop + 140));
			addTextField(new GuiNpcTextField(3, this, fontRendererObj, guiLeft + 380, guiTop + 135, 30, 20,
					ai.getTacticalRange() + ""));
			getTextField(3).numbersOnly = true;
			getTextField(3).setMinMaxDefault(1, npc.stats.aggroRange, 5);
		}
		getButton(17).setEnabled(ai.onAttack == 0);
		getButton(15).setEnabled(ai.onAttack == 0);
		getButton(10).setEnabled((ai.tacticalVariant != 5) || (ai.tacticalVariant != 6));
		addLabel(new GuiNpcLabel(2, "ai.movement", guiLeft + 4, guiTop + 165));
		addButton(new GuiNpcButton(2, guiLeft + 86, guiTop + 160, 60, 20, "selectServer.edit"));
	}

	@Override
	public void save() {
		Client.sendData(EnumPacketServer.MainmenuAISave, ai.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void setGuiData(NBTTagCompound compound) {
		ai.readToNBT(compound);
		initGui();
	}

	@Override
	public void unFocused(GuiNpcTextField textfield) {
		if (textfield.id == 3) {
			ai.setTacticalRange(textfield.getInteger());
		}
	}
}

//

//

package noppes.npcs.client.gui.roles;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.player.companion.GuiNpcCompanionTalents;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcSlider;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ISliderListener;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.constants.EnumCompanionStage;
import noppes.npcs.constants.EnumCompanionTalent;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleCompanion;

public class GuiNpcCompanion extends GuiNPCInterface2 implements ITextfieldListener, ISliderListener {
	private RoleCompanion role;
	private List<GuiNpcCompanionTalents.GuiTalent> talents;

	public GuiNpcCompanion(final EntityNPCInterface npc) {
		super(npc);
		talents = new ArrayList<GuiNpcCompanionTalents.GuiTalent>();
		role = (RoleCompanion) npc.roleInterface;
	}

	@Override
	public void buttonEvent(final GuiButton guibutton) {
		if (guibutton.id == 0) {
			final GuiNpcButton button = (GuiNpcButton) guibutton;
			role.matureTo(EnumCompanionStage.values()[button.getValue()]);
			if (role.canAge) {
				role.ticksActive = role.stage.matureAge;
			}
			initGui();
		}
		if (guibutton.id == 1) {
			Client.sendData(EnumPacketServer.RoleCompanionUpdate, role.stage.ordinal());
		}
		if (guibutton.id == 2) {
			final GuiNpcButton button = (GuiNpcButton) guibutton;
			role.canAge = (button.getValue() == 1);
			initGui();
		}
	}

	@Override
	public void drawScreen(final int i, final int j, final float f) {
		super.drawScreen(i, j, f);
		for (final GuiNpcCompanionTalents.GuiTalent talent : new ArrayList<GuiNpcCompanionTalents.GuiTalent>(talents)) {
			talent.drawScreen(i, j, f);
		}
	}

	@Override
	public void elementClicked() {
	}

	@Override
	public void initGui() {
		super.initGui();
		talents = new ArrayList<GuiNpcCompanionTalents.GuiTalent>();
		int y = guiTop + 4;
		addButton(new GuiNpcButton(0, guiLeft + 70, y, 90, 20,
				new String[] { EnumCompanionStage.BABY.name, EnumCompanionStage.CHILD.name,
						EnumCompanionStage.TEEN.name, EnumCompanionStage.ADULT.name,
						EnumCompanionStage.FULLGROWN.name },
				role.stage.ordinal()));
		addLabel(new GuiNpcLabel(0, "companion.stage", guiLeft + 4, y + 5));
		addButton(new GuiNpcButton(1, guiLeft + 162, y, 90, 20, "gui.update"));
		final int i = 2;
		final int j = guiLeft + 70;
		y += 22;
		addButton(new GuiNpcButton(i, j, y, 90, 20, new String[] { "gui.no", "gui.yes" }, role.canAge ? 1 : 0));
		addLabel(new GuiNpcLabel(2, "companion.age", guiLeft + 4, y + 5));
		if (role.canAge) {
			addTextField(new GuiNpcTextField(2, this, guiLeft + 162, y, 140, 20, role.ticksActive + ""));
			getTextField(2).numbersOnly = true;
			getTextField(2).setMinMaxDefault(0, Integer.MAX_VALUE, 0);
		}
		final List<GuiNpcCompanionTalents.GuiTalent> talents = this.talents;
		final RoleCompanion role = this.role;
		final EnumCompanionTalent inventory = EnumCompanionTalent.INVENTORY;
		final int x = guiLeft + 4;
		y += 26;
		talents.add(new GuiNpcCompanionTalents.GuiTalent(role, inventory, x, y));
		addSlider(new GuiNpcSlider(this, 10, guiLeft + 30, y + 2, 100, 20,
				this.role.getExp(EnumCompanionTalent.INVENTORY) / 5000.0f));
		final List<GuiNpcCompanionTalents.GuiTalent> talents2 = this.talents;
		final RoleCompanion role2 = this.role;
		final EnumCompanionTalent armor = EnumCompanionTalent.ARMOR;
		final int x2 = guiLeft + 4;
		y += 26;
		talents2.add(new GuiNpcCompanionTalents.GuiTalent(role2, armor, x2, y));
		addSlider(new GuiNpcSlider(this, 11, guiLeft + 30, y + 2, 100, 20,
				this.role.getExp(EnumCompanionTalent.ARMOR) / 5000.0f));
		final List<GuiNpcCompanionTalents.GuiTalent> talents3 = this.talents;
		final RoleCompanion role3 = this.role;
		final EnumCompanionTalent sword = EnumCompanionTalent.SWORD;
		final int x3 = guiLeft + 4;
		y += 26;
		talents3.add(new GuiNpcCompanionTalents.GuiTalent(role3, sword, x3, y));
		addSlider(new GuiNpcSlider(this, 12, guiLeft + 30, y + 2, 100, 20,
				this.role.getExp(EnumCompanionTalent.SWORD) / 5000.0f));
		for (final GuiNpcCompanionTalents.GuiTalent gui : this.talents) {
			gui.setWorldAndResolution(mc, width, height);
		}
	}

	@Override
	public void mouseDragged(final GuiNpcSlider slider) {
		if (slider.sliderValue <= 0.0f) {
			slider.setString("gui.disabled");
			role.talents.remove(EnumCompanionTalent.values()[slider.id - 10]);
		} else {
			slider.displayString = ((int) (slider.sliderValue * 50.0f) * 100) + " exp";
			role.setExp(EnumCompanionTalent.values()[slider.id - 10], (int) (slider.sliderValue * 50.0f) * 100);
		}
	}

	@Override
	public void mousePressed(final GuiNpcSlider slider) {
	}

	@Override
	public void mouseReleased(final GuiNpcSlider slider) {
	}

	@Override
	public void save() {
		Client.sendData(EnumPacketServer.RoleSave, role.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void unFocused(final GuiNpcTextField textfield) {
		if (textfield.id == 2) {
			role.ticksActive = textfield.getInteger();
		}
	}
}

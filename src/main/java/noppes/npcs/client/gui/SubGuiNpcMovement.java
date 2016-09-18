
package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.ai.EntityAIAnimation;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.entity.data.DataAI;

public class SubGuiNpcMovement extends SubGuiInterface implements ITextfieldListener {
	private DataAI ai;

	public SubGuiNpcMovement(DataAI ai) {
		this.ai = ai;
		setBackground("menubg.png");
		xSize = 256;
		ySize = 216;
		closeOnEsc = true;
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		GuiNpcButton button = (GuiNpcButton) guibutton;
		if (button.id == 0) {
			ai.setMovingType(button.getValue());
			if (ai.getMovingType() != 0) {
				ai.animationType = 0;
				ai.setStandingType(0);
				DataAI ai = this.ai;
				DataAI ai2 = this.ai;
				DataAI ai3 = this.ai;
				float bodyOffsetX = 5.0f;
				ai3.bodyOffsetZ = bodyOffsetX;
				ai2.bodyOffsetY = bodyOffsetX;
				ai.bodyOffsetX = bodyOffsetX;
			}
			initGui();
		} else if (button.id == 3) {
			ai.animationType = button.getValue();
			initGui();
		} else if (button.id == 4) {
			ai.setStandingType(button.getValue());
			initGui();
		} else if (button.id == 5) {
			ai.npcInteracting = (button.getValue() == 1);
		} else if (button.id == 8) {
			ai.movingPattern = button.getValue();
		} else if (button.id == 9) {
			ai.movingPause = (button.getValue() == 1);
		} else if (button.id == 12) {
			if (button.getValue() == 0) {
				ai.animationType = 0;
			}
			if (button.getValue() == 1) {
				ai.animationType = 4;
			}
			if (button.getValue() == 2) {
				ai.animationType = 6;
			}
			if (button.getValue() == 3) {
				ai.animationType = 5;
			}
			if (button.getValue() == 4) {
				ai.animationType = 7;
			}
			if (button.getValue() == 5) {
				ai.animationType = 8;
			}
		} else if (button.id == 13) {
			ai.stopAndInteract = (button.getValue() == 1);
		} else if (button.id == 15) {
			ai.movementType = button.getValue();
		} else if (button.id == 66) {
			close();
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		int y = guiTop + 4;
		addLabel(new GuiNpcLabel(0, "movement.type", guiLeft + 4, y + 5));
		addButton(new GuiNpcButton(0, guiLeft + 80, y, 100, 20,
				new String[] { "ai.standing", "ai.wandering", "ai.movingpath" }, ai.getMovingType()));
		int i = 15;
		int j = guiLeft + 80;
		y += 22;
		addButton(new GuiNpcButton(i, j, y, 100, 20, new String[] { "movement.ground", "movement.flying" },
				ai.movementType));
		if (ai.getMovingType() == 1) {
			int id = 4;
			int k = guiLeft + 100;
			y += 22;
			addTextField(new GuiNpcTextField(id, this, k, y, 40, 20, ai.walkingRange + ""));
			getTextField(4).numbersOnly = true;
			getTextField(4).setMinMaxDefault(0, 1000, 10);
			addLabel(new GuiNpcLabel(4, "gui.range", guiLeft + 4, y + 5));
			int l = 5;
			int m = guiLeft + 100;
			y += 22;
			addButton(
					new GuiNpcButton(l, m, y, 50, 20, new String[] { "gui.no", "gui.yes" }, ai.npcInteracting ? 1 : 0));
			addLabel(new GuiNpcLabel(5, "movement.wanderinteract", guiLeft + 4, y + 5));
		} else if (ai.getMovingType() == 0) {
			int id2 = 7;
			int i2 = guiLeft + 99;
			y += 22;
			addTextField(new GuiNpcTextField(id2, this, i2, y, 24, 20, (int) ai.bodyOffsetX + ""));
			addLabel(new GuiNpcLabel(17, "spawner.posoffset", guiLeft + 4, y + 5));
			addLabel(new GuiNpcLabel(7, "X:", guiLeft + 115, y + 5));
			getTextField(7).numbersOnly = true;
			getTextField(7).setMinMaxDefault(0, 10, 5);
			addLabel(new GuiNpcLabel(8, "Y:", guiLeft + 125, y + 5));
			addTextField(new GuiNpcTextField(8, this, guiLeft + 135, y, 24, 20, (int) ai.bodyOffsetY + ""));
			getTextField(8).numbersOnly = true;
			getTextField(8).setMinMaxDefault(0, 10, 5);
			addLabel(new GuiNpcLabel(9, "Z:", guiLeft + 161, y + 5));
			addTextField(new GuiNpcTextField(9, this, guiLeft + 171, y, 24, 20, (int) ai.bodyOffsetZ + ""));
			getTextField(9).numbersOnly = true;
			getTextField(9).setMinMaxDefault(0, 10, 5);
			int i3 = 3;
			int j2 = guiLeft + 80;
			y += 22;
			addButton(new GuiNpcButton(i3, j2, y, 100, 20,
					new String[] { "stats.normal", "movement.sitting", "movement.lying", "movement.sneaking",
							"movement.dancing", "movement.aiming", "movement.crawling", "movement.hug" },
					ai.animationType));
			addLabel(new GuiNpcLabel(3, "movement.animation", guiLeft + 4, y + 5));
			if (ai.animationType != 2) {
				int i4 = 4;
				int j3 = guiLeft + 80;
				y += 22;
				addButton(new GuiNpcButton(i4, j3, y, 80, 20,
						new String[] { "movement.body", "movement.manual", "movement.stalking", "movement.head" },
						ai.getStandingType()));
				addLabel(new GuiNpcLabel(1, "movement.rotation", guiLeft + 4, y + 5));
			} else {
				int id3 = 5;
				int i5 = guiLeft + 99;
				y += 22;
				addTextField(new GuiNpcTextField(id3, this, i5, y, 40, 20, ai.orientation + ""));
				getTextField(5).numbersOnly = true;
				getTextField(5).setMinMaxDefault(0, 359, 0);
				addLabel(new GuiNpcLabel(6, "movement.rotation", guiLeft + 4, y + 5));
				addLabel(new GuiNpcLabel(5, "(0-359)", guiLeft + 142, y + 5));
			}
			if ((ai.getStandingType() == 1) || (ai.getStandingType() == 3)) {
				addTextField(new GuiNpcTextField(5, this, guiLeft + 165, y, 40, 20, ai.orientation + ""));
				getTextField(5).numbersOnly = true;
				getTextField(5).setMinMaxDefault(0, 359, 0);
				addLabel(new GuiNpcLabel(5, "(0-359)", guiLeft + 207, y + 5));
			}
		}
		if (ai.getMovingType() != 0) {
			int i6 = 12;
			int j4 = guiLeft + 80;
			y += 22;
			addButton(new GuiNpcButton(i6, j4, y, 100, 20,
					new String[] { "stats.normal", "movement.sneaking", "movement.aiming", "movement.dancing",
							"movement.crawling", "movement.hug" },
					EntityAIAnimation.getWalkingAnimationGuiIndex(ai.animationType)));
			addLabel(new GuiNpcLabel(12, "movement.animation", guiLeft + 4, y + 5));
		}
		if (ai.getMovingType() == 2) {
			int i7 = 8;
			int j5 = guiLeft + 80;
			y += 22;
			addButton(new GuiNpcButton(i7, j5, y, 80, 20, new String[] { "ai.looping", "ai.backtracking" },
					ai.movingPattern));
			addLabel(new GuiNpcLabel(8, "movement.name", guiLeft + 4, y + 5));
			int i8 = 9;
			int j6 = guiLeft + 80;
			y += 22;
			addButton(
					new GuiNpcButton(i8, j6, y, 80, 20, new String[] { "gui.no", "gui.yes" }, ai.movingPause ? 1 : 0));
			addLabel(new GuiNpcLabel(9, "movement.pauses", guiLeft + 4, y + 5));
		}
		int i9 = 13;
		int j7 = guiLeft + 100;
		y += 22;
		addButton(
				new GuiNpcButton(i9, j7, y, 50, 20, new String[] { "gui.no", "gui.yes" }, ai.stopAndInteract ? 1 : 0));
		addLabel(new GuiNpcLabel(13, "movement.stopinteract", guiLeft + 4, y + 5));
		int id4 = 14;
		int i10 = guiLeft + 80;
		y += 22;
		addTextField(new GuiNpcTextField(id4, this, i10, y, 50, 18, ai.getWalkingSpeed() + ""));
		getTextField(14).numbersOnly = true;
		getTextField(14).setMinMaxDefault(0, 10, 4);
		addLabel(new GuiNpcLabel(14, "stats.movespeed", guiLeft + 5, y + 5));
		addButton(new GuiNpcButton(66, guiLeft + 190, guiTop + 190, 60, 20, "gui.done"));
	}

	@Override
	public void unFocused(GuiNpcTextField textfield) {
		if (textfield.id == 7) {
			ai.bodyOffsetX = textfield.getInteger();
		} else if (textfield.id == 8) {
			ai.bodyOffsetY = textfield.getInteger();
		} else if (textfield.id == 9) {
			ai.bodyOffsetZ = textfield.getInteger();
		} else if (textfield.id == 5) {
			ai.orientation = textfield.getInteger();
		} else if (textfield.id == 4) {
			ai.walkingRange = textfield.getInteger();
		} else if (textfield.id == 14) {
			ai.setWalkingSpeed(textfield.getInteger());
		}
	}
}

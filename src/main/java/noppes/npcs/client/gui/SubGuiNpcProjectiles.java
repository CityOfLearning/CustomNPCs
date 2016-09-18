
package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.entity.data.DataRanged;

public class SubGuiNpcProjectiles extends SubGuiInterface implements ITextfieldListener {
	private DataRanged stats;
	private String[] potionNames;
	private String[] trailNames;

	public SubGuiNpcProjectiles(DataRanged stats) {
		potionNames = new String[] { "gui.none", "tile.fire.name", "potion.poison", "potion.hunger", "potion.weakness",
				"potion.moveSlowdown", "potion.confusion", "potion.blindness", "potion.wither" };
		trailNames = new String[] { "gui.none", "Smoke", "Portal", "Redstone", "Lightning", "LargeSmoke", "Magic",
				"Enchant" };
		this.stats = stats;
		setBackground("menubg.png");
		xSize = 256;
		ySize = 216;
		closeOnEsc = true;
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		GuiNpcButton button = (GuiNpcButton) guibutton;
		if (button.id == 0) {
			stats.setHasGravity(button.getValue() == 1);
			initGui();
		}
		if (button.id == 1) {
			stats.setAccelerate(button.getValue() == 1);
		}
		if (button.id == 3) {
			stats.setExplodeSize(button.getValue());
		}
		if (button.id == 4) {
			stats.setEffect(button.getValue(), stats.getEffectStrength(), stats.getEffectTime());
			initGui();
		}
		if (button.id == 5) {
			stats.setParticle(button.getValue());
		}
		if (button.id == 6) {
			stats.setGlows(button.getValue() == 1);
		}
		if (button.id == 7) {
			stats.setRender3D(button.getValue() == 1);
			initGui();
		}
		if (button.id == 8) {
			stats.setSpins(button.getValue() == 1);
		}
		if (button.id == 9) {
			stats.setSticks(button.getValue() == 1);
		}
		if (button.id == 10) {
			stats.setEffect(stats.getEffectType(), button.getValue(), stats.getEffectTime());
		}
		if (button.id == 66) {
			close();
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		addLabel(new GuiNpcLabel(1, "enchantment.arrowDamage", guiLeft + 5, guiTop + 15));
		addTextField(new GuiNpcTextField(1, this, fontRendererObj, guiLeft + 45, guiTop + 10, 50, 18,
				stats.getStrength() + ""));
		getTextField(1).numbersOnly = true;
		getTextField(1).setMinMaxDefault(0, 9999, 5);
		addLabel(new GuiNpcLabel(2, "enchantment.arrowKnockback", guiLeft + 110, guiTop + 15));
		addTextField(new GuiNpcTextField(2, this, fontRendererObj, guiLeft + 150, guiTop + 10, 50, 18,
				stats.getKnockback() + ""));
		getTextField(2).numbersOnly = true;
		getTextField(2).setMinMaxDefault(0, 3, 0);
		addLabel(new GuiNpcLabel(3, "stats.size", guiLeft + 5, guiTop + 45));
		addTextField(
				new GuiNpcTextField(3, this, fontRendererObj, guiLeft + 45, guiTop + 40, 50, 18, stats.getSize() + ""));
		getTextField(3).numbersOnly = true;
		getTextField(3).setMinMaxDefault(5, 20, 10);
		addLabel(new GuiNpcLabel(4, "stats.speed", guiLeft + 5, guiTop + 75));
		addTextField(new GuiNpcTextField(4, this, fontRendererObj, guiLeft + 45, guiTop + 70, 50, 18,
				stats.getSpeed() + ""));
		getTextField(4).numbersOnly = true;
		getTextField(4).setMinMaxDefault(1, 50, 10);
		addLabel(new GuiNpcLabel(5, "stats.hasgravity", guiLeft + 5, guiTop + 105));
		addButton(new GuiNpcButton(0, guiLeft + 60, guiTop + 100, 60, 20, new String[] { "gui.no", "gui.yes" },
				stats.getHasGravity() ? 1 : 0));
		if (!stats.getHasGravity()) {
			addButton(new GuiNpcButton(1, guiLeft + 140, guiTop + 100, 60, 20,
					new String[] { "gui.constant", "gui.accelerate" }, stats.getAccelerate() ? 1 : 0));
		}
		addLabel(new GuiNpcLabel(6, "stats.explosive", guiLeft + 5, guiTop + 135));
		addButton(new GuiNpcButton(3, guiLeft + 60, guiTop + 130, 60, 20,
				new String[] { "gui.none", "gui.small", "gui.medium", "gui.large" }, stats.getExplodeSize() % 4));
		addLabel(new GuiNpcLabel(7, "stats.rangedeffect", guiLeft + 5, guiTop + 165));
		addButton(new GuiNpcButton(4, guiLeft + 60, guiTop + 160, 60, 20, potionNames, stats.getEffectType()));
		if (stats.getEffectType() != 0) {
			addTextField(new GuiNpcTextField(5, this, fontRendererObj, guiLeft + 140, guiTop + 160, 60, 18,
					stats.getEffectTime() + ""));
			getTextField(5).numbersOnly = true;
			getTextField(5).setMinMaxDefault(1, 99999, 5);
			if (stats.getEffectType() != 1) {
				addButton(new GuiNpcButton(10, guiLeft + 210, guiTop + 160, 40, 20,
						new String[] { "stats.regular", "stats.amplified" }, stats.getEffectStrength() % 2));
			}
		}
		addLabel(new GuiNpcLabel(8, "stats.trail", guiLeft + 5, guiTop + 195));
		addButton(new GuiNpcButton(5, guiLeft + 60, guiTop + 190, 60, 20, trailNames, stats.getParticle()));
		addButton(new GuiNpcButton(7, guiLeft + 220, guiTop + 10, 30, 20, new String[] { "2D", "3D" },
				stats.getRender3D() ? 1 : 0));
		if (stats.getRender3D()) {
			addLabel(new GuiNpcLabel(10, "stats.spin", guiLeft + 160, guiTop + 45));
			addButton(new GuiNpcButton(8, guiLeft + 220, guiTop + 40, 30, 20, new String[] { "gui.no", "gui.yes" },
					stats.getSpins() ? 1 : 0));
			addLabel(new GuiNpcLabel(11, "stats.stick", guiLeft + 160, guiTop + 75));
			addButton(new GuiNpcButton(9, guiLeft + 220, guiTop + 70, 30, 20, new String[] { "gui.no", "gui.yes" },
					stats.getSticks() ? 1 : 0));
		}
		addButton(new GuiNpcButton(6, guiLeft + 140, guiTop + 190, 60, 20,
				new String[] { "stats.noglow", "stats.glows" }, stats.getGlows() ? 1 : 0));
		addButton(new GuiNpcButton(66, guiLeft + 210, guiTop + 190, 40, 20, "gui.done"));
	}

	@Override
	public void unFocused(GuiNpcTextField textfield) {
		if (textfield.id == 1) {
			stats.setStrength(textfield.getInteger());
		} else if (textfield.id == 2) {
			stats.setKnockback(textfield.getInteger());
		} else if (textfield.id == 3) {
			stats.setSize(textfield.getInteger());
		} else if (textfield.id == 4) {
			stats.setSpeed(textfield.getInteger());
		} else if (textfield.id == 5) {
			stats.setEffect(stats.getEffectType(), stats.getEffectStrength(), textfield.getInteger());
		}
	}
}

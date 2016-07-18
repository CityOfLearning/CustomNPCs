//

//

package noppes.npcs.client.gui.roles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobHealer;

public class GuiNpcHealer extends GuiNPCInterface2 {
	private JobHealer job;
	private GuiCustomScroll scroll1;
	private GuiCustomScroll scroll2;
	private HashMap<String, Integer> potions;
	private HashMap<String, String> displays;
	private int potency;

	public GuiNpcHealer(final EntityNPCInterface npc) {
		super(npc);
		potency = 0;
		job = (JobHealer) npc.jobInterface;
		potions = new HashMap<String, Integer>();
		displays = new HashMap<String, String>();
		for (int i = 0; i < Potion.potionTypes.length; ++i) {
			if (Potion.potionTypes[i] != null) {
				potions.put(Potion.potionTypes[i].getName(), Potion.potionTypes[i].getId());
			}
		}
	}

	@Override
	protected void actionPerformed(final GuiButton guibutton) {
		final GuiNpcButton button = (GuiNpcButton) guibutton;
		if (button.id == 3) {
			job.type = (byte) button.getValue();
		}
		if ((button.id == 11) && scroll1.hasSelected()) {
			job.effects.put(potions.get(scroll1.getSelected()), getTextField(2).getInteger());
			scroll1.selected = -1;
			scroll2.selected = -1;
			initGui();
		}
		if ((button.id == 12) && scroll2.hasSelected()) {
			job.effects.remove(potions.get(displays.remove(scroll2.getSelected())));
			scroll1.selected = -1;
			scroll2.selected = -1;
			initGui();
		}
		if (button.id == 13) {
			job.effects.clear();
			new ArrayList<String>();
			for (int i = 0; i < Potion.potionTypes.length; ++i) {
				if (Potion.potionTypes[i] != null) {
					final int potionID = Potion.potionTypes[i].getId();
					job.effects.put(potionID, potency);
				}
			}
			scroll1.selected = -1;
			scroll2.selected = -1;
			initGui();
		}
		if (button.id == 14) {
			job.effects.clear();
			displays.clear();
			scroll1.selected = -1;
			scroll2.selected = -1;
			initGui();
		}
	}

	@Override
	public void elementClicked() {
	}

	@Override
	public void initGui() {
		super.initGui();
		addLabel(new GuiNpcLabel(1, "beacon.range", guiLeft + 10, guiTop + 9));
		addTextField(new GuiNpcTextField(1, this, fontRendererObj, guiLeft + 80, guiTop + 4, 40, 20, job.range + ""));
		getTextField(1).numbersOnly = true;
		getTextField(1).setMinMaxDefault(1, 64, 16);
		addLabel(new GuiNpcLabel(4, "stats.speed", guiLeft + 140, guiTop + 9));
		addTextField(new GuiNpcTextField(4, this, fontRendererObj, guiLeft + 220, guiTop + 4, 40, 20, potency + ""));
		getTextField(4).numbersOnly = true;
		getTextField(4).setMinMaxDefault(10, Integer.MAX_VALUE, 20);
		addLabel(new GuiNpcLabel(3, "beacon.affect", guiLeft + 10, guiTop + 31));
		addButton(new GuiNpcButton(3, guiLeft + 56, guiTop + 26, 80, 20,
				new String[] { "faction.friendly", "faction.unfriendly", "spawner.all" }, job.type));
		addLabel(new GuiNpcLabel(2, "beacon.potency", guiLeft + 140, guiTop + 31));
		addTextField(new GuiNpcTextField(2, this, fontRendererObj, guiLeft + 220, guiTop + 26, 40, 20, potency + ""));
		getTextField(2).numbersOnly = true;
		getTextField(2).setMinMaxDefault(0, 3, 0);
		if (scroll1 == null) {
			(scroll1 = new GuiCustomScroll(this, 0)).setSize(175, 154);
		}
		scroll1.guiLeft = guiLeft + 4;
		scroll1.guiTop = guiTop + 58;
		addScroll(scroll1);
		addLabel(new GuiNpcLabel(11, "beacon.availableEffects", guiLeft + 4, guiTop + 48));
		if (scroll2 == null) {
			(scroll2 = new GuiCustomScroll(this, 1)).setSize(175, 154);
		}
		scroll2.guiLeft = guiLeft + 235;
		scroll2.guiTop = guiTop + 58;
		addScroll(scroll2);
		addLabel(new GuiNpcLabel(12, "beacon.currentEffects", guiLeft + 235, guiTop + 48));
		final List<String> all = new ArrayList<String>();
		for (final String names : potions.keySet()) {
			if (!job.effects.containsKey(potions.get(names))) {
				all.add(names);
			} else {
				displays.put(
						I18n.format(names, new Object[0]) + " " + I18n.format(
								"enchantment.level." + (job.effects.get(potions.get(names)) + 1), new Object[0]),
						names);
			}
		}
		scroll1.setList(all);
		final List<String> applied = new ArrayList<String>(displays.keySet());
		scroll2.setList(applied);
		addButton(new GuiNpcButton(11, guiLeft + 180, guiTop + 80, 55, 20, ">"));
		addButton(new GuiNpcButton(12, guiLeft + 180, guiTop + 102, 55, 20, "<"));
		addButton(new GuiNpcButton(13, guiLeft + 180, guiTop + 130, 55, 20, ">>"));
		addButton(new GuiNpcButton(14, guiLeft + 180, guiTop + 152, 55, 20, "<<"));
	}

	@Override
	public void save() {
		job.range = getTextField(1).getInteger();
		potency = getTextField(2).getInteger();
		job.speed = getTextField(4).getInteger();
		Client.sendData(EnumPacketServer.JobSave, job.writeToNBT(new NBTTagCompound()));
	}
}

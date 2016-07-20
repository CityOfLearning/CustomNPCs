//

//

package noppes.npcs.client.gui.questtypes;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.controllers.quest.Quest;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.quests.QuestKill;

public class GuiNpcQuestTypeKill extends SubGuiInterface implements ITextfieldListener, ICustomScrollListener {
	private GuiCustomScroll scroll;
	private QuestKill quest;
	private GuiNpcTextField lastSelected;

	public GuiNpcQuestTypeKill(EntityNPCInterface npc, Quest q, GuiScreen parent) {
		this.npc = npc;
		title = "Quest Kill Setup";
		quest = (QuestKill) q.questInterface;
		setBackground("menubg.png");
		xSize = 356;
		ySize = 216;
		closeOnEsc = true;
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);
		if (guibutton.id == 0) {
			close();
		}
	}

	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {
		if (lastSelected == null) {
			return;
		}
		lastSelected.setText(guiCustomScroll.getSelected());
		saveTargets();
	}

	@Override
	public void initGui() {
		super.initGui();
		int i = 0;
		addLabel(new GuiNpcLabel(0, "You can fill in npc or player names too", guiLeft + 4, guiTop + 50));
		for (String name : quest.targets.keySet()) {
			addTextField(
					new GuiNpcTextField(i, this, fontRendererObj, guiLeft + 4, guiTop + 70 + (i * 22), 180, 20, name));
			addTextField(new GuiNpcTextField(i + 3, this, fontRendererObj, guiLeft + 186, guiTop + 70 + (i * 22), 24,
					20, quest.targets.get(name) + ""));
			getTextField(i + 3).numbersOnly = true;
			getTextField(i + 3).setMinMaxDefault(1, Integer.MAX_VALUE, 1);
			++i;
		}
		while (i < 3) {
			addTextField(
					new GuiNpcTextField(i, this, fontRendererObj, guiLeft + 4, guiTop + 70 + (i * 22), 180, 20, ""));
			addTextField(new GuiNpcTextField(i + 3, this, fontRendererObj, guiLeft + 186, guiTop + 70 + (i * 22), 24,
					20, "1"));
			getTextField(i + 3).numbersOnly = true;
			getTextField(i + 3).setMinMaxDefault(1, Integer.MAX_VALUE, 1);
			++i;
		}
		Map<?, ?> data = EntityList.stringToClassMapping;
		ArrayList<String> list = new ArrayList<String>();
		for (Object name2 : data.keySet()) {
			Class<?> c = (Class<?>) data.get(name2);
			try {
				if (!EntityLivingBase.class.isAssignableFrom(c) || EntityNPCInterface.class.isAssignableFrom(c)
						|| (c.getConstructor(World.class) == null) || Modifier.isAbstract(c.getModifiers())) {
					continue;
				}
				list.add(name2.toString());
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException ex) {
			}
		}
		if (scroll == null) {
			scroll = new GuiCustomScroll(this, 0);
		}
		scroll.setList(list);
		scroll.setSize(130, 198);
		scroll.guiLeft = guiLeft + 220;
		scroll.guiTop = guiTop + 14;
		addScroll(scroll);
		addButton(new GuiNpcButton(0, guiLeft + 4, guiTop + 140, 98, 20, "gui.back"));
		scroll.visible = GuiNpcTextField.isActive();
	}

	@Override
	public void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		scroll.visible = GuiNpcTextField.isActive();
	}

	@Override
	public void save() {
	}

	private void saveTargets() {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < 3; ++i) {
			String name = getTextField(i).getText();
			if (!name.isEmpty()) {
				map.put(name, getTextField(i + 3).getInteger());
			}
		}
		quest.targets = map;
	}

	@Override
	public void unFocused(GuiNpcTextField guiNpcTextField) {
		if (guiNpcTextField.id < 3) {
			lastSelected = guiNpcTextField;
		}
		saveTargets();
	}
}

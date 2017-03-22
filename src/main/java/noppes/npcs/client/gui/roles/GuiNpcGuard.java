
package noppes.npcs.client.gui.roles;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.ai.jobs.JobGuard;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNpcGuard extends GuiNPCInterface2 {
	private JobGuard role;
	private GuiCustomScroll scroll1;
	private GuiCustomScroll scroll2;

	public GuiNpcGuard(EntityNPCInterface npc) {
		super(npc);
		role = (JobGuard) npc.jobInterface;
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		GuiNpcButton button = (GuiNpcButton) guibutton;
		if (button.id == 0) {
			for (Object entity : EntityList.stringToClassMapping.keySet()) {
				String name = "entity." + entity + ".name";
				Class cl = EntityList.stringToClassMapping.get(entity);
				if (EntityAnimal.class.isAssignableFrom(cl) && !role.targets.contains(name)) {
					role.targets.add(name);
				}
			}
			scroll1.selected = -1;
			scroll2.selected = -1;
			initGui();
		}
		if (button.id == 1) {
			for (Object entity : EntityList.stringToClassMapping.keySet()) {
				String name = "entity." + entity + ".name";
				Class cl = EntityList.stringToClassMapping.get(entity);
				if (EntityMob.class.isAssignableFrom(cl) && !EntityCreeper.class.isAssignableFrom(cl)
						&& !role.targets.contains(name)) {
					role.targets.add(name);
				}
			}
			scroll1.selected = -1;
			scroll2.selected = -1;
			initGui();
		}
		if (button.id == 2) {
			for (Object entity : EntityList.stringToClassMapping.keySet()) {
				String name = "entity." + entity + ".name";
				Class cl = EntityList.stringToClassMapping.get(entity);
				if (EntityCreeper.class.isAssignableFrom(cl) && !role.targets.contains(name)) {
					role.targets.add(name);
				}
			}
			scroll1.selected = -1;
			scroll2.selected = -1;
			initGui();
		}
		if ((button.id == 11) && scroll1.hasSelected()) {
			role.targets.add(scroll1.getSelected());
			scroll1.selected = -1;
			scroll2.selected = -1;
			initGui();
		}
		if ((button.id == 12) && scroll2.hasSelected()) {
			role.targets.remove(scroll2.getSelected());
			scroll2.selected = -1;
			initGui();
		}
		if (button.id == 13) {
			role.targets.clear();
			List<String> all = new ArrayList<>();
			for (Object entity2 : EntityList.stringToClassMapping.keySet()) {
				String name2 = "entity." + entity2 + ".name";
				Class cl2 = EntityList.stringToClassMapping.get(entity2);
				if (EntityLivingBase.class.isAssignableFrom(cl2) && !EntityNPCInterface.class.isAssignableFrom(cl2)) {
					all.add(name2);
				}
			}
			role.targets = all;
			scroll1.selected = -1;
			scroll2.selected = -1;
			initGui();
		}
		if (button.id == 14) {
			role.targets.clear();
			scroll1.selected = -1;
			scroll2.selected = -1;
			initGui();
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		addButton(new GuiNpcButton(0, guiLeft + 10, guiTop + 4, 100, 20, "guard.animals"));
		addButton(new GuiNpcButton(1, guiLeft + 140, guiTop + 4, 100, 20, "guard.mobs"));
		addButton(new GuiNpcButton(2, guiLeft + 275, guiTop + 4, 100, 20, "guard.creepers"));
		if (scroll1 == null) {
			(scroll1 = new GuiCustomScroll(this, 0)).setSize(175, 154);
		}
		scroll1.guiLeft = guiLeft + 4;
		scroll1.guiTop = guiTop + 58;
		addScroll(scroll1);
		addLabel(new GuiNpcLabel(11, "guard.availableTargets", guiLeft + 4, guiTop + 48));
		if (scroll2 == null) {
			(scroll2 = new GuiCustomScroll(this, 1)).setSize(175, 154);
		}
		scroll2.guiLeft = guiLeft + 235;
		scroll2.guiTop = guiTop + 58;
		addScroll(scroll2);
		addLabel(new GuiNpcLabel(12, "guard.currentTargets", guiLeft + 235, guiTop + 48));
		List<String> all = new ArrayList<>();
		for (Object entity : EntityList.stringToClassMapping.keySet()) {
			String name = "entity." + entity + ".name";
			Class cl = EntityList.stringToClassMapping.get(entity);
			if (!role.targets.contains(name)) {
				if (EntityNPCInterface.class.isAssignableFrom(cl)) {
					continue;
				}
				if (!EntityLivingBase.class.isAssignableFrom(cl)) {
					continue;
				}
				all.add(name);
			}
		}
		scroll1.setList(all);
		scroll2.setList(role.targets);
		addButton(new GuiNpcButton(11, guiLeft + 180, guiTop + 80, 55, 20, ">"));
		addButton(new GuiNpcButton(12, guiLeft + 180, guiTop + 102, 55, 20, "<"));
		addButton(new GuiNpcButton(13, guiLeft + 180, guiTop + 130, 55, 20, ">>"));
		addButton(new GuiNpcButton(14, guiLeft + 180, guiTop + 152, 55, 20, "<<"));
	}

	@Override
	public void save() {
		Client.sendData(EnumPacketServer.JobSave, role.writeToNBT(new NBTTagCompound()));
	}
}

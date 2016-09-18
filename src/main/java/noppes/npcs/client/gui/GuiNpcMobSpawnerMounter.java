
package noppes.npcs.client.gui;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import noppes.npcs.client.Client;
import noppes.npcs.client.controllers.ClientCloneController;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiMenuSideButton;
import noppes.npcs.client.gui.util.GuiMenuTopButton;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPacketServer;

public class GuiNpcMobSpawnerMounter extends GuiNPCInterface implements IGuiData {
	private static int showingClones;
	private static String search;
	static {
		GuiNpcMobSpawnerMounter.showingClones = 0;
		GuiNpcMobSpawnerMounter.search = "";
	}
	private GuiCustomScroll scroll;
	private int posX;
	private int posY;
	private int posZ;
	private List<String> list;

	private int activeTab;

	public GuiNpcMobSpawnerMounter(int i, int j, int k) {
		activeTab = 1;
		xSize = 256;
		posX = i;
		posY = j;
		posZ = k;
		closeOnEsc = true;
		setBackground("menubg.png");
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		int id = guibutton.id;
		if (id == 0) {
			close();
		}
		if (id == 1) {
			NBTTagCompound compound = getCompound();
			if (compound != null) {
				compound.setTag("Pos", newDoubleNBTList(posX + 0.5, posY + 1, posZ + 0.5));
				Client.sendData(EnumPacketServer.SpawnRider, compound);
				close();
			}
		}
		if (id == 2) {
			Client.sendData(EnumPacketServer.PlayerRider, new Object[0]);
			close();
		}
		if (id == 3) {
			GuiNpcMobSpawnerMounter.showingClones = 0;
			initGui();
		}
		if (id == 4) {
			GuiNpcMobSpawnerMounter.showingClones = 1;
			initGui();
		}
		if (id == 5) {
			GuiNpcMobSpawnerMounter.showingClones = 2;
			initGui();
		}
		if (id > 20) {
			activeTab = id - 20;
			initGui();
		}
	}

	private NBTTagCompound getCompound() {
		String sel = scroll.getSelected();
		if (sel == null) {
			return null;
		}
		if (GuiNpcMobSpawnerMounter.showingClones == 0) {
			return ClientCloneController.Instance.getCloneData(player, sel, activeTab);
		}
		Entity entity = EntityList.createEntityByName(sel, Minecraft.getMinecraft().theWorld);
		if (entity == null) {
			return null;
		}
		NBTTagCompound compound = new NBTTagCompound();
		entity.writeToNBTOptional(compound);
		return compound;
	}

	private List<String> getSearchList() {
		if (GuiNpcMobSpawnerMounter.search.isEmpty()) {
			return new ArrayList<String>(list);
		}
		List<String> list = new ArrayList<String>();
		for (String name : this.list) {
			if (name.toLowerCase().contains(GuiNpcMobSpawnerMounter.search)) {
				list.add(name);
			}
		}
		return list;
	}

	@Override
	public void initGui() {
		super.initGui();
		guiTop += 10;
		if (scroll == null) {
			(scroll = new GuiCustomScroll(this, 0)).setSize(165, 188);
		} else {
			scroll.clear();
		}
		scroll.guiLeft = guiLeft + 4;
		scroll.guiTop = guiTop + 26;
		addScroll(scroll);
		addTextField(new GuiNpcTextField(1, this, fontRendererObj, guiLeft + 4, guiTop + 4, 165, 20,
				GuiNpcMobSpawnerMounter.search));
		GuiMenuTopButton button;
		addTopButton(button = new GuiMenuTopButton(3, guiLeft + 4, guiTop - 17, "spawner.clones"));
		button.active = (GuiNpcMobSpawnerMounter.showingClones == 0);
		addTopButton(button = new GuiMenuTopButton(4, button, "spawner.entities"));
		button.active = (GuiNpcMobSpawnerMounter.showingClones == 1);
		addTopButton(button = new GuiMenuTopButton(5, button, "gui.server"));
		button.active = (GuiNpcMobSpawnerMounter.showingClones == 2);
		addButton(new GuiNpcButton(1, guiLeft + 170, guiTop + 6, 82, 20, "spawner.mount"));
		addButton(new GuiNpcButton(2, guiLeft + 170, guiTop + 50, 82, 20, "spawner.mountplayer"));
		if ((GuiNpcMobSpawnerMounter.showingClones == 0) || (GuiNpcMobSpawnerMounter.showingClones == 2)) {
			addSideButton(new GuiMenuSideButton(21, guiLeft - 69, guiTop + 2, 70, 22, "Tab 1"));
			addSideButton(new GuiMenuSideButton(22, guiLeft - 69, guiTop + 23, 70, 22, "Tab 2"));
			addSideButton(new GuiMenuSideButton(23, guiLeft - 69, guiTop + 44, 70, 22, "Tab 3"));
			addSideButton(new GuiMenuSideButton(24, guiLeft - 69, guiTop + 65, 70, 22, "Tab 4"));
			addSideButton(new GuiMenuSideButton(25, guiLeft - 69, guiTop + 86, 70, 22, "Tab 5"));
			addSideButton(new GuiMenuSideButton(26, guiLeft - 69, guiTop + 107, 70, 22, "Tab 6"));
			addSideButton(new GuiMenuSideButton(27, guiLeft - 69, guiTop + 128, 70, 22, "Tab 7"));
			addSideButton(new GuiMenuSideButton(28, guiLeft - 69, guiTop + 149, 70, 22, "Tab 8"));
			addSideButton(new GuiMenuSideButton(29, guiLeft - 69, guiTop + 170, 70, 22, "Tab 9"));
			getSideButton(20 + activeTab).active = true;
			showClones();
		} else {
			showEntities();
		}
	}

	@Override
	public void keyTyped(char c, int i) {
		super.keyTyped(c, i);
		if (GuiNpcMobSpawnerMounter.search.equals(getTextField(1).getText())) {
			return;
		}
		GuiNpcMobSpawnerMounter.search = getTextField(1).getText().toLowerCase();
		scroll.setList(getSearchList());
	}

	protected NBTTagList newDoubleNBTList(double... par1ArrayOfDouble) {
		NBTTagList nbttaglist = new NBTTagList();
		for (double d1 : par1ArrayOfDouble) {
			nbttaglist.appendTag(new NBTTagDouble(d1));
		}
		return nbttaglist;
	}

	@Override
	public void save() {
	}

	@Override
	public void setGuiData(NBTTagCompound compound) {
		NBTTagList nbtlist = compound.getTagList("List", 8);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < nbtlist.tagCount(); ++i) {
			list.add(nbtlist.getStringTagAt(i));
		}
		this.list = list;
		scroll.setList(getSearchList());
	}

	private void showClones() {
		if (GuiNpcMobSpawnerMounter.showingClones == 2) {
			Client.sendData(EnumPacketServer.CloneList, activeTab);
			return;
		}
		new ArrayList<String>();
		list = ClientCloneController.Instance.getClones(activeTab);
		scroll.setList(getSearchList());
	}

	private void showEntities() {
		Map<?, ?> data = EntityList.stringToClassMapping;
		ArrayList<String> list = new ArrayList<String>();
		for (Object name : data.keySet()) {
			Class<?> c = (Class<?>) data.get(name);
			try {
				if (!EntityLiving.class.isAssignableFrom(c) || (c.getConstructor(World.class) == null)
						|| Modifier.isAbstract(c.getModifiers())) {
					continue;
				}
				list.add(name.toString());
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException ex) {
			}
		}
		this.list = list;
		scroll.setList(getSearchList());
	}
}

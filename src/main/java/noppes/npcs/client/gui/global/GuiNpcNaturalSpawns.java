//

//

package noppes.npcs.client.gui.global;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.GuiNpcMobSpawnerSelector;
import noppes.npcs.client.gui.SubGuiNpcBiomes;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcSlider;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.client.gui.util.ISliderListener;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.controllers.SpawnData;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNpcNaturalSpawns extends GuiNPCInterface2
		implements IGuiData, IScrollData, ITextfieldListener, ICustomScrollListener, ISliderListener {
	private GuiCustomScroll scroll;
	private HashMap<String, Integer> data;
	private SpawnData spawn;

	public GuiNpcNaturalSpawns(final EntityNPCInterface npc) {
		super(npc);
		data = new HashMap<String, Integer>();
		spawn = new SpawnData();
		Client.sendData(EnumPacketServer.NaturalSpawnGetAll, new Object[0]);
	}

	@Override
	public void buttonEvent(final GuiButton guibutton) {
		final int id = guibutton.id;
		if (id == 1) {
			save();
			String name;
			for (name = "New"; data.containsKey(name); name += "_") {
			}
			final SpawnData spawn = new SpawnData();
			spawn.name = name;
			Client.sendData(EnumPacketServer.NaturalSpawnSave, spawn.writeNBT(new NBTTagCompound()));
		}
		if ((id == 2) && data.containsKey(scroll.getSelected())) {
			Client.sendData(EnumPacketServer.NaturalSpawnRemove, spawn.id);
			spawn = new SpawnData();
			scroll.clear();
		}
		if (id == 3) {
			setSubGui(new SubGuiNpcBiomes(spawn));
		}
		if (id == 5) {
			setSubGui(new GuiNpcMobSpawnerSelector());
		}
		if (id == 25) {
			spawn.compound1 = new NBTTagCompound();
			initGui();
		}
		if (id == 27) {
			spawn.type = ((GuiNpcButton) guibutton).getValue();
		}
	}

	@Override
	public void closeSubGui(final SubGuiInterface gui) {
		super.closeSubGui(gui);
		if (gui instanceof GuiNpcMobSpawnerSelector) {
			final GuiNpcMobSpawnerSelector selector = (GuiNpcMobSpawnerSelector) gui;
			final NBTTagCompound compound = selector.getCompound();
			if (compound != null) {
				spawn.compound1 = compound;
			}
			initGui();
		}
	}

	@Override
	public void customScrollClicked(final int i, final int j, final int k, final GuiCustomScroll guiCustomScroll) {
		if (guiCustomScroll.id == 0) {
			save();
			final String selected = scroll.getSelected();
			spawn = new SpawnData();
			Client.sendData(EnumPacketServer.NaturalSpawnGet, data.get(selected));
		}
	}

	private String getTitle(final NBTTagCompound compound) {
		if ((compound != null) && compound.hasKey("ClonedName")) {
			return compound.getString("ClonedName");
		}
		return "gui.selectnpc";
	}

	@Override
	public void initGui() {
		super.initGui();
		if (scroll == null) {
			(scroll = new GuiCustomScroll(this, 0)).setSize(143, 208);
		}
		scroll.guiLeft = guiLeft + 214;
		scroll.guiTop = guiTop + 4;
		addScroll(scroll);
		addButton(new GuiNpcButton(1, guiLeft + 358, guiTop + 38, 58, 20, "gui.add"));
		addButton(new GuiNpcButton(2, guiLeft + 358, guiTop + 61, 58, 20, "gui.remove"));
		if (spawn.id >= 0) {
			showSpawn();
		}
	}

	@Override
	public void mouseDragged(final GuiNpcSlider guiNpcSlider) {
		guiNpcSlider.displayString = StatCollector.translateToLocal("spawning.weightedChance") + ": "
				+ (int) (guiNpcSlider.sliderValue * 100.0f);
	}

	@Override
	public void mousePressed(final GuiNpcSlider guiNpcSlider) {
	}

	@Override
	public void mouseReleased(final GuiNpcSlider guiNpcSlider) {
		spawn.itemWeight = (int) (guiNpcSlider.sliderValue * 100.0f);
	}

	@Override
	public void save() {
		GuiNpcTextField.unfocus();
		if (spawn.id >= 0) {
			Client.sendData(EnumPacketServer.NaturalSpawnSave, spawn.writeNBT(new NBTTagCompound()));
		}
	}

	@Override
	public void setData(final Vector<String> list, final HashMap<String, Integer> data) {
		final String name = scroll.getSelected();
		this.data = data;
		scroll.setList(list);
		if (name != null) {
			scroll.setSelected(name);
		}
		initGui();
	}

	@Override
	public void setGuiData(final NBTTagCompound compound) {
		spawn.readNBT(compound);
		setSelected(spawn.name);
		initGui();
	}

	@Override
	public void setSelected(final String selected) {
	}

	private void showSpawn() {
		addLabel(new GuiNpcLabel(1, "gui.title", guiLeft + 4, guiTop + 8));
		addTextField(new GuiNpcTextField(1, this, fontRendererObj, guiLeft + 60, guiTop + 3, 140, 20, spawn.name));
		addLabel(new GuiNpcLabel(3, "spawning.biomes", guiLeft + 4, guiTop + 30));
		addButton(new GuiNpcButton(3, guiLeft + 120, guiTop + 25, 50, 20, "selectServer.edit"));
		addSlider(new GuiNpcSlider(this, 4, guiLeft + 4, guiTop + 47, 180, 20, spawn.itemWeight / 100.0f));
		final int y = guiTop + 70;
		addButton(new GuiNpcButton(25, guiLeft + 14, y, 20, 20, "X"));
		addLabel(new GuiNpcLabel(5, "1:", guiLeft + 4, y + 5));
		addButton(new GuiNpcButton(5, guiLeft + 36, y, 170, 20, getTitle(spawn.compound1)));
		addLabel(new GuiNpcLabel(26, "spawn.type", guiLeft + 4, guiTop + 100));
		addButton(new GuiNpcButton(27, guiLeft + 70, guiTop + 93, 120, 20,
				new String[] { "spawn.allday", "spawn.dark" }, spawn.type));
	}

	@Override
	public void unFocused(final GuiNpcTextField guiNpcTextField) {
		final String name = guiNpcTextField.getText();
		if (name.isEmpty() || data.containsKey(name)) {
			guiNpcTextField.setText(spawn.name);
		} else {
			final String old = spawn.name;
			data.remove(old);
			spawn.name = name;
			data.put(spawn.name, spawn.id);
			scroll.replace(old, spawn.name);
		}
	}
}

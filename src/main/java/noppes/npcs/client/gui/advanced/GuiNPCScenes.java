//

//

package noppes.npcs.client.gui.advanced;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.ClientProxy;
import noppes.npcs.client.gui.SubGuiNpcTextArea;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.data.DataScenes;

public class GuiNPCScenes extends GuiNPCInterface2 {
	private DataScenes scenes;
	private DataScenes.SceneContainer scene;

	public GuiNPCScenes(EntityNPCInterface npc) {
		super(npc);
		scenes = npc.advanced.scenes;
	}

	@Override
	public void buttonEvent(GuiButton button) {
		if (button.id < 60) {
			DataScenes.SceneContainer scene = scenes.scenes.get(button.id / 10);
			if ((button.id % 10) == 1) {
				scene.enabled = (((GuiNpcButton) button).getValue() == 1);
			}
			if ((button.id % 10) == 2) {
				this.scene = scene;
				setSubGui(new SubGuiNpcTextArea(scene.lines));
			}
			if ((button.id % 10) == 3) {
				scenes.scenes.remove(scene);
				initGui();
			}
			if ((button.id % 10) == 4) {
				scene.btn = ((GuiNpcButton) button).getValue();
				initGui();
			}
		}
		if (button.id == 101) {
			scenes.addScene(getTextField(101).getText());
			initGui();
		}
	}

	@Override
	public void closeSubGui(SubGuiInterface gui) {
		super.closeSubGui(gui);
		if (gui instanceof SubGuiNpcTextArea) {
			scene.lines = ((SubGuiNpcTextArea) gui).text;
			scene = null;
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		addLabel(new GuiNpcLabel(102, "gui.button", guiLeft + 236, guiTop + 4));
		int y = guiTop + 14;
		for (int i = 0; i < scenes.scenes.size(); ++i) {
			DataScenes.SceneContainer scene = scenes.scenes.get(i);
			addLabel(new GuiNpcLabel(0 + (i * 10), scene.name, guiLeft + 10, y + 5));
			addButton(new GuiNpcButton(1 + (i * 10), guiLeft + 120, y, 60, 20,
					new String[] { "gui.disabled", "gui.enabled" }, scene.enabled ? 1 : 0));
			addButton(new GuiNpcButton(2 + (i * 10), guiLeft + 181, y, 50, 20, "Edit"));
			addButton(new GuiNpcButton(3 + (i * 10), guiLeft + 293, y, 50, 20, "X"));
			addButton(new GuiNpcButton(4 + (i * 10), guiLeft + 232, y, 60, 20,
					new String[] { "gui.none", GameSettings.getKeyDisplayString(ClientProxy.Scene1.getKeyCode()),
							GameSettings.getKeyDisplayString(ClientProxy.Scene2.getKeyCode()),
							GameSettings.getKeyDisplayString(ClientProxy.Scene3.getKeyCode()) },
					scene.btn));
			y += 22;
		}
		if (scenes.scenes.size() < 6) {
			addTextField(
					new GuiNpcTextField(101, this, guiLeft + 4, 160, 190, 20, "Scene" + (scenes.scenes.size() + 1)));
			addButton(new GuiNpcButton(101, guiLeft + 204, 160, 60, 20, "gui.add"));
		}
	}

	@Override
	public void save() {
		Client.sendData(EnumPacketServer.MainmenuAdvancedSave, npc.advanced.writeToNBT(new NBTTagCompound()));
	}
}


package noppes.npcs.client.gui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiMenuTopButton;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextArea;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.IJTextAreaListener;
import noppes.npcs.controllers.script.IScriptHandler;
import noppes.npcs.controllers.script.ScriptContainer;
import noppes.npcs.controllers.script.ScriptController;
import noppes.npcs.util.NoppesStringUtils;

public class GuiScriptInterface extends GuiNPCInterface implements IGuiData, GuiYesNoCallback, IJTextAreaListener {
	private int activeTab;
	public IScriptHandler handler;
	public Map<String, List<String>> languages;

	public GuiScriptInterface() {
		activeTab = 0;
		languages = new HashMap<>();
		drawDefaultBackground = true;
		closeOnEsc = true;
		xSize = 420;
		setBackground("menubg.png");
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if ((guibutton.id >= 0) && (guibutton.id < 12)) {
			setScript();
			activeTab = guibutton.id;
			initGui();
		}
		if (guibutton.id == 12) {
			handler.getScripts().add(new ScriptContainer(handler));
			activeTab = handler.getScripts().size();
			initGui();
		}
		if (guibutton.id == 109) {
			displayGuiScreen(new GuiConfirmOpenLink(this, "http://www.kodevelopment.nl/minecraft/customnpcs/scripting",
					0, true));
		}
		if (guibutton.id == 110) {
			displayGuiScreen(new GuiConfirmOpenLink(this, "http://www.kodevelopment.nl/customnpcs/api/", 1, true));
		}
		if (guibutton.id == 111) {
			displayGuiScreen(new GuiConfirmOpenLink(this, "https://github.com/Noppes/CustomNPCsAPI", 2, true));
		}
		if (guibutton.id == 112) {
			displayGuiScreen(new GuiConfirmOpenLink(this,
					"http://www.minecraftforge.net/forum/index.php/board,122.0.html", 3, true));
		}
		if (guibutton.id == 100) {
			NoppesStringUtils.setClipboardContents(getTextField(2).getText());
		}
		if (guibutton.id == 101) {
			getTextField(2).setText(NoppesStringUtils.getClipboardContents());
		}
		if (guibutton.id == 102) {
			if (activeTab > 0) {
				ScriptContainer container = handler.getScripts().get(activeTab - 1);
				container.script = "";
			} else {
				for (ScriptContainer script : handler.getScripts()) {
					script.console.clear();
				}
			}
			initGui();
		}
		if (guibutton.id == 103) {
			handler.setLanguage(((GuiNpcButton) guibutton).displayString);
		}
		if (guibutton.id == 104) {
			handler.setEnabled(((GuiNpcButton) guibutton).getValue() == 1);
		}
		if (guibutton.id == 105) {
			GuiYesNo guiyesno = new GuiYesNo(this, "Confirm", StatCollector.translateToLocal("gui.delete"), 10);
			displayGuiScreen(guiyesno);
		}
		if (guibutton.id == 106) {
			NoppesUtil.openFolder(ScriptController.Instance.dir);
		}
		if (guibutton.id == 107) {
			ScriptContainer container = handler.getScripts().get(activeTab - 1);
			if (container == null) {
				handler.getScripts().add(container = new ScriptContainer(handler));
			}
			setSubGui(new GuiScriptList(languages.get(handler.getLanguage()), container));
		}
	}

	@Override
	public void confirmClicked(boolean flag, int i) {
		if (flag) {
			if (i == 0) {
				openLink("http://www.kodevelopment.nl/minecraft/customnpcs/scripting");
			}
			if (i == 1) {
				openLink("http://www.kodevelopment.nl/customnpcs/api/");
			}
			if (i == 2) {
				openLink("http://www.kodevelopment.nl/minecraft/customnpcs/scripting");
			}
			if (i == 3) {
				openLink("http://www.minecraftforge.net/forum/index.php/board,122.0.html");
			}
			if (i == 10) {
				handler.getScripts().remove(activeTab - 1);
				activeTab = 0;
			}
		}
		displayGuiScreen(this);
	}

	private String getConsoleText() {
		Map<Long, String> map = new TreeMap<>();
		int tab = 0;
		for (ScriptContainer script : handler.getScripts()) {
			++tab;
			for (Map.Entry<Long, String> entry : script.console.entrySet()) {
				map.put(entry.getKey(), " tab " + tab + ":\n" + entry.getValue());
			}
		}
		String console = "";
		for (Map.Entry<Long, String> entry2 : map.entrySet()) {
			console = new Date(entry2.getKey()) + entry2.getValue() + "\n" + console;
		}
		return console;
	}

	private int getScriptIndex() {
		int i = 0;
		for (String language : languages.keySet()) {
			if (language.equalsIgnoreCase(handler.getLanguage())) {
				return i;
			}
			++i;
		}
		return 0;
	}

	@Override
	public void initGui() {
		xSize = (int) (width * 0.88);
		ySize = (int) (xSize * 0.56);
		bgScale = xSize / 400.0f;
		super.initGui();
		guiTop += 10;
		int yoffset = (int) (ySize * 0.02);
		GuiMenuTopButton top;
		addTopButton(top = new GuiMenuTopButton(0, guiLeft + 4, guiTop - 17, "gui.settings"));
		for (int i = 0; i < handler.getScripts().size(); ++i) {
			handler.getScripts().get(i);
			addTopButton(top = new GuiMenuTopButton(i + 1, top, i + 1 + ""));
		}
		if (handler.getScripts().size() < 8) {
			addTopButton(top = new GuiMenuTopButton(12, top, "+"));
		}
		top = getTopButton(activeTab);
		if (top == null) {
			activeTab = 0;
			top = getTopButton(0);
		}
		top.active = true;
		if (activeTab > 0) {
			ScriptContainer container = handler.getScripts().get(activeTab - 1);
			addTextField(new GuiNpcTextArea(2, this, guiLeft + 1 + yoffset, guiTop + yoffset, xSize - 108 - yoffset,
					(int) (ySize * 0.96) - (yoffset * 2), (container == null) ? "" : container.script));
			int left = (guiLeft + xSize) - 104;
			addButton(new GuiNpcButton(102, left, guiTop + yoffset, 60, 20, "gui.clear"));
			addButton(new GuiNpcButton(101, left + 61, guiTop + yoffset, 60, 20, "gui.paste"));
			addButton(new GuiNpcButton(100, left, guiTop + 21 + yoffset, 60, 20, "gui.copy"));
			addButton(new GuiNpcButton(105, left + 61, guiTop + 21 + yoffset, 60, 20, "gui.remove"));
			addButton(new GuiNpcButton(107, left, guiTop + 66 + yoffset, 80, 20, "script.loadscript"));
			GuiCustomScroll scroll = new GuiCustomScroll(this, 0).setUnselectable();
			scroll.setSize(100, (int) (ySize * 0.54) - (yoffset * 2));
			scroll.guiLeft = left;
			scroll.guiTop = guiTop + 88 + yoffset;
			if (container != null) {
				scroll.setList(container.scripts);
			}
			addScroll(scroll);
		} else {
			addTextField(new GuiNpcTextArea(2, this, guiLeft + 4 + yoffset, guiTop + 6 + yoffset, xSize - 160 - yoffset,
					(int) (ySize * 0.92f) - (yoffset * 2), getConsoleText()));
			getTextField(2).canEdit = false;
			int left2 = (guiLeft + xSize) - 150;
			addButton(new GuiNpcButton(100, left2, guiTop + 125, 60, 20, "gui.copy"));
			addButton(new GuiNpcButton(102, left2, guiTop + 146, 60, 20, "gui.clear"));
			addLabel(new GuiNpcLabel(1, "script.language", left2, guiTop + 15));
			addButton(new GuiNpcButton(103, left2 + 60, guiTop + 10, 80, 20,
					languages.keySet().toArray(new String[languages.keySet().size()]), getScriptIndex()));
			getButton(103).enabled = (languages.size() > 0);
			addLabel(new GuiNpcLabel(2, "gui.enabled", left2, guiTop + 36));
			addButton(new GuiNpcButton(104, left2 + 60, guiTop + 31, 50, 20, new String[] { "gui.no", "gui.yes" },
					handler.getEnabled() ? 1 : 0));
			if (MinecraftServer.getServer() != null) {
				addButton(new GuiNpcButton(106, left2, guiTop + 55, 150, 20, "script.openfolder"));
			}
			addButton(new GuiNpcButton(109, left2, guiTop + 78, 80, 20, "gui.website"));
			addButton(new GuiNpcButton(112, left2 + 81, guiTop + 78, 80, 20, "gui.forum"));
			addButton(new GuiNpcButton(110, left2, guiTop + 99, 80, 20, "script.apidoc"));
			addButton(new GuiNpcButton(111, left2 + 81, guiTop + 99, 80, 20, "script.apisrc"));
		}
		xSize = 420;
		ySize = 256;
	}

	@Override
	public void save() {
		setScript();
	}

	@Override
	public void saveText(String text) {
		ScriptContainer container = handler.getScripts().get(activeTab - 1);
		if (container != null) {
			container.script = text;
		}
		initGui();
	}

	@Override
	public void setGuiData(NBTTagCompound compound) {
		NBTTagList data = compound.getTagList("Languages", 10);
		Map<String, List<String>> languages = new HashMap<>();
		for (int i = 0; i < data.tagCount(); ++i) {
			NBTTagCompound comp = data.getCompoundTagAt(i);
			List<String> scripts = new ArrayList<>();
			NBTTagList list = comp.getTagList("Scripts", 8);
			for (int j = 0; j < list.tagCount(); ++j) {
				scripts.add(list.getStringTagAt(j));
			}
			languages.put(comp.getString("Language"), scripts);
		}
		this.languages = languages;
		initGui();
	}

	private void setScript() {
		if (activeTab > 0) {
			ScriptContainer container = handler.getScripts().get(activeTab - 1);
			if (container == null) {
				handler.getScripts().add(container = new ScriptContainer(handler));
			}
			String text = getTextField(2).getText();
			text = text.replace("\r\n", "\n");
			text = text.replace("\r", "\n");
			container.script = text;
		}
	}
}

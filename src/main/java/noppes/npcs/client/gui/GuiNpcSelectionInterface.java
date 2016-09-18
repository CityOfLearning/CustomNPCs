
package noppes.npcs.client.gui;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import noppes.npcs.client.AssetsBrowser;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNPCStringSlot;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.entity.EntityNPCInterface;

public abstract class GuiNpcSelectionInterface extends GuiNPCInterface {
	public GuiNPCStringSlot slot;
	public GuiScreen parent;
	private String up;
	private String root;
	public AssetsBrowser assets;
	private HashSet<String> dataFolder;
	protected HashSet<String> dataTextures;

	public GuiNpcSelectionInterface(EntityNPCInterface npc, GuiScreen parent, String sound) {
		super(npc);
		up = "..<" + StatCollector.translateToLocal("gui.up") + ">..";
		root = "";
		dataFolder = new HashSet<String>();
		dataTextures = new HashSet<String>();
		root = AssetsBrowser.getRoot(sound);
		assets = new AssetsBrowser(root, getExtension());
		drawDefaultBackground = false;
		title = "";
		this.parent = parent;
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		int id = guibutton.id;
		if (id == 2) {
			close();
			NoppesUtil.openGUI(player, parent);
		}
		if (id == 3) {
			root = root.substring(0, root.lastIndexOf("/"));
			assets = new AssetsBrowser(root, getExtension());
			initGui();
		}
	}

	@Override
	public void doubleClicked() {
		if (slot.selected.equals(up)) {
			root = root.substring(0, root.lastIndexOf("/"));
			assets = new AssetsBrowser(root, getExtension());
			initGui();
		} else if (dataFolder.contains(slot.selected)) {
			root += slot.selected;
			assets = new AssetsBrowser(root, getExtension());
			initGui();
		} else {
			close();
			NoppesUtil.openGUI(player, parent);
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		slot.drawScreen(i, j, f);
		super.drawScreen(i, j, f);
	}

	@Override
	public void elementClicked() {
		if ((slot.selected != null) && dataTextures.contains(slot.selected)) {
			if (parent instanceof GuiNPCInterface) {
				((GuiNPCInterface) parent).elementClicked();
			} else if (parent instanceof GuiNPCInterface2) {
				((GuiNPCInterface2) parent).elementClicked();
			}
		}
	}

	public abstract String[] getExtension();

	public String getSelected() {
		return assets.getAsset(slot.selected);
	}

	@Override
	public void handleMouseInput() throws IOException {
		slot.handleMouseInput();
		super.handleMouseInput();
	}

	@Override
	public void initGui() {
		super.initGui();
		dataFolder.clear();
		String ss = "Current Folder: /assets" + root;
		addLabel(new GuiNpcLabel(0, ss, (width / 2) - (fontRendererObj.getStringWidth(ss) / 2), 20, 16777215));
		Vector<String> list = new Vector<String>();
		if (!assets.isRoot) {
			list.add(up);
		}
		for (String folder : assets.folders) {
			list.add("/" + folder);
			dataFolder.add("/" + folder);
		}
		for (String texture : assets.files) {
			list.add(texture);
			dataTextures.add(texture);
		}
		Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
		(slot = new GuiNPCStringSlot(list, this, false, 18)).registerScrollButtons(4, 5);
		addButton(new GuiNpcButton(2, (width / 2) - 100, height - 44, 98, 20, "gui.back"));
		addButton(new GuiNpcButton(3, (width / 2) + 2, height - 44, 98, 20, "gui.up"));
		getButton(3).enabled = !assets.isRoot;
	}

	@Override
	public void save() {
	}
}

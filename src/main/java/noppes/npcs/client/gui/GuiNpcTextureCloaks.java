
package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiScreen;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNpcTextureCloaks extends GuiNpcSelectionInterface {
	public GuiNpcTextureCloaks(EntityNPCInterface npc, GuiScreen parent) {
		super(npc, parent,
				npc.display.getCapeTexture().isEmpty() ? "customnpcs:textures/cloak/" : npc.display.getCapeTexture());
		title = "Select Cloak";
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		int l = -50;
		int i2 = (height / 2) + 30;
		this.drawNpc(npc, l, i2, 2.0f, 180);
		super.drawScreen(i, j, f);
	}

	@Override
	public void elementClicked() {
		if (dataTextures.contains(slot.selected) && (slot.selected != null)) {
			npc.display.setCapeTexture(assets.getAsset(slot.selected));
		}
	}

	@Override
	public String[] getExtension() {
		return new String[] { "png" };
	}

	@Override
	public void initGui() {
		super.initGui();
		int index = npc.display.getCapeTexture().lastIndexOf("/");
		if (index > 0) {
			String asset = npc.display.getCapeTexture().substring(index + 1);
			if (npc.display.getCapeTexture().equals(assets.getAsset(asset))) {
				slot.selected = asset;
			}
		}
	}

	@Override
	public void save() {
	}
}

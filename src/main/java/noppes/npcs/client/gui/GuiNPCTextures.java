
package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiScreen;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCTextures extends GuiNpcSelectionInterface {
	public GuiNPCTextures(EntityNPCInterface npc, GuiScreen parent) {
		super(npc, parent, npc.display.getSkinTexture());
		title = "Select Texture";
		this.parent = parent;
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		int l = -50;
		int i2 = (height / 2) + 30;
		this.drawNpc(npc, l, i2, 2.0f, 0);
		super.drawScreen(i, j, f);
	}

	@Override
	public void elementClicked() {
		if (dataTextures.contains(slot.selected) && (slot.selected != null)) {
			npc.display.setSkinTexture(assets.getAsset(slot.selected));
			npc.textureLocation = null;
		}
	}

	@Override
	public String[] getExtension() {
		return new String[] { "png" };
	}

	@Override
	public void initGui() {
		super.initGui();
		int index = npc.display.getSkinTexture().lastIndexOf("/");
		if (index > 0) {
			String asset = npc.display.getSkinTexture().substring(index + 1);
			if (npc.display.getSkinTexture().equals(assets.getAsset(asset))) {
				slot.selected = asset;
			}
		}
	}

	@Override
	public void save() {
	}
}

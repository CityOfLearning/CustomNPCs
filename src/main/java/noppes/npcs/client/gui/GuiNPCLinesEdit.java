//

//

package noppes.npcs.client.gui;

import java.util.HashMap;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.LogWriter;
import noppes.npcs.client.Client;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.controllers.Line;
import noppes.npcs.controllers.Lines;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCLinesEdit extends GuiNPCInterface2 implements IGuiData {
	private Lines lines;
	private GuiNpcTextField field;
	private GuiNpcSoundSelection gui;
	private final int guiLines = 7;

	public GuiNPCLinesEdit(final EntityNPCInterface npc, final Lines lines) {
		super(npc);
		this.lines = lines;
		for(Line line : lines.lines.values()){
			LogWriter.info("Lines Edited: " + line.text);
		}
		Client.sendData(EnumPacketServer.MainmenuAdvancedGet, new Object[0]);
	}

	@Override
	protected void actionPerformed(final GuiButton guibutton) {
		if(guibutton.id == 99){
			saveLines();
			Client.sendData(EnumPacketServer.MainmenuAdvancedSave, npc.advanced.writeToNBT(new NBTTagCompound()));
		} else {
			final GuiNpcButton button = (GuiNpcButton) guibutton;
			field = getTextField(button.id + guiLines);
			NoppesUtil.openGUI(player, gui = new GuiNpcSoundSelection(this, field.getText()));
		}
	}

	@Override
	public void elementClicked() {
		field.setText(gui.getSelected());
	}

	@Override
	public void initGui() {
		super.initGui();
		//There needs to be a dedicated save dialog button... come now
		for (int i = 0; i < guiLines; ++i) {
			String text = "";
			String sound = "";
			if (lines.lines.containsKey(i)) {
				final Line line = lines.lines.get(i);
				text = line.text;
				sound = line.sound;
			}
			addTextField(
					new GuiNpcTextField(i, this, fontRendererObj, guiLeft + 4, guiTop + 4 + (i * 24), 200, 20, text));
			addTextField(new GuiNpcTextField(i + guiLines, this, fontRendererObj, guiLeft + 208, guiTop + 4 + (i * 24), 146,
					20, sound));
			addButton(new GuiNpcButton(i, guiLeft + 358, guiTop + 4 + (i * 24), 60, 20, "mco.template.button.select"));
		}
		addButton(new GuiNpcButton(99, guiLeft + 338, guiTop + 4 + (guiLines * 24), 60, 20, "Save Lines"));
	}

	@Override
	public void save() {
	//this is a terrible idea... a global save when the page changes?
	}

	private void saveLines() {
		final HashMap<Integer, Line> lines = new HashMap<Integer, Line>();
		for (int i = 0; i < guiLines; ++i) {
			final GuiNpcTextField tf = getTextField(i);
			final GuiNpcTextField tf2 = getTextField(i + guiLines);
			if (!tf.isEmpty()) {
				final Line line = new Line();
				line.text = tf.getText();
				line.sound = tf2.getText();
				lines.put(i, line);
			}
		}
		this.lines.lines = lines;
	}

	@Override
	public void setGuiData(final NBTTagCompound compound) {
		npc.advanced.readToNBT(compound);
		initGui();
	}
}

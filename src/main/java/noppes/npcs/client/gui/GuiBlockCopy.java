//

//

package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import noppes.npcs.blocks.tiles.TileCopy;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.constants.EnumPacketServer;

public class GuiBlockCopy extends GuiNPCInterface implements IGuiData, ITextfieldListener {
	private TileCopy tile;

	public GuiBlockCopy(final int x, final int y, final int z) {
		setBackground("menubg.png");
		xSize = 256;
		ySize = 216;
		closeOnEsc = true;
		tile = (TileCopy) player.worldObj.getTileEntity(new BlockPos(x, y, z));
		Client.sendData(EnumPacketServer.GetTileEntity, x, y, z);
	}

	@Override
	protected void actionPerformed(final GuiButton guibutton) {
		if (guibutton.id == 0) {
			final NBTTagCompound compound = new NBTTagCompound();
			tile.writeToNBT(compound);
			Client.sendData(EnumPacketServer.SchematicStore, getTextField(5).getText(), compound);
		}
		close();
	}

	@Override
	public void initGui() {
		super.initGui();
		int y = guiTop + 4;
		addTextField(new GuiNpcTextField(0, this, guiLeft + 104, y, 50, 20, tile.height + ""));
		addLabel(new GuiNpcLabel(0, "schematic.height", guiLeft + 5, y + 5));
		getTextField(0).numbersOnly = true;
		getTextField(0).setMinMaxDefault(0, 100, 10);
		final boolean id = true;
		final int i = guiLeft + 104;
		y += 23;
		addTextField(new GuiNpcTextField(id ? 1 : 0, this, i, y, 50, 20, tile.width + ""));
		addLabel(new GuiNpcLabel(1, "schematic.width", guiLeft + 5, y + 5));
		getTextField(1).numbersOnly = true;
		getTextField(1).setMinMaxDefault(0, 100, 10);
		final int id2 = 2;
		final int j = guiLeft + 104;
		y += 23;
		addTextField(new GuiNpcTextField(id2, this, j, y, 50, 20, tile.length + ""));
		addLabel(new GuiNpcLabel(2, "schematic.length", guiLeft + 5, y + 5));
		getTextField(2).numbersOnly = true;
		getTextField(2).setMinMaxDefault(0, 100, 10);
		final int id3 = 5;
		final int k = guiLeft + 104;
		y += 23;
		addTextField(new GuiNpcTextField(id3, this, k, y, 100, 20, ""));
		addLabel(new GuiNpcLabel(5, "gui.name", guiLeft + 5, y + 5));
		final boolean l = false;
		final int m = guiLeft + 5;
		y += 30;
		addButton(new GuiNpcButton(l ? 1 : 0, m, y, 60, 20, "gui.save"));
		addButton(new GuiNpcButton(1, guiLeft + 67, y, 60, 20, "gui.cancel"));
	}

	@Override
	public void initPacket() {
	}

	@Override
	public void save() {
		final NBTTagCompound compound = new NBTTagCompound();
		tile.writeToNBT(compound);
		Client.sendData(EnumPacketServer.SaveTileEntity, compound);
	}

	@Override
	public void setGuiData(final NBTTagCompound compound) {
		tile.readFromNBT(compound);
		initGui();
	}

	@Override
	public void unFocused(final GuiNpcTextField textfield) {
		if (textfield.id == 0) {
			tile.height = (short) textfield.getInteger();
		}
		if (textfield.id == 1) {
			tile.width = (short) textfield.getInteger();
		}
		if (textfield.id == 2) {
			tile.length = (short) textfield.getInteger();
		}
	}
}

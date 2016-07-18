//

//

package noppes.npcs.client.gui;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import noppes.npcs.Schematic;
import noppes.npcs.blocks.tiles.TileBuilder;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcButtonYesNo;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketServer;

public class GuiBlockBuilder extends GuiNPCInterface implements IGuiData, ICustomScrollListener, IScrollData {
	private int x;
	private int y;
	private int z;
	private TileBuilder tile;
	private GuiCustomScroll scroll;
	private Schematic selected;

	public GuiBlockBuilder(final int x, final int y, final int z) {
		selected = null;
		this.x = x;
		this.y = y;
		this.z = z;
		setBackground("menubg.png");
		xSize = 256;
		ySize = 216;
		closeOnEsc = true;
		tile = (TileBuilder) player.worldObj.getTileEntity(new BlockPos(x, y, z));
	}

	@Override
	protected void actionPerformed(final GuiButton guibutton) {
		if (guibutton.id == 3) {
			final GuiNpcButtonYesNo button = (GuiNpcButtonYesNo) guibutton;
			if (button.getBoolean()) {
				TileBuilder.SetDrawPos(new BlockPos(x, y, z));
				tile.setDrawSchematic(selected);
			} else {
				TileBuilder.SetDrawPos(null);
				tile.setDrawSchematic(null);
			}
		}
		if (guibutton.id == 4) {
			tile.enabled = ((GuiNpcButtonYesNo) guibutton).getBoolean();
		}
		if (guibutton.id == 5) {
			tile.rotation = ((GuiNpcButton) guibutton).getValue();
		}
		if (guibutton.id == 6) {
			setSubGui(new SubGuiNpcAvailability(tile.availability));
		}
		if (guibutton.id == 7) {
			tile.finished = ((GuiNpcButtonYesNo) guibutton).getBoolean();
			Client.sendData(EnumPacketServer.SchematicsSet, x, y, z, scroll.getSelected());
		}
		if (guibutton.id == 8) {
			tile.started = ((GuiNpcButtonYesNo) guibutton).getBoolean();
		}
	}

	@Override
	public void customScrollClicked(final int i, final int j, final int k, final GuiCustomScroll scroll) {
		if (!scroll.hasSelected()) {
			return;
		}
		if (selected != null) {
			getButton(3).setDisplay(0);
		}
		TileBuilder.SetDrawPos(null);
		tile.setDrawSchematic(null);
		Client.sendData(EnumPacketServer.SchematicsSet, x, y, z, scroll.getSelected());
	}

	@Override
	public void initGui() {
		super.initGui();
		if (scroll == null) {
			(scroll = new GuiCustomScroll(this, 0)).setSize(125, 208);
		}
		scroll.guiLeft = guiLeft + 4;
		scroll.guiTop = guiTop + 4;
		addScroll(scroll);
		if (selected != null) {
			int y = guiTop + 4;
			if (selected.size < 125000) {
				addButton(new GuiNpcButtonYesNo(3, guiLeft + 200, y,
						(TileBuilder.DrawPos != null) && tile.getPos().equals(TileBuilder.DrawPos)));
				addLabel(new GuiNpcLabel(3, "schematic.preview", guiLeft + 130, y + 5));
			}
			final boolean id = false;
			final String string = StatCollector.translateToLocal("schematic.width") + ": " + selected.width;
			final int x = guiLeft + 130;
			y += 22;
			addLabel(new GuiNpcLabel(id ? 1 : 0, string, x, y));
			final boolean id2 = true;
			final String string2 = StatCollector.translateToLocal("schematic.length") + ": " + selected.length;
			final int x2 = guiLeft + 130;
			y += 12;
			addLabel(new GuiNpcLabel(id2 ? 1 : 0, string2, x2, y));
			final int id3 = 2;
			final String string3 = StatCollector.translateToLocal("schematic.height") + ": " + selected.height;
			final int x3 = guiLeft + 130;
			y += 12;
			addLabel(new GuiNpcLabel(id3, string3, x3, y));
			final int id4 = 4;
			final int x4 = guiLeft + 200;
			y += 16;
			addButton(new GuiNpcButtonYesNo(id4, x4, y, tile.enabled));
			addLabel(new GuiNpcLabel(4, StatCollector.translateToLocal("gui.enabled"), guiLeft + 130, y + 5));
			final int id5 = 7;
			final int x5 = guiLeft + 200;
			y += 23;
			addButton(new GuiNpcButtonYesNo(id5, x5, y, tile.finished));
			addLabel(new GuiNpcLabel(7, StatCollector.translateToLocal("gui.finished"), guiLeft + 130, y + 5));
			final int id6 = 8;
			final int x6 = guiLeft + 200;
			y += 23;
			addButton(new GuiNpcButtonYesNo(id6, x6, y, tile.started));
			addLabel(new GuiNpcLabel(8, StatCollector.translateToLocal("gui.started"), guiLeft + 130, y + 5));
			final int id7 = 9;
			final int i = guiLeft + 200;
			y += 23;
			addTextField(new GuiNpcTextField(id7, this, i, y, 50, 20, tile.yOffest + ""));
			addLabel(new GuiNpcLabel(9, StatCollector.translateToLocal("gui.yoffset"), guiLeft + 130, y + 5));
			getTextField(9).numbersOnly = true;
			getTextField(9).setMinMaxDefault(-10, 10, 0);
			final int j = 5;
			final int k = guiLeft + 200;
			y += 23;
			addButton(new GuiNpcButton(j, k, y, 50, 20, new String[] { "0", "90", "180", "270" }, tile.rotation));
			addLabel(new GuiNpcLabel(5, StatCollector.translateToLocal("movement.rotation"), guiLeft + 130, y + 5));
			final int l = 6;
			final int m = guiLeft + 130;
			y += 22;
			addButton(new GuiNpcButton(l, m, y, 120, 20, "availability.options"));
		}
	}

	@Override
	public void initPacket() {
		Client.sendData(EnumPacketServer.SchematicsTile, x, y, z);
	}

	@Override
	public void save() {
		if (getTextField(9) != null) {
			tile.yOffest = getTextField(9).getInteger();
		}
		Client.sendData(EnumPacketServer.SchematicsTileSave, x, y, z, tile.writePartNBT(new NBTTagCompound()));
	}

	@Override
	public void setData(final Vector<String> list, final HashMap<String, Integer> data) {
		scroll.setList(list);
		if (selected != null) {
			scroll.setSelected(selected.name);
		}
		initGui();
	}

	@Override
	public void setGuiData(final NBTTagCompound compound) {
		if (compound.hasKey("Width")) {
			(selected = new Schematic(compound.getString("SchematicName"))).load(compound);
			if ((TileBuilder.DrawPos != null) && TileBuilder.DrawPos.equals(tile.getPos())) {
				tile.setDrawSchematic(selected);
			}
			scroll.setSelected(selected.name);
			scroll.scrollTo(selected.name);
		} else {
			tile.readPartNBT(compound);
		}
		initGui();
	}

	@Override
	public void setSelected(final String selected) {
	}
}

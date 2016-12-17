
package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import noppes.npcs.blocks.tiles.TileRedstoneBlock;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPacketServer;

public class GuiNpcRedstoneBlock extends GuiNPCInterface implements IGuiData {
	private TileRedstoneBlock tile;

	public GuiNpcRedstoneBlock(int x, int y, int z) {
		tile = (TileRedstoneBlock) player.worldObj.getTileEntity(new BlockPos(x, y, z));
		Client.sendData(EnumPacketServer.GetTileEntity, x, y, z);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		int id = guibutton.id;
		if (id == 0) {
			close();
		}
		if (id == 1) {
			tile.setDetailed((((GuiNpcButton) guibutton).getValue() == 1));
			initGui();
		}
		if (id == 4) {
			save();
			setSubGui(new SubGuiNpcAvailability(tile.getAvailability()));
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		addButton(new GuiNpcButton(4, guiLeft + 40, guiTop + 20, 120, 20, "availability.options"));
		addLabel(new GuiNpcLabel(11, "gui.detailed", guiLeft + 40, guiTop + 47, 16777215));
		addButton(new GuiNpcButton(1, guiLeft + 110, guiTop + 42, 50, 20, new String[] { "gui.no", "gui.yes" },
				tile.isDetailed() ? 1 : 0));
		if (tile.isDetailed()) {
			addLabel(new GuiNpcLabel(0, StatCollector.translateToLocal("bard.ondistance") + " X:", guiLeft + 1,
					guiTop + 76, 16777215));
			addTextField(new GuiNpcTextField(0, this, fontRendererObj, guiLeft + 80, guiTop + 71, 30, 20,
					tile.getOnRangeX() + ""));
			getTextField(0).numbersOnly = true;
			getTextField(0).setMinMaxDefault(0, 50, 6);
			addLabel(new GuiNpcLabel(1, "Y:", guiLeft + 113, guiTop + 76, 16777215));
			addTextField(new GuiNpcTextField(1, this, fontRendererObj, guiLeft + 122, guiTop + 71, 30, 20,
					tile.getOnRangeY() + ""));
			getTextField(1).numbersOnly = true;
			getTextField(1).setMinMaxDefault(0, 50, 6);
			addLabel(new GuiNpcLabel(2, "Z:", guiLeft + 155, guiTop + 76, 16777215));
			addTextField(new GuiNpcTextField(2, this, fontRendererObj, guiLeft + 164, guiTop + 71, 30, 20,
					tile.getOnRangeZ() + ""));
			getTextField(2).numbersOnly = true;
			getTextField(2).setMinMaxDefault(0, 50, 6);
			addLabel(new GuiNpcLabel(3, StatCollector.translateToLocal("bard.offdistance") + " X:", guiLeft - 3,
					guiTop + 99, 16777215));
			addTextField(new GuiNpcTextField(3, this, fontRendererObj, guiLeft + 80, guiTop + 94, 30, 20,
					tile.getOffRangeX() + ""));
			getTextField(3).numbersOnly = true;
			getTextField(3).setMinMaxDefault(0, 50, 10);
			addLabel(new GuiNpcLabel(4, "Y:", guiLeft + 113, guiTop + 99, 16777215));
			addTextField(new GuiNpcTextField(4, this, fontRendererObj, guiLeft + 122, guiTop + 94, 30, 20,
					tile.getOffRangeY() + ""));
			getTextField(4).numbersOnly = true;
			getTextField(4).setMinMaxDefault(0, 50, 10);
			addLabel(new GuiNpcLabel(5, "Z:", guiLeft + 155, guiTop + 99, 16777215));
			addTextField(new GuiNpcTextField(5, this, fontRendererObj, guiLeft + 164, guiTop + 94, 30, 20,
					tile.getOffRangeZ() + ""));
			getTextField(5).numbersOnly = true;
			getTextField(5).setMinMaxDefault(0, 50, 10);
		} else {
			addLabel(new GuiNpcLabel(0, "bard.ondistance", guiLeft + 1, guiTop + 76, 16777215));
			addTextField(new GuiNpcTextField(0, this, fontRendererObj, guiLeft + 80, guiTop + 71, 30, 20,
					tile.getOnRange() + ""));
			getTextField(0).numbersOnly = true;
			getTextField(0).setMinMaxDefault(0, 50, 6);
			addLabel(new GuiNpcLabel(3, "bard.offdistance", guiLeft - 3, guiTop + 99, 16777215));
			addTextField(new GuiNpcTextField(3, this, fontRendererObj, guiLeft + 80, guiTop + 94, 30, 20,
					tile.getOffRange() + ""));
			getTextField(3).numbersOnly = true;
			getTextField(3).setMinMaxDefault(0, 50, 10);
		}
		addButton(new GuiNpcButton(0, guiLeft + 40, guiTop + 190, 120, 20, "Done"));
	}

	@Override
	public void save() {
		if (tile == null) {
			return;
		}
		if (tile.isDetailed()) {
			tile.setOnRangeX(getTextField(0).getInteger());
			tile.setOnRangeY(getTextField(1).getInteger());
			tile.setOnRangeZ(getTextField(2).getInteger());
			tile.setOffRangeX(getTextField(3).getInteger());
			tile.setOffRangeY(getTextField(4).getInteger());
			tile.setOffRangeZ(getTextField(5).getInteger());
			if (tile.getOnRangeX() > tile.getOffRangeX()) {
				tile.setOffRangeX(tile.getOnRangeX());
			}
			if (tile.getOnRangeY() > tile.getOffRangeY()) {
				tile.setOffRangeY(tile.getOnRangeY());
			}
			if (tile.getOnRangeZ() > tile.getOffRangeZ()) {
				tile.setOffRangeZ(tile.getOnRangeZ());
			}
		} else {
			tile.setOnRange(getTextField(0).getInteger());
			tile.setOffRange(getTextField(3).getInteger());
			if (tile.getOnRange() > tile.getOffRange()) {
				tile.setOffRange(tile.getOnRange());
			}
		}
		NBTTagCompound compound = new NBTTagCompound();
		tile.writeToNBT(compound);
		compound.removeTag("BlockActivated");
		Client.sendData(EnumPacketServer.SaveTileEntity, compound);
	}

	@Override
	public void setGuiData(NBTTagCompound compound) {
		tile.readFromNBT(compound);
	}
}

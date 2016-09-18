
package noppes.npcs.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileCopy extends TileEntity {
	public short length;
	public short width;
	public short height;
	public String name;

	public TileCopy() {
		length = 10;
		width = 10;
		height = 10;
		name = "";
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setShort("Length", length);
		compound.setShort("Width", width);
		compound.setShort("Height", height);
		S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(pos, 0, compound);
		return packet;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + width + 1, pos.getY() + height + 1,
				pos.getZ() + length + 1);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound compound = pkt.getNbtCompound();
		length = compound.getShort("Length");
		width = compound.getShort("Width");
		height = compound.getShort("Height");
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		length = compound.getShort("Length");
		width = compound.getShort("Width");
		height = compound.getShort("Height");
		name = compound.getString("Name");
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setShort("Length", length);
		compound.setShort("Width", width);
		compound.setShort("Height", height);
		compound.setString("Name", name);
	}
}

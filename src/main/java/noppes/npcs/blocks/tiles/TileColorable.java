
package noppes.npcs.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileColorable extends TileNpcEntity {
	private int color;
	private int rotation;

	public TileColorable() {
		color = 14;
	}

	public boolean canUpdate() {
		return false;
	}

	public int getColor() {
		return color;
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		compound.removeTag("Items");
		compound.removeTag("ExtraData");
		S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(pos, 0, compound);
		return packet;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
	}

	public int getRotation() {
		return rotation;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound compound = pkt.getNbtCompound();
		readFromNBT(compound);
	}

	public int powerProvided() {
		return 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		color = compound.getInteger("BannerColor");
		rotation = compound.getInteger("BannerRotation");
	}

	public void setColor(int color) {
		// new RuntimeException().printStackTrace();
		this.color = color;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("BannerColor", color);
		compound.setInteger("BannerRotation", rotation);
	}
}

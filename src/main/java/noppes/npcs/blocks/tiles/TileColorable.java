//

//

package noppes.npcs.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileColorable extends TileNpcEntity {
	public int color;
	public int rotation;

	public TileColorable() {
		color = 14;
	}

	public boolean canUpdate() {
		return false;
	}

	@Override
	public Packet getDescriptionPacket() {
		final NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		compound.removeTag("Items");
		compound.removeTag("ExtraData");
		final S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(pos, 0, compound);
		return packet;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
	}

	@Override
	public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity pkt) {
		final NBTTagCompound compound = pkt.getNbtCompound();
		readFromNBT(compound);
	}

	public int powerProvided() {
		return 0;
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		super.readFromNBT(compound);
		color = compound.getInteger("BannerColor");
		rotation = compound.getInteger("BannerRotation");
	}

	@Override
	public void writeToNBT(final NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("BannerColor", color);
		compound.setInteger("BannerRotation", rotation);
	}
}

package noppes.npcs.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import noppes.npcs.TextBlock;

public class TileBigSign extends TileNpcEntity {
	public int rotation;
	public boolean canEdit = true;
	public boolean hasChanged = true;
	private String signText = "";
	public TextBlock block;

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		compound.removeTag("ExtraData");
		S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(pos, 0, compound);
		return packet;
	}

	public String getText() {
		return signText;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound compound = pkt.getNbtCompound();
		readFromNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		rotation = compound.getInteger("SignRotation");
		setText(compound.getString("SignText"));
	}

	public void setText(String text) {
		signText = text;
		hasChanged = true;
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("SignRotation", rotation);
		compound.setString("SignText", signText);
	}
}

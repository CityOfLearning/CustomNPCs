//

//

package noppes.npcs.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import noppes.npcs.TextBlock;

public class TileBigSign extends TileNpcEntity {
	public int rotation;
	public boolean canEdit;
	public boolean hasChanged;
	private String signText;
	public TextBlock block;

	public TileBigSign() {
		canEdit = true;
		hasChanged = true;
		signText = "";
	}

	@Override
	public Packet getDescriptionPacket() {
		final NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		compound.removeTag("ExtraData");
		final S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(pos, 0, compound);
		return packet;
	}

	public String getText() {
		return signText;
	}

	@Override
	public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity pkt) {
		final NBTTagCompound compound = pkt.getNbtCompound();
		readFromNBT(compound);
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		super.readFromNBT(compound);
		rotation = compound.getInteger("SignRotation");
		setText(compound.getString("SignText"));
	}

	public void setText(final String text) {
		signText = text;
		hasChanged = true;
	}

	@Override
	public void writeToNBT(final NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("SignRotation", rotation);
		compound.setString("SignText", signText);
	}
}

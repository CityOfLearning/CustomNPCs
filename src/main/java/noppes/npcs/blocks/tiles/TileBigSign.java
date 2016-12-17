package noppes.npcs.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import noppes.npcs.util.TextBlock;

public class TileBigSign extends TileNpcEntity {
	private int rotation;
	private boolean canEdit = true;

	private boolean hasChanged = true;

	private String signText = "";

	private TextBlock block;

	public TextBlock getBlock() {
		return block;
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		compound.removeTag("ExtraData");
		S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(pos, 0, compound);
		return packet;
	}

	public int getRotation() {
		return rotation;
	}

	public String getSignText() {
		return signText;
	}

	public String getText() {
		return signText;
	}

	public boolean isCanEdit() {
		return canEdit;
	}

	public boolean isHasChanged() {
		return hasChanged;
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

	public void setBlock(TextBlock block) {
		this.block = block;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	public void setHasChanged(boolean hasChanged) {
		this.hasChanged = hasChanged;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public void setSignText(String signText) {
		this.signText = signText;
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

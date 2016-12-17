package noppes.npcs.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;

public class TileCouchWood extends TileChair {
	private boolean hasLeft = false;
	private boolean hasRight = false;

	public boolean hasLeft() {
		return hasLeft;
	}

	public boolean hasRight() {
		return hasRight;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		hasLeft = compound.getBoolean("CouchLeft");
		hasRight = compound.getBoolean("CouchRight");
	}

	public void setHasLeft(boolean hasLeft) {
		this.hasLeft = hasLeft;
	}

	public void setHasRight(boolean hasRight) {
		this.hasRight = hasRight;
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setBoolean("CouchLeft", hasLeft);
		compound.setBoolean("CouchRight", hasRight);
	}
}

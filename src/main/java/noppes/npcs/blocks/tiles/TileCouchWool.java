package noppes.npcs.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;

public class TileCouchWool extends TileChair {
	private boolean hasLeft = false;
	private boolean hasRight = false;
	private boolean hasCornerLeft = false;
	private boolean hasCornerRight = false;

	public boolean hasCornerLeft() {
		return hasCornerLeft;
	}

	public boolean hasCornerRight() {
		return hasCornerRight;
	}

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
		hasCornerLeft = compound.getBoolean("CouchCornerLeft");
		hasCornerRight = compound.getBoolean("CouchCornerRight");
	}

	public void setHasCornerLeft(boolean hasCornerLeft) {
		this.hasCornerLeft = hasCornerLeft;
	}

	public void setHasCornerRight(boolean hasCornerRight) {
		this.hasCornerRight = hasCornerRight;
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
		compound.setBoolean("CouchCornerLeft", hasCornerLeft);
		compound.setBoolean("CouchCornerRight", hasCornerRight);
	}
}

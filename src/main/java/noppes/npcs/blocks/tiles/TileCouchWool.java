//

//

package noppes.npcs.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;

public class TileCouchWool extends TileColorable {
	public boolean hasLeft;
	public boolean hasRight;
	public boolean hasCornerLeft;
	public boolean hasCornerRight;

	public TileCouchWool() {
		hasLeft = false;
		hasRight = false;
		hasCornerLeft = false;
		hasCornerRight = false;
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		super.readFromNBT(compound);
		hasLeft = compound.getBoolean("CouchLeft");
		hasRight = compound.getBoolean("CouchRight");
		hasCornerLeft = compound.getBoolean("CouchCornerLeft");
		hasCornerRight = compound.getBoolean("CouchCornerRight");
	}

	@Override
	public void writeToNBT(final NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setBoolean("CouchLeft", hasLeft);
		compound.setBoolean("CouchRight", hasRight);
		compound.setBoolean("CouchCornerLeft", hasCornerLeft);
		compound.setBoolean("CouchCornerRight", hasCornerRight);
	}
}

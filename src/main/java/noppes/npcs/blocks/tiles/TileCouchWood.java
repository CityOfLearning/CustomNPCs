//

//

package noppes.npcs.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;

public class TileCouchWood extends TileColorable {
	public boolean hasLeft;
	public boolean hasRight;

	public TileCouchWood() {
		hasLeft = false;
		hasRight = false;
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		super.readFromNBT(compound);
		hasLeft = compound.getBoolean("CouchLeft");
		hasRight = compound.getBoolean("CouchRight");
	}

	@Override
	public void writeToNBT(final NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setBoolean("CouchLeft", hasLeft);
		compound.setBoolean("CouchRight", hasRight);
	}
}

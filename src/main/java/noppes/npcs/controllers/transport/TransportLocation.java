//

//

package noppes.npcs.controllers.transport;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

public class TransportLocation {
	public int id;
	public String name;
	public BlockPos pos;
	public int type;
	public int dimension;
	public TransportCategory category;

	public TransportLocation() {
		id = -1;
		name = "default name";
		type = 0;
		dimension = 0;
	}

	public boolean isDefault() {
		return type == 1;
	}

	public void readNBT(NBTTagCompound compound) {
		if (compound == null) {
			return;
		}
		id = compound.getInteger("Id");
		pos = new BlockPos(compound.getDouble("PosX"), compound.getDouble("PosY"), compound.getDouble("PosZ"));
		type = compound.getInteger("Type");
		dimension = compound.getInteger("Dimension");
		name = compound.getString("Name");
	}

	public NBTTagCompound writeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("Id", id);
		compound.setDouble("PosX", pos.getX());
		compound.setDouble("PosY", pos.getY());
		compound.setDouble("PosZ", pos.getZ());
		compound.setInteger("Type", type);
		compound.setInteger("Dimension", dimension);
		compound.setString("Name", name);
		return compound;
	}
}

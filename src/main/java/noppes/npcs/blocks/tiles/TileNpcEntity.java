
package noppes.npcs.blocks.tiles;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileNpcEntity extends TileEntity {
	public Map<String, Object> tempData;
	public NBTTagCompound extraData;

	public TileNpcEntity() {
		tempData = new HashMap<String, Object>();
		extraData = new NBTTagCompound();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		extraData = compound.getCompoundTag("ExtraData");
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("ExtraData", extraData);
	}
}

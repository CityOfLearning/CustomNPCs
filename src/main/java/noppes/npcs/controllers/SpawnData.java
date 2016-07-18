//

//

package noppes.npcs.controllers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandom;
import noppes.npcs.NBTTags;

public class SpawnData extends WeightedRandom.Item {
	public List<String> biomes;
	public int id;
	public String name;
	public NBTTagCompound compound1;
	public boolean liquid;
	public int type;

	public SpawnData() {
		super(10);
		biomes = new ArrayList<String>();
		id = -1;
		name = "";
		compound1 = new NBTTagCompound();
		liquid = false;
		type = 0;
	}

	public void readNBT(final NBTTagCompound compound) {
		id = compound.getInteger("SpawnId");
		name = compound.getString("SpawnName");
		itemWeight = compound.getInteger("SpawnWeight");
		if (itemWeight == 0) {
			itemWeight = 1;
		}
		biomes = NBTTags.getStringList(compound.getTagList("SpawnBiomes", 10));
		compound1 = compound.getCompoundTag("SpawnCompound1");
		type = compound.getInteger("SpawnType");
	}

	public NBTTagCompound writeNBT(final NBTTagCompound compound) {
		compound.setInteger("SpawnId", id);
		compound.setString("SpawnName", name);
		compound.setInteger("SpawnWeight", itemWeight);
		compound.setTag("SpawnBiomes", NBTTags.nbtStringList(biomes));
		compound.setTag("SpawnCompound1", compound1);
		compound.setInteger("SpawnType", type);
		return compound;
	}
}

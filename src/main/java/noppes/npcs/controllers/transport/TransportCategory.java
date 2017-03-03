
package noppes.npcs.controllers.transport;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TransportCategory {
	public int id;
	public String title;
	public HashMap<Integer, TransportLocation> locations;

	public TransportCategory() {
		id = -1;
		title = "";
		locations = new HashMap<>();
	}

	public Vector<TransportLocation> getDefaultLocations() {
		Vector<TransportLocation> list = new Vector<>();
		for (TransportLocation loc : locations.values()) {
			if (loc.isDefault()) {
				list.add(loc);
			}
		}
		return list;
	}

	public void readNBT(NBTTagCompound compound) {
		id = compound.getInteger("CategoryId");
		title = compound.getString("CategoryTitle");
		NBTTagList locs = compound.getTagList("CategoryLocations", 10);
		if ((locs == null) || (locs.tagCount() == 0)) {
			return;
		}
		for (int ii = 0; ii < locs.tagCount(); ++ii) {
			TransportLocation location = new TransportLocation();
			location.readNBT(locs.getCompoundTagAt(ii));
			location.category = this;
			locations.put(location.id, location);
		}
	}

	public void writeNBT(NBTTagCompound compound) {
		compound.setInteger("CategoryId", id);
		compound.setString("CategoryTitle", title);
		NBTTagList locs = new NBTTagList();
		for (TransportLocation location : locations.values()) {
			locs.appendTag(location.writeNBT());
		}
		compound.setTag("CategoryLocations", locs);
	}
}

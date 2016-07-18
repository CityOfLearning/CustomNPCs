//

//

package noppes.npcs.controllers;

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
		locations = new HashMap<Integer, TransportLocation>();
	}

	public Vector<TransportLocation> getDefaultLocations() {
		final Vector<TransportLocation> list = new Vector<TransportLocation>();
		for (final TransportLocation loc : locations.values()) {
			if (loc.isDefault()) {
				list.add(loc);
			}
		}
		return list;
	}

	public void readNBT(final NBTTagCompound compound) {
		id = compound.getInteger("CategoryId");
		title = compound.getString("CategoryTitle");
		final NBTTagList locs = compound.getTagList("CategoryLocations", 10);
		if ((locs == null) || (locs.tagCount() == 0)) {
			return;
		}
		for (int ii = 0; ii < locs.tagCount(); ++ii) {
			final TransportLocation location = new TransportLocation();
			location.readNBT(locs.getCompoundTagAt(ii));
			location.category = this;
			locations.put(location.id, location);
		}
	}

	public void writeNBT(final NBTTagCompound compound) {
		compound.setInteger("CategoryId", id);
		compound.setString("CategoryTitle", title);
		final NBTTagList locs = new NBTTagList();
		for (final TransportLocation location : locations.values()) {
			locs.appendTag(location.writeNBT());
		}
		compound.setTag("CategoryLocations", locs);
	}
}

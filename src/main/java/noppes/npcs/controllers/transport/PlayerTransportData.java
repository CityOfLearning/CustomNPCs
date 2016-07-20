//

//

package noppes.npcs.controllers.transport;

import java.util.HashSet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerTransportData {
	public HashSet<Integer> transports;

	public PlayerTransportData() {
		transports = new HashSet<Integer>();
	}

	public void loadNBTData(final NBTTagCompound compound) {
		final HashSet<Integer> dialogsRead = new HashSet<Integer>();
		if (compound == null) {
			return;
		}
		final NBTTagList list = compound.getTagList("TransportData", 10);
		if (list == null) {
			return;
		}
		for (int i = 0; i < list.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
			dialogsRead.add(nbttagcompound.getInteger("Transport"));
		}
		transports = dialogsRead;
	}

	public void saveNBTData(final NBTTagCompound compound) {
		final NBTTagList list = new NBTTagList();
		for (final int dia : transports) {
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Transport", dia);
			list.appendTag(nbttagcompound);
		}
		compound.setTag("TransportData", list);
	}
}


package noppes.npcs.controllers.transport;

import java.util.HashSet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerTransportData {
	public HashSet<Integer> transports;

	public PlayerTransportData() {
		transports = new HashSet<>();
	}

	public void loadNBTData(NBTTagCompound compound) {
		HashSet<Integer> dialogsRead = new HashSet<>();
		if (compound == null) {
			return;
		}
		NBTTagList list = compound.getTagList("TransportData", 10);
		if (list == null) {
			return;
		}
		for (int i = 0; i < list.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
			dialogsRead.add(nbttagcompound.getInteger("Transport"));
		}
		transports = dialogsRead;
	}

	public void saveNBTData(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		for (int dia : transports) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Transport", dia);
			list.appendTag(nbttagcompound);
		}
		compound.setTag("TransportData", list);
	}
}

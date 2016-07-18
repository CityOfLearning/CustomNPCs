//

//

package noppes.npcs.roles;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentTranslation;
import noppes.npcs.controllers.InnDoorData;
import noppes.npcs.entity.EntityNPCInterface;

public class RoleInnkeeper extends RoleInterface {
	private String innName;
	private HashMap<String, InnDoorData> doors;

	public RoleInnkeeper(final EntityNPCInterface npc) {
		super(npc);
		innName = "Inn";
		doors = new HashMap<String, InnDoorData>();
	}

	private HashMap<String, InnDoorData> getInnDoors(final NBTTagList tagList) {
		final HashMap<String, InnDoorData> list = new HashMap<String, InnDoorData>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			final String name = nbttagcompound.getString("Name");
			final InnDoorData door = new InnDoorData();
			door.x = nbttagcompound.getInteger("posX");
			door.y = nbttagcompound.getInteger("posY");
			door.z = nbttagcompound.getInteger("posZ");
			list.put(name, door);
		}
		return list;
	}

	@Override
	public void interact(final EntityPlayer player) {
		npc.say(player, npc.advanced.getInteractLine());
		if (doors.isEmpty()) {
			player.addChatMessage(new ChatComponentTranslation("No Rooms available", new Object[0]));
		}
	}

	private NBTBase nbtInnDoors(final HashMap<String, InnDoorData> doors1) {
		final NBTTagList nbttaglist = new NBTTagList();
		if (doors1 == null) {
			return nbttaglist;
		}
		for (final String name : doors1.keySet()) {
			final InnDoorData door = doors1.get(name);
			if (door == null) {
				continue;
			}
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setString("Name", name);
			nbttagcompound.setInteger("posX", door.x);
			nbttagcompound.setInteger("posY", door.y);
			nbttagcompound.setInteger("posZ", door.z);
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbttagcompound) {
		innName = nbttagcompound.getString("InnName");
		doors = getInnDoors(nbttagcompound.getTagList("InnDoors", 10));
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
		nbttagcompound.setString("InnName", innName);
		nbttagcompound.setTag("InnDoors", nbtInnDoors(doors));
		return nbttagcompound;
	}
}

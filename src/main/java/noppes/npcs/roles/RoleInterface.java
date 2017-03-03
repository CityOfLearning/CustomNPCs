
package noppes.npcs.roles;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.entity.data.INPCRole;
import noppes.npcs.entity.EntityNPCInterface;

public abstract class RoleInterface implements INPCRole {
	public EntityNPCInterface npc;
	public HashMap<String, String> dataString;

	public RoleInterface(EntityNPCInterface npc) {
		dataString = new HashMap<>();
		this.npc = npc;
	}

	public boolean aiContinueExecute() {
		return false;
	}

	public boolean aiShouldExecute() {
		return false;
	}

	public void aiStartExecuting() {
	}

	public void aiUpdateTask() {
	}

	public void clientUpdate() {
	}

	public boolean defendOwner() {
		return false;
	}

	public void delete() {
	}

	@Override
	public int getType() {
		return npc.advanced.role;
	}

	public abstract void interact(EntityPlayer p0);

	public void killed() {
	}

	public abstract void readFromNBT(NBTTagCompound p0);

	public abstract NBTTagCompound writeToNBT(NBTTagCompound p0);
}

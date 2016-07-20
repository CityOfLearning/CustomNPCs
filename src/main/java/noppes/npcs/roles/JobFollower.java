//

//

package noppes.npcs.roles;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.CustomNpcs;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.data.role.IJobFollower;
import noppes.npcs.entity.EntityNPCInterface;

public class JobFollower extends JobInterface implements IJobFollower {
	public EntityNPCInterface following;
	private int ticks;
	private int range;
	public String name;

	public JobFollower(EntityNPCInterface npc) {
		super(npc);
		following = null;
		ticks = 40;
		range = 20;
		name = "";
	}

	@Override
	public boolean aiShouldExecute() {
		if (npc.isAttacking()) {
			return false;
		}
		--ticks;
		if (ticks > 0) {
			return false;
		}
		ticks = 10;
		following = null;
		List<EntityNPCInterface> list = npc.worldObj.getEntitiesWithinAABB((Class) EntityNPCInterface.class,
				npc.getEntityBoundingBox().expand(getRange(), getRange(), getRange()));
		for (EntityNPCInterface entity : list) {
			if (entity != npc) {
				if (entity.isKilled()) {
					continue;
				}
				if (entity.display.getName().equalsIgnoreCase(name)) {
					following = entity;
					break;
				}
				continue;
			}
		}
		return false;
	}

	@Override
	public String getFollowing() {
		return name;
	}

	@Override
	public ICustomNpc getFollowingNpc() {
		if (following == null) {
			return null;
		}
		return following.wrappedNPC;
	}

	private int getRange() {
		if (range > CustomNpcs.NpcNavRange) {
			return CustomNpcs.NpcNavRange;
		}
		return range;
	}

	public boolean hasOwner() {
		return !name.isEmpty();
	}

	@Override
	public boolean isFollowing() {
		return following != null;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		name = compound.getString("FollowingEntityName");
	}

	@Override
	public void reset() {
	}

	@Override
	public void resetTask() {
		following = null;
	}

	@Override
	public void setFollowing(String name) {
		this.name = name;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setString("FollowingEntityName", name);
		return compound;
	}
}

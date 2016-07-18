//

//

package noppes.npcs.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;

public class EntityNPCGolem extends EntityNPCInterface {
	public EntityNPCGolem(final World world) {
		super(world);
		display.setSkinTexture("customnpcs:textures/entity/golem/Iron Golem.png");
		width = 1.4f;
		height = 2.5f;
	}

	@Override
	public void onUpdate() {
		isDead = true;
		if (!worldObj.isRemote) {
			final NBTTagCompound compound = new NBTTagCompound();
			writeToNBT(compound);
			final EntityCustomNpc npc = new EntityCustomNpc(worldObj);
			npc.readFromNBT(compound);
			final ModelData data = npc.modelData;
			data.setEntityClass(EntityNPCGolem.class);
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}

	@Override
	public void updateHitbox() {
		currentAnimation = dataWatcher.getWatchableObjectInt(14);
		if (currentAnimation == 2) {
			final float n = 0.5f;
			height = n;
			width = n;
		} else if (currentAnimation == 1) {
			width = 1.4f;
			height = 2.0f;
		} else {
			width = 1.4f;
			height = 2.5f;
		}
	}
}

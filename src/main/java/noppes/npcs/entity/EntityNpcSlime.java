//

//

package noppes.npcs.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;

public class EntityNpcSlime extends EntityNPCInterface {
	public EntityNpcSlime(final World world) {
		super(world);
		scaleX = 2.0f;
		scaleY = 2.0f;
		scaleZ = 2.0f;
		display.setSkinTexture("customnpcs:textures/entity/slime/Slime.png");
		width = 0.8f;
		height = 0.8f;
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
			data.setEntityClass(EntityNpcSlime.class);
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}

	@Override
	public void updateHitbox() {
		width = 0.8f;
		height = 0.8f;
	}
}

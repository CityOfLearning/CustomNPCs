
package noppes.npcs.entity.old;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.model.ModelData;

public class EntityNPCEnderman extends EntityNpcEnderchibi {
	public EntityNPCEnderman(World world) {
		super(world);
		display.setSkinTexture("customnpcs:textures/entity/enderman/enderman.png");
		display.setOverlayTexture("customnpcs:textures/overlays/ender_eyes.png");
		width = 0.6f;
		height = 2.9f;
	}

	@Override
	public void onUpdate() {
		isDead = true;
		if (!worldObj.isRemote) {
			NBTTagCompound compound = new NBTTagCompound();
			writeToNBT(compound);
			EntityCustomNpc npc = new EntityCustomNpc(worldObj);
			npc.readFromNBT(compound);
			ModelData data = npc.modelData;
			data.setEntityClass(EntityEnderman.class);
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}

	@Override
	public void updateHitbox() {
		if (currentAnimation == 2) {
			float n = 0.2f;
			height = n;
			width = n;
		} else if (currentAnimation == 1) {
			width = 0.6f;
			height = 2.3f;
		} else {
			width = 0.6f;
			height = 2.9f;
		}
		width = (width / 5.0f) * display.getSize();
		height = (height / 5.0f) * display.getSize();
	}
}

//

//

package noppes.npcs.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;

public class EntityNpcCrystal extends EntityNPCInterface {
	public EntityNpcCrystal(final World world) {
		super(world);
		scaleX = 0.7f;
		scaleY = 0.7f;
		scaleZ = 0.7f;
		display.setSkinTexture("customnpcs:textures/entity/crystal/EnderCrystal.png");
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
			data.setEntityClass(EntityNpcCrystal.class);
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}
}

//

//

package noppes.npcs.entity.old;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNPCVillager extends EntityNPCInterface {
	public EntityNPCVillager(final World world) {
		super(world);
		display.setSkinTexture("textures/entity/villager/villager.png");
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
			data.setEntityClass(EntityVillager.class);
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}
}

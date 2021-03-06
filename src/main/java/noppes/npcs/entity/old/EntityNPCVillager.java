
package noppes.npcs.entity.old;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.model.ModelData;

public class EntityNPCVillager extends EntityNPCInterface {
	public EntityNPCVillager(World world) {
		super(world);
		display.setSkinTexture("textures/entity/villager/villager.png");
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
			data.setEntityClass(EntityVillager.class);
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}
}

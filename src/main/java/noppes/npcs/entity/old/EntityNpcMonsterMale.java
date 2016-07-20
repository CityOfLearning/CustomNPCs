//

//

package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNpcMonsterMale extends EntityNPCInterface {
	public EntityNpcMonsterMale(World world) {
		super(world);
		display.setSkinTexture("customnpcs:textures/entity/monstermale/ZombieSteve.png");
	}

	@Override
	public void onUpdate() {
		isDead = true;
		if (!worldObj.isRemote) {
			NBTTagCompound compound = new NBTTagCompound();
			writeToNBT(compound);
			EntityCustomNpc npc = new EntityCustomNpc(worldObj);
			npc.readFromNBT(compound);
			npc.ai.animationType = 8;
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}
}

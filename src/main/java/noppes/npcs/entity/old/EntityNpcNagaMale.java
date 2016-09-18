
package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.ModelPartData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNpcNagaMale extends EntityNPCInterface {
	public EntityNpcNagaMale(World world) {
		super(world);
		display.setSkinTexture("customnpcs:textures/entity/nagamale/Cobra.png");
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
			ModelPartData legs = data.getOrCreatePart(EnumParts.LEGS);
			legs.playerTexture = true;
			legs.type = 1;
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}
}

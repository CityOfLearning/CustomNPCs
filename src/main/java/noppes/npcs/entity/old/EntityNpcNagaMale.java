//

//

package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.ModelPartData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNpcNagaMale extends EntityNPCInterface {
	public EntityNpcNagaMale(final World world) {
		super(world);
		display.setSkinTexture("customnpcs:textures/entity/nagamale/Cobra.png");
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
			final ModelPartData legs = data.getOrCreatePart(EnumParts.LEGS);
			legs.playerTexture = true;
			legs.type = 1;
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}
}

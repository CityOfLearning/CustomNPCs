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

public class EntityNPCFurryMale extends EntityNPCInterface {
	public EntityNPCFurryMale(final World world) {
		super(world);
		display.setSkinTexture("customnpcs:textures/entity/furrymale/WolfGrey.png");
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
			final ModelPartData ears = data.getOrCreatePart(EnumParts.EARS);
			ears.type = 0;
			ears.color = 6182997;
			final ModelPartData snout = data.getOrCreatePart(EnumParts.SNOUT);
			snout.type = 2;
			snout.color = 6182997;
			final ModelPartData tail = data.getOrCreatePart(EnumParts.TAIL);
			tail.type = 0;
			tail.color = 6182997;
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}
}

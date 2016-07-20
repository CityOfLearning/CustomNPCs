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
	public EntityNPCFurryMale(World world) {
		super(world);
		display.setSkinTexture("customnpcs:textures/entity/furrymale/WolfGrey.png");
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
			ModelPartData ears = data.getOrCreatePart(EnumParts.EARS);
			ears.type = 0;
			ears.color = 6182997;
			ModelPartData snout = data.getOrCreatePart(EnumParts.SNOUT);
			snout.type = 2;
			snout.color = 6182997;
			ModelPartData tail = data.getOrCreatePart(EnumParts.TAIL);
			tail.type = 0;
			tail.color = 6182997;
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}
}

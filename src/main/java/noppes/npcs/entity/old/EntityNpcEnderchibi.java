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

public class EntityNpcEnderchibi extends EntityNPCInterface {
	public EntityNpcEnderchibi(final World world) {
		super(world);
		display.setSkinTexture("customnpcs:textures/entity/enderchibi/MrEnderchibi.png");
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
			data.getPartConfig(EnumParts.LEG_LEFT).setScale(0.65f, 0.75f);
			data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.5f, 1.45f);
			final ModelPartData part = data.getOrCreatePart(EnumParts.PARTICLES);
			part.type = 1;
			part.color = 16711680;
			part.playerTexture = true;
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}
}

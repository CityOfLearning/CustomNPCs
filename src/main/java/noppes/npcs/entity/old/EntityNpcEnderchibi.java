
package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.model.ModelData;
import noppes.npcs.model.ModelPartData;

public class EntityNpcEnderchibi extends EntityNPCInterface {
	public EntityNpcEnderchibi(World world) {
		super(world);
		display.setSkinTexture("customnpcs:textures/entity/enderchibi/MrEnderchibi.png");
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
			data.getPartConfig(EnumParts.LEG_LEFT).setScale(0.65f, 0.75f);
			data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.5f, 1.45f);
			ModelPartData part = data.getOrCreatePart(EnumParts.PARTICLES);
			part.type = 1;
			part.color = 16711680;
			part.playerTexture = true;
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}
}

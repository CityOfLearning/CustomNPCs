//

//

package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNPCOrcFemale extends EntityNPCInterface {
	public EntityNPCOrcFemale(World world) {
		super(world);
		float scaleX = 0.9375f;
		scaleZ = scaleX;
		scaleY = scaleX;
		this.scaleX = scaleX;
		display.setSkinTexture("customnpcs:textures/entity/orcfemale/StrandedFemaleOrc.png");
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
			data.getOrCreatePart(EnumParts.BREASTS).type = 2;
			data.getPartConfig(EnumParts.LEG_LEFT).setScale(1.1f, 1.0f);
			data.getPartConfig(EnumParts.ARM_LEFT).setScale(1.1f, 1.0f);
			data.getPartConfig(EnumParts.BODY).setScale(1.1f, 1.0f, 1.25f);
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}
}

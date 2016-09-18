
package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNPCOrcMale extends EntityNPCInterface {
	public EntityNPCOrcMale(World world) {
		super(world);
		scaleY = 1.0f;
		float n = 1.2f;
		scaleZ = n;
		scaleX = n;
		display.setSkinTexture("customnpcs:textures/entity/orcmale/StrandedOrc.png");
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
			data.getPartConfig(EnumParts.LEG_LEFT).setScale(1.2f, 1.05f);
			data.getPartConfig(EnumParts.ARM_LEFT).setScale(1.2f, 1.05f);
			data.getPartConfig(EnumParts.BODY).setScale(1.4f, 1.1f, 1.5f);
			data.getPartConfig(EnumParts.HEAD).setScale(1.2f, 1.1f);
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}
}

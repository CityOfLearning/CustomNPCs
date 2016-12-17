
package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.model.ModelData;

public class EntityNPCDwarfFemale extends EntityNPCInterface {
	public EntityNPCDwarfFemale(World world) {
		super(world);
		float n = 0.75f;
		scaleZ = n;
		scaleX = n;
		scaleY = 0.6275f;
		display.setSkinTexture("customnpcs:textures/entity/dwarffemale/Simone.png");
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
			data.getPartConfig(EnumParts.LEG_LEFT).setScale(0.9f, 0.65f);
			data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.9f, 0.65f);
			data.getPartConfig(EnumParts.BODY).setScale(1.0f, 0.65f, 1.1f);
			data.getPartConfig(EnumParts.HEAD).setScale(0.85f, 0.85f);
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}
}

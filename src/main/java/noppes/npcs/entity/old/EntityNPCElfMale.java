
package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.model.ModelData;

public class EntityNPCElfMale extends EntityNPCInterface {
	public EntityNPCElfMale(World world) {
		super(world);
		scaleX = 0.85f;
		scaleY = 1.07f;
		scaleZ = 0.85f;
		display.setSkinTexture("customnpcs:textures/entity/elfmale/ElfMale.png");
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
			data.getPartConfig(EnumParts.LEG_LEFT).setScale(0.85f, 1.15f);
			data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.85f, 1.15f);
			data.getPartConfig(EnumParts.BODY).setScale(0.85f, 1.15f);
			data.getPartConfig(EnumParts.HEAD).setScale(0.85f, 0.95f);
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}
}

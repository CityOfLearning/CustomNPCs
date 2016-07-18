//

//

package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNPCDwarfMale extends EntityNPCInterface {
	public EntityNPCDwarfMale(final World world) {
		super(world);
		final float n = 0.85f;
		scaleZ = n;
		scaleX = n;
		scaleY = 0.6875f;
		display.setSkinTexture("customnpcs:textures/entity/dwarfmale/Simon.png");
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
			data.getPartConfig(EnumParts.LEG_LEFT).setScale(1.1f, 0.7f, 0.9f);
			data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.9f, 0.7f);
			data.getPartConfig(EnumParts.BODY).setScale(1.2f, 0.7f, 1.5f);
			data.getPartConfig(EnumParts.HEAD).setScale(0.85f, 0.85f);
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}
}

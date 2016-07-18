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

public class EntityNPCFurryFemale extends EntityNPCInterface {
	public EntityNPCFurryFemale(final World world) {
		super(world);
		final float scaleX = 0.9075f;
		scaleZ = scaleX;
		scaleY = scaleX;
		this.scaleX = scaleX;
		display.setSkinTexture("customnpcs:textures/entity/furryfemale/WolfBlack.png");
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
			data.getOrCreatePart(EnumParts.BREASTS).type = 2;
			data.getPartConfig(EnumParts.HEAD).setScale(0.95f, 0.95f);
			data.getPartConfig(EnumParts.LEG_LEFT).setScale(0.92f, 0.92f);
			data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.8f, 0.92f);
			data.getPartConfig(EnumParts.BODY).setScale(0.92f, 0.92f);
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}
}

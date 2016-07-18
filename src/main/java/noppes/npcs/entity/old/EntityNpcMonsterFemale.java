//

//

package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNpcMonsterFemale extends EntityNPCInterface {
	public EntityNpcMonsterFemale(final World world) {
		super(world);
		final float scaleX = 0.9075f;
		scaleZ = scaleX;
		scaleY = scaleX;
		this.scaleX = scaleX;
		display.setSkinTexture("customnpcs:textures/entity/monsterfemale/ZombieStephanie.png");
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
			data.getOrCreatePart(EnumParts.BREASTS).type = 2;
			data.getPartConfig(EnumParts.LEG_LEFT).setScale(0.92f, 0.92f);
			data.getPartConfig(EnumParts.HEAD).setScale(0.95f, 0.95f);
			data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.8f, 0.92f);
			data.getPartConfig(EnumParts.BODY).setScale(0.92f, 0.92f);
			npc.ai.animationType = 8;
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}
}

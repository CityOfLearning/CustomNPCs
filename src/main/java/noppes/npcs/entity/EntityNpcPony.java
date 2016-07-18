//

//

package noppes.npcs.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import noppes.npcs.ModelData;

public class EntityNpcPony extends EntityNPCInterface {
	public boolean isPegasus;
	public boolean isUnicorn;
	public boolean isFlying;
	public ResourceLocation checked;

	public EntityNpcPony(final World world) {
		super(world);
		isPegasus = false;
		isUnicorn = false;
		isFlying = false;
		checked = null;
		display.setSkinTexture("customnpcs:textures/entity/ponies/MineLP Derpy Hooves.png");
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
			data.setEntityClass(EntityNpcPony.class);
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}
}

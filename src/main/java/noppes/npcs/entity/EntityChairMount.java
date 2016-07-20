//

//

package noppes.npcs.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityChairMount extends Entity {
	public EntityChairMount(World world) {
		super(world);
		setSize(0.0f, 0.0f);
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	protected void entityInit() {
	}

	@Override
	public void fall(float p_70069_1_, float mod) {
	}

	@Override
	public double getMountedYOffset() {
		return 0.5;
	}

	@Override
	public boolean isEntityInvulnerable(DamageSource source) {
		return true;
	}

	@Override
	public boolean isInvisible() {
		return true;
	}

	@Override
	public void moveEntity(double p_70091_1_, double p_70091_3_, double p_70091_5_) {
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if ((worldObj != null) && !worldObj.isRemote && (riddenByEntity == null)) {
			isDead = true;
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_, double p_70056_5_, float p_70056_7_,
			float p_70056_8_, int p_70056_9_, boolean bo) {
		setPosition(p_70056_1_, p_70056_3_, p_70056_5_);
		setRotation(p_70056_7_, p_70056_8_);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
	}
}

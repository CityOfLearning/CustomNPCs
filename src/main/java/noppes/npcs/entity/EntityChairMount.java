package noppes.npcs.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.blocks.BlockChair;
import noppes.npcs.blocks.BlockCouchWood;
import noppes.npcs.blocks.BlockCouchWool;
import noppes.npcs.blocks.tiles.TileChair;

public class EntityChairMount extends Entity {

	public EntityChairMount(World world) {
		super(world);
		setSize(0.0F, 0.0F);
	}

	public EntityChairMount(World worldIn, double x, double y, double z) {
		this(worldIn);
		this.setPosition(x, y, z);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
	}

	public EntityChairMount(World worldIn, double x, double y, double z, float yaw) {
		this(worldIn);
		this.setPosition(x, y, z);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
		this.rotationYaw = yaw;
		this.prevRotationYaw = yaw;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	protected boolean canTriggerWalking() {
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
		return 0.4D;
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
		if (worldObj != null) {
			super.onEntityUpdate();
			if ((!worldObj.isRemote) && !(worldObj.getTileEntity(getPosition()) instanceof TileChair)) {
				isDead = true;
			}
		}
	}

	public void updateRiderPosition() {
		super.updateRiderPosition();
		if (this.riddenByEntity != null && worldObj != null && worldObj.getBlockState(getPosition()) != null) {
			Block b = worldObj.getBlockState(getPosition()).getBlock();
			if (b instanceof BlockChair) {
				if (Math.abs(this.riddenByEntity.rotationYaw - this.rotationYaw) > 90) {
					this.riddenByEntity.rotationYaw = this.riddenByEntity.rotationYaw - this.rotationYaw > 0
							? rotationYaw + 90 : rotationYaw - 90;
				}
			} else if (b instanceof BlockCouchWood || b instanceof BlockCouchWool) {
				if (Math.abs(this.riddenByEntity.rotationYaw - this.rotationYaw) > 45) {
					this.riddenByEntity.rotationYaw = this.riddenByEntity.rotationYaw - this.rotationYaw > 0
							? rotationYaw + 45 : rotationYaw - 45;
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements,
			boolean p_180426_10_) {
		setPosition(x, y, z);
		setRotation(yaw, pitch);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompound) {

	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound) {

	}
}

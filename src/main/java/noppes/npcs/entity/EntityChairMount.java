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
		setPosition(x, y, z);
		motionX = 0.0D;
		motionY = 0.0D;
		motionZ = 0.0D;
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;
	}

	public EntityChairMount(World worldIn, double x, double y, double z, float yaw) {
		this(worldIn);
		setPosition(x, y, z);
		motionX = 0.0D;
		motionY = 0.0D;
		motionZ = 0.0D;
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;
		rotationYaw = yaw;
		prevRotationYaw = yaw;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	protected boolean canTriggerWalking() {
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

	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompound) {

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements,
			boolean p_180426_10_) {
		setPosition(x, y, z);
		setRotation(yaw, pitch);
	}

	@Override
	public void updateRiderPosition() {
		super.updateRiderPosition();
		if ((riddenByEntity != null) && (worldObj != null) && (worldObj.getBlockState(getPosition()) != null)) {
			Block b = worldObj.getBlockState(getPosition()).getBlock();
			if (b instanceof BlockChair) {
				if (Math.abs(riddenByEntity.rotationYaw - rotationYaw) > 90) {
					riddenByEntity.rotationYaw = (riddenByEntity.rotationYaw - rotationYaw) > 0 ? rotationYaw + 90
							: rotationYaw - 90;
				}
			} else if ((b instanceof BlockCouchWood) || (b instanceof BlockCouchWool)) {
				if (Math.abs(riddenByEntity.rotationYaw - rotationYaw) > 45) {
					riddenByEntity.rotationYaw = (riddenByEntity.rotationYaw - rotationYaw) > 0 ? rotationYaw + 45
							: rotationYaw - 45;
				}
			}
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound) {

	}
}

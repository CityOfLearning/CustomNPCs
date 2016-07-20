//

//

package noppes.npcs.blocks.tiles;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import noppes.npcs.CustomNpcs;
import noppes.npcs.blocks.BlockNpcRedstone;
import noppes.npcs.controllers.Availability;

public class TileRedstoneBlock extends TileNpcEntity implements ITickable {
	public int onRange;
	public int offRange;
	public int onRangeX;
	public int onRangeY;
	public int onRangeZ;
	public int offRangeX;
	public int offRangeY;
	public int offRangeZ;
	public boolean isDetailed;
	public Availability availability;
	public boolean isActivated;
	private int ticks;

	public TileRedstoneBlock() {
		onRange = 6;
		offRange = 10;
		onRangeX = 6;
		onRangeY = 6;
		onRangeZ = 6;
		offRangeX = 10;
		offRangeY = 10;
		offRangeZ = 10;
		isDetailed = false;
		availability = new Availability();
		isActivated = false;
		ticks = 10;
	}

	public boolean canUpdate() {
		return true;
	}

	private List<EntityPlayer> getPlayerList(int x, int y, int z) {
		return worldObj.getEntitiesWithinAABB((Class) EntityPlayer.class,
				new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)
						.expand(x, y, z));
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		onRange = compound.getInteger("BlockOnRange");
		offRange = compound.getInteger("BlockOffRange");
		isDetailed = compound.getBoolean("BlockIsDetailed");
		if (compound.hasKey("BlockOnRangeX")) {
			isDetailed = true;
			onRangeX = compound.getInteger("BlockOnRangeX");
			onRangeY = compound.getInteger("BlockOnRangeY");
			onRangeZ = compound.getInteger("BlockOnRangeZ");
			offRangeX = compound.getInteger("BlockOffRangeX");
			offRangeY = compound.getInteger("BlockOffRangeY");
			offRangeZ = compound.getInteger("BlockOffRangeZ");
		}
		if (compound.hasKey("BlockActivated")) {
			isActivated = compound.getBoolean("BlockActivated");
		}
		availability.readFromNBT(compound);
		if (worldObj != null) {
			setActive(getBlockType(), isActivated);
		}
	}

	private void setActive(Block block, boolean bo) {
		isActivated = bo;
		IBlockState state = block.getDefaultState().withProperty(BlockNpcRedstone.ACTIVE, isActivated);
		worldObj.setBlockState(pos, state, 2);
		worldObj.markBlockForUpdate(pos);
		block.onBlockAdded(worldObj, pos, state);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public void update() {
		if (worldObj.isRemote) {
			return;
		}
		--ticks;
		if (ticks > 0) {
			return;
		}
		ticks = 20;
		Block block = getBlockType();
		if ((block == null) || !(block instanceof BlockNpcRedstone)) {
			return;
		}
		if (CustomNpcs.FreezeNPCs) {
			if (isActivated) {
				setActive(block, false);
			}
			return;
		}
		if (!isActivated) {
			int x = isDetailed ? onRangeX : onRange;
			int y = isDetailed ? onRangeY : onRange;
			int z = isDetailed ? onRangeZ : onRange;
			List<EntityPlayer> list = getPlayerList(x, y, z);
			if (list.isEmpty()) {
				return;
			}
			for (EntityPlayer player : list) {
				if (availability.isAvailable(player)) {
					setActive(block, true);
				}
			}
		} else {
			int x = isDetailed ? offRangeX : offRange;
			int y = isDetailed ? offRangeY : offRange;
			int z = isDetailed ? offRangeZ : offRange;
			List<EntityPlayer> list = getPlayerList(x, y, z);
			for (EntityPlayer player : list) {
				if (availability.isAvailable(player)) {
					return;
				}
			}
			setActive(block, false);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("BlockOnRange", onRange);
		compound.setInteger("BlockOffRange", offRange);
		compound.setBoolean("BlockActivated", isActivated);
		compound.setBoolean("BlockIsDetailed", isDetailed);
		if (isDetailed) {
			compound.setInteger("BlockOnRangeX", onRangeX);
			compound.setInteger("BlockOnRangeY", onRangeY);
			compound.setInteger("BlockOnRangeZ", onRangeZ);
			compound.setInteger("BlockOffRangeX", offRangeX);
			compound.setInteger("BlockOffRangeY", offRangeY);
			compound.setInteger("BlockOffRangeZ", offRangeZ);
		}
		availability.writeToNBT(compound);
	}
}

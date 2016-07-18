//

//

package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockStem;
import net.minecraft.block.NpcBlockHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.controllers.BlockData;
import noppes.npcs.controllers.MassBlockController;
import noppes.npcs.entity.EntityNPCInterface;

public class JobFarmer extends JobInterface implements MassBlockController.IMassBlock {
	public int chestMode;
	private List<BlockPos> trackedBlocks;
	private int ticks;
	private int walkTicks;
	private int blockTicks;
	private boolean waitingForBlocks;
	private BlockPos ripe;
	private BlockPos chest;
	private ItemStack holding;

	public JobFarmer(final EntityNPCInterface npc) {
		super(npc);
		chestMode = 1;
		trackedBlocks = new ArrayList<BlockPos>();
		ticks = 0;
		walkTicks = 0;
		blockTicks = 800;
		waitingForBlocks = false;
		ripe = null;
		chest = null;
		holding = null;
		overrideMainHand = true;
	}

	@Override
	public boolean aiContinueExecute() {
		return false;
	}

	@Override
	public boolean aiShouldExecute() {
		if (holding != null) {
			if (chestMode == 0) {
				setHolding(null);
			} else if (chestMode == 1) {
				if (chest == null) {
					dropItem(holding);
					setHolding(null);
				} else {
					chest();
				}
			} else if (chestMode == 2) {
				dropItem(holding);
				setHolding(null);
			}
			return false;
		}
		if (ripe != null) {
			pluck();
			return false;
		}
		if (!waitingForBlocks && (blockTicks++ > 1200)) {
			blockTicks = 0;
			waitingForBlocks = true;
			MassBlockController.Queue(this);
		}
		if (ticks++ < 100) {
			return false;
		}
		ticks = 0;
		return true;
	}

	@Override
	public void aiUpdateTask() {
		final Iterator<BlockPos> ite = trackedBlocks.iterator();
		while (ite.hasNext() && (ripe == null)) {
			final BlockPos pos = ite.next();
			IBlockState state = npc.worldObj.getBlockState(pos);
			final Block b = state.getBlock();
			if (b instanceof BlockCrops) {
				if (state.getValue(BlockCrops.AGE) < 7) {
					continue;
				}
				ripe = pos;
			} else if (b instanceof BlockStem) {
				state = b.getActualState(state, npc.worldObj, pos);
				final EnumFacing facing = state.getValue(BlockStem.FACING);
				if (facing == EnumFacing.UP) {
					continue;
				}
				ripe = pos;
			} else {
				ite.remove();
			}
		}
		npc.ai.returnToStart = (ripe == null);
		if (ripe != null) {
			npc.getNavigator().clearPathEntity();
			npc.getLookHelper().setLookPosition(ripe.getX(), ripe.getY(), ripe.getZ(), 10.0f,
					npc.getVerticalFaceSpeed());
		}
	}

	private void chest() {
		final BlockPos pos = chest;
		npc.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.0);
		npc.getLookHelper().setLookPosition(pos.getX(), pos.getY(), pos.getZ(), 10.0f, npc.getVerticalFaceSpeed());
		if (npc.nearPosition(pos) || (walkTicks++ > 400)) {
			if (walkTicks < 400) {
				npc.swingItem();
			}
			npc.getNavigator().clearPathEntity();
			ticks = 100;
			walkTicks = 0;
			final IBlockState state = npc.worldObj.getBlockState(pos);
			if (state.getBlock() instanceof BlockChest) {
				final TileEntityChest tile = (TileEntityChest) npc.worldObj.getTileEntity(pos);
				for (int i = 0; (holding != null) && (i < tile.getSizeInventory()); ++i) {
					holding = mergeStack(tile, i, holding);
				}
				for (int i = 0; (holding != null) && (i < tile.getSizeInventory()); ++i) {
					final ItemStack item = tile.getStackInSlot(i);
					if (item == null) {
						tile.setInventorySlotContents(i, holding);
						holding = null;
					}
				}
				if (holding != null) {
					dropItem(holding);
					holding = null;
				}
			} else {
				chest = null;
			}
			setHolding(holding);
		}
	}

	private void dropItem(final ItemStack item) {
		final EntityItem entityitem = new EntityItem(npc.worldObj, npc.posX, npc.posY, npc.posZ, item);
		entityitem.setDefaultPickupDelay();
		npc.worldObj.spawnEntityInWorld(entityitem);
	}

	@Override
	public IItemStack getMainhand() {
		final String name = npc.getDataWatcher().getWatchableObjectString(17);
		final ItemStack item = stringToItem(name);
		if (item == null) {
			return npc.inventory.weapons.get(0);
		}
		return new ItemStackWrapper(item);
	}

	@Override
	public int getMutexBits() {
		return npc.getNavigator().noPath() ? 0 : AiMutex.LOOK;
	}

	@Override
	public EntityNPCInterface getNpc() {
		return npc;
	}

	@Override
	public int getRange() {
		return 16;
	}

	private ItemStack mergeStack(final IInventory inventory, final int slot, final ItemStack item) {
		final ItemStack item2 = inventory.getStackInSlot(slot);
		if (!NoppesUtilPlayer.compareItems(item, item2, false, false)) {
			return item;
		}
		final int size = item2.getMaxStackSize() - item2.stackSize;
		if (size >= item.stackSize) {
			final ItemStack itemStack = item2;
			itemStack.stackSize += item.stackSize;
			return null;
		}
		item2.stackSize = item2.getMaxStackSize();
		item.stackSize -= size;
		if ((item == null) || (item.stackSize <= 0)) {
			return null;
		}
		return item;
	}

	private void pluck() {
		BlockPos pos = ripe;
		npc.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.0);
		npc.getLookHelper().setLookPosition(pos.getX(), pos.getY(), pos.getZ(), 10.0f, npc.getVerticalFaceSpeed());
		if (npc.nearPosition(pos) || (walkTicks++ > 400)) {
			if (walkTicks > 400) {
				pos = NoppesUtilServer.GetClosePos(pos, npc.worldObj);
				npc.setPositionAndUpdate(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			}
			ripe = null;
			npc.getNavigator().clearPathEntity();
			ticks = 90;
			walkTicks = 0;
			npc.swingItem();
			IBlockState state = npc.worldObj.getBlockState(pos);
			Block b = state.getBlock();
			if (b instanceof BlockCrops) {
				npc.worldObj.setBlockState(pos, state.withProperty(BlockCrops.AGE, 0));
				holding = new ItemStack(NpcBlockHelper.GetCrop((BlockCrops) b));
			}
			if (b instanceof BlockStem) {
				state = b.getActualState(state, npc.worldObj, pos);
				final EnumFacing facing = state.getValue(BlockStem.FACING);
				if ((facing == EnumFacing.UP) || (facing == EnumFacing.DOWN)) {
					return;
				}
				pos = pos.add(facing.getDirectionVec());
				b = npc.worldObj.getBlockState(pos).getBlock();
				npc.worldObj.setBlockToAir(pos);
				if (b != Blocks.air) {
					holding = new ItemStack(b);
				}
			}
			setHolding(holding);
		}
	}

	@Override
	public void processed(final List<BlockData> list) {
		final List<BlockPos> trackedBlocks = new ArrayList<BlockPos>();
		BlockPos chest = null;
		for (final BlockData data : list) {
			final Block b = data.state.getBlock();
			if (b instanceof BlockChest) {
				if ((chest != null) && (npc.getDistanceSq(chest) <= npc.getDistanceSq(data.pos))) {
					continue;
				}
				chest = data.pos;
			} else {
				if (!(b instanceof BlockCrops) && !(b instanceof BlockStem)) {
					continue;
				}
				if (trackedBlocks.contains(data.pos)) {
					continue;
				}
				trackedBlocks.add(data.pos);
			}
		}
		this.chest = chest;
		this.trackedBlocks = trackedBlocks;
		waitingForBlocks = false;
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		chestMode = compound.getInteger("JobChestMode");
		if (compound.hasKey("JobHolding")) {
			holding = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("JobHolding"));
		}
		blockTicks = 1100;
	}

	public void setHolding(final ItemStack item) {
		holding = item;
		if (holding == null) {
			npc.getDataWatcher().updateObject(17, "");
		} else {
			npc.getDataWatcher().updateObject(17, itemToString(holding));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		compound.setInteger("JobChestMode", chestMode);
		if (holding != null) {
			compound.setTag("JobHolding", holding.writeToNBT(new NBTTagCompound()));
		}
		return compound;
	}
}
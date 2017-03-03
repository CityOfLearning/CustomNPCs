
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
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.controllers.BlockData;
import noppes.npcs.controllers.MassBlockController;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.NoppesUtilPlayer;
import noppes.npcs.util.NoppesUtilServer;

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

	public JobFarmer(EntityNPCInterface npc) {
		super(npc);
		chestMode = 1;
		trackedBlocks = new ArrayList<>();
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
		Iterator<BlockPos> ite = trackedBlocks.iterator();
		while (ite.hasNext() && (ripe == null)) {
			BlockPos pos = ite.next();
			IBlockState state = npc.worldObj.getBlockState(pos);
			Block b = state.getBlock();
			if (b instanceof BlockCrops) {
				if (state.getValue(BlockCrops.AGE) < 7) {
					continue;
				}
				ripe = pos;
			} else if (b instanceof BlockStem) {
				state = b.getActualState(state, npc.worldObj, pos);
				EnumFacing facing = state.getValue(BlockStem.FACING);
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
		BlockPos pos = chest;
		npc.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.0);
		npc.getLookHelper().setLookPosition(pos.getX(), pos.getY(), pos.getZ(), 10.0f, npc.getVerticalFaceSpeed());
		if (npc.nearPosition(pos) || (walkTicks++ > 400)) {
			if (walkTicks < 400) {
				npc.swingItem();
			}
			npc.getNavigator().clearPathEntity();
			ticks = 100;
			walkTicks = 0;
			IBlockState state = npc.worldObj.getBlockState(pos);
			if (state.getBlock() instanceof BlockChest) {
				TileEntityChest tile = (TileEntityChest) npc.worldObj.getTileEntity(pos);
				for (int i = 0; (holding != null) && (i < tile.getSizeInventory()); ++i) {
					holding = mergeStack(tile, i, holding);
				}
				for (int i = 0; (holding != null) && (i < tile.getSizeInventory()); ++i) {
					ItemStack item = tile.getStackInSlot(i);
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

	private void dropItem(ItemStack item) {
		EntityItem entityitem = new EntityItem(npc.worldObj, npc.posX, npc.posY, npc.posZ, item);
		entityitem.setDefaultPickupDelay();
		npc.worldObj.spawnEntityInWorld(entityitem);
	}

	@Override
	public IItemStack getMainhand() {
		String name = npc.getDataWatcher().getWatchableObjectString(17);
		ItemStack item = stringToItem(name);
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

	private ItemStack mergeStack(IInventory inventory, int slot, ItemStack item) {
		ItemStack item2 = inventory.getStackInSlot(slot);
		if (!NoppesUtilPlayer.compareItems(item, item2, false, false)) {
			return item;
		}
		int size = item2.getMaxStackSize() - item2.stackSize;
		if (size >= item.stackSize) {
			ItemStack itemStack = item2;
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
				EnumFacing facing = state.getValue(BlockStem.FACING);
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
	public void processed(List<BlockData> list) {
		List<BlockPos> trackedBlocks = new ArrayList<>();
		BlockPos chest = null;
		for (BlockData data : list) {
			Block b = data.state.getBlock();
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
	public void readFromNBT(NBTTagCompound compound) {
		chestMode = compound.getInteger("JobChestMode");
		if (compound.hasKey("JobHolding")) {
			holding = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("JobHolding"));
		}
		blockTicks = 1100;
	}

	public void setHolding(ItemStack item) {
		holding = item;
		if (holding == null) {
			npc.getDataWatcher().updateObject(17, "");
		} else {
			npc.getDataWatcher().updateObject(17, itemToString(holding));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("JobChestMode", chestMode);
		if (holding != null) {
			compound.setTag("JobHolding", holding.writeToNBT(new NBTTagCompound()));
		}
		return compound;
	}
}

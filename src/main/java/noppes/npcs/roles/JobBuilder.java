//

//

package noppes.npcs.roles;

import java.util.Stack;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.blocks.tiles.TileBuilder;
import noppes.npcs.controllers.BlockData;
import noppes.npcs.entity.EntityNPCInterface;

public class JobBuilder extends JobInterface {
	public TileBuilder build;
	private BlockPos possibleBuildPos;
	private Stack<BlockData> placingList;
	private BlockData placing;
	private int tryTicks;
	private int ticks;

	public JobBuilder(final EntityNPCInterface npc) {
		super(npc);
		build = null;
		possibleBuildPos = null;
		placingList = null;
		placing = null;
		tryTicks = 0;
		ticks = 0;
		overrideMainHand = true;
	}

	@Override
	public boolean aiShouldExecute() {
		if (possibleBuildPos != null) {
			final TileEntity tile = npc.worldObj.getTileEntity(possibleBuildPos);
			if (tile instanceof TileBuilder) {
				build = (TileBuilder) tile;
			} else {
				placingList.clear();
			}
			possibleBuildPos = null;
		}
		return build != null;
	}

	@Override
	public void aiStartExecuting() {
		npc.ai.returnToStart = false;
	}

	@Override
	public void aiUpdateTask() {
		if ((build.finished && (placingList == null)) || !build.enabled || build.isInvalid()) {
			build = null;
			npc.getNavigator().tryMoveToXYZ(npc.getStartXPos(), npc.getStartYPos(), npc.getStartZPos(), 1.0);
			return;
		}
		if (ticks++ < 10) {
			return;
		}
		ticks = 0;
		if (((placingList == null) || placingList.isEmpty()) && (placing == null)) {
			placingList = build.getBlock();
			npc.getDataWatcher().updateObject(17, "");
			return;
		}
		if (placing == null) {
			placing = placingList.pop();
			tryTicks = 0;
			npc.getDataWatcher().updateObject(17, blockToString(placing));
		}
		npc.getNavigator().tryMoveToXYZ(placing.pos.getX(), placing.pos.getY() + 1, placing.pos.getZ(), 1.0);
		if ((tryTicks++ > 40) || npc.nearPosition(placing.pos)) {
			BlockPos blockPos = placing.pos;
			placeBlock();
			if (tryTicks > 40) {
				blockPos = NoppesUtilServer.GetClosePos(blockPos, npc.worldObj);
				npc.setPositionAndUpdate(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
			}
		}
	}

	private String blockToString(final BlockData data) {
		if (data.state.getBlock() == Blocks.air) {
			return "minecraft:iron_pickaxe";
		}
		final String name = itemToString(data.getStack());
		if (name == null) {
			return "";
		}
		return name;
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

	public void placeBlock() {
		if (placing == null) {
			return;
		}
		npc.getNavigator().clearPathEntity();
		npc.swingItem();
		npc.worldObj.setBlockState(placing.pos, placing.state, 2);
		if ((placing.state.getBlock() instanceof ITileEntityProvider) && (placing.tile != null)) {
			final TileEntity tile = npc.worldObj.getTileEntity(placing.pos);
			if (tile != null) {
				tile.readFromNBT(placing.tile);
			}
		}
		placing = null;
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		if (compound.hasKey("BuildX")) {
			possibleBuildPos = new BlockPos(compound.getInteger("BuildX"), compound.getInteger("BuildY"),
					compound.getInteger("BuildZ"));
		}
		if ((possibleBuildPos != null) && compound.hasKey("Placing")) {
			final Stack<BlockData> placing = new Stack<BlockData>();
			final NBTTagList list = compound.getTagList("Placing", 10);
			for (int i = 0; i < list.tagCount(); ++i) {
				final BlockData data = BlockData.getData(list.getCompoundTagAt(i));
				if (data != null) {
					placing.add(data);
				}
			}
			placingList = placing;
		}
		npc.ai.doorInteract = 1;
	}

	@Override
	public void reset() {
		build = null;
		npc.ai.returnToStart = true;
		npc.getDataWatcher().updateObject(17, "");
	}

	@Override
	public void resetTask() {
		reset();
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		if (build != null) {
			compound.setInteger("BuildX", build.getPos().getX());
			compound.setInteger("BuildY", build.getPos().getY());
			compound.setInteger("BuildZ", build.getPos().getZ());
			if ((placingList != null) && !placingList.isEmpty()) {
				final NBTTagList list = new NBTTagList();
				for (final BlockData data : placingList) {
					list.appendTag(data.getNBT());
				}
				if (placing != null) {
					list.appendTag(placing.getNBT());
				}
				compound.setTag("Placing", list);
			}
		}
		return compound;
	}
}

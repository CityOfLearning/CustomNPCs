//

//

package noppes.npcs.ai;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIOpenAnyDoor extends EntityAIBase {
	private EntityNPCInterface npc;
	private BlockPos position;
	private Block door;
	private IProperty property;
	private boolean hasStoppedDoorInteraction;
	private float entityPositionX;
	private float entityPositionZ;
	private int closeDoorTemporisation;

	public EntityAIOpenAnyDoor(final EntityNPCInterface npc) {
		this.npc = npc;
	}

	@Override
	public boolean continueExecuting() {
		return (closeDoorTemporisation > 0) && !hasStoppedDoorInteraction;
	}

	public Block getDoor(final BlockPos pos) {
		final IBlockState state = npc.worldObj.getBlockState(pos);
		final Block block = state.getBlock();
		if (block.isFullBlock() || (block == Blocks.iron_door)) {
			return null;
		}
		if (block instanceof BlockDoor) {
			return block;
		}
		final Set<IProperty> set = state.getProperties().keySet();
		for (final IProperty prop : set) {
			if ((prop instanceof PropertyBool) && prop.getName().equals("open")) {
				property = prop;
				return block;
			}
		}
		return null;
	}

	@Override
	public void resetTask() {
		setDoorState(door, position, false);
	}

	public void setDoorState(final Block block, final BlockPos position, final boolean open) {
		if (block instanceof BlockDoor) {
			((BlockDoor) block).toggleDoor(npc.worldObj, position, open);
		} else {
			final IBlockState state = npc.worldObj.getBlockState(position);
			if (state.getBlock() != block) {
				return;
			}
			npc.worldObj.setBlockState(position, state.withProperty(property, open));
			npc.worldObj.playAuxSFXAtEntity((EntityPlayer) null, open ? 1003 : 1006, position, 0);
		}
	}

	@Override
	public boolean shouldExecute() {
		if (!npc.isCollidedHorizontally) {
			return false;
		}
		final PathEntity pathentity = npc.getNavigator().getPath();
		if ((pathentity != null) && !pathentity.isFinished()) {
			for (int i = 0; i < Math.min(pathentity.getCurrentPathIndex() + 2,
					pathentity.getCurrentPathLength()); ++i) {
				final PathPoint pathpoint = pathentity.getPathPointFromIndex(i);
				position = new BlockPos(pathpoint.xCoord, pathpoint.yCoord + 1, pathpoint.zCoord);
				if (npc.getDistanceSq(position.getX(), npc.posY, position.getZ()) <= 2.25) {
					door = getDoor(position);
					if (door != null) {
						return true;
					}
				}
			}
			position = new BlockPos(npc).up();
			door = getDoor(position);
			return door != null;
		}
		return false;
	}

	@Override
	public void startExecuting() {
		hasStoppedDoorInteraction = false;
		entityPositionX = (float) ((position.getX() + 0.5f) - npc.posX);
		entityPositionZ = (float) ((position.getZ() + 0.5f) - npc.posZ);
		closeDoorTemporisation = 20;
		setDoorState(door, position, true);
	}

	@Override
	public void updateTask() {
		--closeDoorTemporisation;
		final float f = (float) ((position.getX() + 0.5f) - npc.posX);
		final float f2 = (float) ((position.getZ() + 0.5f) - npc.posZ);
		final float f3 = (entityPositionX * f) + (entityPositionZ * f2);
		if (f3 < 0.0f) {
			hasStoppedDoorInteraction = true;
		}
	}
}

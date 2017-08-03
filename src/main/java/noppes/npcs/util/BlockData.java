
package noppes.npcs.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

public class BlockData {
	public static BlockData readFromNBT(NBTTagCompound compound) {
		BlockPos pos = BlockPos.fromLong(compound.getLong("pos"));
		Block b = Block.getBlockFromName(compound.getString("Block"));
		if (b == null) {
			return null;
		}
		IBlockState state = b.getStateFromMeta(compound.getShort("Meta"));
		NBTTagCompound tile = null;
		if (compound.hasKey("Tile")) {
			tile = compound.getCompoundTag("Tile");
		}
		return new BlockData(pos, state, tile);
	}

	public BlockPos pos;
	public IBlockState state;
	public NBTTagCompound tile;

	private ItemStack stack;

	public BlockData(BlockPos pos, IBlockState state, NBTTagCompound tile) {
		this.pos = pos;
		this.state = state;
		this.tile = tile;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {		
		compound.setLong("pos", pos.toLong());
		compound.setString("Block", Block.blockRegistry.getNameForObject(state.getBlock()).toString());
		compound.setShort("Meta", (short) state.getBlock().getMetaFromState(state));
		if (tile != null) {
			compound.setTag("Tile", tile);
		}
		return compound;
	}

	public ItemStack getStack() {
		if (stack == null) {
			stack = new ItemStack(state.getBlock(), 1, state.getBlock().damageDropped(state));
		}
		return stack;
	}
}

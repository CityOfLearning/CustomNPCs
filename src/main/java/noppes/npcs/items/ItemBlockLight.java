//

//

package noppes.npcs.items;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import noppes.npcs.blocks.BlockLightable;

public class ItemBlockLight extends ItemBlock {
	public ItemBlockLight(final Block block) {
		super(block);
	}

	@Override
	public boolean placeBlockAt(final ItemStack stack, final EntityPlayer player, final World world, final BlockPos pos,
			final EnumFacing side, final float hitX, final float hitY, final float hitZ, final IBlockState newState) {
		final boolean bo = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
		if (bo) {
			final IBlockState state = world.getBlockState(pos);
			((BlockLightable) block).onPostBlockPlaced(world, pos, state, player, stack, side);
		}
		return bo;
	}
}

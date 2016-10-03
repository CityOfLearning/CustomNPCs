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
	public ItemBlockLight(Block block) {
		super(block);
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ, IBlockState newState) {
		boolean bo = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
		if (bo) {
			IBlockState state = world.getBlockState(pos);
			((BlockLightable) block).onPostBlockPlaced(world, pos, state, player, stack, side);
		}
		return bo;
	}
}

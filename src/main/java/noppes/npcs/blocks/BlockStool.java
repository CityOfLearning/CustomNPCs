//

//

package noppes.npcs.blocks;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import noppes.npcs.blocks.tiles.TileStool;

public class BlockStool extends BlockRotated {
	public BlockStool() {
		super(Blocks.planks);
		setBlockBounds(0.1f, 0.0f, 0.1f, 0.9f, 0.6f, 0.9f);
	}

	@Override
	public TileEntity createNewTileEntity(final World var1, final int var2) {
		return new TileStool();
	}

	@Override
	public int damageDropped(final IBlockState state) {
		return state.getValue(BlockRotated.DAMAGE);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(final World world, final BlockPos pos, final IBlockState state) {
		return new AxisAlignedBB(pos.getX() + 0.1f, pos.getY(), pos.getZ() + 0.1f, pos.getX() + 0.9f, pos.getY() + 0.5f,
				pos.getZ() + 0.9f);
	}

	@Override
	public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
		par3List.add(new ItemStack(par1, 1, 2));
		par3List.add(new ItemStack(par1, 1, 3));
		par3List.add(new ItemStack(par1, 1, 4));
		par3List.add(new ItemStack(par1, 1, 5));
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		return BlockChair.MountBlock(world, pos, player);
	}

	@Override
	public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state,
			final EntityLivingBase entity, final ItemStack stack) {
		world.setBlockState(pos, state.withProperty(BlockRotated.DAMAGE, stack.getItemDamage()), 2);
		super.onBlockPlacedBy(world, pos, state, entity, stack);
	}
}

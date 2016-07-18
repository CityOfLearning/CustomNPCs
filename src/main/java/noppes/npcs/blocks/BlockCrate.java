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
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import noppes.npcs.CustomNpcs;
import noppes.npcs.blocks.tiles.TileCrate;
import noppes.npcs.blocks.tiles.TileNpcContainer;
import noppes.npcs.constants.EnumGuiType;

public class BlockCrate extends BlockRotated {
	public BlockCrate() {
		super(Blocks.planks);
	}

	@Override
	public void breakBlock(final World world, final BlockPos pos, final IBlockState state) {
		final TileNpcContainer tile = (TileNpcContainer) world.getTileEntity(pos);
		if (tile == null) {
			return;
		}
		tile.dropItems(world, pos);
		world.updateComparatorOutputLevel(pos, state.getBlock());
		super.breakBlock(world, pos, state);
	}

	@Override
	public TileEntity createNewTileEntity(final World var1, final int var2) {
		return new TileCrate();
	}

	@Override
	public int damageDropped(final IBlockState state) {
		return state.getValue(BlockRotated.DAMAGE);
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
	public boolean onBlockActivated(final World par1World, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		if (par1World.isRemote) {
			return true;
		}
		par1World.playSoundEffect(pos.getX(), pos.getY() + 0.5, pos.getZ(), "random.chestopen", 0.5f,
				(par1World.rand.nextFloat() * 0.1f) + 0.9f);
		player.openGui(CustomNpcs.instance, EnumGuiType.Crate.ordinal(), par1World, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state,
			final EntityLivingBase entity, final ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, entity, stack);
		world.setBlockState(pos, state.withProperty(BlockRotated.DAMAGE, stack.getItemDamage()), 2);
	}
}

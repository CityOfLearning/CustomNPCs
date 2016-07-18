//

//

package noppes.npcs.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockPlaceholder extends Block {
	public static final PropertyInteger DAMAGE;

	static {
		DAMAGE = PropertyInteger.create("damage", 0, 15);
	}

	public BlockPlaceholder() {
		super(Material.rock);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { BlockPlaceholder.DAMAGE });
	}

	@Override
	public int damageDropped(final IBlockState state) {
		return state.getValue(BlockPlaceholder.DAMAGE);
	}

	@Override
	public int getMetaFromState(final IBlockState state) {
		return damageDropped(state);
	}

	@Override
	public IBlockState getStateFromMeta(final int meta) {
		return getDefaultState().withProperty(BlockPlaceholder.DAMAGE, meta);
	}

	@Override
	public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
		for (int i = 0; i < 16; ++i) {
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	@Override
	public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state,
			final EntityLivingBase entity, final ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, entity, stack);
		world.setBlockState(pos, state.withProperty(BlockPlaceholder.DAMAGE, stack.getItemDamage()), 2);
	}
}

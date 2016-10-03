package noppes.npcs.blocks;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.blocks.tiles.TileBeam;
import noppes.npcs.blocks.tiles.TileColorable;

public class BlockBeam extends BlockRotated {
	public BlockBeam() {
		super(Blocks.planks);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileBeam();
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(DAMAGE).intValue();
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
		par3List.add(new ItemStack(par1, 1, 2));
		par3List.add(new ItemStack(par1, 1, 3));
		par3List.add(new ItemStack(par1, 1, 4));
		par3List.add(new ItemStack(par1, 1, 5));
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity,
			ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, entity, stack);
		world.setBlockState(pos, state.withProperty(DAMAGE, Integer.valueOf(stack.getItemDamage())), 2);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
		TileEntity tileentity = world.getTileEntity(pos);
		if (!(tileentity instanceof TileColorable)) {
			super.setBlockBoundsBasedOnState(world, pos);
			return;
		}
		TileColorable tile = (TileColorable) tileentity;
		if (tile.rotation == 0) {
			setBlockBounds(0.33F, 0.33F, 0.25F, 0.67F, 0.67F, 1.0F);
		} else if (tile.rotation == 2) {
			setBlockBounds(0.33F, 0.33F, 0.0F, 0.67F, 0.67F, 0.75F);
		} else if (tile.rotation == 3) {
			setBlockBounds(0.25F, 0.33F, 0.33F, 1.0F, 0.67F, 0.67F);
		} else if (tile.rotation == 1) {
			setBlockBounds(0.0F, 0.33F, 0.33F, 0.75F, 0.67F, 0.67F);
		}
	}
}

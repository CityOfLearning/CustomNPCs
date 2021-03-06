package noppes.npcs.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
		super(Material.wood);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileNpcContainer tile = (TileNpcContainer) world.getTileEntity(pos);
		if (tile == null) {
			return;
		}
		tile.dropItems(world, pos);

		world.updateComparatorOutputLevel(pos, state.getBlock());
		super.breakBlock(world, pos, state);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileCrate();
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
	public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (par1World.isRemote) {
			return true;
		}
		par1World.playSoundEffect(pos.getX(), pos.getY() + 0.5D, pos.getZ(), "random.chestopen", 0.5F,
				(par1World.rand.nextFloat() * 0.1F) + 0.9F);
		player.openGui(CustomNpcs.instance, EnumGuiType.Crate.ordinal(), par1World, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity,
			ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, entity, stack);
		world.setBlockState(pos, state.withProperty(DAMAGE, Integer.valueOf(stack.getItemDamage())), 2);
	}
}

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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import noppes.npcs.blocks.tiles.TileChair;
import noppes.npcs.entity.EntityChairMount;

public class BlockChair extends BlockRotated {

	public BlockChair() {
		super(Material.wood);
		setBlockBounds(0.1F, 0.0F, 0.1F, 0.9F, 1.0F, 0.9F);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileChair();
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		((TileChair) world.getTileEntity(pos)).killMount();
		world.removeTileEntity(pos);
		super.breakBlock(world, pos, state);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(DAMAGE).intValue();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
		return new AxisAlignedBB(pos.getX() + 0.1F, pos.getY(), pos.getZ() + 0.1F, pos.getX() + 0.9F, pos.getY() + 0.5F,
				pos.getZ() + 0.9F);
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}
		((TileChair) world.getTileEntity(pos)).mount(world, pos, player);
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity,
			ItemStack stack) {
		world.setBlockState(pos, state.withProperty(DAMAGE, Integer.valueOf(stack.getItemDamage())), 2);
		super.onBlockPlacedBy(world, pos, state, entity, stack);
		TileChair tile = ((TileChair) world.getTileEntity(pos));
		EntityChairMount mount = new EntityChairMount(world, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5D);
		mount.rotationYaw = ((2 + tile.getRotation()) % 4) * 90;
		world.spawnEntityInWorld(mount);
		tile.setMount(mount);
	}
}

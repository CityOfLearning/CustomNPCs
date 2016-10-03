package noppes.npcs.blocks;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
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
import noppes.npcs.blocks.tiles.TileChair;
import noppes.npcs.entity.EntityChairMount;
import noppes.npcs.entity.EntityCustomNpc;

public class BlockChair extends BlockRotated {
	public static boolean MountBlock(World world, BlockPos pos, EntityPlayer player) {
		if (world.isRemote) {
			return true;
		}
		List<Entity> list = world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.fromBounds(pos.getX(), pos.getY(),
				pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1));
		for (Entity entity : list) {
			if (((entity instanceof EntityChairMount)) || ((entity instanceof EntityCustomNpc))) {
				return false;
			}
		}
		EntityChairMount mount = new EntityChairMount(world);
		mount.setPosition(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5D);
		world.spawnEntityInWorld(mount);
		player.mountEntity(mount);
		return true;
	}

	public BlockChair() {
		super(Blocks.planks);
		setBlockBounds(0.1F, 0.0F, 0.1F, 0.9F, 1.0F, 0.9F);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileChair();
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
		return MountBlock(world, pos, player);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity,
			ItemStack stack) {
		world.setBlockState(pos, state.withProperty(DAMAGE, Integer.valueOf(stack.getItemDamage())), 2);
		super.onBlockPlacedBy(world, pos, state, entity, stack);
	}
}

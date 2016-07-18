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
import net.minecraft.item.ItemSword;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.blocks.tiles.TileNpcContainer;
import noppes.npcs.blocks.tiles.TilePedestal;

public class BlockPedestal extends BlockTrigger {
	public BlockPedestal() {
		super(Blocks.stone);
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
		return new TilePedestal();
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
	}

	@Override
	public boolean onBlockActivated(final World par1World, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		if (par1World.isRemote) {
			return true;
		}
		final TilePedestal tile = (TilePedestal) par1World.getTileEntity(pos);
		final ItemStack item = player.getCurrentEquippedItem();
		final ItemStack weapon = tile.getStackInSlot(0);
		if ((item == null) && (weapon != null)) {
			tile.setInventorySlotContents(0, null);
			player.inventory.setInventorySlotContents(player.inventory.currentItem, weapon);
			par1World.markBlockForUpdate(pos);
			updateSurrounding(par1World, pos);
		} else {
			if ((item == null) || (item.getItem() == null) || !(item.getItem() instanceof ItemSword)) {
				return true;
			}
			if ((item != null) && (weapon == null)) {
				tile.setInventorySlotContents(0, item);
				player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack) null);
				par1World.markBlockForUpdate(pos);
				updateSurrounding(par1World, pos);
			}
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state,
			final EntityLivingBase entity, final ItemStack stack) {
		world.setBlockState(pos, state.withProperty(BlockRotated.DAMAGE, stack.getItemDamage()), 2);
		super.onBlockPlacedBy(world, pos, state, entity, stack);
	}

	@Override
	public void setBlockBoundsBasedOnState(final IBlockAccess world, final BlockPos pos) {
		final TileEntity tileentity = world.getTileEntity(pos);
		if (!(tileentity instanceof TileColorable)) {
			super.setBlockBoundsBasedOnState(world, pos);
			return;
		}
		final TileColorable tile = (TileColorable) tileentity;
		if ((tile.rotation % 2) == 0) {
			setBlockBounds(0.0f, 0.0f, 0.2f, 1.0f, 0.5f, 0.8f);
		} else {
			setBlockBounds(0.2f, 0.0f, 0.0f, 0.8f, 0.5f, 1.0f);
		}
	}
}

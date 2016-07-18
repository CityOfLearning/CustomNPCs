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
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.blocks.tiles.TileBanner;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.blocks.tiles.TileSign;

public class BlockSign extends BlockRotated {
	public BlockSign() {
		super(Blocks.planks);
	}

	@Override
	public TileEntity createNewTileEntity(final World var1, final int var2) {
		return new TileSign();
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
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		final TileBanner tile = (TileBanner) world.getTileEntity(pos);
		return tile.canEdit();
	}

	@Override
	public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state,
			final EntityLivingBase entity, final ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, entity, stack);
		final TileSign tile = (TileSign) world.getTileEntity(pos);
		tile.time = System.currentTimeMillis();
		world.setBlockState(pos, state.withProperty(BlockRotated.DAMAGE, stack.getItemDamage()), 2);
		if ((entity instanceof EntityPlayer) && world.isRemote) {
			((EntityPlayer) entity)
					.addChatComponentMessage(new ChatComponentTranslation("availability.editIcon", new Object[0]));
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(final IBlockAccess world, final BlockPos pos) {
		final TileEntity tileentity = world.getTileEntity(pos);
		if (!(tileentity instanceof TileColorable)) {
			super.setBlockBoundsBasedOnState(world, pos);
			return;
		}
		final TileColorable tile = (TileColorable) tileentity;
		if ((tile.rotation % 2) == 1) {
			setBlockBounds(0.0f, 0.3f, 0.3f, 1.0f, 1.0f, 0.7f);
		} else {
			setBlockBounds(0.3f, 0.3f, 0.0f, 0.7f, 1.0f, 1.0f);
		}
	}
}

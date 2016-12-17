package noppes.npcs.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
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
		super(Material.wood);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileSign();
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		TileBanner tile = (TileBanner) world.getTileEntity(pos);
		return tile.canEdit();
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity,
			ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, entity, stack);
		TileSign tile = (TileSign) world.getTileEntity(pos);
		tile.setTime(System.currentTimeMillis());
		world.setBlockState(pos, state.withProperty(DAMAGE, Integer.valueOf(stack.getItemDamage())), 2);
		if (((entity instanceof EntityPlayer)) && (world.isRemote)) {
			((EntityPlayer) entity)
					.addChatComponentMessage(new ChatComponentTranslation("availability.editIcon", new Object[0]));
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
		TileEntity tileentity = world.getTileEntity(pos);
		if (!(tileentity instanceof TileColorable)) {
			super.setBlockBoundsBasedOnState(world, pos);
			return;
		}
		TileColorable tile = (TileColorable) tileentity;
		if ((tile.getRotation() % 2) == 1) {
			setBlockBounds(0.0F, 0.3F, 0.3F, 1.0F, 1.0F, 0.7F);
		} else {
			setBlockBounds(0.3F, 0.3F, 0.0F, 0.7F, 1.0F, 1.0F);
		}
	}
}

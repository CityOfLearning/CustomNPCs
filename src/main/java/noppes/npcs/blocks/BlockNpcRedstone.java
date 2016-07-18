//

//

package noppes.npcs.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileRedstoneBlock;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.util.IPermission;

public class BlockNpcRedstone extends BlockContainer implements IPermission {
	public static final PropertyBool ACTIVE;

	static {
		ACTIVE = PropertyBool.create("active");
	}

	public BlockNpcRedstone() {
		super(Material.rock);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { BlockNpcRedstone.ACTIVE });
	}

	@Override
	public TileEntity createNewTileEntity(final World var1, final int var2) {
		return new TileRedstoneBlock();
	}

	@Override
	public int getMetaFromState(final IBlockState state) {
		return (state.getValue(BlockNpcRedstone.ACTIVE)) ? 1 : 0;
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	public IBlockState getStateFromMeta(final int meta) {
		return getDefaultState().withProperty(BlockNpcRedstone.ACTIVE, false);
	}

	@Override
	public int getStrongPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state,
			final EnumFacing side) {
		return isActivated(state);
	}

	@Override
	public int getWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state,
			final EnumFacing side) {
		return isActivated(state);
	}

	public int isActivated(final IBlockState state) {
		return state.getValue(BlockNpcRedstone.ACTIVE) ? 15 : 0;
	}

	@Override
	public boolean isAllowed(final EnumPacketServer e) {
		return e == EnumPacketServer.SaveTileEntity;
	}

	@Override
	public boolean onBlockActivated(final World par1World, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		if (par1World.isRemote) {
			return false;
		}
		final ItemStack currentItem = player.inventory.getCurrentItem();
		if ((currentItem != null) && (currentItem.getItem() == CustomItems.wand)
				&& CustomNpcsPermissions.hasPermission(player, CustomNpcsPermissions.EDIT_BLOCKS)) {
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.RedstoneBlock, null, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return false;
	}

	@Override
	public void onBlockAdded(final World par1World, final BlockPos pos, final IBlockState state) {
		par1World.notifyNeighborsOfStateChange(pos, this);
		par1World.notifyNeighborsOfStateChange(pos.down(), this);
		par1World.notifyNeighborsOfStateChange(pos.up(), this);
		par1World.notifyNeighborsOfStateChange(pos.west(), this);
		par1World.notifyNeighborsOfStateChange(pos.east(), this);
		par1World.notifyNeighborsOfStateChange(pos.south(), this);
		par1World.notifyNeighborsOfStateChange(pos.north(), this);
	}

	@Override
	public void onBlockDestroyedByPlayer(final World par1World, final BlockPos pos, final IBlockState state) {
		onBlockAdded(par1World, pos, state);
	}

	@Override
	public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state,
			final EntityLivingBase entityliving, final ItemStack item) {
		if ((entityliving instanceof EntityPlayer) && !world.isRemote) {
			NoppesUtilServer.sendOpenGui((EntityPlayer) entityliving, EnumGuiType.RedstoneBlock, null, pos.getX(),
					pos.getY(), pos.getZ());
		}
	}
}

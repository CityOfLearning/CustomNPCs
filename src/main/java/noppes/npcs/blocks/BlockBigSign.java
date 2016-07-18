//

//

package noppes.npcs.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileBigSign;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.client.renderer.ITileRenderer;
import noppes.npcs.constants.EnumGuiType;

public class BlockBigSign extends BlockContainer implements ITileRenderer {
	public int renderId;
	private TileEntity renderTile;

	public BlockBigSign() {
		super(Material.wood);
		renderId = -1;
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { BlockRotated.DAMAGE });
	}

	@Override
	public TileEntity createNewTileEntity(final World var1, final int var2) {
		return new TileBigSign();
	}

	@Override
	public int damageDropped(final IBlockState state) {
		return state.getValue(BlockRotated.DAMAGE);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(final World world, final BlockPos pos, final IBlockState state) {
		return null;
	}

	@Override
	public int getMetaFromState(final IBlockState state) {
		return damageDropped(state);
	}

	@Override
	public int getRenderType() {
		return renderId;
	}

	@Override
	public IBlockState getStateFromMeta(final int meta) {
		return getDefaultState().withProperty(BlockRotated.DAMAGE, meta);
	}

	@Override
	public TileEntity getTile() {
		if (renderTile == null) {
			renderTile = createNewTileEntity(null, 0);
		}
		return renderTile;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
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
			final TileBigSign tile = (TileBigSign) par1World.getTileEntity(pos);
			tile.canEdit = true;
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.BigSign, null, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return false;
	}

	@Override
	public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state,
			final EntityLivingBase entity, final ItemStack stack) {
		int l = MathHelper.floor_double(((entity.rotationYaw * 4.0f) / 360.0f) + 0.5) & 0x3;
		l %= 4;
		final TileBigSign tile = (TileBigSign) world.getTileEntity(pos);
		tile.rotation = l;
		world.setBlockState(pos, state.withProperty(BlockRotated.DAMAGE, stack.getItemDamage()), 2);
		if ((entity instanceof EntityPlayer) && !world.isRemote) {
			NoppesUtilServer.sendOpenGui((EntityPlayer) entity, EnumGuiType.BigSign, null, pos.getX(), pos.getY(),
					pos.getZ());
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
		tile.getBlockMetadata();
		float xStart = 0.0f;
		float zStart = 0.0f;
		float xEnd = 1.0f;
		float zEnd = 1.0f;
		if (tile.rotation == 0) {
			zStart = 0.87f;
		} else if (tile.rotation == 2) {
			zEnd = 0.13f;
		} else if (tile.rotation == 3) {
			xStart = 0.87f;
		} else if (tile.rotation == 1) {
			xEnd = 0.13f;
		}
		setBlockBounds(xStart, 0.0f, zStart, xEnd, 1.0f, zEnd);
	}
}

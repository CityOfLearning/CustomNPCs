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
import noppes.npcs.blocks.tiles.TileBigSign;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.client.renderer.ITileRenderer;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.util.NoppesUtilServer;

public class BlockBigSign extends BlockContainer implements ITileRenderer {
	public int renderId = -1;
	private TileEntity renderTile;

	public BlockBigSign() {
		super(Material.wood);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { BlockRotated.DAMAGE });
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileBigSign();
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(BlockRotated.DAMAGE).intValue();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
		return null;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return damageDropped(state);
	}

	@Override
	public int getRenderType() {
		return renderId;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BlockRotated.DAMAGE, Integer.valueOf(meta));
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
	public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (par1World.isRemote) {
			return false;
		}
		ItemStack currentItem = player.inventory.getCurrentItem();
		if ((currentItem != null) && (currentItem.getItem() == CustomItems.wand)
				&& (CustomNpcsPermissions.hasPermission(player, CustomNpcsPermissions.EDIT_BLOCKS))) {
			TileBigSign tile = (TileBigSign) par1World.getTileEntity(pos);
			tile.setCanEdit(true);
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.BigSign, null, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity,
			ItemStack stack) {
		int l = MathHelper.floor_double(((entity.rotationYaw * 4.0F) / 360.0F) + 0.5D) & 0x3;
		l %= 4;

		TileBigSign tile = (TileBigSign) world.getTileEntity(pos);
		tile.setRotation(l);

		world.setBlockState(pos, state.withProperty(BlockRotated.DAMAGE, Integer.valueOf(stack.getItemDamage())), 2);
		if (((entity instanceof EntityPlayer)) && (!world.isRemote)) {
			NoppesUtilServer.sendOpenGui((EntityPlayer) entity, EnumGuiType.BigSign, null, pos.getX(), pos.getY(),
					pos.getZ());
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
		tile.getBlockMetadata();
		float xStart = 0.0F;
		float zStart = 0.0F;
		float xEnd = 1.0F;
		float zEnd = 1.0F;
		if (tile.getRotation() == 0) {
			zStart = 0.87F;
		} else if (tile.getRotation() == 2) {
			zEnd = 0.13F;
		} else if (tile.getRotation() == 3) {
			xStart = 0.87F;
		} else if (tile.getRotation() == 1) {
			xEnd = 0.13F;
		}
		setBlockBounds(xStart, 0.0F, zStart, xEnd, 1.0F, zEnd);
	}
}

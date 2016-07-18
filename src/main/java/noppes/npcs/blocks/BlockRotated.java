//

//

package noppes.npcs.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.client.renderer.ITileRenderer;

public abstract class BlockRotated extends BlockContainer implements ITileRenderer {
	public static final PropertyInteger DAMAGE;
	static {
		DAMAGE = PropertyInteger.create("damage", 0, 6);
	}
	public int renderId;

	private TileEntity renderTile;

	protected BlockRotated(final Block block) {
		super(block.getMaterial());
		renderId = -1;
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { BlockRotated.DAMAGE });
	}

	@Override
	public int damageDropped(final IBlockState state) {
		return state.getValue(BlockRotated.DAMAGE);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(final World world, final BlockPos pos, final IBlockState state) {
		setBlockBoundsBasedOnState(world, pos);
		return super.getCollisionBoundingBox(world, pos, state);
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
			renderTile = createNewTileEntity((World) null, 0);
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
	public boolean isSideSolid(final IBlockAccess world, final BlockPos pos, final EnumFacing side) {
		return true;
	}

	public int maxRotation() {
		return 4;
	}

	@Override
	public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state,
			final EntityLivingBase placer, final ItemStack stack) {
		int l = MathHelper.floor_double(((placer.rotationYaw * maxRotation()) / 360.0f) + 0.5) & (maxRotation() - 1);
		l %= maxRotation();
		final TileColorable tile = (TileColorable) world.getTileEntity(pos);
		tile.rotation = l;
	}
}

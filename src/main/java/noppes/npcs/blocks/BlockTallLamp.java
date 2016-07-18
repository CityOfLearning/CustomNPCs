//

//

package noppes.npcs.blocks;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.blocks.tiles.TileTallLamp;
import noppes.npcs.client.renderer.ITileRenderer;

public class BlockTallLamp extends BlockContainer implements ITileRenderer {
	public static final PropertyInteger DAMAGE;
	public static final PropertyBool IS_TOP;
	static {
		DAMAGE = PropertyInteger.create("damage", 0, 6);
		IS_TOP = PropertyBool.create("istop");
	}
	public int renderId;

	private TileColorable renderTile;

	public BlockTallLamp() {
		super(Material.wood);
		renderId = -1;
		setDefaultState(blockState.getBaseState().withProperty(BlockTallLamp.IS_TOP, false));
		setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f);
		setLightLevel(1.0f);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { BlockTallLamp.DAMAGE, BlockTallLamp.IS_TOP });
	}

	@Override
	public TileEntity createNewTileEntity(final World var1, final int var2) {
		if (var2 < 7) {
			return new TileTallLamp();
		}
		return null;
	}

	@Override
	public int damageDropped(final IBlockState state) {
		return state.getValue(BlockTallLamp.DAMAGE);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(final World world, final BlockPos pos, final IBlockState state) {
		setBlockBoundsBasedOnState(world, pos);
		return super.getCollisionBoundingBox(world, pos, state);
	}

	@Override
	public int getMetaFromState(final IBlockState state) {
		return state.getValue(BlockTallLamp.DAMAGE) + (state.getValue(BlockTallLamp.IS_TOP) ? 7 : 0);
	}

	@Override
	public int getRenderType() {
		return renderId;
	}

	@Override
	public IBlockState getStateFromMeta(final int meta) {
		return getDefaultState().withProperty(BlockTallLamp.DAMAGE, (meta % 7)).withProperty(BlockTallLamp.IS_TOP,
				(meta >= 7));
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
	public TileColorable getTile() {
		if (renderTile == null) {
			renderTile = (TileColorable) createNewTileEntity(null, 0);
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
	public boolean onBlockActivated(final World par1World, BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		final ItemStack item = player.inventory.getCurrentItem();
		if ((item == null) || (item.getItem() != Items.dye)) {
			return false;
		}
		if (state.getValue(BlockTallLamp.IS_TOP)) {
			pos = pos.down();
		}
		final TileColorable tile = (TileColorable) par1World.getTileEntity(pos);
		final int color = EnumDyeColor.byDyeDamage(item.getItemDamage()).getMetadata();
		if (tile.color != color) {
			NoppesUtilServer.consumeItemStack(1, player);
			tile.color = color;
			par1World.markBlockForUpdate(pos);
		}
		return true;
	}

	@Override
	public void onBlockHarvested(final World world, final BlockPos pos, final IBlockState state,
			final EntityPlayer player) {
		if (state.getValue(BlockTallLamp.IS_TOP) && (world.getBlockState(pos.down()).getBlock() == this)) {
			world.setBlockToAir(pos.down());
		} else if (!(boolean) state.getValue(BlockTallLamp.IS_TOP)
				&& (world.getBlockState(pos.up()).getBlock() == this)) {
			world.setBlockToAir(pos.up());
		}
	}

	@Override
	public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state,
			final EntityLivingBase entity, final ItemStack stack) {
		if (!world.isAirBlock(pos.up())) {
			world.setBlockToAir(pos);
		} else {
			int l = MathHelper.floor_double(((entity.rotationYaw * 4.0f) / 360.0f) + 0.5) & 0x3;
			l %= 4;
			world.setBlockState(pos, state.withProperty(BlockTallLamp.DAMAGE, stack.getItemDamage())
					.withProperty(BlockTallLamp.IS_TOP, false), 2);
			world.setBlockState(pos.up(), state.withProperty(BlockTallLamp.DAMAGE, stack.getItemDamage())
					.withProperty(BlockTallLamp.IS_TOP, true), 2);
			final TileColorable tile = (TileColorable) world.getTileEntity(pos);
			tile.rotation = l;
			tile.color = 15 - stack.getItemDamage();
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(final IBlockAccess world, final BlockPos pos) {
		try {
			if (world.getBlockState(pos).getValue(BlockTallLamp.IS_TOP)) {
				setBlockBounds(0.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f);
			} else {
				setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f);
			}
		} catch (IllegalArgumentException ex) {
		}
	}
}

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
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileBanner;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.client.renderer.ITileRenderer;

public class BlockBanner extends BlockContainer implements ITileRenderer {
	public static final PropertyInteger DAMAGE;
	public static final PropertyBool TOP;
	static {
		DAMAGE = PropertyInteger.create("damage", 0, 5);
		TOP = PropertyBool.create("top");
	}

	private TileColorable renderTile;

	public BlockBanner() {
		super(Material.rock);
		setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { BlockBanner.DAMAGE, BlockBanner.TOP });
	}

	@Override
	public TileEntity createNewTileEntity(final World var1, final int var2) {
		if (var2 < 7) {
			return new TileBanner();
		}
		return null;
	}

	@Override
	public int damageDropped(final IBlockState state) {
		return state.getValue(BlockBanner.DAMAGE);
	}

	@Override
	public int getMetaFromState(final IBlockState state) {
		return state.getValue(BlockBanner.DAMAGE) + (state.getValue(BlockBanner.TOP) ? 7 : 0);
	}

	@Override
	public IBlockState getStateFromMeta(final int meta) {
		return getDefaultState().withProperty(BlockBanner.DAMAGE, (meta % 7)).withProperty(BlockBanner.TOP,
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
		if (item == null) {
			return false;
		}
		if (state.getValue(BlockBanner.TOP)) {
			pos = pos.down();
		}
		final TileBanner tile = (TileBanner) par1World.getTileEntity(pos);
		if (tile.canEdit()) {
			return true;
		}
		if (item.getItem() != Items.dye) {
			return false;
		}
		final int color = EnumDyeColor.byMetadata(item.getItemDamage()).getMapColor().colorValue;
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
		if (state.getValue(BlockBanner.TOP) && (world.getBlockState(pos.down()).getBlock() == this)) {
			world.setBlockToAir(pos.down());
		} else if (!(boolean) state.getValue(BlockBanner.TOP) && (world.getBlockState(pos.up()).getBlock() == this)) {
			world.setBlockToAir(pos.up());
		}
	}

	@Override
	public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state,
			final EntityLivingBase entity, final ItemStack stack) {
		if (!world.isAirBlock(pos.up())) {
			world.setBlockToAir(pos);
		} else {
			world.setBlockState(pos, getDefaultState().withProperty(BlockBanner.TOP, false)
					.withProperty(BlockBanner.DAMAGE, stack.getItemDamage()), 2);
			world.setBlockState(pos.up(), getDefaultState().withProperty(BlockBanner.TOP, true)
					.withProperty(BlockBanner.DAMAGE, stack.getItemDamage()), 2);
			int l = MathHelper.floor_double(((entity.rotationYaw * 4.0f) / 360.0f) + 0.5) & 0x3;
			l %= 4;
			final TileBanner tile = (TileBanner) world.getTileEntity(pos);
			tile.rotation = l;
			tile.color = 15 - stack.getItemDamage();
			tile.time = System.currentTimeMillis();
			if ((entity instanceof EntityPlayer) && world.isRemote) {
				((EntityPlayer) entity)
						.addChatComponentMessage(new ChatComponentTranslation("availability.editIcon", new Object[0]));
			}
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(final IBlockAccess world, final BlockPos pos) {
		try {
			if (world.getBlockState(pos).getValue(BlockBanner.TOP)) {
				setBlockBounds(0.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f);
			} else {
				setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f);
			}
		} catch (IllegalArgumentException ex) {
		}
	}
}

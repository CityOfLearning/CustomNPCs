//

//

package noppes.npcs.blocks;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.blocks.tiles.TileWallBanner;
import noppes.npcs.client.renderer.ITileRenderer;

public class BlockWallBanner extends BlockContainer implements ITileRenderer {
	public static final PropertyInteger DAMAGE;
	static {
		DAMAGE = PropertyInteger.create("damage", 0, 6);
	}
	public int renderId;

	private TileColorable renderTile;

	public BlockWallBanner() {
		super(Material.rock);
		renderId = -1;
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { BlockWallBanner.DAMAGE });
	}

	@Override
	public TileEntity createNewTileEntity(final World var1, final int var2) {
		return new TileWallBanner();
	}

	@Override
	public int damageDropped(final IBlockState state) {
		return state.getValue(BlockWallBanner.DAMAGE);
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
		return getDefaultState().withProperty(BlockWallBanner.DAMAGE, meta);
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
	public boolean onBlockActivated(final World par1World, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		final ItemStack item = player.inventory.getCurrentItem();
		if (item == null) {
			return false;
		}
		final TileWallBanner tile = (TileWallBanner) par1World.getTileEntity(pos);
		if (tile.canEdit()) {
			return true;
		}
		if (item.getItem() != Items.dye) {
			return false;
		}
		final int color = EnumDyeColor.byDyeDamage(item.getItemDamage()).getMetadata();
		if (tile.color != color) {
			NoppesUtilServer.consumeItemStack(1, player);
			tile.color = color;
			par1World.markBlockForUpdate(pos);
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state,
			final EntityLivingBase entity, final ItemStack stack) {
		int l = MathHelper.floor_double(((entity.rotationYaw * 4.0f) / 360.0f) + 0.5) & 0x3;
		l %= 4;
		world.setBlockState(pos, state.withProperty(BlockWallBanner.DAMAGE, stack.getItemDamage()), 2);
		final TileWallBanner tile = (TileWallBanner) world.getTileEntity(pos);
		tile.rotation = l;
		tile.color = 15 - stack.getItemDamage();
		tile.time = System.currentTimeMillis();
		if ((entity instanceof EntityPlayer) && world.isRemote) {
			((EntityPlayer) entity)
					.addChatComponentMessage(new ChatComponentTranslation("availability.editIcon", new Object[0]));
		}
	}
}

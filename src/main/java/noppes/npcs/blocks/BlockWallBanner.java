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
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.blocks.tiles.TileWallBanner;
import noppes.npcs.client.renderer.ITileRenderer;
import noppes.npcs.util.NoppesUtilServer;

public class BlockWallBanner extends BlockContainer implements ITileRenderer {
	public static final PropertyInteger DAMAGE = PropertyInteger.create("damage", 0, 6);
	public int renderId = -1;
	private TileColorable renderTile;

	public BlockWallBanner() {
		super(Material.rock);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { DAMAGE });
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileWallBanner();
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(DAMAGE).intValue();
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
		return getDefaultState().withProperty(DAMAGE, Integer.valueOf(meta));
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
		par3List.add(new ItemStack(par1, 1, 2));
		par3List.add(new ItemStack(par1, 1, 3));
		par3List.add(new ItemStack(par1, 1, 4));
	}

	@Override
	public TileColorable getTile() {
		if (renderTile == null) {
			renderTile = ((TileColorable) createNewTileEntity(null, 0));
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
		ItemStack item = player.inventory.getCurrentItem();
		if (item == null) {
			return false;
		}
		TileWallBanner tile = (TileWallBanner) par1World.getTileEntity(pos);
		if (tile.canEdit()) {
			return true;
		}
		if (item.getItem() != Items.dye) {
			return false;
		}
		int color = EnumDyeColor.byDyeDamage(item.getItemDamage()).getMetadata();
		if (tile.getColor() != color) {
			NoppesUtilServer.consumeItemStack(1, player);
			tile.setColor(color);
			par1World.markBlockForUpdate(pos);
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity,
			ItemStack stack) {
		int l = MathHelper.floor_double(((entity.rotationYaw * 4.0F) / 360.0F) + 0.5D) & 0x3;
		l %= 4;
		world.setBlockState(pos, state.withProperty(DAMAGE, Integer.valueOf(stack.getItemDamage())), 2);
		TileWallBanner tile = (TileWallBanner) world.getTileEntity(pos);
		tile.setRotation(l);
		tile.setColor((15 - stack.getItemDamage()));
		tile.setTime(System.currentTimeMillis());
		if (((entity instanceof EntityPlayer)) && (world.isRemote)) {
			((EntityPlayer) entity)
					.addChatComponentMessage(new ChatComponentTranslation("availability.editIcon", new Object[0]));
		}
	}
}

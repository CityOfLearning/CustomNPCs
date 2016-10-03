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
	public static final PropertyInteger DAMAGE = PropertyInteger.create("damage", 0, 6);
	public static final PropertyBool IS_TOP = PropertyBool.create("istop");
	public int renderId = -1;
	private TileColorable renderTile;

	public BlockTallLamp() {
		super(Material.wood);
		setDefaultState(blockState.getBaseState().withProperty(IS_TOP, Boolean.valueOf(false)));
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
		setLightLevel(1.0F);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { DAMAGE, IS_TOP });
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		if (var2 < 7) {
			return new TileTallLamp();
		}
		return null;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(DAMAGE).intValue();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
		setBlockBoundsBasedOnState(world, pos);
		return super.getCollisionBoundingBox(world, pos, state);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(DAMAGE).intValue() + (state.getValue(IS_TOP).booleanValue() ? 7 : 0);
	}

	@Override
	public int getRenderType() {
		return renderId;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(DAMAGE, Integer.valueOf(meta % 7)).withProperty(IS_TOP,
				Boolean.valueOf(meta >= 7));
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
		if ((item == null) || (item.getItem() != Items.dye)) {
			return false;
		}
		if (state.getValue(IS_TOP).booleanValue()) {
			pos = pos.down();
		}
		TileColorable tile = (TileColorable) par1World.getTileEntity(pos);
		int color = EnumDyeColor.byDyeDamage(item.getItemDamage()).getMetadata();
		if (tile.color != color) {
			NoppesUtilServer.consumeItemStack(1, player);
			tile.color = color;
			par1World.markBlockForUpdate(pos);
		}
		return true;
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if ((state.getValue(IS_TOP).booleanValue()) && (world.getBlockState(pos.down()).getBlock() == this)) {
			world.setBlockToAir(pos.down());
		} else if ((!state.getValue(IS_TOP).booleanValue()) && (world.getBlockState(pos.up()).getBlock() == this)) {
			world.setBlockToAir(pos.up());
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity,
			ItemStack stack) {
		if (!world.isAirBlock(pos.up())) {
			world.setBlockToAir(pos);
		} else {
			int l = MathHelper.floor_double(((entity.rotationYaw * 4.0F) / 360.0F) + 0.5D) & 0x3;
			l %= 4;

			world.setBlockState(pos, state.withProperty(DAMAGE, Integer.valueOf(stack.getItemDamage()))
					.withProperty(IS_TOP, Boolean.valueOf(false)), 2);
			world.setBlockState(pos.up(), state.withProperty(DAMAGE, Integer.valueOf(stack.getItemDamage()))
					.withProperty(IS_TOP, Boolean.valueOf(true)), 2);

			TileColorable tile = (TileColorable) world.getTileEntity(pos);
			tile.rotation = l;
			tile.color = (15 - stack.getItemDamage());
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
		try {
			if (world.getBlockState(pos).getValue(IS_TOP).booleanValue()) {
				setBlockBounds(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			} else {
				setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
			}
		} catch (IllegalArgumentException ex) {
		}
	}
}

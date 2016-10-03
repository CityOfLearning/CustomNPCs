package noppes.npcs.blocks;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileBigSign;
import noppes.npcs.blocks.tiles.TileTombstone;
import noppes.npcs.client.renderer.ITileRenderer;
import noppes.npcs.constants.EnumGuiType;

public class BlockTombstone extends BlockContainer implements ITileRenderer {
	public static final PropertyInteger DAMAGE = PropertyInteger.create("damage", 0, 6);
	private TileEntity renderTile;

	public BlockTombstone() {
		super(Blocks.cobblestone.getMaterial());
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { DAMAGE });
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileTombstone();
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(DAMAGE).intValue();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return damageDropped(state);
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

	public int maxRotation() {
		return 4;
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
			if (tile.getBlockMetadata() >= 2) {
				return false;
			}
			tile.canEdit = true;
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.BigSign, null, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity,
			ItemStack stack) {
		int l = MathHelper.floor_double(((entity.rotationYaw * maxRotation()) / 360.0F) + 0.5D) & (maxRotation() - 1);
		l %= maxRotation();

		TileTombstone tile = (TileTombstone) world.getTileEntity(pos);
		tile.rotation = l;

		world.setBlockState(pos, state.withProperty(DAMAGE, Integer.valueOf(stack.getItemDamage())), 2);
		if (((entity instanceof EntityPlayer)) && (!world.isRemote) && (stack.getItemDamage() < 2)) {
			NoppesUtilServer.sendOpenGui((EntityPlayer) entity, EnumGuiType.BigSign, null, pos.getX(), pos.getY(),
					pos.getZ());
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
		TileEntity tileentity = world.getTileEntity(pos);
		if (!(tileentity instanceof TileTombstone)) {
			super.setBlockBoundsBasedOnState(world, pos);
			return;
		}
		TileTombstone tile = (TileTombstone) tileentity;
		if ((tile.rotation % 2) == 0) {
			setBlockBounds(0.0F, 0.0F, 0.3F, 1.0F, 1.0F, 0.7F);
		} else {
			setBlockBounds(0.3F, 0.0F, 0.0F, 0.7F, 1.0F, 1.0F);
		}
	}
}

//

//

package noppes.npcs.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileBorder;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.util.IPermission;

public class BlockBorder extends BlockContainer implements IPermission {
	public static final PropertyInteger ROTATION;

	static {
		ROTATION = PropertyInteger.create("rotation", 0, 3);
	}

	public BlockBorder() {
		super(Material.rock);
		setBlockUnbreakable();
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { BlockBorder.ROTATION });
	}

	@Override
	public TileEntity createNewTileEntity(final World var1, final int var2) {
		return new TileBorder();
	}

	@Override
	public int getMetaFromState(final IBlockState state) {
		return state.getValue(BlockBorder.ROTATION);
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	public IBlockState getStateFromMeta(final int meta) {
		return getDefaultState().withProperty(BlockBorder.ROTATION, meta);
	}

	private TileBorder getTile(final World world, final BlockPos pos) {
		final TileEntity tile = world.getTileEntity(pos);
		if ((tile != null) && (tile instanceof TileBorder)) {
			return (TileBorder) tile;
		}
		return null;
	}

	@Override
	public boolean isAllowed(final EnumPacketServer e) {
		return e == EnumPacketServer.SaveTileEntity;
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
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		final ItemStack currentItem = player.inventory.getCurrentItem();
		if (!world.isRemote && (currentItem != null) && (currentItem.getItem() == CustomItems.wand)) {
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.Border, null, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return false;
	}

	@Override
	public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state,
			final EntityLivingBase entity, final ItemStack stack) {
		int l = MathHelper.floor_double(((entity.rotationYaw * 4.0f) / 360.0f) + 0.5) & 0x3;
		l %= 4;
		world.setBlockState(pos, state.withProperty(BlockBorder.ROTATION, l));
		final TileBorder tile = (TileBorder) world.getTileEntity(pos);
		TileBorder adjacent = getTile(world, pos.west());
		if (adjacent == null) {
			adjacent = getTile(world, pos.south());
		}
		if (adjacent == null) {
			adjacent = getTile(world, pos.north());
		}
		if (adjacent == null) {
			adjacent = getTile(world, pos.east());
		}
		if (adjacent != null) {
			final NBTTagCompound compound = new NBTTagCompound();
			adjacent.writeExtraNBT(compound);
			tile.readExtraNBT(compound);
		}
		tile.rotation = l;
		if ((entity instanceof EntityPlayer) && !world.isRemote) {
			NoppesUtilServer.sendOpenGui((EntityPlayer) entity, EnumGuiType.Border, null, pos.getX(), pos.getY(),
					pos.getZ());
		}
	}
}

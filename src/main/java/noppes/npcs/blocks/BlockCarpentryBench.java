
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import noppes.npcs.CustomNpcs;
import noppes.npcs.blocks.tiles.TileBlockAnvil;
import noppes.npcs.client.renderer.ITileRenderer;
import noppes.npcs.constants.EnumGuiType;

public class BlockCarpentryBench extends BlockContainer implements ITileRenderer {
	public static PropertyInteger TYPE;
	public static PropertyInteger ROTATION;
	static {
		TYPE = PropertyInteger.create("type", 0, 1);
		ROTATION = PropertyInteger.create("rotation", 0, 3);
	}

	private TileEntity renderTile;

	public BlockCarpentryBench() {
		super(Material.wood);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { BlockCarpentryBench.TYPE, BlockCarpentryBench.ROTATION });
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileBlockAnvil();
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(BlockCarpentryBench.TYPE);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BlockCarpentryBench.ROTATION) + (state.getValue(BlockCarpentryBench.TYPE) * 4);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BlockCarpentryBench.TYPE, (Integer.valueOf(meta) >> 2))
				.withProperty(BlockCarpentryBench.ROTATION, (meta % 4));
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
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
		if (!par1World.isRemote) {
			player.openGui(CustomNpcs.instance, EnumGuiType.PlayerAnvil.ordinal(), par1World, pos.getX(), pos.getY(),
					pos.getZ());
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity,
			ItemStack stack) {
		int var6 = MathHelper.floor_double((entity.rotationYaw / 90.0f) + 0.5) & 0x3;
		world.setBlockState(pos, state.withProperty(BlockCarpentryBench.TYPE, stack.getItemDamage())
				.withProperty(BlockCarpentryBench.ROTATION, var6), 2);
	}
}

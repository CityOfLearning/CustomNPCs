
package noppes.npcs.blocks;

import java.util.ArrayList;
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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.Server;
import noppes.npcs.blocks.tiles.TileMailbox;
import noppes.npcs.client.renderer.ITileRenderer;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketClient;

public class BlockMailbox extends BlockContainer implements ITileRenderer {
	public static PropertyInteger ROTATION;
	public static PropertyInteger TYPE;
	static {
		ROTATION = PropertyInteger.create("rotation", 0, 8);
		TYPE = PropertyInteger.create("type", 0, 2);
	}

	private TileEntity renderTile;

	public BlockMailbox() {
		super(Material.iron);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { BlockMailbox.TYPE, BlockMailbox.ROTATION });
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileMailbox();
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(BlockMailbox.TYPE);
	}

	@Override
	public ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<>();
		int damage = state.getValue(BlockMailbox.TYPE);
		ret.add(new ItemStack(this, 1, damage));
		return ret;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BlockMailbox.ROTATION) | (state.getValue(BlockMailbox.TYPE) << 2);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BlockMailbox.TYPE, (Integer.valueOf(meta) >> 2))
				.withProperty(BlockMailbox.ROTATION, (meta | 0x4));
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
			Server.sendData((EntityPlayerMP) player, EnumPacketClient.GUI, EnumGuiType.PlayerMailbox, pos.getX(),
					pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity,
			ItemStack stack) {
		int l = MathHelper.floor_double(((entity.rotationYaw * 4.0f) / 360.0f) + 0.5) & 0x3;
		l %= 4;
		world.setBlockState(pos,
				state.withProperty(BlockMailbox.TYPE, stack.getItemDamage()).withProperty(BlockMailbox.ROTATION, l), 2);
	}
}

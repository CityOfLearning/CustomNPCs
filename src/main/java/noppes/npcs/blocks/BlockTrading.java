//

//

package noppes.npcs.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.CustomNpcs;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.blocks.tiles.TileTrading;
import noppes.npcs.client.renderer.ITileRenderer;
import noppes.npcs.constants.EnumGuiType;

public class BlockTrading extends BlockContainer implements ITileRenderer {
	private TileTrading renderTile;

	public BlockTrading() {
		super(Material.wood);
		setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.4f, 1.0f);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileTrading();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
		return null;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public TileEntity getTile() {
		if (renderTile == null) {
			renderTile = (TileTrading) createNewTileEntity(null, 0);
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
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}
		TileTrading tile = (TileTrading) world.getTileEntity(pos);
		if (tile.isFull()) {
			player.addChatComponentMessage(new ChatComponentTranslation("trader.busy", new Object[0]));
			return false;
		}
		player.openGui(CustomNpcs.instance, EnumGuiType.TradingBlock.ordinal(), world, pos.getX(), pos.getY(),
				pos.getZ());
		tile.addTrader(player);
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		int l = MathHelper.floor_double(((placer.rotationYaw * 4.0f) / 360.0f) + 0.5) & 0x3;
		l %= 4;
		TileColorable tile = (TileColorable) world.getTileEntity(pos);
		tile.rotation = l;
	}
}

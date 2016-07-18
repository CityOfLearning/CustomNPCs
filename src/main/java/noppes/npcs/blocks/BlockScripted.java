//

//

package noppes.npcs.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.util.IPermission;

public class BlockScripted extends BlockContainer implements IPermission {
	public BlockScripted() {
		super(Material.rock);
		setBlockBounds(0.001f, 0.001f, 0.001f, 0.998f, 0.998f, 0.998f);
	}

	@Override
	public void breakBlock(final World world, final BlockPos pos, final IBlockState state) {
		if (world.isRemote) {
			return;
		}
		final TileScripted tile = (TileScripted) world.getTileEntity(pos);
		EventHooks.onScriptBlockBreak(tile);
	}

	@Override
	public boolean canCreatureSpawn(final IBlockAccess world, final BlockPos pos,
			final EntityLiving.SpawnPlacementType type) {
		return true;
	}

	@Override
	public boolean canEntityDestroy(final IBlockAccess world, final BlockPos pos, final Entity entity) {
		return super.canEntityDestroy(world, pos, entity);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(final World worldIn, final int meta) {
		return new TileScripted();
	}

	@Override
	public void fillWithRain(final World world, final BlockPos pos) {
		if (world.isRemote) {
			return;
		}
		final TileScripted tile = (TileScripted) world.getTileEntity(pos);
		EventHooks.onScriptBlockRainFill(tile);
	}

	@Override
	public float getBlockHardness(final World worldIn, final BlockPos pos) {
		return blockHardness;
	}

	@Override
	public float getEnchantPowerBonus(final World world, final BlockPos pos) {
		return super.getEnchantPowerBonus(world, pos);
	}

	@Override
	public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
		return null;
	}

	@Override
	public int getLightValue(final IBlockAccess world, final BlockPos pos) {
		final TileScripted tile = (TileScripted) world.getTileEntity(pos);
		if (tile == null) {
			return 0;
		}
		return tile.lightValue;
	}

	@Override
	public int getStrongPower(final IBlockAccess world, final BlockPos pos, final IBlockState state,
			final EnumFacing side) {
		return ((TileScripted) world.getTileEntity(pos)).activePowering;
	}

	@Override
	public int getWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state,
			final EnumFacing side) {
		return getStrongPower(worldIn, pos, state, side);
	}

	@Override
	public boolean isAllowed(final EnumPacketServer e) {
		return (e == EnumPacketServer.SaveTileEntity) || (e == EnumPacketServer.ScriptBlockDataSave);
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public boolean isLadder(final IBlockAccess world, final BlockPos pos, final EntityLivingBase entity) {
		return ((TileScripted) world.getTileEntity(pos)).isLadder;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		if (world.isRemote) {
			return true;
		}
		final ItemStack currentItem = player.inventory.getCurrentItem();
		if ((currentItem != null)
				&& ((currentItem.getItem() == CustomItems.wand) || (currentItem.getItem() == CustomItems.scripter))) {
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.ScriptBlock, null, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		final TileScripted tile = (TileScripted) world.getTileEntity(pos);
		return !EventHooks.onScriptBlockInteract(tile, player, side.getIndex(), hitX, hitY, hitZ);
	}

	@Override
	public void onBlockClicked(final World world, final BlockPos pos, final EntityPlayer player) {
		if (world.isRemote) {
			return;
		}
		final TileScripted tile = (TileScripted) world.getTileEntity(pos);
		EventHooks.onScriptBlockClicked(tile, player);
	}

	@Override
	public void onBlockExploded(final World world, final BlockPos pos, final Explosion explosion) {
		if (!world.isRemote) {
			final TileScripted tile = (TileScripted) world.getTileEntity(pos);
			if (EventHooks.onScriptBlockExploded(tile)) {
				return;
			}
		}
		super.onBlockExploded(world, pos, explosion);
	}

	@Override
	public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state,
			final EntityLivingBase entity, final ItemStack stack) {
		if ((entity instanceof EntityPlayer) && !world.isRemote) {
			NoppesUtilServer.sendOpenGui((EntityPlayer) entity, EnumGuiType.ScriptBlock, null, pos.getX(), pos.getY(),
					pos.getZ());
		}
	}

	@Override
	public void onEntityCollidedWithBlock(final World world, final BlockPos pos, final IBlockState state,
			final Entity entityIn) {
		if (world.isRemote) {
			return;
		}
		final TileScripted tile = (TileScripted) world.getTileEntity(pos);
		EventHooks.onScriptBlockCollide(tile, entityIn);
	}

	@Override
	public void onFallenUpon(final World world, final BlockPos pos, final Entity entity, float fallDistance) {
		if (world.isRemote) {
			return;
		}
		final TileScripted tile = (TileScripted) world.getTileEntity(pos);
		fallDistance = EventHooks.onScriptBlockFallenUpon(tile, entity, fallDistance);
		super.onFallenUpon(world, pos, entity, fallDistance);
	}

	@Override
	public void onNeighborBlockChange(final World world, final BlockPos pos, final IBlockState state,
			final Block neighborBlock) {
		if (world.isRemote) {
			return;
		}
		final TileScripted tile = (TileScripted) world.getTileEntity(pos);
		EventHooks.onScriptBlockNeighborChanged(tile);
		int power = 0;
		for (final EnumFacing enumfacing : EnumFacing.values()) {
			final int p = world.getRedstonePower(pos.offset(enumfacing), enumfacing);
			if (p > power) {
				power = p;
			}
		}
		if ((tile.prevPower != power) && (tile.powering <= 0)) {
			tile.newPower = power;
		}
	}

	@Override
	public boolean removedByPlayer(final World world, final BlockPos pos, final EntityPlayer player,
			final boolean willHarvest) {
		if (world.isRemote) {
			return super.removedByPlayer(world, pos, player, willHarvest);
		}
		final TileScripted tile = (TileScripted) world.getTileEntity(pos);
		return !EventHooks.onScriptBlockHarvest(tile, player) && super.removedByPlayer(world, pos, player, willHarvest);
	}
}


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
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (world.isRemote) {
			return;
		}
		TileScripted tile = (TileScripted) world.getTileEntity(pos);
		EventHooks.onScriptBlockBreak(tile);
	}

	@Override
	public boolean canCreatureSpawn(IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
		return true;
	}

	@Override
	public boolean canEntityDestroy(IBlockAccess world, BlockPos pos, Entity entity) {
		return super.canEntityDestroy(world, pos, entity);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileScripted();
	}

	@Override
	public void fillWithRain(World world, BlockPos pos) {
		if (world.isRemote) {
			return;
		}
		TileScripted tile = (TileScripted) world.getTileEntity(pos);
		EventHooks.onScriptBlockRainFill(tile);
	}

	@Override
	public float getBlockHardness(World worldIn, BlockPos pos) {
		return blockHardness;
	}

	@Override
	public float getEnchantPowerBonus(World world, BlockPos pos) {
		return super.getEnchantPowerBonus(world, pos);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	@Override
	public int getLightValue(IBlockAccess world, BlockPos pos) {
		TileScripted tile = (TileScripted) world.getTileEntity(pos);
		if (tile == null) {
			return 0;
		}
		return tile.lightValue;
	}

	@Override
	public int getStrongPower(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing side) {
		return ((TileScripted) world.getTileEntity(pos)).activePowering;
	}

	@Override
	public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
		return getStrongPower(worldIn, pos, state, side);
	}

	@Override
	public boolean isAllowed(EnumPacketServer e) {
		return (e == EnumPacketServer.SaveTileEntity) || (e == EnumPacketServer.ScriptBlockDataSave);
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public boolean isLadder(IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
		return ((TileScripted) world.getTileEntity(pos)).isLadder;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}
		ItemStack currentItem = player.inventory.getCurrentItem();
		if ((currentItem != null)
				&& ((currentItem.getItem() == CustomItems.wand) || (currentItem.getItem() == CustomItems.scripter))) {
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.ScriptBlock, null, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		TileScripted tile = (TileScripted) world.getTileEntity(pos);
		return !EventHooks.onScriptBlockInteract(tile, player, side.getIndex(), hitX, hitY, hitZ);
	}

	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
		if (world.isRemote) {
			return;
		}
		TileScripted tile = (TileScripted) world.getTileEntity(pos);
		EventHooks.onScriptBlockClicked(tile, player);
	}

	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
		if (!world.isRemote) {
			TileScripted tile = (TileScripted) world.getTileEntity(pos);
			if (EventHooks.onScriptBlockExploded(tile)) {
				return;
			}
		}
		super.onBlockExploded(world, pos, explosion);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity,
			ItemStack stack) {
		if ((entity instanceof EntityPlayer) && !world.isRemote) {
			NoppesUtilServer.sendOpenGui((EntityPlayer) entity, EnumGuiType.ScriptBlock, null, pos.getX(), pos.getY(),
					pos.getZ());
		}
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entityIn) {
		if (world.isRemote) {
			return;
		}
		TileScripted tile = (TileScripted) world.getTileEntity(pos);
		EventHooks.onScriptBlockCollide(tile, entityIn);
	}

	@Override
	public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) {
		if (world.isRemote) {
			return;
		}
		TileScripted tile = (TileScripted) world.getTileEntity(pos);
		fallDistance = EventHooks.onScriptBlockFallenUpon(tile, entity, fallDistance);
		super.onFallenUpon(world, pos, entity, fallDistance);
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
		if (world.isRemote) {
			return;
		}
		TileScripted tile = (TileScripted) world.getTileEntity(pos);
		EventHooks.onScriptBlockNeighborChanged(tile);
		int power = 0;
		for (EnumFacing enumfacing : EnumFacing.values()) {
			int p = world.getRedstonePower(pos.offset(enumfacing), enumfacing);
			if (p > power) {
				power = p;
			}
		}
		if ((tile.prevPower != power) && (tile.powering <= 0)) {
			tile.newPower = power;
		}
	}

	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if (world.isRemote) {
			return super.removedByPlayer(world, pos, player, willHarvest);
		}
		TileScripted tile = (TileScripted) world.getTileEntity(pos);
		return !EventHooks.onScriptBlockHarvest(tile, player) && super.removedByPlayer(world, pos, player, willHarvest);
	}
}

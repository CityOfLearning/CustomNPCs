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
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileCopy;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.util.IPermission;

public class BlockCopy extends BlockContainer implements IPermission {
	public BlockCopy() {
		super(Material.rock);
	}

	@Override
	public TileEntity createNewTileEntity(final World var1, final int var2) {
		return new TileCopy();
	}

	@Override
	public boolean isAllowed(final EnumPacketServer e) {
		return (e == EnumPacketServer.GetTileEntity) || (e == EnumPacketServer.SchematicsTile)
				|| (e == EnumPacketServer.SchematicsTileSave) || (e == EnumPacketServer.SaveTileEntity);
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
		if (par1World.isRemote) {
			return true;
		}
		final ItemStack currentItem = player.inventory.getCurrentItem();
		if ((currentItem != null) && (currentItem.getItem() == CustomItems.wand)) {
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.CopyBlock, null, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state,
			final EntityLivingBase entity, final ItemStack stack) {
		if ((entity instanceof EntityPlayer) && !world.isRemote) {
			NoppesUtilServer.sendOpenGui((EntityPlayer) entity, EnumGuiType.CopyBlock, null, pos.getX(), pos.getY(),
					pos.getZ());
		}
	}
}

//

//

package noppes.npcs.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.Server;
import noppes.npcs.blocks.tiles.TileBook;
import noppes.npcs.constants.EnumPacketClient;

public class BlockBook extends BlockRotated {
	public BlockBook() {
		super(Blocks.planks);
		setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.2f, 1.0f);
	}

	@Override
	public TileEntity createNewTileEntity(final World var1, final int var2) {
		return new TileBook();
	}

	@Override
	public String getUnlocalizedName() {
		return "item.book";
	}

	@Override
	public boolean onBlockActivated(final World par1World, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		if (par1World.isRemote) {
			return true;
		}
		final TileEntity tile = par1World.getTileEntity(pos);
		if (!(tile instanceof TileBook)) {
			return false;
		}
		final ItemStack currentItem = player.inventory.getCurrentItem();
		if ((currentItem != null) && (currentItem.getItem() == CustomItems.wand)
				&& CustomNpcsPermissions.hasPermission(player, CustomNpcsPermissions.EDIT_BLOCKS)) {
			((TileBook) tile).book.setItem(Items.writable_book);
		}
		Server.sendData((EntityPlayerMP) player, EnumPacketClient.OPEN_BOOK, pos.getX(), pos.getY(), pos.getZ(),
				((TileBook) tile).book.writeToNBT(new NBTTagCompound()));
		return true;
	}
}

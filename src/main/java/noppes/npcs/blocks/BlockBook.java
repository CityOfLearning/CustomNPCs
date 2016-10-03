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
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileBook();
	}

	@Override
	public String getUnlocalizedName() {
		return "item.book";
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (par1World.isRemote) {
			return true;
		}
		TileEntity tile = par1World.getTileEntity(pos);
		if (!(tile instanceof TileBook)) {
			return false;
		}
		ItemStack currentItem = player.inventory.getCurrentItem();
		if ((currentItem != null) && (currentItem.getItem() == CustomItems.wand)
				&& (CustomNpcsPermissions.hasPermission(player, CustomNpcsPermissions.EDIT_BLOCKS))) {
			((TileBook) tile).book.setItem(Items.writable_book);
		}
		Server.sendData((EntityPlayerMP) player, EnumPacketClient.OPEN_BOOK,
				new Object[] { Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()),
						((TileBook) tile).book.writeToNBT(new NBTTagCompound()) });
		return true;
	}
}

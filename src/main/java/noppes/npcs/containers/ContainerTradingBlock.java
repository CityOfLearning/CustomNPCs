
package noppes.npcs.containers;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import noppes.npcs.Server;
import noppes.npcs.blocks.tiles.TileTrading;
import noppes.npcs.constants.EnumPacketClient;

public class ContainerTradingBlock extends ContainerNpcInterface {
	public class SlotTrader extends Slot {
		public SlotTrader(IInventory par1iInventory, int par2, int par3, int par4) {
			super(par1iInventory, par2, par3, par4);
		}

		@Override
		public boolean isItemValid(ItemStack par1ItemStack) {
			if (player.worldObj.isRemote) {
				return (tile.getTrader1() != null) && (tile.getTrader2() != null) && (state < 3);
			}
			return tile.isFull() && (state < 3);
		}

		@Override
		public void onSlotChanged() {
			super.onSlotChanged();
			if (player.worldObj.isRemote || !tile.isFull()) {
				return;
			}
			EntityPlayer other = tile.other(player);
			if (other == null) {
				return;
			}
			if ((state == 2) || (state == 1)) {
				setState(0);
				((ContainerTradingBlock) other.openContainer).setState(0);
			}
			Server.sendData((EntityPlayerMP) other, EnumPacketClient.GUI_DATA, itemsToComp());
		}
	}

	public static Map<Integer, ItemStack> CompToItem(NBTTagCompound compound) {
		Map<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		NBTTagList list = compound.getTagList("Items", 10);
		for (int i = 0; i < list.tagCount(); ++i) {
			NBTTagCompound c = list.getCompoundTagAt(i);
			int slot = c.getInteger("Slot");
			ItemStack item = ItemStack.loadItemStackFromNBT(c);
			if (item != null) {
				items.put(slot, item);
			}
		}
		return items;
	}

	public InventoryCrafting craftMatrix;
	public TileTrading tile;
	private EntityPlayer player;

	private BlockPos pos;

	public int state;

	public ContainerTradingBlock(EntityPlayer player, BlockPos pos) {
		super(player);
		craftMatrix = new InventoryCrafting(this, 9, 1);
		state = 0;
		this.pos = pos;
		tile = (TileTrading) player.worldObj.getTileEntity(pos);
		this.player = player;
		for (int j = 0; j < 4; ++j) {
			for (int k = 0; k < 9; ++k) {
				addSlotToContainer(new Slot(player.inventory, k + (j * 9), 8 + (k * 18), 150 + (j * 18)));
			}
		}
		for (int j = 0; j < 9; ++j) {
			addSlotToContainer(new SlotTrader(craftMatrix, j, 8 + (j * 18), 125));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		TileEntity tile = player.worldObj.getTileEntity(pos);
		if ((tile == null) || !(tile instanceof TileTrading)) {
			return false;
		}
		this.tile = (TileTrading) tile;
		return !player.isDead && (player.getPosition().distanceSq(pos) < 16.0);
	}

	public NBTTagCompound itemsToComp() {
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < craftMatrix.getSizeInventory(); ++i) {
			ItemStack item = craftMatrix.getStackInSlot(i);
			if (item != null) {
				NBTTagCompound c = new NBTTagCompound();
				c.setInteger("Slot", i);
				item.writeToNBT(c);
				list.appendTag(c);
			}
		}
		compound.setTag("Items", list);
		return compound;
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		if (player.worldObj.isRemote) {
			return;
		}
		EntityPlayer other = tile.other(player);
		if (tile.getTrader1() == player) {
			tile.setTrader1(null);
		} else {
			tile.setTrader2(null);
		}
		if ((state != 3) && (other != null)) {
			((ContainerTradingBlock) other.openContainer).setState(4);
		}
		for (int i = 0; i < craftMatrix.getSizeInventory(); ++i) {
			ItemStack itemstack = craftMatrix.removeStackFromSlot(i);
			if (itemstack != null) {
				if (!player.inventory.addItemStackToInventory(itemstack)) {
					player.dropItem(itemstack, true, false);
				}
			}
		}
	}

	public void setState(int i) {
		if (state == i) {
			return;
		}
		state = i;
		if (!player.worldObj.isRemote) {
			NBTTagCompound compound = new NBTTagCompound();
			compound.setInteger("State", i);
			Server.sendData((EntityPlayerMP) player, EnumPacketClient.GUI_DATA, compound);
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i) {
		return null;
	}
}

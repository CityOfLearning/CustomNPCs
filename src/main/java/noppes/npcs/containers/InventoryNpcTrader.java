
package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class InventoryNpcTrader implements IInventory {
	private String inventoryTitle;
	private int slotsCount;
	private ItemStack[] inventoryContents;
	private ContainerNPCTrader con;

	public InventoryNpcTrader(String s, int i, ContainerNPCTrader con) {
		this.con = con;
		inventoryTitle = s;
		slotsCount = i;
		inventoryContents = new ItemStack[i];
	}

	@Override
	public void clear() {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (inventoryContents[i] != null) {
			ItemStack itemstack = inventoryContents[i];
			return ItemStack.copyItemStack(itemstack);
		}
		return null;
	}

	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText(inventoryTitle);
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public int getSizeInventory() {
		return slotsCount;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		ItemStack toBuy = inventoryContents[i];
		if (toBuy == null) {
			return null;
		}
		return ItemStack.copyItemStack(toBuy);
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void markDirty() {
		con.onCraftMatrixChanged(this);
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public ItemStack removeStackFromSlot(int i) {
		return null;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if (itemstack != null) {
			inventoryContents[i] = itemstack.copy();
		}
		markDirty();
	}
}

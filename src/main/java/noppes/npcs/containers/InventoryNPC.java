
package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class InventoryNPC implements IInventory {
	private String inventoryTitle;
	private int slotsCount;
	private ItemStack[] inventoryContents;
	private Container con;

	public InventoryNPC(String s, int i, Container con) {
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
		if (inventoryContents[i] == null) {
			return null;
		}
		if (inventoryContents[i].stackSize <= j) {
			ItemStack itemstack = inventoryContents[i];
			inventoryContents[i] = null;
			return itemstack;
		}
		ItemStack itemstack2 = inventoryContents[i].splitStack(j);
		if (inventoryContents[i].stackSize == 0) {
			inventoryContents[i] = null;
		}
		return itemstack2;
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
		return inventoryContents[i];
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
		return false;
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
		inventoryContents[i] = itemstack;
		if ((itemstack != null) && (itemstack.stackSize > getInventoryStackLimit())) {
			itemstack.stackSize = getInventoryStackLimit();
		}
	}
}

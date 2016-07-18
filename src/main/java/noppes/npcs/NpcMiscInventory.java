//

//

package noppes.npcs;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;

public class NpcMiscInventory implements IInventory {
	public HashMap<Integer, ItemStack> items;
	public int stackLimit;
	private int size;

	public NpcMiscInventory(final int size) {
		items = new HashMap<Integer, ItemStack>();
		stackLimit = 64;
		this.size = size;
	}

	public boolean addItemStack(final ItemStack item) {
		boolean merged = false;
		ItemStack mergable;
		while (((mergable = getMergableItem(item)) != null) && (mergable.stackSize > 0)) {
			final int size = mergable.getMaxStackSize() - mergable.stackSize;
			if (size > item.stackSize) {
				mergable.stackSize = mergable.getMaxStackSize();
				item.stackSize -= size;
				merged = true;
			} else {
				final ItemStack itemStack = mergable;
				itemStack.stackSize += item.stackSize;
				item.stackSize = 0;
			}
		}
		if (item.stackSize <= 0) {
			return true;
		}
		final int slot = firstFreeSlot();
		if (slot >= 0) {
			items.put(slot, item.copy());
			item.stackSize = 0;
			return true;
		}
		return merged;
	}

	@Override
	public void clear() {
	}

	@Override
	public void closeInventory(final EntityPlayer player) {
	}

	@Override
	public ItemStack decrStackSize(final int par1, final int par2) {
		if (items.get(par1) == null) {
			return null;
		}
		ItemStack var4 = null;
		if (items.get(par1).stackSize <= par2) {
			var4 = items.get(par1);
			items.put(par1, null);
		} else {
			var4 = items.get(par1).splitStack(par2);
			if (items.get(par1).stackSize == 0) {
				items.put(par1, null);
			}
		}
		return var4;
	}

	public boolean decrStackSize(final ItemStack eating, final int decrease) {
		for (final int slot : items.keySet()) {
			final ItemStack item = items.get(slot);
			if ((items != null) && (eating == item) && (item.stackSize >= decrease)) {
				item.splitStack(decrease);
				if (item.stackSize <= 0) {
					items.put(slot, null);
				}
				return true;
			}
		}
		return false;
	}

	public int firstFreeSlot() {
		for (int i = 0; i < getSizeInventory(); ++i) {
			if (items.get(i) == null) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public IChatComponent getDisplayName() {
		return null;
	}

	@Override
	public int getField(final int id) {
		return 0;
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return stackLimit;
	}

	public ItemStack getMergableItem(final ItemStack item) {
		for (final ItemStack is : items.values()) {
			if (NoppesUtilPlayer.compareItems(item, is, false, false) && (is.stackSize < is.getMaxStackSize())) {
				return is;
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return "Npc Misc Inventory";
	}

	@Override
	public int getSizeInventory() {
		return size;
	}

	@Override
	public ItemStack getStackInSlot(final int var1) {
		return items.get(var1);
	}

	public NBTTagCompound getToNBT() {
		final NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setTag("NpcMiscInv", NBTTags.nbtItemStackList(items));
		return nbttagcompound;
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
		return true;
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer var1) {
		return true;
	}

	@Override
	public void markDirty() {
	}

	@Override
	public void openInventory(final EntityPlayer player) {
	}

	@Override
	public ItemStack removeStackFromSlot(final int var1) {
		if (items.get(var1) != null) {
			final ItemStack var2 = items.get(var1);
			items.put(var1, null);
			return var2;
		}
		return null;
	}

	@Override
	public void setField(final int id, final int value) {
	}

	public void setFromNBT(final NBTTagCompound nbttagcompound) {
		items = NBTTags.getItemStackList(nbttagcompound.getTagList("NpcMiscInv", 10));
	}

	@Override
	public void setInventorySlotContents(final int var1, final ItemStack var2) {
		if (var1 >= getSizeInventory()) {
			return;
		}
		items.put(var1, var2);
	}

	public void setSize(final int i) {
		size = i;
	}
}

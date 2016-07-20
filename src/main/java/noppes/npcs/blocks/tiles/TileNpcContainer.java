//

//

package noppes.npcs.blocks.tiles;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public abstract class TileNpcContainer extends TileColorable implements IInventory {
	private ItemStack[] chestContents;
	public String customName;
	public int playerUsing;

	public TileNpcContainer() {
		customName = "";
		playerUsing = 0;
		chestContents = new ItemStack[getSizeInventory()];
	}

	@Override
	public void clear() {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		--playerUsing;
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2) {
		if (chestContents[par1] == null) {
			return null;
		}
		if (chestContents[par1].stackSize <= par2) {
			ItemStack itemstack = chestContents[par1];
			chestContents[par1] = null;
			markDirty();
			return itemstack;
		}
		ItemStack itemstack = chestContents[par1].splitStack(par2);
		if (chestContents[par1].stackSize == 0) {
			chestContents[par1] = null;
		}
		markDirty();
		return itemstack;
	}

	public void dropItems(World world, BlockPos pos) {
		for (int i1 = 0; i1 < getSizeInventory(); ++i1) {
			ItemStack itemstack = getStackInSlot(i1);
			if (itemstack != null) {
				float f = (world.rand.nextFloat() * 0.8f) + 0.1f;
				float f2 = (world.rand.nextFloat() * 0.8f) + 0.1f;
				float f3 = (world.rand.nextFloat() * 0.8f) + 0.1f;
				while (itemstack.stackSize > 0) {
					int j1 = world.rand.nextInt(21) + 10;
					if (j1 > itemstack.stackSize) {
						j1 = itemstack.stackSize;
					}
					ItemStack itemStack = itemstack;
					itemStack.stackSize -= j1;
					EntityItem entityitem = new EntityItem(world, pos.getX() + f, pos.getY() + f2, pos.getZ() + f3,
							new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
					float f4 = 0.05f;
					entityitem.motionX = (float) world.rand.nextGaussian() * f4;
					entityitem.motionY = ((float) world.rand.nextGaussian() * f4) + 0.2f;
					entityitem.motionZ = (float) world.rand.nextGaussian() * f4;
					if (itemstack.hasTagCompound()) {
						entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
					}
					world.spawnEntityInWorld(entityitem);
				}
			}
		}
	}

	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText(hasCustomName() ? customName : getName());
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
	public abstract String getName();

	@Override
	public int getSizeInventory() {
		return 54;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return chestContents[var1];
	}

	@Override
	public boolean hasCustomName() {
		return !customName.isEmpty();
	}

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		return true;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return (player.isDead || (worldObj.getTileEntity(pos) == this))
				&& (player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0);
	}

	@Override
	public void openInventory(EntityPlayer player) {
		++playerUsing;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		NBTTagList nbttaglist = compound.getTagList("Items", 10);
		chestContents = new ItemStack[getSizeInventory()];
		if (compound.hasKey("CustomName", 8)) {
			customName = compound.getString("CustomName");
		}
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 0xFF;
			if ((j >= 0) && (j < chestContents.length)) {
				chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public boolean receiveClientEvent(int p_145842_1_, int p_145842_2_) {
		if (p_145842_1_ == 1) {
			playerUsing = p_145842_2_;
			return true;
		}
		return super.receiveClientEvent(p_145842_1_, p_145842_2_);
	}

	@Override
	public ItemStack removeStackFromSlot(int par1) {
		if (chestContents[par1] != null) {
			ItemStack itemstack = chestContents[par1];
			chestContents[par1] = null;
			return itemstack;
		}
		return null;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
		chestContents[par1] = par2ItemStack;
		if ((par2ItemStack != null) && (par2ItemStack.stackSize > getInventoryStackLimit())) {
			par2ItemStack.stackSize = getInventoryStackLimit();
		}
		markDirty();
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < chestContents.length; ++i) {
			if (chestContents[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				chestContents[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		compound.setTag("Items", nbttaglist);
		if (hasCustomName()) {
			compound.setString("CustomName", customName);
		}
	}
}

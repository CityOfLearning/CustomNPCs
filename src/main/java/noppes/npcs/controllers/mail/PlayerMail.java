//

//

package noppes.npcs.controllers.mail;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IChatComponent;
import noppes.npcs.api.entity.data.IPlayerMail;
import noppes.npcs.controllers.quest.Quest;
import noppes.npcs.controllers.quest.QuestController;

public class PlayerMail implements IInventory, IPlayerMail {
	public String subject;
	public String sender;
	public NBTTagCompound message;
	public long time;
	public boolean beenRead;
	public int questId;
	public String questTitle;
	public ItemStack[] items;
	public long timePast;

	public PlayerMail() {
		subject = "";
		sender = "";
		message = new NBTTagCompound();
		time = 0L;
		beenRead = false;
		questId = -1;
		questTitle = "";
		items = new ItemStack[4];
	}

	@Override
	public void clear() {
	}

	@Override
	public void closeInventory(final EntityPlayer player) {
	}

	public PlayerMail copy() {
		final PlayerMail mail = new PlayerMail();
		mail.readNBT(writeNBT());
		return mail;
	}

	@Override
	public ItemStack decrStackSize(final int par1, final int par2) {
		if (items[par1] == null) {
			return null;
		}
		if (items[par1].stackSize <= par2) {
			final ItemStack itemstack = items[par1];
			items[par1] = null;
			markDirty();
			return itemstack;
		}
		final ItemStack itemstack = items[par1].splitStack(par2);
		if (items[par1].stackSize == 0) {
			items[par1] = null;
		}
		markDirty();
		return itemstack;
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
		return 64;
	}

	@Override
	public String getName() {
		return null;
	}

	public Quest getQuest() {
		return (QuestController.instance != null) ? QuestController.instance.quests.get(questId) : null;
	}

	@Override
	public int getSizeInventory() {
		return 4;
	}

	@Override
	public ItemStack getStackInSlot(final int var1) {
		return items[var1];
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	public boolean hasQuest() {
		return getQuest() != null;
	}

	@Override
	public boolean isItemValidForSlot(final int var1, final ItemStack var2) {
		return true;
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer var1) {
		return true;
	}

	public boolean isValid() {
		return !subject.isEmpty() && !message.hasNoTags() && !sender.isEmpty();
	}

	@Override
	public void markDirty() {
	}

	@Override
	public void openInventory(final EntityPlayer player) {
	}

	public void readNBT(final NBTTagCompound compound) {
		subject = compound.getString("Subject");
		sender = compound.getString("Sender");
		time = compound.getLong("Time");
		beenRead = compound.getBoolean("BeenRead");
		message = compound.getCompoundTag("Message");
		timePast = compound.getLong("TimePast");
		if (compound.hasKey("MailQuest")) {
			questId = compound.getInteger("MailQuest");
		}
		questTitle = compound.getString("MailQuestTitle");
		items = new ItemStack[getSizeInventory()];
		final NBTTagList nbttaglist = compound.getTagList("MailItems", 10);
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			final int j = nbttagcompound1.getByte("Slot") & 0xFF;
			if ((j >= 0) && (j < items.length)) {
				items[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public ItemStack removeStackFromSlot(final int var1) {
		if (items[var1] != null) {
			final ItemStack itemstack = items[var1];
			items[var1] = null;
			return itemstack;
		}
		return null;
	}

	@Override
	public void setField(final int id, final int value) {
	}

	@Override
	public void setInventorySlotContents(final int par1, final ItemStack par2ItemStack) {
		items[par1] = par2ItemStack;
		if ((par2ItemStack != null) && (par2ItemStack.stackSize > getInventoryStackLimit())) {
			par2ItemStack.stackSize = getInventoryStackLimit();
		}
		markDirty();
	}

	public NBTTagCompound writeNBT() {
		final NBTTagCompound compound = new NBTTagCompound();
		compound.setString("Subject", subject);
		compound.setString("Sender", sender);
		compound.setLong("Time", time);
		compound.setBoolean("BeenRead", beenRead);
		compound.setTag("Message", message);
		compound.setLong("TimePast", System.currentTimeMillis() - time);
		compound.setInteger("MailQuest", questId);
		if (hasQuest()) {
			compound.setString("MailQuestTitle", getQuest().title);
		}
		final NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < items.length; ++i) {
			if (items[i] != null) {
				final NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				items[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		compound.setTag("MailItems", nbttaglist);
		return compound;
	}
}

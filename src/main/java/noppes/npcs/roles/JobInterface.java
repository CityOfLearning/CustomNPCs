//

//

package noppes.npcs.roles;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.entity.data.INPCJob;
import noppes.npcs.entity.EntityNPCInterface;

public abstract class JobInterface implements INPCJob {
	public EntityNPCInterface npc;
	public boolean overrideMainHand;
	public boolean overrideOffHand;
	protected IItemStack mainhand;
	protected IItemStack offhand;

	public JobInterface(final EntityNPCInterface npc) {
		overrideMainHand = false;
		overrideOffHand = false;
		mainhand = null;
		offhand = null;
		this.npc = npc;
	}

	public boolean aiContinueExecute() {
		return aiShouldExecute();
	}

	public boolean aiShouldExecute() {
		return false;
	}

	public void aiStartExecuting() {
	}

	public void aiUpdateTask() {
	}

	public void delete() {
	}

	public IItemStack getMainhand() {
		return mainhand;
	}

	public int getMutexBits() {
		return 0;
	}

	public IItemStack getOffhand() {
		return offhand;
	}

	@Override
	public int getType() {
		return npc.advanced.job;
	}

	public String itemToString(final ItemStack item) {
		if ((item == null) || (item.getItem() == null)) {
			return null;
		}
		return Item.itemRegistry.getNameForObject(item.getItem()) + " - " + item.getItemDamage();
	}

	public void killed() {
	}

	public abstract void readFromNBT(final NBTTagCompound p0);

	public void reset() {
	}

	public void resetTask() {
	}

	public ItemStack stringToItem(String s) {
		if (s.isEmpty()) {
			return null;
		}
		int damage = 0;
		if (s.contains(" - ")) {
			final String[] split = s.split(" - ");
			if (split.length == 2) {
				try {
					damage = Integer.parseInt(split[1]);
				} catch (NumberFormatException ex) {
				}
				s = split[0];
			}
		}
		final Item item = Item.getByNameOrId(s);
		if (item == null) {
			return null;
		}
		return new ItemStack(item, 1, damage);
	}

	public abstract NBTTagCompound writeToNBT(final NBTTagCompound p0);
}

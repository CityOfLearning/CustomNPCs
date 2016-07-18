//

//

package noppes.npcs.api.wrapper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.entity.IEntityLiving;

public class ItemStackWrapper implements IItemStack {
	public static ItemStack MCItem(final IItemStack item) {
		if (item == null) {
			return null;
		}
		return item.getMCItemStack();
	}

	public ItemStack item;

	public ItemStackWrapper(final ItemStack item) {
		this.item = item;
	}

	@Override
	public void damageItem(final int damage, final IEntityLiving living) {
		item.damageItem(damage, (living == null) ? null : living.getMCEntity());
	}

	@Override
	public String getDisplayName() {
		return item.getDisplayName();
	}

	@Override
	public int getItemDamage() {
		return item.getItemDamage();
	}

	@Override
	public String getItemName() {
		return item.getItem().getItemStackDisplayName(item);
	}

	@Override
	public ItemStack getMCItemStack() {
		return item;
	}

	@Override
	public String getName() {
		return Item.itemRegistry.getNameForObject(item.getItem()) + "";
	}

	@Override
	public int getStackSize() {
		return item.stackSize;
	}

	private NBTTagCompound getTag() {
		if (!item.hasTagCompound()) {
			item.setTagCompound(new NBTTagCompound());
		}
		return item.getTagCompound();
	}

	@Override
	public Object getTag(final String key) {
		final NBTBase tag = this.getTag().getTag(key);
		if (tag == null) {
			return null;
		}
		if (tag instanceof NBTBase.NBTPrimitive) {
			return ((NBTBase.NBTPrimitive) tag).getDouble();
		}
		if (tag instanceof NBTTagString) {
			return ((NBTTagString) tag).getString();
		}
		return tag;
	}

	@Override
	public boolean hasCustomName() {
		return item.hasDisplayName();
	}

	@Override
	public boolean hasEnchant(final int id) {
		if (!isEnchanted()) {
			return false;
		}
		final NBTTagList list = item.getEnchantmentTagList();
		for (int i = 0; i < list.tagCount(); ++i) {
			final NBTTagCompound compound = list.getCompoundTagAt(i);
			if (compound.getShort("id") == id) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasTag(final String key) {
		return this.getTag().hasKey(key);
	}

	@Override
	public boolean isBlock() {
		final Block block = Block.getBlockFromItem(item.getItem());
		return (block != null) && (block != Blocks.air);
	}

	@Override
	public boolean isEnchanted() {
		return item.isItemEnchanted();
	}

	@Override
	public void setCustomName(final String name) {
		item.setStackDisplayName(name);
	}

	@Override
	public void setItemDamage(final int value) {
		item.setItemDamage(value);
	}

	@Override
	public void setStackSize(int size) {
		if (size < 0) {
			size = 1;
		} else if (size > 64) {
			size = 64;
		}
		item.stackSize = size;
	}

	@Override
	public void setTag(final String key, final Object value) {
		if (value instanceof Number) {
			this.getTag().setDouble(key, ((Number) value).doubleValue());
		} else if (value instanceof String) {
			this.getTag().setString(key, (String) value);
		}
	}
}

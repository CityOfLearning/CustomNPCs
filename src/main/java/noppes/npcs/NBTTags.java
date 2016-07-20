//

//

package noppes.npcs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.controllers.script.IScriptHandler;
import noppes.npcs.controllers.script.ScriptContainer;

public class NBTTags {
	public static HashMap<Integer, Boolean> getBooleanList(final NBTTagList tagList) {
		final HashMap<Integer, Boolean> list = new HashMap<Integer, Boolean>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			list.put(nbttagcompound.getInteger("Slot"), nbttagcompound.getBoolean("Boolean"));
		}
		return list;
	}

	public static Map<Integer, IItemStack> getIItemStackMap(final NBTTagList tagList) {
		final Map<Integer, IItemStack> list = new HashMap<Integer, IItemStack>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			final ItemStack item = ItemStack.loadItemStackFromNBT(nbttagcompound);
			if (item != null) {
				try {
					list.put(nbttagcompound.getByte("Slot") & 0xFF, new ItemStackWrapper(item));
				} catch (ClassCastException e) {
					list.put(nbttagcompound.getInteger("Slot"), new ItemStackWrapper(item));
				}
			}
		}
		return list;
	}

	public static ArrayList<int[]> getIntegerArraySet(final NBTTagList tagList) {
		final ArrayList<int[]> set = new ArrayList<int[]>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			final NBTTagCompound compound = tagList.getCompoundTagAt(i);
			set.add(compound.getIntArray("Array"));
		}
		return set;
	}

	public static HashMap<Integer, Integer> getIntegerIntegerMap(final NBTTagList tagList) {
		final HashMap<Integer, Integer> list = new HashMap<Integer, Integer>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			list.put(nbttagcompound.getInteger("Slot"), nbttagcompound.getInteger("Integer"));
		}
		return list;
	}

	public static List<Integer> getIntegerList(final NBTTagList tagList) {
		final List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			list.add(nbttagcompound.getInteger("Integer"));
		}
		return list;
	}

	public static HashMap<Integer, Long> getIntegerLongMap(final NBTTagList tagList) {
		final HashMap<Integer, Long> list = new HashMap<Integer, Long>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			list.put(nbttagcompound.getInteger("Slot"), nbttagcompound.getLong("Long"));
		}
		return list;
	}

	public static HashSet<Integer> getIntegerSet(final NBTTagList tagList) {
		final HashSet<Integer> list = new HashSet<Integer>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			list.add(nbttagcompound.getInteger("Integer"));
		}
		return list;
	}

	public static HashMap<Integer, String> getIntegerStringMap(final NBTTagList tagList) {
		final HashMap<Integer, String> list = new HashMap<Integer, String>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			list.put(nbttagcompound.getInteger("Slot"), nbttagcompound.getString("Value"));
		}
		return list;
	}

	public static ItemStack[] getItemStackArray(final NBTTagList tagList) {
		final ItemStack[] list = new ItemStack[tagList.tagCount()];
		for (int i = 0; i < tagList.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			list[nbttagcompound.getByte("Slot") & 0xFF] = ItemStack.loadItemStackFromNBT(nbttagcompound);
		}
		return list;
	}

	public static HashMap<Integer, ItemStack> getItemStackList(final NBTTagList tagList) {
		final HashMap<Integer, ItemStack> list = new HashMap<Integer, ItemStack>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			try {
				list.put(nbttagcompound.getByte("Slot") & 0xFF, ItemStack.loadItemStackFromNBT(nbttagcompound));
			} catch (ClassCastException e) {
				list.put(nbttagcompound.getInteger("Slot"), ItemStack.loadItemStackFromNBT(nbttagcompound));
			}
		}
		return list;
	}

	public static Map<Long, String> GetLongStringMap(final NBTTagList tagList) {
		final HashMap<Long, String> list = new HashMap<Long, String>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			list.put(nbttagcompound.getLong("Long"), nbttagcompound.getString("String"));
		}
		return list;
	}

	public static List<ScriptContainer> GetScript(final NBTTagList list, final IScriptHandler handler) {
		final List<ScriptContainer> scripts = new ArrayList<ScriptContainer>();
		for (int i = 0; i < list.tagCount(); ++i) {
			final NBTTagCompound compoundd = list.getCompoundTagAt(i);
			final ScriptContainer script = new ScriptContainer(handler);
			script.readFromNBT(compoundd);
			scripts.add(script);
		}
		return scripts;
	}

	public static String[] getStringArray(final NBTTagList tagList, final int size) {
		final String[] arr = new String[size];
		for (int i = 0; i < tagList.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			final String line = nbttagcompound.getString("Value");
			final int slot = nbttagcompound.getInteger("Slot");
			arr[slot] = line;
		}
		return arr;
	}

	public static HashMap<String, Integer> getStringIntegerMap(final NBTTagList tagList) {
		final HashMap<String, Integer> list = new HashMap<String, Integer>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			list.put(nbttagcompound.getString("Slot"), nbttagcompound.getInteger("Value"));
		}
		return list;
	}

	public static List<String> getStringList(final NBTTagList tagList) {
		final List<String> list = new ArrayList<String>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			final String line = nbttagcompound.getString("Line");
			list.add(line);
		}
		return list;
	}

	public static HashMap<String, String> getStringStringMap(final NBTTagList tagList) {
		final HashMap<String, String> list = new HashMap<String, String>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			list.put(nbttagcompound.getString("Slot"), nbttagcompound.getString("Value"));
		}
		return list;
	}

	public static HashMap<String, Vector<String>> getVectorMap(final NBTTagList tagList) {
		final HashMap<String, Vector<String>> map = new HashMap<String, Vector<String>>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			final Vector<String> values = new Vector<String>();
			final NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			final NBTTagList list = nbttagcompound.getTagList("Values", 10);
			for (int j = 0; j < list.tagCount(); ++j) {
				final NBTTagCompound value = list.getCompoundTagAt(j);
				values.add(value.getString("Value"));
			}
			map.put(nbttagcompound.getString("Key"), values);
		}
		return map;
	}

	public static NBTTagList nbtBooleanList(final HashMap<Integer, Boolean> updatedSlots) {
		final NBTTagList nbttaglist = new NBTTagList();
		if (updatedSlots == null) {
			return nbttaglist;
		}
		for (final Integer slot : updatedSlots.keySet()) {
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Slot", slot);
			nbttagcompound.setBoolean("Boolean", updatedSlots.get(slot));
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtDoubleList(final double... par1ArrayOfDouble) {
		final NBTTagList nbttaglist = new NBTTagList();
		for (final double d1 : par1ArrayOfDouble) {
			nbttaglist.appendTag(new NBTTagDouble(d1));
		}
		return nbttaglist;
	}

	public static NBTTagList nbtIItemStackMap(final Map<Integer, IItemStack> inventory) {
		final NBTTagList nbttaglist = new NBTTagList();
		if (inventory == null) {
			return nbttaglist;
		}
		for (final int slot : inventory.keySet()) {
			final IItemStack item = inventory.get(slot);
			if (item == null) {
				continue;
			}
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setByte("Slot", (byte) slot);
			item.getMCItemStack().writeToNBT(nbttagcompound);
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtIntegerArraySet(final List<int[]> set) {
		final NBTTagList nbttaglist = new NBTTagList();
		if (set == null) {
			return nbttaglist;
		}
		for (final int[] arr : set) {
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setIntArray("Array", arr);
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtIntegerCollection(final Collection<Integer> set) {
		final NBTTagList nbttaglist = new NBTTagList();
		if (set == null) {
			return nbttaglist;
		}
		for (final int slot : set) {
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Integer", slot);
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtIntegerIntegerMap(final Map<Integer, Integer> lines) {
		final NBTTagList nbttaglist = new NBTTagList();
		if (lines == null) {
			return nbttaglist;
		}
		for (final int slot : lines.keySet()) {
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Slot", slot);
			nbttagcompound.setInteger("Integer", lines.get(slot));
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtIntegerLongMap(final HashMap<Integer, Long> lines) {
		final NBTTagList nbttaglist = new NBTTagList();
		if (lines == null) {
			return nbttaglist;
		}
		for (final int slot : lines.keySet()) {
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Slot", slot);
			nbttagcompound.setLong("Long", lines.get(slot));
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTBase nbtIntegerStringMap(final HashMap<Integer, String> map) {
		final NBTTagList nbttaglist = new NBTTagList();
		if (map == null) {
			return nbttaglist;
		}
		for (final int slot : map.keySet()) {
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Slot", slot);
			nbttagcompound.setString("Value", map.get(slot));
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtItemStackArray(final ItemStack[] inventory) {
		final NBTTagList nbttaglist = new NBTTagList();
		if (inventory == null) {
			return nbttaglist;
		}
		for (int slot = 0; slot < inventory.length; ++slot) {
			final ItemStack item = inventory[slot];
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setByte("Slot", (byte) slot);
			if (item != null) {
				item.writeToNBT(nbttagcompound);
			}
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtItemStackList(final HashMap<Integer, ItemStack> inventory) {
		final NBTTagList nbttaglist = new NBTTagList();
		if (inventory == null) {
			return nbttaglist;
		}
		for (final int slot : inventory.keySet()) {
			final ItemStack item = inventory.get(slot);
			if (item == null) {
				continue;
			}
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setByte("Slot", (byte) slot);
			item.writeToNBT(nbttagcompound);
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList NBTLongStringMap(final Map<Long, String> map) {
		final NBTTagList nbttaglist = new NBTTagList();
		if (map == null) {
			return nbttaglist;
		}
		for (final long slot : map.keySet()) {
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setLong("Long", slot);
			nbttagcompound.setString("String", map.get(slot));
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagCompound NBTMerge(final NBTTagCompound data, final NBTTagCompound merge) {
		final NBTTagCompound compound = (NBTTagCompound) data.copy();
		final Set<String> names = merge.getKeySet();
		for (final String name : names) {
			NBTBase base = merge.getTag(name);
			if (base.getId() == 10) {
				base = NBTMerge(compound.getCompoundTag(name), (NBTTagCompound) base);
			}
			compound.setTag(name, base);
		}
		return compound;
	}

	public static NBTTagList NBTScript(final List<ScriptContainer> scripts) {
		final NBTTagList list = new NBTTagList();
		for (final ScriptContainer script : scripts) {
			final NBTTagCompound compound = new NBTTagCompound();
			script.writeToNBT(compound);
			list.appendTag(compound);
		}
		return list;
	}

	public static NBTTagList nbtStringArray(final String[] list) {
		final NBTTagList nbttaglist = new NBTTagList();
		if (list == null) {
			return nbttaglist;
		}
		for (int i = 0; i < list.length; ++i) {
			if (list[i] != null) {
				final NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setString("Value", list[i]);
				nbttagcompound.setInteger("Slot", i);
				nbttaglist.appendTag(nbttagcompound);
			}
		}
		return nbttaglist;
	}

	public static NBTTagList nbtStringIntegerMap(final HashMap<String, Integer> map) {
		final NBTTagList nbttaglist = new NBTTagList();
		if (map == null) {
			return nbttaglist;
		}
		for (final String slot : map.keySet()) {
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setString("Slot", slot);
			nbttagcompound.setInteger("Value", map.get(slot));
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtStringList(final List<String> list) {
		final NBTTagList nbttaglist = new NBTTagList();
		for (final String s : list) {
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setString("Line", s);
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtStringStringMap(final HashMap<String, String> map) {
		final NBTTagList nbttaglist = new NBTTagList();
		if (map == null) {
			return nbttaglist;
		}
		for (final String slot : map.keySet()) {
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setString("Slot", slot);
			nbttagcompound.setString("Value", map.get(slot));
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtVectorMap(final HashMap<String, Vector<String>> map) {
		final NBTTagList list = new NBTTagList();
		if (map == null) {
			return list;
		}
		for (final String key : map.keySet()) {
			final NBTTagCompound compound = new NBTTagCompound();
			compound.setString("Key", key);
			final NBTTagList values = new NBTTagList();
			for (final String value : map.get(key)) {
				final NBTTagCompound comp = new NBTTagCompound();
				comp.setString("Value", value);
				values.appendTag(comp);
			}
			compound.setTag("Values", values);
			list.appendTag(compound);
		}
		return list;
	}
}

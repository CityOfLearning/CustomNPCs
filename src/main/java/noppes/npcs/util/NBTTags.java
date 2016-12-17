
package noppes.npcs.util;

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
	public static HashMap<Integer, Boolean> getBooleanList(NBTTagList tagList) {
		HashMap<Integer, Boolean> list = new HashMap<Integer, Boolean>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			list.put(nbttagcompound.getInteger("Slot"), nbttagcompound.getBoolean("Boolean"));
		}
		return list;
	}

	public static Map<Integer, IItemStack> getIItemStackMap(NBTTagList tagList) {
		Map<Integer, IItemStack> list = new HashMap<Integer, IItemStack>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			ItemStack item = ItemStack.loadItemStackFromNBT(nbttagcompound);
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

	public static ArrayList<int[]> getIntegerArraySet(NBTTagList tagList) {
		ArrayList<int[]> set = new ArrayList<int[]>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound compound = tagList.getCompoundTagAt(i);
			set.add(compound.getIntArray("Array"));
		}
		return set;
	}

	public static HashMap<Integer, Integer> getIntegerIntegerMap(NBTTagList tagList) {
		HashMap<Integer, Integer> list = new HashMap<Integer, Integer>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			list.put(nbttagcompound.getInteger("Slot"), nbttagcompound.getInteger("Integer"));
		}
		return list;
	}

	public static List<Integer> getIntegerList(NBTTagList tagList) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			list.add(nbttagcompound.getInteger("Integer"));
		}
		return list;
	}

	public static HashMap<Integer, Long> getIntegerLongMap(NBTTagList tagList) {
		HashMap<Integer, Long> list = new HashMap<Integer, Long>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			list.put(nbttagcompound.getInteger("Slot"), nbttagcompound.getLong("Long"));
		}
		return list;
	}

	public static HashSet<Integer> getIntegerSet(NBTTagList tagList) {
		HashSet<Integer> list = new HashSet<Integer>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			list.add(nbttagcompound.getInteger("Integer"));
		}
		return list;
	}

	public static HashMap<Integer, String> getIntegerStringMap(NBTTagList tagList) {
		HashMap<Integer, String> list = new HashMap<Integer, String>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			list.put(nbttagcompound.getInteger("Slot"), nbttagcompound.getString("Value"));
		}
		return list;
	}

	public static ItemStack[] getItemStackArray(NBTTagList tagList) {
		ItemStack[] list = new ItemStack[tagList.tagCount()];
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			list[nbttagcompound.getByte("Slot") & 0xFF] = ItemStack.loadItemStackFromNBT(nbttagcompound);
		}
		return list;
	}

	public static HashMap<Integer, ItemStack> getItemStackList(NBTTagList tagList) {
		HashMap<Integer, ItemStack> list = new HashMap<Integer, ItemStack>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			try {
				list.put(nbttagcompound.getByte("Slot") & 0xFF, ItemStack.loadItemStackFromNBT(nbttagcompound));
			} catch (ClassCastException e) {
				list.put(nbttagcompound.getInteger("Slot"), ItemStack.loadItemStackFromNBT(nbttagcompound));
			}
		}
		return list;
	}

	public static Map<Long, String> GetLongStringMap(NBTTagList tagList) {
		HashMap<Long, String> list = new HashMap<Long, String>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			list.put(nbttagcompound.getLong("Long"), nbttagcompound.getString("String"));
		}
		return list;
	}

	public static List<ScriptContainer> GetScript(NBTTagList list, IScriptHandler handler) {
		List<ScriptContainer> scripts = new ArrayList<ScriptContainer>();
		for (int i = 0; i < list.tagCount(); ++i) {
			NBTTagCompound compoundd = list.getCompoundTagAt(i);
			ScriptContainer script = new ScriptContainer(handler);
			script.readFromNBT(compoundd);
			scripts.add(script);
		}
		return scripts;
	}

	public static String[] getStringArray(NBTTagList tagList, int size) {
		String[] arr = new String[size];
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			String line = nbttagcompound.getString("Value");
			int slot = nbttagcompound.getInteger("Slot");
			arr[slot] = line;
		}
		return arr;
	}

	public static HashMap<String, Integer> getStringIntegerMap(NBTTagList tagList) {
		HashMap<String, Integer> list = new HashMap<String, Integer>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			list.put(nbttagcompound.getString("Slot"), nbttagcompound.getInteger("Value"));
		}
		return list;
	}

	public static List<String> getStringList(NBTTagList tagList) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			String line = nbttagcompound.getString("Line");
			list.add(line);
		}
		return list;
	}

	public static HashMap<String, String> getStringStringMap(NBTTagList tagList) {
		HashMap<String, String> list = new HashMap<String, String>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			list.put(nbttagcompound.getString("Slot"), nbttagcompound.getString("Value"));
		}
		return list;
	}

	public static HashMap<String, Vector<String>> getVectorMap(NBTTagList tagList) {
		HashMap<String, Vector<String>> map = new HashMap<String, Vector<String>>();
		for (int i = 0; i < tagList.tagCount(); ++i) {
			Vector<String> values = new Vector<String>();
			NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			NBTTagList list = nbttagcompound.getTagList("Values", 10);
			for (int j = 0; j < list.tagCount(); ++j) {
				NBTTagCompound value = list.getCompoundTagAt(j);
				values.add(value.getString("Value"));
			}
			map.put(nbttagcompound.getString("Key"), values);
		}
		return map;
	}

	public static NBTTagList nbtBooleanList(HashMap<Integer, Boolean> updatedSlots) {
		NBTTagList nbttaglist = new NBTTagList();
		if (updatedSlots == null) {
			return nbttaglist;
		}
		for (Integer slot : updatedSlots.keySet()) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Slot", slot);
			nbttagcompound.setBoolean("Boolean", updatedSlots.get(slot));
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtDoubleList(double... par1ArrayOfDouble) {
		NBTTagList nbttaglist = new NBTTagList();
		for (double d1 : par1ArrayOfDouble) {
			nbttaglist.appendTag(new NBTTagDouble(d1));
		}
		return nbttaglist;
	}

	public static NBTTagList nbtIItemStackMap(Map<Integer, IItemStack> inventory) {
		NBTTagList nbttaglist = new NBTTagList();
		if (inventory == null) {
			return nbttaglist;
		}
		for (int slot : inventory.keySet()) {
			IItemStack item = inventory.get(slot);
			if (item == null) {
				continue;
			}
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setByte("Slot", (byte) slot);
			item.getMCItemStack().writeToNBT(nbttagcompound);
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtIntegerArraySet(List<int[]> set) {
		NBTTagList nbttaglist = new NBTTagList();
		if (set == null) {
			return nbttaglist;
		}
		for (int[] arr : set) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setIntArray("Array", arr);
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtIntegerCollection(Collection<Integer> set) {
		NBTTagList nbttaglist = new NBTTagList();
		if (set == null) {
			return nbttaglist;
		}
		for (int slot : set) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Integer", slot);
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtIntegerIntegerMap(Map<Integer, Integer> lines) {
		NBTTagList nbttaglist = new NBTTagList();
		if (lines == null) {
			return nbttaglist;
		}
		for (int slot : lines.keySet()) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Slot", slot);
			nbttagcompound.setInteger("Integer", lines.get(slot));
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtIntegerLongMap(HashMap<Integer, Long> lines) {
		NBTTagList nbttaglist = new NBTTagList();
		if (lines == null) {
			return nbttaglist;
		}
		for (int slot : lines.keySet()) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Slot", slot);
			nbttagcompound.setLong("Long", lines.get(slot));
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTBase nbtIntegerStringMap(HashMap<Integer, String> map) {
		NBTTagList nbttaglist = new NBTTagList();
		if (map == null) {
			return nbttaglist;
		}
		for (int slot : map.keySet()) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Slot", slot);
			nbttagcompound.setString("Value", map.get(slot));
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtItemStackArray(ItemStack[] inventory) {
		NBTTagList nbttaglist = new NBTTagList();
		if (inventory == null) {
			return nbttaglist;
		}
		for (int slot = 0; slot < inventory.length; ++slot) {
			ItemStack item = inventory[slot];
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setByte("Slot", (byte) slot);
			if (item != null) {
				item.writeToNBT(nbttagcompound);
			}
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtItemStackList(HashMap<Integer, ItemStack> inventory) {
		NBTTagList nbttaglist = new NBTTagList();
		if (inventory == null) {
			return nbttaglist;
		}
		for (int slot : inventory.keySet()) {
			ItemStack item = inventory.get(slot);
			if (item == null) {
				continue;
			}
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setByte("Slot", (byte) slot);
			item.writeToNBT(nbttagcompound);
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList NBTLongStringMap(Map<Long, String> map) {
		NBTTagList nbttaglist = new NBTTagList();
		if (map == null) {
			return nbttaglist;
		}
		for (long slot : map.keySet()) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setLong("Long", slot);
			nbttagcompound.setString("String", map.get(slot));
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagCompound NBTMerge(NBTTagCompound data, NBTTagCompound merge) {
		NBTTagCompound compound = (NBTTagCompound) data.copy();
		Set<String> names = merge.getKeySet();
		for (String name : names) {
			NBTBase base = merge.getTag(name);
			if (base.getId() == 10) {
				base = NBTMerge(compound.getCompoundTag(name), (NBTTagCompound) base);
			}
			compound.setTag(name, base);
		}
		return compound;
	}

	public static NBTTagList NBTScript(List<ScriptContainer> scripts) {
		NBTTagList list = new NBTTagList();
		for (ScriptContainer script : scripts) {
			NBTTagCompound compound = new NBTTagCompound();
			script.writeToNBT(compound);
			list.appendTag(compound);
		}
		return list;
	}

	public static NBTTagList nbtStringArray(String[] list) {
		NBTTagList nbttaglist = new NBTTagList();
		if (list == null) {
			return nbttaglist;
		}
		for (int i = 0; i < list.length; ++i) {
			if (list[i] != null) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setString("Value", list[i]);
				nbttagcompound.setInteger("Slot", i);
				nbttaglist.appendTag(nbttagcompound);
			}
		}
		return nbttaglist;
	}

	public static NBTTagList nbtStringIntegerMap(HashMap<String, Integer> map) {
		NBTTagList nbttaglist = new NBTTagList();
		if (map == null) {
			return nbttaglist;
		}
		for (String slot : map.keySet()) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setString("Slot", slot);
			nbttagcompound.setInteger("Value", map.get(slot));
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtStringList(List<String> list) {
		NBTTagList nbttaglist = new NBTTagList();
		for (String s : list) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setString("Line", s);
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtStringStringMap(HashMap<String, String> map) {
		NBTTagList nbttaglist = new NBTTagList();
		if (map == null) {
			return nbttaglist;
		}
		for (String slot : map.keySet()) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setString("Slot", slot);
			nbttagcompound.setString("Value", map.get(slot));
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtVectorMap(HashMap<String, Vector<String>> map) {
		NBTTagList list = new NBTTagList();
		if (map == null) {
			return list;
		}
		for (String key : map.keySet()) {
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("Key", key);
			NBTTagList values = new NBTTagList();
			for (String value : map.get(key)) {
				NBTTagCompound comp = new NBTTagCompound();
				comp.setString("Value", value);
				values.appendTag(comp);
			}
			compound.setTag("Values", values);
			list.appendTag(compound);
		}
		return list;
	}
}

//

//

package noppes.npcs.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.CustomNpcs;
import noppes.npcs.LogWriter;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.util.NBTJsonUtil;

public class QuestController {
	public static QuestController instance;
	public HashMap<Integer, QuestCategory> categories;
	public HashMap<Integer, Quest> quests;
	private int lastUsedCatID;
	private int lastUsedQuestID;

	public QuestController() {
		categories = new HashMap<Integer, QuestCategory>();
		quests = new HashMap<Integer, Quest>();
		lastUsedCatID = 0;
		lastUsedQuestID = 0;
		QuestController.instance = this;
	}

	private boolean containsCategoryName(String name) {
		name = name.toLowerCase();
		for (final QuestCategory cat : categories.values()) {
			if (cat.title.toLowerCase().equals(name)) {
				return true;
			}
		}
		return false;
	}

	private boolean containsQuestName(final QuestCategory category, final Quest quest) {
		for (final Quest q : category.quests.values()) {
			if ((q.id != quest.id) && q.title.equalsIgnoreCase(quest.title)) {
				return true;
			}
		}
		return false;
	}

	private File getDir() {
		return new File(CustomNpcs.getWorldSaveDirectory(), "quests");
	}

	public void load() {
		categories.clear();
		quests.clear();
		lastUsedCatID = 0;
		lastUsedQuestID = 0;
		try {
			File file = new File(CustomNpcs.getWorldSaveDirectory(), "quests.dat");
			if (file.exists()) {
				loadCategoriesOld(file);
				file.delete();
				file = new File(CustomNpcs.getWorldSaveDirectory(), "quests.dat_old");
				if (file.exists()) {
					file.delete();
				}
				return;
			}
		} catch (Exception ex) {
		}
		final File dir = getDir();
		if (!dir.exists()) {
			dir.mkdir();
		} else {
			for (final File file2 : dir.listFiles()) {
				if (file2.isDirectory()) {
					final QuestCategory category = loadCategoryDir(file2);
					final Iterator<Integer> ite = category.quests.keySet().iterator();
					while (ite.hasNext()) {
						final int id = ite.next();
						if (id > lastUsedQuestID) {
							lastUsedQuestID = id;
						}
						final Quest quest = category.quests.get(id);
						if (quests.containsKey(id)) {
							LogWriter.error("Duplicate id " + quest.id + " from category " + category.title);
							ite.remove();
						} else {
							quests.put(id, quest);
						}
					}
					++lastUsedCatID;
					category.id = lastUsedCatID;
					categories.put(category.id, category);
				}
			}
		}
	}

	private void loadCategoriesOld(final File file) throws Exception {
		final NBTTagCompound nbttagcompound1 = CompressedStreamTools.readCompressed(new FileInputStream(file));
		lastUsedCatID = nbttagcompound1.getInteger("lastID");
		lastUsedQuestID = nbttagcompound1.getInteger("lastQuestID");
		final NBTTagList list = nbttagcompound1.getTagList("Data", 10);
		if (list != null) {
			for (int i = 0; i < list.tagCount(); ++i) {
				final QuestCategory category = new QuestCategory();
				category.readNBT(list.getCompoundTagAt(i));
				categories.put(category.id, category);
				saveCategory(category);
				final Iterator<Map.Entry<Integer, Quest>> ita = category.quests.entrySet().iterator();
				while (ita.hasNext()) {
					final Map.Entry<Integer, Quest> entry = ita.next();
					final Quest quest = entry.getValue();
					quest.id = entry.getKey();
					quest.category = category;
					if (quests.containsKey(quest.id)) {
						ita.remove();
					} else {
						saveQuest(category.id, quest);
					}
				}
			}
		}
	}

	private QuestCategory loadCategoryDir(final File dir) {
		final QuestCategory category = new QuestCategory();
		category.title = dir.getName();
		for (final File file : dir.listFiles()) {
			if (file.isFile()) {
				if (file.getName().endsWith(".json")) {
					try {
						final Quest quest = new Quest();
						quest.id = Integer.parseInt(file.getName().substring(0, file.getName().length() - 5));
						quest.readNBTPartial(NBTJsonUtil.LoadFile(file));
						category.quests.put(quest.id, quest);
						quest.category = category;
					} catch (Exception e) {
						LogWriter.error("Error loading: " + file.getAbsolutePath(), e);
					}
				}
			}
		}
		return category;
	}

	public void removeCategory(final int category) {
		final QuestCategory cat = categories.get(category);
		if (cat == null) {
			return;
		}
		final File dir = new File(getDir(), cat.title);
		if (!dir.delete()) {
			return;
		}
		for (final int dia : cat.quests.keySet()) {
			quests.remove(dia);
		}
		categories.remove(category);
	}

	public void removeQuest(final Quest quest) {
		final File file = new File(new File(getDir(), quest.category.title), quest.id + ".json");
		if (!file.delete()) {
			return;
		}
		quests.remove(quest.id);
		quest.category.quests.remove(quest.id);
	}

	public void saveCategory(final QuestCategory category) {
		category.title = NoppesStringUtils.cleanFileName(category.title);
		if (categories.containsKey(category.id)) {
			final QuestCategory currentCategory = categories.get(category.id);
			if (!currentCategory.title.equals(category.title)) {
				while (containsCategoryName(category.title)) {
					category.title += "_";
				}
				final File newdir = new File(getDir(), category.title);
				final File olddir = new File(getDir(), currentCategory.title);
				if (newdir.exists()) {
					return;
				}
				if (!olddir.renameTo(newdir)) {
					return;
				}
			}
			category.quests = currentCategory.quests;
		} else {
			if (category.id < 0) {
				++lastUsedCatID;
				category.id = lastUsedCatID;
			}
			while (containsCategoryName(category.title)) {
				category.title += "_";
			}
			final File dir = new File(getDir(), category.title);
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}
		categories.put(category.id, category);
	}

	public void saveQuest(final int categoryID, final Quest quest) {
		final QuestCategory category = categories.get(categoryID);
		if (category == null) {
			return;
		}
		quest.category = category;
		while (containsQuestName(quest.category, quest)) {
			quest.title += "_";
		}
		if (quest.id < 0) {
			++lastUsedQuestID;
			quest.id = lastUsedQuestID;
		}
		quests.put(quest.id, quest);
		category.quests.put(quest.id, quest);
		final File dir = new File(getDir(), category.title);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		final File file = new File(dir, quest.id + ".json_new");
		final File file2 = new File(dir, quest.id + ".json");
		try {
			NBTJsonUtil.SaveFile(file, quest.writeToNBTPartial(new NBTTagCompound()));
			if (file2.exists()) {
				file2.delete();
			}
			file.renameTo(file2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

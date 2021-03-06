
package noppes.npcs.controllers.dialog;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.CustomNpcs;
import noppes.npcs.api.constants.EnumOptionType;
import noppes.npcs.util.NBTJsonUtil;
import noppes.npcs.util.NoppesStringUtils;

public class DialogController {
	public static DialogController instance;
	public HashMap<Integer, DialogCategory> categories;
	public HashMap<Integer, Dialog> dialogs;
	private int lastUsedDialogID;
	private int lastUsedCatID;

	public DialogController() {
		categories = new HashMap<>();
		dialogs = new HashMap<>();
		lastUsedDialogID = 0;
		lastUsedCatID = 0;
		(DialogController.instance = this).load();
	}

	private boolean containsCategoryName(String name) {
		name = name.toLowerCase();
		for (DialogCategory cat : categories.values()) {
			if (cat.title.toLowerCase().equals(name)) {
				return true;
			}
		}
		return false;
	}

	private boolean containsDialogName(DialogCategory category, Dialog dialog) {
		for (Dialog dia : category.dialogs.values()) {
			if ((dia.id != dialog.id) && dia.title.equalsIgnoreCase(dialog.title)) {
				return true;
			}
		}
		return false;
	}

	private File getDir() {
		return new File(CustomNpcs.getWorldSaveDirectory(), "dialogs");
	}

	public Map<String, Integer> getScroll() {
		Map<String, Integer> map = new HashMap<>();
		for (DialogCategory category : categories.values()) {
			map.put(category.title, category.id);
		}
		return map;
	}

	public boolean hasDialog(int dialogId) {
		return dialogs.containsKey(dialogId);
	}

	public void load() {
		loadCategories();
	}

	private void loadCategories() {
		categories.clear();
		dialogs.clear();
		lastUsedCatID = 0;
		lastUsedDialogID = 0;
		try {
			File file = new File(CustomNpcs.getWorldSaveDirectory(), "dialog.dat");
			if (file.exists()) {
				loadCategoriesOld(file);
				file.delete();
				file = new File(CustomNpcs.getWorldSaveDirectory(), "dialog.dat_old");
				if (file.exists()) {
					file.delete();
				}
				return;
			}
		} catch (Exception e) {
			CustomNpcs.logger.catching(e);
		}
		File dir = getDir();
		if (!dir.exists()) {
			dir.mkdir();
			loadDefaultDialogs();
		} else {
			for (File file2 : dir.listFiles()) {
				if (file2.isDirectory()) {
					DialogCategory category = loadCategoryDir(file2);
					Iterator<Map.Entry<Integer, Dialog>> ite = category.dialogs.entrySet().iterator();
					while (ite.hasNext()) {
						Map.Entry<Integer, Dialog> entry = ite.next();
						int id = entry.getKey();
						if (id > lastUsedDialogID) {
							lastUsedDialogID = id;
						}
						Dialog dialog = entry.getValue();
						if (dialogs.containsKey(id)) {
							CustomNpcs.logger.error("Duplicate id " + dialog.id + " from category " + category.title);
							ite.remove();
						} else {
							dialogs.put(id, dialog);
						}
					}
					++lastUsedCatID;
					category.id = lastUsedCatID;
					categories.put(category.id, category);
				}
			}
		}
	}

	private void loadCategoriesOld(File file) throws Exception {
		NBTTagCompound nbttagcompound1 = CompressedStreamTools.readCompressed(new FileInputStream(file));
		NBTTagList list = nbttagcompound1.getTagList("Data", 10);
		if (list == null) {
			return;
		}
		for (int i = 0; i < list.tagCount(); ++i) {
			DialogCategory category = new DialogCategory();
			category.readNBT(list.getCompoundTagAt(i));
			saveCategory(category);
			Iterator<Map.Entry<Integer, Dialog>> ita = category.dialogs.entrySet().iterator();
			while (ita.hasNext()) {
				Map.Entry<Integer, Dialog> entry = ita.next();
				Dialog dialog = entry.getValue();
				dialog.id = entry.getKey();
				dialog.category = category;
				if (dialogs.containsKey(dialog.id)) {
					ita.remove();
				} else {
					saveDialog(category.id, dialog);
				}
			}
		}
	}

	private DialogCategory loadCategoryDir(File dir) {
		DialogCategory category = new DialogCategory();
		category.title = dir.getName();
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				if (file.getName().endsWith(".json")) {
					try {
						Dialog dialog = new Dialog();
						dialog.id = Integer.parseInt(file.getName().substring(0, file.getName().length() - 5));
						dialog.readNBTPartial(NBTJsonUtil.LoadFile(file));
						category.dialogs.put(dialog.id, dialog);
						dialog.category = category;
					} catch (Exception e) {
						CustomNpcs.logger.error("Error loading: " + file.getAbsolutePath(), e);
					}
				}
			}
		}
		return category;
	}

	private void loadDefaultDialogs() {
		DialogCategory cat = new DialogCategory();
		cat.id = lastUsedCatID++;
		cat.title = "Villager";
		Dialog dia1 = new Dialog();
		dia1.id = 1;
		dia1.category = cat;
		dia1.title = "Start";
		dia1.text = "Hello {player}, \n\nWelcome to our village. I hope you enjoy your stay";
		Dialog dia2 = new Dialog();
		dia2.id = 2;
		dia2.category = cat;
		dia2.title = "Ask about village";
		dia2.text = "This village has been around for ages. Enjoy your stay here.";
		Dialog dia3 = new Dialog();
		dia3.id = 3;
		dia3.category = cat;
		dia3.title = "Who are you";
		dia3.text = "I'm a villager here. I have lived in this village my whole life.";
		cat.dialogs.put(dia1.id, dia1);
		cat.dialogs.put(dia2.id, dia2);
		cat.dialogs.put(dia3.id, dia3);
		DialogOption option = new DialogOption();
		option.title = "Tell me something about this village";
		option.dialogId = 2;
		option.optionType = EnumOptionType.DIALOG_OPTION;
		DialogOption option2 = new DialogOption();
		option2.title = "Who are you?";
		option2.dialogId = 3;
		option2.optionType = EnumOptionType.DIALOG_OPTION;
		DialogOption option3 = new DialogOption();
		option3.title = "Goodbye";
		option3.optionType = EnumOptionType.QUIT_OPTION;
		dia1.options.put(0, option2);
		dia1.options.put(1, option);
		dia1.options.put(2, option3);
		DialogOption option4 = new DialogOption();
		option4.title = "Back";
		option4.dialogId = 1;
		dia2.options.put(1, option4);
		dia3.options.put(1, option4);
		lastUsedDialogID = 3;
		lastUsedCatID = 1;
		saveCategory(cat);
		saveDialog(cat.id, dia1);
		saveDialog(cat.id, dia2);
		saveDialog(cat.id, dia3);
	}

	public void removeCategory(int category) {
		DialogCategory cat = categories.get(category);
		if (cat == null) {
			return;
		}
		File dir = new File(getDir(), cat.title);
		if (!dir.delete()) {
			return;
		}
		for (int dia : cat.dialogs.keySet()) {
			dialogs.remove(dia);
		}
		categories.remove(category);
	}

	public void removeDialog(Dialog dialog) {
		DialogCategory category = dialog.category;
		File file = new File(new File(getDir(), category.title), dialog.id + ".json");
		if (!file.delete()) {
			return;
		}
		category.dialogs.remove(dialog.id);
		dialogs.remove(dialog.id);
	}

	public void saveCategory(DialogCategory category) {
		category.title = NoppesStringUtils.cleanFileName(category.title);
		if (categories.containsKey(category.id)) {
			DialogCategory currentCategory = categories.get(category.id);
			if (!currentCategory.title.equals(category.title)) {
				while (containsCategoryName(category.title)) {
					category.title += "_";
				}
				File newdir = new File(getDir(), category.title);
				File olddir = new File(getDir(), currentCategory.title);
				if (newdir.exists()) {
					return;
				}
				if (!olddir.renameTo(newdir)) {
					return;
				}
			}
			category.dialogs = currentCategory.dialogs;
		} else {
			if (category.id < 0) {
				++lastUsedCatID;
				category.id = lastUsedCatID;
			}
			while (containsCategoryName(category.title)) {
				category.title += "_";
			}
			File dir = new File(getDir(), category.title);
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}
		categories.put(category.id, category);
	}

	public Dialog saveDialog(int categoryId, Dialog dialog) {
		DialogCategory category = categories.get(categoryId);
		if (category == null) {
			return dialog;
		}
		dialog.category = category;
		while (containsDialogName(dialog.category, dialog)) {
			dialog.title += "_";
		}
		if (dialog.id < 0) {
			++lastUsedDialogID;
			dialog.id = lastUsedDialogID;
		}
		dialogs.put(dialog.id, dialog);
		category.dialogs.put(dialog.id, dialog);
		File dir = new File(getDir(), category.title);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, dialog.id + ".json_new");
		File file2 = new File(dir, dialog.id + ".json");
		try {
			NBTJsonUtil.SaveFile(file, dialog.writeToNBTPartial(new NBTTagCompound()));
			if (file2.exists()) {
				file2.delete();
			}
			file.renameTo(file2);
		} catch (Exception e) {
			CustomNpcs.logger.catching(e);
		}
		return dialog;
	}
}

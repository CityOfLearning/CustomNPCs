//

//

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
import noppes.npcs.LogWriter;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.api.constants.EnumOptionType;
import noppes.npcs.util.NBTJsonUtil;

public class DialogController {
	public static DialogController instance;
	public HashMap<Integer, DialogCategory> categories;
	public HashMap<Integer, Dialog> dialogs;
	private int lastUsedDialogID;
	private int lastUsedCatID;

	public DialogController() {
		categories = new HashMap<Integer, DialogCategory>();
		dialogs = new HashMap<Integer, Dialog>();
		lastUsedDialogID = 0;
		lastUsedCatID = 0;
		(DialogController.instance = this).load();
	}

	private boolean containsCategoryName(String name) {
		name = name.toLowerCase();
		for (final DialogCategory cat : categories.values()) {
			if (cat.title.toLowerCase().equals(name)) {
				return true;
			}
		}
		return false;
	}

	private boolean containsDialogName(final DialogCategory category, final Dialog dialog) {
		for (final Dialog dia : category.dialogs.values()) {
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
		final Map<String, Integer> map = new HashMap<String, Integer>();
		for (final DialogCategory category : categories.values()) {
			map.put(category.title, category.id);
		}
		return map;
	}

	public boolean hasDialog(final int dialogId) {
		return dialogs.containsKey(dialogId);
	}

	public void load() {
		LogWriter.info("Loading Dialogs");
		loadCategories();
		LogWriter.info("Done loading Dialogs");
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
			LogWriter.except(e);
		}
		final File dir = getDir();
		if (!dir.exists()) {
			dir.mkdir();
			loadDefaultDialogs();
		} else {
			for (final File file2 : dir.listFiles()) {
				if (file2.isDirectory()) {
					final DialogCategory category = loadCategoryDir(file2);
					final Iterator<Map.Entry<Integer, Dialog>> ite = category.dialogs.entrySet().iterator();
					while (ite.hasNext()) {
						final Map.Entry<Integer, Dialog> entry = ite.next();
						final int id = entry.getKey();
						if (id > lastUsedDialogID) {
							lastUsedDialogID = id;
						}
						final Dialog dialog = entry.getValue();
						if (dialogs.containsKey(id)) {
							LogWriter.error("Duplicate id " + dialog.id + " from category " + category.title);
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

	private void loadCategoriesOld(final File file) throws Exception {
		final NBTTagCompound nbttagcompound1 = CompressedStreamTools.readCompressed(new FileInputStream(file));
		final NBTTagList list = nbttagcompound1.getTagList("Data", 10);
		if (list == null) {
			return;
		}
		for (int i = 0; i < list.tagCount(); ++i) {
			final DialogCategory category = new DialogCategory();
			category.readNBT(list.getCompoundTagAt(i));
			saveCategory(category);
			final Iterator<Map.Entry<Integer, Dialog>> ita = category.dialogs.entrySet().iterator();
			while (ita.hasNext()) {
				final Map.Entry<Integer, Dialog> entry = ita.next();
				final Dialog dialog = entry.getValue();
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

	private DialogCategory loadCategoryDir(final File dir) {
		final DialogCategory category = new DialogCategory();
		category.title = dir.getName();
		for (final File file : dir.listFiles()) {
			if (file.isFile()) {
				if (file.getName().endsWith(".json")) {
					try {
						final Dialog dialog = new Dialog();
						dialog.id = Integer.parseInt(file.getName().substring(0, file.getName().length() - 5));
						dialog.readNBTPartial(NBTJsonUtil.LoadFile(file));
						category.dialogs.put(dialog.id, dialog);
						dialog.category = category;
					} catch (Exception e) {
						LogWriter.error("Error loading: " + file.getAbsolutePath(), e);
					}
				}
			}
		}
		return category;
	}

	private void loadDefaultDialogs() {
		final DialogCategory cat = new DialogCategory();
		cat.id = lastUsedCatID++;
		cat.title = "Villager";
		final Dialog dia1 = new Dialog();
		dia1.id = 1;
		dia1.category = cat;
		dia1.title = "Start";
		dia1.text = "Hello {player}, \n\nWelcome to our village. I hope you enjoy your stay";
		final Dialog dia2 = new Dialog();
		dia2.id = 2;
		dia2.category = cat;
		dia2.title = "Ask about village";
		dia2.text = "This village has been around for ages. Enjoy your stay here.";
		final Dialog dia3 = new Dialog();
		dia3.id = 3;
		dia3.category = cat;
		dia3.title = "Who are you";
		dia3.text = "I'm a villager here. I have lived in this village my whole life.";
		cat.dialogs.put(dia1.id, dia1);
		cat.dialogs.put(dia2.id, dia2);
		cat.dialogs.put(dia3.id, dia3);
		final DialogOption option = new DialogOption();
		option.title = "Tell me something about this village";
		option.dialogId = 2;
		option.optionType = EnumOptionType.DIALOG_OPTION;
		final DialogOption option2 = new DialogOption();
		option2.title = "Who are you?";
		option2.dialogId = 3;
		option2.optionType = EnumOptionType.DIALOG_OPTION;
		final DialogOption option3 = new DialogOption();
		option3.title = "Goodbye";
		option3.optionType = EnumOptionType.QUIT_OPTION;
		dia1.options.put(0, option2);
		dia1.options.put(1, option);
		dia1.options.put(2, option3);
		final DialogOption option4 = new DialogOption();
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

	public void removeCategory(final int category) {
		final DialogCategory cat = categories.get(category);
		if (cat == null) {
			return;
		}
		final File dir = new File(getDir(), cat.title);
		if (!dir.delete()) {
			return;
		}
		for (final int dia : cat.dialogs.keySet()) {
			dialogs.remove(dia);
		}
		categories.remove(category);
	}

	public void removeDialog(final Dialog dialog) {
		final DialogCategory category = dialog.category;
		final File file = new File(new File(getDir(), category.title), dialog.id + ".json");
		if (!file.delete()) {
			return;
		}
		category.dialogs.remove(dialog.id);
		dialogs.remove(dialog.id);
	}

	public void saveCategory(final DialogCategory category) {
		category.title = NoppesStringUtils.cleanFileName(category.title);
		if (categories.containsKey(category.id)) {
			final DialogCategory currentCategory = categories.get(category.id);
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
			category.dialogs = currentCategory.dialogs;
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

	public Dialog saveDialog(final int categoryId, final Dialog dialog) {
		final DialogCategory category = categories.get(categoryId);
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
		final File dir = new File(getDir(), category.title);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		final File file = new File(dir, dialog.id + ".json_new");
		final File file2 = new File(dir, dialog.id + ".json");
		try {
			NBTJsonUtil.SaveFile(file, dialog.writeToNBTPartial(new NBTTagCompound()));
			if (file2.exists()) {
				file2.delete();
			}
			file.renameTo(file2);
		} catch (Exception e) {
			LogWriter.except(e);
		}
		return dialog;
	}
}

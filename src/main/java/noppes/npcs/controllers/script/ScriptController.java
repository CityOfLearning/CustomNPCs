
package noppes.npcs.controllers.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noppes.npcs.CustomNpcs;
import noppes.npcs.api.wrapper.WorldWrapper;
import noppes.npcs.util.NBTJsonUtil;

public class ScriptController {
	public static ScriptController Instance;
	public static boolean HasStart;
	static {
		ScriptController.HasStart = false;
	}
	private ScriptEngineManager manager;
	public Map<String, String> languages;
	public Map<String, String> scripts;
	public long lastLoaded;
	public File dir;
	public NBTTagCompound compound;

	public boolean shouldSave;

	public ScriptController() {
		languages = new HashMap<>();
		scripts = new HashMap<>();
		lastLoaded = 0L;
		compound = new NBTTagCompound();
		shouldSave = false;
		ScriptController.Instance = this;
		manager = new ScriptEngineManager();
		CustomNpcs.logger.info("Script Engines Available:");
		for (ScriptEngineFactory fac : manager.getEngineFactories()) {
			if (fac.getExtensions().isEmpty()) {
				continue;
			}
			if (!(manager.getEngineByName(fac.getLanguageName()) instanceof Invocable)) {
				continue;
			}
			String ext = "." + fac.getExtensions().get(0).toLowerCase();
			CustomNpcs.logger.info(fac.getLanguageName() + ": " + ext);
			languages.put(fac.getLanguageName(), ext);
		}
	}

	public ScriptEngine getEngineByName(String language) {
		return manager.getEngineByName(language);
	}

	private File getSavedFile() {
		return new File(dir, "world_data.json");
	}

	private List<String> getScripts(String language) {
		List<String> list = new ArrayList<>();
		String ext = languages.get(language);
		if (ext == null) {
			return list;
		}
		for (String script : scripts.keySet()) {
			if (script.endsWith(ext)) {
				list.add(script);
			}
		}
		return list;
	}

	private void loadCategories() {
		dir = new File(CustomNpcs.getWorldSaveDirectory(), "scripts");
		if (!dir.exists()) {
			dir.mkdir();
		}
		if (!getSavedFile().exists()) {
			shouldSave = true;
		}
		WorldWrapper.tempData.clear();
		scripts.clear();
		for (String language : languages.keySet()) {
			String ext = languages.get(language);
			File scriptDir = new File(dir, language.toLowerCase());
			if (!scriptDir.exists()) {
				scriptDir.mkdir();
			} else {
				loadDir(scriptDir, "", ext);
			}
		}
		lastLoaded = System.currentTimeMillis();
	}

	private void loadDir(File dir, String name, String ext) {
		for (File file : dir.listFiles()) {
			String filename = name + file.getName().toLowerCase();
			if (file.isDirectory()) {
				loadDir(file, filename + "/", ext);
			} else if (filename.endsWith(ext)) {
				try {
					scripts.put(filename, readFile(file));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean loadStoredData() {
		loadCategories();
		File file = getSavedFile();
		try {
			if (!file.exists()) {
				return false;
			}
			compound = NBTJsonUtil.LoadFile(file);
			shouldSave = false;
		} catch (Exception e) {
			CustomNpcs.logger.error("Error loading: " + file.getAbsolutePath(), e);
			return false;
		}
		return true;
	}

	public NBTTagList nbtLanguages() {
		NBTTagList list = new NBTTagList();
		for (String language : languages.keySet()) {
			NBTTagCompound compound = new NBTTagCompound();
			NBTTagList scripts = new NBTTagList();
			for (String script : getScripts(language)) {
				scripts.appendTag(new NBTTagString(script));
			}
			compound.setTag("Scripts", scripts);
			compound.setString("Language", language);
			list.appendTag(compound);
		}
		return list;
	}

	private String readFile(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		try {
			StringBuilder sb = new StringBuilder();
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				sb.append(line);
				sb.append("\n");
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}

	@SubscribeEvent
	public void saveWorld(WorldEvent.Save event) {
		if (!shouldSave || event.world.isRemote || (event.world != MinecraftServer.getServer().worldServers[0])) {
			return;
		}
		try {
			NBTJsonUtil.SaveFile(getSavedFile(), compound);
		} catch (Exception e) {
			CustomNpcs.logger.catching(e);
		}
		shouldSave = false;
	}
}

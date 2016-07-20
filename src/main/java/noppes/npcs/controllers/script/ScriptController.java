//

//

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
import noppes.npcs.LogWriter;
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
		languages = new HashMap<String, String>();
		scripts = new HashMap<String, String>();
		lastLoaded = 0L;
		compound = new NBTTagCompound();
		shouldSave = false;
		ScriptController.Instance = this;
		manager = new ScriptEngineManager();
		LogWriter.info("Script Engines Available:");
		for (final ScriptEngineFactory fac : manager.getEngineFactories()) {
			if (fac.getExtensions().isEmpty()) {
				continue;
			}
			if (!(manager.getEngineByName(fac.getLanguageName()) instanceof Invocable)) {
				continue;
			}
			final String ext = "." + fac.getExtensions().get(0).toLowerCase();
			LogWriter.info(fac.getLanguageName() + ": " + ext);
			languages.put(fac.getLanguageName(), ext);
		}
	}

	public ScriptEngine getEngineByName(final String language) {
		return manager.getEngineByName(language);
	}

	private File getSavedFile() {
		return new File(dir, "world_data.json");
	}

	private List<String> getScripts(final String language) {
		final List<String> list = new ArrayList<String>();
		final String ext = languages.get(language);
		if (ext == null) {
			return list;
		}
		for (final String script : scripts.keySet()) {
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
		for (final String language : languages.keySet()) {
			final String ext = languages.get(language);
			final File scriptDir = new File(dir, language.toLowerCase());
			if (!scriptDir.exists()) {
				scriptDir.mkdir();
			} else {
				loadDir(scriptDir, "", ext);
			}
		}
		lastLoaded = System.currentTimeMillis();
	}

	private void loadDir(final File dir, final String name, final String ext) {
		for (final File file : dir.listFiles()) {
			final String filename = name + file.getName().toLowerCase();
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
		final File file = getSavedFile();
		try {
			if (!file.exists()) {
				return false;
			}
			compound = NBTJsonUtil.LoadFile(file);
			shouldSave = false;
		} catch (Exception e) {
			LogWriter.error("Error loading: " + file.getAbsolutePath(), e);
			return false;
		}
		return true;
	}

	public NBTTagList nbtLanguages() {
		final NBTTagList list = new NBTTagList();
		for (final String language : languages.keySet()) {
			final NBTTagCompound compound = new NBTTagCompound();
			final NBTTagList scripts = new NBTTagList();
			for (final String script : getScripts(language)) {
				scripts.appendTag(new NBTTagString(script));
			}
			compound.setTag("Scripts", scripts);
			compound.setString("Language", language);
			list.appendTag(compound);
		}
		return list;
	}

	private String readFile(final File file) throws IOException {
		final BufferedReader br = new BufferedReader(new FileReader(file));
		try {
			final StringBuilder sb = new StringBuilder();
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
	public void saveWorld(final WorldEvent.Save event) {
		if (!shouldSave || event.world.isRemote || (event.world != MinecraftServer.getServer().worldServers[0])) {
			return;
		}
		try {
			NBTJsonUtil.SaveFile(getSavedFile(), compound);
		} catch (Exception e) {
			LogWriter.except(e);
		}
		shouldSave = false;
	}
}

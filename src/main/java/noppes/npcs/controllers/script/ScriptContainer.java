//

//

package noppes.npcs.controllers.script;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.constants.AnimationType;
import noppes.npcs.api.constants.EntityType;
import noppes.npcs.api.constants.JobType;
import noppes.npcs.api.constants.ParticleType;
import noppes.npcs.api.constants.PotionEffectType;
import noppes.npcs.api.constants.RoleType;
import noppes.npcs.api.constants.TacticalType;
import noppes.npcs.constants.EnumScriptType;

public class ScriptContainer {
	private static AnimationType animations;
	private static EntityType entities;
	private static JobType jobs;
	private static RoleType roles;
	private static TacticalType tacticalVariantTypes;
	private static PotionEffectType potionEffectTypes;
	private static ParticleType particleTypes;
	static {
		animations = new AnimationType();
		entities = new EntityType();
		jobs = new JobType();
		roles = new RoleType();
		tacticalVariantTypes = new TacticalType();
		potionEffectTypes = new PotionEffectType();
		particleTypes = new ParticleType();
	}
	public String fullscript;
	public String script;
	public Map<Long, String> console;
	public boolean errored;
	public List<String> scripts;
	private List<Integer> unknownFunctions;
	private long lastCreated;
	private String currentScriptLanguage;
	public ScriptEngine engine;
	private IScriptHandler handler;

	private boolean init;

	public ScriptContainer(IScriptHandler handler) {
		fullscript = "";
		script = "";
		console = new HashMap<Long, String>();
		errored = false;
		scripts = new ArrayList<String>();
		unknownFunctions = new ArrayList<Integer>();
		lastCreated = 0L;
		currentScriptLanguage = null;
		engine = null;
		this.handler = null;
		init = false;
		this.handler = handler;
	}

	public void appandConsole(String message) {
		if ((message == null) || message.isEmpty()) {
			return;
		}
		console.put(System.currentTimeMillis(), message);
	}

	public String getCode() {
		if (ScriptController.Instance.lastLoaded > lastCreated) {
			lastCreated = ScriptController.Instance.lastLoaded;
			fullscript = script;
			if (!fullscript.isEmpty()) {
				fullscript += "\n";
			}
			for (String loc : scripts) {
				String code = ScriptController.Instance.scripts.get(loc);
				if ((code != null) && !code.isEmpty()) {
					fullscript = fullscript + code + "\n";
				}
			}
			unknownFunctions = new ArrayList<Integer>();
			init = false;
		}
		return fullscript;
	}

	public boolean hasCode() {
		return !getCode().isEmpty();
	}

	public void readFromNBT(NBTTagCompound compound) {
		script = compound.getString("Script");
		console = NBTTags.GetLongStringMap(compound.getTagList("Console", 10));
		scripts = NBTTags.getStringList(compound.getTagList("ScriptList", 10));
		lastCreated = 0L;
	}

	public void run(EnumScriptType type, Event event) {
		if (!hasCode() || unknownFunctions.contains(type.ordinal())) {
			return;
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		engine.getContext().setWriter(pw);
		engine.getContext().setErrorWriter(pw);
		try {
			if (!init) {
				engine.eval(getCode());
				init = true;
			}
			((Invocable) engine).invokeFunction(type.function, event);
		} catch (NoSuchMethodException e2) {
			unknownFunctions.add(type.ordinal());
		} catch (Exception e) {
			errored = true;
			e.printStackTrace(pw);
			NoppesUtilServer.NotifyOPs(handler.noticeString() + " script errored", new Object[0]);
		}
		appandConsole(sw.getBuffer().toString().trim());
		pw.close();
	}

	public void setEngine(String scriptLanguage) {
		if ((currentScriptLanguage != null) && currentScriptLanguage.equals(scriptLanguage)) {
			return;
		}
		engine = ScriptController.Instance.getEngineByName(scriptLanguage);
		if (engine == null) {
			errored = true;
			return;
		}
		engine.put("AnimationType", ScriptContainer.animations);
		engine.put("EntityType", ScriptContainer.entities);
		engine.put("RoleType", ScriptContainer.roles);
		engine.put("JobType", ScriptContainer.jobs);
		engine.put("TacticalVariantType", ScriptContainer.tacticalVariantTypes);
		engine.put("PotionEffectType", ScriptContainer.potionEffectTypes);
		engine.put("ParticleType", ScriptContainer.particleTypes);
		currentScriptLanguage = scriptLanguage;
		init = false;
	}

	public void writeToNBT(NBTTagCompound compound) {
		compound.setString("Script", script);
		compound.setTag("Console", NBTTags.NBTLongStringMap(console));
		compound.setTag("ScriptList", NBTTags.nbtStringList(scripts));
	}
}

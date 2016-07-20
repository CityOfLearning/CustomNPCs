//

//

package noppes.npcs.entity.data;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Objects;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.EventHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.script.IScriptHandler;
import noppes.npcs.controllers.script.ScriptContainer;
import noppes.npcs.controllers.script.ScriptController;
import noppes.npcs.entity.EntityNPCInterface;

public class DataScript implements IScriptHandler {
	private List<ScriptContainer> scripts;
	private String scriptLanguage;
	private EntityNPCInterface npc;
	private boolean enabled;
	public boolean hasInited;

	public DataScript(final EntityNPCInterface npc) {
		scripts = new ArrayList<ScriptContainer>();
		scriptLanguage = "ECMAScript";
		enabled = false;
		hasInited = false;
		this.npc = npc;
	}

	@Override
	public boolean getEnabled() {
		return enabled;
	}

	@Override
	public String getLanguage() {
		return scriptLanguage;
	}

	@Override
	public List<ScriptContainer> getScripts() {
		return scripts;
	}

	@Override
	public boolean isClient() {
		return npc.isRemote();
	}

	public boolean isEnabled() {
		return enabled && ScriptController.HasStart && !npc.worldObj.isRemote;
	}

	@Override
	public String noticeString() {
		final BlockPos pos = npc.getPosition();
		return Objects.toStringHelper(npc).add("x", pos.getX()).add("y", pos.getY()).add("z", pos.getZ()).toString();
	}

	public void readFromNBT(final NBTTagCompound compound) {
		scripts = NBTTags.GetScript(compound.getTagList("Scripts", 10), this);
		scriptLanguage = compound.getString("ScriptLanguage");
		enabled = compound.getBoolean("ScriptEnabled");
	}

	@Override
	public void runScript(final EnumScriptType type, final Event event) {
		if (!isEnabled()) {
			return;
		}
		if (!hasInited) {
			hasInited = true;
			EventHooks.onNPCInit(npc);
		}
		for (final ScriptContainer script : scripts) {
			if (!script.errored) {
				if (!script.hasCode()) {
					continue;
				}
				script.setEngine(scriptLanguage);
				if (script.engine == null) {
					continue;
				}
				script.run(type, event);
			}
		}
	}

	@Override
	public void setEnabled(final boolean bo) {
		enabled = bo;
	}

	@Override
	public void setLanguage(final String lang) {
		scriptLanguage = lang;
	}

	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		compound.setTag("Scripts", NBTTags.NBTScript(scripts));
		compound.setString("ScriptLanguage", scriptLanguage);
		compound.setBoolean("ScriptEnabled", enabled);
		return compound;
	}
}

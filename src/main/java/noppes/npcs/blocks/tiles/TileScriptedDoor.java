
package noppes.npcs.blocks.tiles;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Objects;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.EventHooks;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.wrapper.BlockScriptedDoorWrapper;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.script.IScriptBlockHandler;
import noppes.npcs.controllers.script.ScriptContainer;
import noppes.npcs.controllers.script.ScriptController;
import noppes.npcs.entity.data.DataTimers;
import noppes.npcs.util.NBTTags;

public class TileScriptedDoor extends TileDoor implements ITickable, IScriptBlockHandler {
	public List<ScriptContainer> scripts;
	public boolean shouldRefreshData;
	public String scriptLanguage;
	public boolean enabled;
	private IBlock blockDummy;
	public DataTimers timers;
	public boolean hasInited;
	private short ticksExisted;
	public int newPower;
	public int prevPower;

	public TileScriptedDoor() {
		scripts = new ArrayList<ScriptContainer>();
		shouldRefreshData = false;
		scriptLanguage = "ECMAScript";
		enabled = false;
		blockDummy = null;
		timers = new DataTimers(this);
		hasInited = false;
		ticksExisted = 0;
		newPower = 0;
		prevPower = 0;
	}

	@Override
	public IBlock getBlock() {
		if (blockDummy == null) {
			blockDummy = new BlockScriptedDoorWrapper(getWorld(), getBlockType(), getPos());
		}
		return blockDummy;
	}

	@Override
	public boolean getEnabled() {
		return enabled;
	}

	@Override
	public String getLanguage() {
		return scriptLanguage;
	}

	public NBTTagCompound getNBT(NBTTagCompound compound) {
		compound.setTag("Scripts", NBTTags.NBTScript(scripts));
		compound.setString("ScriptLanguage", scriptLanguage);
		compound.setBoolean("ScriptEnabled", enabled);
		compound.setInteger("BlockPrevPower", prevPower);
		return compound;
	}

	@Override
	public List<ScriptContainer> getScripts() {
		return scripts;
	}

	@Override
	public boolean isClient() {
		return getWorld().isRemote;
	}

	private boolean isEnabled() {
		return enabled && ScriptController.HasStart && !worldObj.isRemote;
	}

	@Override
	public String noticeString() {
		BlockPos pos = getPos();
		return Objects.toStringHelper(this).add("x", pos.getX()).add("y", pos.getY()).add("z", pos.getZ()).toString();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		setNBT(compound);
		timers.readFromNBT(compound);
	}

	@Override
	public void runScript(EnumScriptType type, Event event) {
		if (!isEnabled()) {
			return;
		}
		if (!hasInited) {
			hasInited = true;
			EventHooks.onScriptBlockInit(this);
		}
		for (ScriptContainer script : scripts) {
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
	public void setEnabled(boolean bo) {
		enabled = bo;
	}

	@Override
	public void setLanguage(String lang) {
		scriptLanguage = lang;
	}

	public void setNBT(NBTTagCompound compound) {
		scripts = NBTTags.GetScript(compound.getTagList("Scripts", 10), this);
		scriptLanguage = compound.getString("ScriptLanguage");
		enabled = compound.getBoolean("ScriptEnabled");
		prevPower = compound.getInteger("BlockPrevPower");
	}

	@Override
	public void update() {
		super.update();
		++ticksExisted;
		if (prevPower != newPower) {
			EventHooks.onScriptBlockRedstonePower(this, prevPower, newPower);
			prevPower = newPower;
		}
		timers.update();
		if (ticksExisted >= 10) {
			EventHooks.onScriptBlockUpdate(this);
			ticksExisted = 0;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		getNBT(compound);
		timers.writeToNBT(compound);
	}
}

//

//

package noppes.npcs.entity.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.EventHooks;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.ITimers;
import noppes.npcs.controllers.IScriptBlockHandler;
import noppes.npcs.entity.EntityNPCInterface;

public class DataTimers implements ITimers {
	class Timer {
		public int id;
		private boolean repeat;
		private int timerTicks;
		private int ticks;

		public Timer(final int id, final int ticks, final boolean repeat) {
			this.ticks = 0;
			this.id = id;
			this.repeat = repeat;
			timerTicks = ticks;
			this.ticks = ticks;
		}

		public void update() {
			if (ticks-- > 0) {
				return;
			}
			if (repeat) {
				ticks = timerTicks;
			} else {
				stop(id);
			}
			final Object ob = parent;
			if (ob instanceof EntityNPCInterface) {
				EventHooks.onNPCTimer((EntityNPCInterface) ob, id);
			} else {
				EventHooks.onScriptBlockTimer((IScriptBlockHandler) ob, id);
			}
		}
	}

	private Object parent;

	private Map<Integer, Timer> timers;

	public DataTimers(final Object parent) {
		timers = new HashMap<Integer, Timer>();
		this.parent = parent;
	}

	@Override
	public boolean has(final int id) {
		return timers.containsKey(id);
	}

	public void readFromNBT(final NBTTagCompound compound) {
		final Map<Integer, Timer> timers = new HashMap<Integer, Timer>();
		final NBTTagList list = compound.getTagList("NpcsTimers", 10);
		for (int i = 0; i < list.tagCount(); ++i) {
			final NBTTagCompound c = list.getCompoundTagAt(i);
			final Timer t = new Timer(c.getInteger("ID"), c.getInteger("TimerTicks"), c.getBoolean("Repeat"));
			t.ticks = c.getInteger("Ticks");
			timers.put(t.id, t);
		}
		this.timers = timers;
	}

	@Override
	public void start(final int id, final int ticks, final boolean repeat) {
		if (timers.containsKey(id)) {
			throw new CustomNPCsException("There is already a timer with id: " + id, new Object[0]);
		}
		timers.put(id, new Timer(id, ticks, repeat));
	}

	@Override
	public boolean stop(final int id) {
		return timers.remove(id) != null;
	}

	public void update() {
		for (final Timer timer : new ArrayList<Timer>(timers.values())) {
			timer.update();
		}
	}

	public void writeToNBT(final NBTTagCompound compound) {
		final NBTTagList list = new NBTTagList();
		for (final Timer timer : timers.values()) {
			final NBTTagCompound c = new NBTTagCompound();
			c.setInteger("ID", timer.id);
			c.setInteger("TimerTicks", timer.id);
			c.setBoolean("Repeat", timer.repeat);
			c.setInteger("Ticks", timer.id);
			list.appendTag(c);
		}
		compound.setTag("NpcsTimers", list);
	}
}

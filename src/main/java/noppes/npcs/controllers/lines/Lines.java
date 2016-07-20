//

//

package noppes.npcs.controllers.lines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class Lines {
	private static final Random random;
	static {
		random = new Random();
	}
	private int lastLine;

	public HashMap<Integer, Line> lines;

	public Lines() {
		lastLine = -1;
		lines = new HashMap<Integer, Line>();
	}

	public Line getLine(final boolean isRandom) {
		if (lines.isEmpty()) {
			return null;
		}
		if (isRandom) {
			final List<Line> lines = new ArrayList<Line>(this.lines.values());
			Line line;
			while (true) { //dont get random empty lines...
				line = lines.get(Lines.random.nextInt(lines.size()));
				if (line != null) {
					break;
				}
			}
			return line;
		}
		++lastLine;
		Line line;
		while (true) {
			lastLine %= 7;
			line = lines.get(lastLine);
			if (line != null) {
				break;
			}
			++lastLine;
		}
		return line;
	}

	public boolean isEmpty() {
		return lines.isEmpty();
	}

	public void readNBT(final NBTTagCompound compound) {
		final NBTTagList nbttaglist = compound.getTagList("Lines", 10);
		final HashMap<Integer, Line> map = new HashMap<Integer, Line>();
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
			final Line line = new Line();
			line.text = nbttagcompound.getString("Line");
			line.sound = nbttagcompound.getString("Song");
			map.put(nbttagcompound.getInteger("Slot"), line);
		}
		lines = map;
	}

	public NBTTagCompound writeToNBT() {
		final NBTTagCompound compound = new NBTTagCompound();
		final NBTTagList nbttaglist = new NBTTagList();
		for (final int slot : lines.keySet()) {
			final Line line = lines.get(slot);
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Slot", slot);
			nbttagcompound.setString("Line", line.text);
			nbttagcompound.setString("Song", line.sound);
			nbttaglist.appendTag(nbttagcompound);
		}
		compound.setTag("Lines", nbttaglist);
		return compound;
	}
}

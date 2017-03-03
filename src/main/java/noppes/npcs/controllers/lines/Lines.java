
package noppes.npcs.controllers.lines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class Lines {
	private static Random random;
	static {
		random = new Random();
	}
	private int lastLine;

	public HashMap<Integer, Line> lines;

	public Lines() {
		lastLine = -1;
		lines = new HashMap<>();
	}

	public Line getLine(boolean isRandom) {
		if (lines.isEmpty()) {
			return null;
		}
		if (isRandom) {
			List<Line> lines = new ArrayList<>(this.lines.values());
			Line line;
			while (true) { // dont get random empty lines...
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

	public void readNBT(NBTTagCompound compound) {
		NBTTagList nbttaglist = compound.getTagList("Lines", 10);
		HashMap<Integer, Line> map = new HashMap<>();
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
			Line line = new Line();
			line.text = nbttagcompound.getString("Line");
			line.sound = nbttagcompound.getString("Song");
			map.put(nbttagcompound.getInteger("Slot"), line);
		}
		lines = map;
	}

	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagList nbttaglist = new NBTTagList();
		for (int slot : lines.keySet()) {
			Line line = lines.get(slot);
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Slot", slot);
			nbttagcompound.setString("Line", line.text);
			nbttagcompound.setString("Song", line.sound);
			nbttaglist.appendTag(nbttagcompound);
		}
		compound.setTag("Lines", nbttaglist);
		return compound;
	}
}

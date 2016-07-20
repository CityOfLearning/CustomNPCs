//

//

package noppes.npcs.api.block;

import noppes.npcs.api.ITimers;

public interface IBlockScriptedDoor extends IBlock {
	String getBlockModel();

	boolean getOpen();

	ITimers getTimers();

	void setBlockModel(String p0);

	void setOpen(boolean p0);
}

//

//

package noppes.npcs.api.block;

import noppes.npcs.api.ITimers;

public interface IBlockScriptedDoor extends IBlock {
	String getBlockModel();

	boolean getOpen();

	ITimers getTimers();

	void setBlockModel(final String p0);

	void setOpen(final boolean p0);
}

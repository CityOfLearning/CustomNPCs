//

//

package noppes.npcs.api.block;

import noppes.npcs.api.IItemStack;
import noppes.npcs.api.ITimers;

public interface IBlockScripted extends IBlock {
	void executeCommand(final String p0);

	boolean getIsLadder();

	int getLight();

	IItemStack getModel();

	int getRedstonePower();

	int getRotationX();

	int getRotationY();

	int getRotationZ();

	float getScaleX();

	float getScaleY();

	float getScaleZ();

	ITimers getTimers();

	void setIsLadder(final boolean p0);

	void setLight(final int p0);

	void setModel(final IItemStack p0);

	void setModel(final String p0);

	void setRedstonePower(final int p0);

	void setRotation(final int p0, final int p1, final int p2);

	void setScale(final float p0, final float p1, final float p2);
}

//

//

package noppes.npcs.api.block;

import noppes.npcs.api.IItemStack;
import noppes.npcs.api.ITimers;

public interface IBlockScripted extends IBlock {
	void executeCommand(String p0);

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

	void setIsLadder(boolean p0);

	void setLight(int p0);

	void setModel(IItemStack p0);

	void setModel(String p0);

	void setRedstonePower(int p0);

	void setRotation(int p0, int p1, int p2);

	void setScale(float p0, float p1, float p2);
}

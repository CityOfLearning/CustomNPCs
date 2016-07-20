//

//

package noppes.npcs.api.entity;

import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.IItemStack;

public interface IPlayer<T extends EntityPlayerMP> extends IEntityLivingBase<T> {
	void addFactionPoints(int p0, int p1);

	void finishQuest(int p0);

	String getDisplayName();

	int getExpLevel();

	int getFactionPoints(int p0);

	int getGamemode();

	IItemStack[] getInventory();

	@Override
	T getMCEntity();

	String getName();

	boolean giveItem(IItemStack p0);

	boolean giveItem(String p0, int p1, int p2);

	boolean hasAchievement(String p0);

	boolean hasActiveQuest(int p0);

	boolean hasFinishedQuest(int p0);

	boolean hasPermission(String p0);

	boolean hasReadDialog(int p0);

	int inventoryItemCount(IItemStack p0);

	void message(String p0);

	void removeAllItems(IItemStack p0);

	boolean removeItem(IItemStack p0, int p1);

	boolean removeItem(String p0, int p1, int p2);

	void removeQuest(int p0);

	void resetSpawnpoint();

	void setExpLevel(int p0);

	void setGamemode(int p0);

	void setSpawnpoint(int p0, int p1, int p2);

	void showDialog(int p0, String p1);

	void startQuest(int p0);

	void stopQuest(int p0);
}

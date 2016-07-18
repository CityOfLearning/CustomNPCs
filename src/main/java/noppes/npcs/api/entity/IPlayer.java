//

//

package noppes.npcs.api.entity;

import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.IItemStack;

public interface IPlayer<T extends EntityPlayerMP> extends IEntityLivingBase<T> {
	void addFactionPoints(final int p0, final int p1);

	void finishQuest(final int p0);

	String getDisplayName();

	int getExpLevel();

	int getFactionPoints(final int p0);

	int getGamemode();

	IItemStack[] getInventory();

	@Override
	T getMCEntity();

	String getName();

	boolean giveItem(final IItemStack p0);

	boolean giveItem(final String p0, final int p1, final int p2);

	boolean hasAchievement(final String p0);

	boolean hasActiveQuest(final int p0);

	boolean hasFinishedQuest(final int p0);

	boolean hasPermission(final String p0);

	boolean hasReadDialog(final int p0);

	int inventoryItemCount(final IItemStack p0);

	void message(final String p0);

	void removeAllItems(final IItemStack p0);

	boolean removeItem(final IItemStack p0, final int p1);

	boolean removeItem(final String p0, final int p1, final int p2);

	void removeQuest(final int p0);

	void resetSpawnpoint();

	void setExpLevel(final int p0);

	void setGamemode(final int p0);

	void setSpawnpoint(final int p0, final int p1, final int p2);

	void showDialog(final int p0, final String p1);

	void startQuest(final int p0);

	void stopQuest(final int p0);
}

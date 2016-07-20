//

//

package noppes.npcs.api.entity;

import net.minecraft.entity.EntityCreature;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.ITimers;
import noppes.npcs.api.entity.data.INPCAi;
import noppes.npcs.api.entity.data.INPCDisplay;
import noppes.npcs.api.entity.data.INPCInventory;
import noppes.npcs.api.entity.data.INPCJob;
import noppes.npcs.api.entity.data.INPCRole;
import noppes.npcs.api.entity.data.INPCStats;
import noppes.npcs.api.handler.data.IFaction;

public interface ICustomNpc<T extends EntityCreature> extends IEntityLivingBase<T> {
	void executeCommand(String p0);

	INPCAi getAi();

	INPCDisplay getDisplay();

	IFaction getFaction();

	int getHomeX();

	int getHomeY();

	int getHomeZ();

	INPCInventory getInventory();

	INPCJob getJob();

	@Override
	T getMCEntity();

	INPCRole getRole();

	INPCStats getStats();

	ITimers getTimers();

	void giveItem(IPlayer p0, IItemStack p1);

	void kill();

	void reset();

	void say(String p0);

	void setFaction(int p0);

	void setHome(int p0, int p1, int p2);

	void shootItem(IEntityLivingBase p0, IItemStack p1, int p2);
}

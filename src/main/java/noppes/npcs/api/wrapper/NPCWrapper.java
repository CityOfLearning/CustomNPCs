//

//

package noppes.npcs.api.wrapper;

import net.minecraft.util.BlockPos;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.ITimers;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IEntityLivingBase;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.entity.data.INPCAi;
import noppes.npcs.api.entity.data.INPCDisplay;
import noppes.npcs.api.entity.data.INPCInventory;
import noppes.npcs.api.entity.data.INPCJob;
import noppes.npcs.api.entity.data.INPCRole;
import noppes.npcs.api.entity.data.INPCStats;
import noppes.npcs.api.handler.data.IFaction;
import noppes.npcs.controllers.faction.Faction;
import noppes.npcs.controllers.faction.FactionController;
import noppes.npcs.controllers.lines.Line;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.ValueUtil;

public class NPCWrapper<T extends EntityNPCInterface> extends EntityLivingWrapper<T> implements ICustomNpc {
	public NPCWrapper(final T npc) {
		super(npc);
	}

	@Override
	public void executeCommand(final String command) {
		NoppesUtilServer.runCommand(entity, entity.getName(), command, null);
	}

	@Override
	public long getAge() {
		return entity.totalTicksAlive;
	}

	@Override
	public INPCAi getAi() {
		return entity.ai;
	}

	@Override
	public INPCDisplay getDisplay() {
		return entity.display;
	}

	@Override
	public IFaction getFaction() {
		return entity.faction;
	}

	@Override
	public int getHomeX() {
		return entity.ai.startPos().getX();
	}

	@Override
	public int getHomeY() {
		return entity.ai.startPos().getY();
	}

	@Override
	public int getHomeZ() {
		return entity.ai.startPos().getZ();
	}

	@Override
	public INPCInventory getInventory() {
		return entity.inventory;
	}

	@Override
	public INPCJob getJob() {
		return entity.jobInterface;
	}

	public int getOffsetX() {
		return (int) entity.ai.bodyOffsetX;
	}

	public int getOffsetY() {
		return (int) entity.ai.bodyOffsetY;
	}

	public int getOffsetZ() {
		return (int) entity.ai.bodyOffsetZ;
	}

	@Override
	public INPCRole getRole() {
		return entity.roleInterface;
	}

	@Override
	public INPCStats getStats() {
		return entity.stats;
	}

	@Override
	public ITimers getTimers() {
		return entity.timers;
	}

	@Override
	public int getType() {
		return 2;
	}

	@Override
	public void giveItem(final IPlayer player, final IItemStack item) {
		entity.givePlayerItem(player.getMCEntity(), item.getMCItemStack());
	}

	@Override
	public void kill() {
		entity.setDead();
	}

	@Override
	public void reset() {
		entity.reset();
	}

	@Override
	public void say(final String message) {
		entity.saySurrounding(new Line(message));
	}

	@Override
	public void setFaction(final int id) {
		final Faction faction = FactionController.getInstance().getFaction(id);
		if (faction == null) {
			throw new CustomNPCsException("Unknown faction id: " + id, new Object[0]);
		}
		entity.setFaction(id);
	}

	@Override
	public void setHome(final int x, final int y, final int z) {
		entity.ai.setStartPos(new BlockPos(x, y, z));
	}

	@Override
	public void setMaxHealth(final float health) {
		super.setMaxHealth(health);
		entity.stats.maxHealth = (int) health;
	}

	public void setOffset(final int x, final int y, final int z) {
		entity.ai.bodyOffsetX = ValueUtil.correctFloat(x, 0.0f, 9.0f);
		entity.ai.bodyOffsetY = ValueUtil.correctFloat(y, 0.0f, 9.0f);
		entity.ai.bodyOffsetZ = ValueUtil.correctFloat(z, 0.0f, 9.0f);
	}

	@Override
	public void shootItem(final IEntityLivingBase target, final IItemStack item, int accuracy) {
		if (item == null) {
			throw new CustomNPCsException("No item was given", new Object[0]);
		}
		if (target == null) {
			throw new CustomNPCsException("No target was given", new Object[0]);
		}
		accuracy = ValueUtil.CorrectInt(accuracy, 1, 100);
		entity.shoot(target.getMCEntity(), accuracy, item.getMCItemStack(), false);
	}
	
	@Override
	public boolean typeOf(final int type) {
		return (type == 2) || super.typeOf(type);
	}
}

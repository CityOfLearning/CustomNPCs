//

//

package noppes.npcs.blocks.tiles;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ITickable;
import net.minecraft.util.StatCollector;
import noppes.npcs.api.constants.EnumQuestType;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.PlayerQuestData;
import noppes.npcs.controllers.QuestData;
import noppes.npcs.quests.QuestLocation;

public class TileWaypoint extends TileNpcEntity implements ITickable {
	public String name;
	private int ticks;
	private List<EntityPlayer> recentlyChecked;
	private List<EntityPlayer> toCheck;
	public int range;

	public TileWaypoint() {
		name = "";
		ticks = 10;
		recentlyChecked = new ArrayList<EntityPlayer>();
		range = 10;
	}

	private List<EntityPlayer> getPlayerList(final int x, final int y, final int z) {
		return worldObj.getEntitiesWithinAABB((Class) EntityPlayer.class,
				new AxisAlignedBB(pos, pos.add(1, 1, 1)).expand(x, y, z));
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		super.readFromNBT(compound);
		name = compound.getString("LocationName");
		range = compound.getInteger("LocationRange");
		if (range < 2) {
			range = 2;
		}
	}

	@Override
	public void update() {
		if (worldObj.isRemote || name.isEmpty()) {
			return;
		}
		--ticks;
		if (ticks > 0) {
			return;
		}
		ticks = 10;
		(toCheck = getPlayerList(range, range, range)).removeAll(recentlyChecked);
		final List<EntityPlayer> listMax = getPlayerList(range + 10, range + 10, range + 10);
		recentlyChecked.retainAll(listMax);
		recentlyChecked.addAll(toCheck);
		if (toCheck.isEmpty()) {
			return;
		}
		for (final EntityPlayer player : toCheck) {
			final PlayerQuestData playerdata = PlayerDataController.instance.getPlayerData(player).questData;
			for (final QuestData data : playerdata.activeQuests.values()) {
				if (data.quest.type != EnumQuestType.LOCATION) {
					continue;
				}
				final QuestLocation quest = (QuestLocation) data.quest.questInterface;
				if (!quest.setFound(data, name)) {
					continue;
				}
				player.addChatMessage(new ChatComponentTranslation(
						name + " " + StatCollector.translateToLocal("quest.found"), new Object[0]));
				playerdata.checkQuestCompletion(player, EnumQuestType.LOCATION);
			}
		}
	}

	@Override
	public void writeToNBT(final NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (!name.isEmpty()) {
			compound.setString("LocationName", name);
		}
		compound.setInteger("LocationRange", range);
	}
}

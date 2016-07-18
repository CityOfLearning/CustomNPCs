//

//

package noppes.npcs.controllers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;

public class FactionOptions {
	public boolean decreaseFactionPoints;
	public boolean decreaseFaction2Points;
	public int factionId;
	public int faction2Id;
	public int factionPoints;
	public int faction2Points;

	public FactionOptions() {
		decreaseFactionPoints = false;
		decreaseFaction2Points = false;
		factionId = -1;
		faction2Id = -1;
		factionPoints = 100;
		faction2Points = 100;
	}

	public void addPoints(final EntityPlayer player) {
		if ((factionId < 0) && (faction2Id < 0)) {
			return;
		}
		final PlayerFactionData data = PlayerDataController.instance.getPlayerData(player).factionData;
		if ((factionId >= 0) && (factionPoints > 0)) {
			this.addPoints(player, data, factionId, decreaseFactionPoints, factionPoints);
		}
		if ((faction2Id >= 0) && (faction2Points > 0)) {
			this.addPoints(player, data, faction2Id, decreaseFaction2Points, faction2Points);
		}
	}

	private void addPoints(final EntityPlayer player, final PlayerFactionData data, final int factionId,
			final boolean decrease, final int points) {
		final Faction faction = FactionController.getInstance().getFaction(factionId);
		if (faction == null) {
			return;
		}
		if (!faction.hideFaction) {
			final String message = decrease ? "faction.decreasepoints" : "faction.increasepoints";
			player.addChatMessage(new ChatComponentTranslation(message, new Object[] { faction.name, points }));
		}
		data.increasePoints(factionId, decrease ? (-points) : points);
	}

	public boolean hasFaction(final int id) {
		return (factionId == id) || (faction2Id == id);
	}

	public void readFromNBT(final NBTTagCompound compound) {
		factionId = compound.getInteger("OptionFactions1");
		faction2Id = compound.getInteger("OptionFactions2");
		decreaseFactionPoints = compound.getBoolean("DecreaseFaction1Points");
		decreaseFaction2Points = compound.getBoolean("DecreaseFaction2Points");
		factionPoints = compound.getInteger("OptionFaction1Points");
		faction2Points = compound.getInteger("OptionFaction2Points");
	}

	public NBTTagCompound writeToNBT(final NBTTagCompound par1NBTTagCompound) {
		par1NBTTagCompound.setInteger("OptionFactions1", factionId);
		par1NBTTagCompound.setInteger("OptionFactions2", faction2Id);
		par1NBTTagCompound.setInteger("OptionFaction1Points", factionPoints);
		par1NBTTagCompound.setInteger("OptionFaction2Points", faction2Points);
		par1NBTTagCompound.setBoolean("DecreaseFaction1Points", decreaseFactionPoints);
		par1NBTTagCompound.setBoolean("DecreaseFaction2Points", decreaseFaction2Points);
		return par1NBTTagCompound;
	}
}

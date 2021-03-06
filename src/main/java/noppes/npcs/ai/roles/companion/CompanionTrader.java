
package noppes.npcs.ai.roles.companion;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.util.NoppesUtilServer;

public class CompanionTrader extends CompanionJobInterface {
	@Override
	public NBTTagCompound getNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		return compound;
	}

	public void interact(EntityPlayer player) {
		NoppesUtilServer.sendOpenGui(player, EnumGuiType.CompanionTrader, npc);
	}

	@Override
	public void setNBT(NBTTagCompound compound) {
	}
}

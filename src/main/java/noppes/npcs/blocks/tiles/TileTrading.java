//

//

package noppes.npcs.blocks.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.Server;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.containers.ContainerTradingBlock;

public class TileTrading extends TileColorable {
	public EntityPlayer trader1;
	public EntityPlayer trader2;

	public TileTrading() {
		trader1 = null;
		trader2 = null;
	}

	public void addTrader(EntityPlayer player) {
		if (isFull()) {
			return;
		}
		if ((trader1 != player) && (trader2 != player)) {
			if (trader1 == null) {
				trader1 = player;
			} else if ((trader2 == null) || (trader2 == player)) {
				trader2 = player;
			}
		}
		if (isFull()) {
			NBTTagCompound data = new NBTTagCompound();
			data.setString("Player", trader1.getUniqueID().toString());
			Server.sendDataDelayed((EntityPlayerMP) trader2, EnumPacketClient.GUI_DATA, 100, data);
			data = new NBTTagCompound();
			data.setString("Player", trader2.getUniqueID().toString());
			Server.sendDataDelayed((EntityPlayerMP) trader1, EnumPacketClient.GUI_DATA, 100, data);
		}
	}

	public boolean isFull() {
		if (trader1 != null) {
			if (!(trader1.openContainer instanceof ContainerTradingBlock)) {
				trader1 = null;
			} else if (((ContainerTradingBlock) trader1.openContainer).state >= 3) {
				return true;
			}
		}
		if (trader2 != null) {
			if (!(trader2.openContainer instanceof ContainerTradingBlock)) {
				trader2 = null;
			} else if (((ContainerTradingBlock) trader2.openContainer).state >= 3) {
				return true;
			}
		}
		return (trader1 != null) && (trader2 != null);
	}

	public EntityPlayer other(EntityPlayer player) {
		if (player == trader1) {
			return trader2;
		}
		return trader1;
	}
}

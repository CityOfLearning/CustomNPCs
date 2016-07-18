//

//

package noppes.npcs.client.gui;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import noppes.npcs.blocks.tiles.TileScriptedDoor;
import noppes.npcs.client.Client;
import noppes.npcs.constants.EnumPacketServer;

public class GuiScriptDoor extends GuiScriptInterface {
	private TileScriptedDoor script;

	public GuiScriptDoor(final int x, final int y, final int z) {
		final TileScriptedDoor tileScriptedDoor = (TileScriptedDoor) player.worldObj
				.getTileEntity(new BlockPos(x, y, z));
		script = tileScriptedDoor;
		handler = tileScriptedDoor;
		Client.sendData(EnumPacketServer.ScriptDoorDataGet, x, y, z);
	}

	@Override
	public void save() {
		super.save();
		final BlockPos pos = script.getPos();
		Client.sendData(EnumPacketServer.ScriptDoorDataSave, pos.getX(), pos.getY(), pos.getZ(),
				script.getNBT(new NBTTagCompound()));
	}

	@Override
	public void setGuiData(final NBTTagCompound compound) {
		script.setNBT(compound);
		super.setGuiData(compound);
	}
}

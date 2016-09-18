
package noppes.npcs.client.gui;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.data.DataScript;

public class GuiScript extends GuiScriptInterface {
	private DataScript script;

	public GuiScript(EntityNPCInterface npc) {
		DataScript script = npc.script;
		this.script = script;
		handler = script;
		Client.sendData(EnumPacketServer.ScriptDataGet, new Object[0]);
	}

	@Override
	public void save() {
		super.save();
		Client.sendData(EnumPacketServer.ScriptDataSave, script.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void setGuiData(NBTTagCompound compound) {
		script.readFromNBT(compound);
		super.setGuiData(compound);
	}
}

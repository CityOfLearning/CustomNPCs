package noppes.npcs.client.gui;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.GuiScriptInterface;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.data.DataScript;

public class GuiScript extends GuiScriptInterface {

   private DataScript script;


   public GuiScript(EntityNPCInterface npc) {
      this.handler = this.script = npc.script;
      Client.sendData(EnumPacketServer.ScriptDataGet, new Object[0]);
   }

   public void setGuiData(NBTTagCompound compound) {
      this.script.readFromNBT(compound);
      super.setGuiData(compound);
   }

   public void save() {
      super.save();
      Client.sendData(EnumPacketServer.ScriptDataSave, new Object[]{this.script.writeToNBT(new NBTTagCompound())});
   }
}

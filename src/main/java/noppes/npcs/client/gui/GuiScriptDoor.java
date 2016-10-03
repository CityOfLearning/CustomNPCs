package noppes.npcs.client.gui;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import noppes.npcs.blocks.tiles.TileScriptedDoor;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.GuiScriptInterface;
import noppes.npcs.constants.EnumPacketServer;

public class GuiScriptDoor extends GuiScriptInterface {

   private TileScriptedDoor script;


   public GuiScriptDoor(int x, int y, int z) {
      this.handler = this.script = (TileScriptedDoor)this.player.worldObj.getTileEntity(new BlockPos(x, y, z));
      Client.sendData(EnumPacketServer.ScriptDoorDataGet, new Object[]{Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z)});
   }

   public void setGuiData(NBTTagCompound compound) {
      this.script.setNBT(compound);
      super.setGuiData(compound);
   }

   public void save() {
      super.save();
      BlockPos pos = this.script.getPos();
      Client.sendData(EnumPacketServer.ScriptDoorDataSave, new Object[]{Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()), this.script.getNBT(new NBTTagCompound())});
   }
}

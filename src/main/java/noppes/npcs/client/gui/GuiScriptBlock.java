package noppes.npcs.client.gui;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.GuiScriptInterface;
import noppes.npcs.constants.EnumPacketServer;

public class GuiScriptBlock extends GuiScriptInterface {

   private TileScripted script;


   public GuiScriptBlock(int x, int y, int z) {
      this.handler = this.script = (TileScripted)this.player.worldObj.getTileEntity(new BlockPos(x, y, z));
      Client.sendData(EnumPacketServer.ScriptBlockDataGet, new Object[]{Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z)});
   }

   public void setGuiData(NBTTagCompound compound) {
      this.script.setNBT(compound);
      super.setGuiData(compound);
   }

   public void save() {
      super.save();
      BlockPos pos = this.script.getPos();
      Client.sendData(EnumPacketServer.ScriptBlockDataSave, new Object[]{Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()), this.script.getNBT(new NBTTagCompound())});
   }
}

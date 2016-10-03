package noppes.npcs.client.gui.player;

import net.minecraft.util.BlockPos;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.blocks.tiles.TileBigSign;
import noppes.npcs.client.gui.SubGuiNpcTextArea;
import noppes.npcs.constants.EnumPlayerPacket;

public class GuiBigSign extends SubGuiNpcTextArea {

   public TileBigSign tile;


   public GuiBigSign(int x, int y, int z) {
      super("");
      this.tile = (TileBigSign)this.player.worldObj.getTileEntity(new BlockPos(x, y, z));
      this.text = this.tile.getText();
   }

   public void close() {
      super.close();
      NoppesUtilPlayer.sendData(EnumPlayerPacket.SignSave, new Object[]{Integer.valueOf(this.tile.getPos().getX()), Integer.valueOf(this.tile.getPos().getY()), Integer.valueOf(this.tile.getPos().getZ()), this.text});
   }
}

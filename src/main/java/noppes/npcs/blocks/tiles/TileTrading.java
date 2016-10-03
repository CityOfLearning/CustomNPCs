package noppes.npcs.blocks.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.Server;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.containers.ContainerTradingBlock;

public class TileTrading extends TileColorable {

   public EntityPlayer trader1 = null;
   public EntityPlayer trader2 = null;


   public boolean isFull() {
      if(this.trader1 != null) {
         if(!(this.trader1.openContainer instanceof ContainerTradingBlock)) {
            this.trader1 = null;
         } else if(((ContainerTradingBlock)this.trader1.openContainer).state >= 3) {
            return true;
         }
      }

      if(this.trader2 != null) {
         if(!(this.trader2.openContainer instanceof ContainerTradingBlock)) {
            this.trader2 = null;
         } else if(((ContainerTradingBlock)this.trader2.openContainer).state >= 3) {
            return true;
         }
      }

      return this.trader1 != null && this.trader2 != null;
   }

   public void addTrader(EntityPlayer player) {
      if(!this.isFull()) {
         if(this.trader1 != player && this.trader2 != player) {
            if(this.trader1 == null) {
               this.trader1 = player;
            } else if(this.trader2 == null || this.trader2 == player) {
               this.trader2 = player;
            }
         }

         if(this.isFull()) {
            NBTTagCompound data = new NBTTagCompound();
            data.setString("Player", this.trader1.getUniqueID().toString());
            Server.sendDataDelayed((EntityPlayerMP)this.trader2, EnumPacketClient.GUI_DATA, 100, new Object[]{data});
            data = new NBTTagCompound();
            data.setString("Player", this.trader2.getUniqueID().toString());
            Server.sendDataDelayed((EntityPlayerMP)this.trader1, EnumPacketClient.GUI_DATA, 100, new Object[]{data});
         }

      }
   }

   public EntityPlayer other(EntityPlayer player) {
      return player == this.trader1?this.trader2:this.trader1;
   }
}

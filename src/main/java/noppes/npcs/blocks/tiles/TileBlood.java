package noppes.npcs.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import noppes.npcs.blocks.tiles.TileNpcEntity;

public class TileBlood extends TileNpcEntity {

   public boolean hideTop = false;
   public boolean hideBottom = false;
   public boolean hideNorth = false;
   public boolean hideSouth = false;
   public boolean hideEast = false;
   public boolean hideWest = false;
   public int rotation = 0;


   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      this.hideTop = compound.getBoolean("HideTop");
      this.hideBottom = compound.getBoolean("HideBottom");
      this.hideNorth = compound.getBoolean("HideNorth");
      this.hideSouth = compound.getBoolean("HideSouth");
      this.hideEast = compound.getBoolean("HideEast");
      this.hideWest = compound.getBoolean("HideWest");
      this.rotation = compound.getInteger("Rotation");
   }

   public void writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      compound.setBoolean("HideTop", this.hideTop);
      compound.setBoolean("HideBottom", this.hideBottom);
      compound.setBoolean("HideNorth", this.hideNorth);
      compound.setBoolean("HideSouth", this.hideSouth);
      compound.setBoolean("HideEast", this.hideEast);
      compound.setBoolean("HideWest", this.hideWest);
      compound.setInteger("Rotation", this.rotation);
   }

   public Packet getDescriptionPacket() {
      NBTTagCompound compound = new NBTTagCompound();
      this.writeToNBT(compound);
      compound.removeTag("ExtraData");
      S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(this.pos, 0, compound);
      return packet;
   }

   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      NBTTagCompound compound = pkt.getNbtCompound();
      this.readFromNBT(compound);
   }
}

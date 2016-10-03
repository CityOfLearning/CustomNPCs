package noppes.npcs.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import noppes.npcs.TextBlock;
import noppes.npcs.blocks.tiles.TileNpcEntity;

public class TileBigSign extends TileNpcEntity {

   public int rotation;
   public boolean canEdit = true;
   public boolean hasChanged = true;
   private String signText = "";
   public TextBlock block;


   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      this.rotation = compound.getInteger("SignRotation");
      this.setText(compound.getString("SignText"));
   }

   public void writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      compound.setInteger("SignRotation", this.rotation);
      compound.setString("SignText", this.signText);
   }

   public void setText(String text) {
      this.signText = text;
      this.hasChanged = true;
   }

   public String getText() {
      return this.signText;
   }

   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      NBTTagCompound compound = pkt.getNbtCompound();
      this.readFromNBT(compound);
   }

   public Packet getDescriptionPacket() {
      NBTTagCompound compound = new NBTTagCompound();
      this.writeToNBT(compound);
      compound.removeTag("ExtraData");
      S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(this.pos, 0, compound);
      return packet;
   }
}
